package com.jbh360.trade.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopResult implements Serializable{

	/** 
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月29日 下午6:38:10 
	 * longShopResult.javacom.jbh360.trade.vominimall-trade-api 
	 */  
	private static final long serialVersionUID = 1L;
	
	private Long storeId;
	private String storeName;
	
	private List<ShopCartResult> shopCarts;

	@XmlElementWrapper(name="shop_cars")
	@XmlElements({
		@XmlElement(name="shop_car",type=ShopCartResult.class)		
	})
	@JsonProperty("shop_cars")
	public List<ShopCartResult> getShopCarts() {
		return shopCarts;
	}

	public void setShopCarts(List<ShopCartResult> shopCarts) {
		this.shopCarts = shopCarts;
	}
	
	@XmlElement(name="store_id")
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	@XmlElement(name="store_name")
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	
}
