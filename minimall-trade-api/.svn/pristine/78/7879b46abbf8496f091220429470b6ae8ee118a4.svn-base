package com.jbh360.trade.vo.rs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbh360.common.base.PageUtil;

public class OrderExpressListResult extends PageUtil{
	
	public OrderExpressListResult() {
		super();
	}

	public OrderExpressListResult(int pageSize, int pageNo, long totalCount) {
		super(pageSize, pageNo, totalCount);
	}

	private List<OrderExpressResult> orderExpressResults;
	
	@XmlElementWrapper(name="order_express")
	@XmlElements({
		@XmlElement(name="order_expres",type=OrderExpressResult.class)		
	})
	@JsonProperty("order_express")
	public List<OrderExpressResult> getOrderExpressResults() {
		return orderExpressResults;
	}

	public void setOrderExpressResults(List<OrderExpressResult> orderExpressResults) {
		this.orderExpressResults = orderExpressResults;
	}
}
