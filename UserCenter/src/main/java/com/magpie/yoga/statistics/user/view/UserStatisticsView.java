package com.magpie.yoga.statistics.user.view;

import com.magpie.user.view.FacebookUserView;
import com.magpie.user.model.UserBackgroundMusic.MusicType;
import com.magpie.user.model.UserBackgroundMusic.Status;

public class UserStatisticsView extends FacebookUserView {
	private Status musicStatus;
	private MusicType musicType;
	public Status getMusicStatus() {
		return musicStatus;
	}
	public void setMusicStatus(Status musicStatus) {
		this.musicStatus = musicStatus;
	}
	public MusicType getMusicType() {
		return musicType;
	}
	public void setMusicType(MusicType musicType) {
		this.musicType = musicType;
	}
	
	
}
