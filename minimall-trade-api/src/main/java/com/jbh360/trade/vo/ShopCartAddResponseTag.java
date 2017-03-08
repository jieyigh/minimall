package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class ShopCartAddResponseTag {
	
	private ShopCartResult shopCartResult;
	
	@XmlElement(name="minimall_shop_cart_add_response")
	public ShopCartResult getShopCartResult() {
		return shopCartResult;
	}

	public void setShopCartResult(ShopCartResult shopCartResult) {
		this.shopCartResult = shopCartResult;
	}

	
	
}
