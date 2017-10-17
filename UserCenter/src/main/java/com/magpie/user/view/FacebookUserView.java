package com.magpie.user.view;

import com.magpie.user.model.User;
import com.magpie.yoga.model.UserConfiguration;
import com.magpie.yoga.model.UserState;

public class FacebookUserView {

	private String facebookUid;//

	private byte[] headerImgContent;

	private String gender;
	private String name;

	private String email;

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

	public String getFacebookUid() {
		return facebookUid;
	}

	public void setFacebookUid(String facebookUid) {
		this.facebookUid = facebookUid;
	}

	public byte[] getHeaderImgContent() {
		return headerImgContent;
	}

	public void setHeaderImgContent(byte[] headerImgContent) {
		this.headerImgContent = headerImgContent;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
