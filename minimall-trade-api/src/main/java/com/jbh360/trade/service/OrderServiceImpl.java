package com.jbh360.trade.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbh360.common.base.ErrorResult;
import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.Constants;
import com.jbh360.common.utils.DateUtil;
import com.jbh360.goods.entity.Product;
import com.jbh360.goods.entity.ProductSku;
import com.jbh360.goods.mapper.ProductMapper;
import com.jbh360.goods.mapper.ProductSkuMapper;
import com.jbh360.marketing.entity.StoreCoupon;
import com.jbh360.marketing.mapper.StoreCouponMapper;
import com.jbh360.member.oms.entity.Member;
import com.jbh360.member.oms.mapper.MemberMapper;
import com.jbh360.trade.dao.OrderDao;
import com.jbh360.trade.dao.TradeDao;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.entity.OrderDetail;
import com.jbh360.trade.entity.OrderDetailExample;
import com.jbh360.trade.entity.OrderReceiver;
import com.jbh360.trade.entity.PreferentialRecord;
import com.jbh360.trade.entity.PreferentialRecordExample;
import com.jbh360.trade.entity.Trade;
import com.jbh360.trade.entity.TradeOrderRecord;
import com.jbh360.trade.mapper.OrderDetailMapper;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.mapper.OrderReceiverMapper;
import com.jbh360.trade.mapper.PreferentialRecordMapper;
import com.jbh360.trade.mapper.TradeMapper;
import com.jbh360.trade.mapper.TradeOrderRecordMapper;
import com.jbh360.trade.vo.param.OrderParams;
import com.jbh360.trade.vo.param.OrderReceiverEditParams;
import com.jbh360.trade.vo.rs.OrderReceiverEditResult;
import com.jbh360.trade.vo.rs.OrderVo;
import com.jbh360.trade.vo.rs.TradeVo;
import com.soft.redis.client.template.ValueRedisTemplate;

/**
 * 
 * @Title: OrderServiceImpl.java
 * @Package com.jbh360.trade.service
 * @ClassName: OrderServiceImpl
 * @Description: 订单
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月31日 下午3:45:28
 * @version V3.0
 */
@Service
public class OrderServiceImpl {
	
	Logger log = Logger.getLogger(OrderServiceImpl.class);

	@Autowired
	private TradeMapper tradeMapper;
	
	
	@Autowired
	private OrderMapper orderMapper;
	
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private OrderDao orderDao;
	
	
	@Autowired
	private TradeDao tradeDao;
	
	
	@Autowired
	private TradeOrderRecordMapper tradeOrderRecordMapper;
	
	@Autowired
	private OrderReceiverMapper orderReceiverMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	public ValueRedisTemplate valueRedisTemplate;
	
	@Autowired
	private ProductSkuMapper productSkuMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private StoreCouponMapper storeCouponMapper;
	
	@Autowired
	private PreferentialRecordMapper preferentialRecordMapper;
	
	public String getUserName(Long memberId){
    	String userName = null;
    	if(null != memberId){
    		Member member = memberMapper.selectByPrimaryKey(memberId);
        	if(null != member){
        		userName = member.getRealName();
        	}
    	}
    	return userName;
    }
	
