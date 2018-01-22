package com.magpie.user.view;

import com.magpie.yoga.model.AppUpdate;

public class AppUpdateSettingView extends AppUpdate {
	private int type=0;//0是普通更新，1是强制更新

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
