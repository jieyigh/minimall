package com.jbh360.trade.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jbh360.api.sdk.ApiInnerClient;
import com.jbh360.api.sdk.member.app.request.MinimallMemberWalletAddRequest;
import com.jbh360.api.sdk.member.app.response.MinimallMemberWalletAddResponse;
import com.jbh360.api.sdk.utils.StringUtils;
import com.jbh360.common.base.ErrorResult;
import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.Constants;
import com.jbh360.common.utils.Constants.WalletType;
import com.jbh360.common.utils.DateUtil;
import com.jbh360.common.utils.SignVerifyUtil;
import com.jbh360.common.utils.TransType;
import com.jbh360.goods.entity.Product;
import com.jbh360.goods.entity.ProductSku;
import com.jbh360.goods.mapper.ProductMapper;
import com.jbh360.goods.mapper.ProductSkuMapper;
import com.jbh360.member.oms.entity.Member;
import com.jbh360.member.oms.entity.MemberWeixinTicket;
import com.jbh360.member.oms.entity.MemberWeixinTicketExample;
import com.jbh360.member.oms.mapper.MemberMapper;
import com.jbh360.member.oms.mapper.MemberWeixinTicketMapper;
import com.jbh360.trade.dao.OrderDao;
import com.jbh360.trade.dao.OrderPayFlowDao;
import com.jbh360.trade.dao.TradeDao;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.entity.OrderDetail;
import com.jbh360.trade.entity.OrderDetailExample;
import com.jbh360.trade.entity.OrderPayFlow;
import com.jbh360.trade.entity.OrderPayFlowExample;
import com.jbh360.trade.entity.Trade;
import com.jbh360.trade.entity.TradeOrderRecord;
import com.jbh360.trade.mapper.OrderDetailMapper;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.mapper.OrderPayFlowMapper;
import com.jbh360.trade.mapper.TradeMapper;
import com.jbh360.trade.mapper.TradeOrderRecordMapper;
import com.jbh360.trade.vo.param.OrderPayParams;
import com.jbh360.trade.vo.rs.OrderPayFlowVo;
import com.jbh360.trade.vo.rs.OrderPayResult;
import com.jbh360.trade.vo.rs.OrderVo;
import com.jbh360.trade.vo.rs.PayResult;
import com.jbh360.trade.vo.rs.TradeVo;
import com.yooyo.util.RestUtil;


/**
 * 
 * @Title: OrderPayServiceImpl.java
 * @Package com.jbh360.trade.service
 * @ClassName: OrderPayServiceImpl
 * @Description: 支付单
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月31日 下午3:44:54
 * @version V3.0
 */
@Service
public class OrderPayServiceImpl {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private TradeMapper tradeMapper;
	
	@Autowired
	private  MemberWeixinTicketMapper memberWeixinTicketMapper;
	
	
	@Autowired
	private OrderPayFlowMapper orderPayFlowMapper;
	
	
	@Autowired
	protected OrderServiceServiceImpl orderServiceServiceImpl;
	
	
	@Autowired
    private MemberMapper memberMapper;
	
	
	@Autowired
	private TradeOrderRecordMapper tradeOrderRecordMapper;
	
	@Autowired
	private TradeDao tradeDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderPayFlowDao orderPayFlowDao;
	
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private ProductMapper productMapper;
	
	@Resource(name = "siteConfig")
	private Properties siteConfig;
	
	
	@Resource
	private ApiInnerClient apiInnerClient;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private TradeBillService tradeBillService;
	
