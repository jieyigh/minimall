package com.jbh360.trade.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**  
* @Title: AppTradeParam.java
* @Package com.jbh360.trade.vo.param
* @Description: TODO(app订单参数)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月3日 上午11:45:08
* @version V3.0  
*/
public class AppTradeParam implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="用户id不能为空")
	private Long member_id;
	
	private Long store_id;
	
	private String come_from;  //订单来源(wap、app、web)
	
	@NotNull(message="收货方式receiver_type不能为空")
	private Short receiver_type; //收货方式(0：自提，1：物流快递)
	
	private Long warehouse_id;//自提点id
	
	@NotBlank(message="收货人姓名receiver_name不能为空")
	private String receiver_name;  //收货人姓名(自提人姓名)
	
	@NotBlank(message="收货人手机号不能为空")
	private String receiver_mobile;  //收货人手机号(自提人手机)
	 
	private String receiver_area_ccode; //收货人街道ccode，
	 
	private String receiver_address; //收货人地址（不带省市区）
	
	private String receiver_zip_code;//收货人邮政编码
	
	@NotNull(message="实付总金额不能为空")
	private BigDecimal trade_total_amount;//实付总金额
	
	private BigDecimal trade_goods_total_amount;  //商品总金额
	
	private BigDecimal trade_freight_amount;//邮费总金额
	
	private BigDecimal trade_discount_total_amount;//优惠总金额
	
	
	@NotEmpty(message="订单列表不能为空")
	@Valid
	private List<AppOrderParam> orders;
	
	public Long getMember_id() {
		return member_id;
	}

	public void setMember_id(Long member_id) {
		this.member_id = member_id;
	}


	public String getCome_from() {
		return come_from;
	}


	public void setCome_from(String come_from) {
		this.come_from = come_from;
	}


	public Short getReceiver_type() {
		return receiver_type;
	}

	public void setReceiver_type(Short receiver_type) {
		this.receiver_type = receiver_type;
	}

	public Long getWarehouse_id() {
		return warehouse_id;
	}




	public void setWarehouse_id(Long warehouse_id) {
		this.warehouse_id = warehouse_id;
	}




	public String getReceiver_name() {
		return receiver_name;
	}




	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}




	public String getReceiver_mobile() {
		return receiver_mobile;
	}




	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}




	public String getReceiver_area_ccode() {
		return receiver_area_ccode;
	}




	public void setReceiver_area_ccode(String receiver_area_ccode) {
		this.receiver_area_ccode = receiver_area_ccode;
	}




	public String getReceiver_address() {
		return receiver_address;
	}




	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}




	public String getReceiver_zip_code() {
		return receiver_zip_code;
	}




	public void setReceiver_zip_code(String receiver_zip_code) {
		this.receiver_zip_code = receiver_zip_code;
	}




	public BigDecimal getTrade_total_amount() {
		return trade_total_amount;
	}




	public void setTrade_total_amount(BigDecimal trade_total_amount) {
		this.trade_total_amount = trade_total_amount;
	}




	public BigDecimal getTrade_goods_total_amount() {
		return trade_goods_total_amount;
	}




	public void setTrade_goods_total_amount(BigDecimal trade_goods_total_amount) {
		this.trade_goods_total_amount = trade_goods_total_amount;
	}




	public BigDecimal getTrade_freight_amount() {
		return trade_freight_amount;
	}




	public void setTrade_freight_amount(BigDecimal trade_freight_amount) {
		this.trade_freight_amount = trade_freight_amount;
	}


	public BigDecimal getTrade_discount_total_amount() {
		return trade_discount_total_amount;
	}


	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public void setTrade_discount_total_amount(BigDecimal trade_discount_total_amount) {
		this.trade_discount_total_amount = trade_discount_total_amount;
	}




	public List<AppOrderParam> getOrders() {
		return orders;
	}




	public void setOrders(List<AppOrderParam> orders) {
		this.orders = orders;
	}
		 
	
}	
