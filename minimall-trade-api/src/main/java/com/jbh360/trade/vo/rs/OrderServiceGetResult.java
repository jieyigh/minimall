package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;

public class OrderServiceGetResult {

	private OrderServiceResult orderServiceResult;

	@XmlElement(name = "order_service")
	public OrderServiceResult getOrderServiceResult() {
		return orderServiceResult;
	}

	public void setOrderServiceResult(OrderServiceResult orderServiceResult) {
		this.orderServiceResult = orderServiceResult;
	}
	
	
	
	
	
}
