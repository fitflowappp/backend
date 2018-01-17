package com.magpie.statistics.user.view;

import com.magpie.user.view.FacebookUserView;

public class UserStatisticsView extends FacebookUserView {
	private int musicStatus;
	private int musicType;
	public int getMusicStatus() {
		return musicStatus;
	}
	public void setMusicStatus(int musicStatus) {
		this.musicStatus = musicStatus;
	}
	public int getMusicType() {
		return musicType;
	}
	public void setMusicType(int musicType) {
		this.musicType = musicType;
	}
	
	
}
