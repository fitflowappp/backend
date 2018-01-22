package com.magpie.user.view;
/**
 * app用户信用
* @ClassName: AppUserView  
* @Description: 一般用于app获取用户信息，会添加更多的配置数据 
* @author jiangming.xia  
* @date 2018年1月19日 上午11:22:22  
*
 */
public class AppUserView extends UserView {
	private int musicType;
	private int musicStatus;
	private float musicVolume;
	public int getMusicType() {
		return musicType;
	}
	public void setMusicType(int musicType) {
		this.musicType = musicType;
	}
	public int getMusicStatus() {
		return musicStatus;
	}
	public void setMusicStatus(int musicStatus) {
		this.musicStatus = musicStatus;
	}
	public float getMusicVolume() {
		return musicVolume;
	}
	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}
}
