package com.magpie.base.dao;

import java.util.Calendar;
import java.util.Date;

public class Duplicate {
	private String id;
	private Date opDate = Calendar.getInstance().getTime();
	private String model;
	private Object object;

	public Duplicate() {

	}

	public Duplicate(String model, Object object) {
		this.model = model;
		this.object = object;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
