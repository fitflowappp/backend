package com.magpie.yoga.view;

import java.util.List;

import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.Topic;
import com.magpie.yoga.model.TopicSingles;

public class TopicView extends Topic{
	private Challenge challenge=null;
	
	
	private List<TopicSinglesView> singles;

	
	public Challenge getChallenge() {
		return challenge;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public List<TopicSinglesView> getSingles() {
		return singles;
	}

	public void setSingles(List<TopicSinglesView> singles) {
		this.singles = singles;
	}
	
	
}
