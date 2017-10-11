package com.magpie.base.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public static Date getStartTime() {
		Calendar calendar = Calendar.getInstance();
		// 将时间设置为今天的0点 xx年xx月xx日 00:00:00
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date getPrevDayStartTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return getStartTime(calendar.getTime());
	}

	public static Date getStartTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将时间设置为今天的0点 xx年xx月xx日 00:00:00
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getEndTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
