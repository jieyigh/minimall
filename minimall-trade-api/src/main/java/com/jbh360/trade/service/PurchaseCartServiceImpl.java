package com.jbh360.trade.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jbh360.common.base.SuccessResult;
import com.jbh360.common.exception.ServiceException;
import com.jbh360.common.exception.SystemErrorCode;
import com.jbh360.common.utils.Constants;
import com.jbh360.common.utils.DateUtil;
import com.jbh360.goods.entity.ProductSku;
import com.jbh360.goods.entity.ProductSkuExample;
import com.jbh360.goods.entity.ProductWithBLOBs;
import com.jbh360.goods.mapper.ProductMapper;
import com.jbh360.goods.mapper.ProductSkuMapper;
import com.jbh360.member.oms.entity.Member;
import com.jbh360.member.oms.mapper.MemberMapper;
import com.jbh360.trade.dao.PurchaseCartDao;
import com.jbh360.trade.entity.PurchaseCart;
import com.jbh360.trade.entity.PurchaseCartExample;
import com.jbh360.trade.mapper.PurchaseCartMapper;
import com.jbh360.trade.vo.PriceResult;
import com.jbh360.trade.vo.PurchaseCartListResult;
import com.jbh360.trade.vo.PurchaseCartParams;
import com.jbh360.trade.vo.PurchaseCartParams.PurchaseCartUpdateValid;
import com.jbh360.trade.vo.PurchaseCartResult;
import com.jbh360.trade.vo.StepPriceResult;
import com.jbh360.trade.vo.SupplierResult;

@Service
public class PurchaseCartServiceImpl{
	
	private Logger log = Logger.getLogger(PurchaseCartServiceImpl.class);
	
	@Autowired
	private PurchaseCartMapper purchaseCartMapper; 	// 采购车 Mapper 数据操作层
	
	@Autowired
	private PurchaseCartDao purchaseCartDao;		// 采购车 DAO 数据操作层
	
	@Autowired
	private ProductMapper productMapper;				// 商品表 数据操作层
	
	/*@Autowired
	private ProductPictureMapper productPictureMapper; 	// 商品图片数据操作层
*/	
	@Autowired
	private MemberMapper memberMapper;					// 用户数据操作层		
	
	@Autowired
	private ProductSkuMapper productSkuMapper;			// 产品数据操作层

	/**
	 * 获取采购车列表 
	 * 根据  member_id 微店长会员id
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午2:17:46 
	 * @param params
	 * @return
	 */
	public PurchaseCartListResult findPurchaseCartList(PurchaseCartParams params) {
		PurchaseCartListResult cartListResult = new PurchaseCartListResult();
		
		List<PurchaseCartResult> purchaseCarts = null;
		// 商家集合
		List<SupplierResult> suppliers = new ArrayList<SupplierResult>();
		// 产品阶梯价集合
		List<StepPriceResult> stepPrices = new ArrayList<StepPriceResult>();
		
		
		if(params != null && params.getMember_id() != null){
			// 根据用户获取所有采购车集合
			purchaseCarts = this.purchaseCartDao.findListByMemberId(params.getMember_id());
			
			putPurchaseInfo(purchaseCarts, suppliers, stepPrices);
			
		}
		
		cartListResult.setSuppliers(suppliers);
		cartListResult.setStepPrices(stepPrices);
		
		return cartListResult;
	}

