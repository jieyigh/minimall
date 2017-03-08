package com.jbh360.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.jbh360.trade.vo.StoreOrderListParams;
import com.jbh360.trade.vo.StoreOrderResult;
import com.jbh360.trade.vo.UcOrderListParams;
import com.jbh360.trade.vo.UcOrderResult;
import com.jbh360.trade.vo.OrderResult;

public interface OrderListDao {

	List<StoreOrderResult> getStoreOrderList(RowBounds rowBounds, @Param("param")StoreOrderListParams params);
	
	Long countStoreOrderList(@Param("param")StoreOrderListParams params);
	
	List<UcOrderResult> getUcOrderList(RowBounds rowBounds, @Param("param")UcOrderListParams params);
	
	Long countUcOrderList(@Param("param")UcOrderListParams params);
	
	OrderResult getOrder(@Param("id")Long id);
}
