package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class AppVersion extends BaseModel {
	private String id;
	private int system=0;//0是android,1是苹果
	private String version;
	private int build;
	
	
	
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getSystem() {
		return system;
	}
	public void setSystem(int system) {
		this.system = system;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	
}
