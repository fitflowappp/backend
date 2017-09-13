package com.magpie.uc.view;

import com.magpie.user.model.User;

public class UserView extends User{

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
