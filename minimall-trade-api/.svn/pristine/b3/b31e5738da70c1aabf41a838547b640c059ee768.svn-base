package com.jbh360.trade.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

import com.jbh360.common.base.PageUtil;

public class StoreOrderListResult extends PageUtil {
	
	private List<StoreOrderResult> storeOrderList;
	
	public StoreOrderListResult() {
		super();
	}

	public StoreOrderListResult(int pageSize, int pageNo, long totalCount) {
		super(pageSize, pageNo, totalCount);
	}

	@XmlElementWrapper(name="store_order_lists")
	@XmlElements({
		@XmlElement(name="store_order_list",type=StoreOrderResult.class)		
	})
	public List<StoreOrderResult> getStoreOrderList() {
		return storeOrderList;
	}

	public void setStoreOrderList(List<StoreOrderResult> storeOrderList) {
		this.storeOrderList = storeOrderList;
	}
}
