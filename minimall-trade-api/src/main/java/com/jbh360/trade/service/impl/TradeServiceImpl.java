package com.jbh360.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbh360.api.sdk.ApiInnerClient;
import com.jbh360.api.sdk.marketing.entity.MemberGameResult;
import com.jbh360.api.sdk.marketing.request.MinimallProductGameMemberCheckRequest;
import com.jbh360.api.sdk.marketing.response.MinimallProductGameMemberCheckResponse;
import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.exception.SystemErrorCode.TradeAddErrorCode;
import com.jbh360.common.utils.Constants;
import com.jbh360.common.utils.Constants.AuditState;
import com.jbh360.common.utils.Constants.CommonState;
import com.jbh360.common.utils.Constants.OrderExpressState;
import com.jbh360.common.utils.Constants.OrderReceiverType;
import com.jbh360.common.utils.Constants.OrderType;
import com.jbh360.common.utils.Constants.ProductState;
import com.jbh360.common.utils.DateUtil;
import com.jbh360.common.utils.NoUtil;
import com.jbh360.common.utils.SEQGenerate;
import com.jbh360.common.utils.SessionUtils;
import com.jbh360.goods.entity.Product;
import com.jbh360.goods.entity.ProductSku;
import com.jbh360.goods.entity.ProductSkuExample;
import com.jbh360.goods.entity.StoreGoodsSku;
import com.jbh360.goods.entity.StoreGoodsSkuExample;
import com.jbh360.goods.mapper.ProductMapper;
import com.jbh360.goods.mapper.ProductSkuMapper;
import com.jbh360.goods.mapper.StoreGoodsSkuMapper;
import com.jbh360.marketing.entity.GdsProductGameWithBLOBs;
import com.jbh360.marketing.entity.OmsCouponType;
import com.jbh360.marketing.entity.StoreCoupon;
import com.jbh360.marketing.entity.StoreCouponExample;
import com.jbh360.marketing.entity.StoreCouponRecord;
import com.jbh360.marketing.entity.StorePromotion;
import com.jbh360.marketing.mapper.GdsProductGameMapper;
import com.jbh360.marketing.mapper.OmsCouponTypeMapper;
import com.jbh360.marketing.mapper.StoreCouponMapper;
import com.jbh360.marketing.mapper.StoreCouponRecordMapper;
import com.jbh360.marketing.mapper.StorePromotionMapper;
import com.jbh360.member.oms.entity.Member;
import com.jbh360.member.oms.entity.SupplierWarehouse;
import com.jbh360.member.oms.mapper.MemberMapper;
import com.jbh360.member.oms.mapper.SupplierWarehouseMapper;
import com.jbh360.store.entity.StoreInfo;
import com.jbh360.store.mapper.StoreInfoMapper;
import com.jbh360.trade.dao.TradeDao;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.entity.OrderDetail;
import com.jbh360.trade.entity.OrderReceiver;
import com.jbh360.trade.entity.PurchaseCart;
import com.jbh360.trade.entity.ShopCart;
import com.jbh360.trade.entity.Trade;
import com.jbh360.trade.entity.TradeOrderRecord;
import com.jbh360.trade.mapper.OrderDetailMapper;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.mapper.OrderReceiverMapper;
import com.jbh360.trade.mapper.PurchaseCartMapper;
import com.jbh360.trade.mapper.ShopCartMapper;
import com.jbh360.trade.mapper.TradeMapper;
import com.jbh360.trade.mapper.TradeOrderRecordMapper;
import com.jbh360.trade.service.TradeService;
import com.jbh360.trade.utils.OrderConstants.PreferentialRecordType;
import com.jbh360.trade.vo.param.AppOrderParam;
import com.jbh360.trade.vo.param.AppTradeParam;
import com.jbh360.trade.vo.param.WapOrderParam;
import com.jbh360.trade.vo.param.WapTradeParam;
import com.jbh360.trade.vo.rs.TradeResult;
import com.soft.redis.client.template.ValueRedisTemplate;
import com.yooyo.util.RestUtil;

/**  
* @Title: TradeServiceImpl.java
* @Package com.jbh360.trade.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月3日 下午3:06:14
* @version V3.0  
*/
@Service
public class TradeServiceImpl implements TradeService{
	
	@Autowired
	private GdsProductGameMapper gdsProductGameMapper; 
	@Autowired
	private MemberMapper memberMapper; 
	@Autowired
	private ApiInnerClient apiInnerClient;
	@Autowired
	private ProductSkuMapper productSkuMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private StoreGoodsSkuMapper storeGoodsSkuMapper;
	@Autowired
	private StorePromotionMapper storePromotionMapper;
	@Autowired
	private StoreCouponMapper storeCouponMapper;
	@Autowired
	private TradeDao tradeDao;
	@Autowired
	private TradeMapper tradeMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private OrderReceiverMapper orderReceiverMapper;
	@Autowired
	private StoreInfoMapper storeInfoMapper;
	@Autowired
	private SupplierWarehouseMapper supplierWarehouseMapper;
	@Autowired
	private ValueRedisTemplate valueRedisTemplate;
	@Autowired
	private PurchaseCartMapper purchaseCartMapper;
	@Autowired
	private ShopCartMapper shopCartMapper;
	@Autowired
	private StoreCouponRecordMapper storeCouponRecordMapper;
	@Autowired
	private OmsCouponTypeMapper omsCouponTypeMapper;
	
	@Autowired
	private TradeOrderRecordMapper tradeOrderRecordMapper;
	
	private Logger log = Logger.getLogger(TradeServiceImpl.class);
	
