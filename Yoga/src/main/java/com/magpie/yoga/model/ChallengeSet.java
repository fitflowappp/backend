package com.magpie.yoga.model;

import java.util.List;

import com.magpie.base.model.BaseModel;

public class ChallengeSet extends BaseModel {

	private String id;
	private List<Challenge> challenges;
	private boolean primary;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Challenge> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<Challenge> challenges) {
		this.challenges = challenges;
	}

	/**
	 * @return the primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary
	 *            the primary to set
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
