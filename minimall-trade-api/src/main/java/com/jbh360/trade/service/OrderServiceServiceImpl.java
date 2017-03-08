package com.jbh360.trade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbh360.common.base.ErrorResult;
import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.BeanUtil;
import com.jbh360.common.utils.Constants;
import com.jbh360.common.utils.Constants.OrderDetailServiceState;
import com.jbh360.common.utils.Constants.OrderExpressState;
import com.jbh360.common.utils.Constants.OrderPreSplitTranType;
import com.jbh360.common.utils.Constants.OrderServiceDealType;
import com.jbh360.common.utils.Constants.OrderServiceRefundReason;
import com.jbh360.common.utils.Constants.OrderServiceState;
import com.jbh360.common.utils.Constants.OrderServiceStateCode;
import com.jbh360.common.utils.Constants.OrderServiceWailtSendState;
import com.jbh360.common.utils.Constants.OrderStateCode;
import com.jbh360.common.utils.DateUtil;
import com.jbh360.common.utils.Page;
import com.jbh360.member.oms.entity.Member;
import com.jbh360.member.oms.mapper.MemberMapper;
import com.jbh360.system.entity.ExpressCompany;
import com.jbh360.system.mapper.ExpressCompanyMapper;
import com.jbh360.trade.dao.OrderServiceDao;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.entity.OrderDetail;
import com.jbh360.trade.entity.OrderDetailExample;
import com.jbh360.trade.entity.OrderService;
import com.jbh360.trade.entity.OrderServiceExample;
import com.jbh360.trade.entity.OrderServicePicture;
import com.jbh360.trade.entity.OrderServiceRecord;
import com.jbh360.trade.entity.Trade;
import com.jbh360.trade.entity.TradeOrderRecord;
import com.jbh360.trade.mapper.OrderDetailMapper;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.mapper.OrderServiceMapper;
import com.jbh360.trade.mapper.OrderServicePictureMapper;
import com.jbh360.trade.mapper.OrderServiceRecordMapper;
import com.jbh360.trade.mapper.TradeMapper;
import com.jbh360.trade.mapper.TradeOrderRecordMapper;
import com.jbh360.trade.vo.OrderPreSplitParams;
import com.jbh360.trade.vo.param.OrderServiceParams;
import com.jbh360.trade.vo.rs.OrderResult;
import com.jbh360.trade.vo.rs.OrderServicePictureResult;
import com.jbh360.trade.vo.rs.OrderServicePictureVo;
import com.jbh360.trade.vo.rs.OrderServiceRecordResult;
import com.jbh360.trade.vo.rs.OrderServiceRecordVo;
import com.jbh360.trade.vo.rs.OrderServiceRefundReasonType;
import com.jbh360.trade.vo.rs.OrderServiceResult;
import com.jbh360.trade.vo.rs.OrderServiceVo;

/**
 * 
 * @Title: OrderServiceServiceImpl.java
 * @Package com.jbh360.trade.service
 * @ClassName: OrderServiceServiceImpl
 * @Description: 售后单
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月31日 下午3:46:14
 * @version V3.0
 */
@Service
public class OrderServiceServiceImpl{
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    
    @Autowired
    private TradeMapper tradeMapper;
    
    
    @Autowired
    private OrderServiceMapper orderServiceMapper;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    
    @Autowired
    private OrderServicePictureMapper orderServicePictureMapper;
	
	@Autowired
    private OrderServiceRecordMapper orderServiceRecordMapper;
	
	@Autowired
    private OrderServiceDao orderServiceDao;
	
	
	@Autowired
    private TradeOrderRecordMapper tradeOrderRecordMapper;
	
	private Logger log = Logger.getLogger(OrderServiceServiceImpl.class);
	
	@Autowired
	private OrderPreSplitServiceImpl orderPreSplitServiceImpl;
    
	/**
	 * 退款处理方式
	 * @author 揭懿
	 * @param params
	 * @return
	 */
    public List<com.jbh360.trade.vo.rs.OrderServiceDealType> getOrderServiceDealType(OrderServiceParams params){
    	List<com.jbh360.trade.vo.rs.OrderServiceDealType> orderServiceDealTypeList = new ArrayList<com.jbh360.trade.vo.rs.OrderServiceDealType>();
    	Order order = orderMapper.selectByPrimaryKey(params.getFk_order_id());
    	if(null != order){
    		if(null != order.getStateCode()){
    			if(order.getStateCode().equals(Constants.OrderStateCode.待收货.getKey())){
    				return getOrderServiceDealTypeList();
    			}else if(order.getStateCode().equals(Constants.OrderStateCode.待发货.getKey())){
    				com.jbh360.trade.vo.rs.OrderServiceDealType orderServiceDealType = new com.jbh360.trade.vo.rs.OrderServiceDealType();
    	    		orderServiceDealType.setDeal_type(Constants.OrderServiceDealType.我要退款但不退货.getKey());
    	    		orderServiceDealType.setDeal_type_name(Constants.OrderServiceDealType.我要退款但不退货.getValue());
    	    		orderServiceDealTypeList.add(orderServiceDealType);
    	    		return orderServiceDealTypeList;
    			}
    		}
    	}
    	return getOrderServiceDealTypeList();
    }
    
    /**
     * 退款处理方式
     * @author 揭懿
     * @return
     */
    public List<com.jbh360.trade.vo.rs.OrderServiceDealType> getOrderServiceDealTypeList(){
    	List<com.jbh360.trade.vo.rs.OrderServiceDealType> orderServiceDealTypeList = new ArrayList<com.jbh360.trade.vo.rs.OrderServiceDealType>();
    	OrderServiceDealType[] types = Constants.OrderServiceDealType.values();
    	for(OrderServiceDealType type:types){
    		com.jbh360.trade.vo.rs.OrderServiceDealType orderServiceDealType = new com.jbh360.trade.vo.rs.OrderServiceDealType();
    		orderServiceDealType.setDeal_type(type.getKey());
    		orderServiceDealType.setDeal_type_name(type.getValue());
    		orderServiceDealTypeList.add(orderServiceDealType);
    	}
    	return orderServiceDealTypeList;
    }
    
    /**
     * 退款原因
     * @author 揭懿
     * @return
     */
    public List<OrderServiceRefundReasonType> getOrderServiceRefundReasonType(){
    	List<OrderServiceRefundReasonType> orderServiceRefundReasonTypeList = new ArrayList<OrderServiceRefundReasonType>();
    	OrderServiceRefundReason[] types = Constants.OrderServiceRefundReason.values();
    	
    	for(OrderServiceRefundReason type:types){
    		OrderServiceRefundReasonType orderServiceRefundReasonType = new OrderServiceRefundReasonType();
    		orderServiceRefundReasonType.setRefund_reason_type_id(type.getKey());
    		orderServiceRefundReasonType.setRefund_reason_type_name(type.getValue());
    		orderServiceRefundReasonTypeList.add(orderServiceRefundReasonType);
    	}
    	
    	return orderServiceRefundReasonTypeList;
    }
    
