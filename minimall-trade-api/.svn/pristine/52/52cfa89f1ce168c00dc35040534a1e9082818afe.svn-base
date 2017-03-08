package com.jbh360.trade.controller;

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
import com.jbh360.trade.service.ShopCartServiceImpl;
import com.jbh360.trade.vo.ShopCartAddResponseTag;
import com.jbh360.trade.vo.ShopCartDeleteResponseTag;
import com.jbh360.trade.vo.ShopCartListResponseTag;
import com.jbh360.trade.vo.ShopCartListResult;
import com.jbh360.trade.vo.ShopCartParams;
import com.jbh360.trade.vo.ShopCartParams.ShopCartAddParams;
import com.jbh360.trade.vo.ShopCartParams.ShopCartDeleteParams;
import com.jbh360.trade.vo.ShopCartParams.ShopCartListParams;
import com.jbh360.trade.vo.ShopCartParams.ShopCartUpdateParams;
import com.jbh360.trade.vo.ShopCartResult;
import com.jbh360.trade.vo.ShopCartUpdateResponseTag;
import com.yooyo.util.RestUtil;

/**
 * 购物车控制层
 * @author : liguosheng 
 * @CreateDate : 2015年9月28日 下午4:15:52
 */
@Controller
@RequestMapping("/shop/cart")
public class ShopCartController {
	@Autowired
	private ShopCartServiceImpl shopCartServiceImpl;

	/**
	 * 购物车列表
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午4:17:39 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object list(@Validated({ShopCartListParams.class}) ShopCartParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		ShopCartListResponseTag cartListResponseTag = new ShopCartListResponseTag();
		ShopCartListResult cartListResult = null;
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			
			// 业务逻辑方法
			cartListResult = this.shopCartServiceImpl.findShopCartList(params);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ServiceException){
				errorResult.setErrorCode(((ServiceException) e).getCode());
			}else{
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			}
			errorResult.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(errorResult);
			return errorResopneTag;
		}
		
		cartListResponseTag.setShopCartListResult(cartListResult);
		System.out.println("购物车列表JSON格式："+RestUtil.toJson(cartListResponseTag));
		return cartListResponseTag;
	}
	
	/**
	 * 新增购物车
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午4:19:47 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "add", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object add(@Validated({ShopCartAddParams.class}) ShopCartParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		ShopCartAddResponseTag cartAddResponseTag = new ShopCartAddResponseTag();
		ShopCartResult cartResult = null;
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			
			// 业务逻辑方法
			cartResult = this.shopCartServiceImpl.addShopCart(params);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ServiceException){
				errorResult.setErrorCode(((ServiceException) e).getCode());
			}else{
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			}
			errorResult.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(errorResult);
			return errorResopneTag;
		}
		
		cartAddResponseTag.setShopCartResult(cartResult);
		return cartAddResponseTag;
	}
	
	/**
	 * 更新购物车
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午4:19:47 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "update", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object update(@Validated({ShopCartUpdateParams.class}) ShopCartParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		ShopCartUpdateResponseTag cartUpdateResponseTag = new ShopCartUpdateResponseTag();
		SuccessResult successResult = null;
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			
			// 业务逻辑方法
			successResult = this.shopCartServiceImpl.updateShopCart(params);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ServiceException){
				errorResult.setErrorCode(((ServiceException) e).getCode());
			}else{
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			}
			errorResult.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(errorResult);
			return errorResopneTag;
		}
		
		cartUpdateResponseTag.setIs_success(successResult);
		return cartUpdateResponseTag;
	}
	
	/**
	 * 更新购物车
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午4:19:47 
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public Object delete(@Validated({ShopCartDeleteParams.class}) ShopCartParams params,BindingResult bindingResult) {
		
		ErrorResult errorResult = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		ShopCartDeleteResponseTag cartDeleteResponseTag = new ShopCartDeleteResponseTag();
		SuccessResult successResult = null;
		
		try {
			// 验证表单
			if (bindingResult.hasErrors()) {
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				errorResult.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(errorResult);
				return errorResopneTag;
			}
			
			// 业务逻辑方法
			successResult = this.shopCartServiceImpl.deleteShopCart(params);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof ServiceException){
				errorResult.setErrorCode(((ServiceException) e).getCode());
			}else{
				errorResult.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			}
			errorResult.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(errorResult);
			return errorResopneTag;
		}
		
		cartDeleteResponseTag.setIs_success(successResult);
		return cartDeleteResponseTag;
	}
}
