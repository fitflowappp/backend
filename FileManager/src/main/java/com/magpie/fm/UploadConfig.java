package com.magpie.fm;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@Configuration
public class UploadConfig {
	// uploadTemp=/data/bbsdata-pro/temp/
	// showTemp=/preview/
	// privatePath=/data/bbsdata-pro/upload
	// publicPath=/data/bbsdata-pro/upload/public
	// uploadImgPath=/img
	// showImgPath=/view/img
	// uploadVideoPath=/video
	// showVideoPath=/view/video
	// uploadOtherPath=/other
	// showOtherPath=/view/other
	// defaultVideoType=.mp4
	// defaultImgType=.jpg
	// allowedImgTypes=.jpg.jpeg.png.gif
	// allowedVideoTypes=.mp4
	// imgSize=640x205 100x100 196x110

	private String basedir = "/data/file";
	private String uploadTemp = basedir + "/temp/";
	private String showTemp = "/preview/";
	private String privatePath = basedir + "/upload";
	private String publicPath = basedir + "/upload/public";
	private String uploadImgPath = "/img";
	private String showImgPath = "/view/img";
	private String uploadVideoPath = "/video";
	private String showVideoPath = "/view/video";
	private String uploadOtherPath = "/other";
	private String showOtherPath = "/view/other";
	private String defaultVideoType = ".mp4";
	private String defaultImgType = ".jpg";
	private String allowedImgTypes = ".jpg.jpeg.png.gif";
	private String allowedVideoTypes = ".mp4";
	private String imgSize = "640x205 100x100 196x110";

	public String getUploadTemp() {
		return uploadTemp;
	}

	public void setUploadTemp(String uploadTemp) {
		this.uploadTemp = uploadTemp;
	}

	public String getShowTemp() {
		return showTemp;
	}

	public void setShowTemp(String showTemp) {
		this.showTemp = showTemp;
	}

	public String getPrivatePath() {
		return privatePath;
	}

	public void setPrivatePath(String privatePath) {
		this.privatePath = privatePath;
	}

	public String getPublicPath() {
		return publicPath;
	}

	public void setPublicPath(String publicPath) {
		this.publicPath = publicPath;
	}

	public String getUploadImgPath() {
		return uploadImgPath;
	}

	public void setUploadImgPath(String uploadImgPath) {
		this.uploadImgPath = uploadImgPath;
	}

	public String getShowImgPath() {
		return showImgPath;
	}

	public void setShowImgPath(String showImgPath) {
		this.showImgPath = showImgPath;
	}

	public String getUploadVideoPath() {
		return uploadVideoPath;
	}

	public void setUploadVideoPath(String uploadVideoPath) {
		this.uploadVideoPath = uploadVideoPath;
	}

	public String getShowVideoPath() {
		return showVideoPath;
	}

	public void setShowVideoPath(String showVideoPath) {
		this.showVideoPath = showVideoPath;
	}

	public String getUploadOtherPath() {
		return uploadOtherPath;
	}

	public void setUploadOtherPath(String uploadOtherPath) {
		this.uploadOtherPath = uploadOtherPath;
	}

	public String getShowOtherPath() {
		return showOtherPath;
	}

	public void setShowOtherPath(String showOtherPath) {
		this.showOtherPath = showOtherPath;
	}

	public String getDefaultVideoType() {
		return defaultVideoType;
	}

	public void setDefaultVideoType(String defaultVideoType) {
		this.defaultVideoType = defaultVideoType;
	}

	public String getDefaultImgType() {
		return defaultImgType;
	}

	public void setDefaultImgType(String defaultImgType) {
		this.defaultImgType = defaultImgType;
	}

	public String getAllowedImgTypes() {
		return allowedImgTypes;
	}

	public void setAllowedImgTypes(String allowedImgTypes) {
		this.allowedImgTypes = allowedImgTypes;
	}

	public String getAllowedVideoTypes() {
		return allowedVideoTypes;
	}

	public void setAllowedVideoTypes(String allowedVideoTypes) {
		this.allowedVideoTypes = allowedVideoTypes;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

}
