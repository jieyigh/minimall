package com.jbh360.trade.vo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class StoreOrderResult implements Serializable {
	
	private static final long serialVersionUID = -4112164353125504537L;

	private Long order_id;
	
	private String order_no;
	
	private Date order_time;
	
	private String state_code;
	
	private String state_name;
	
	private Short order_type;
	
	private Long game_id;
	
	private String game_name;
	
	private Long conpon_id;
	
	private String conpon_name;
	
	private String trade_logo_rsurl;
	
	private Integer buy_count;
	
	private String receiver_name;
	
	private String receiver_mobile;

	@XmlElement(name="order_id")
	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	@XmlElement(name="order_no")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@XmlElement(name="order_time")
	public Date getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Date order_time) {
		this.order_time = order_time;
	}

	@XmlElement(name="state_code")
	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	@XmlElement(name="state_name")
	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	@XmlElement(name="order_type")
	public Short getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Short order_type) {
		this.order_type = order_type;
	}

	@XmlElement(name="game_id")
	public Long getGame_id() {
		return game_id;
	}

	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}

	@XmlElement(name="game_name")
	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	@XmlElement(name="conpon_id")
	public Long getConpon_id() {
		return conpon_id;
	}

	public void setConpon_id(Long conpon_id) {
		this.conpon_id = conpon_id;
	}

	@XmlElement(name="conpon_name")
	public String getConpon_name() {
		return conpon_name;
	}

	public void setConpon_name(String conpon_name) {
		this.conpon_name = conpon_name;
	}

	@XmlElement(name="trade_logo_rsurl")
	public String getTrade_logo_rsurl() {
		return trade_logo_rsurl;
	}

	public void setTrade_logo_rsurl(String trade_logo_rsurl) {
		this.trade_logo_rsurl = trade_logo_rsurl;
	}

	@XmlElement(name="buy_count")
	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	@XmlElement(name="receiver_name")
	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	@XmlElement(name="receiver_mobile")
	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}
}
