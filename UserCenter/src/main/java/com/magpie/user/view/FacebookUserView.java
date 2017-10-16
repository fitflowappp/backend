package com.magpie.user.view;

import com.magpie.user.model.FaceBookUser;
import com.magpie.user.model.User;

public class FacebookUserView extends FaceBookUser {

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