	/******
	 * 
	 * 
	 * 
	 * 订单新增
	 * （1）判断订单类型
	 * （2）处理订单商品
	 * （3）处理优惠
	 */
	@Override
	public TradeResult appAdd(AppTradeParam params){
		/** 最终返回的 订单ID、订单编号  **/
		String rOrderNo = null;
		Long rOrderId = null;
		
		/** 校验用户 **/
		checkMember(params.getMember_id());
		
		
		/** 交易单对象 **/		
		Trade trade = new Trade();
		trade.setId(SEQGenerate.getId());
		
		/** 收货物流对象 **/
		OrderReceiver orderReceiver = putOrderReceiver(params,trade);
		
		/** 参数对象 **/
		List<AppOrderParam> orderParams = params.getOrders();
		
		// 标题，用于保存订单产品名称，如果一个以上，直接在第一个产品名称后面加等等就好。
		String tradeTitle = null;
		
		BigDecimal tradeAppDiscountTotalAmount = BigDecimal.ZERO;	// 平台优惠
		BigDecimal tradeFreightTotalAmount = BigDecimal.ZERO;  		// 邮费
		BigDecimal tradeOrderDiscountTotalAmount=  BigDecimal.ZERO;	// 订单优惠总金额
		BigDecimal tradePayAmount = BigDecimal.ZERO; 				// 订单实付总金额
		BigDecimal tradeProductTotalAmount = BigDecimal.ZERO; 		// 产品总 金额
		
		List<Order> orders = new ArrayList<Order>();	//订单信息
		
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();	//订单明细信息
		
//		OrderType orderType = OrderType.getValue(params.getOrder_type());
		
		
		if(orderParams != null && !orderParams.isEmpty()){
			for (AppOrderParam orderParam : orderParams) {
				Order order =new Order();//订单信息
				orders.add(order);
				order.setId(SEQGenerate.getId());
				order.setFkTradeId(trade.getId());
				
				List<com.jbh360.trade.vo.param.AppProduct> productParams = orderParam.getProducts();
				
				// 商品实付金额汇总
				BigDecimal orderGoodsTotalAmont = BigDecimal.ZERO;
				// 商品优惠金额汇总
				BigDecimal orderGoodsDiscountTotalAmount = BigDecimal.ZERO;
				// 运费优惠（邮资卡）
				BigDecimal freightDiscountAmount = BigDecimal.ZERO;
				// 邮费
				BigDecimal orderFreightAmount = orderParam.getFreight_amount()==null?BigDecimal.ZERO:orderParam.getFreight_amount();
				
				/********************************************************************************************************/
				/**************************************订单明细计算开始***************************************************/
				/********************************************************************************************************/
				// 优游劵ID 
				Long order_coupon_id = orderParam.getOrder_coupon_id();
				String product_coupon_ids = orderParam.getProduct_coupon_ids();
				BigDecimal order_coupon_product_total_amount = BigDecimal.ZERO;  // 优惠 产品实付价汇总
				// 活动ID
				Long order_game_id = orderParam.getOrder_game_id();
				String product_game_ids = orderParam.getProduct_game_ids();
				BigDecimal order_game_product_total_amoun = BigDecimal.ZERO;	 // 活动产品实付价汇总		 
				
				if(productParams != null && !productParams.isEmpty()){
					for (com.jbh360.trade.vo.param.AppProduct productParam : productParams) {
						
						log.info("开始 ===============================计算产品ID"+productParam.getProduct_id());
						
						// 校验产品对象
						Product minimallProduct = checkProduct(productParam.getProduct_id());
						
						// 校验产品sku对象
						ProductSku productSku = checkProductSku(productParam.getProduct_id(),productParam.getProduct_sku_barcode());
						
						// 设置订单明细
						OrderDetail orderDetail = putOrderDetail(trade,order,productParam, minimallProduct, productSku);
						
						// 存入订单明细集合
						orderDetails.add(orderDetail);

						/** 校验更新库存 **/
						checkProductSkuStock(productParam.getProduct_id(), productSku.getId(), productParam.getBuy_count());

						/** 设置阶梯定价 **/
						setLadderPrice(productParam, productSku, orderDetail);
						
						/**
						 * 促销活动
						 */
						if(productParam.getProduct_game_id() != null && productParam.getProduct_game_id() != 0l){
							
							// 校验，Product_game_id 不能与 (order_game_id 或  coupon_id) 同时存在 
							if(order_coupon_id != null || order_game_id != null){
								throw new ServiceException(TradeAddErrorCode.GAME_CAN_NOT_WITH_DISCOUNT_ERROR,"促销产品不能同时拥有其它优惠信息");
							}
							
							/**
							 * 循环调用查询促销活动，时间问题
							 * 2015年10月3日17:46:28 joe
							 */
							log.info("有产品活动ID："+productParam.getProduct_game_id());
							
							MinimallProductGameMemberCheckRequest minimallProductGameMemberCheckRequest = new MinimallProductGameMemberCheckRequest(params.getMember_id(), productParam.getProduct_game_id(), productParam.getProduct_id(), productParam.getProduct_sku_barcode(), productParam.getBuy_count());
							MinimallProductGameMemberCheckResponse response = apiInnerClient.execute(minimallProductGameMemberCheckRequest);
							List<MemberGameResult> gs = response.getMemberGameResults();
							
							if(response.isSuccess()){
								if(!CollectionUtils.isEmpty(gs)){
									MemberGameResult g = gs.get(0);
									if(g.getIs_can_buy()){
										log.info("单价取产品活动价："+g.getProduct_game_price());
										orderDetail.setDealUnitPrice(g.getProduct_game_price());
										orderDetail.setFkGameId(productParam.getProduct_game_id());
									}else{
										throw new ServiceException(TradeAddErrorCode.GAME_COULD_NOT_BE_USED_ERROR,"活动id为"+productParam.getProduct_game_id()+"的活动无法使用；"+g.getCan_not_buy_reason());
									}
								}else{
									throw new ServiceException("系统有误");
								}
								
							}else if(StringUtils.isNotBlank(response.getErrorCode())){
								throw new ServiceException(response.getErrorMessage(),response.getErrorCode());
							}
						}
						
						// 实付金额 = 成交单价 * 数量
						orderDetail.setPaymentAmount(orderDetail.getDealUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getBuyCount())));
						// 产品优惠金额 
						orderDetail.setGoodsDiscountAmount(BigDecimal.ZERO);
						
						orderGoodsDiscountTotalAmount = orderGoodsDiscountTotalAmount.add(orderDetail.getGoodsDiscountAmount());
						
						/***
						 * 算活动产品实付价汇总
						 */
						if(order_game_id != null && order_game_id != 0l){
							if(StringUtils.isNotBlank(product_game_ids)){
								String[] ids=product_game_ids.split(",");
								if(ids != null){
									for (int i = 0; i < ids.length; i++) {
										if(ids[i].equals(productParam.getProduct_id()+"")){
											order_game_product_total_amoun = order_game_product_total_amoun.add(orderDetail.getPaymentAmount());
											orderDetail.setFkGameId(order_game_id);
										}
									}
								}
							}else{
								/** product_game_ids为空的时候店铺所有商品通用*/
								order_game_product_total_amoun = order_game_product_total_amoun.add(orderDetail.getPaymentAmount());
								orderDetail.setFkGameId(order_game_id);
							}
						}
						
						/***
						 * 算优惠产品实付汇总
						 */
						if(order_coupon_id != null && order_coupon_id != 0l){
							
							if(StringUtils.isNotBlank(product_coupon_ids)){
								String[] ids=product_coupon_ids.split(",");
								if(ids != null){
									for (int i = 0; i < ids.length; i++) {
										if(ids[i].equals(productParam.getProduct_id()+"")){
											order_coupon_product_total_amount = order_coupon_product_total_amount.add(orderDetail.getPaymentAmount());
											orderDetail.setFkCouponId(order_coupon_id);
										}
									}
								}
							}else{
								/** product_coupon_ids为空的时候店铺所有商品通用*/
								order_coupon_product_total_amount = order_coupon_product_total_amount.add(orderDetail.getPaymentAmount());
								orderDetail.setFkCouponId(order_coupon_id);
							}
						}
							
						orderGoodsTotalAmont = orderGoodsTotalAmont.add(orderDetail.getPaymentAmount());
						
						log.info("结束===============================计算产品ID"+productParam.getProduct_id());
					}
				}
				/************************************订单明细计算结束********************************************************************/
				/**********************************************************************************************************************/
				/**********************************************************************************************************************/
				/**********************************************************************************************************************/
				
				// 传入的店铺优惠金额
				BigDecimal order_coupon_discount_amount = orderParam.getOrder_coupon_discount_amount();
				
				if(order_coupon_discount_amount == null) order_coupon_discount_amount=BigDecimal.ZERO;
				
				// 传入的满减活动金额
				BigDecimal order_game_discount_amount = orderParam.getOrder_game_discount_amount();
				if(order_game_discount_amount == null) order_game_discount_amount = BigDecimal.ZERO;
				
				/** 优惠券检测 **/
				checkCoupon(order_coupon_id, 
							product_coupon_ids,
							order_coupon_discount_amount,
							order_coupon_product_total_amount, 
							order.getId(),
							trade.getId(),
							orderParam,
							params
				);
				
				
				/** 满减活动和专题活动校验 **/
				checkGame(	order_game_id, 
							product_game_ids,
							order_game_discount_amount,
							order_game_product_total_amoun, 
							order.getId()
				);
				
				order.setFkCouponId(order_coupon_id);
				order.setFkGameId(order_game_id);
				
				// 运费优惠（包括邮资卡）
				order.setFreightDiscountAmount(freightDiscountAmount);
				
				// 改价优惠
				order.setChangePriceAmount(BigDecimal.ZERO);
				
				// 邮费
				order.setOrderFreightAmount(orderFreightAmount);
				
				// .....
				order.setOrderTotalPayLossAmount(BigDecimal.ZERO);
				
				// 商品总额 
				order.setOrderGoodsTotalAmount(orderGoodsTotalAmont);
				
				// 商品优惠金额汇总
				order.setGoodsDiscountAmount(orderGoodsDiscountTotalAmount);

				// 订单优惠金额  = 店铺优惠 + 满减活动 /专题活动  
				order.setOrderDiscountAmount(order_coupon_discount_amount.add(order_game_discount_amount));
				
				// 优惠总金额  = 订单优惠金额 + 商品优惠 +改价优惠+邮费优惠
				order.setOrderDiscountTotalAmount(getOrderDiscountTotalAmount(order));
				
				// 订单总价  === 商品总金额-优惠总金额
				order.setOrderTotalAmount(orderGoodsTotalAmont.subtract(order.getOrderDiscountTotalAmount()));
				
				// 实付价钱 == 订单总价 + 邮费 
				order.setOrderPaymentAmount(getOrderPaymentAmounMethod(order));
				
				// 是否七天退货
				order.setIsSupportSellService(getSupportReturnType(orderParam.getSeller_store_id()));
				
				order.setBuyerRemark(orderParam.getBuyer_remark());
				order.setIsFreeze((short)0);
				order.setCreateTime(new Date());
				order.setCreateUserId(SessionUtils.getMember_id());
				order.setDealType((short)2);//及时到账
				StoreInfo  buerStoreInfo = storeInfoMapper.selectByPrimaryKey(params.getStore_id());
				if(buerStoreInfo != null && buerStoreInfo.getId() != 0l && buerStoreInfo.getIsGuarantee() != null){
					if(buerStoreInfo.getIsGuarantee().equals((short)1)){
						order.setDealType((short)1);//是担保
					}
				}
				order.setOrderType(OrderType.采购单.getKey());
//				order.setEndTime(new Timestamp(System.currentTimeMillis()+7*24*60*60*1000));
				order.setExtendSingTime(null);		
				
				setAppUserInfo(order,params,orderParam);
				
				order.setFkOrderReceiverId(orderReceiver.getId());
				order.setStateCode("wait_buyer_pay");
				order.setStateName("待付款");
				order.setCreateUserId(SessionUtils.getMember_id());
				order.setCreateTime(new Date());
				order.setOrderNo(NoUtil.getOrderNo("minimall-order-no"));
				order.setReceiverType(params.getReceiver_type());
				order.setStartTime(DateUtil.now());
				
				tradeProductTotalAmount = tradeProductTotalAmount.add(order.getOrderGoodsTotalAmount());
				
				tradeFreightTotalAmount = tradeFreightTotalAmount.add(order.getOrderFreightAmount());
				
				tradeOrderDiscountTotalAmount = tradeOrderDiscountTotalAmount.add(order.getOrderDiscountTotalAmount());
				
				tradePayAmount = tradePayAmount.add(order.getOrderPaymentAmount());
				
				rOrderId = order.getId();
				rOrderNo = order.getOrderNo();
				
				if(order.getFkBuyeStoreMemberId().equals(order.getFkSellerMemberId())){
					throw new ServiceException("用户不能购买自己的商品",TradeAddErrorCode.USER_NOT_ALLOWED);
				}
				
				createTradeOrderRecord(order);
			}
			
		}
		trade.setCreateTime(new Date());
		trade.setCreateUserId(SessionUtils.getMember_id());
		trade.setTradeNo(NoUtil.getOrderNo("minimall-trade-no"));
		trade.setPayNo(NoUtil.getOrderNo("minimall-pay-no"));
		trade.setStateCode("wait_buyer_pay");
		trade.setStateName("待付款");
		trade.setPayState((short)0);
		
		
		// 平台优惠
		trade.setTradeDiscountTotalAmount(tradeAppDiscountTotalAmount);   
		
		// 订单的优惠总金额
		trade.setOrderDiscountTotalAmount(tradeOrderDiscountTotalAmount); 
		
		// 产品总额
		trade.setGoodsTotalAmount(tradeProductTotalAmount); 
		
		// 优惠总金额          ====  商品优惠总金额+订单优惠总金额+订单的改价优惠
		trade.setDiscountTotalAmount(tradeOrderDiscountTotalAmount.add(trade.getTradeDiscountTotalAmount())); 
		
		// 邮费
		trade.setFreightTotalAmount(tradeFreightTotalAmount);
		
		// 实付总金额 = 订单实付金额总和
		trade.setTradeShouldAmount(tradePayAmount);
		
		
		trade.setTradeTotalPayLossAmount(BigDecimal.ZERO);
		trade.setFkMemberId(params.getMember_id());
		trade.setRecycleState((short)1);
		
		if(!CollectionUtils.isEmpty(orderDetails)){
			tradeTitle = orderDetails.get(0).getProductName();
			if(orderDetails.size() > 1){
				tradeTitle +=" 等等";
			}
			trade.setTitle(tradeTitle);
		}	
		/**
		 * 金额验证
		 */
		if(trade.getTradeShouldAmount().compareTo(tradePayAmount) != 0){
			throw new ServiceException("实付总金额不符,后台计算为："+trade.getTradeShouldAmount(),TradeAddErrorCode.PAP_AMOUONT_INCONSISTENT_ERROR);
		}
		
		/** 数据录入 **/
		if(tradeMapper.insertSelective(trade)>0){
			for (Order order : orders) {
				orderMapper.insertSelective(order);
			}
			for (OrderDetail orderDetail : orderDetails) {
				orderDetailMapper.insertSelective(orderDetail);
			}
			orderReceiverMapper.insertSelective(orderReceiver);
			
			/** 删除采购车 **/
			deletePurchaCard(orderParams);
		}
		
		return new TradeResult(trade.getId(), trade.getTradeNo(),rOrderId,rOrderNo);
	}

	/**
	 * 保存相关用户信息(app)
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月7日 下午2:17:21 
	 * @param order
	 * @param params
	 * @param orderParam
	 */
	private void setAppUserInfo(Order order, AppTradeParam params,AppOrderParam orderParam) {
		Member sellmember = this.memberMapper.selectByPrimaryKey(orderParam.getSeller_member_id());
		
		if(sellmember == null){
			throw new ServiceException("该商户ID"+orderParam.getSeller_member_id()+"不存在");
		}
		
		StoreInfo sellStore = this.storeInfoMapper.selectByPrimaryKey(orderParam.getSeller_store_id());
		if(sellStore == null){
			throw new ServiceException("该商户店铺"+orderParam.getSeller_store_id() + "不存在");
		}
		
		
		// 掌柜店铺ID
		order.setFkBuyerStoreId(params.getStore_id());
		// 掌柜ID
		order.setFkBuyeStoreMemberId(params.getMember_id());
		
		// 商户ID
		order.setFkSellerMemberId(orderParam.getSeller_member_id());
		// 商户店铺ID
		order.setFkSellerStoreId(orderParam.getSeller_store_id());
		
		// 商户名称名称
		order.setSellerMemberName(sellmember.getRealName());
		// 商户店铺名称
		order.setSellerStoreName(sellStore.getStoreName());
		// 主商户ID
		order.setFkSellerSupplierId(sellmember.getFkSupplierId());
		
	}



	/**
	 * 	获取order订单实付金额 =  订单总价 + 邮费
	 */
	private BigDecimal getOrderPaymentAmounMethod(Order order) {
		// 订单总价
		BigDecimal orderTotalAmount = order.getOrderTotalAmount() == null?BigDecimal.ZERO:order.getOrderTotalAmount();
		// 邮费
		BigDecimal orderFreightAmount = order.getOrderFreightAmount() == null?BigDecimal.ZERO:order.getOrderFreightAmount();
		
		return orderTotalAmount.add(orderFreightAmount);
	}

	
	/**
	 *   获取order优惠总额
	 *   (app)订单优惠金额 = 满减活动优惠 + 店铺优惠 + 改价优惠  		/     (wap)订单优惠金额 =  私密折扣 + 改价优惠
	 * 	  优惠总额  = 订单优惠金额  + 商品优惠  
	 */
	private BigDecimal getOrderDiscountTotalAmount(Order order) {
		// 订单优惠
		BigDecimal orderDiscountAmount = order.getOrderDiscountAmount()==null?BigDecimal.ZERO:order.getOrderDiscountAmount();
		// 产品优惠
		BigDecimal goodsDiscountAmount = order.getGoodsDiscountAmount()==null?BigDecimal.ZERO:order.getGoodsDiscountAmount();
		
		return orderDiscountAmount.add(goodsDiscountAmount);
	}

	/**
	 * 设置阶梯定价(针对采购单才有)
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月14日 下午2:28:29 
	 * @param productParam 获取购买数量
	 * @param productSku   获取产品单价
	 * @param orderDetail  
	 */
	private void setLadderPrice(com.jbh360.trade.vo.param.AppProduct productParam,
			ProductSku productSku, OrderDetail orderDetail) {
		
		log.info("产品ID:"+productSku.getFkProductId() + "数量为："+productParam.getBuy_count());
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("productId", productSku.getFkProductId());
		map.put("buycount", productParam.getBuy_count());
		Map<String, Object> resultMap = tradeDao.getPrice(map);
		
		
		orderDetail.setSaleUnitPrice(productSku.getSettlementPrice());
		if(resultMap != null && !resultMap.isEmpty() && resultMap.get("price")!=null){
			BigDecimal dealUnitPrice = new BigDecimal(resultMap.get("price")+"");
			log.info("取阶梯价"+dealUnitPrice);
			orderDetail.setDealUnitPrice(dealUnitPrice);
		}else{
			log.info("取单价："+productSku.getSettlementPrice());
			orderDetail.setDealUnitPrice(productSku.getSettlementPrice());
		}
	}

	/**
	 * 设置订单明细信息
	 * @author : liguosheng 
	 * @param trade 
	 * @param order
	 * @param product 传参param对象
	 * @param minimallProduct
	 * @param productSku
	 * @return
	 */
	private OrderDetail putOrderDetail(Trade trade, Order order,
			com.jbh360.trade.vo.param.AppProduct product, Product minimallProduct,
			ProductSku productSku) {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setId(SEQGenerate.getId());
		orderDetail.setFkTradeId(trade.getId());
		orderDetail.setFkOrderId(order.getId());
		orderDetail.setFkProductId(product.getProduct_id());
		orderDetail.setSkuBarcode(product.getProduct_sku_barcode());
		orderDetail.setFkStoreGoodsId(product.getStore_goods_id());
		orderDetail.setBuyCount(product.getBuy_count());
		orderDetail.setCreateTime(new Date());
		orderDetail.setExpressState(OrderExpressState.待发货.getKey());
		orderDetail.setServiceState(null);
		orderDetail.setProductName(minimallProduct.getName());
		orderDetail.setProductLogoRsurl(minimallProduct.getLogoRsurl());
		orderDetail.setFkProductHistoryId(productSku.getCurrentHistoryId());
		orderDetail.setFkProductHistorySkuId(productSku.getId());
		orderDetail.setProductVersionNo(productSku.getCurrentHistoryVersion());
		orderDetail.setOuterSkuCode(productSku.getOuterSkuId());
		orderDetail.setSkuPropertyValue(productSku.getSkuProperties());
		orderDetail.setSaleUnitPrice(productSku.getSettlementPrice());	//代销单：店铺销售单价；自营单:店铺销售单价(取自产品表的结算价)
		return orderDetail;
	}

	
	/**
	 * 校验商品sku
	 */
	private ProductSku checkProductSku(Long productId,String productSkuBarcode){
		ProductSkuExample example = new ProductSkuExample();
		example.createCriteria().andFkProductIdEqualTo(productId).andProductSkuBarcodeEqualTo(productSkuBarcode);
		List<ProductSku> productSkus = productSkuMapper.selectByExample(example);
		if(productSkus == null || productSkus.isEmpty()){
			throw new  ServiceException(TradeAddErrorCode.PRODUCT_SKU_NOT_EXIST_ERROR,"product_sku_barcode为"+productSkuBarcode+"的商品不存在");
		}
		
		ProductSku productSku = productSkus.get(0);
		return productSku;
	}

	/**
	 * 校验商品
	 */
	private Product checkProduct(Long productId){
		Product minimallProduct = productMapper.selectByPrimaryKey(productId);
		if(minimallProduct == null || minimallProduct.getId() == null || minimallProduct.getId() == 0l){
			throw new ServiceException(TradeAddErrorCode.PRODUCT_NOT_EXIST_ERROR,"id为"+productId+"的商品不存在");
		}
		return minimallProduct;
	}


	/**
	 * 校验用户
	 */
	private void checkMember(Long member_id) {
		Member member = memberMapper.selectByPrimaryKey(member_id);
		if(member == null || member.getId()==null || member.getId()==0l){
			throw new ServiceException(TradeAddErrorCode.MEMBER_NOT_EXIST_ERROR,"该用户不存在");
		}
	}


	/**
	 * 收货物流对象赋值
	 * @CreateDate : 2015年10月14日 下午1:09:34 
	 * @param params
	 * @param trade
	 * @return
	 */
	private OrderReceiver putOrderReceiver(AppTradeParam params, Trade trade) {
		OrderReceiver orderReceiver = new OrderReceiver();
		orderReceiver.setId(SEQGenerate.getId());
		orderReceiver.setCreateUserId(SessionUtils.getMember_id());
		orderReceiver.setCreateTime(new Date());
		orderReceiver.setFkTradeId(trade.getId());
		orderReceiver.setReceiverMobile(params.getReceiver_mobile());
		orderReceiver.setReceiverName(params.getReceiver_name());
		orderReceiver.setReceiverPhoneExt(params.getReceiver_mobile());
		orderReceiver.setReceiverPhoneNumber(params.getReceiver_mobile());
		orderReceiver.setReceiverZipCode(params.getReceiver_zip_code());
		orderReceiver.setReceiverAddress(params.getReceiver_address());
		orderReceiver.setReceiverAreaCcode(params.getReceiver_area_ccode());
		if(params.getReceiver_area_ccode() != null){
			orderReceiver.setAddressRegionName(valueRedisTemplate.get("BMREGION-"+params.getReceiver_area_ccode()));
		}
		//orderReceiver.setAddressRegionName(params.getReceiver_address());//代码的地址
		
		
		/** 收货方式(0：自提，1：物流快递) **/
		if(OrderReceiverType.自提.getKey().equals(params.getReceiver_type())){
			if(params.getWarehouse_id() != null && params.getWarehouse_id() != 0l){
				SupplierWarehouse supplierWarehouse = supplierWarehouseMapper.selectByPrimaryKey(params.getWarehouse_id());
				if(supplierWarehouse == null || supplierWarehouse.getId() == null || supplierWarehouse.getId() == 0l || supplierWarehouse.getState() != 50){
					throw new ServiceException("收货方式为自提的时候没找到该自提点");
				}
				orderReceiver.setPickupplaceBusinessTime(supplierWarehouse.getOpeningTime());
				orderReceiver.setPickupplaceContactName(supplierWarehouse.getTel());
				orderReceiver.setReceiverAddress(supplierWarehouse.getAreaCcodeName() + supplierWarehouse.getAddress());
				orderReceiver.setAddressRegionName(supplierWarehouse.getAreaCcodeName());
				orderReceiver.setPickupplacePhoneArea(null);
				orderReceiver.setPickupplaceContactName(supplierWarehouse.getLinkman());
				orderReceiver.setPickupplacePhoneNumber(supplierWarehouse.getMoblie());
			}else{
				throw new ServiceException("收货方式为自提的时候必须选择自提点");
			}
			
		}else if(OrderReceiverType.物流快递.getKey().equals(params.getReceiver_type())){
			
		}
		
		
		return orderReceiver;
	}


	/**
	 * 删除采购车
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月13日 下午5:29:31 
	 * @param orderParams
	 */
	private void deletePurchaCard(List<AppOrderParam> orderParams) {
		if(!CollectionUtils.isEmpty(orderParams)){
			for(AppOrderParam orderParam : orderParams){
				Long[] cardIds = orderParam.getCart_ids();
				if(cardIds != null && cardIds.length > 0){
					for(Long cardId : cardIds){
						PurchaseCart record = new PurchaseCart();
						record.setId(cardId);
						record.setState(Constants.CommonState.DELETE.getValue());
						this.purchaseCartMapper.updateByPrimaryKeySelective(record);
					}
				}
			}
		}
	}

	/**
	 * 校验订单满减活动
	 * @param order_game_id 				活动ID
	 * @param product_game_ids 				活动产品IDs
	 * @param discount_amount 	需要校验的活动价格
	 * @param totalAmount  					产品实付价汇总
	 * @param order_id						订单ID
	 * @throws Exception
	 */
	private void checkGame(Long order_game_id,String product_game_ids, BigDecimal discount_amount,BigDecimal totalAmount,Long order_id)
			{
		GdsProductGameWithBLOBs  productGame = null;
		if(order_game_id != null && order_game_id != 0l){
			
			log.info("校验满减活动订单"+order_game_id);
			
			productGame = gdsProductGameMapper.selectByPrimaryKey(order_game_id);
			if(productGame==null || productGame.getId()==null || productGame.getId()==0l){
				throw new ServiceException(TradeAddErrorCode.GAME_NOT_EXIST_ERROR,"当传了促销活动的id时，该促销活动不存在");
			}
			if(productGame.getStartTime().after(new Date())){
				throw new ServiceException(TradeAddErrorCode.GAME_TIME_NOT_BEGIN_ERROR,"该促销活动未开始");
			}else if(productGame.getEndTime().before(new Date())){
				throw new ServiceException(TradeAddErrorCode.GAME_TIME_IS_OVER_ERROR,"该促销活动已结束");
			}else if(productGame.getState()!=50){
				throw new ServiceException(TradeAddErrorCode.GAME_COULD_NOT_BE_USED_ERROR,"该促销活动不可以使用");
			}else if(productGame.getActivityState()!=50){
				throw new ServiceException(TradeAddErrorCode.GAME_COULD_NOT_BE_USED_ERROR,"该促销活动不可以使用");
			}else if(productGame.getActivityState()!=50){
				throw new ServiceException(TradeAddErrorCode.GAME_COULD_NOT_BE_USED_ERROR,"该促销活动不可以使用");
			}
			
			// 赠送活动ID
			Long present_coupon_id = null;
			
			//促销活动的满减  等满减活动规则出来(需要校验)
			if(productGame.getBizType()==4){
				// 活动规则JSON字符串
				String regulationJson = productGame.getGameRegulation();
				
				if(StringUtils.isEmpty(regulationJson)){
					throw new ServiceException(TradeAddErrorCode.GAMEREGULATION_IS_NULL_ERROR,"满减活动规则为空");
				}
				
				// 转换后的map对象
				List<Map<String,Object>> regulaList = toMapsFromJson(regulationJson);
				
				if(CollectionUtils.isEmpty(regulaList)){
					throw new ServiceException(TradeAddErrorCode.GAMEREGULATION_IS_NULL_ERROR,"满减活动规则为空");
				}
				
				/** 根据下标index 获取符合条件满减记录  **/
				int index = 0;
				BigDecimal m_indexAmount = BigDecimal.ZERO;
				
				for(int i = 0;i<regulaList.size();i++){
					Map<String,Object> regulaMap = regulaList.get(i);
					// 满金额
					BigDecimal m_totalamount = BigDecimal.valueOf(Double.valueOf(regulaMap.get("total_amount").toString()));
					if(totalAmount.compareTo(m_totalamount) >= 0 && m_totalamount.compareTo(m_indexAmount) >= 0){
						index = i;
						m_indexAmount = m_totalamount;
					}
				}

				/** 满减校验 **/
				Map<String,Object> regulaMap = regulaList.get(index);
				BigDecimal subtractt_amount = BigDecimal.valueOf(Double.valueOf(regulaMap.get("subtractt_amount").toString()));
				if(subtractt_amount.compareTo(discount_amount) != 0){
					throw new ServiceException(TradeAddErrorCode.GAME_FULL_CUT_INCONSISTENT_ERROR,"满减金额不符");
				}
				present_coupon_id = regulaMap.get("present_coupon_id") == null?null:Long.valueOf(regulaMap.get("present_coupon_id").toString());
				
			}else{
				throw new ServiceException(TradeAddErrorCode.GAME_CAN_NOT_WITH_DISCOUNT_ERROR,"参加了优惠促销活动，不能同时使用优惠券");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", PreferentialRecordType.GAME.getValue());
			map.put("discount_amount", discount_amount);
			map.put("product_ids", product_game_ids);
			map.put("fk_order_id", order_id);
			map.put("fk_id", order_game_id);
			map.put("present_coupon_id",present_coupon_id);
			map.put("create_time", new Date());
			tradeDao.insertPreferentialRecord(map);
		}
	}
	
	/**
	 * 将json字符串 转换 为 Map集合对象
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月14日 下午3:49:33 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> toMapsFromJson(String json){
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		try {
			Object[] arrayMaps = RestUtil.fromJson(json, Object[].class);
			if(!ArrayUtils.isEmpty(arrayMaps)){
				for(Object o : arrayMaps){
					Map<String,Object> map = (Map<String, Object>) o;
					mapList.add(map);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return mapList;
	}
	
	/**
	 * 订单店铺优惠校验
	 * @CreateDate : 2015年10月14日 下午1:13:20 
	 * @param couponId 			优惠卷ID
	 * @param productIds		优惠产品IDs
	 * @param discountAmount 	需要校验的优惠价格
	 * @param totalAmount		产品实付价汇总
	 * @param order_id			订单ID
	 * @throws Exception
	 */
	public void checkCoupon(Long couponId, String productIds,
			BigDecimal discountAmount, BigDecimal totalAmount, Long order_id,Long trade_id,
			AppOrderParam orderParam,
			AppTradeParam tradeParam) {
		
		//Long buy_store_id = orderParam.getBuyer_store_id();
		Long memberId = tradeParam.getMember_id();
		if(couponId != null && couponId != 0l){
			
			log.info("校验优惠券"+couponId);
			
			StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(couponId);
			if(storeCoupon == null || storeCoupon.getId() == null || storeCoupon.getId() == 0l){
				throw new ServiceException(TradeAddErrorCode.COUPON_NOT_EXIST_ERROR,"没有找到优惠券id"+couponId+"的优惠券");
			}
			if(storeCoupon.getState() != 0 && storeCoupon.getState() != 1){
				throw new ServiceException(TradeAddErrorCode.COUPON_NOT_AVAILABLE_ERROR,"优惠券id为"+couponId+"的优惠券无法使用");
			}
			if(!memberId.equals(storeCoupon.getFkMemberId())){
				throw new ServiceException(TradeAddErrorCode.COUPON_NOT_AVAILABLE_ERROR,"该优惠券id"+couponId+"不属于用户ID"+memberId);
			}
			
			// 优惠券类型
			Integer couponType = storeCoupon.getCouponType();
			
			orderParam.setCouponType(couponType);
			
			BigDecimal subtracttAmount = storeCoupon.getSubtracttAmount() == null?BigDecimal.ZERO:storeCoupon.getSubtracttAmount();
			
			if(couponType==1){ //代金券total_amount  总金额减
				if(storeCoupon.getTotalAmount().compareTo(BigDecimal.ZERO) == 0){
					if(subtracttAmount.compareTo(discountAmount) != 0){
						throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					}
				}else{
					if(totalAmount.compareTo(storeCoupon.getTotalAmount()) >=0 ){
						if(subtracttAmount.compareTo(discountAmount) != 0){
							throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
						}
					}else{
						throw new ServiceException(TradeAddErrorCode.COUPON_CONDITION_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券在该订单中无法满足使用条件");
					}
				}
			}else if(couponType==2){//邮资卡
				
				// 邮费金额
				BigDecimal freight_amount = orderParam.getFreight_amount() == null?BigDecimal.ZERO:orderParam.getFreight_amount();
				
				// 邮资卡小于等运费 
				if(subtracttAmount.compareTo(freight_amount) <= 0){
					if(subtracttAmount.compareTo(discountAmount) != 0){
						throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					}
				}else{
					// 优惠金额需要等运费
					if(discountAmount.compareTo(freight_amount) != 0){
						throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					}
				}
				
			}else if(couponType==3){//3(指定产品减少价格
				if(subtracttAmount.compareTo(discountAmount) != 0){
					throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
				}
			}else if(couponType==4){//4(创业基金)创业折扣率
				// 剩余金额
				BigDecimal leavingAmount = storeCoupon.getCouponLeavingAmount();
				// 优惠金额
				BigDecimal discountRateAmount = totalAmount.multiply(storeCoupon.getDiscountRate()).divide(BigDecimal.valueOf(100));
				
				 if(leavingAmount == null){
					 throw new ServiceException("创业基金为空，请配置");
				 }
				 // 优惠金额 大于 剩余金额 则取剩余金额
				 if(discountRateAmount.compareTo(leavingAmount) > 0){
					 if(leavingAmount.compareTo(discountAmount) != 0){
						 throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					 }
				 }else{
					 if(discountRateAmount.compareTo(discountAmount) != 0){
						 throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					 }
				 }
				 
				 if(leavingAmount.compareTo(discountAmount)<0){
					 throw new ServiceException(TradeAddErrorCode.COUPON_LEAVING_AMOUNT_NOT_SUFFICIENT_FUNDS_ERROR,"该创业余额不足");
				 }
				 storeCoupon.setCouponLeavingAmount(leavingAmount.subtract(discountAmount));
			}else if(couponType==5){//5(优惠码）满多少减多少
				if(storeCoupon.getTotalAmount().compareTo(BigDecimal.ZERO) == 0){
					if(subtracttAmount.compareTo(discountAmount) != 0){
						throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					}
				}else{
					if(totalAmount.compareTo(storeCoupon.getTotalAmount()) >=0 ){
						if(subtracttAmount.compareTo(discountAmount) != 0){
							throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
						}
					}else{
						throw new ServiceException(TradeAddErrorCode.COUPON_DISCOUNT_INCONSISTENT_ERROR,"优惠券id为"+couponId+"的优惠券优惠券金额不符");
					}
				}
			}else{
				throw new ServiceException(TradeAddErrorCode.COUPON_COUPONTYPE_ERROR,"优惠券id为"+couponId+"的优惠券类型有误");
			}
			
			// 如果是创业基金 需要保存记录到 store_coupon_record 店铺优惠记录表中
			if(couponType==4){
				StoreCouponRecord sCrecord = new StoreCouponRecord();
				// 店铺优惠劵id
				sCrecord.setFkStoreCouponId(couponId);
				// 会员ID
				sCrecord.setFkMemberId(tradeParam.getMember_id());
				// 店铺ID 
				sCrecord.setFkStoreId(tradeParam.getStore_id());
				// 交易单ID
				sCrecord.setFkTradeId(trade_id);
				// 商户ID
				sCrecord.setFkSupplierId(orderParam.getSupplier_id());
				// 商户店铺ID
				sCrecord.setFkSupplierStoreId(orderParam.getSeller_store_id());
				// 优惠劵金额，优惠劵的面值总金额
				sCrecord.setCouponAmount(discountAmount);
				// 优惠劵剩余金额
				sCrecord.setCouponLeavingAmount(storeCoupon.getCouponLeavingAmount());
				// 操作类型
				sCrecord.setOpType("减优惠券");
				sCrecord.setOpTime(DateUtil.now());
				
				storeCouponRecordMapper.insertSelective(sCrecord);
			}
			
			// 如果是  coupontype = 4 创业基金 且coupon_leaving_amount剩余金额 > 0,设置state = 1 使用中
			if(couponType==4 && storeCoupon.getCouponLeavingAmount().compareTo(BigDecimal.ZERO) > 0){
				storeCoupon.setState(1);
			}else{
				storeCoupon.setState(3);
			}
			
			// 如果第一次使用该优惠券,则更新 优惠券类型使用人数+1
			if(this.isFirstUse(storeCoupon)){
				OmsCouponType omsCtype = this.omsCouponTypeMapper.selectByPrimaryKey(storeCoupon.getFkCouponTypeId());
				if(omsCtype != null){
					Integer useNum = omsCtype.getUseNum() == null ? 0:omsCtype.getUseNum();
					useNum++;
					omsCtype.setUseNum(useNum);
					this.omsCouponTypeMapper.updateByPrimaryKeySelective(omsCtype);
				}
			}
			
			storeCouponMapper.updateByPrimaryKeySelective(storeCoupon);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", PreferentialRecordType.COUPON.getValue());
			map.put("discount_amount", discountAmount);
			map.put("product_ids", productIds);
			map.put("fk_order_id", order_id);
			map.put("fk_id", couponId);
			map.put("create_time", new Date());
			tradeDao.insertPreferentialRecord(map);
		}
	}
	
	/**
	 * 下单校验并  减除产品库存
	 * 库存更新有加锁
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月14日 下午1:11:18 
	 * @param productId
	 * @param productSkuId
	 * @param amount
	 */
	private void checkProductSkuStock(Long productId,Long productSkuId,Integer amount){
		
		Product product = this.tradeDao.selectGdsProductByPrimaryKeyForUpdate(productId);
		if(product != null){
			
			// 验证是否下架
			if(!ProductState.上架.getKey().equals(product.getState())){
				throw new ServiceException("产品ID："+productId+" 失效,可能已下架");
			}
			
			// 验证是否未审核通过
			if(!AuditState.审核通过.getKey().equals(product.getAuditState())){
				throw new ServiceException("产品ID："+productId+" 失效,未审核通过");
			}
			
			// 商品库存数量
			Integer product_stock = product.getProductStock() == null?0:product.getProductStock();
			
			if(product_stock < 0){
				throw new ServiceException("产品ID"+productId+"库存有误");
			}
			
			if(product_stock < amount){
				throw new ServiceException("库存不足,产品ID"+productId+"库存为("+product_stock+")");
			}
			
			product.setProductStock(product_stock - amount);
			
			this.productMapper.updateByPrimaryKey(product);
			
		}
		
		ProductSku productSku = this.tradeDao.selectGdsProductSkuByPrimaryKeyForUpdate(productSkuId);
		if(productSku != null){
			
			if(!CommonState.NORMAL.getValue().equals(productSku.getState())){
				throw new ServiceException("产品skuID"+productSkuId+"已失效");
			}
			
			// 商品库存数量
			Integer product_stock = productSku.getProductStock() == null?0:productSku.getProductStock();
			
			if(product_stock < 0){
				throw new ServiceException("产品skuID"+productSkuId+"库存有误");
			}
			
			if(product_stock < amount){
				throw new ServiceException("库存不足,产品skuID"+productSkuId+"库存为("+product_stock+")");
			}
			
			productSku.setProductStock(product_stock - amount);
			
			this.productSkuMapper.updateByPrimaryKey(productSku);
		}
	}
	
	
	/**
	 * wap下单(代销单、自营单 )
	 */
	@Override
	public TradeResult wapAdd(WapTradeParam params) {
		
		/** 最终返回的 订单ID、订单编号  **/
		String rOrderNo = null;
		Long rOrderId = null;
		
		OrderType orderType = OrderType.getValue(params.getTrade_type());
		
		// 检验用户
		checkMember(params.getMember_id());
				
		Trade trade = new Trade();//交易信息
		trade.setId(SEQGenerate.getId());
		
		// 标题，用于保存订单产品名称，如果一个以上，直接在第一个产品名称后面加等等就好。
		String tradeTitle = null;
		
		OrderReceiver orderReceiver = new OrderReceiver();//收货物流信息
		orderReceiver.setId(SEQGenerate.getId());
		orderReceiver.setCreateUserId(SessionUtils.getMember_id());
		orderReceiver.setCreateTime(new Date());
		orderReceiver.setFkTradeId(trade.getId());
		orderReceiver.setReceiverMobile(params.getReceiver_mobile());
		orderReceiver.setReceiverName(params.getReceiver_name());
		orderReceiver.setReceiverPhoneExt(params.getReceiver_mobile());
		orderReceiver.setReceiverPhoneNumber(params.getReceiver_mobile());
		orderReceiver.setReceiverZipCode(params.getReceiver_zip_code());
		orderReceiver.setReceiverAddress(params.getReceiver_address());
		orderReceiver.setReceiverAreaCcode(params.getReceiver_area_ccode());
		if(params.getReceiver_area_ccode() != null){
			orderReceiver.setAddressRegionName(valueRedisTemplate.get(params.getReceiver_area_ccode()));
		}
		
		BigDecimal tradeAppDiscountTotalAmount = BigDecimal.ZERO;	//平台优惠
		BigDecimal tradeFreightTotalAmount = BigDecimal.ZERO;  		//邮费
		BigDecimal tradeOrderDiscountTotalAmount=  BigDecimal.ZERO;	//订单优惠总金额
		BigDecimal tradeTotalAmount = BigDecimal.ZERO; 				//订单实付总金额
		BigDecimal tradeProductTotalAmount = BigDecimal.ZERO; 		//产品总 金额
		BigDecimal tradePayAmount = BigDecimal.ZERO; 				//交易单实付总金额
		
		if(params.getReceiver_type().equals(0)){//收货方式(0：自提，1：物流快递)
			if(params.getWarehouse_id() != null && params.getWarehouse_id() != 0l){
				SupplierWarehouse supplierWarehouse = supplierWarehouseMapper.selectByPrimaryKey(params.getWarehouse_id());
				if(supplierWarehouse == null || supplierWarehouse.getId() == null || supplierWarehouse.getId() == 0l || supplierWarehouse.getState() != 50){
					throw new ServiceException(TradeAddErrorCode.RECEIVE_WAREHOURE_IS_NULL_ERROR,"收货方式为自提的时候没找到该自提点");
				}
				orderReceiver.setPickupplaceBusinessTime(supplierWarehouse.getOpeningTime());
				orderReceiver.setPickupplaceContactName(supplierWarehouse.getTel());
				orderReceiver.setReceiverAddress(supplierWarehouse.getAddress());
				orderReceiver.setAddressRegionName(supplierWarehouse.getAreaCcodeName());
				orderReceiver.setPickupplacePhoneArea(null);
				orderReceiver.setPickupplaceContactName(supplierWarehouse.getLinkman());
				orderReceiver.setPickupplacePhoneNumber(supplierWarehouse.getMoblie());
			}else{
				throw new ServiceException(TradeAddErrorCode.RECEIVE_WAREHOURE_ID_ID_NOT_NULL_ERROR,"收货方式为自提的时候必须选择自提点");
			}
			
		}else if(params.getReceiver_type().equals(1)){
			
		}
		//orderReceiver.setAddressRegionName(params.getReceiver_address());//代码的地址
		orderReceiver.setReceiverAreaCcode(params.getReceiver_area_ccode());
		orderReceiver.setFkTradeId(trade.getId());
		
		List<Order> orders = new ArrayList<Order>();//订单信息
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();//订单明细信息
		
//		OrderType orderType = OrderType.getValue(params.getOrder_type());
		List<WapOrderParam> orderParams = params.getOrders();
		if(orderParams != null && !orderParams.isEmpty()){
			for (WapOrderParam orderParam : orderParams) {
				Order order =new Order();//订单信息
				orders.add(order);
				order.setId(SEQGenerate.getId());
				order.setFkTradeId(trade.getId());
				
				List<com.jbh360.trade.vo.param.WapProduct> productParams = orderParam.getProducts();
				
				
				/************************************订单明细计算开始********************************************************************/
				/**********************************************************************************************************************/
				/**********************************************************************************************************************/
				/**********************************************************************************************************************/
				// 订单优惠金额  = 私密折扣价
				BigDecimal order_discount_aAmount = BigDecimal.ZERO;
				
				// 商品优惠价
				BigDecimal orderGoodsDiscountAmount = BigDecimal.ZERO;
				
				// 商品总价
				BigDecimal orderGoodsTotalAmount = BigDecimal.ZERO;
				
				// 邮费优惠
				BigDecimal orderFreightDiscountAmount = BigDecimal.ZERO;
				
				// 订单邮费
				BigDecimal orderFreightAmount = orderParam.getFreight_amount()==null?BigDecimal.ZERO:orderParam.getFreight_amount();
				
				
				if(productParams != null && !productParams.isEmpty()){
					for (com.jbh360.trade.vo.param.WapProduct productParam : productParams) {
						OrderDetail orderDetail = new OrderDetail();
						orderDetails.add(orderDetail);
						orderDetail.setId(SEQGenerate.getId());
						orderDetail.setFkTradeId(trade.getId());
						orderDetail.setFkOrderId(order.getId());
						orderDetail.setFkProductId(productParam.getProduct_id());
						orderDetail.setSkuBarcode(productParam.getProduct_sku_barcode());
						orderDetail.setFkStoreGoodsId(productParam.getStore_goods_id());
						orderDetail.setBuyCount(productParam.getBuy_count());
						orderDetail.setCreateTime(new Date());
						orderDetail.setExpressState(OrderExpressState.待发货.getKey());
						orderDetail.setServiceState(null);
						
						Product minimallProduct = productMapper.selectByPrimaryKey(productParam.getProduct_id());
						if(minimallProduct == null || minimallProduct.getId() == null || minimallProduct.getId() == 0l){
							throw new  ServiceException(TradeAddErrorCode.PRODUCT_NOT_EXIST_ERROR,"id为"+productParam.getProduct_id()+"的商品不存在");
						}
						orderDetail.setProductName(minimallProduct.getName());
						orderDetail.setProductLogoRsurl(minimallProduct.getLogoRsurl());
						
						ProductSkuExample example = new ProductSkuExample();
						example.createCriteria().andFkProductIdEqualTo(productParam.getProduct_id()).andProductSkuBarcodeEqualTo(productParam.getProduct_sku_barcode());
						List<ProductSku> productSkus = productSkuMapper.selectByExample(example);
						if(productSkus == null || productSkus.isEmpty()){
							throw new  ServiceException(TradeAddErrorCode.PRODUCT_SKU_NOT_EXIST_ERROR,"product_sku_barcode为"+productParam.getProduct_sku_barcode()+"的商品不存在");
						}
						ProductSku productSku = productSkus.get(0);
						orderDetail.setFkProductHistoryId(productSku.getCurrentHistoryId());
						orderDetail.setFkProductHistorySkuId(productSku.getId());
						orderDetail.setSkuPropertyValue(productSku.getSkuProperties());
						
						
						orderDetail.setProductVersionNo(productSku.getCurrentHistoryVersion());
						orderDetail.setOuterSkuCode(productSku.getOuterSkuId());
						orderDetail.setSaleUnitPrice(productSku.getSettlementPrice());	//代销单：店铺销售单价；自营单:店铺销售单价(取自产品表的结算价)
						
						/**
						 * 校验更新库存
						 */
						checkProductSkuStock(productParam.getProduct_id(), productSku.getId(), productParam.getBuy_count());
						
						if(orderType.equals(OrderType.代销单)){
							// 设置是否七天退货
							order.setIsSupportSellService(getSupportReturnType(orderParam.getSeller_store_id()));
							
							orderDetail.setSettleUnitPrice(productSku.getSettlementPrice());
							/**
							 * 私密折扣
							 */
							
							log.info("商品BarCode "+productParam.getProduct_sku_barcode());
							
							StoreGoodsSkuExample storeGoodsSkuExample = new StoreGoodsSkuExample();
							storeGoodsSkuExample.createCriteria()
								.andStateEqualTo(Constants.CommonState.NORMAL.getValue())
								.andProductSkuBarcodeEqualTo(productParam.getProduct_sku_barcode());
							List<StoreGoodsSku> storeGoodsSkus=storeGoodsSkuMapper.selectByExample(storeGoodsSkuExample);
							if(storeGoodsSkus==null || storeGoodsSkus.isEmpty() || storeGoodsSkus.size() != 1){
								throw new ServiceException(TradeAddErrorCode.STOREGOODS_IS_NULL_ERROR,"没找到该店铺的商品");
							}
							
							log.info("代销商品ID "+storeGoodsSkus.get(0).getId());
							
							orderDetail.setFkPromotionId(productParam.getPromotion_id());
							//  销售单价
							orderDetail.setSaleUnitPrice(storeGoodsSkus.get(0).getStoreSalePrice());
							if(productParam.getPromotion_id() != null && productParam.getPromotion_id() != 0l){
								
								StorePromotion storePromotion = storePromotionMapper.selectByPrimaryKey(productParam.getPromotion_id());
							
								if(storePromotion.getEffectStartTime().after(new Date())){
									throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_NOT_BEGIN_ERROR,"该折扣优惠未开始");
								}else if(storePromotion.getEffectEndTime().before(new Date())){
									throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_OVER_ERROR,"该折扣优惠已结束");
								}
								
								if(storePromotion.getDiscountType()==1){
									orderDetail.setDealUnitPrice(storeGoodsSkus.get(0).getStoreSalePrice().multiply(storePromotion.getDiscountContent().divide(BigDecimal.valueOf(100))));
									order_discount_aAmount.add(getPromotionDiscountAmount(storeGoodsSkus.get(0).getStoreSalePrice(), storePromotion));
								}else if(storePromotion.getDiscountType()==2){
									orderDetail.setDealUnitPrice(storeGoodsSkus.get(0).getStoreSalePrice().subtract(storePromotion.getDiscountContent()));
									order_discount_aAmount.add(storePromotion.getDiscountContent());
								}
							}else{
								orderDetail.setDealUnitPrice(storeGoodsSkus.get(0).getStoreSalePrice());
							}
							orderDetail.setGoodsDiscountAmount(orderDetail.getSaleUnitPrice().subtract(orderDetail.getDealUnitPrice()).multiply(BigDecimal.valueOf(orderDetail.getBuyCount())));
							orderDetail.setOrderDiscountShareAmount(BigDecimal.ZERO);
							orderDetail.setPaymentAmount(orderDetail.getDealUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getBuyCount())).subtract(orderDetail.getOrderDiscountShareAmount()));
						}else if(orderType.equals(OrderType.自营单)){
							// 设置是否七天退货
							order.setIsSupportSellService(getSupportReturnType(orderParam.getBuyer_store_id()));
							/**
							 * 私密折扣
							 */
							orderDetail.setFkPromotionId(productParam.getPromotion_id());
							if(productParam.getPromotion_id() != null && productParam.getPromotion_id() != 0l){
								StorePromotion storePromotion = storePromotionMapper.selectByPrimaryKey(productParam.getPromotion_id());
								
								if(storePromotion.getEffectStartTime().after(new Date())){
									throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_NOT_BEGIN_ERROR,"该折扣优惠未开始");
								}else if(storePromotion.getEffectEndTime().before(new Date())){
									throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_OVER_ERROR,"该折扣优惠已结束");
								}
								
								if(storePromotion.getDiscountType()==1){
									orderDetail.setDealUnitPrice(productSku.getRecommendPrice().multiply(storePromotion.getDiscountContent().divide(BigDecimal.valueOf(100))));
									order_discount_aAmount.add(getPromotionDiscountAmount(productSku.getRecommendPrice(), storePromotion));
								}else if(storePromotion.getDiscountType()==2){
									orderDetail.setDealUnitPrice(productSku.getRecommendPrice().subtract(storePromotion.getDiscountContent()));
									order_discount_aAmount.add(storePromotion.getDiscountContent());
								}
							}else{
								orderDetail.setDealUnitPrice(productSku.getSettlementPrice());
							}
						
							orderDetail.setGoodsDiscountAmount(orderDetail.getSaleUnitPrice().subtract(orderDetail.getDealUnitPrice()).multiply(BigDecimal.valueOf(orderDetail.getBuyCount())));
							orderDetail.setOrderDiscountShareAmount(BigDecimal.ZERO);
							orderDetail.setPaymentAmount(orderDetail.getDealUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getBuyCount())).subtract(orderDetail.getOrderDiscountShareAmount()));
						}else{
							throw new ServiceException("订单类型trade_type错误，只能是代销单(2)或自营单(3)");
						}
					 
						log.info("商品ID "+orderDetail.getFkProductId()+"销售单价为："+orderDetail.getSaleUnitPrice()); 
						log.info("商品ID "+orderDetail.getFkProductId()+"成交单价为："+orderDetail.getDealUnitPrice()); 
						
						orderDetail.setPaymentAmount(orderDetail.getDealUnitPrice().multiply(BigDecimal.valueOf(orderDetail.getBuyCount())));
						
						orderDetail.setGoodsDiscountAmount(BigDecimal.ZERO);
						
						orderGoodsDiscountAmount = orderGoodsDiscountAmount.add(orderDetail.getGoodsDiscountAmount());
						
						log.info("商品ID"+orderDetail.getFkProductId()+"支付价为："+orderDetail.getPaymentAmount());
						
						orderGoodsTotalAmount = orderGoodsTotalAmount.add(orderDetail.getPaymentAmount());
						
					}
				}
				
				/************************************订单明细计算结束********************************************************************/
				
				
				/****
				 * 开始处理订单
				 */
				order.setBuyerRemark(orderParam.getBuyer_remark());
				order.setIsFreeze((short)0);
				order.setCreateTime(new Date());
				order.setCreateUserId(SessionUtils.getMember_id());
				order.setDealType((short)2);//及时到账
				StoreInfo  buerStoreInfo = storeInfoMapper.selectByPrimaryKey(orderParam.getBuyer_store_id());
				if(buerStoreInfo != null && buerStoreInfo.getId() != 0l && buerStoreInfo.getIsGuarantee() != null){
					if(buerStoreInfo.getIsGuarantee().equals((short)1)){
						order.setDealType((short)1);//是担保
					}
				}
				order.setOrderType(orderType.getKey());
