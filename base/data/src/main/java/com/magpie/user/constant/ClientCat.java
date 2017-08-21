package com.magpie.user.constant;

public enum ClientCat {
	ANDROID(0, "android"), // android客戶端
	WECHAT(2, "wechat"), // 微信客戶端
	IPHONE(1, "ios"), // iphone客戶端
	PC(5,"pc"),//pc
	UNKNOWN(10, "unknown client");

	private int code;
	private String name;

	private ClientCat(int code, String name) {
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
