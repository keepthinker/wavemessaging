package com.keepthinker.wavemessaging.dao.model;

import java.util.Date;

public class ClientInfo {
	private long id;
	private long clientId;
	private String username;
	private String password;
	private Date updateTime;
	private Date createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ClientInfo [id=" + id + ", clientId=" + clientId + ", username=" + username + ", password=" + password
				+ ", updateTime=" + updateTime + ", createTime=" + createTime + "]";
	}
	
}