	/**
	 * 
	 * @Title: checkPayResult
	 * @Description: 验证支付返回参数
	 * @date 2015年10月9日 下午3:19:49
	 * @author 揭懿
	 * @param param
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public boolean checkPayResult(OrderPayResult param, HttpServletRequest request) throws IOException{
		
		logger.info("处理支付返回参数");
		
		Map<String, String> params = SignVerifyUtil.getParams(request.getParameterMap(), request.getQueryString());
		
		logger.info("支付返回参数：" + params);
		
		boolean verify = SignVerifyUtil.verifySign(params, Constants.SIGN_KEY);
		
		if(verify && "success".equals(params.get("status"))) {
			logger.info("支付验证成功");
			return true;
		}
		logger.info("支付验证失败");
		return false;
	}
	
	
	/**
	 * 
	 * @Title: asyncNoticeTrade
	 * @Description: 支付成功异步通知
	 * @date 2015年10月8日 下午8:25:30
	 * @author 揭懿
	 * @param params
	 * @param successResult
	 * @param error
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void asyncNoticeTrade(OrderPayResult params,SuccessResult successResult,ErrorResult error) {
		
		OrderPayFlowVo payFlow = orderPayFlowDao.selectByPayNoForUpdate(params.getBiz_no());
		
		if(null != payFlow){
			OrderPayFlow record = new OrderPayFlow();
			record.setId(payFlow.getId());
			record.setPayState(Constants.PayState.付款成功.getKey());
			record.setPayNoticeConfirmTime(DateUtil.now());
			record.setPayFlowNo(params.getSerial_no());//支付流水号
			record.setLastUpdateTime(DateUtil.now());
			orderPayFlowMapper.updateByPrimaryKeySelective(record);
			
			
			TradeVo trade = tradeDao.selectByPrimaryKeyForUpdate(payFlow.getFkTradeId());
			if(null != trade){
				Trade tRecord = new Trade();
				tRecord.setId(trade.getId());
				tRecord.setStateCode(Constants.OrderStateCode.待发货.getKey());
				tRecord.setStateName(Constants.OrderStateCode.待发货.getValue());
				tRecord.setPayState(Constants.PayState.付款成功.getKey());
				tRecord.setPayNoticeConfirmTime(DateUtil.now());
				tRecord.setPayFlowNo(params.getSerial_no());
				tRecord.setPayNo(payFlow.getPayNo());
				tRecord.setPayTime(DateUtil.now());
				
				if(null != params.getAmount()){
					BigDecimal amount = new BigDecimal(params.getAmount());
					tRecord.setTradePaymentAmount(amount);
				}
				tRecord.setLastUpdateTime(DateUtil.now());
				tradeMapper.updateByPrimaryKeySelective(tRecord);
			}
			
			
			
			OrderVo order = orderDao.selectByPrimaryKeyForUpdate(payFlow.getFkOrderId());
			if(null != order){
				Order oRecord = new Order();
				oRecord.setId(order.getId());
				oRecord.setStateCode(Constants.OrderStateCode.待发货.getKey());
				oRecord.setStateName(Constants.OrderStateCode.待发货.getValue());
				oRecord.setLastUpdateTime(DateUtil.now());
				orderMapper.updateByPrimaryKeySelective(oRecord);
				
				// 更新库存已售数
				updateStock(order);
			}
			
			
			TradeOrderRecord toRecord = new TradeOrderRecord();
			toRecord.setFkTradeId(payFlow.getFkTradeId());
			toRecord.setFkOrderId(payFlow.getFkOrderId());
			toRecord.setOpType(Constants.TradeOrderRecordOpType.支付成功异步通知.getValue());
			toRecord.setOpTime(DateUtil.now());
			toRecord.setOpContent("支付系统异步通知");
			tradeOrderRecordMapper.insertSelective(toRecord);
			
			tradeBillService.splitBill(payFlow.getFkOrderId());
			
			successResult.setIs_success(true);
		}else{
			error.setErrorCode(SystemErrorCode.OrderErrorCode.PAY_NO_EXISTENCE);
			error.setErrorMessage("支付单号无效");
		}
	}
	
	/**
	 * 支付后，添加库存已售数 (包括产品、产品sku)
	 * 支付成功后，OrderDetail 下的 产品 buy_count ,要加到 product、prodcutSku 的已售数中(sold_stock)
	 * @author : liguosheng 
	 * 
	 * @CreateDate : 2015年11月18日 上午10:56:27 
	 * @param order
	 */
	private void updateStock(OrderVo order) {
		
		OrderDetailExample detailexample = new OrderDetailExample();
		detailexample.createCriteria().andFkOrderIdEqualTo(order.getId());
		
		List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(detailexample);
		
		if(!CollectionUtils.isEmpty(orderDetails)){
			for(OrderDetail orderDetail : orderDetails){
				
				Long productId = orderDetail.getFkProductId();
				
				Product product = this.tradeDao.selectGdsProductByPrimaryKeyForUpdate(productId);
				
				if(product != null){
					// 已售数
					Integer soldStock = product.getSoldStock();
					
					product.setSoldStock(soldStock + orderDetail.getBuyCount());
					
					this.productMapper.updateByPrimaryKey(product);
				}
				
				Long productSkuId = orderDetail.getFkProductSkuId();
				
				ProductSku productSku = this.tradeDao.selectGdsProductSkuByPrimaryKeyForUpdate(productSkuId);
				
				if(productSku != null){
					// 已售数
					Integer soldStock = productSku.getSoldStock();
					
					productSku.setSoldStock(soldStock + orderDetail.getBuyCount());
					
					this.productSkuMapper.updateByPrimaryKey(productSku);
				}
			}
		}
		
	}


