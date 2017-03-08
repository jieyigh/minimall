package com.jbh360.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.exception.SystemErrorCode.OrderExtendCode;
import com.jbh360.common.utils.Constants.OrderServiceState;
import com.jbh360.common.utils.Constants.OrderStateCode;
import com.jbh360.trade.entity.Order;
import com.jbh360.trade.mapper.OrderMapper;
import com.jbh360.trade.vo.OrderExtendParams;
@Service
public class OrderExtendServiceImp {
	// 默认延长时间为5天
	private final Integer EXTEND_TIME = 5*24;
	@Autowired
	private OrderMapper orderMapper;
	/**
	 * 延长收货
	 * @author : liguosheng 
	 * @param params 
	 * @param successResult
	 * @CreateDate : 2015年10月26日 上午10:58:35 
	 */
	public void extend(OrderExtendParams params, SuccessResult successResult) {
		Long orderId =	params.getOrder_id();
		// 延迟时长（单位：小时）
		Integer eSingTime = params.getExtend_sing_time() == null?EXTEND_TIME : params.getExtend_sing_time();
		
		Order order = this.orderMapper.selectByPrimaryKey(orderId);
		
		if(order == null){
			throw new ServiceException("订单不存在",OrderExtendCode.ORDER_NOT_EXIST_ERROR);
		}
		
		// 已经延长不能再二次延长
		if(order.getExtendSingTime() != null){
			throw new ServiceException("订单不能做二次延长收货时长",OrderExtendCode.ORDER_EXTEND_NOT_AGAIN_ERROR);
		}
		
		//所有货物发完时间 	如果为空则报错
		if(order.getLastExpressTime() == null){
			throw new ServiceException("所有货物发完才能申请售后",OrderExtendCode.ORDER_NOT_EXIST_ERROR);
		}
		
		// 所有货物签收完时间 	如果不为空则报错
		if(order.getLastSignTime() != null){
			throw new ServiceException("所有货物已签收",OrderExtendCode.ORDER_NOT_EXIST_ERROR);
		}
		
		// 售后中不能延长时间
		if(OrderServiceState.售后中.getKey().equals(order.getStateCode())){
			throw new ServiceException("订单售后中，不能申请延长收货时间",OrderExtendCode.ORDER_NOT_EXTEND_FOR_SERVICE);
		}
		
		// 延长时长
		order.setExtendSingTime(eSingTime);
		// 延长收货原因
		order.setExtendSingTimeReson(params.getExtend_sing_time_reson());
		
		int count = this.orderMapper.updateByPrimaryKey(order);
		
		if(count != 1){
			throw new ServiceException("订单保存失败",OrderExtendCode.ORDER_SAVE_FAIL_ERROR);
		}
		
		successResult.setIs_success(true);
		
	}
	
}
