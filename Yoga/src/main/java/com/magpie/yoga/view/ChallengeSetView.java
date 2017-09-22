package com.magpie.yoga.view;

import java.util.List;

public class ChallengeSetView {

	private String id;
	private List<ChallengeView> challenges;
	private boolean primary;

	private String currentChallengeId;

	private int status;
	private boolean avail;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isAvail() {
		return avail;
	}

	public void setAvail(boolean avail) {
		this.avail = avail;
	}

	public String getCurrentChallengeId() {
		return currentChallengeId;
	}

	public void setCurrentChallengeId(String currentChallengeId) {
		this.currentChallengeId = currentChallengeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ChallengeView> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<ChallengeView> challenges) {
		this.challenges = challenges;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