	/**
	 * 钱包支付，
	 */
	public PayResult payWallet(TradeVo trade,OrderVo order,PayResult result){
		
		/** 抵扣钱包 **/
		MinimallMemberWalletAddRequest request = new MinimallMemberWalletAddRequest();
		request.setMember_id(order.getFkBuyeStoreMemberId());
		request.setWallet_type(WalletType.store.getCode());
		//request.setStore_id(9L);
		request.setAmount(trade.getTradeShouldAmount());
		request.setTrans_type(TransType.BUY.getCode());
		MinimallMemberWalletAddResponse response = apiInnerClient.execute(request);
		
		if(response.isSuccess()){
			boolean b = response.getIsSuccess();
			result.setIs_success(b);
			
			/** 更新交易单待发货 **/
			Trade tRecord = new Trade();
			tRecord.setId(trade.getId());
			tRecord.setStateCode(Constants.OrderStateCode.待发货.getKey());
			tRecord.setStateName(Constants.OrderStateCode.待发货.getValue());
			tRecord.setPayState(Constants.PayState.付款成功.getKey());
			tRecord.setPayNoticeConfirmTime(DateUtil.now());
			tRecord.setPayTime(DateUtil.now());
			tRecord.setLastUpdateTime(DateUtil.now());
			tradeMapper.updateByPrimaryKeySelective(tRecord);
			
			/** 记录交易单操作记录 **/
			TradeOrderRecord toRecord = new TradeOrderRecord(); 
			
			if(null != order){
				Order oRecord = new Order();
				oRecord.setId(order.getId());
				oRecord.setStateCode(Constants.OrderStateCode.待发货.getKey());
				oRecord.setStateName(Constants.OrderStateCode.待发货.getValue());
				oRecord.setLastUpdateTime(DateUtil.now());
				orderMapper.updateByPrimaryKeySelective(oRecord);
				
				toRecord.setFkOrderId(order.getId());
			}
			toRecord.setFkTradeId(trade.getId());
			
			toRecord.setOpType(Constants.TradeOrderRecordOpType.钱包支付成功.getValue());
			toRecord.setOpTime(DateUtil.now());
			toRecord.setOpContent("钱包支付成功");
			tradeOrderRecordMapper.insertSelective(toRecord);
			
			
		}else{
			result.setIs_success(false);
			result.setRet_msg("钱包支付失败"+response.getErrorMessage());
		}
		
		return result;
	}
	
