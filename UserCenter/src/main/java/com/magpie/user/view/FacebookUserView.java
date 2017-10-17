package com.magpie.user.view;

import com.magpie.user.model.FaceBookUser;
import com.magpie.user.model.User;
import com.magpie.yoga.model.UserState;

public class FacebookUserView extends FaceBookUser {

	private User user;

	private UserState userState;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the userState
	 */
	public UserState getUserState() {
		return userState;
	}

	/**
	 * @param userState the userState to set
	 */
	public void setUserState(UserState userState) {
		this.userState = userState;
	}

}