	/**
	 * 循环采购车集合：
	 * 1.对商户店铺ID的进行分组，存入supperMaps中， key(商户ID) value(商户下采购车集合)
	 * 2.对产品ID进行分组，存入productKeySet,同时获取对应的阶梯价格集合，存入 stepPrices
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月28日 下午3:03:22 
	 * @param purchaseCarts
	 * @param suppliers
	 * @param stepPrices
	 */
	private void putPurchaseInfo(List<PurchaseCartResult> purchaseCarts,List<SupplierResult> suppliers, List<StepPriceResult> stepPrices) {
		if(!CollectionUtils.isEmpty(purchaseCarts)){
			
			// Map<商户店铺ID,采购车集合> 映射对象
			Map<Long,List<PurchaseCartResult>> supperMaps = new HashMap<Long,List<PurchaseCartResult>>();
			// Set<产品ID> 映射对象
			Set<Long> productKeySet = new HashSet<Long>();
			// 循环采购车列表
			for(PurchaseCartResult purchaseCart :purchaseCarts){
				
				Long supplierStoreId = purchaseCart.getFkSupplierStoreId();
				Long productId = purchaseCart.getFkProductId();
				List<PurchaseCartResult> pcars = null;
				
				if(!supperMaps.containsKey(supplierStoreId)){
					 pcars =  new ArrayList<PurchaseCartResult>();
				}else{
					 pcars = supperMaps.get(supplierStoreId);
				}
				
				purchaseCart.setPrices(this.purchaseCartDao.getStepPrices(productId));
				
				pcars.add(purchaseCart);
				
				supperMaps.put(supplierStoreId, pcars);
				
				if(!productKeySet.contains(productId)){
					productKeySet.add(productId);
					StepPriceResult stepPriceResult = new StepPriceResult();
					stepPriceResult.setProductId(productId);
					List<PriceResult> prices = this.purchaseCartDao.getStepPrices(productId);
					stepPriceResult.setPrices(prices);
					stepPrices.add(stepPriceResult);
				}
				
			}
			
			// 重新整合 商户集合对象 suppliers
			if(supperMaps.size() > 0){
				for(Long supplierStoreId : supperMaps.keySet()){
					SupplierResult supplierResult = new SupplierResult();
					List<PurchaseCartResult> list = supperMaps.get(supplierStoreId);
					supplierResult.setPurchaseCarts(list);
					supplierResult.setSellerMemberId(list.get(0).getFkSuppliererMemberId());
					supplierResult.setSellerStoreName(list.get(0).getFkSupplierStoreName());
					supplierResult.setSellerStoreId(list.get(0).getFkSupplierStoreId());
					suppliers.add(supplierResult);
				}
			}
		}
	}

	
	/**
	 * 添加采购车
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午2:13:43 
	 * @param params
	 * @return
	 */
	public PurchaseCartResult addPurchaseCart(PurchaseCartParams params) {
		log.info("===================添加采购车开始addPurchaseCart===================");
		
		PurchaseCartResult purchaseCartResult = new PurchaseCartResult();
		
		/** 校验 **/
		validPurchaseCart(params);
		
		
		/** 根据产品ID、skuID查询采购车产品
		 *  如果有在原有记录更新
		 *  如果没有新增一条
		 * */
		
		List<PurchaseCart> purchaseCarts = findPurchaseCart(params);
		int count = 0;
		PurchaseCart purchaseCart = null;
		if(CollectionUtils.isEmpty(purchaseCarts)){
			/** 价格 **/
			validPrice(params.getProduct_id(), params.getProduct_sku_id(), params.getProduct_unit_price(), params.getBuy_count());
			purchaseCart = putAddData(params);
			count = this.purchaseCartMapper.insertSelective(purchaseCart);
		}else{
			
			Integer buy_count = null;
			
			if(params.getBuy_count() != null){
				buy_count = purchaseCarts.get(0).getBuyCount() + params.getBuy_count();
			}else{
				buy_count = purchaseCarts.get(0).getBuyCount();
			}
			params.setBuy_count(buy_count);
			
			purchaseCart = putUpudateData(params,purchaseCarts.get(0));
			count = this.purchaseCartMapper.updateByPrimaryKey(purchaseCart);
		}
		if(count > 0){
			purchaseCartResult.setId(purchaseCart.getId());
		}
		
		log.info("===================添加采购车结束addPurchaseCart===================");
		
		return purchaseCartResult;
	}


	/**
	 * 赋值采购车对象数据(更新)
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月25日 下午2:40:04 
	 * @param purchaseCart
	 * @return
	 */
	private PurchaseCart putUpudateData(PurchaseCartParams params,PurchaseCart purchaseCart) {
		// 购买数量
		Integer buyCount = params.getBuy_count() == null ? purchaseCart.getBuyCount() : params.getBuy_count();
		// 单价
		//String productUnitPriceStr = params.getProduct_unit_price();
		
		/** 系统自动更新价格 **/
		BigDecimal productUnitPrice  = getPrice(purchaseCart.getFkProductId(), purchaseCart.getFkProductSkuId(), buyCount);
		/*if(!StringUtils.isEmpty(productUnitPriceStr)){
			productUnitPrice = BigDecimal.valueOf(Double.valueOf(productUnitPriceStr));
		}else{
			productUnitPrice = purchaseCart.getProductUnitPrice();
		}*/
		
		// 价格校验
		//validPrice(purchaseCart.getFkProductId(), purchaseCart.getFkProductSkuId(), productUnitPrice.toString(), buyCount);
		
		// 节省价
		BigDecimal frugalPrice = BigDecimal.ZERO;
		// 总价
		BigDecimal newBigBuyCount = new BigDecimal(buyCount);
		BigDecimal totalPrice = productUnitPrice.multiply(newBigBuyCount);
		/** 购买数量 **/
		purchaseCart.setBuyCount(buyCount);
		/** 产品单价  **/
		if(productUnitPrice != null){
			purchaseCart.setProductUnitPrice(productUnitPrice);
		}
		/** 节省价 **/
		purchaseCart.setFrugalPrice(frugalPrice);
		/** 总价 **/
		purchaseCart.setTotalPrice(totalPrice);
		return purchaseCart;
	}

