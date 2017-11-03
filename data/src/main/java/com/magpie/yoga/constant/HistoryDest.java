package com.magpie.yoga.constant;

public enum HistoryDest {

	CHALLENGE(1, "challenge"), //
	WORKOUT(2, "workout"), //
	ROUTINE(3, "routine"); //

	private int code;
	private String name;

	private HistoryDest(int code, String name) {
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
