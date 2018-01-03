package com.magpie.yoga.view;
/**
 * 个人用户统计workout状况，包括完成以及用时
* @ClassName: UserWorkOutStat  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author jiangming.xia  
* @date 2018年1月2日 下午3:53:03  
*
 */
public class UserWorkoutStat {
	private int workoutCount=0;
	private int seconds=0;
	public int getWorkoutCount() {
		return workoutCount;
	}
	public void setWorkoutCount(int workoutCount) {
		this.workoutCount = workoutCount;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	
}
