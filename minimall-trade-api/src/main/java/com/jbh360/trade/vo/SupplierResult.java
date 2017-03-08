package com.jbh360.trade.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupplierResult implements Serializable{

	/** 
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午1:00:48 
	 * longSupplierResult.javacom.jbh360.trade.vominimall-trade-api 
	 */  
	private static final long serialVersionUID = 1L;
	
	// 商户店铺ID
	private Long sellerStoreId;
	// 商户店铺名称
	private String sellerStoreName;
	// 商户掌柜ID
	private Long sellerMemberId;
	private List<PurchaseCartResult> purchaseCarts;

	@XmlElementWrapper(name="purchases")
	@XmlElements({
		@XmlElement(name="purchase",type=PurchaseCartResult.class)		
	})
	@JsonProperty("purchases")
	public List<PurchaseCartResult> getPurchaseCarts() {
		return purchaseCarts;
	}
	public void setPurchaseCarts(List<PurchaseCartResult> purchaseCarts) {
		this.purchaseCarts = purchaseCarts;
	}
	
	@XmlElement(name="seller_store_id")
	public Long getSellerStoreId() {
		return sellerStoreId;
	}
	public void setSellerStoreId(Long sellerStoreId) {
		this.sellerStoreId = sellerStoreId;
	}
	
	@XmlElement(name="seller_member_id")
	public Long getSellerMemberId() {
		return sellerMemberId;
	}
	public void setSellerMemberId(Long sellerMemberId) {
		this.sellerMemberId = sellerMemberId;
	}
	
	@XmlElement(name="seller_store_name")
	public String getSellerStoreName() {
		return sellerStoreName;
	}
	public void setSellerStoreName(String sellerStoreName) {
		this.sellerStoreName = sellerStoreName;
	}
}	