	/**
	 * 赋值采购车对象数据(新增)
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月25日 下午2:33:26 
	 * @param purchaseCart
	 * @param params
	 * @param member
	 * @param product
	 * @param productSku
	 */
	private PurchaseCart putAddData(PurchaseCartParams params) {
		
		Member member = memberMapper.selectByPrimaryKey(params.getMember_id());
		ProductWithBLOBs product = productMapper.selectByPrimaryKey(params.getProduct_id());
		String barcode = params.getProduct_barcode();
		ProductSku productSku = this.getProductByBarcode(barcode);
		
		/*if(params.getProduct_sku_id() != null){
			productSku = productSkuMapper.selectByPrimaryKey(params.getProduct_sku_id());
		}*/
		
		PurchaseCart purchaseCart = new PurchaseCart();
		// 购买数量
		Integer buyCount = params.getBuy_count() == null ? 0 : params.getBuy_count();
		// 单价
		BigDecimal productUnitPrice = BigDecimal.valueOf(Double.valueOf(params.getProduct_unit_price()));
		// 节省价
		BigDecimal frugalPrice = BigDecimal.ZERO;
		// 总价
		BigDecimal newBigBuyCount = new BigDecimal(buyCount);
		BigDecimal totalPrice = productUnitPrice.multiply(newBigBuyCount);
		
		/** 掌柜信息 **/
		purchaseCart.setFkBuyerMemberId(member.getId());
		purchaseCart.setFkBuyerNickName(member.getRealName());
		//purchaseCart.setFkBuyerStoreId(); 掌柜店铺ID
		
		/** 商户信息 **/
		purchaseCart.setFkSupplierStoreId(product.getFkStoreId());
		purchaseCart.setFkSuppliererMemberId(product.getFkMemberId());
		
		purchaseCart.setFkProductId(params.getProduct_id());
		purchaseCart.setVersionNo(product.getCurrentHistoryVersion());
		purchaseCart.setProductName(product.getName());
		purchaseCart.setProductLogoRsurl(product.getLogoRsurl());
		purchaseCart.setFkProductSkuId(params.getProduct_sku_id());
		
		/** 商品的sku条形码 **/
		purchaseCart.setBarCode(params.getProduct_barcode());
		/** sku属性名组合字符串 **/
		purchaseCart.setSkuPropertiesName(productSku.getSkuProperties());
		purchaseCart.setFkGameId(params.getGame_id());
		/** 购买数量 **/
		purchaseCart.setBuyCount(buyCount);
		/** 产品单价  **/
		purchaseCart.setProductUnitPrice(productUnitPrice);
		/** 节省价 **/
		purchaseCart.setFrugalPrice(frugalPrice);
		/** 总价 **/
		purchaseCart.setTotalPrice(totalPrice);
		purchaseCart.setState(Constants.CommonState.NORMAL.getValue());
		purchaseCart.setCreateTime(DateUtil.now());
		purchaseCart.setLastUpdateTime(DateUtil.now());
		purchaseCart.setCreateUserId(member.getId());
		purchaseCart.setLastUpdateUserId(member.getId());
		
		return purchaseCart;
	}

	/**
	 * 获取采购车产品Sku
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午8:41:28 
	 * @param member_id
	 * @param product_id
	 * @param product_sku_id
	 * @return
	 */
	private List<PurchaseCart> findPurchaseCart(PurchaseCartParams params) {
		PurchaseCartExample chaseCartExample = new PurchaseCartExample();
		chaseCartExample.createCriteria()
			.andFkBuyerMemberIdEqualTo(params.getMember_id())
			.andStateEqualTo(Constants.CommonState.NORMAL.getValue())
			.andFkProductSkuIdEqualTo(params.getProduct_sku_id());
		
		return this.purchaseCartMapper.selectByExample(chaseCartExample);
	}


