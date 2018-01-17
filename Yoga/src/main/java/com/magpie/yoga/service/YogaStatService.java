package com.magpie.yoga.service;

import java.util.Date;
import java.util.List;

import com.magpie.yoga.model.Dashboard;

public interface YogaStatService {
	/**
	 * get dashboard list
	 * 
	 * @return
	 */
	public List<Dashboard> getDashboard() ;

	/**
	 * 每天更新Challenge,workout,routine统计数据
	 */
	public void updateCount();

	/**
	 * Generate all Dashboard data
	 * 
	 * @return
	 */
	public Dashboard generateDashboard() ;

	/**
	 * Generate Dashboard data of yesterday
	 * 
	 * @return
	 */
	public Dashboard generatePrevDayDashboard() ;

	/**
	 * Generate Dashboard data of today
	 * 
	 * @return
	 */
	public Dashboard generateTodayDashboard() ;

	/**
	 * Generate Dashboard data within the period
	 * 
	 * @return
	 */
	public Dashboard generateDashboard(Date date) ;
}
