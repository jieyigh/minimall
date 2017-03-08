package com.jbh360.trade.vo.rs;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class OrderResult {
	
	private Long id;
	
	private Long fkTradeId;
	
	private String orderNo;

	private String stateCode;
	
	private String stateName;
	
	private String receiverName;
	
	private String receiverMobile;
	
	
	private Date payTime;						// 支付时间
		
	private BigDecimal orderPaymentAmount;		// 实付金额
	
	
	
	
	@XmlElement(name = "order_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "order_no")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@XmlElement(name = "state_code")
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	@XmlElement(name = "state_name")
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@XmlElement(name = "receiver_name")
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@XmlElement(name = "receiver_mobile")
	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	@XmlElement(name="trade_id")
	public Long getFkTradeId() {
		return fkTradeId;
	}

	public void setFkTradeId(Long fkTradeId) {
		this.fkTradeId = fkTradeId;
	}

	@XmlElement(name="pay_time")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@XmlElement(name="order_payment_amount")
	public BigDecimal getOrderPaymentAmount() {
		return orderPaymentAmount;
	}

	public void setOrderPaymentAmount(BigDecimal orderPaymentAmount) {
		this.orderPaymentAmount = orderPaymentAmount;
	}
	
	
	
}
