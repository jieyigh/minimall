package com.jbh360.trade.vo.rs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderServiceRefundReasonTypeList {

	private List<OrderServiceRefundReasonType> reasonTypes;

	@XmlElementWrapper(name="reason_types")
	@XmlElements({
		@XmlElement(name="reason_type",type=OrderServiceRefundReasonType.class)		
	})
	@JsonProperty("reason_types")
	public List<OrderServiceRefundReasonType> getReasonTypes() {
		return reasonTypes;
	}

	public void setReasonTypes(List<OrderServiceRefundReasonType> reasonTypes) {
		this.reasonTypes = reasonTypes;
	}
	
	
}
