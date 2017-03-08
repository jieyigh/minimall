package com.jbh360.trade.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**  
* @Title: OrderParam.java
* @Package com.jbh360.trade.vo.param
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月12日 上午11:44:33
* @version V3.0  
*/
public class AppOrderParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String buyer_remark;		//：买家备注，
	private String invoice_info; 		//发票抬头
 
	private Long romotion_id;			//：店铺促销id，私密折扣
	
	@NotNull(message="卖方店铺id不能为空")
	private Long seller_store_id;		//卖方店铺id，
	
	@NotNull(message="卖方商户ID不能为空")
	private Long seller_member_id;		//卖方商户主用户id
	
	private Long supplier_id;			//卖方商户id
	
	private Long[] cart_ids;			//采购车id(采购单可选，如果为空表示直接下单；不为空在下单成功后删除采购车此记录)，
	
	private BigDecimal order_total_amount;				//实付总金额
	private BigDecimal order_goods_total_amount;		//商品总金额
	private BigDecimal order_freight_amount;			//邮费总金额
	private BigDecimal order_discount_total_amount;		//优惠总金额
	
	
	private Long order_coupon_id;						//使用的优惠券id
	private BigDecimal order_coupon_discount_amount;	//优惠券优惠金额
	private	String product_coupon_ids; 					//那些产品可以使用优惠券（订单的全部商品，可认为是订单优惠）
	
	private Long order_game_id;							//促销活动id，只有满减属于订单级活动
	private BigDecimal order_game_discount_amount;		//活动优惠金额
	private	String product_game_ids;
	
	private BigDecimal freight_amount; 	// 邮费
	
	// 优惠券类型
	private Integer couponType; 
	
	@NotEmpty(message="产品列表不能为空")
	@Valid
	private List<AppProduct> products;
	
	public String getBuyer_remark() {
		return buyer_remark;
	}

	public void setBuyer_remark(String buyer_remark) {
		this.buyer_remark = buyer_remark;
	}

	public String getInvoice_info() {
		return invoice_info;
	}

	public void setInvoice_info(String invoice_info) {
		this.invoice_info = invoice_info;
	}

	public Long getRomotion_id() {
		return romotion_id;
	}



	public void setRomotion_id(Long romotion_id) {
		this.romotion_id = romotion_id;
	}



	public Long getSeller_store_id() {
		return seller_store_id;
	}



	public void setSeller_store_id(Long seller_store_id) {
		this.seller_store_id = seller_store_id;
	}



	public Long getSeller_member_id() {
		return seller_member_id;
	}



	public void setSeller_member_id(Long seller_member_id) {
		this.seller_member_id = seller_member_id;
	}



	public Long getSupplier_id() {
		return supplier_id;
	}



	public void setSupplier_id(Long supplier_id) {
		this.supplier_id = supplier_id;
	}


	public Long[] getCart_ids() {
		return cart_ids;
	}



	public void setCart_ids(Long[] cart_ids) {
		this.cart_ids = cart_ids;
	}



	public BigDecimal getOrder_total_amount() {
		return order_total_amount;
	}



	public void setOrder_total_amount(BigDecimal order_total_amount) {
		this.order_total_amount = order_total_amount;
	}



	public BigDecimal getOrder_goods_total_amount() {
		return order_goods_total_amount;
	}



	public void setOrder_goods_total_amount(BigDecimal order_goods_total_amount) {
		this.order_goods_total_amount = order_goods_total_amount;
	}



	public BigDecimal getOrder_freight_amount() {
		return order_freight_amount;
	}



	public void setOrder_freight_amount(BigDecimal order_freight_amount) {
		this.order_freight_amount = order_freight_amount;
	}



	public BigDecimal getOrder_discount_total_amount() {
		return order_discount_total_amount;
	}



	public void setOrder_discount_total_amount(BigDecimal order_discount_total_amount) {
		this.order_discount_total_amount = order_discount_total_amount;
	}


	public Long getOrder_coupon_id() {
		return order_coupon_id;
	}



	public void setOrder_coupon_id(Long order_coupon_id) {
		this.order_coupon_id = order_coupon_id;
	}



	public BigDecimal getOrder_coupon_discount_amount() {
		return order_coupon_discount_amount;
	}



	public void setOrder_coupon_discount_amount(BigDecimal order_coupon_discount_amount) {
		this.order_coupon_discount_amount = order_coupon_discount_amount;
	}



	public Long getOrder_game_id() {
		return order_game_id;
	}



	public void setOrder_game_id(Long order_game_id) {
		this.order_game_id = order_game_id;
	}



	public BigDecimal getOrder_game_discount_amount() {
		return order_game_discount_amount;
	}



	public void setOrder_game_discount_amount(BigDecimal order_game_discount_amount) {
		this.order_game_discount_amount = order_game_discount_amount;
	}



	public List<AppProduct> getProducts() {
		return products;
	}



	public void setProducts(List<AppProduct> products) {
		this.products = products;
	}
	



	public String getProduct_coupon_ids() {
		return product_coupon_ids;
	}



	public void setProduct_coupon_ids(String product_coupon_ids) {
		this.product_coupon_ids = product_coupon_ids;
	}



	public String getProduct_game_ids() {
		return product_game_ids;
	}


	public void setProduct_game_ids(String product_game_ids) {
		this.product_game_ids = product_game_ids;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public BigDecimal getFreight_amount() {
		return freight_amount;
	}

	public void setFreight_amount(BigDecimal freight_amount) {
		this.freight_amount = freight_amount;
	}
}
