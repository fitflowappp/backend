package com.magpie.getui.getui.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.magpie.getui.getui.cache.GetuiMessage;

@Component
public class AsyncGeTuiMsgSender {

	private static ExecutorService normalExecutorService = Executors.newFixedThreadPool(15);

	@Autowired
	private MsgSender geTuiMsgSender;

	/**
	 * 异步发送指定用户//延时500毫秒发送
	 * 
	 * @param uid
	 * @param msg
	 * @return
	 */
	public boolean send(String uid, GetuiMessage msg, boolean sendOffline) {
		if (StringUtils.isEmpty(uid) || msg == null) {
			return false;
		}
		// 延时500毫秒发送
		normalExecutorService.execute(new MyThread(uid, msg, 1500, sendOffline));
		return true;
	}

	/**
	 * 异步发送全部用户//延时500毫秒发送
	 * 
	 * @param msg
	 * @return
	 */
	public boolean sendAll(GetuiMessage msg, boolean sendOffline) {
		if (msg == null) {
			return false;
		}
		// 延时500毫秒发送
		normalExecutorService.execute(new MyThread(null, msg, 1500, sendOffline));
		return true;
	}

	class MyThread extends Thread {

		private int delayMilli;
		private String uid;
		private GetuiMessage getuiMessage;
		private boolean sendOffline;

		public MyThread(String uid, GetuiMessage getuiMessage, int delayMilli, boolean sendOffline) {
			this.delayMilli = delayMilli;
			this.uid = uid;
			this.getuiMessage = getuiMessage;
			this.sendOffline = sendOffline;
		}

		public void run() {

			try {
				Thread.sleep(delayMilli);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (getuiMessage != null) {
				if (StringUtils.isEmpty(uid)) {
					AsyncGeTuiMsgSender.this.geTuiMsgSender.sendAll(getuiMessage, sendOffline);
				} else {
					AsyncGeTuiMsgSender.this.geTuiMsgSender.send(uid, getuiMessage, sendOffline);
				}
			}

		}
	}

}
