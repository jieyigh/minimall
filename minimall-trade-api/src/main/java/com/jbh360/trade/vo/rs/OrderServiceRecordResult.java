package com.jbh360.trade.vo.rs;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderServiceRecordResult {

	
	private String opType;
	
	private String opTypeName;
	
	private String opUserType;
	
	private String opUserTypeName;
	
	private String opSign;
	
	private String refundReasonTypeName;
	
	private String dealType;
	
	private String dealTypeName;
	
	private BigDecimal refundPrice;
	
	private String refundRemark;
	
	private String applyPhone;
	
	private String returnAddress;
	
	private String refuseReason;
	
	private Long buyerExpressCompanyId;
	
	private String buyerExpressCompanyName;
	
	private String buyerExpressBillNo;
	
	private String returnRemark;
	
	private String returnPhone;
	
	private String opUserName;
	
	private String opContent;
	
	private Date opTime;
	
	private String remark;
	
	private List<OrderServicePictureResult> picResults;
	
	

	@XmlElementWrapper(name="order_service_pictures")
	@XmlElements({
		@XmlElement(name="order_service_picture",type=OrderServicePictureResult.class)		
	})
	@JsonProperty("order_service_pictures")
	public List<OrderServicePictureResult> getPicResults() {
		return picResults;
	}

	public void setPicResults(List<OrderServicePictureResult> picResults) {
		this.picResults = picResults;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOpTypeName() {
		return opTypeName;
	}

	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}

	public String getOpUserType() {
		return opUserType;
	}

	public void setOpUserType(String opUserType) {
		this.opUserType = opUserType;
	}

	@XmlElement(name = "op_user_type_name")
	public String getOpUserTypeName() {
		return opUserTypeName;
	}

	public void setOpUserTypeName(String opUserTypeName) {
		this.opUserTypeName = opUserTypeName;
	}

	@XmlElement(name = "op_sign")
	public String getOpSign() {
		return opSign;
	}

	public void setOpSign(String opSign) {
		this.opSign = opSign;
	}

	@XmlElement(name = "refund_type_reason_name")
	public String getRefundReasonTypeName() {
		return refundReasonTypeName;
	}

	public void setRefundReasonTypeName(String refundReasonTypeName) {
		this.refundReasonTypeName = refundReasonTypeName;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	@XmlElement(name = "deal_type_name")
	public String getDealTypeName() {
		return dealTypeName;
	}

	public void setDealTypeName(String dealTypeName) {
		this.dealTypeName = dealTypeName;
	}

	@XmlElement(name = "refund_price")
	public BigDecimal getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}

	@XmlElement(name = "refund_remark")
	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	@XmlElement(name = "apply_phone")
	public String getApplyPhone() {
		return applyPhone;
	}

	public void setApplyPhone(String applyPhone) {
		this.applyPhone = applyPhone;
	}

	@XmlElement(name = "return_address")
	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	@XmlElement(name = "refuse_reason")
	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Long getBuyerExpressCompanyId() {
		return buyerExpressCompanyId;
	}

	public void setBuyerExpressCompanyId(Long buyerExpressCompanyId) {
		this.buyerExpressCompanyId = buyerExpressCompanyId;
	}

	@XmlElement(name = "buyer_express_company_name")
	public String getBuyerExpressCompanyName() {
		return buyerExpressCompanyName;
	}

	public void setBuyerExpressCompanyName(String buyerExpressCompanyName) {
		this.buyerExpressCompanyName = buyerExpressCompanyName;
	}

	@XmlElement(name = "buyer_express_bill_no")
	public String getBuyerExpressBillNo() {
		return buyerExpressBillNo;
	}

	public void setBuyerExpressBillNo(String buyerExpressBillNo) {
		this.buyerExpressBillNo = buyerExpressBillNo;
	}

	@XmlElement(name = "return_remark")
	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}

	@XmlElement(name = "return_phone")
	public String getReturnPhone() {
		return returnPhone;
	}

	public void setReturnPhone(String returnPhone) {
		this.returnPhone = returnPhone;
	}

	public String getOpUserName() {
		return opUserName;
	}

	public void setOpUserName(String opUserName) {
		this.opUserName = opUserName;
	}

	public String getOpContent() {
		return opContent;
	}

	public void setOpContent(String opContent) {
		this.opContent = opContent;
	}

	@XmlElement(name = "op_time")
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
