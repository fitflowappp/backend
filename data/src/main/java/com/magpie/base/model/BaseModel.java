package com.magpie.base.model;

import java.util.Date;

import com.magpie.base.utils.DateUtil;


/**
 * Model基类
 * 
 * @author Jiangqinrao
 * 
 */
public class BaseModel {

	// 创建日期
	private Date crDate = DateUtil.getCurrentDate();
	// 修改日期
	private Date mdDate = DateUtil.getCurrentDate();
	// 创建者uid
	private String crUid;
	// 修改者uid
	private String mdUid;

	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	public Date getMdDate() {
		return mdDate;
	}

	public void setMdDate(Date mdDate) {
		this.mdDate = mdDate;
	}

	public String getCrUid() {
		return crUid;
	}

	public void setCrUid(String crUid) {
		this.crUid = crUid;
	}

	public String getMdUid() {
		return mdUid;
	}

	public void setMdUid(String mdUid) {
		this.mdUid = mdUid;
	}

}
