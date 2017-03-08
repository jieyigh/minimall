package com.jbh360.trade.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopCartListResult {
	private List<ShopResult> shops;
	
	@XmlElementWrapper(name="shops")
	@XmlElements({
		@XmlElement(name="shop",type=ShopResult.class)		
	})
	@JsonProperty("shops")
	public List<ShopResult> getShops() {
		return shops;
	}
	public void setShops(List<ShopResult> shops) {
		this.shops = shops;
	}
	
}
