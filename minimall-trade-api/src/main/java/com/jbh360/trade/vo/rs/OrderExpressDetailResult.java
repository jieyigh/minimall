package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;

public class OrderExpressDetailResult {

	
	private Long id;
	
	private Long fkOrderExpressId;
	
	private Long fkOrderDetailId;
	
	private Long fkProductSkuId;
	
	private String skuBarcode;
	
	private String productName;
	
	private Long productCount;
	
	private String productPictureUrl;

	@XmlElement(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "fk_order_express_id")
	public Long getFkOrderExpressId() {
		return fkOrderExpressId;
	}

	public void setFkOrderExpressId(Long fkOrderExpressId) {
		this.fkOrderExpressId = fkOrderExpressId;
	}

	@XmlElement(name = "fk_order_detail_id")
	public Long getFkOrderDetailId() {
		return fkOrderDetailId;
	}

	public void setFkOrderDetailId(Long fkOrderDetailId) {
		this.fkOrderDetailId = fkOrderDetailId;
	}

	@XmlElement(name = "fk_product_sku_id")
	public Long getFkProductSkuId() {
		return fkProductSkuId;
	}

	public void setFkProductSkuId(Long fkProductSkuId) {
		this.fkProductSkuId = fkProductSkuId;
	}

	@XmlElement(name = "sku_barcode:sku")
	public String getSkuBarcode() {
		return skuBarcode;
	}

	public void setSkuBarcode(String skuBarcode) {
		this.skuBarcode = skuBarcode;
	}

	@XmlElement(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@XmlElement(name = "product_count")
	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	@XmlElement(name = "product_picture_url")
	public String getProductPictureUrl() {
		return productPictureUrl;
	}

	public void setProductPictureUrl(String productPictureUrl) {
		this.productPictureUrl = productPictureUrl;
	}
	
	
}
