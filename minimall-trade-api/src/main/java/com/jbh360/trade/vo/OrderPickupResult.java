package com.jbh360.trade.vo;

import javax.xml.bind.annotation.XmlElement;


public class OrderPickupResult{
	
	private String pickup_name;
	
	private String pickup_mobile;
	
	private String pickup_address;
	
	private String pickupplace_phone_number;
	
	private String pickupplace_contact_name;
	
	private String pickpuplace_business_time;

	@XmlElement(name="pickup_name")
	public String getPickup_name() {
		return pickup_name;
	}

	public void setPickup_name(String pickup_name) {
		this.pickup_name = pickup_name;
	}

	@XmlElement(name="pickup_mobile")
	public String getPickup_mobile() {
		return pickup_mobile;
	}

	public void setPickup_mobile(String pickup_mobile) {
		this.pickup_mobile = pickup_mobile;
	}

	@XmlElement(name="pickup_address")
	public String getPickup_address() {
		return pickup_address;
	}

	public void setPickup_address(String pickup_address) {
		this.pickup_address = pickup_address;
	}

	@XmlElement(name="pickupplace_phone_number")
	public String getPickupplace_phone_number() {
		return pickupplace_phone_number;
	}

	public void setPickupplace_phone_number(String pickupplace_phone_number) {
		this.pickupplace_phone_number = pickupplace_phone_number;
	}

	@XmlElement(name="pickupplace_contact_name")
	public String getPickupplace_contact_name() {
		return pickupplace_contact_name;
	}

	public void setPickupplace_contact_name(String pickupplace_contact_name) {
		this.pickupplace_contact_name = pickupplace_contact_name;
	}

	@XmlElement(name="pickpuplace_business_time")
	public String getPickpuplace_business_time() {
		return pickpuplace_business_time;
	}

	public void setPickpuplace_business_time(String pickpuplace_business_time) {
		this.pickpuplace_business_time = pickpuplace_business_time;
	}

	
}
