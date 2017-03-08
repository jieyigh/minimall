package com.jbh360.trade.dao;

import com.jbh360.trade.vo.rs.OrderVo;

/**
 * 
 * @Title: OrderDao.java
 * @Package com.jbh360.trade.dao
 * @ClassName: OrderDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月8日 下午8:46:47
 * @version V3.0
 */
public interface OrderDao {

	public OrderVo selectByPrimaryKeyForUpdate(Long id);
	
	public OrderVo selectByFkTradeIdForUpdate(Long fk_trade_id);
	
	public OrderVo selectByOrderNoForUpdate(String order_no);
}