//				order.setEndTime(new Timestamp(System.currentTimeMillis()+7*24*60*60*1000));
				order.setStartTime(DateUtil.now());
				order.setExtendSingTime(null);	
				order.setFkOrderReceiverId(orderReceiver.getId());
				
				setWapUserInfo(params, orderType, orderParam, order);
				
				order.setStateCode("wait_buyer_pay");
				order.setStateName("待付款");
				order.setCreateUserId(SessionUtils.getMember_id());
				order.setCreateTime(new Date());
				order.setOrderNo(NoUtil.getOrderNo("minimall-order-no"));
				order.setReceiverType(params.getReceiver_type());
				
				
				/**
				 * 私密折扣
				 */
				order.setFkPromotionId(orderParam.getPromotion_id());
				if(orderParam.getPromotion_id() != null && orderParam.getPromotion_id() != 0l){
					StorePromotion storePromotion = storePromotionMapper.selectByPrimaryKey(orderParam.getPromotion_id());
					
					if(storePromotion.getEffectStartTime().after(new Date())){
						throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_NOT_BEGIN_ERROR,"该折扣优惠未开始");
					}else if(storePromotion.getEffectEndTime().before(new Date())){
						throw new ServiceException(TradeAddErrorCode.PROMOTION_TIME_IS_OVER_ERROR,"该折扣优惠已结束");
					}
					
					if(storePromotion.getDiscountType()==1){
						//order_discount_aAmount.add(order.getOrderGoodsTotalAmount().subtract(order.getOrderGoodsTotalAmount().multiply(storePromotion.getDiscountContent()).divide(BigDecimal.valueOf(100))));
						order_discount_aAmount.add(getPromotionDiscountAmount(order.getOrderGoodsTotalAmount(), storePromotion));
					}else if(storePromotion.getDiscountType()==2){
						order_discount_aAmount.add(storePromotion.getDiscountContent());
					}
				}
				
				
				// 订单邮费
				order.setOrderFreightAmount(orderFreightAmount);
				
				// 运费优惠
				order.setFreightDiscountAmount(orderFreightDiscountAmount);
				
				// 改价优惠
				order.setChangePriceAmount(BigDecimal.ZERO);
				
				order.setOrderTotalPayLossAmount(BigDecimal.ZERO);
				
				// 商品优惠
				order.setGoodsDiscountAmount(orderGoodsDiscountAmount);
				
				// 商品总金额
				order.setOrderGoodsTotalAmount(orderGoodsTotalAmount);
				
				// 优惠金额 = 私密折扣
				order.setOrderDiscountAmount(order_discount_aAmount);
				
				// 优惠总金额
				order.setOrderDiscountTotalAmount(getOrderDiscountTotalAmount(order));
				
				// 订单总额 = 商品总金额-优惠总金额
				order.setOrderTotalAmount(orderGoodsTotalAmount.subtract(order.getOrderDiscountTotalAmount()));
				
				// 订单支付额  = 订单总价 + 邮费
				order.setOrderPaymentAmount(getOrderPaymentAmounMethod(order));
				
				tradePayAmount = tradePayAmount.add(order.getOrderPaymentAmount());
				
				tradeProductTotalAmount = tradeProductTotalAmount.add(order.getOrderGoodsTotalAmount());
				tradeFreightTotalAmount = tradeFreightTotalAmount.add(order.getOrderFreightAmount());
				tradeOrderDiscountTotalAmount = tradeOrderDiscountTotalAmount.add(order.getOrderDiscountTotalAmount());
				tradeTotalAmount = tradeTotalAmount.add(order.getOrderTotalAmount());
				
				rOrderId = order.getId();
				rOrderNo = order.getOrderNo();
				
				createTradeOrderRecord(order);
				
			}
			
		}
		trade.setCreateTime(new Date());
		trade.setCreateUserId(SessionUtils.getMember_id());
		trade.setTradeNo(NoUtil.getOrderNo("minimall-trade-no"));
		trade.setPayNo(NoUtil.getOrderNo("minimall-pay-no"));
		trade.setStateCode("wait_buyer_pay");
		trade.setStateName("待付款");
		trade.setPayState((short)0);
		
		// 平台优惠
		trade.setTradeDiscountTotalAmount(tradeAppDiscountTotalAmount);
		
		// 商品总价
		trade.setGoodsTotalAmount(tradeProductTotalAmount);
		
		// 邮费
		trade.setFreightTotalAmount(tradeFreightTotalAmount);
		
		// 订单优惠总额
		trade.setOrderDiscountTotalAmount(tradeOrderDiscountTotalAmount);
		
		// 优惠总额   ===  订单优惠总额  + 平台优惠总额
		trade.setDiscountTotalAmount(tradeOrderDiscountTotalAmount.add(trade.getTradeDiscountTotalAmount()));
		
		// 订单支付价
		trade.setTradeShouldAmount(tradePayAmount);
		
		trade.setTradeTotalPayLossAmount(BigDecimal.ZERO);
		trade.setFkMemberId(params.getMember_id());
		trade.setRecycleState((short)1);
		
		if(!CollectionUtils.isEmpty(orderDetails)){
			tradeTitle = orderDetails.get(0).getProductName();
			if(orderDetails.size() > 1){
				tradeTitle +=" 等等";
			}
			trade.setTitle(tradeTitle);
		}	
		
		/**
		 * 金额验证
		 */
		log.info("价格校验");
		if(trade.getTradeShouldAmount().compareTo(params.getTrade_total_amount()) != 0){
			String errorstr = "实付总金额不符,"+"前端计算："+params.getTrade_total_amount()+" 后端计算："+trade.getTradeShouldAmount();
			log.error(errorstr);
			throw new ServiceException(errorstr,TradeAddErrorCode.PAP_AMOUONT_INCONSISTENT_ERROR);
		}
		
		
		if(tradeMapper.insertSelective(trade) > 0){
			for (Order order : orders) {
				orderMapper.insertSelective(order);
			}
			for (OrderDetail orderDetail : orderDetails) {
				orderDetailMapper.insertSelective(orderDetail);
			}
			orderReceiverMapper.insertSelective(orderReceiver);
			deleteShopCard(orderParams);
		}
		
		return new TradeResult(trade.getId(), trade.getTradeNo(),rOrderId,rOrderNo);
	}


	/**
	 * 保存用户相关信息(wap)
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月7日 下午2:17:43 
	 * @param params
	 * @param orderType
	 * @param orderParam
	 * @param order
	 */
	private void setWapUserInfo(WapTradeParam params, OrderType orderType,WapOrderParam orderParam, Order order) {
		// 用户
		Member cusmember = this.memberMapper.selectByPrimaryKey(params.getMember_id());
		// 掌柜
		Member buymember = this.memberMapper.selectByPrimaryKey(orderParam.getBuye_store_member_id());
		// 掌柜店铺
		StoreInfo buystoreinfo = this.storeInfoMapper.selectByPrimaryKey(orderParam.getBuyer_store_id());
		
		
		if(cusmember == null){
			throw new ServiceException("用户ID"+params.getMember_id() + "不存在");
		}
		if(buymember == null){
			throw new ServiceException("掌柜ID"+orderParam.getBuye_store_member_id() + "不存在");
		}
		if(buystoreinfo == null){
			throw new ServiceException("掌柜店铺ID"+orderParam.getBuyer_store_id() + "不存在");
		}
		
		
		// 客户ID
		order.setFkCustomeMemberId(params.getMember_id());
		// 客户名称
		order.setCustomerMemberName(cusmember.getRealName());
		
		// 买方
		order.setFkBuyeStoreMemberId(orderParam.getBuye_store_member_id());
		order.setFkBuyerStoreId(orderParam.getBuyer_store_id());
		order.setBuyerStoreName(buystoreinfo.getStoreName());
		order.setBuyeStoreMemberName(buymember.getRealName());
		
		
		if(order.getFkCustomeMemberId().equals(order.getFkBuyeStoreMemberId())){
			throw new ServiceException("用户不能购买自己的商品",TradeAddErrorCode.USER_NOT_ALLOWED);
		}
		
		// 卖方(代销单才有)
		if(orderType.equals(OrderType.代销单)){
			
			if(orderParam.getSeller_member_id() == null || orderParam.getSeller_store_id() == null){
				throw new ServiceException("请输入商户信息");
			}
			
			Member sellermember = this.memberMapper.selectByPrimaryKey(orderParam.getSeller_member_id());
			
			StoreInfo sellerstoreinfo = this.storeInfoMapper.selectByPrimaryKey(orderParam.getSeller_store_id());
			
			if(sellermember == null){
				throw new ServiceException("商户ID"+orderParam.getSeller_member_id()+"不存在 ");
			}
			if(sellerstoreinfo == null){
				throw new ServiceException("商户店铺ID"+orderParam.getSeller_store_id()+"不存在 ");
			}
			
			order.setFkSellerMemberId(orderParam.getSeller_member_id());	// 商户ID
			order.setFkSellerStoreId(orderParam.getSeller_store_id());		// 商户店铺ID
			order.setFkSellerSupplierId(sellermember.getFkSupplierId());	// 主商户ID
			order.setSellerStoreName(sellerstoreinfo.getStoreName());		// 商户店铺名称
			order.setSellerMemberName(sellermember.getRealName());			// 商户名称
			
			if(order.getFkCustomeMemberId().equals(order.getFkSellerMemberId())){
				throw new ServiceException("用户不能购买自己的商品",TradeAddErrorCode.USER_NOT_ALLOWED);
			}
		}
	}


	/**
	 * 获取私密折扣优惠价 = 商品价(订单总价) * (1-折扣率)
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月16日 下午5:05:01 
	 * @param totalAmount 商品价 或者订单商品总价
	 * @param storePromotion 私密折扣
	 * @return
	 */
	private BigDecimal getPromotionDiscountAmount(BigDecimal totalAmount,StorePromotion storePromotion) {
		BigDecimal discountContentTotal = BigDecimal.valueOf(1);
		BigDecimal discountContent = storePromotion.getDiscountContent().divide(BigDecimal.valueOf(100));
		return totalAmount.multiply(discountContentTotal.subtract(discountContent));
	}


	/**
	 * 删除购物车
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月15日 下午3:11:24 
	 * @param orderParams
	 */
	private void deleteShopCard(List<WapOrderParam> orderParams) {
		if(!CollectionUtils.isEmpty(orderParams)){
			for(WapOrderParam orderParam : orderParams){
				Long[] cardIds = orderParam.getCart_ids();
				if(cardIds != null && cardIds.length > 0){
					for(Long cardId : cardIds){
						ShopCart record = new ShopCart();
						record.setId(cardId);
						record.setState(Constants.CommonState.DELETE.getValue());
						this.shopCartMapper.updateByPrimaryKeySelective(record);
					}
				}
			}
		}
	}
	
	
	/**
	 * 根据店铺拍判断：
	 * 是否支持7天无理由退换货（0：不支持，1：支持）
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月2日 上午11:23:20 
	 * @param storeId
	 * @return
	 */
	private Short getSupportReturnType(Long storeId){
		StoreInfo storeInfo = this.storeInfoMapper.selectByPrimaryKey(storeId); 
		Short isReturn = 0;
		if(storeInfo != null && storeInfo.getIsReturn() != null){
			isReturn = storeInfo.getIsReturn();
		}
		return isReturn;
	}
	
	/**
	 * 判断是否第一次用指定优惠券(根据优惠券类型和用户ID查询，状态为：(1已使用，3使用完)
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月6日 下午3:50:35 
	 * @param storeCoupon
	 * @return
	 */
	private boolean isFirstUse(StoreCoupon storeCoupon){
		// 是否第一次使用 默认否
		boolean isFirstUseFlag = false;
		// 用户ID
		Long memberId = storeCoupon.getFkMemberId();
		// 微商城优惠劵类型id
		Long couponTypeId = storeCoupon.getFkCouponTypeId();
		
		StoreCouponExample example = new StoreCouponExample();
		List<Integer> values = new ArrayList<Integer>();
		values.add(1);
		values.add(3);
		
		example.createCriteria()
			.andStateIn(values)
			.andFkMemberIdEqualTo(memberId)
			.andFkCouponTypeIdEqualTo(couponTypeId);
		
		// 如果为空，为第一次使用
		List<StoreCoupon> ccs = this.storeCouponMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(ccs)){
			isFirstUseFlag =  true;
		}
		
		return isFirstUseFlag;
	}
	
	/**
	 * 创建订单操作记录
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月17日 下午2:05:42 
	 * @param order
	 */
	private void createTradeOrderRecord(Order order){
		TradeOrderRecord record = new TradeOrderRecord();
		record.setFkTradeId(order.getFkTradeId());
		record.setFkOrderId(order.getId());
		record.setOpType(Constants.TradeOrderRecordOpType.创建订单.getValue());
		record.setOpUserType(Constants.OrderServiceOpUserType.买家.getValue());
		record.setOpUserId(order.getCreateUserId());
		record.setOpUserName(getUserName(order.getCreateUserId()));
		record.setOpTime(DateUtil.now());
		record.setOpContent("订单创建");
		this.tradeOrderRecordMapper.insertSelective(record);
	}
	
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
	
}




