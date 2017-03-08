package com.jbh360.trade.vo.rs;


public class PayResult {

	private boolean is_success;
	
	private String ret_msg;
	
	private String data;
	
	private String ret_flag;
	
	private String error_code;

	public boolean getIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getRet_flag() {
		return ret_flag;
	}

	public void setRet_flag(String ret_flag) {
		this.ret_flag = ret_flag;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	
	
}
