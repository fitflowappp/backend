package com.magpie.yoga.api;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.magpie.getui.PushRecord;
import com.magpie.getui.PushRecordDao;
import com.magpie.getui.getui.cache.GetuiMessage;
import com.magpie.getui.getui.service.AsyncGeTuiMsgSender;
import com.magpie.user.dao.FacebookDao;
import com.magpie.user.model.FaceBookUser;
import com.magpie.yoga.dao.UserConfigurationDao;
import com.magpie.yoga.model.UserConfiguration;
import com.magpie.yoga.service.YogaStatService;

@Component
@EnableScheduling
public class TaskComponent {
	@Autowired
	private YogaStatService yogaStatService;

	@Autowired
	private UserConfigurationDao userConfigurationDao;

	@Autowired
	private AsyncGeTuiMsgSender asyncGeTuiMsgSender;
	@Autowired
	private FacebookDao facebookDao;
	@Autowired
	private PushRecordDao pushRecordDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

//	@Scheduled(cron = "0 30 0 * * *")
	public void generateDashboard() {
		yogaStatService.generatePrevDayDashboard();
	}

	// @Scheduled(cron = "0 0 0/4 * * *")
//	@Scheduled(cron = "0 0/30 * * * *")
	// Challenge,Workout,Routine统计数据
	public void generateCWRStatistics() {
		yogaStatService.updateCount();
	}

//	@Scheduled(cron = "0 0/1 * * * *")
	// 推送处理
	public void pushProcess() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date current = calendar.getTime();

		for (UserConfiguration userConfiguration : userConfigurationDao.findUserconfigurations(true)) {

			if (userConfiguration.getSchedulingDays() == null) {
				continue;
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(userConfiguration.getSchedulingTime());
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date scheduleTime = cal.getTime();

			FaceBookUser faceBookUser = facebookDao.findByUid(userConfiguration.getUid());
			GetuiMessage message = new GetuiMessage(GetuiMessage.DISP_TYPE,
					"Hey " + (faceBookUser == null ? "" : faceBookUser.getName())
							+ "! Time for yoga. You know you will feel great afterwards.");
			int index = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			if (index == 1) {// 周日
				index = 6;
			} else {
				index = index - 2;
			}
			if (index < 0) {
				logger.error("bug report index:{},{}", index, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
				continue;
			}
			int flg = userConfiguration.getSchedulingDays()[index];
			logger.info("flg:{},equal:{},currentTime:{},scheduleTime:{}", flg, current.compareTo(scheduleTime), current,
					scheduleTime);
			if (flg == 1 && current.compareTo(scheduleTime) == 0) {
				asyncGeTuiMsgSender.send(userConfiguration.getUid(), message, true, 100);

				PushRecord pushRecord = new PushRecord();
				pushRecord.setUid(userConfiguration.getUid());
				pushRecord.setContent(message.getContent());
				pushRecord.setType(message.getType());
				pushRecordDao.save(pushRecord);
			}

		}
	}

	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
	}

}
