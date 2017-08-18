package com.magpie.resource.model;

public class ResourceRef {

	private String id;
	// 资源URI（外部浏览用）
	private String contentUri;

	private int height;// 高
	private int width;// 宽

	public ResourceRef() {
	}

	public ResourceRef(String contentUri) {
		this.contentUri = contentUri;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentUri() {
		return contentUri;
	}

	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}

}
