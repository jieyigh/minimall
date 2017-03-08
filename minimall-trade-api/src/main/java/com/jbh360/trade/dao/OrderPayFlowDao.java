package com.jbh360.trade.dao;

import com.jbh360.trade.vo.rs.OrderPayFlowVo;

/**
 * 
 * @Title: OrderPayFlowDao.java
 * @Package com.jbh360.trade.dao
 * @ClassName: OrderPayFlowDao
 * @Description: 支付流水
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月9日 上午11:14:36
 * @version V3.0
 */
public interface OrderPayFlowDao {

	public OrderPayFlowVo selectByPayNoForUpdate(String pay_no);
	
	public OrderPayFlowVo selectByFkTradeIdForUpdate(String fk_trade_id);
	
}
