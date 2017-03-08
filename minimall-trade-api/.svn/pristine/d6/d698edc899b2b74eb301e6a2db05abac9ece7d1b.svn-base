package com.jbh360.trade.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.jbh360.common.base.PageUtil;

public class UcOrderListResult extends PageUtil {
	
	private List<UcOrderResult> ucOrderList;
	
	public UcOrderListResult() {
		super();
	}

	public UcOrderListResult(int pageSize, int pageNo, long totalCount) {
		super(pageSize, pageNo, totalCount);
	}

	@XmlElementWrapper(name="uc_order_lists")
	@XmlElements({
		@XmlElement(name="uc_order_list",type=UcOrderResult.class)		
	})
	public List<UcOrderResult> getUcOrderList() {
		return ucOrderList;
	}

	public void setUcOrderList(List<UcOrderResult> ucOrderList) {
		this.ucOrderList = ucOrderList;
	}
}
