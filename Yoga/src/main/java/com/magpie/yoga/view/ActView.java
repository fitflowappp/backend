package com.magpie.yoga.view;

public class ActView {

	private int type;
	private String content;
	private String shareTitle;
	private String shareUrl;
	
	

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public ActView() {

	}

	public ActView(int type, String content) {
		this.type = type;
		this.content = content;
	}
	public ActView(int type, String content, String shareTitle , String shareUrl) {
		this.type = type;
		this.content = content;
		this.shareTitle=shareTitle;
		this.shareUrl=shareUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
