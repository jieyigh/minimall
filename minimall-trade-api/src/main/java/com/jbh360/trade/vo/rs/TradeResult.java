package com.jbh360.trade.vo.rs;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**  
* @Title: TradeResult.java
* @Package com.jbh360.trade.vo.rs
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月3日 下午2:58:57
* @version V3.0  
*/
public class TradeResult implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long trade_id;
	private String trade_no;
	private Long order_id;
	private String order_no;
	
	public TradeResult(Long trade_id, String trade_no,Long order_id,String order_no){
		this.trade_id = trade_id;
		this.trade_no = trade_no;
		this.order_id = order_id;
		this.order_no = order_no;
	}
	
	@XmlElement(name="trade_id")
	public Long getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(Long trade_id) {
		this.trade_id = trade_id;
	}
	
	@XmlElement(name="trade_no")
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	
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
}
