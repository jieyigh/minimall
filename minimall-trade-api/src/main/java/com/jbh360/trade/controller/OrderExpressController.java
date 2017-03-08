package com.jbh360.trade.controller;

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
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.Page;
import com.jbh360.common.utils.ValidateUtil;
import com.jbh360.trade.service.OrderExpressServiceImpl;
import com.jbh360.trade.vo.param.ExpressComputeProduct;
import com.jbh360.trade.vo.param.OrderExpressParams;
import com.jbh360.trade.vo.param.OrderExpressParams.OrderExpressComputeParams;
import com.jbh360.trade.vo.rs.ExpressAmountResult;
import com.jbh360.trade.vo.rs.OrderExpressGetResult;
import com.jbh360.trade.vo.rs.OrderExpressListResult;
import com.jbh360.trade.vo.rs.OrderExpressResult;
import com.jbh360.trade.vo.rs.OrderResponseTag;

/**
 * 
 * @Title: OrderExpressController.java
 * @Package com.jbh360.trade.controller
 * @ClassName: OrderExpressController
 * @Description: 订单物流
 * @author 揭懿
 * @email yi.jie@yooyo.com
 * @date 2015年10月31日 下午3:06:39
 * @version V3.0
 */
@Controller
public class OrderExpressController extends BaseController {

	
	@Autowired
	protected OrderExpressServiceImpl orderExpressServiceImpl;
	
	
	/**
	 * 
	 * @Title: compute
	 * @Description: 运费计算
	 * @date 2015年10月31日 下午5:33:12
	 * @author 揭懿
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "order/express/compute", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Object compute(@Validated(OrderExpressComputeParams.class) OrderExpressParams params,BindingResult bindingResult){
		ErrorResult error= new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		try {	
			
			if(null != params.getCompute_products() && !params.getCompute_products().isEmpty()){
				for(ExpressComputeProduct product:params.getCompute_products()){
					this.orderExpressServiceImpl.computeExpressProduct(product, bindingResult);
				}
			}
			if(bindingResult.hasErrors()) {
				error.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				error.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			ExpressAmountResult expressAmountResult = new ExpressAmountResult();
			String  express_amount = orderExpressServiceImpl.computeExpress(params).toString();
			expressAmountResult.setExpress_amount(express_amount);
			if(error!=null&&!StringUtils.isBlank(error.getErrorCode())){
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			OrderResponseTag responseTag = new OrderResponseTag();
			responseTag.setExpressAmountResult(expressAmountResult);
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
	 * 查看物流包裹信息
	 * @param params
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "trade/express/get", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Object get(OrderExpressParams params,BindingResult bindingResult){
		ErrorResult error= new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		try {	
			if(bindingResult.hasErrors()) {
				error.setErrorCode(SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
				error.setErrorMessage(ValidateUtil.errorMessage(bindingResult));
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			OrderExpressGetResult orderExpressGetResult = new OrderExpressGetResult();
			OrderExpressResult orderExpressResult = orderExpressServiceImpl.getOrderExpressResult(params, error);
			orderExpressGetResult.setOrderExpressResult(orderExpressResult);
			if(error!=null&&!StringUtils.isBlank(error.getErrorCode())){
				errorResopneTag.setErrorResult(error);
				return errorResopneTag;
			}
			
			OrderResponseTag responseTag = new OrderResponseTag();
			responseTag.setOrderExpressGetResult(orderExpressGetResult);
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
	 * 物流信息列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "trade/express/list", method = { RequestMethod.POST,RequestMethod.GET })
	public @ResponseBody Object list(OrderExpressParams params){
		ErrorResult error = new ErrorResult();
		ErrorResultResponseTag errorResopneTag= new ErrorResultResponseTag();
		
		try {
			Page<OrderExpressResult> page = new Page<OrderExpressResult>();
			if (null == params.getPage_no() ) {
				params.setPage_no(1);
			}

			if (null == params.getPage_size() ) {
				params.setPage_size(10);
			}
			
			page = orderExpressServiceImpl.getOrderExpressResults(page, params);
			OrderExpressListResult orderExpressListResult = new OrderExpressListResult(params.getPage_size(), params.getPage_no(), page.getTotalCount());
			orderExpressListResult.setOrderExpressResults(page.getResult());
			OrderResponseTag responseTag = new OrderResponseTag();
			responseTag.setOrderExpressListResult(orderExpressListResult);
			return responseTag;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			error.setErrorCode(SystemErrorCode.CommonErrorCode.SYSEM_ERROR);
			error.setErrorMessage(e.getMessage());
			errorResopneTag.setErrorResult(error);
			return errorResopneTag;
		}
	}
}
