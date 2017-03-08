package com.jbh360.trade.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jbh360.common.utils.Page;
import com.jbh360.trade.vo.StoreOrderListParams;
import com.jbh360.trade.vo.StoreOrderResult;
import com.jbh360.trade.dao.OrderListDao;
import com.jbh360.trade.vo.UcOrderListParams;
import com.jbh360.trade.vo.UcOrderResult;
import com.jbh360.trade.vo.OrderResult;

@Service
public class OrderListServiceImpl{
	
	@Autowired
	private OrderListDao orderListDao;
   
	/**
	 * 获取店铺的订单列表
	 * @param page
	 * @param params
	 * @return
	 * @author beiqiting
	 */
    public Page<StoreOrderResult> getStoreOrderList(Page<StoreOrderResult> page, StoreOrderListParams params){
	    List<StoreOrderResult> list = orderListDao.getStoreOrderList(new RowBounds(page.getFirst()-1, page.getPageSize()), params);
	    Long count = orderListDao.countStoreOrderList(params);
	    page.setResult(list);
	    page.setTotalCount(count);
	    return page;
    }
	
    /**
     * 普通会员的订单列表
     * @param page
     * @param params
     * @return
     * @author beiqiting
     */
    public Page<UcOrderResult> getUcOrderList(Page<UcOrderResult> page, UcOrderListParams params){
	    List<UcOrderResult> list = orderListDao.getUcOrderList(new RowBounds(page.getFirst()-1, page.getPageSize()), params);
	    Long count = orderListDao.countUcOrderList(params);
	    page.setResult(list);
	    page.setTotalCount(count);
	    return page;
    }
    
    /**
     * 获取订单详细
     * @param id
     * @return
     * @author beiqiting
     */
    public OrderResult getOrder(Long id){
    	OrderResult orderResult = new OrderResult();
    	orderResult = orderListDao.getOrder(id);
    	/*if(null != orderResult &&  null != orderResult.getExtend_time()){
    		orderResult.getLast_express_time().setHours(24*7 + orderResult.getExtend_time());
    		orderResult.setExtend_sing_time(orderResult.getLast_express_time());
    	}*/
    	return orderResult;
    }
}