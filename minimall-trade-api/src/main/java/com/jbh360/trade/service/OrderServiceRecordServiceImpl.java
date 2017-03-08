package com.jbh360.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbh360.trade.mapper.OrderServiceMapper;
import com.jbh360.trade.mapper.OrderServicePictureMapper;
import com.jbh360.trade.mapper.OrderServiceRecordMapper;

@Service
public class OrderServiceRecordServiceImpl {

	@Autowired
    private OrderServiceMapper orderServiceMapper;
	
	@Autowired
    private OrderServicePictureMapper orderServicePictureMapper;
	
	@Autowired
    private OrderServiceRecordMapper orderServiceRecordMapper;
}
