package com.magpie.getui.getui.cache;

public class GetuiMessage {

	public static final String SILENT_TYPE = "0";
	public static final String DISP_TYPE = "1";
	public static final String SHOW_ORDER_TYPE = "2";
	public static final String STORY_READ_PUSH = "1001";

	private String type;
	private String content;

	public GetuiMessage(String type, String content) {
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
