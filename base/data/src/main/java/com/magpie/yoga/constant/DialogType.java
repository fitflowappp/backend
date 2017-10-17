package com.magpie.yoga.constant;

public enum DialogType {

	WORKOUT(1, "achieve workouts num"), //
	DURATION(2, "achieve duration minutes"); //

	private int code;
	private String name;

	private DialogType(int code, String name) {
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
