package com.magpie.resource.constant;

public enum ResourceType {
	
	GODDESS("100"), // 女神活动图片

	USER("user"), //
	ALIPAYBILL("8"), //
	YTPAYBILL("9"), // yin tong
	POST("0"), //
	RESOURCE("1"), //
	QUESTION("2"), //
	WORK("3"), //
	STANDALONE("4"), //
	OTHER("5"), //
	OUTERSOURCE("6"), //
	STANDALONE_WORK("7"), // 外部资源 6
	
	PURCHASEPLANDETAIL("10"), // 采购计划明细
	
	SKUCODE("20");//产品sku编码

	private String code;

	private ResourceType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
