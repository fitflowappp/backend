package com.magpie.user.view;

import com.magpie.user.model.User;

public class UserView extends User {

	private String sessionId;

	private int registerDays;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the registerDays
	 */
	public int getRegisterDays() {
		return registerDays;
	}

	/**
	 * @param registerDays the registerDays to set
	 */
	public void setRegisterDays(int registerDays) {
		this.registerDays = registerDays;
	}

}
