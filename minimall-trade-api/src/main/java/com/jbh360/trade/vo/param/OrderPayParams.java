package com.jbh360.trade.vo.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class OrderPayParams {
	
	public interface OrderPayIdNotNullParams{};
	
	public interface OrderPayNotNullParams{};

	@NotNull(groups={OrderPayIdNotNullParams.class})
	private Long id;
	
	private String openid;
	
	
	
	@NotNull(groups={OrderPayNotNullParams.class})
	private Long oid;
	
	@NotNull(groups={OrderPayNotNullParams.class})
	private Long pid;
	
	
	private Long sid;
	
	
	@NotEmpty(groups={OrderPayNotNullParams.class})
	@Length(max=50,groups={OrderPayNotNullParams.class})
	private String channel;
	
	private String service;
	
	private Long current_user_id ;

	private Long member_id;
	
	private String edition;
	
	private String return_url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Long getCurrent_user_id() {
		return current_user_id;
	}

	public void setCurrent_user_id(Long current_user_id) {
		this.current_user_id = current_user_id;
	}

	public Long getMember_id() {
		return member_id;
	}

	public void setMember_id(Long member_id) {
		this.member_id = member_id;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	
	
	
}
