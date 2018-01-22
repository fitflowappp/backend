package com.magpie.user.model;

import com.magpie.base.model.BaseModel;

public class UserBackgroundMusic extends BaseModel {
	public enum Status{
		ON(0),OFF(1);
		private int code;
		private Status(int code){
			this.code=code;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
	}
	public enum MusicType{
		Classic(0),Relaxing(1),Energetic(2);
		private int code;

		private MusicType(int code){
			this.code=code;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
	}
	private String id;
	private String userId;
	private int status=1;
	private int musicType;
	private float volume;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getVolume() {
		return volume;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public int getMusicType() {
		return musicType;
	}
	public void setMusicType(int musicType) {
		this.musicType = musicType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