	/**
	 * 
	 * @Title: pay
	 * @Description: 支付
	 * @date 2015年10月8日 下午8:27:03
	 * @author 揭懿
	 * @param params
	 * @param error
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public PayResult pay(OrderPayParams params,ErrorResult error) {
		PayResult result = new PayResult();
		Integer i = 0;
		
		TradeVo trade = tradeDao.selectByPrimaryKeyForUpdate(params.getOid());
		
		if (null != trade
				&& null != trade.getStateCode()
				&& trade.getStateCode().equals(
						Constants.OrderStateCode.待付款.getKey())) {

			// TODO
			// 钱包支付
			if (params.getChannel().equals(Constants.PayChannel.钱包支付.getKey())) {

				OrderVo order = orderDao.selectByFkTradeIdForUpdate(trade
						.getId());
				
				result = this.payWallet(trade, order, result);

				return result;
			} else {

				OrderVo order = orderDao.selectByFkTradeIdForUpdate(trade.getId());
				Trade record = new Trade();
				TradeOrderRecord tradeRecord = new TradeOrderRecord();

				record.setId(trade.getId());
				record.setPayChannel(params.getChannel());
				Map<String, String> hParams = new HashMap<String, String>();
				hParams.put("oid", trade.getId().toString());
				hParams.put("pid", params.getPid().toString());

				if (null != params.getSid())
					hParams.put("sid", params.getSid().toString());

				if (null != params.getOpenid()) {
					hParams.put("openid", params.getOpenid());
				}

				if (null != params.getChannel()) {
					hParams.put("channel", params.getChannel());
					record.setPayChannel(params.getChannel());
				}
				if (null != params.getService())
					hParams.put("service", params.getService());
				
				if (null != params.getEdition())
					hParams.put("version", params.getEdition());
				
				if (!StringUtils.isEmpty(params.getReturn_url())){
					hParams.put("return_url", params.getReturn_url());
				}
				
				MultiValueMap<String, String> p = new LinkedMultiValueMap<String, String>();
				for (Entry<String, String> e : hParams.entrySet()) {
					p.add(e.getKey(), e.getValue());
				}
				RestTemplate temp = RestUtil.getRestTemplate();
				result = temp.postForObject(
						this.siteConfig.getProperty("pay_generate_trade_url"),
						p, PayResult.class);

				if (result.getRet_flag().equals("1")
						&& null != result.getData()) {
					OrderPayFlowExample example = new OrderPayFlowExample();
					OrderPayFlowExample.Criteria criteria = example
							.createCriteria();
					criteria.andFkTradeIdEqualTo(trade.getId());
					if (null != params.getChannel())
						criteria.andPayChannelEqualTo(params.getChannel());
					criteria.andPayNoEqualTo(trade.getPayNo());
					criteria.andPaymentPriceEqualTo(trade.getTradeShouldAmount());
					List<OrderPayFlow> flows = orderPayFlowMapper
							.selectByExample(example);

					if (null != flows && !flows.isEmpty()) {
						OrderPayFlow flow = flows.get(0);

						OrderPayFlow newFlow = new OrderPayFlow();
						newFlow.setId(flow.getId());
						newFlow.setPayNo(trade.getPayNo());
						newFlow.setPayUrl(result.getData());
						newFlow.setLastUpdateTime(DateUtil.now());
						orderPayFlowMapper.updateByPrimaryKeySelective(newFlow);

					} else {

						OrderPayFlow flow = new OrderPayFlow();
						flow.setCreateTime(DateUtil.now());
						flow.setFkTradeId(trade.getId());
						flow.setFkOrderId(order.getId());
						if (null != params.getChannel())
							flow.setPayChannel(params.getChannel());

						flow.setPaymentPrice(trade.getTradeShouldAmount());
						flow.setPayNo(trade.getPayNo());
						flow.setPayReceivable(trade.getPayReceivable());
						flow.setPayState(Constants.PayState.已付款待确认.getKey());
						flow.setPayTimeout(DateUtil.addMinutes(DateUtil.now(),
								30));
						flow.setPayUrl(result.getData());
						orderPayFlowMapper.insertSelective(flow);
					}

					result.setIs_success(true);
					record.setPayState(Constants.PayState.已付款待确认.getKey());
					record.setPayUrl(result.getData());
					record.setLastUpdateTime(DateUtil.now());
					if (null != params.getMember_id()) {
						record.setLastUpdateUserId(params.getMember_id());
						tradeRecord.setOpUserId(params.getMember_id());
						Member member = memberMapper.selectByPrimaryKey(params
								.getMember_id());
						if (null != member) {
							tradeRecord.setOpUserName(member.getRealName());
						}
					}
					i = tradeMapper.updateByPrimaryKeySelective(record);


					if (i > 0) {
						tradeRecord.setFkTradeId(record.getId());

						if (null != order)
							tradeRecord.setFkOrderId(order.getId());

						tradeRecord.setOpTime(DateUtil.now());
						tradeRecord
								.setOpContent(Constants.TradeOrderRecordOpType.支付订单
										.getValue());
						tradeRecord
								.setOpType(Constants.TradeOrderRecordOpType.支付订单
										.getValue());
						tradeRecord
								.setOpUserType(Constants.OrderServiceOpUserType.买家
										.getValue());
						tradeOrderRecordMapper.insertSelective(tradeRecord);
					}
					return result;
				} else {
					error.setErrorCode(result.getError_code());
					error.setErrorMessage(result.getRet_msg());
					logger.info(RestUtil.toJson(error));
					return null;
				}
			}
		} else {
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.TRADE_NO_EXISTENCE);
			error.setErrorMessage("交易单无效");
			return null;
		}
		
		
	}
	 
	/**
	 * 1.获取交易单对象
	 * 2.
	 * @CreateDate : 2015年11月17日 上午9:54:18 
	 * @param params 	 传参 
	 * @param error		 错误对象
	 * @return OrderPayResult 对象
	 */
	public OrderPayResult getPayOrder(OrderPayParams params,ErrorResult error){
		OrderPayResult result = new OrderPayResult();
		
		Trade trade = tradeMapper.selectByPrimaryKey(params.getId());
		if(null != trade){
			
			// 交易金额
			result.setAmount(String.valueOf(trade.getTradeShouldAmount()));
			// 支付单号
			result.setBiz_no(trade.getPayNo());
			// 产品描述，多个子产品在第一个产品名称后加上 ‘等等’
			result.setDescribe(trade.getTitle());
			/** 异步通知地址 **/
			result.setNotice_url(this.siteConfig.getProperty("pay.trade.notice_url"));
			
			//result.setOrder_url(order_url);
			
			result.setProduct(trade.getTitle());
			
			// 系统默认返回目标链接
			result.setReturn_url(MessageFormat.format(
					this.siteConfig.getProperty("pay.trade.return_url"),trade.getTradeNo()));
			
			result.setTime_limit(30);
			
			Map<String, String> signParams = new HashMap<String,String>();
			
			signParams.put("biz_no",result.getBiz_no());
			signParams.put("amount", result.getAmount());
			signParams.put("product", result.getProduct());
			signParams.put("describe", result.getDescribe());
			
			if(null != result.getOrder_url())
			signParams.put("order_url", result.getOrder_url());
			
			if(null != result.getNotice_url())
			signParams.put("notice_url", result.getNotice_url());
			
			if(null != result.getReturn_url())
			signParams.put("return_url", result.getReturn_url());
			
			signParams.put("time_limit", String.valueOf(result.getTime_limit()));
			
			if(null != params.getOpenid()){
				  MemberWeixinTicketExample example = new MemberWeixinTicketExample();
				  MemberWeixinTicketExample.Criteria criteria = example.createCriteria();
				  criteria.andWeixinTicketEqualTo(params.getOpenid());
				  List<MemberWeixinTicket> wxtickets = memberWeixinTicketMapper.selectByExample(example);
				  
				  if(null != wxtickets && !wxtickets.isEmpty()){
					  MemberWeixinTicket wxticket =  wxtickets.get(0);
					  signParams.put("openid", wxticket.getWeixinOpenid());
					  result.setOpenid(wxticket.getWeixinOpenid());
				  }
			}
			String sign = SignVerifyUtil.sign(signParams, Constants.SIGN_KEY);
			result.setSign(sign);
		}else{
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.TRADE_NO_EXISTENCE);
    		error.setErrorMessage("交易单无效");
    		return null;
		}
		return result;
	}
	
}
