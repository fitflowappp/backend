package com.magpie.yoga.view;

import com.magpie.yoga.model.Routine;

public class RoutineView extends Routine {

	private int status;
	private boolean avail;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isAvail() {
		return avail;
	}

	public void setAvail(boolean avail) {
		this.avail = avail;
	}

}
