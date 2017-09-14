package com.magpie.user.constant;

public enum RoleType {
	ADMIN("0", "系统管理员"), //
	CUSTOMER("1", "用户"), //
	BUYER("2", "买手"), //
	STOREKEEPER("3", "仓库管理员"), //
	STYLIST("4", "搭配师"), //
	PURCHAER("5", "采购人"), //
	ACCOUNTANT("6", "财务"), //

	CAMPUSAMBASSADOR("10", "校园大使");//

	private String code;
	private String desc;

	private RoleType() {

	}

	private RoleType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static String getDesc(String code) {
		for (RoleType roleType : RoleType.values()) {
			if (code.equals(roleType.getCode())) {
				return roleType.getDesc() == null ? "" : roleType.getDesc();
			}
		}
		return "";
	}
}
