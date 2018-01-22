package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class AppUpdate extends BaseModel {
	private String id;
	private String desc;
	private int build;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
	
	
	
	
}
