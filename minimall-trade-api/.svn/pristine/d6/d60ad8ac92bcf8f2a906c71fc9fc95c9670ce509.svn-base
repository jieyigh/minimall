package com.jbh360.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 采购车结果集
 * @author : liguosheng 
 * @CreateDate : 2015年9月24日 上午9:40:11
 */
public class PurchaseCartResult implements Serializable {
	private Long id;
	// 买家掌柜id
	private Long fkBuyerMemberId;
	// 买家昵称
	private String fkBuyerNickName;
	// 买家店铺id
	private Long fkBuyerStoreId;
	// 商户店铺id
	private Long fkSupplierStoreId;
	// 商户店铺名称
	private String fkSupplierStoreName;
	// 商户店铺掌柜id
	private Long fkSuppliererMemberId;
	// 产品id
	private Long fkProductId;
	// 产品版本号
	private Integer versionNo;
	// 产品名称
	private String productName;
	// 产品logo图
	private String productLogoRsurl;
	// 产品skuid
	private Long fkProductSkuId;
	// 产品barcode
	private String barCode;
	// sku属性名组合字符串
	private String skuPropertiesName;
	// 活动id
	private Long fkGameId;
	// 购买数量
	private Integer buyCount;
	// 产品单价
	private BigDecimal productUnitPrice;
	// 节省价
	private BigDecimal frugalPrice;
	// 总价（单价*数量-折扣）
	private BigDecimal totalPrice;
	// 状态(50正常，-50删除)
	private Short state;
	private Date createTime;
	private Date lastUpdateTime;
	private Long createUserId;
	private Long lastUpdateUserId;
	
	private Short is7days;		
	private Short freightType;	 
	private Short isGuarantee;	// 是否担保交易
	
	private List<PriceResult> prices;
	
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
	
	@XmlElement(name="purchase_id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@XmlElement(name="buyer_member_id")
	public Long getFkBuyerMemberId() {
		return fkBuyerMemberId;
	}
	public void setFkBuyerMemberId(Long fkBuyerMemberId) {
		this.fkBuyerMemberId = fkBuyerMemberId;
	}
	@XmlElement(name="buyer_nick_name")
	public String getFkBuyerNickName() {
		return fkBuyerNickName;
	}
	public void setFkBuyerNickName(String fkBuyerNickName) {
		this.fkBuyerNickName = fkBuyerNickName;
	}
	
	@XmlElement(name="buyer_store_id")
	public Long getFkBuyerStoreId() {
		return fkBuyerStoreId;
	}
	public void setFkBuyerStoreId(Long fkBuyerStoreId) {
		this.fkBuyerStoreId = fkBuyerStoreId;
	}
	
	@XmlElement(name="supplier_store_id")
	public Long getFkSupplierStoreId() {
		return fkSupplierStoreId;
	}
	public void setFkSupplierStoreId(Long fkSupplierStoreId) {
		this.fkSupplierStoreId = fkSupplierStoreId;
	}
	
	@XmlElement(name="supplier_member_id")
	public Long getFkSuppliererMemberId() {
		return fkSuppliererMemberId;
	}
	public void setFkSuppliererMemberId(Long fkSuppliererMemberId) {
		this.fkSuppliererMemberId = fkSuppliererMemberId;
	}
	
	
	@XmlElement(name="product_id")
	public Long getFkProductId() {
		return fkProductId;
	}
	public void setFkProductId(Long fkProductId) {
		this.fkProductId = fkProductId;
	}
	
	@XmlElement(name="version_no")
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	
	@XmlElement(name="product_name")
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@XmlElement(name="product_logo_rsurl")
	public String getProductLogoRsurl() {
		return productLogoRsurl;
	}
	public void setProductLogoRsurl(String productLogoRsurl) {
		this.productLogoRsurl = productLogoRsurl;
	}
	
	@XmlElement(name="product_sku_id")
	public Long getFkProductSkuId() {
		return fkProductSkuId;
	}
	public void setFkProductSkuId(Long fkProductSkuId) {
		this.fkProductSkuId = fkProductSkuId;
	}
	
	@XmlElement(name="bar_code")
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	
	@XmlElement(name="properties_name")
	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}
	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}
	
	@XmlElement(name="game_id")
	public Long getFkGameId() {
		return fkGameId;
	}
	public void setFkGameId(Long fkGameId) {
		this.fkGameId = fkGameId;
	}
	
	@XmlElement(name="buy_count")
	public Integer getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}
	
	@XmlElement(name="product_unit_price")
	public BigDecimal getProductUnitPrice() {
		return productUnitPrice;
	}
	public void setProductUnitPrice(BigDecimal productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}
	
	@XmlElement(name="frugal_price")
	public BigDecimal getFrugalPrice() {
		return frugalPrice;
	}
	public void setFrugalPrice(BigDecimal frugalPrice) {
		this.frugalPrice = frugalPrice;
	}
	
	@XmlElement(name="total_price")
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Short getState() {
		return state;
	}
	public void setState(Short state) {
		this.state = state;
	}
	
	@XmlElement(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@XmlElement(name="last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@XmlElement(name="create_user_id")
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	
	@XmlElement(name="last_update_userId")
	public Long getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public void setLastUpdateUserId(Long lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	
	@XmlElement(name="supplier_store_name")
	public String getFkSupplierStoreName() {
		return fkSupplierStoreName;
	}
	public void setFkSupplierStoreName(String fkSupplierStoreName) {
		this.fkSupplierStoreName = fkSupplierStoreName;
	}
	
	
	
	@XmlElement(name="freight_type")
	public Short getFreightType() {
		return freightType;
	}
	public void setFreightType(Short freightType) {
		this.freightType = freightType;
	}
	
	@XmlElement(name="is_7days")
	public Short getIs7days() {
		return is7days;
	}
	public void setIs7days(Short is7days) {
		this.is7days = is7days;
	}
	
	@XmlElement(name="is_guarantee")
	public Short getIsGuarantee() {
		return isGuarantee;
	}
	public void setIsGuarantee(Short isGuarantee) {
		this.isGuarantee = isGuarantee;
	}
	
	
}
