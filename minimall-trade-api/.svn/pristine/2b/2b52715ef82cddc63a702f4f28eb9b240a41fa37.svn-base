package com.jbh360.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.utils.Constants.OrderType;
import com.jbh360.common.utils.Constants.YesOrNo;
import com.jbh360.common.utils.SEQGenerate;
import com.jbh360.marketing.entity.StoreCoupon;
import com.jbh360.marketing.mapper.StoreCouponMapper;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.entity.OrderDetail;
import com.jbh360.trade.entity.OrderDetailExample;
import com.jbh360.trade.entity.PreSplitRecord;
import com.jbh360.trade.entity.PreSplitRecordLog;
import com.jbh360.trade.entity.PreferentialRecord;
import com.jbh360.trade.entity.PreferentialRecordExample;
import com.jbh360.trade.mapper.OrderDetailMapper;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.mapper.PreSplitRecordLogMapper;
import com.jbh360.trade.mapper.PreSplitRecordMapper;
import com.jbh360.trade.mapper.PreferentialRecordMapper;
import com.jbh360.trade.service.OrderPreSplitServiceImpl;
import com.jbh360.trade.service.TradeBillService;
import com.jbh360.trade.utils.OrderConstants.AmountType;
import com.jbh360.trade.utils.OrderConstants.AmountWay;
import com.jbh360.trade.utils.OrderConstants.Identity;
import com.jbh360.trade.utils.OrderConstants.PayState;
import com.jbh360.trade.utils.OrderConstants.PreferentialRecordType;
import com.jbh360.trade.utils.OrderConstants.TranType;
import com.yooyo.util.RestUtil;

/**  
* @Title: TradeBillServiceImpl.java
* @Package com.jbh360.trade.service.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年11月23日 上午10:32:47
* @version V3.0  
*/
@Service
@Lazy
public class TradeBillServiceImpl implements TradeBillService{
	
	private Logger log = Logger.getLogger(OrderPreSplitServiceImpl.class);
	
	@Autowired
	private PreSplitRecordMapper preSplitRecordMapper;
	@Autowired
	private OrderMapper oderMapper;
	@Autowired
	private StoreCouponMapper storeCouponMapper;
	@Autowired
	private PreferentialRecordMapper preferentialRecordMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private PreSplitRecordLogMapper preSplitRecordLogMapper;
	
	
	@Override
	@Async
	public void splitBill(Long orderId) {
		 PreSplitRecordLog preSplitRecordLog = new PreSplitRecordLog();
		 preSplitRecordLog.setFkId(orderId);
		 preSplitRecordLog.setType(TranType.入账.getKey());
		 try{
			 Order order = oderMapper.selectByPrimaryKey(orderId);
			 if(order == null || order.getId() == null || order.getId() == 0l ){
				 throw new Exception("订单id为；" + orderId + "，无法找到数据，无法分账");
			 }
			 preSplitRecordLog.setJson(RestUtil.toJson(order));
			 /**
			  * 自营单
			  */
			 if(order.getOrderType().equals(OrderType.自营单.getKey())){
				 handleSelf(order);
			 }else if(order.getOrderType().equals(OrderType.采购单.getKey())){
				 handlePurchase(order);
			 }else if(order.getOrderType().equals(OrderType.代销单.getKey())){
				 handleAgent(order);
			 }
			 preSplitRecordLog.setIsSuccess(Identity.SUCCESS.isValue());
			 preSplitRecordLog.setFkNo(order.getOrderNo());
		 }catch(ServiceException e){
			 e.printStackTrace();
			 preSplitRecordLog.setExceptionMsg(e.getMessage());
			 preSplitRecordLog.setExceptionName(e.getClass().getName());
			 preSplitRecordLog.setIsSuccess(Identity.FAIL.isValue());
			 log.error(e.getMessage(), e);
		 }catch(Exception e){
			 e.printStackTrace();
			 preSplitRecordLog.setExceptionMsg(e.getMessage());
			 preSplitRecordLog.setExceptionName(e.getClass().getName());
			 preSplitRecordLog.setIsSuccess(Identity.FAIL.isValue());
			 log.error(e.getMessage(), e);
		 }finally {
			 preSplitRecordLog.setCreateTime(new Date());
			 preSplitRecordLogMapper.insertSelective(preSplitRecordLog);
		}
	}

