package com.jbh360.trade.dao;

import java.util.Map;

import com.jbh360.goods.entity.Product;
import com.jbh360.goods.entity.ProductSku;
import com.jbh360.trade.vo.rs.TradeVo;

/**  
* @Title: TradeDao.java
* @Package com.jbh360.trade.dao
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月8日 下午5:27:57
* @version V3.0  
*/
public interface TradeDao {
	
	public Map<String,Object> getPrice(Map<String, Object> map);
	
	public Long getNo(String seq_name);
	
	public void update(Map<String, Object> map);
	
	public void insertPreferentialRecord(Map<String, Object> map);
	
	
	public TradeVo selectByPrimaryKeyForUpdate(Long id);
	
	public TradeVo selectByTradeNoForUpdate(String trade_no); 
	
	public TradeVo selectByPayNoForUpdate(String pay_no);
	
	public Product selectGdsProductByPrimaryKeyForUpdate(Long id);
    
    public ProductSku selectGdsProductSkuByPrimaryKeyForUpdate(Long id);
}
