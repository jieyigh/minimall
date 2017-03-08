package com.jbh360.trade.vo.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class OrderParams {
	
	public interface OrderCloseParams{};

	@NotNull(groups=OrderCloseParams.class)
	private Long order_id;
	
	private String close_reason;
	
	@NotNull(groups=OrderCloseParams.class)
	private Long member_id;
	
	

	

	public Long getMember_id() {
		return member_id;
	}

	public void setMember_id(Long member_id) {
		this.member_id = member_id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getClose_reason() {
		return close_reason;
	}

	public void setClose_reason(String close_reason) {
		this.close_reason = close_reason;
	}
	
	
	
}
