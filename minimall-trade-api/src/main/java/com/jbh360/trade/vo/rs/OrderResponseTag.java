package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;

import com.jbh360.common.base.SuccessResult;

public class OrderResponseTag {
	
	private PayResult payResult;
	
	
	private OrderExpressGetResult orderExpressGetResult;
	
	
	private OrderExpressListResult orderExpressListResult;
	
	
	private OrderPayResult orderPayResult;
	
	
	private SuccessResult closeSuccessResult;
	
	
	
	
	
	private ExpressAmountResult expressAmountResult;
	
	
	private OrderReceiverEditGetResult orderReceiverEditGetResult;
	
	
	
	
	
	@XmlElement(name="minimall_order_receiver_edit_response")
	public OrderReceiverEditGetResult getOrderReceiverEditGetResult() {
		return orderReceiverEditGetResult;
	}

	public void setOrderReceiverEditGetResult(
			OrderReceiverEditGetResult orderReceiverEditGetResult) {
		this.orderReceiverEditGetResult = orderReceiverEditGetResult;
	}

	@XmlElement(name="minimall_order_express_compute_response")
	public ExpressAmountResult getExpressAmountResult() {
		return expressAmountResult;
	}

	public void setExpressAmountResult(ExpressAmountResult expressAmountResult) {
		this.expressAmountResult = expressAmountResult;
	}
	
	
	
	
	
	@XmlElement(name="minimall_order_close_response")
	public SuccessResult getCloseSuccessResult() {
		return closeSuccessResult;
	}

	public void setCloseSuccessResult(SuccessResult closeSuccessResult) {
		this.closeSuccessResult = closeSuccessResult;
	}

	@XmlElement(name="minimall_order_pay_order_get_response")
	public OrderPayResult getOrderPayResult() {
		return orderPayResult;
	}

	public void setOrderPayResult(OrderPayResult orderPayResult) {
		this.orderPayResult = orderPayResult;
	}

	@XmlElement(name="minimall_trade_express_list_response")
	public OrderExpressListResult getOrderExpressListResult() {
		return orderExpressListResult;
	}

	public void setOrderExpressListResult(
			OrderExpressListResult orderExpressListResult) {
		this.orderExpressListResult = orderExpressListResult;
	}

	@XmlElement(name="minimall_trade_express_get_response")
	public OrderExpressGetResult getOrderExpressGetResult() {
		return orderExpressGetResult;
	}

	public void setOrderExpressGetResult(OrderExpressGetResult orderExpressGetResult) {
		this.orderExpressGetResult = orderExpressGetResult;
	}

	@XmlElement(name="minimall_order_pay_response")
	public PayResult getPayResult() {
		return payResult;
	}

	public void setPayResult(PayResult payResult) {
		this.payResult = payResult;
	}
	
	

}
