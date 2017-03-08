package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class PurchaseCartAddResponseTag {
	
	private PurchaseCartResult purchaseCartResult;
	
	@XmlElement(name="minimall_purchase_cart_add_response")
	public PurchaseCartResult getPurchaseCartResult() {
		return purchaseCartResult;
	}

	public void setPurchaseCartResult(PurchaseCartResult purchaseCartResult) {
		this.purchaseCartResult = purchaseCartResult;
	}

	
}
