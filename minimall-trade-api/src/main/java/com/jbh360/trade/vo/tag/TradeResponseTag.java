package com.jbh360.trade.vo.tag;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jbh360.trade.vo.rs.TradeResult;

/**  
* @Title: TradeResponseTag.java
* @Package com.jbh360.trade.vo.tag
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年10月3日 下午3:01:19
* @version V3.0  
*/
@XmlRootElement(name="xml")
public class TradeResponseTag implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TradeResult result;
	
	public TradeResponseTag(){
		super();
	}
	
	public TradeResponseTag(TradeResult result){
		this.result = result;
	}
	@XmlElement(name="minimall_trade_add_response")
	public TradeResult getResult() {
		return result;
	}

	public void setResult(TradeResult result) {
		this.result = result;
	}
}	
