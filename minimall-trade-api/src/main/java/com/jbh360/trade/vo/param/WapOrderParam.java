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
public class WapOrderParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String buyer_remark;						//：买家备注，
	private String invoice_info; 						//发票抬头
	
	@NotNull(message="buyer_store_id掌柜店铺ID不能为空")
	private Long buyer_store_id;						//卖方店铺id（掌柜店铺）
	
	@NotNull(message="buyer_store_id掌柜ID不能为空")
	private Long buye_store_member_id;					//卖方掌柜id，
 
	private Long seller_store_id;						//卖方商户店铺id，
	
	private Long seller_member_id;						//卖方商户主用户id
	
	private Long supplier_id;							//卖方商户id
	
	private Long[] cart_ids;							//采购车id(采购单可选，如果为空表示直接下单；不为空在下单成功后删除采购车此记录)，
	
	private BigDecimal order_total_amount;				//实付总金额
	private BigDecimal order_goods_total_amount;		//商品总金额
	private BigDecimal order_freight_amount;			//邮费总金额
	private BigDecimal order_discount_total_amount;		//优惠总金额
	
	private Long promotion_id; //：店铺促销id，
	
	private BigDecimal  promotion_total_amount;	//:优惠金额
	
	private BigDecimal freight_amount;	// 邮费

	
	@NotEmpty(message="产品列表不能为空")
	@Valid
	private List<WapProduct> products;
	
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



	public Long getBuyer_store_id() {
		return buyer_store_id;
	}



	public void setBuyer_store_id(Long buyer_store_id) {
		this.buyer_store_id = buyer_store_id;
	}



	public Long getBuye_store_member_id() {
		return buye_store_member_id;
	}



	public void setBuye_store_member_id(Long buye_store_member_id) {
		this.buye_store_member_id = buye_store_member_id;
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


	public Long getPromotion_id() {
		return promotion_id;
	}



	public void setPromotion_id(Long promotion_id) {
		this.promotion_id = promotion_id;
	}



	public BigDecimal getPromotion_total_amount() {
		return promotion_total_amount;
	}



	public void setPromotion_total_amount(BigDecimal promotion_total_amount) {
		this.promotion_total_amount = promotion_total_amount;
	}



	public List<WapProduct> getProducts() {
		return products;
	}



	public void setProducts(List<WapProduct> products) {
		this.products = products;
	}



	public BigDecimal getFreight_amount() {
		return freight_amount;
	}



	public void setFreight_amount(BigDecimal freight_amount) {
		this.freight_amount = freight_amount;
	}


}
