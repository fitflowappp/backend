package com.magpie.resource.constant;

public enum ResourceContentType {

	IMG("IMG"), VIDEO("VIDEO"), OTHER("OTHER");

	private String code;

	private ResourceContentType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