	/**
	 * 删除采购车 
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午2:09:17 
	 * @param params
	 * @param is_success
	 */
	public SuccessResult deletePurchaseCart(PurchaseCartParams params) {
		SuccessResult result = new SuccessResult();
		String[] purchaseIds = params.getPurchase_ids();
		if(!ArrayUtils.isEmpty(purchaseIds)){
			for(String id : purchaseIds){
				PurchaseCart pct = this.purchaseCartMapper.selectByPrimaryKey(Long.valueOf(id));
				if(null != pct && !Constants.CommonState.DELETE.getValue().equals(pct.getState())){
					if(pct.getFkBuyerMemberId() != params.getMember_id()){
						throw new ServiceException("该微店长会员id输入不正确", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_DELETE_MEMBER_ID_WRONG); 
					}
					pct.setState(Constants.CommonState.DELETE.getValue());
					pct.setLastUpdateTime(DateUtil.now());
					pct.setLastUpdateUserId(params.getMember_id());
					int count = this.purchaseCartMapper.updateByPrimaryKey(pct);
					if(count > 0){
						result.setIs_success(true);
					}
				}else{
					throw new ServiceException("该purchase_id="+id+"的采购车记录不存在或者已删除", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_DELETE_PURCHASE_ID_NONEXISTENT); 
				}
			}
		}
		return result;
	}

	/**
	 * 更新采购车产品数量
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午2:10:49 
	 * @param params
	 * @param successResult
	 */
	public SuccessResult updatePurchaseCart(PurchaseCartParams params) {
		SuccessResult result = new SuccessResult();
		// 校验
		//this.validPurchaseCart(params);
		PurchaseCart purchaseCart = this.purchaseCartMapper.selectByPrimaryKey(params.getPurchase_id());
		
		if(purchaseCart == null || Constants.CommonState.DELETE.getValue().equals(purchaseCart.getState())){
			throw new ServiceException("该purchase_id="+params.getPurchase_id()+"的采购车记录不存在或者已删除", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_DELETE_PURCHASE_ID_NONEXISTENT);
		}
		
		if(purchaseCart.getFkBuyerMemberId() != params.getMember_id()){
			throw new ServiceException("该微店长会员id输入不正确", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_DELETE_MEMBER_ID_WRONG); 
		}
		
		purchaseCart =  this.putUpudateData(params, purchaseCart);
		int count = this.purchaseCartMapper.updateByPrimaryKey(purchaseCart);
		if(count > 0){
			result.setIs_success(true);
		}
		return result;
	}
	
	
	/**
	 * 校验用户、产品、产品sku
	 * @author : liguosheng 
	 * @CreateDate : 2015年9月24日 下午8:55:32 
	 * @param params
	 * @param member
	 * @param product
	 * @param productSku
	 */
	private void validPurchaseCart(PurchaseCartParams params) {
		
		Member member = null;
		ProductWithBLOBs product = null;
		ProductSku productSku = null;
		
		String barcode = params.getProduct_barcode();
		
		log.info("===========校验开始addPurchaseCart===========");
		
		member =  memberMapper.selectByPrimaryKey(params.getMember_id());
		if(member == null){
			throw new ServiceException("无效的微店长会员id", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_MEMBER_ID_NOT_INVALID);
		}
		
		product = productMapper.selectByPrimaryKey(params.getProduct_id());
		if(product == null){
			throw new ServiceException("无效的产品ID", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_PRODUCT_ID_NOT_INVALID);
		}
		
		
		if(!Constants.ProductIdentityType.SUPPLIER.getKey().equals(product.getIdentityType())){
			throw new ServiceException("产品发布者必须为供应商类型",SystemErrorCode.PurchaseCartErrorCode.PURCHASE_PRODUCT_IDENTITYTYPE_NOT_SUPPLIER);
		}
		
		productSku = this.getProductByBarcode(barcode);
		if(productSku == null){
			throw new ServiceException("无效的产品sku ID", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_PRODUCT_SKU_ID_NOT_INVALID);
		}
		if(!product.getId().equals(productSku.getFkProductId())){
			throw new ServiceException("产品ID与产品skuid不对应", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_PRODUCT_ID_AND_PRODUCT_SKU_ID_NOT_INVALID);
		}
		if(params.getProduct_sku_id() != null && !params.getProduct_sku_id().equals(productSku.getId())){
			throw new ServiceException("barcode与skuid不对应");
		}
		
		/*if(params.getProduct_sku_id() != null){
			productSku = productSkuMapper.selectByPrimaryKey(params.getProduct_sku_id());
			if(productSku == null){
				throw new ServiceException("无效的产品sku ID", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_PRODUCT_SKU_ID_NOT_INVALID);
			}
			if(!product.getId().equals(productSku.getFkProductId())){
				throw new ServiceException("产品ID与产品skuid不对应", SystemErrorCode.PurchaseCartErrorCode.PURCHASE_CART_ADD_PRODUCT_ID_AND_PRODUCT_SKU_ID_NOT_INVALID);
			}
		}*/
		
		
		log.info("===========校验结束addPurchaseCart===========");
	}
	
