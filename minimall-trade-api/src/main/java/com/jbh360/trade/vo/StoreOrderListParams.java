package com.jbh360.trade.vo;

public class StoreOrderListParams{
	
	//1：采购单、2：代销单、3：自营单；
	private Short order_type;
	
	//wait_buyer_pay待付款、wait_seller_send_goods待发货（自提单是“备货中”状态）、wait_buyer_confirm_goods待收货、stock_finish已备货、trade_finished交易完成，trade_closed交易关闭
	private String state_code;
	
	private String key_word;
	
	private Long store_id;
	
	private Long member_id;
	
    private Integer page_no;
	
	private Integer page_size;

	public Short getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Short order_type) {
		this.order_type = order_type;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public String getKey_word() {
		return key_word;
	}

	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public Long getMember_id() {
		return member_id;
	}

	public void setMember_id(Long member_id) {
		this.member_id = member_id;
	}

	public Integer getPage_no() {
		return page_no;
	}

	public void setPage_no(Integer page_no) {
		this.page_no = page_no;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}
	
}