	@Override
	@Async
	public void discountAverageBill(Long orderId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Async
	public void customerService(Long serviceId) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 处理自营单
	 * @param order
	 * @throws Exception
	 */
	private void handleSelf(Order order) throws Exception{
		if(order == null || order.getId() == null || order.getId() == 0l ){
			throw  new NullPointerException("自营订单对象不能为null");
		}
		PreSplitRecord pre = new PreSplitRecord();
		pre.setAmount(order.getOrderTotalAmount());//订单总金额
		pre.setAmountType(AmountType.现金.getKey());
		pre.setAmountWay(AmountWay.钱包.getKey());
		pre.setCreateTime(new Date());
		pre.setFkMemberId(order.getFkBuyeStoreMemberId());
		pre.setFkStoreId(order.getFkBuyerStoreId());
		pre.setFkOrderId(order.getId());
		pre.setOrderType(OrderType.自营单.getKey());
		pre.setPayState(PayState.未结算.getKey());
		pre.setFkStoreId(order.getFkBuyerStoreId());
		pre.setId(SEQGenerate.getId());
		pre.setRemark("自营订单预结算："+order.getOrderNo());
		pre.setTranType(TranType.入账.getKey());
		if(pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
			preSplitRecordMapper.insert(pre);
		}
		if(order.getIsFreeze() != null && order.getIsFreightFree().equals(YesOrNo.NO.getKey())){
			if(order.getOrderFreightAmount() != null && order.getOrderFreightAmount().compareTo(BigDecimal.ZERO) > 0){
				pre.setId(SEQGenerate.getId());
				pre.setAmountType(AmountType.邮费.getKey());
				pre.setAmount(order.getOrderFreightAmount());
				if(pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
					preSplitRecordMapper.insert(pre);
				}
			}
		}
	}
	
	/**
	 * 处理采购单
	 * @param order
	 * @throws Exception
	 */
	private void handlePurchase(Order order) {
		if(order == null || order.getId() == null || order.getId() == 0l ){
			throw  new NullPointerException("采购订单对象不能为null");
		}
		BigDecimal freightDiscountAmount = BigDecimal.ZERO; //邮资卡
		BigDecimal ventureDiscountAmount = BigDecimal.ZERO; //创业基金
		BigDecimal changeFreightDiscountAmount = BigDecimal.ZERO;
		/**
		 * 处理优惠券
		 */
		if(order.getFkCouponId() != null && order.getFkCouponId() != null && order.getFkCouponId() != 0l ){
			StoreCoupon storeCoupon = storeCouponMapper.selectByPrimaryKey(order.getFkCouponId());
			if(storeCoupon != null && storeCoupon.getId() != null &&storeCoupon.getId() != 0l ){
				PreferentialRecordExample preferentialRecordExample = new PreferentialRecordExample();
				preferentialRecordExample.createCriteria().andFkOrderIdEqualTo(order.getId())
															.andFkIdEqualTo(storeCoupon.getId()).andTypeEqualTo(PreferentialRecordType.COUPON.getValue());
				List<PreferentialRecord> preferentialRecords = preferentialRecordMapper.selectByExample(preferentialRecordExample);
				PreferentialRecord preferentialRecord = new PreferentialRecord();
				if(preferentialRecords != null && !preferentialRecords.isEmpty() && preferentialRecords.size() == 1){
					preferentialRecord = preferentialRecords.get(0);
				}
				if(storeCoupon.getCouponType() != null && storeCoupon.getCouponType().equals(2) && preferentialRecord.getDiscountAmount() != null ){ // 判断邮资卡
					freightDiscountAmount = preferentialRecord.getDiscountAmount();
				}else if(storeCoupon.getCouponType() != null && storeCoupon.getCouponType().equals(4) && preferentialRecord.getDiscountAmount() != null){ // 创业基金
					ventureDiscountAmount = preferentialRecord.getDiscountAmount();
				}else{
					//其他暂时优惠不做处理
				}
			}
		}
		/**
		 * 处理改价优惠
		 */
		//免邮
		if(order.getFreightDiscountAmount() != null){
			changeFreightDiscountAmount = order.getFreightDiscountAmount();
		}
		PreSplitRecord pre = new PreSplitRecord();
		pre.setAmount(order.getOrderTotalAmount().add(freightDiscountAmount));//订单总金额 把优惠给找出来 1 创业基金 2 邮资卡
		pre.setAmountType(AmountType.现金.getKey());
		pre.setAmountWay(AmountWay.钱包.getKey());
		pre.setCreateTime(new Date());
		pre.setFkMemberId(order.getFkSellerMemberId());
		pre.setFkStoreId(order.getFkSellerStoreId());
		pre.setFkOrderId(order.getId());
		pre.setOrderType(OrderType.采购单.getKey());
		pre.setPayState(PayState.未结算.getKey());
		pre.setId(SEQGenerate.getId());
		pre.setRemark("采购订单预结算："+order.getOrderNo());
		pre.setTranType(TranType.入账.getKey());
		preSplitRecordMapper.insert(pre);
		if(order.getIsFreeze() != null && order.getIsFreightFree().equals(YesOrNo.NO.getKey())){
			if(order.getOrderFreightAmount() != null && order.getOrderFreightAmount().compareTo(BigDecimal.ZERO) > 0){
				pre.setId(SEQGenerate.getId());
				pre.setAmountType(AmountType.邮费.getKey());
				pre.setAmount(order.getOrderFreightAmount().subtract(freightDiscountAmount).subtract(changeFreightDiscountAmount));
				if(pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
					preSplitRecordMapper.insert(pre);
				}
			}
		}
		if(ventureDiscountAmount != null && ventureDiscountAmount.compareTo(BigDecimal.ZERO) > 0){
			pre.setId(SEQGenerate.getId());
			pre.setAmountType(AmountType.创业基金.getKey());
			pre.setAmount(ventureDiscountAmount);
			if(pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
				preSplitRecordMapper.insert(pre);
			}
		}
	
	}
	/**
	 * 处理代销单(商户和掌柜分钱)
	 * @param order
	 * @throws Exception
	 */
	private void handleAgent(Order order) {
		if(order == null || order.getId() == null || order.getId() == 0l ){
			throw  new NullPointerException("代销订单对象不能为null");
		}
		OrderDetailExample orderDetailExample = new OrderDetailExample();
		orderDetailExample.createCriteria().andFkOrderIdEqualTo(order.getId());
		List<OrderDetail> orderDetails = orderDetailMapper.selectByExample(orderDetailExample);
		if(orderDetails == null || orderDetails.isEmpty()){
			throw new ServiceException("订单No:"+order.getOrderNo()+",数据异常，无法查找到订单明细");
		}
		BigDecimal settlementAmount = BigDecimal.ZERO;
		for (OrderDetail orderDetail : orderDetails) {//算出商户的结算金额+邮费，剩下的即为掌柜提成
			settlementAmount = settlementAmount.add(orderDetail.getSettleUnitPrice().multiply(new BigDecimal(orderDetail.getBuyCount())));
		}
		/***********************商户的计算开始***********************/
		PreSplitRecord pre = new PreSplitRecord();
		pre.setAmount(settlementAmount);
		pre.setAmountType(AmountType.现金.getKey());
		pre.setAmountWay(AmountWay.钱包.getKey());
		pre.setCreateTime(new Date());
		pre.setFkMemberId(order.getFkSellerMemberId());
		pre.setFkStoreId(order.getFkSellerStoreId());
		pre.setFkOrderId(order.getId());
		pre.setOrderType(OrderType.代销单.getKey());
		pre.setPayState(PayState.未结算.getKey());
		pre.setId(SEQGenerate.getId());
		pre.setRemark("处理代销订单预结算："+order.getOrderNo());
		pre.setTranType(TranType.入账.getKey());
		if(settlementAmount.compareTo(BigDecimal.ZERO) > 0 && pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
			preSplitRecordMapper.insert(pre);
		}
		//邮费是由商户
		if(order.getIsFreeze() != null && order.getIsFreightFree().equals(YesOrNo.NO.getKey())){ 
			if(order.getOrderFreightAmount() != null && order.getOrderFreightAmount().compareTo(BigDecimal.ZERO) > 0){
				pre.setId(SEQGenerate.getId());
				pre.setAmountType(AmountType.邮费.getKey());
				pre.setAmount(order.getOrderFreightAmount());
				if(pre.getAmount().compareTo(BigDecimal.ZERO) > 0){
					preSplitRecordMapper.insert(pre);
				}
			}
		}
		/***********************商户的计算结束***********************/
		
		/**********************掌柜的提成开始************************/
		PreSplitRecord shopkeeperPre = new PreSplitRecord();
		shopkeeperPre.setAmount(order.getOrderTotalAmount().subtract(settlementAmount));
		shopkeeperPre.setAmountType(AmountType.现金.getKey());
		shopkeeperPre.setAmountWay(AmountWay.钱包.getKey());
		shopkeeperPre.setCreateTime(new Date());
		shopkeeperPre.setFkMemberId(order.getFkBuyeStoreMemberId());
		shopkeeperPre.setFkStoreId(order.getFkBuyerStoreId());
		shopkeeperPre.setFkOrderId(order.getId());
		shopkeeperPre.setOrderType(OrderType.代销单.getKey());
		shopkeeperPre.setPayState(PayState.未结算.getKey());
		shopkeeperPre.setId(SEQGenerate.getId());
		shopkeeperPre.setRemark("处理代销订单预结算："+order.getOrderNo());
		shopkeeperPre.setTranType(TranType.入账.getKey());
		if(settlementAmount.compareTo(BigDecimal.ZERO) > 0){
			preSplitRecordMapper.insert(shopkeeperPre);
		}
		
		
		
	}
	public static void main(String[] args) {
		BigDecimal settlementAmount = BigDecimal.ZERO;
		for (int i = 0; i <= 100; i++) {
			settlementAmount = settlementAmount.add(new BigDecimal(i));
		}
		System.out.println(settlementAmount);
	}
}
