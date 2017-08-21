package com.magpie.user.constant;

public enum UserFrom {
	QQ(0, "qq"), // 
	WECHAT(2, "wechat"), //
	WEIBO(1, "weibo"), // 
	PC(5,"pc");

	private int code;
	private String name;

	private UserFrom(int code, String name) {
		this.setCode(code);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