	/**
	 * 价格校验
	 * @author : liguosheng 
	 * @CreateDate : 2015年10月19日 下午5:26:31 
	 * @param params
	 */
	private void validPrice(Long productId,Long productSkuId,String product_unit_price,Integer buyCount){
		
		BigDecimal unitPrice = BigDecimal.valueOf(Double.valueOf(product_unit_price));
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("product_id", productId);
		map.put("count", buyCount);
		BigDecimal stePrice = this.purchaseCartDao.getStepPriceByProductSaleCount(map);
		
		boolean isError = false;
		
		if(stePrice != null){
			if(stePrice.compareTo(unitPrice) != 0){
				isError = true;
			}
		}else{
			ProductSku productSku = this.productSkuMapper.selectByPrimaryKey(productSkuId);
			if(productSku != null && productSku.getSettlementPrice() != null && productSku.getSettlementPrice().compareTo(unitPrice) != 0){
				isError = true;
			}
		}
		
		if(isError){
			throw new ServiceException("产品单价错误",SystemErrorCode.PurchaseCartErrorCode.PURCHASE_SALE_PRICE_ERROR);
		}
		
	}
	
	/**
	 * 获取阶梯价
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月9日 下午4:48:54 
	 * @param productId
	 * @param productSkuId
	 * @param product_unit_price
	 * @param buyCount
	 * @return
	 */
	private BigDecimal getPrice(Long productId,Long productSkuId,Integer buyCount){
		
		BigDecimal unitPrice  = BigDecimal.ZERO;
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("product_id", productId);
		map.put("count", buyCount);
		BigDecimal stePrice = this.purchaseCartDao.getStepPriceByProductSaleCount(map);
		
		if(stePrice != null){
			unitPrice =  stePrice;
		}else{
			ProductSku productSku = this.productSkuMapper.selectByPrimaryKey(productSkuId);
			if(productSku != null && productSku.getSettlementPrice() != null && productSku.getSettlementPrice().compareTo(unitPrice) != 0){
				unitPrice =  productSku.getSettlementPrice();
			}
		}
		return unitPrice;
		
	}
	
	
	
	/**
	 * 根据barcode获取产品sku信息
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月3日 下午1:02:42 
	 * @param barcode
	 * @return
	 */
	public ProductSku getProductByBarcode(String barcode){
		ProductSkuExample example  = new ProductSkuExample();
		example.createCriteria().andProductSkuBarcodeEqualTo(barcode).andStateEqualTo(Constants.CommonState.NORMAL.getValue());
		List<ProductSku> pros = this.productSkuMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(pros)){
			return null;
		}
		return pros.get(0);
	}

	/**
	 * 批量更新
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月4日 上午10:49:03 
	 * @param params
	 * @return
	 */
	public SuccessResult updatePurchaseCartBatch(PurchaseCartParams params) {
		SuccessResult result = new SuccessResult();
		List<PurchaseCartParams> paramslist = params.getPurchases();
		
		Long memberId = params.getMember_id();
		
		validBatch(paramslist,memberId);
		for(PurchaseCartParams param : paramslist){
			param.setMember_id(memberId);
			updatePurchaseCart(param);
		}
		result.setIs_success(true);
		return result;
	}
	
	/**
	 * 采购车批量参数校验
	 * @author : liguosheng 
	 * @CreateDate : 2015年11月4日 上午10:55:37 
	 * @param paramslist
	 */
	private void validBatch(List<PurchaseCartParams> paramslist,Long memberId) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		for(PurchaseCartParams param : paramslist){
			param.setMember_id(memberId);
			String error = "";
			Set<ConstraintViolation<PurchaseCartParams>> violations = validator.validate(param, PurchaseCartUpdateValid.class);
			if(!CollectionUtils.isEmpty(violations)){
				for(ConstraintViolation<PurchaseCartParams> violation : violations){
					error +=violation.getMessage()+" ";
				}
				throw new ServiceException(error,SystemErrorCode.CommonErrorCode.INPUT_VALIDATION_ERROR);
			}
		}
	}
	
	
}
