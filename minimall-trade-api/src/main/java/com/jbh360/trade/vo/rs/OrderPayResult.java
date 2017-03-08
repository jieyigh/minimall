package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;


public class OrderPayResult {
	
	private String biz_no;
	
	private String status;
	
	private String amount;
	
	private String product;
	
	private String describe;
	
	private String order_url;
	
	private String notice_url;
	
	private String return_url;
	
	private Integer time_limit;
	
	private String sign;
	
	private String serial_no;
	
	private String openid;

	@XmlElement(name = "biz_no")
	public String getBiz_no() {
		return biz_no;
	}

	public void setBiz_no(String biz_no) {
		this.biz_no = biz_no;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "product")
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@XmlElement(name = "describe")
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@XmlElement(name = "order_url")
	public String getOrder_url() {
		return order_url;
	}

	public void setOrder_url(String order_url) {
		this.order_url = order_url;
	}

	@XmlElement(name = "notice_url")
	public String getNotice_url() {
		return notice_url;
	}

	public void setNotice_url(String notice_url) {
		this.notice_url = notice_url;
	}

	@XmlElement(name = "return_url")
	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	@XmlElement(name = "time_limit")
	public Integer getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(Integer time_limit) {
		this.time_limit = time_limit;
	}

	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@XmlElement(name = "serial_no")
	public String getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	@XmlElement(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	
}
