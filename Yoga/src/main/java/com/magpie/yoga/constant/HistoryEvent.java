package com.magpie.yoga.constant;

public enum HistoryEvent {
	
	START(1, "start watching"), // 
	SKIPPED(2, "skip watching"), // 
	STOP(3, "stop watching");

	private int code;
	private String name;

	private HistoryEvent(int code, String name) {
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