    /**
     * 申请售后：
     * 
     * 	1.待发货状态 只能做订单级别售后
     *  2.部分发货状态 只能做 商品级别售后 （因为退款退货类型不一样）
     *  3.待收货、交易完成（七天担保）两者都可
     * 
     * 	1. 订单级别：只传订单ID  2.产品级别：需要传明细ID(只传一个)
     *  2. 必须当前用户才能申请售后
     *  3. 是否支持七天无理由退货 ,售后许可条件：
     *  		     空---- 报错
     *  		     是 ---- 允许待发货、部分发货、待收货、交易完成(七天内)[订单完成时间+七天>=今天]. 
     *  		不是 ---- 只允许待发货、部分发货、待收货
     * 
     * @author : 揭懿
     * @CreateDate : 2015年11月14日 下午4:52:28 
     * @param params
     * @param error
     * @return
     */
    public OrderServiceResult apply(OrderServiceParams params,ErrorResult error){
    	
    	OrderServiceResult orderServiceResult = new OrderServiceResult();
    	
    	// 初始化售后对象
    	OrderService record = new OrderService();
    	
    	Trade trade = null;
    	Order order = null;
    	OrderDetail orderDetail = null;
    	OrderService orderService = null;
    	
    	String orderState = null;			// 订单状态
    	
    	Short isSupportSellService = null;  // 是否支持7天无理由退换货
    	
    	Short isWaitSendState = 0;			// 是否待发货申请售后  0:否，1：是
    	
    	
    	if(null != params.getFk_trade_id()){
    		trade = tradeMapper.selectByPrimaryKey(params.getFk_trade_id());
    	}    	
    	if(null != params.getFk_order_id()){
    		order = orderMapper.selectByPrimaryKey(params.getFk_order_id());
    	}   
    	 
    	
    	if(null == trade){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.TRADE_NO_EXISTENCE);
    		error.setErrorMessage("交易单无效");
    		return null;
    	}
    	if(null == order){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_NO_EXISTENCE);
    		error.setErrorMessage("订单无效");
    		return null;
    	}
    	
    	orderState = order.getStateCode();
    	
    	isSupportSellService = order.getIsSupportSellService();
    	
		if(null == isSupportSellService){
    		error.setErrorCode(SystemErrorCode.OrderErrorCode.IS_SUPPORT_7_INVALID);
    		error.setErrorMessage("订单是否支持7天无理由退换不能为空");
    		return null;
    	}
    	
    	if(null == orderState){
    		error.setErrorCode(SystemErrorCode.OrderErrorCode.STATE_INVALID);
    		error.setErrorMessage("订单状态不能为空");
    		return null;
    	}
    	
    	if(OrderStateCode.待发货.getKey().equals(orderState)){
    		isWaitSendState = 1;
    	}
    	
    	/** 产品级别售后 校验 **/
    	if(null != params.getFk_order_detail_id()){
    		
    		/** 在待发货状态下,不能申请 **/
    		if(OrderStateCode.待发货.getKey().equals(orderState)){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.CANNOT_ACCEPT_WHEN_ORDER_WAIT_SEND);
        		error.setErrorMessage("产品级别售后 在待发货状态下,不能申请");
        		return null;
    		}
    		
    	    orderDetail = orderDetailMapper.selectByPrimaryKey(params.getFk_order_detail_id());
    	    
    	    if(null == orderDetail){
        		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_DETAIL_NO_EXISTENCE);
        		error.setErrorMessage("订单详细无效");
        		return null;
        	}
    	    
    	    /** 待发货，只能做退款不退货 **/
    	    if(OrderExpressState.待发货.getKey().equals(orderDetail.getExpressState())
    	    		&& !OrderServiceDealType.我要退款但不退货.getKey().equals(params.getDeal_type())){
    	    	error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.CAN_REFUND_BUT_NOT_RETURNGOODS);
        		error.setErrorMessage("订单明细ID "+orderDetail.getId()+"是待发货，只能做退款不退货 ");
        		return null;
    	    }
    	    
    	}else{ /** 订单级别售后校验 **/
    		
    		/** 部分发货不能做订单级别申请  **/ 
    		if(OrderStateCode.部分发货.getKey().equals(orderState)){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ONLY_ACCEPT_WHEN_ORDER_WAIT_SEND);
        		error.setErrorMessage("订单级别售后 只能在待 发货状态或 待收货下 申请");
        		return null;
    		}
    		
    		/** 待发货下，只能退款不退货 **/
    		if(!OrderServiceDealType.我要退款但不退货.getKey().equals(params.getDeal_type())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.CAN_REFUND_BUT_NOT_RETURNGOODS);
        		error.setErrorMessage("订单ID "+order.getId()+"是待发货，只能做退款不退货 ");
        		return null;
    		}
    	}
    	
    	
    	// 判断是否当前用户
    	if(null != order.getCreateUserId() && !params.getMember_id().equals(order.getCreateUserId())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("该订单不属于该用户，不能申请售后");
    		return null;
		}
    	
    	
    	/**
    	 * update 2015-11-04 by jieyi
    	 * 添加判断支持7天无理由售后逻辑
    	 */
		if(isSupportSellService.equals(Constants.OrderIsSupportSellService.支持.getKey())){
			
			if(!orderState.equals(Constants.OrderStateCode.待发货.getKey()) 
					&& !orderState.equals(Constants.OrderStateCode.部分发货.getKey())
	    			&& !orderState.equals(Constants.OrderStateCode.待收货.getKey())
	    			&& !orderState.equals(Constants.OrderStateCode.交易完成.getKey())){  
				error.setErrorCode(SystemErrorCode.OrderErrorCode.STATE_INVALID);
        		error.setErrorMessage("订单状态无效");
        		return null;
			}
			
			if(orderState.equals(Constants.OrderStateCode.交易完成.getKey())){
				
				if(null == order.getEndTime()){
					error.setErrorCode(SystemErrorCode.OrderErrorCode.ORDER_END_TIME_INVALID);
		    		error.setErrorMessage("订单结束时间不能为空");
		    		return null;
				}
				
				if(DateUtil.addDay(order.getEndTime(), 7).getTime() < DateUtil.now().getTime()){
					error.setErrorCode(SystemErrorCode.OrderErrorCode.ORDER_OUT_7_INVALID);
					error.setErrorMessage("该订单不能申请售后，只有在交易完成7天之内才能申请售后！");
					return null;
				}
				
			}
			
		}else if(isSupportSellService.equals(Constants.OrderIsSupportSellService.不支持.getKey())){
			
			if(!orderState.equals(Constants.OrderStateCode.待发货.getKey()) 
	    			&& !orderState.equals(Constants.OrderStateCode.部分发货.getKey())
	    			&& !orderState.equals(Constants.OrderStateCode.待收货.getKey())
	    			){  
				error.setErrorCode(SystemErrorCode.OrderErrorCode.STATE_INVALID);
        		error.setErrorMessage("订单状态无效");
        		return null;
			}
			
		}
    		
    	
		/** 检验可退款金额 **/
		log.info("校验可退款金额");
    	if(!checkRefundPrice(params)){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.REFUND_PRICE_ERROR);
    		error.setErrorMessage("输入退款金额有误，请重新输入金额");
    		return null;
    	}
    	
    	
    	String userName = getUserName(params);
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return null;
    	}
    	
    	record.setApplyName(userName);
    	record.setApplyPhone(params.getApply_phone());
    	record.setApplyTime(DateUtil.now());
    	
    	record.setDealType(params.getDeal_type());
    	record.setDealTypeName(Constants.OrderServiceDealType.toMap().get(params.getDeal_type()));
    	
    	record.setStartTime(DateUtil.now());	
    	
    	// 退款原因
    	record.setRefundReasonTypeId(params.getRefund_reason_type_id());
    	record.setRefundReasonTypeName(Constants.OrderServiceRefundReason.getValue(params.getRefund_reason_type_id()).getValue());
    	
    	record.setRefundRemark(params.getApply_remark());
    	record.setMarkedWords(Constants.OrderServiceMarkedWords.等待商家处理退款申请.getValue());
    	
    	BigDecimal refundPrice = new BigDecimal(params.getRefund_price());
    	record.setRefundPrice(refundPrice);
    	
    	
    	record.setStateCode(Constants.OrderServiceStateCode.申请中.getKey());
    	record.setStateName(Constants.OrderServiceStateCode.申请中.getValue());
    	
    	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待商家处理.getKey());
    	record.setSubStateName(Constants.OrderServiceSubStateCode.等待商家处理.getValue());
    	
    	record.setIsWaitsendState(isWaitSendState);
    	
    	if(null != params.getOrder_service_id()){
    		orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    		if(orderService!=null){
    			
    			if(null != orderService.getCreateUserId() && !params.getMember_id().equals(orderService.getCreateUserId())){
        			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
            		error.setErrorMessage("该售后单买家身份与当前用户不对应");
            		return null;
        		}
    			
    			update(orderServiceResult, params, record, orderService, userName);
    		}else{
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
        		error.setErrorMessage("售后单无效");
        		return null;
    		}
    	}else{
    		// 校验不能重复申请
    		if(this.checkOrderService(params)){
        		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
        		error.setErrorMessage("售后单已经存在");
        		return null;
        	}
    		
    		save(orderServiceResult, params, record, trade, order, userName);
    	}
    	
    	return orderServiceResult;
    }
    
    /**
     *  有明细需更新售后 退款数量、相关金额
     *    退款数量=明细商品数量
     *    退款商品总价 = 明细商品支付价
     *    如果 前端退款金额   大于  商品支付价   ， 退款金额 = 商品支付价 , 运费 = 前端退款金额 - 商品支付价
     * @param params		传参对象
     * @param record		售后VO对象
     * @param orderDetail	订单明细PO对象
     * @return
     */
    public OrderDetail saveHaveFkOrderDetailId(OrderServiceParams params,OrderService record,OrderDetail orderDetail){
    	orderDetail = orderDetailMapper.selectByPrimaryKey(params.getFk_order_detail_id());
    	
    	record.setFkCouponId(orderDetail.getFkCouponId());
    	
    	// 退款数量
    	short refundCount = orderDetail.getBuyCount().shortValue();
    	
    	record.setRefundCount(refundCount);
    	record.setRefundCouponAmount(orderDetail.getGoodsDiscountAmount());
    	
    	// 实际支付的
    	record.setRefundGoodsPrice(orderDetail.getPaymentAmount());
    	
    	// 前端传入退款金额
    	BigDecimal refundPrice = new BigDecimal(params.getRefund_price());
    	
    	record.setRefundPrice(refundPrice);
    	
    	if(null != orderDetail.getPaymentAmount()){
    		if(refundPrice.compareTo(orderDetail.getPaymentAmount())>0){
        		BigDecimal refundExpressPrice = refundPrice.subtract(orderDetail.getPaymentAmount());
        		//运费
            	record.setRefundExpressPrice(refundExpressPrice);
        	} 
    	}
    	
    	// 更新订单明细售后中
    	orderDetail.setServiceState(OrderDetailServiceState.售后中.getKey());
    	this.orderDetailMapper.updateByPrimaryKey(orderDetail);
    	
    	return orderDetail;
    }
    
    /**
     *  1.从明细集合中获取第一个明细实体对象
     *  
     *  运费 = 前端退款金额 - 订单支付价金额 
     *  
     * @param params		传参对象
     * @param record		售后VO对象
     * @param orderDetail	订单明细PO对象
     * @param order			订单PO对象
     * @return
     */
    public OrderDetail saveNotFkOrderDetailId(OrderServiceParams params,OrderService record,OrderDetail orderDetail,Order order){
    	// 从明细集合中获取第一个明细实体对象
    	OrderDetailExample example = new OrderDetailExample();
    	OrderDetailExample.Criteria criteria = example.createCriteria();
    	criteria.andFkOrderIdEqualTo(params.getFk_order_id());
    	List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(example);
    	if(null != orderDetails && !orderDetails.isEmpty()){
    		orderDetail = orderDetails.get(0);
    	}
    	
    	record.setFkCouponId(order.getFkCouponId());
    	
    	record.setRefundCouponAmount(order.getOrderDiscountTotalAmount());
    	//实际支付的
    	record.setRefundGoodsPrice(order.getOrderPaymentAmount());
    	// 前端传入退款金额
    	BigDecimal refundPrice = new BigDecimal(params.getRefund_price());
    	record.setRefundPrice(refundPrice);
    	
    	if(null != order.getOrderPaymentAmount()){
    		if(refundPrice.compareTo(order.getOrderPaymentAmount())>0){
        		BigDecimal refundExpressPrice = refundPrice.subtract(order.getOrderPaymentAmount());
        		//运费
            	record.setRefundExpressPrice(refundExpressPrice);
        	} 
    	}
    	return orderDetail;
    }
    
    /**
     * 更新售后单
     *  1.更新售后单
     *  2.新增售后记录
     *  3.新增售后图片记录(一个图片一条记录)
     *  
     * @param orderServiceResult 	返回对象
     * @param params				传参对象
     * @param record				售后更新对象
     * @param orderService  		售后实体对象
     * @param userName				申请人姓名
     * @return
     */
	public OrderServiceResult update(	
										OrderServiceResult orderServiceResult,
										OrderServiceParams params, 
										OrderService record,
										OrderService orderService, 
										String userName
									) {
		record.setId(orderService.getId());
    	record.setLastUpdateTime(DateUtil.now());
    	record.setLastUpdateUserId(params.getMember_id());
    	
    	// 1.更新售后单
    	orderServiceMapper.updateByPrimaryKeySelective(record);
    	
    	// 2.新增售后单操作记录
		OrderServiceRecord recRecord = new OrderServiceRecord();
		recRecord.setApplyPhone(record.getApplyPhone());
    	recRecord.setCreateTime(DateUtil.now());
    	recRecord.setCreateUserId(params.getMember_id());
    	recRecord.setDealType(record.getDealType());
    	recRecord.setDealTypeName(record.getDealTypeName());
    	
    	recRecord.setFkServiceId(record.getId());
    	
    	recRecord.setOpSign(Constants.OrderServiceOpSign.修改了退款申请等待商家处理.getValue());
    	recRecord.setOpTime(DateUtil.now());
    	recRecord.setOpType(Constants.OrderServiceRecordOpType.修改退款申请.getKey());
    	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.修改退款申请.getValue());
    	recRecord.setOpUserId(params.getMember_id());
    	recRecord.setOpUserName(userName);
    	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
    	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
    	
    	recRecord.setRefundPrice(record.getRefundPrice());
    	recRecord.setRefundReasonTypeName(record.getRefundReasonTypeName());
    	recRecord.setRefundRemark(record.getRefundRemark());
    	
    	
    	orderServiceRecordMapper.insertSelective(recRecord);
    	
		// 3. 新增售后图片记录(一个图片一条记录)
		if(null != params.getBuyer_express_pic_rsurl() && params.getBuyer_express_pic_rsurl().length>0){
			for(String rsurl:params.getBuyer_express_pic_rsurl()){
    			OrderServicePicture picRecord = new OrderServicePicture();
    	    	picRecord.setCreateTime(DateUtil.now());
    	    	picRecord.setFkServiceId(record.getId());
    	    	picRecord.setFkServiceRecordId(recRecord.getId());
    	    	picRecord.setOpType(Constants.OrderServiceRecordOpType.修改退款申请.getKey());
    	    	picRecord.setOpTypeName(Constants.OrderServiceRecordOpType.修改退款申请.getValue());
    	    	picRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
    	    	picRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
    	    	picRecord.setPicRsurl(rsurl);
    	    	
    	    	orderServicePictureMapper.insertSelective(picRecord);
			}
		}
		
		// 售后单ID
		orderServiceResult.setId(record.getId());
		// 售后单号
		orderServiceResult.setBillNo(orderService.getBillNo());
		return orderServiceResult;
    }
    
	/**
	 * 新增售后单
	 * 		1.
	 * 		2.
	 * 		3.
	 * 
	 * @param orderServiceResult	返回对象
	 * @param params				传参
	 * @param record				售后新增对象
	 * @param trade					交易单实体对象
	 * @param order					订单实体对象
	 * @param userName				申请人姓名
	 * @return
	 */
	public OrderServiceResult save(OrderServiceResult orderServiceResult,
			OrderServiceParams params, OrderService record, Trade trade,
			Order order, String userName) {
    	String billNo = null;
    	
    	// 初始化订单明细对象
    	OrderDetail orderDetail = new OrderDetail();
    	
    	// 如果有传入订单明细ID
    	if(null != params.getFk_order_detail_id()){
    		orderDetail = saveHaveFkOrderDetailId(params, record, orderDetail);
    	}else{
    		orderDetail = saveNotFkOrderDetailId(params, record, orderDetail, order);
    	}
    	
    	//售后单号
    	billNo = DateUtil.format(DateUtil.createDateFormat("yyyyMMddHHmmssSSS"), DateUtil.now());
    	record.setBillNo(billNo);
    	
    	record.setCreateTime(DateUtil.now());
    	record.setCreateUserId(params.getMember_id());
    	
    	if(null != order.getOrderType()){
    		if(order.getOrderType().equals(Constants.OrderType.采购单.getKey())){
    			//买家
        		record.setFkShopMemberId(params.getMember_id());
        		//record.setFkShopSupplierId(fkShopSupplierId);
        		//record.setFkSupplierId(fkSupplierId);
        		
        		//卖家
        		record.setFkSupplierId(order.getFkSellerMemberId());
        		//record.setFkSupplierMemberId(order.getFkSellerSupplierId());
        		record.setFkSupplierStoreId(order.getFkSellerStoreId());
        		
        	}else if(order.getOrderType().equals(Constants.OrderType.自营单.getKey())){
        		//买家
        		record.setFkCustomerId(params.getMember_id());
        		
        		//卖家
        		record.setFkShopMemberId(order.getFkBuyeStoreMemberId());
        		record.setFkShopStoreId(order.getFkBuyerStoreId());
        		//record.setFkShopSupplierId(fkShopSupplierId);
        		//record.setFkSupplierId(fkSupplierId);
        	}else if(order.getOrderType().equals(Constants.OrderType.代销单.getKey())){
        		//买家
        		record.setFkCustomerId(params.getMember_id());
        		
        		
        		//卖家
        		record.setFkSupplierId(order.getFkSellerMemberId());
        		//record.setFkSupplierMemberId(fkSupplierMemberId);
        		record.setFkSupplierStoreId(order.getFkSellerStoreId());
        	}
    	}
    	
    	
    	
    	record.setFkCustomerId(trade.getFkMemberId());
    	record.setFkOrderDetailId(params.getFk_order_detail_id());
    	record.setFkOrderId(params.getFk_order_id());
    	
    	/*record.setFkShopMemberId(order.getFkBuyeStoreMemberId());
    	record.setFkShopStoreId(order.getFkBuyerStoreId());
    	record.setFkShopSupplierId(order.getFkSellerSupplierId());
    	
    	record.setFkSupplierId(order.getFkSellerSupplierId());
    	record.setFkSupplierMemberId(order.getFkSellerMemberId());
    	record.setFkSupplierStoreId(order.getFkSellerStoreId());*/
    	
    	record.setFkTradeId(params.getFk_trade_id());
    	
    	record.setOrderType(order.getOrderType());
    	
    	// 保存明细信息
    	record.setProductLogoRsurl(orderDetail.getProductLogoRsurl());
    	record.setProductName(orderDetail.getProductName());
    	record.setSkuPropertyValue(orderDetail.getSkuPropertyValue());
    	record.setProductPrice(orderDetail.getDealUnitPrice());
    	
    	
    	orderServiceMapper.insertSelective(record);
    	
    	//订单表
    	Order newOrder = new Order();
    	newOrder.setId(order.getId());
    	newOrder.setServiceState(Constants.OrderServiceState.售后中.getKey());
    	newOrder.setLastUpdateTime(DateUtil.now());
    	newOrder.setLastUpdateUserId(params.getMember_id());
    	
    	orderMapper.updateByPrimaryKeySelective(newOrder);
    	
    	//订单记录
    	TradeOrderRecord tradeOrderRecord = new TradeOrderRecord();
    	tradeOrderRecord.setFkOrderId(order.getId());
    	//tradeOrderRecord.setFkRecordDetailId(fkRecordDetailId);
    	tradeOrderRecord.setFkTradeId(order.getFkTradeId());
    	tradeOrderRecord.setOpContent("申请售后");
    	tradeOrderRecord.setOpTime(DateUtil.now());
    	tradeOrderRecord.setOpType(Constants.TradeOrderRecordOpType.申请售后.getValue());
    	tradeOrderRecord.setOpUserId(params.getMember_id());
    	tradeOrderRecord.setOpUserName(userName);
    	tradeOrderRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
    	tradeOrderRecord.setRemark("");
    	tradeOrderRecordMapper.insertSelective(tradeOrderRecord);
    	
    	//售后记录
    	OrderServiceRecord recRecord = new OrderServiceRecord();
    	recRecord.setApplyPhone(params.getApply_phone());
    	recRecord.setCreateTime(DateUtil.now());
    	recRecord.setCreateUserId(params.getMember_id());
    	recRecord.setDealType(params.getDeal_type());
    	recRecord.setDealTypeName(Constants.OrderServiceDealType.toMap().get(params.getDeal_type()));
    	
    	recRecord.setFkServiceId(record.getId());
    	
    	recRecord.setOpSign(Constants.OrderServiceOpSign.发起了退款申请等待商家处理.getValue());
    	recRecord.setOpTime(DateUtil.now());
    	recRecord.setOpType(Constants.OrderServiceRecordOpType.申请退款.getKey());
    	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.申请退款.getValue());
    	recRecord.setOpUserId(params.getMember_id());
    	recRecord.setOpUserName(userName);
    	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
    	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
    	
    	recRecord.setRefundPrice(record.getRefundPrice());
    	recRecord.setRefundReasonTypeName(record.getRefundReasonTypeName());
    	recRecord.setRefundRemark(record.getRefundRemark());
    	
    	orderServiceRecordMapper.insertSelective(recRecord);
    	//
		if(null != params.getBuyer_express_pic_rsurl() && params.getBuyer_express_pic_rsurl().length>0){
			for(String rsurl:params.getBuyer_express_pic_rsurl()){
				OrderServicePicture picRecord = new OrderServicePicture();
		    	picRecord.setCreateTime(DateUtil.now());
		    	picRecord.setFkServiceId(record.getId());
		    	picRecord.setFkServiceRecordId(recRecord.getId());
		    	picRecord.setOpType(Constants.OrderServiceRecordOpType.申请退款.getKey());
		    	picRecord.setOpTypeName(Constants.OrderServiceRecordOpType.申请退款.getValue());
		    	picRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
		    	picRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
		    	picRecord.setPicRsurl(rsurl);
		    	
		    	orderServicePictureMapper.insertSelective(picRecord);
			}
		}
		
		orderServiceResult.setId(record.getId());
		orderServiceResult.setBillNo(billNo);
		return orderServiceResult;
    }
    
    /**
     * 查询售后单是否已经申请过(过滤掉 待发货的售后)
     * 1. 先根据订单ID查询是否 有申请记录
     * 2. 订单ID不存在申请记录，如果有传入 明细ID，继续查询是否有申请记录
     */
    public boolean checkOrderService(OrderServiceParams params){
    	
    	Long order_id = params.getFk_order_id();
    	Long order_detail_id = params.getFk_order_detail_id();
    	
    	// 1. 先根据订单ID查询是否 有申请记录 ,过滤掉 待发货的售后
    	OrderServiceExample oexample = new OrderServiceExample();
		OrderServiceExample.Criteria ocriteria = oexample.createCriteria();
		ocriteria.andFkOrderIdEqualTo(order_id);
		ocriteria.andIsWaitsendStateNotEqualTo(OrderServiceWailtSendState.是.getKey());
		
		List<OrderService> orderServiceListsA = orderServiceMapper.selectByExample(oexample);
		if(null != orderServiceListsA && !orderServiceListsA.isEmpty() && orderServiceListsA.size() == 2){
			return true;
		} 
    	
		// 2. 订单ID不存在申请记录，如果有传入 明细ID，继续查询是否有申请记录
		if(null != order_detail_id){
    		OrderServiceExample example = new OrderServiceExample();
    		OrderServiceExample.Criteria criteria = example.createCriteria();
    		criteria.andFkOrderDetailIdEqualTo(order_detail_id);
    		List<OrderService> orderServiceListsB = orderServiceMapper.selectByExample(example);
    		if(null != orderServiceListsB && !orderServiceListsB.isEmpty()){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * 获取退款最大值
     * 如果是第一次退款，可退邮费。第二次以上则不退邮费
     */
    public BigDecimal getMaxRefundPrice(OrderServiceParams params){
    	BigDecimal maxRefundPrice = new BigDecimal(0);
    	//是否第一次退款
    	boolean first = false;
    	
    	BigDecimal freightAmount = new BigDecimal(0);
    	
    	Order order = new Order();
    	
    	if(null != params.getFk_order_id()){
    		// 根据订单ID 去售后表查询是否有售后记录,有代表之前退过款
    		OrderServiceExample example = new OrderServiceExample();
    		OrderServiceExample.Criteria criteria = example.createCriteria();
    		criteria.andFkOrderIdEqualTo(params.getFk_order_id());
    		criteria.andStateCodeNotEqualTo(OrderServiceStateCode.已关闭.getKey());
    		
    		List<OrderService> orderServiceLists = orderServiceMapper.selectByExample(example);
    		if(null != orderServiceLists && !orderServiceLists.isEmpty()){
    			first = true;
    		}
    		order = orderMapper.selectByPrimaryKey(params.getFk_order_id());
    		if(null != order && !first){
    			if(null != order.getOrderFreightAmount())
    				freightAmount = order.getOrderFreightAmount();
    		}
    	}
    	
    	if(null != params.getFk_order_detail_id()){
    		// 商品级别的退款 = 商品结算金额 + 邮费(之前做个申请，且不关闭的则不算邮费)
    		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(params.getFk_order_detail_id());
    		if(null != orderDetail && null != orderDetail.getPaymentAmount()){
    			BigDecimal paymentAmount = orderDetail.getPaymentAmount();
        		maxRefundPrice = paymentAmount.add(freightAmount);
    		}
    	}else{
    		//订单级别的退款 = 支付价
    		if(null != order && null != order.getOrderPaymentAmount()){
    			BigDecimal orderPaymentAmount = order.getOrderTotalAmount();
        		maxRefundPrice = orderPaymentAmount;
    		}
    	}
    	return maxRefundPrice;
    }

    /**
     * 检查退款,不能大于可退款金额
     * @return
     */
    public boolean checkRefundPrice(OrderServiceParams params){
    	boolean check = true;
    	// 退款参数金额
    	BigDecimal refundPrice = new BigDecimal(params.getRefund_price());
    	// 系统可以退金额
    	BigDecimal maxRefundPrice = this.getMaxRefundPrice(params);
    	
    	//maxRefundPrice必须大于或者等于refundPrice
		if(maxRefundPrice.compareTo(refundPrice)<0){
			check = false;
		}
		return check;
    }
    
    /**
     * 判断退款金额接口
     * @param params
     * @param successResult
     * @param error
     */
    public void checkPrice(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	if(null != params.getFk_order_id()){
    		Order order = orderMapper.selectByPrimaryKey(params.getFk_order_id());
    		if(null==order){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_NO_EXISTENCE);
        		error.setErrorMessage("订单无效");
        		return;
    		}
    	}
    	
    	if(null != params.getFk_order_detail_id()){
    		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(params.getFk_order_detail_id());
    		if(null == orderDetail){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_DETAIL_NO_EXISTENCE);
        		error.setErrorMessage("订单详细无效");
        		return;
    		}
    	}
    	successResult.setIs_success(checkRefundPrice(params));
    }
    
    /**
     * 获取用户名
     * @author 揭懿
     * @param params
     * @return
     */
    public String getUserName(OrderServiceParams params){
    	String userName = null;
    	if(null != params.getMember_id()){
    		Member member = memberMapper.selectByPrimaryKey(params.getMember_id());
        	if(null != member){
        		userName = member.getRealName();
        	}
    	}
    	return userName;
    }
    
    /**
     * 填写退货物流
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void setExpress(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	String buyerExpressCompanyName = null;
    	if(null != orderService){
    		
    		
    		if(null != orderService.getCreateUserId() && !params.getMember_id().equals(orderService.getCreateUserId())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
        		error.setErrorMessage("该售后单买家身份与当前用户不对应");
        		return;
    		}
    		
    		if(null != orderService.getDealType() 
    				&& orderService.getDealType().equals(Constants.OrderServiceDealType.我要退款但不退货.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_DEAL_TYPE_INVALID);
        		error.setErrorMessage("售后单处理方式不正确");
        		return;
    		}
    		
    		if(null != orderService.getStateCode() 
    				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.处理中.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
        		error.setErrorMessage("售后单状态不正确，正确状态是处理中");
        		return;
    		}
    		
    		if(null != orderService.getSubStateCode() 
    				&& !orderService.getSubStateCode().equals(Constants.OrderServiceSubStateCode.等待买家处理.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_SUBSTATE_INVALID);
        		error.setErrorMessage("售后单子状态不正确，正确状态是等待买家处理");
        		return;
    		}
    		
    		if(null != orderService.getServiceTips() 
    				&& !orderService.getServiceTips().equals(Constants.OrderServiceTips.商家同意退货.getValue())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_TIPS_INVALID);
        		error.setErrorMessage("售后单阶段不正确，正确阶段是商家同意退货");
        		return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
    		record.setBuyerExpressBillNo(params.getBuyer_express_bill_no());
        	record.setBuyerExpressCompanyId(params.getBuyer_express_company_id());
        	
        	ExpressCompany expressCompany = expressCompanyMapper.selectByPrimaryKey(params.getBuyer_express_company_id());
        	if(null != expressCompany){
        		buyerExpressCompanyName = expressCompany.getName();
        	}else{
        		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_EXPRESS_COMPANY_INVALID);
        		error.setErrorMessage("物流方式无效");
        		return;
        	}
        		
        	record.setBuyerExpressCompanyName(buyerExpressCompanyName);
        	record.setBuyerExpressTime(DateUtil.now());
        	
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.等待商家确认收货.getValue());
        	
        	record.setServiceTips(Constants.OrderServiceTips.等待商家确认收货.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待商家处理.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.等待商家处理.getValue());
        	
    		orderServiceMapper.updateByPrimaryKeySelective(record);
    		
    		
    		//售后单操作记录
    		OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setBuyerExpressBillNo(params.getBuyer_express_bill_no());
        	recRecord.setBuyerExpressCompanyId(params.getBuyer_express_company_id());
        	recRecord.setBuyerExpressCompanyName(buyerExpressCompanyName);
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.已退货等待商家确认收货.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.填写退货物流信息.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.填写退货物流信息.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
        	
        	
        	recRecord.setReturnPhone(params.getReturn_phone());
        	recRecord.setReturnRemark(params.getReturn_remark());
    		
        	orderServiceRecordMapper.insertSelective(recRecord);
    		//
    		if(null != params.getBuyer_express_pic_rsurl() && params.getBuyer_express_pic_rsurl().length>0){
    			for(String rsurl:params.getBuyer_express_pic_rsurl()){
    				OrderServicePicture picRecord = new OrderServicePicture();
    		    	picRecord.setCreateTime(DateUtil.now());
    		    	picRecord.setFkServiceId(record.getId());
    		    	picRecord.setFkServiceRecordId(recRecord.getId());
    		    	picRecord.setOpType(Constants.OrderServiceRecordOpType.填写退货物流信息.getKey());
    		    	picRecord.setOpTypeName(Constants.OrderServiceRecordOpType.填写退货物流信息.getValue());
    		    	picRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
    		    	picRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
    		    	picRecord.setPicRsurl(rsurl);
    		    	
    		    	orderServicePictureMapper.insertSelective(picRecord);
    			}
    		}
    		
    		successResult.setIs_success(true);
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    
    private boolean checkApplyState(OrderService orderService,ErrorResult error){
    	
    	if(null != orderService.getStateCode() 
				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.申请中.getKey())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
    		error.setErrorMessage("售后单状态不正确，正确状态是申请中");
    		return true;
		}
		
		if(null != orderService.getSubStateCode() 
				&& !orderService.getSubStateCode().equals(Constants.OrderServiceSubStateCode.等待商家处理.getKey())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_SUBSTATE_INVALID);
    		error.setErrorMessage("售后单子状态不正确，正确状态是等待商家处理");
    		return true;
		}
		
		return false;
    }
    
    /**
     * 
     * @Title: checkSellerMemberId
     * @Description: 确认卖家身份
     * @date 2015年11月7日 下午4:21:20
     * @author 揭懿
     * @param orderService
     * @param params
     * @param error
     * @return
     */
    private boolean checkSellerMemberId(OrderService orderService,OrderServiceParams params,ErrorResult error){
    	if(null != orderService.getOrderType()){
			if(orderService.getOrderType().equals(Constants.OrderType.自营单.getKey())){
				if(null != params.getMember_id() && !params.getMember_id().equals(orderService.getFkShopMemberId())){
					error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
		    		error.setErrorMessage("该售后单卖家身份与当前用户不对应");
		    		return true;
				}
			}else{
				if(null != params.getMember_id() && !params.getMember_id().equals(orderService.getFkSupplierId())){
					error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
		    		error.setErrorMessage("该售后单卖家身份与当前用户不对应");
		    		return true;
				}
			}
		}
    	
    	return false;
    }
    
    
    /**
     * 同意退款or同意退货
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void agree(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		if(this.checkApplyState(orderService, error)){
    			return;
    		}
    		
    		if(this.checkSellerMemberId(orderService, params, error)){
    			return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
        	
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	// 售后单操作记录
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.同意退款给买家.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.同意退款.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.同意退款.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.商家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.商家.getValue());
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setRefundPrice(orderService.getRefundPrice());
        	
        	if(orderService.getDealType().equals(Constants.OrderServiceDealType.我要退款但不退货.getKey())){
        		this.agreeRefund(record, recRecord);
        		
        		closeOrderMain(record, params,userName);
        		
        	}else if(orderService.getDealType().equals(Constants.OrderServiceDealType.我要退款并退货.getKey())){
        		if(null == params.getReturn_address()){
        			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.RETURN_ADDRESS_IS_NULL);
            		error.setErrorMessage("退货地址不能为空");
            		return;
        		}
        		
        		this.agreeReturn(params, record, recRecord);
        	}
        	
        	successResult.setIs_success(true);
        	
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    /**
     *  售后完成， 关闭订单 
     *  如果是订单级别的，直接关闭 订单、交易单
     *  如果是商品级别的，只更新明细售后完成，然后在判断同级其他商品如果已售后完成，在关闭订单、交易单
     * @author : liguosheng 
     * @CreateDate : 2015年11月21日 下午1:58:00 
     * @param record
     * @param params
     * @param userName
     */
    private void closeOrderMain(OrderService record,OrderServiceParams params,String userName){
    	Long orderDetailId = record.getFkOrderDetailId();
    	Long orderId = record.getFkOrderId();
    	
    	if(orderDetailId == null){
    		closeTradeAndOrder(record, params, userName);
    	}else{
    		/** 更新订单明细售后状态为 售后完成  **/
    		OrderDetail orderdetail = this.orderDetailMapper.selectByPrimaryKey(orderDetailId);
    		orderdetail.setServiceState(OrderDetailServiceState.售后完成.getKey());
    		this.orderDetailMapper.updateByPrimaryKey(orderdetail);
    		
    		/** 同级其他产品无售后，则更新订单售后状态 **/
			OrderDetailExample example1 = new OrderDetailExample();
			example1.createCriteria().andServiceStateEqualTo(OrderDetailServiceState.售后中.getKey());
			int count = this.orderDetailMapper.countByExample(example1);
			if(count <= 0){
				
				log.info("统计无其他售后中产品,更细订单售后已结束");
				
				Order order = this.orderMapper.selectByPrimaryKey(orderId);
				order.setServiceState(OrderServiceState.售后已结束.getKey());
				this.orderMapper.updateByPrimaryKey(order);
				
				/** 统计没走过售后的产品明细,如果没有数据则说明已走完售后,需调用分账接口  **/
				OrderDetailExample example2 = new OrderDetailExample();
				example2.createCriteria().andServiceStateIsNull();
				int nullcount = this.orderDetailMapper.countByExample(example2);
				if(nullcount <= 0){
					
					log.info("统计无走过售后订单明细数为0,需要调用预分账接口");
					closeTradeAndOrder(record, params, userName);
					/** TODO 调用分账  **/
				}
				
			}
    		
    		
    	}
    }
    
    /**
     * 
     * @Title: closeTradeAndOrder
     * @Description: 关闭订单，交易单
     * @date 2015年11月4日 下午8:30:38
     * @author 揭懿
     * @param record
     * @param params
     */
    private void closeTradeAndOrder(OrderService record,OrderServiceParams params,String userName){
    	
    	Order order = orderMapper.selectByPrimaryKey(record.getFkOrderId());
    	
    	if(null != order){
    		String stateCode = Constants.OrderStateCode.交易关闭.getKey();
    		String stateName = Constants.OrderStateCode.交易关闭.getValue();
    		
    		Order newOrder = new Order();
        	newOrder.setId(order.getId());
        	newOrder.setStateCode(stateCode);
        	newOrder.setStateName(stateName);
        	newOrder.setEndTime(DateUtil.now());
        	newOrder.setServiceState(Constants.OrderServiceState.售后已结束.getKey());
        	newOrder.setLastUpdateTime(DateUtil.now());
        	newOrder.setLastUpdateUserId(params.getMember_id());
        	
        	orderMapper.updateByPrimaryKeySelective(newOrder);
        	
        	
        	Trade trade = tradeMapper.selectByPrimaryKey(order.getFkTradeId());
        	
        	if(null != trade){
        		Trade newTrade = new Trade();
        		newTrade.setId(trade.getId());
        		newTrade.setStateCode(stateCode);
        		newTrade.setStateName(stateName);
        		newTrade.setLastUpdateTime(DateUtil.now());
        		newTrade.setLastUpdateUserId(params.getMember_id());
        		
        		tradeMapper.updateByPrimaryKeySelective(newTrade);
        	}
        	
        	
        	
        	//订单记录
        	TradeOrderRecord tradeOrderRecord = new TradeOrderRecord();
        	tradeOrderRecord.setFkOrderId(order.getId());
        	//tradeOrderRecord.setFkRecordDetailId(fkRecordDetailId);
        	tradeOrderRecord.setFkTradeId(order.getFkTradeId());
        	tradeOrderRecord.setOpContent("关闭订单");
        	tradeOrderRecord.setOpTime(DateUtil.now());
        	tradeOrderRecord.setOpType(Constants.TradeOrderRecordOpType.关闭订单.getValue());
        	tradeOrderRecord.setOpUserId(params.getMember_id());
        	tradeOrderRecord.setOpUserName(userName);
        	tradeOrderRecord.setOpUserType(Constants.OrderServiceOpUserType.商家.getKey());
        	tradeOrderRecord.setRemark("");
        	tradeOrderRecordMapper.insertSelective(tradeOrderRecord);
        	
        	
    	}
    }
    
    /**
     * 同意（我要退款但不退货）
     * @author 揭懿
     * @param record
     * @param recRecord
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void agreeRefund(OrderService record,OrderServiceRecord recRecord){
    	record.setEndTime(DateUtil.now());
    	
    	record.setStateCode(Constants.OrderServiceStateCode.已完成.getKey());
    	record.setStateName(Constants.OrderServiceStateCode.已完成.getValue());
    	record.setSubStateCode(Constants.OrderServiceSubStateCode.同意退款.getKey());
    	record.setSubStateName(Constants.OrderServiceSubStateCode.同意退款.getValue());
    	
    	record.setMarkedWords(Constants.OrderServiceMarkedWords.退款成功.getValue());
    	
    	orderServiceMapper.updateByPrimaryKeySelective(record);
    	//增加退款单
    	
    	orderServiceRecordMapper.insertSelective(recRecord);
    	
    	
    	
    	
    }
    
    /**
     * 同意（我要退款并退货）
     * @author 揭懿
     * @param params
     * @param record
     * @param recRecord
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    private void agreeReturn(OrderServiceParams params,OrderService record,OrderServiceRecord recRecord){
    	record.setStateCode(Constants.OrderServiceStateCode.处理中.getKey());
    	record.setStateName(Constants.OrderServiceStateCode.处理中.getValue());
    	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待买家处理.getKey());
    	record.setSubStateName(Constants.OrderServiceSubStateCode.等待买家处理.getValue());
    	
    	record.setMarkedWords(Constants.OrderServiceMarkedWords.商家已同意退款请退货给商家.getValue());
    	record.setServiceTips(Constants.OrderServiceTips.商家同意退货.getValue());
    	
    	record.setReturnAddress(params.getReturn_address());
    	orderServiceMapper.updateByPrimaryKeySelective(record);
    	
    	recRecord.setReturnAddress(params.getReturn_address());
    	orderServiceRecordMapper.insertSelective(recRecord);
    }
    
    /**
     * 拒绝售后
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void refuse(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	
    	String userName = getUserName(params);
    	
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		if(this.checkApplyState(orderService, error)){
    			return;
    		}
    		
    		if(this.checkSellerMemberId(orderService, params, error)){
    			return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
    		record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
    		
    		record.setSellerSupplierAcceptAdminId(params.getMember_id());
        	record.setSellerSupplierAcceptAdminName(userName);
        	record.setSellerSupplierAcceptTime(DateUtil.now());
        	record.setSellerSupplierFeedbackResult(Constants.OrderServiceSellerSupplierFeedbackResult.拒绝.getKey());
        	record.setSellerSupplierRefuseReason(params.getRefuse_reason());
        	
        	record.setStateCode(Constants.OrderServiceStateCode.处理中.getKey());
        	record.setStateName(Constants.OrderServiceStateCode.处理中.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待买家处理.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.等待买家处理.getValue());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.商家拒绝退款给买家.getValue());
    		record.setServiceTips(Constants.OrderServiceTips.商家拒绝.getValue());
    		
    		orderServiceMapper.updateByPrimaryKeySelective(record);
    		
    		//售后单操作记录
    		OrderServiceRecord recRecord = new OrderServiceRecord();
    		
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.商家拒绝退款给买家.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.拒绝退款.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.拒绝退款.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.商家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.商家.getValue());
        	
        	recRecord.setRefuseReason(params.getRefuse_reason());
        	
        	
    		orderServiceRecordMapper.insertSelective(recRecord);
    		
    		successResult.setIs_success(true);
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    
    private boolean checkDealState(OrderService orderService,ErrorResult error){
    	
    	if(null != orderService.getDealType() 
				&& orderService.getDealType().equals(Constants.OrderServiceDealType.我要退款但不退货.getKey())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_DEAL_TYPE_INVALID);
    		error.setErrorMessage("售后单处理方式不正确");
    		return true;
		}
		
		if(null != orderService.getStateCode() 
				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.处理中.getKey())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
    		error.setErrorMessage("售后单状态不正确，正确状态是处理中");
    		return true;
		}
		
		if(null != orderService.getSubStateCode() 
				&& !orderService.getSubStateCode().equals(Constants.OrderServiceSubStateCode.等待商家处理.getKey())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_SUBSTATE_INVALID);
    		error.setErrorMessage("售后单子状态不正确，正确状态是等待商家处理");
    		return true;
		}
		
		if(null != orderService.getServiceTips() 
				&& !orderService.getServiceTips().equals(Constants.OrderServiceTips.等待商家确认收货.getValue())){
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_TIPS_INVALID);
    		error.setErrorMessage("售后单阶段不正确，正确阶段是等待商家确认收货");
    		return true;
		}
		
		return false;
    }
    
    /**
     * 确认收货并退款
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void confirmReceiving(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		if(this.checkDealState(orderService, error)){
    			return;
    		}
    		
    		if(this.checkSellerMemberId(orderService, params, error)){
    			return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
    		record.setEndTime(DateUtil.now());
    		
    		record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.退款成功.getValue());
        	
        	record.setSellerSupplierComfirmAdminId(params.getMember_id());
        	record.setSellerSupplierComfirmAdminName(userName);
        	record.setStateCode(Constants.OrderServiceStateCode.已完成.getKey());
        	record.setStateName(Constants.OrderServiceStateCode.已完成.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.同意退款.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.同意退款.getValue());
        	record.setServiceTips("");
        	
        	orderServiceMapper.updateByPrimaryKeySelective(record);
        	
        	//增加退款单
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.商家确认收货并退款.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.确认收货并退款.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.确认收货并退款.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.商家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.商家.getValue());
        	
        	recRecord.setRefundPrice(record.getRefundPrice());
        	recRecord.setReturnAddress(record.getReturnAddress());
        	
        	orderServiceRecordMapper.insertSelective(recRecord);
        	
        	closeOrderMain(record, params,userName);
        	
        	successResult.setIs_success(true);
        	
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    /**
     * 拒绝确认收货
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void refuseReceiving(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		if(this.checkDealState(orderService, error)){
    			return;
    		}
    		
    		
    		if(this.checkSellerMemberId(orderService, params, error)){
    			return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.商家拒绝确认收货.getValue());
        	
        	record.setServiceTips(Constants.OrderServiceTips.商家拒绝.getValue());
        	
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待买家处理.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.等待买家处理.getValue());
        	
        	orderServiceMapper.updateByPrimaryKeySelective(record);
        	
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.商家拒绝确认收货.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.拒绝确认收货.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.拒绝确认收货.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.商家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.商家.getValue());
        	
        	recRecord.setRefundPrice(record.getRefundPrice());
        	recRecord.setReturnAddress(record.getReturnAddress());
        	
        	orderServiceRecordMapper.insertSelective(recRecord);
        	
        	successResult.setIs_success(true);
        	
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    /**
     * 申请平台客服介入
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void consult(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		
    		if(null != orderService.getCreateUserId() && !params.getMember_id().equals(orderService.getCreateUserId())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
        		error.setErrorMessage("该售后单买家身份与当前用户不对应");
        		return;
    		}
    		
    		if(null != orderService.getStateCode() 
    				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.处理中.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
        		error.setErrorMessage("售后单状态不正确，正确状态是处理中");
        		return;
    		}
    		
    		if(null != orderService.getSubStateCode() 
    				&& !orderService.getSubStateCode().equals(Constants.OrderServiceSubStateCode.等待买家处理.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_SUBSTATE_INVALID);
        		error.setErrorMessage("售后单子状态不正确，正确状态是等待买家处理");
        		return;
    		}
    		
    		if(null != orderService.getServiceTips() 
    				&& !orderService.getServiceTips().equals(Constants.OrderServiceTips.商家拒绝.getValue())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_TIPS_INVALID);
        		error.setErrorMessage("售后单阶段不正确，正确阶段是商家拒绝");
        		return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.申请掌柜客服介入.getValue());
        	record.setMediationBy(userName);
        	
        	record.setPlatformMediationState(Constants.OrderServicePlatformMediationState.申请中.getKey());
        	
        	
        	record.setServiceTips(Constants.OrderServiceTips.等待平台客服介入.getValue());
        	//udpate 2015-11-09 by jieyi不改变售后状态
        	//record.setStateCode(Constants.OrderServiceStateCode.平台客服介入协商中.getKey());
        	//record.setStateName(Constants.OrderServiceStateCode.平台客服介入协商中.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.平台客服介入.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.平台客服介入.getValue());
        	
        	orderServiceMapper.updateByPrimaryKeySelective(record);
        	
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.申请掌柜客服介入.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.申请平台客服介入.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.申请平台客服介入.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
        	
        	orderServiceRecordMapper.insertSelective(recRecord);
        	
        	successResult.setIs_success(true);
        	
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    /**
     * 撤销平台客服介入
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void cancelConsult(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		
    		if(null != orderService.getCreateUserId() && !params.getMember_id().equals(orderService.getCreateUserId())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
        		error.setErrorMessage("该售后单买家身份与当前用户不对应");
        		return;
    		}
    		
    		/*if(null != orderService.getStateCode() 
    				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.平台客服介入协商中.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
        		error.setErrorMessage("售后单状态不正确，正确状态是平台客服介入协商中");
        		return;
    		}*/
    		
    		if(null != orderService.getStateCode() 
    				&& !orderService.getStateCode().equals(Constants.OrderServiceStateCode.处理中.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_STATE_INVALID);
        		error.setErrorMessage("售后单状态不正确，正确状态是处理中");
        		return;
    		}
    		
    		if(null != orderService.getSubStateCode() 
    				&& !orderService.getSubStateCode().equals(Constants.OrderServiceSubStateCode.平台客服介入.getKey())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_SUBSTATE_INVALID);
        		error.setErrorMessage("售后单子状态不正确，正确状态是平台客服介入");
        		return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    		
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.撤销客服介入.getValue());
        	
        	record.setStateCode(Constants.OrderServiceStateCode.申请中.getKey());
        	record.setStateName(Constants.OrderServiceStateCode.申请中.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.等待商家处理.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.等待商家处理.getValue());
        	
        	orderServiceMapper.updateByPrimaryKeySelective(record);
        	
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.撤销客服介入.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.撤销客服介入.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.撤销客服介入.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
        	
        	orderServiceRecordMapper.insertSelective(recRecord);
        	
        	successResult.setIs_success(true);
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
    
    /**
     * 关闭售后
     * @author 揭懿
     * @param params
     * @param successResult
     * @param error
     */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void closeOrderService(OrderServiceParams params,SuccessResult successResult,ErrorResult error){
    	String userName = getUserName(params);
    	if(userName==null){
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
    		error.setErrorMessage("当前用户名无效");
    		return;
    	}
    	
    	OrderService orderService = orderServiceMapper.selectByPrimaryKey(params.getOrder_service_id());
    	
    	if(null != orderService){
    		
    		if(this.checkApplyState(orderService, error)){
    			return;
    		}
    		
    		if(null != orderService.getCreateUserId() && !params.getMember_id().equals(orderService.getCreateUserId())){
    			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.USER_NAME_INVALID);
        		error.setErrorMessage("该售后单买家身份与当前用户不对应");
        		return;
    		}
    		
    		OrderService record = new OrderService();
    		record.setId(orderService.getId());
    	
        	record.setEndTime(DateUtil.now());
        	
        	record.setLastUpdateTime(DateUtil.now());
        	record.setLastUpdateUserId(params.getMember_id());
        	
        	record.setMarkedWords(Constants.OrderServiceMarkedWords.买家关闭退款服务.getValue());
        	
        	record.setStateCode(Constants.OrderServiceStateCode.已关闭.getKey());
        	record.setStateName(Constants.OrderServiceStateCode.已关闭.getValue());
        	record.setSubStateCode(Constants.OrderServiceSubStateCode.售后撤销.getKey());
        	record.setSubStateName(Constants.OrderServiceSubStateCode.售后撤销.getValue());
        	
        	orderServiceMapper.updateByPrimaryKeySelective(record);
        	
        	OrderServiceRecord recRecord = new OrderServiceRecord();
        	recRecord.setCreateTime(DateUtil.now());
        	recRecord.setCreateUserId(params.getMember_id());
        	
        	recRecord.setFkServiceId(record.getId());
        	
        	recRecord.setOpSign(Constants.OrderServiceOpSign.买家关闭退款服务.getValue());
        	recRecord.setOpTime(DateUtil.now());
        	recRecord.setOpType(Constants.OrderServiceRecordOpType.关闭申请退款.getKey());
        	recRecord.setOpTypeName(Constants.OrderServiceRecordOpType.关闭申请退款.getValue());
        	recRecord.setOpUserId(params.getMember_id());
        	recRecord.setOpUserName(userName);
        	recRecord.setOpUserType(Constants.OrderServiceOpUserType.买家.getKey());
        	recRecord.setOpUserTypeName(Constants.OrderServiceOpUserType.买家.getValue());
        	
        	orderServiceRecordMapper.insertSelective(recRecord);
        	
        	successResult.setIs_success(true);
        	
        	// 更新订单、订单明细售后状态为关闭
        	closeToUpdateOrder(orderService);
        	
    	}else{
    		error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return;
    	}
    }
	
	/**
	 * TODO 售后关闭，更新订单
	 * 订单级别  	售后关闭后，更新 订单售后状态
	 * 商品级别	售后，更新售后状态
	 * 同级其他产品无售后，则更新订单售后状态
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月20日 下午5:10:41 
	 * @param fkOrderId
	 */
	private void closeToUpdateOrder(OrderService orderService) {
		
		Long orderId = orderService.getFkOrderId();
		Long orderDetailId = orderService.getFkOrderDetailId();
		
		Order order = this.orderMapper.selectByPrimaryKey(orderId);
		if(order != null){
			
			/** 订单级别售后，只更新订单状态  **/
			if(orderDetailId != null){
				order.setServiceState(OrderServiceState.售后已结束.getKey());
				this.orderMapper.updateByPrimaryKey(order);
			}else{
				/** 商品级别售后，更新售后状态。**/
				OrderDetail orderdetail = this.orderDetailMapper.selectByPrimaryKey(orderDetailId);
				if(orderdetail != null){
					orderdetail.setServiceState(OrderDetailServiceState.售后关闭.getKey());
					this.orderDetailMapper.updateByPrimaryKey(orderdetail);
					
					/** 同级其他产品无售后，则更新订单售后状态 **/
					OrderDetailExample example = new OrderDetailExample();
					example.createCriteria().andServiceStateEqualTo(OrderDetailServiceState.售后中.getKey());
					int count = this.orderDetailMapper.countByExample(example);
					if(count <= 0){
						order.setServiceState(OrderServiceState.售后已结束.getKey());
						this.orderMapper.updateByPrimaryKey(order);
					}
				}
			}
			
			
		}
	}

	
	/**
	 * 获取售后单详情
	 * @param params
	 * @param error
	 * @return
	 */
	public OrderServiceResult getOrderServiceResult(OrderServiceParams params,ErrorResult error){
		OrderServiceResult result = new OrderServiceResult();
		List<OrderServiceVo> orderServiceVoLists = orderServiceDao.getOrderServiceVo(params);
		
		OrderServiceVo orderServiceVo = null;
		if(null != orderServiceVoLists && !orderServiceVoLists.isEmpty()){
			orderServiceVo = orderServiceVoLists.get(0);
		}
	    
		if(null != orderServiceVo){
			BeanUtil.copyProperties(result, orderServiceVo);
			
			List<OrderServiceRecordResult> recordResults = new ArrayList<OrderServiceRecordResult>();
			for(OrderServiceRecordVo recordVo:orderServiceVo.getRecordVos()){
				
				OrderServiceRecordResult recordResult = new OrderServiceRecordResult();
				BeanUtil.copyProperties(recordResult, recordVo);
				
				
				List<OrderServicePictureResult> picResults = new ArrayList<OrderServicePictureResult>();
				for(OrderServicePictureVo picVo:recordVo.getPicVos()){
					OrderServicePictureResult picResult = new OrderServicePictureResult();
					BeanUtil.copyProperties(picResult, picVo);
					picResults.add(picResult);
				}
				recordResult.setPicResults(picResults);
				
				recordResults.add(recordResult);
			}
			
			result.setRecordResults(recordResults);
			
			/** ORDER对象 **/
			OrderResult orderResult = new OrderResult();
			
			if(null != orderServiceVo.getOrderVo()){
				BeanUtil.copyProperties(orderResult, orderServiceVo.getOrderVo());
			}
			
			Long tradeId = orderResult.getFkTradeId();
			if(tradeId != null){
				Trade trade = this.tradeMapper.selectByPrimaryKey(tradeId);
				if(trade != null){
					// 支付时间
					orderResult.setPayTime(trade.getPayTime());
					// 交易金额
					orderResult.setOrderPaymentAmount(trade.getTradeShouldAmount());
				}
			}
			
			result.setOrderResult(orderResult);
			
			
		}else{
			error.setErrorCode(SystemErrorCode.OrderServiceErrorCode.ORDER_SERVICE_INVALID);
    		error.setErrorMessage("售后单无效");
    		return result;
		}
		return result;
	}
	
	
	/**
	 * 售后单列表
	 * @param page
	 * @param params
	 * @return
	 */
	public Page<OrderServiceResult> getOrderServiceLists(Page<OrderServiceResult> page, OrderServiceParams params){
		
		int pageNo = params.getPage_no() == null?0:params.getPage_no();
		int pageSize = params.getPage_size() == null?10:params.getPage_size();
		
		List<OrderServiceVo> serviceVos = orderServiceDao.getOrderServiceVoLists(new RowBounds((pageNo-1)*pageSize, pageSize), params);
		
		List<OrderServiceResult> result = new ArrayList<OrderServiceResult>();
		
		for(OrderServiceVo serviceVo:serviceVos){
			OrderServiceResult orderServiceResult = new OrderServiceResult();
			BeanUtil.copyProperties(orderServiceResult, serviceVo);
			result.add(orderServiceResult);
		}
		
		Long count = orderServiceDao.countOrderServiceVoLists(params);
		page.setResult(result);
		page.setTotalCount(count);
		return page;
	}
    
	/**
	 * 订单级别售后完成，生成预分账
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月21日 下午2:20:11 
	 * @param orderService
	 */
	public void orderServiceSplit(OrderService orderService){
		
		Long orderId = orderService.getFkOrderId();				 // 订单ID

		BigDecimal refundPrice = orderService.getRefundPrice();  // 退款金额
		
		SuccessResult is_success = new SuccessResult();
		
		OrderPreSplitParams params = new OrderPreSplitParams();
		
		// 订单ID
		params.setOrder_id(orderId);
		// 操作类型 退款
		params.setTran_type(OrderPreSplitTranType.退款.getKey());
		// 退款金额
		params.setRefund_amount(refundPrice);
		
		
		orderPreSplitServiceImpl.preSplit(params, is_success);
		
		if(!is_success.isIs_success()){
			log.info("售后单ID"+orderService.getId()+"预分账创建失败");
		}
		
	}
    
}












