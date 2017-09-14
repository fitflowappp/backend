package com.magpie.yoga.model;

import java.util.List;

public class ChallengeSet {

	private String id;
	private List<Challenge> challenges;
	
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
	
}
