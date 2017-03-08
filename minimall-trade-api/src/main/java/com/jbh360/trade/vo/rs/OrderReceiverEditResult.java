package com.jbh360.trade.vo.rs;

import javax.xml.bind.annotation.XmlElement;

public class OrderReceiverEditResult {

	private Long id;
	
	private String receiverName;
	
	private String receiverAreaCcode;
	
	private String addressRegionName;
	
	private String receiverAddress;
	
	private String receiverZipCode;
	
	private String receiverPhoneArea;
	
	private String receiverPhoneNumber;
	
	private String receiverPhoneExt;
	
	private String receiverMobile;
	
	private String receiverType;

	@XmlElement(name = "order_receiver_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "receiver_name")
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@XmlElement(name = "receiver_area_ccode")
	public String getReceiverAreaCcode() {
		return receiverAreaCcode;
	}

	public void setReceiverAreaCcode(String receiverAreaCcode) {
		this.receiverAreaCcode = receiverAreaCcode;
	}

	@XmlElement(name = "address_region_name")
	public String getAddressRegionName() {
		return addressRegionName;
	}

	public void setAddressRegionName(String addressRegionName) {
		this.addressRegionName = addressRegionName;
	}

	@XmlElement(name = "receiver_address")
	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	@XmlElement(name = "receiver_zip_code")
	public String getReceiverZipCode() {
		return receiverZipCode;
	}

	public void setReceiverZipCode(String receiverZipCode) {
		this.receiverZipCode = receiverZipCode;
	}

	@XmlElement(name = "receiver_phone_area")
	public String getReceiverPhoneArea() {
		return receiverPhoneArea;
	}

	public void setReceiverPhoneArea(String receiverPhoneArea) {
		this.receiverPhoneArea = receiverPhoneArea;
	}

	@XmlElement(name = "receiver_phone_number")
	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}

	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}

	@XmlElement(name = "receiver_phone_ext")
	public String getReceiverPhoneExt() {
		return receiverPhoneExt;
	}

	public void setReceiverPhoneExt(String receiverPhoneExt) {
		this.receiverPhoneExt = receiverPhoneExt;
	}

	@XmlElement(name = "receiver_mobile")
	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	@XmlElement(name = "receiver_type")
	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}
	
	
}
