package com.magpie.yoga.constant;

public enum HistoryEvent {

	UNWATCH(0, "start watching"), //
	START(1, "start watching"), //
	STOP(2, "stop watching"), //
	SKIPPED(3, "skip watching"), //
	COMPLETE(4, "complete watching"); //

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
