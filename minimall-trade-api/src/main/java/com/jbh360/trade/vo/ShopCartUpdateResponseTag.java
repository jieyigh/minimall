package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jbh360.common.base.SuccessResult;

@XmlRootElement(name="xml")
public class ShopCartUpdateResponseTag {
	
	private SuccessResult is_success;
	
	@XmlElement(name="minimall_shop_cart_update_response")
	public SuccessResult getIs_success() {
		return is_success;
	}

	public void setIs_success(SuccessResult is_success) {
		this.is_success = is_success;
	}

	
}
