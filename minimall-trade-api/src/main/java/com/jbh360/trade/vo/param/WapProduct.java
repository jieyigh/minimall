package com.jbh360.trade.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**  
* @Title: Product.java
* @Package com.jbh360.trade.vo.param
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月12日 上午11:45:25
* @version V3.0  
*/
public class WapProduct implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="产品id不能为空")
	private Long product_id;//产品id(必填)，
	@NotNull(message="产品product_sku_barcode不能为空")
	private String product_sku_barcode;//产品sku;//编码(必填)，
	private Long store_goods_id;//店铺商品id（销售单必填），
	@NotNull(message="产品buy_count不能为空")
	private Integer buy_count;//购买数量(必填)，
	
	private Long promotion_id;//促销活动id，
	private BigDecimal promotion_price;//促销活动商品单价
	
	private BigDecimal product_price;//商品单价

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public String getProduct_sku_barcode() {
		return product_sku_barcode;
	}

	public void setProduct_sku_barcode(String product_sku_barcode) {
		this.product_sku_barcode = product_sku_barcode;
	}

	public Long getStore_goods_id() {
		return store_goods_id;
	}

	public void setStore_goods_id(Long store_goods_id) {
		this.store_goods_id = store_goods_id;
	}

	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}


	public Long getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(Long promotion_id) {
		this.promotion_id = promotion_id;
	}

	public BigDecimal getPromotion_price() {
		return promotion_price;
	}

	public void setPromotion_price(BigDecimal promotion_price) {
		this.promotion_price = promotion_price;
	}

	public BigDecimal getProduct_price() {
		return product_price;
	}

	public void setProduct_price(BigDecimal product_price) {
		this.product_price = product_price;
	}

}
