package com.jbh360.trade.utils;

/**  
* @Title: Constants.java
* @Package com.jbh360.trade.utils
* @Description: TODO(用一句话描述该文件做什么)
* @author joe 
* @email aboutou@126.com 
* @date 2015年11月23日 上午11:43:04
* @version V3.0  
*/
public class OrderConstants {
	
	public static enum AmountType{
		创业基金((short)1,"创业基金"),
		现金((short)2,"现金"),
		邮费((short)3,"现金");
		private final Short key;
		private final String value;
		private AmountType(Short key,String value){
			this.key = key;
			this.value = value;
		}
		public Short getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		public static AmountType getValue(Short key){
			for(AmountType inst : values()){
				if(key == inst.getKey()){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + key);
		}
	}
	
	public static enum AmountWay{
		钱包((short)1),
		退款单((short)2);
		private final Short key;
		private AmountWay(Short key){
			this.key = key;
		}
		public Short getKey() {
			return key;
		}
		public static AmountWay getValue(Short key){
			for(AmountWay inst : values()){
				if(key == inst.getKey()){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + key);
		}
	}
	public static enum PayState{
		未结算((short)0),
		已结算((short)1);
		private final Short key;
		private PayState(Short key){
			this.key = key;
		}
		public Short getKey() {
			return key;
		}
		public static PayState getValue(Short key){
			for(PayState inst : values()){
				if(key == inst.getKey()){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + key);
		}
	}
	
	public static enum TranType{
		入账((short)1),
		退款((short)2);
		private final Short key;
		private TranType(Short key){
			this.key = key;
		}
		public Short getKey() {
			return key;
		}
		public static TranType getValue(Short key){
			for(TranType inst : values()){
				if(key == inst.getKey()){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + key);
		}
	}
	public static enum PreferentialRecordType{
		GAME("GAME"),
		COUPON("COUPON");
		
		private final String value;
		private PreferentialRecordType(final String value){
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		public static PreferentialRecordType getValue(String value){
			for(PreferentialRecordType inst : values()){
				if(value.equals(inst.getValue())){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + value);
		}
	}
	
	public static enum Identity{
		SUCCESS(true),
		FAIL(false);
		private final Boolean value;
		private Identity(final Boolean value){
			this.value = value;
		}
		public boolean isValue() {
			return value;
		}
		public static Identity getValue(Boolean value){
			for(Identity inst : values()){
				if(value.equals(inst.isValue())){
					return inst;
				}
			}
			throw new IllegalArgumentException("不支持的常量：" + value);
		}
	}
}
