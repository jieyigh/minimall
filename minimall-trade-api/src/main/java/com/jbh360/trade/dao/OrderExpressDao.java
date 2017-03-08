package com.jbh360.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.jbh360.trade.vo.param.OrderExpressParams;
import com.jbh360.trade.vo.rs.OrderExpressVo;



public interface OrderExpressDao {

	public List<OrderExpressVo> getOrderExpressVoLists(RowBounds rowBounds, @Param("params")OrderExpressParams params);
	
	public Long countOrderExpressVoLists(@Param("params")OrderExpressParams params);

	public Long getOrderDetailCount(@Param("order_id") Long orderId);

	/**
	 * 获取已签收发货记录数据
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月28日 下午5:34:08 
	 * @param orderId
	 * @return
	 */
	public Long getExpressAsignDetailCount(@Param("order_id") Long orderId);
	
}
