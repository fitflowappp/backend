package com.magpie.story.model;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class Line extends BaseModel {

	private String id;
	private String authorId;
	private String storyId;
	
	private ResourceRef imgRef;
	private int index;
	private int type;// 0文字,1图片,2分支,10道具,11非模糊显示图片
	private String name;
	private String nameColorCode;
	
	private String text;
	private boolean share;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getStoryId() {
		return storyId;
	}
	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}
	public ResourceRef getImgRef() {
		return imgRef;
	}
	public void setImgRef(ResourceRef imgRef) {
		this.imgRef = imgRef;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameColorCode() {
		return nameColorCode;
	}
	public void setNameColorCode(String nameColorCode) {
		this.nameColorCode = nameColorCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}
	
}
