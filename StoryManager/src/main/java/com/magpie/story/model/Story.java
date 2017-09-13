package com.magpie.story.model;

import java.util.List;

import com.magpie.base.model.BaseModel;

public class Story extends BaseModel {

	private List<Line> lines;
	private int no;
	private String authorId;

	/**
	 * @return the lines
	 */
	public List<Line> getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(int no) {
		this.no = no;
	}
	
}
