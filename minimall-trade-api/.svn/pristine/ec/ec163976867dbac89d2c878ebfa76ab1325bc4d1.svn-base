package com.jbh360.trade.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbh360.common.base.ErrorResult;
import com.jbh360.common.base.ErrorResultResponseTag;
import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.ValidateUtil;
import com.jbh360.trade.service.OrderPreSplitServiceImpl;
import com.jbh360.trade.service.OrderServiceImpl;
import com.jbh360.trade.vo.OrderPreSplitParams;
import com.jbh360.trade.vo.OrderPreSplitResponseTag;
import com.jbh360.trade.vo.param.OrderParams;
import com.jbh360.trade.vo.param.OrderParams.OrderCloseParams;
import com.jbh360.trade.vo.param.OrderReceiverEditParams;
import com.jbh360.trade.vo.rs.OrderReceiverEditGetResult;
import com.jbh360.trade.vo.rs.OrderReceiverEditResult;
import com.jbh360.trade.vo.rs.OrderResponseTag;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    protected OrderServiceImpl orderServiceImpl;
    
    @Autowired
    private OrderPreSplitServiceImpl preSplitServiceImpl;
    
    
    /**
     * 
     * @Title: receiverEdit
     * @Description: 修改收货地址
     * @date 2015年11月2日 下午8:24:00
     * @author 揭懿
     * @param params
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "receiver/edit", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Object receiverEdit(OrderReceiverEditParams params,BindingResult bindingResult){
		ErrorResult error= new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		try {	
			if(bindingResult.hasErrors()) {
				error.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				error.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			OrderReceiverEditGetResult orderReceiverEditGetResult = new OrderReceiverEditGetResult();
			OrderReceiverEditResult orderReceiverEditResult = orderServiceImpl.receiverEdit(params, error);
			orderReceiverEditGetResult.setOrderReceiverEditResult(orderReceiverEditResult);
			if(error!=null&&!StringUtils.isBlank(error.getErrorCode())){
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			OrderResponseTag responseTag = new OrderResponseTag();
			responseTag.setOrderReceiverEditGetResult(orderReceiverEditGetResult);
			return responseTag;
			 
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			error.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			error.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(error);
			return errorResopneTag;
		}
	}
    
    
	/**
	 * 
	 * @Title: close
	 * @Description: 订单关闭
	 * @date 2015年10月31日 下午3:46:50
	 * @author 揭懿
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "close", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Object close(@Validated({OrderCloseParams.class}) OrderParams params,BindingResult bindingResult){
		ErrorResult error= new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		try {	
			if(bindingResult.hasErrors()) {
				error.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				error.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			SuccessResult successResult = new SuccessResult();
			orderServiceImpl.close(params, successResult, error);
			if(error!=null&&!StringUtils.isBlank(error.getErrorCode())){
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			OrderResponseTag responseTag = new OrderResponseTag();
			responseTag.setCloseSuccessResult(successResult);
			return responseTag;
			 
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			error.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			error.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(error);
			return errorResopneTag;
		}
	}
	
	
	/**
	 * 订单预分账控制层
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月29日 下午4:29:20 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "pre/split", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object split(@Valid OrderPreSplitParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		OrderPreSplitResponseTag responseTag = new OrderPreSplitResponseTag();
		
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
			this.preSplitServiceImpl.preSplit(params,is_success);
			
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
		
		
		responseTag.setIs_success(is_success);
		return responseTag;
	}
	
}