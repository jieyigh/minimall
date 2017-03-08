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
import com.jbh360.trade.service.OrderExtendServiceImp;
import com.jbh360.trade.vo.OrderExtendParams;
import com.jbh360.trade.vo.OrderExtendResponseTag;

@Controller
@RequestMapping("/order")
public class OrderExtendController {
	@Autowired
	private OrderExtendServiceImp orderExtendServiceImp;

	/**
	 * 延长收货
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月26日 上午10:57:53 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "extend", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object extend(@Valid OrderExtendParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		OrderExtendResponseTag orderExtendResponseTag = new OrderExtendResponseTag();
		
		SuccessResult successResult = new SuccessResult();
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			this.orderExtendServiceImp.extend(params,successResult);
			
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
		
		orderExtendResponseTag.setSuccessResult(successResult);
		return orderExtendResponseTag;
	}
}
