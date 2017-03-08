package com.jbh360.trade.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 订单延长收货参数
 * @author : liguosheng 
 * @CreateDate : 2015年10月26日 上午10:49:16
 */
public class OrderExtendParams {
	@NotNull(message="订单ID不能为空")
	private Long order_id;
	
	private Integer extend_sing_time;
	
	@NotBlank(message="延长原因不能为空")
	private String extend_sing_time_reson;
	
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public Integer getExtend_sing_time() {
		return extend_sing_time;
	}
	public void setExtend_sing_time(Integer extend_sing_time) {
		this.extend_sing_time = extend_sing_time;
	}
	public String getExtend_sing_time_reson() {
		return extend_sing_time_reson;
	}
	public void setExtend_sing_time_reson(String extend_sing_time_reson) {
		this.extend_sing_time_reson = extend_sing_time_reson;
	}
}
