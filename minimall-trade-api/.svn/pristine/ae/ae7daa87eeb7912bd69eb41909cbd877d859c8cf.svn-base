package com.jbh360.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;


public class UcOrderResult implements Serializable {
	
	private static final long serialVersionUID = 3605936882711862171L;

	private Long order_id;
	
	private String order_no;
	
	private Date order_time;
	
	private String state_code;
	
	private String state_name;
	
	private BigDecimal payment_amount;
	
	private BigDecimal order_freight_amount;
	
	private List<UcOrderGoodResult> good_lists;

	@XmlElement(name="order_id")
	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	@XmlElement(name="order_no")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@XmlElement(name="order_time")
	public Date getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Date order_time) {
		this.order_time = order_time;
	}

	@XmlElement(name="state_code")
	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	@XmlElement(name="state_name")
	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	@XmlElement(name="payment_amount")
	public BigDecimal getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_amount(BigDecimal payment_amount) {
		this.payment_amount = payment_amount;
	}

	@XmlElement(name="order_freight_amount")
	public BigDecimal getOrder_freight_amount() {
		return order_freight_amount;
	}

	public void setOrder_freight_amount(BigDecimal order_freight_amount) {
		this.order_freight_amount = order_freight_amount;
	}

	@XmlElementWrapper(name="good_lists")
	@XmlElements({
		@XmlElement(name="good_list",type=UcOrderGoodResult.class)		
	})
	public List<UcOrderGoodResult> getGood_lists() {
		return good_lists;
	}

	public void setGood_lists(List<UcOrderGoodResult> good_lists) {
		this.good_lists = good_lists;
	}

	
}
