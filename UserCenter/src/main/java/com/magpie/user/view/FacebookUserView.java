package com.magpie.user.view;

import com.magpie.user.model.FaceBookUser;
import com.magpie.user.model.User;
import com.magpie.yoga.model.UserConfiguration;
import com.magpie.yoga.model.UserState;

public class FacebookUserView extends FaceBookUser {

	private User user;

	private UserState userState;

	private long shareCount;

	private UserConfiguration userConfiguration;

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
	 * @param userState
	 *            the userState to set
	 */
	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	/**
	 * @return the shareCount
	 */
	public long getShareCount() {
		return shareCount;
	}

	/**
	 * @param shareCount
	 *            the shareCount to set
	 */
	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}

	/**
	 * @return the userConfiguration
	 */
	public UserConfiguration getUserConfiguration() {
		return userConfiguration;
	}

	/**
	 * @param userConfiguration
	 *            the userConfiguration to set
	 */
	public void setUserConfiguration(UserConfiguration userConfiguration) {
		this.userConfiguration = userConfiguration;
	}

}
