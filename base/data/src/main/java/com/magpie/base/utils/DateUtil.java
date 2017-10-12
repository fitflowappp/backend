package com.magpie.base.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date from, Date end) {
		from = from == null ? Calendar.getInstance().getTime() : from;
		end = end == null ? Calendar.getInstance().getTime() : end;
		Calendar cal = Calendar.getInstance();
		cal.setTime(getStartTime(from));
		long time1 = cal.getTimeInMillis() / (3600 * 24 * 1000);
		cal.setTime(getStartTime(end));
		long time2 = cal.getTimeInMillis() / (3600 * 24 * 1000);

		return (int) (time2 - time1);
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
