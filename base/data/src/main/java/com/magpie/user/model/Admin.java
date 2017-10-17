package com.magpie.user.model;

import com.magpie.base.model.BaseModel;

/**
 * 
 * 管理员及搭配师
 * 
 * @author msdj0
 * 
 */
public class Admin extends BaseModel {

	private String id;
	private String name;
	private String password;
	private String role;// {#RoleType}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
