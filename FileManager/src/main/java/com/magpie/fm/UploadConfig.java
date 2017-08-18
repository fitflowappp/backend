package com.magpie.fm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@Configuration
public class UploadConfig {

	private @Value("#{file.allowedImgTypes}")
	String allowedImgTypes;
	private @Value("#{file.allowedVideoTypes}")
	String allowedVideoTypes;
	private @Value("#{file.defaultImgType}")
	String defaultImgType;
	private @Value("#{file.defaultVideoType}")
	String defaultVideoType;
	private @Value("#{file.showImgPath}")
	String showImgPath;
	private @Value("#{file.showOtherPath}")
	String showOtherPath;
	private @Value("#{file.showTemp}")
	String showTemp;
	private @Value("#{file.showVideoPath}")
	String showVideoPath;
	private @Value("#{file.uploadImgPath}")
	String uploadImgPath;
	private @Value("#{file.uploadOtherPath}")
	String uploadOtherPath;
	private @Value("#{file.publicPath}")
	String publicPath;
	private @Value("#{file.privatePath}")
	String privatePath;
	private @Value("#{file.uploadTemp}")
	String uploadTemp;
	private @Value("#{file.uploadVideoPath}")
	String uploadVideoPath;
	private @Value("#{file.imgSize}")
	String imgSize;

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

	public String getAllowedImgTypes() {
		return allowedImgTypes;
	}

	public String getAllowedVideoTypes() {
		return allowedVideoTypes;
	}

	public String getDefaultImgType() {
		return defaultImgType;
	}

	public String getDefaultVideoType() {
		return defaultVideoType;
	}

	public String getShowImgPath() {
		return showImgPath;
	}

	public String getShowOtherPath() {
		return showOtherPath;
	}

	public String getShowTemp() {
		return showTemp;
	}

	public String getShowVideoPath() {
		return showVideoPath;
	}

	public String getUploadImgPath() {
		return uploadImgPath;
	}

	public String getUploadOtherPath() {
		return uploadOtherPath;
	}

	public String getUploadTemp() {
		return uploadTemp;
	}

	public String getUploadVideoPath() {
		return uploadVideoPath;
	}

	public void setAllowedImgTypes(String allowedImgTypes) {
		this.allowedImgTypes = allowedImgTypes;
	}

	public void setAllowedVideoTypes(String allowedVideoTypes) {
		this.allowedVideoTypes = allowedVideoTypes;
	}

	public void setDefaultImgType(String defaultImgType) {
		this.defaultImgType = defaultImgType;
	}

	public void setDefaultVideoType(String defaultVideoType) {
		this.defaultVideoType = defaultVideoType;
	}

	public void setShowImgPath(String showImgPath) {
		this.showImgPath = showImgPath;
	}

	public void setShowOtherPath(String showOtherPath) {
		this.showOtherPath = showOtherPath;
	}

	public void setShowTemp(String showTemp) {
		this.showTemp = showTemp;
	}

	public void setShowVideoPath(String showVideoPath) {
		this.showVideoPath = showVideoPath;
	}

	public void setUploadImgPath(String uploadImgPath) {
		this.uploadImgPath = uploadImgPath;
	}

	public void setUploadOtherPath(String uploadOtherPath) {
		this.uploadOtherPath = uploadOtherPath;
	}

	public void setUploadTemp(String uploadTemp) {
		this.uploadTemp = uploadTemp;
	}

	public void setUploadVideoPath(String uploadVideoPath) {
		this.uploadVideoPath = uploadVideoPath;
	}

	public String getPublicPath() {
		return publicPath;
	}

	public void setPublicPath(String publicPath) {
		this.publicPath = publicPath;
	}

	public String getPrivatePath() {
		return privatePath;
	}

	public void setPrivatePath(String privatePath) {
		this.privatePath = privatePath;
	}

}
