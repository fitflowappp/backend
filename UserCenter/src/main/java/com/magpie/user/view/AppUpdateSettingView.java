package com.magpie.user.view;

import com.magpie.yoga.model.AppUpdate;

public class AppUpdateSettingView extends AppUpdate {
	public static int NORMAL=0;
	public static int FORCE=1;
	private int type=NORMAL;//0是普通更新，1是强制更新
	private String downloadUrl;
	
	
	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
}