	/**
	 * 
	 * @Title: receiverEdit
	 * @Description: 修改收货地址
	 * @date 2015年11月2日 下午6:28:12
	 * @author 揭懿
	 * @param params
	 * @param error
	 */
	public OrderReceiverEditResult receiverEdit(OrderReceiverEditParams params,ErrorResult error){
		
		
		OrderReceiverEditResult orderReceiverEditResult = new OrderReceiverEditResult();
		
		String userName = getUserName(params.getMember_id());
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return null;
    	}
    	
		
		OrderReceiver orderReceiver = orderReceiverMapper.selectByPrimaryKey(params.getOrder_receiver_id());
		
		
		if(null == orderReceiver){
			error.setErrorCode(SystemErrorCode.OrderErrorCode.ORDER_RECEIVER_NO_EXISTENCE);
    		error.setErrorMessage("收货物流信息ID无效");
			return null;
		}
		
		
		if(null != orderReceiver.getFkTradeId()){
			Order order = orderDao.selectByFkTradeIdForUpdate(orderReceiver.getFkTradeId());
			
			if(null != order){
				//订单
				Order newOrder = new Order();
				newOrder.setId(order.getId());
				newOrder.setReceiverType(params.getReceiver_type());
				newOrder.setLastUpdateTime(DateUtil.now());
				newOrder.setLastUpdateUserId(params.getMember_id());
				orderMapper.updateByPrimaryKeySelective(newOrder);
				
				//TradeOrderRecord
				//交易订单操作流水表
				TradeOrderRecord tradeOrderRecord = new TradeOrderRecord();
				tradeOrderRecord.setFkTradeId(order.getFkTradeId());
				tradeOrderRecord.setFkOrderId(order.getId());
				tradeOrderRecord.setOpType(Constants.TradeOrderRecordOpType.修改收货地址.getValue());
				tradeOrderRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getValue());
				tradeOrderRecord.setOpUserId(params.getMember_id());
				tradeOrderRecord.setOpUserName(userName);
				tradeOrderRecord.setOpTime(DateUtil.now());
				tradeOrderRecordMapper.insertSelective(tradeOrderRecord);
			}
		}
		
		OrderReceiver newOrderReceiver = new OrderReceiver();
		
		String addressRegionName = "";
		String ccode="";
		if(null != params.getReceiver_province_ccode()){
			addressRegionName = valueRedisTemplate.get("BMREGION-"+params.getReceiver_province_ccode());
		}
		if(null != params.getReceiver_city_ccode()){
			addressRegionName = valueRedisTemplate.get("BMREGION-"+params.getReceiver_city_ccode());
		}
		if(null != params.getReceiver_area_ccode()){
			addressRegionName = valueRedisTemplate.get("BMREGION-"+params.getReceiver_area_ccode());
			ccode = params.getReceiver_area_ccode();
		}
		if(null != params.getReceiver_street_ccode()){
			addressRegionName = valueRedisTemplate.get("BMREGION-"+params.getReceiver_street_ccode());
			ccode = params.getReceiver_street_ccode();
		}
		
		newOrderReceiver.setAddressRegionName(addressRegionName);
		
		newOrderReceiver.setFkTradeId(orderReceiver.getFkTradeId());
		newOrderReceiver.setLastUpdateTime(DateUtil.now());
		newOrderReceiver.setLastUpdateUserId(params.getMember_id());
		//newOrderReceiver.setPickupplaceBusinessTime(pickupplaceBusinessTime);
		//newOrderReceiver.setPickupplaceContactName(pickupplaceContactName);
		//newOrderReceiver.setPickupplacePhoneNumber(pickupplacePhoneNumber);
		newOrderReceiver.setReceiverAddress(params.getReceiver_address());
		//newOrderReceiver.setPickupplacePhoneArea(pickupplacePhoneArea);
		newOrderReceiver.setReceiverAreaCcode(ccode);
		newOrderReceiver.setReceiverMobile(params.getReceiver_mobile());
		newOrderReceiver.setReceiverName(params.getReceiver_name());
		//newOrderReceiver.setReceiverPhoneArea(params.getr);
		//newOrderReceiver.setReceiverPhoneExt(receiverPhoneExt);
		//newOrderReceiver.setReceiverPhoneNumber(params.getReceiver_mobile());
		newOrderReceiver.setReceiverZipCode(params.getReceiver_zip_code());
		newOrderReceiver.setId(orderReceiver.getId());
		
		orderReceiverMapper.updateByPrimaryKeySelective(newOrderReceiver);
		
		
		orderReceiverEditResult.setId(orderReceiver.getId());
		orderReceiverEditResult.setAddressRegionName(addressRegionName);
		orderReceiverEditResult.setReceiverAddress(params.getReceiver_address());
		orderReceiverEditResult.setReceiverAreaCcode(params.getReceiver_area_ccode());
		orderReceiverEditResult.setReceiverMobile(params.getReceiver_mobile());
		orderReceiverEditResult.setReceiverName(params.getReceiver_name());
		orderReceiverEditResult.setReceiverPhoneArea(orderReceiver.getReceiverPhoneArea());
		orderReceiverEditResult.setReceiverPhoneExt(orderReceiver.getReceiverPhoneExt());
		orderReceiverEditResult.setReceiverPhoneNumber(orderReceiver.getReceiverPhoneNumber());
		orderReceiverEditResult.setReceiverType(String.valueOf(params.getReceiver_type()));
		orderReceiverEditResult.setReceiverZipCode(params.getReceiver_zip_code());
		
		return orderReceiverEditResult;
	}
	
	
	/**
	 * 1.待付款情况下才能关闭
	 * 2.关闭订单、关闭交易单、恢复库存、交易订单操作流水、优惠券恢复、活动恢复
	 * 
	 * @Title: close
	 * @Description: 订单关闭
	 * @date 2015年10月31日 下午3:45:42
	 * @author 揭懿
	 * @param params
	 * @param successResult
	 * @param error
	 */
	public void close(OrderParams params,SuccessResult successResult,ErrorResult error){
		String userName = getUserName(params.getMember_id());
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderVo orderVo = orderDao.selectByPrimaryKeyForUpdate(params.getOrder_id());
		
    	
    	String stateCode = Constants.OrderStateCode.交易关闭.getKey();
    	String stateName = Constants.OrderStateCode.交易关闭.getValue();
    	
		if(null != orderVo){
			
			if(null != orderVo.getStateCode() 
					&& !orderVo.getStateCode().equals(Constants.OrderStateCode.待付款.getKey())){
				error.setErrorCode(SystemErrorCode.OrderErrorCode.STATE_INVALID);
	    		error.setErrorMessage("订单状态无效");
	    		return;
			}
			
			
			Order record = new Order();
			
			record.setId(orderVo.getId());
			record.setStateCode(stateCode);
			record.setStateName(stateName);
			record.setEndTime(DateUtil.now());
			
			record.setLastUpdateTime(DateUtil.now());
			record.setLastUpdateUserId(params.getMember_id());
			
			orderMapper.updateByPrimaryKeySelective(record);
			
			
			
			//更新trade
			TradeVo tradeVo = tradeDao.selectByPrimaryKeyForUpdate(record.getFkTradeId());
			
			if(null != tradeVo){
				Trade trade = new Trade();
				trade.setId(tradeVo.getId());
				
				trade.setStateCode(stateCode);
				trade.setStateName(stateName);
				
				trade.setLastUpdateTime(DateUtil.now());
				trade.setLastUpdateUserId(params.getMember_id());
				
				tradeMapper.updateByPrimaryKeySelective(trade);
			}
			
			// 恢复库存
			restoreProductStock(params.getOrder_id());
			
			// 恢复优惠信息
			restoreCoupon(params.getOrder_id());
			
			//交易订单操作流水表
			TradeOrderRecord tradeOrderRecord = new TradeOrderRecord();
			
			tradeOrderRecord.setFkTradeId(orderVo.getFkTradeId());
			tradeOrderRecord.setFkOrderId(orderVo.getId());
			
			tradeOrderRecord.setOpType(Constants.TradeOrderRecordOpType.关闭订单.getValue());
			tradeOrderRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getValue());
			tradeOrderRecord.setOpUserId(params.getMember_id());
			tradeOrderRecord.setOpUserName(userName);
			tradeOrderRecord.setOpTime(DateUtil.now());
			
			tradeOrderRecordMapper.insertSelective(tradeOrderRecord);
			successResult.setIs_success(true);
			
			
		}else{
			error.setErrorCode(SystemErrorCode.OrderErrorCode.ORDER_NO_EXISTENCE);
    		error.setErrorMessage("订单无效");
    		return;
		}
	}

	/**
	 * 恢复优惠信息
	 *  1.优惠券
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月23日 下午4:43:18 
	 * @param order_id
	 */
	private void restoreCoupon(Long order_id) {
		
		log.info("订单ID："+order_id+" 恢复优惠券 开始");
		try {
			Order order = this.orderMapper.selectByPrimaryKey(order_id);

			Long couponId = order.getFkCouponId();
			log.info("优惠券ID："+couponId);
			if(couponId != null){
				StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);
				// 优惠券类型
				Integer couponType = storeCoupon.getCouponType();
				
				if(couponType == 4){// 创业基金
					log.info("恢复创业基金");
					
					// 剩余金额
					BigDecimal couponLeavingAmount = storeCoupon.getCouponLeavingAmount() == null ? BigDecimal.ZERO:storeCoupon.getCouponLeavingAmount();
					
					PreferentialRecordExample example = new PreferentialRecordExample();
					example.createCriteria().andFkOrderIdEqualTo(order_id).andFkIdEqualTo(couponId);
					List<PreferentialRecord> pres = preferentialRecordMapper.selectByExample(example);
					// 恢复金额
					BigDecimal discountAmount = pres.get(0).getDiscountAmount();
					log.info("恢复金额："+discountAmount);
					storeCoupon.setCouponLeavingAmount(couponLeavingAmount.add(discountAmount));
					
				}
				
				// 店铺优惠劵（状态：0未使用、1已使用，2未领取，3使用完，-1已过期，-50已删除）
				storeCoupon.setState(0);
				this.storeCouponMapper.updateByPrimaryKey(storeCoupon);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("订单ID："+order_id+" 恢复优惠券 结束");

	}

	/**
	 * 恢复库存
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月17日 上午10:57:48 
	 * @param order_id
	 */
	private void restoreProductStock(Long order_id) {
		// TODO 恢复库存
		
		OrderDetailExample example = new OrderDetailExample();
		example.createCriteria().andFkOrderIdEqualTo(order_id);
		List<OrderDetail> details = this.orderDetailMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(details)){
			for(OrderDetail detail :details){
				
				// 恢复库存
				Integer buyCount = detail.getBuyCount() == null? 0 : detail.getBuyCount();
				
				Product product = this.tradeDao.selectGdsProductByPrimaryKeyForUpdate(detail.getFkProductId());
				if(product != null){
					// 产品库存数量
					Integer product_stock = product.getProductStock() == null?0:product.getProductStock();
					
					product.setProductStock(product_stock + buyCount);
					
					this.productMapper.updateByPrimaryKey(product);
					
				}
				
				ProductSku productSku = this.tradeDao.selectGdsProductSkuByPrimaryKeyForUpdate(detail.getFkProductSkuId());
				if(productSku != null){
					// 商品库存数
					Integer product_stock = productSku.getProductStock() == null?0:productSku.getProductStock();
					
					productSku.setProductStock(product_stock + buyCount);
					
					this.productSkuMapper.updateByPrimaryKey(productSku);
				}
				
			}
		}
		
	}
}
