package com.jbh360.trade.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jbh360.trade.vo.PriceResult;
import com.jbh360.trade.vo.PurchaseCartResult;

public interface PurchaseCartDao {

	List<PurchaseCartResult> findListByMemberId(@Param("member_id") Long member_id);
	
	/**
	 * 获取产品阶梯集合
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午2:49:41 
	 * @param productId
	 * @return
	 */
	List<PriceResult> getStepPrices(@Param("product_id") Long productId);
	
	
	/**
	 * 根据产品ID与数量，查询阶梯价格
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月9日 上午10:38:57 
	 * @param map
	 * @return
	 */
	BigDecimal getStepPriceByProductSaleCount(Map<String, Object> map);
}	
