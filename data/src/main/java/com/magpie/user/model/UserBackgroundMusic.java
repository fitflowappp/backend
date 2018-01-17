package com.magpie.user.model;

import com.magpie.base.model.BaseModel;

public class UserBackgroundMusic extends BaseModel {
	public enum Status{
		ON(0),OFF(1);
		private Status(int code){
			
		}
	}
	public enum MusicType{
		Classic(0),Relaxing(1),Energetic(2);
		private MusicType(int code){
			
		}
	}
	private String userId;
	private Status status;
	private MusicType musicType;
	private float volume;
	
	
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public MusicType getMusicType() {
		return musicType;
	}
	public void setMusicType(MusicType musicType) {
		this.musicType = musicType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
