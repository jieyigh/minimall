package com.jbh360.trade.vo.rs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderServiceDealTypeList {
	
	private List<OrderServiceDealType> dealTypes;

	@XmlElementWrapper(name="deal_types")
	@XmlElements({
		@XmlElement(name="deal_type",type=OrderServiceDealType.class)		
	})
	@JsonProperty("deal_types")
	public List<OrderServiceDealType> getDealTypes() {
		return dealTypes;
	}

	public void setDealTypes(List<OrderServiceDealType> dealTypes) {
		this.dealTypes = dealTypes;
	}
	
	
	
}
