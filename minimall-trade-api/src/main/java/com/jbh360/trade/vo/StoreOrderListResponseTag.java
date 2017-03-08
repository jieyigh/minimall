package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class StoreOrderListResponseTag {
	
	private StoreOrderListResult storeOrderListResult;

	@XmlElement(name="minimall_store_order_list")
	public StoreOrderListResult getStoreOrderListResult() {
		return storeOrderListResult;
	}

	public void setStoreOrderListResult(StoreOrderListResult storeOrderListResult) {
		this.storeOrderListResult = storeOrderListResult;
	}

	
}
