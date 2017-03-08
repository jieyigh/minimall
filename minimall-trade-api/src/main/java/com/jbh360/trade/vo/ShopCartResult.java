package com.jbh360.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

/**
 * 购物车结果集
 * @author : liguosheng 
 * @CreateDate : 2015年9月25日 下午4:32:03
 */
public class ShopCartResult implements Serializable {

	/** 
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月25日 下午4:32:35 
	 * longShopCartResult.javacom.jbh360.trade.vominimall-trade-api 
	 */  
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Short goodsType;
	private Long fkShopStoreId;
	private String shopStoreName;
	private Long fkShopMemberId;
	private String shopStoreMemberName;
	private Long fkCustomerMemberId;
	private String customerMemberName;
	private String customerMemberMobile;
	private Long fkSupplierId;
	private Long fkSupplierStoreId;
	private Long fkSupplierStoreMemberId;
	private Long fkProductId;
	private Long fkProductSkuId;
	private Integer versionNo;
	private Long fkGoodsId;
	private Long fkStoreGoodsSkuId;
	private String productName;
	private String productLogoRsurl;
	private String productSkuBarcode;
	private String skuPropertiesName;
	private Integer buyCount;
	private BigDecimal productUnitPrice;
	private BigDecimal frugalPrice;
	private BigDecimal totalPrice;
	private Long fkPromotionId;
	private Short state;
	private Date createTime;
	private Date lastUpdateTime;
	private Long createUserId;
	private Long lastUpdateUserId;
	private String supplierStoreName;
	
	private Short is7days;
	private Short freightType;
	
	// 放在cookie中的key，标志唯一的用户，如果传入空，会创建一个
	private String cookiesKey;
	
	@XmlElement(name="shop_cart_id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@XmlElement(name="goods_type")
	public Short getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(Short goodsType) {
		this.goodsType = goodsType;
	}
	
	@XmlElement(name="shop_store_id")
	public Long getFkShopStoreId() {
		return fkShopStoreId;
	}
	public void setFkShopStoreId(Long fkShopStoreId) {
		this.fkShopStoreId = fkShopStoreId;
	}
	
	@XmlElement(name="shop_store_name")
	public String getShopStoreName() {
		return shopStoreName;
	}
	public void setShopStoreName(String shopStoreName) {
		this.shopStoreName = shopStoreName;
	}
	
	@XmlElement(name="shop_member_id")
	public Long getFkShopMemberId() {
		return fkShopMemberId;
	}
	public void setFkShopMemberId(Long fkShopMemberId) {
		this.fkShopMemberId = fkShopMemberId;
	}
	
	@XmlElement(name="shop_store_member_name")
	public String getShopStoreMemberName() {
		return shopStoreMemberName;
	}
	public void setShopStoreMemberName(String shopStoreMemberName) {
		this.shopStoreMemberName = shopStoreMemberName;
	}
	
	@XmlElement(name="customer_member_id")
	public Long getFkCustomerMemberId() {
		return fkCustomerMemberId;
	}
	public void setFkCustomerMemberId(Long fkCustomerMemberId) {
		this.fkCustomerMemberId = fkCustomerMemberId;
	}
	
	@XmlElement(name="customer_member_name")
	public String getCustomerMemberName() {
		return customerMemberName;
	}
	public void setCustomerMemberName(String customerMemberName) {
		this.customerMemberName = customerMemberName;
	}
	
	@XmlElement(name="customer_member_mobile")
	public String getCustomerMemberMobile() {
		return customerMemberMobile;
	}
	public void setCustomerMemberMobile(String customerMemberMobile) {
		this.customerMemberMobile = customerMemberMobile;
	}
	
	@XmlElement(name="supplier_id")
	public Long getFkSupplierId() {
		return fkSupplierId;
	}
	public void setFkSupplierId(Long fkSupplierId) {
		this.fkSupplierId = fkSupplierId;
	}
	
	@XmlElement(name="supplier_store_id")
	public Long getFkSupplierStoreId() {
		return fkSupplierStoreId;
	}
	public void setFkSupplierStoreId(Long fkSupplierStoreId) {
		this.fkSupplierStoreId = fkSupplierStoreId;
	}
	
	@XmlElement(name="supplier_store_member_id")
	public Long getFkSupplierStoreMemberId() {
		return fkSupplierStoreMemberId;
	}
	public void setFkSupplierStoreMemberId(Long fkSupplierStoreMemberId) {
		this.fkSupplierStoreMemberId = fkSupplierStoreMemberId;
	}
	
	
	@XmlElement(name="product_id")
	public Long getFkProductId() {
		return fkProductId;
	}
	public void setFkProductId(Long fkProductId) {
		this.fkProductId = fkProductId;
	}
	
	@XmlElement(name="product_sku_id")
	public Long getFkProductSkuId() {
		return fkProductSkuId;
	}
	public void setFkProductSkuId(Long fkProductSkuId) {
		this.fkProductSkuId = fkProductSkuId;
	}
	
	@XmlElement(name="version_no")
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	@XmlElement(name="goods_id")
	public Long getFkGoodsId() {
		return fkGoodsId;
	}
	public void setFkGoodsId(Long fkGoodsId) {
		this.fkGoodsId = fkGoodsId;
	}
	
	@XmlElement(name="store_goodsSku_id")
	public Long getFkStoreGoodsSkuId() {
		return fkStoreGoodsSkuId;
	}
	public void setFkStoreGoodsSkuId(Long fkStoreGoodsSkuId) {
		this.fkStoreGoodsSkuId = fkStoreGoodsSkuId;
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
	
	@XmlElement(name="product_sku_barcode")
	public String getProductSkuBarcode() {
		return productSkuBarcode;
	}
	public void setProductSkuBarcode(String productSkuBarcode) {
		this.productSkuBarcode = productSkuBarcode;
	}
	
	@XmlElement(name="properties_name")
	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}
	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
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
	
	@XmlElement(name="promotion_id")
	public Long getFkPromotionId() {
		return fkPromotionId;
	}
	public void setFkPromotionId(Long fkPromotionId) {
		this.fkPromotionId = fkPromotionId;
	}
	
	@XmlElement(name="state")
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
	
	@XmlElement(name="last_update_user_id")
	public Long getLastUpdateUserId() {
		return lastUpdateUserId;
	}
	public void setLastUpdateUserId(Long lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@XmlElement(name="supplier_store_name")
	public String getSupplierStoreName() {
		return supplierStoreName;
	}
	public void setSupplierStoreName(String supplierStoreName) {
		this.supplierStoreName = supplierStoreName;
	}
	@XmlElement(name="is7days")
	public Short getIs7days() {
		return is7days;
	}
	public void setIs7days(Short is7days) {
		this.is7days = is7days;
	}
	@XmlElement(name="freight_type")
	public Short getFreightType() {
		return freightType;
	}
	public void setFreightType(Short freightType) {
		this.freightType = freightType;
	}
	@XmlElement(name="buyer_cookies_key")
	public String getCookiesKey() {
		return cookiesKey;
	}
	public void setCookiesKey(String cookiesKey) {
		this.cookiesKey = cookiesKey;
	}
	
}
