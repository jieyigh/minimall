package com.jbh360.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class PriceResult implements Serializable{

	/** 
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午1:13:08 
	 * longPriceResult.javacom.jbh360.trade.vominimall-trade-api 
	 */  
	private static final long serialVersionUID = 1L;
	
	private Integer startCount;
	private BigDecimal sellPrice;
	
	@XmlElement(name="start_count")
	public Integer getStartCount() {
		return startCount;
	}
	public void setStartCount(Integer startCount) {
		this.startCount = startCount;
	}
	
	@XmlElement(name="sell_price")
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	

}
