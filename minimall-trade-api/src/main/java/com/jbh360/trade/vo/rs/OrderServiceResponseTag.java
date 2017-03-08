package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;

import com.jbh360.common.base.SuccessResult;

public class OrderServiceResponseTag {

	private OrderServiceDealTypeList orderServiceDealTypeList;
	
	private OrderServiceRefundReasonTypeList orderServiceRefundReasonTypeList;
	
	private SuccessResult agreeSuccessResult;
	
	private SuccessResult refuseSuccessResult;
	
	private SuccessResult consultSuccessResult;
	
	private SuccessResult cancelConsultSuccessResult;
	
	private SuccessResult closeSuccessResult;
	
	private SuccessResult expressSuccessResult;
	
	private SuccessResult confirmReceivingSuccessResult;
	
	private SuccessResult refuseReceivingSuccessResult;
	
	private SuccessResult checkPriceSuccessResult;
	
	
	private OrderServiceGetResult orderServiceGetResult;
	
	private OrderServiceListResult orderServiceListResult;
	
	private OrderServiceGetResult orderServiceApplyResult;
	
	
	private OrderServiceMaxRefundPrice orderServiceMaxRefundPrice;
	
	
	
	@XmlElement(name="minimall_order_service_max_refund_price_response")
	public OrderServiceMaxRefundPrice getOrderServiceMaxRefundPrice() {
		return orderServiceMaxRefundPrice;
	}

	public void setOrderServiceMaxRefundPrice(
			OrderServiceMaxRefundPrice orderServiceMaxRefundPrice) {
		this.orderServiceMaxRefundPrice = orderServiceMaxRefundPrice;
	}

	@XmlElement(name="minimall_order_service_apply_response")
	public OrderServiceGetResult getOrderServiceApplyResult() {
		return orderServiceApplyResult;
	}

	public void setOrderServiceApplyResult(
			OrderServiceGetResult orderServiceApplyResult) {
		this.orderServiceApplyResult = orderServiceApplyResult;
	}

	@XmlElement(name="minimall_order_service_list_response")
	public OrderServiceListResult getOrderServiceListResult() {
		return orderServiceListResult;
	}

	public void setOrderServiceListResult(
			OrderServiceListResult orderServiceListResult) {
		this.orderServiceListResult = orderServiceListResult;
	}

	@XmlElement(name="minimall_order_service_get_response")
	public OrderServiceGetResult getOrderServiceGetResult() {
		return orderServiceGetResult;
	}

	public void setOrderServiceGetResult(OrderServiceGetResult orderServiceGetResult) {
		this.orderServiceGetResult = orderServiceGetResult;
	}

	@XmlElement(name="minimall_order_service_price_check_response")
	public SuccessResult getCheckPriceSuccessResult() {
		return checkPriceSuccessResult;
	}

	public void setCheckPriceSuccessResult(SuccessResult checkPriceSuccessResult) {
		this.checkPriceSuccessResult = checkPriceSuccessResult;
	}

	@XmlElement(name="minimall_order_service_receiving_confirm_response")
	public SuccessResult getConfirmReceivingSuccessResult() {
		return confirmReceivingSuccessResult;
	}

	public void setConfirmReceivingSuccessResult(
			SuccessResult confirmReceivingSuccessResult) {
		this.confirmReceivingSuccessResult = confirmReceivingSuccessResult;
	}

	@XmlElement(name="minimall_order_service_receiving_refuse_response")
	public SuccessResult getRefuseReceivingSuccessResult() {
		return refuseReceivingSuccessResult;
	}

	public void setRefuseReceivingSuccessResult(
			SuccessResult refuseReceivingSuccessResult) {
		this.refuseReceivingSuccessResult = refuseReceivingSuccessResult;
	}

	@XmlElement(name="minimall_order_service_express_set_response")
	public SuccessResult getExpressSuccessResult() {
		return expressSuccessResult;
	}

	public void setExpressSuccessResult(SuccessResult expressSuccessResult) {
		this.expressSuccessResult = expressSuccessResult;
	}

	@XmlElement(name="minimall_order_service_close_response")
	public SuccessResult getCloseSuccessResult() {
		return closeSuccessResult;
	}

	public void setCloseSuccessResult(SuccessResult closeSuccessResult) {
		this.closeSuccessResult = closeSuccessResult;
	}

	@XmlElement(name="minimall_order_service_consult_cancel_response")
	public SuccessResult getCancelConsultSuccessResult() {
		return cancelConsultSuccessResult;
	}

	public void setCancelConsultSuccessResult(
			SuccessResult cancelConsultSuccessResult) {
		this.cancelConsultSuccessResult = cancelConsultSuccessResult;
	}

	@XmlElement(name="minimall_order_service_consult_response")
	public SuccessResult getConsultSuccessResult() {
		return consultSuccessResult;
	}

	public void setConsultSuccessResult(SuccessResult consultSuccessResult) {
		this.consultSuccessResult = consultSuccessResult;
	}

	@XmlElement(name="minimall_order_service_refuse_response")
	public SuccessResult getRefuseSuccessResult() {
		return refuseSuccessResult;
	}

	public void setRefuseSuccessResult(SuccessResult refuseSuccessResult) {
		this.refuseSuccessResult = refuseSuccessResult;
	}

	@XmlElement(name="minimall_order_service_agree_response")
	public SuccessResult getAgreeSuccessResult() {
		return agreeSuccessResult;
	}

	public void setAgreeSuccessResult(SuccessResult agreeSuccessResult) {
		this.agreeSuccessResult = agreeSuccessResult;
	}


	@XmlElement(name="minimall_order_service_deal_type_response")
	public OrderServiceDealTypeList getOrderServiceDealTypeList() {
		return orderServiceDealTypeList;
	}

	public void setOrderServiceDealTypeList(
			OrderServiceDealTypeList orderServiceDealTypeList) {
		this.orderServiceDealTypeList = orderServiceDealTypeList;
	}

	@XmlElement(name="minimall_order_service_refund_reason_type_response")
	public OrderServiceRefundReasonTypeList getOrderServiceRefundReasonTypeList() {
		return orderServiceRefundReasonTypeList;
	}

	public void setOrderServiceRefundReasonTypeList(
			OrderServiceRefundReasonTypeList orderServiceRefundReasonTypeList) {
		this.orderServiceRefundReasonTypeList = orderServiceRefundReasonTypeList;
	}
	
	
	
}
