package com.magpie.user.view;

import com.magpie.user.model.Admin;

public class AdminView extends Admin {

	// sessionId
	private String sessionId;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
