package com.jbh360.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.jbh360.trade.vo.param.OrderServiceParams;
import com.jbh360.trade.vo.rs.OrderServiceVo;


public interface OrderServiceDao {

	
	public List<OrderServiceVo> getOrderServiceVo(@Param("params")OrderServiceParams params);
	
	
	public List<OrderServiceVo> getOrderServiceVoLists(RowBounds rowBounds, @Param("params")OrderServiceParams params);
	
	public Long countOrderServiceVoLists(@Param("params")OrderServiceParams params);
}	
