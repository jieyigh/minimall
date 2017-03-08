package com.jbh360.trade.vo.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ExpressComputeProduct {
	
	public interface ExpressComputeProductViolate{};

	@NotNull(groups=ExpressComputeProductViolate.class,message="产品ID不能为空")
	private Long product_id;
	
	@NotEmpty(groups=ExpressComputeProductViolate.class,message="购买数量不能为空")
	private String buy_count;

	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
	}

	public String getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(String buy_count) {
		this.buy_count = buy_count;
	}
	
	
	
}
