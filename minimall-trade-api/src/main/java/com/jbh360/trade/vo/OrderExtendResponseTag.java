package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jbh360.common.base.SuccessResult;

@XmlRootElement(name="xml")
public class OrderExtendResponseTag {
	private SuccessResult successResult;

	@XmlElement(name="minimall_oms_order_extend")
	public SuccessResult getSuccessResult() {
		return successResult;
	}

	public void setSuccessResult(SuccessResult successResult) {
		this.successResult = successResult;
	}
}
