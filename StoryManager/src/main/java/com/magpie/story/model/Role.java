package com.magpie.story.model;

import com.magpie.share.ResourceRef;

public class Role {

	private String id;
	private Color color;
	private ColorRef nameColor;
	private ColorRef textColor;
	private ColorRef dialogColor;

	private String storyId;
	private String storySeriesId;

	private String name;
	private ResourceRef headerImg;

	private int position;// 0:左边，1右边

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ColorRef getNameColor() {
		return nameColor;
	}

	public void setNameColor(ColorRef nameColor) {
		this.nameColor = nameColor;
	}

	public ColorRef getTextColor() {
		return textColor;
	}

	public void setTextColor(ColorRef textColor) {
		this.textColor = textColor;
	}

	public ColorRef getDialogColor() {
		return dialogColor;
	}

	public void setDialogColor(ColorRef dialogColor) {
		this.dialogColor = dialogColor;
	}

	/**
	 * @return the storySeriesId
	 */
	public String getStorySeriesId() {
		return storySeriesId;
	}

	/**
	 * @param storySeriesId
	 *            the storySeriesId to set
	 */
	public void setStorySeriesId(String storySeriesId) {
		this.storySeriesId = storySeriesId;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the headerImg
	 */
	public ResourceRef getHeaderImg() {
		return headerImg;
	}

	/**
	 * @param headerImg
	 *            the headerImg to set
	 */
	public void setHeaderImg(ResourceRef headerImg) {
		this.headerImg = headerImg;
	}

}
