package com.magpie.yoga.view;

import java.util.List;

import com.magpie.yoga.model.ChallengeSet;

public class ChallengeSetView extends ChallengeSet {

	private String currentChallengeId;

	private List<Integer> statuses;// challenge status list

	private List<Boolean> avails;

	public String getCurrentChallengeId() {
		return currentChallengeId;
	}

	public void setCurrentChallengeId(String currentChallengeId) {
		this.currentChallengeId = currentChallengeId;
	}

	/**
	 * @return the statuses
	 */
	public List<Integer> getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses
	 *            the statuses to set
	 */
	public void setStatuses(List<Integer> statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return the avails
	 */
	public List<Boolean> getAvails() {
		return avails;
	}

	/**
	 * @param avails the avails to set
	 */
	public void setAvails(List<Boolean> avails) {
		this.avails = avails;
	}

}
