package com.magpie.yoga.model;

public class AppUpdateSetting {
	public static int NORMAL_UPDATE=0;
	public static int FORCE_UPDATE=NORMAL_UPDATE+1;
	private String id;
	private AppUpdate update;
	private AppUpdate force;
	private int system;
	
	
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
	public AppUpdate getUpdate() {
		return update;
	}
	public void setUpdate(AppUpdate update) {
		this.update = update;
	}
	public AppUpdate getForce() {
		return force;
	}
	public void setForce(AppUpdate force) {
		this.force = force;
	}
	
	
}
