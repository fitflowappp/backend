package com.magpie.base.view;

public class Result {

	public static final String CODE_SUCCESS = "1";
	public static final String CODE_FAILURE = "0";

	public static final Result SUCCESS = new Result(CODE_SUCCESS, "操作成功");
	public static final Result FAILURE = new Result(CODE_FAILURE, "操作失败");

	private String code;
	private String msg;

	public Result() {
	}

	public Result(String code) {
		this.code = code;
	}

	public Result(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

