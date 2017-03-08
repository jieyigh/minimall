package com.jbh360.trade.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbh360.common.base.ErrorResult;
import com.jbh360.common.base.ErrorResultResponseTag;
import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.ValidateUtil;
import com.jbh360.trade.service.OrderSignServiceImpl;
import com.jbh360.trade.vo.OrderSignParams;
import com.jbh360.trade.vo.OrderSignResponseTag;

/**
 * 订单包裹签收控制层
 * @author : liguosheng 
 * @CreateDate : 2015年10月28日 下午2:06:51
 */
@Controller
@RequestMapping("/order")
public class OrderSignController {
	@Autowired
	private OrderSignServiceImpl orderSignServiceImpl;	// 订单签收业务逻辑层
	
	
	@RequestMapping(value = "sign", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object sign(@Valid OrderSignParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		OrderSignResponseTag orderSignResponseTag = new OrderSignResponseTag();
		
		SuccessResult is_success = new SuccessResult();
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			
			// 业务逻辑方法
			this.orderSignServiceImpl.orderSign(params,is_success);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ServiceException){
				errorResult.setErrorCode(((ServiceException) e).getCode());
			}else{
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			}			errorResult.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(errorResult);
			return errorResopneTag;
		}
		
		
		orderSignResponseTag.setIs_success(is_success);
		return orderSignResponseTag;
	}
	
}
