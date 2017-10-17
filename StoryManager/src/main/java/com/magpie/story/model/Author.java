package com.magpie.story.model;

import com.magpie.share.ResourceRef;

public class Author {

	private String uid;
	private String name;

	private ResourceRef headerImg;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceRef getHeaderImg() {
		return headerImg;
	}

	public void setHeaderImg(ResourceRef headerImg) {
		this.headerImg = headerImg;
	}

}
