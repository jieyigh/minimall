package com.jbh360.trade.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StepPriceResult implements Serializable {
	private Long productId;
	private List<PriceResult> prices;
	
	@XmlElement(name="product_id")
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@XmlElementWrapper(name="prices")
	@XmlElements({
		@XmlElement(name="price",type=PriceResult.class)		
	})
	@JsonProperty("prices")
	public List<PriceResult> getPrices() {
		return prices;
	}

	public void setPrices(List<PriceResult> prices) {
		this.prices = prices;
	}
	
	
	
	
}
