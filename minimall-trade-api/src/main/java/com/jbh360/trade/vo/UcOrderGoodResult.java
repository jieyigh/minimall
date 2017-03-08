package com.jbh360.trade.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

public class UcOrderGoodResult implements Serializable {
	
	private static final long serialVersionUID = 3071450644066391903L;
	
	private Long product_id;

	private String goods_name;
	
	private String product_logo_rsurl;
	
	private String sku_property_value;
	
	private BigDecimal sale_unit_price;
	
	private Integer buy_count;
	
	private Short service_state;
	
	private Short has_comment;
	
	private Long fk_store_goods_id;
	
	private Long order_detail_id;
	
	private String sku_barcode;
	
	private Short express_state;

	@XmlElement(name="goods_name")
	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	@XmlElement(name="product_logo_rsurl")
	public String getProduct_logo_rsurl() {
		return product_logo_rsurl;
	}

	public void setProduct_logo_rsurl(String product_logo_rsurl) {
		this.product_logo_rsurl = product_logo_rsurl;
	}

	@XmlElement(name="sku_property_value")
	public String getSku_property_value() {
		return sku_property_value;
	}

	public void setSku_property_value(String sku_property_value) {
		this.sku_property_value = sku_property_value;
	}

	@XmlElement(name="sale_unit_price")
	public BigDecimal getSale_unit_price() {
		return sale_unit_price;
	}

	public void setSale_unit_price(BigDecimal sale_unit_price) {
		this.sale_unit_price = sale_unit_price;
	}

	@XmlElement(name="buy_count")
	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	@XmlElement(name="service_state")
	public Short getService_state() {
		return service_state;
	}

	public void setService_state(Short service_state) {
		this.service_state = service_state;
	}

	@XmlElement(name="product_id")
	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	@XmlElement(name="has_comment")
	public Short getHas_comment() {
		return has_comment;
	}

	public void setHas_comment(Short has_comment) {
		this.has_comment = has_comment;
	}

	@XmlElement(name="fk_store_goods_id")
	public Long getFk_store_goods_id() {
		return fk_store_goods_id;
	}

	public void setFk_store_goods_id(Long fk_store_goods_id) {
		this.fk_store_goods_id = fk_store_goods_id;
	}

	@XmlElement(name="order_detail_id")
	public Long getOrder_detail_id() {
		return order_detail_id;
	}

	public void setOrder_detail_id(Long order_detail_id) {
		this.order_detail_id = order_detail_id;
	}

	@XmlElement(name="sku_barcode")
	public String getSku_barcode() {
		return sku_barcode;
	}

	public void setSku_barcode(String sku_barcode) {
		this.sku_barcode = sku_barcode;
	}

	@XmlElement(name="express_state")
	public Short getExpress_state() {
		return express_state;
	}

	public void setExpress_state(Short express_state) {
		this.express_state = express_state;
	}

	
}
