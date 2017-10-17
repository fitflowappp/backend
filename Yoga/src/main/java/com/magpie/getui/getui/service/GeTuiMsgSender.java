package com.magpie.getui.getui.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.magpie.getui.getui.cache.GetuiMessage;
import com.magpie.getui.getui.cache.MsgClient;
import com.magpie.getui.getui.cache.MsgClientCacheService;

@Service
public class GeTuiMsgSender implements MsgSender {

	@Autowired
	private MsgClientCacheService msgClientCacheService;

	private boolean closed = false;

	@Override
	public boolean sendAll(GetuiMessage msg, boolean sendOffline) {
		if (closed) {
			return true;
		}
		sendToApp(makeAndroidTransmissionTemplate(msg), "ANDROID", sendOffline);
		sendToApp(makeIosTransmissionTemplate(msg, msg.getContent()), "IOS", sendOffline);
		return true;
	}

	private void sendToApp(AbstractTemplate msgTemplate, String phoneType, boolean sendOffline) {
		IGtPush push = new IGtPush(GeTuiConfig.host, GeTuiConfig.appkey, GeTuiConfig.master);

		AppMessage message = new AppMessage();
		message.setData(msgTemplate);

		message.setOffline(sendOffline);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		// 推送给App的目标用户需要满足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(GeTuiConfig.appId);
		message.setAppIdList(appIdList);
		// 手机类型
		List<String> phoneTypeList = new ArrayList<String>();
		phoneTypeList.add(phoneType);
		// // 省份
		// List<String> provinceList = new ArrayList<String>();
		// // 自定义tag
		// List<String> tagList = new ArrayList<String>();

		cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
		// cdt.addCondition(AppConditions.REGION, provinceList);
		// cdt.addCondition(AppConditions.TAG, tagList);
		message.setConditions(cdt);

		IPushResult ret = push.pushMessageToApp(message, "任务别名_toApp");
		System.out.println(ret.getResponse().toString());

	}

	@Override
	public boolean send(String uid, GetuiMessage msg, boolean sendOffline) {
		if (closed) {
			return true;
		}
		MsgClient msgClient = msgClientCacheService.getByUid(uid);
		if (msgClient == null || StringUtils.isEmpty(msgClient.getCid())) {
			// 未注册到推送平台用户
			return false;
		} else {
			if (ClientCat.ANDROID.getName().equals(msgClient.getType())) {
				return sendToCid(makeAndroidTransmissionTemplate(msg), msgClient.getCid(), sendOffline);
			} else if (ClientCat.IPHONE.getName().equals(msgClient.getType())) {

				return sendToCid(makeIosTransmissionTemplate(msg, msg.getContent()), msgClient.getCid(), sendOffline);
			} else {
				// Unknown client
				return false;
			}
		}
	}

	private boolean sendToCid(AbstractTemplate msgTemplate, String cid, boolean sendOffline) {

		IGtPush push = new IGtPush(GeTuiConfig.host, GeTuiConfig.appkey, GeTuiConfig.master);
		SingleMessage message = new SingleMessage();
		message.setOffline(sendOffline);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(msgTemplate);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);
		Target target = new Target();
		target.setAppId(GeTuiConfig.appId);
		target.setClientId(cid);
		// target.setAlias(Alias);
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
		} else {
			System.out.println("服务器响应异常");
		}
		return false;
	}

	public TransmissionTemplate makeAndroidTransmissionTemplate(GetuiMessage msg) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(GeTuiConfig.appId);
		template.setAppkey(GeTuiConfig.appkey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(2);
		template.setTransmissionContent(JSON.toJSONString(msg));
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		return template;
	}

	private TransmissionTemplate makeIosTransmissionTemplate(GetuiMessage msg, String alertMsg) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(GeTuiConfig.appId);
		template.setAppkey(GeTuiConfig.appkey);
		template.setTransmissionContent(JSON.toJSONString(msg));
		template.setTransmissionType(2);
		APNPayload payload = new APNPayload();
		payload.setBadge(1);
		payload.setContentAvailable(1);
		payload.addCustomMsg("custom", JSON.toJSONString(msg));
		payload.setSound("default");
		payload.setCategory("$由客户端定义");
		// 简单模式APNPayload.SimpleMsg
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(alertMsg));
		// 字典模式使用下者
		// payload.setAlertMsg(getDictionaryAlertMsg());
		template.setAPNInfo(payload);
		return template;
	}

	private APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody("body");
		alertMsg.setActionLocKey("ActionLockey");
		alertMsg.setLocKey("LocKey");
		alertMsg.addLocArg("loc-args");
		alertMsg.setLaunchImage("launch-image");
		// IOS8.2以上版本支持
		alertMsg.setTitle("Title");
		alertMsg.setTitleLocKey("TitleLocKey");
		alertMsg.addTitleLocArg("TitleLocArg");
		return alertMsg;
	}

	public static void main(String[] args) {
		GeTuiMsgSender sender = new GeTuiMsgSender();
		// sender.sendToClient("48a5dc638fc93cf61b634d76f1484251", "hello");
		// bafcf298206b5a55523de13e9d06cedc
		// sender.sendToClient("bafcf298206b5a55523de13e9d06cedc", "hello");

		// System.out.print(unicode2String("\ue415"));
		GetuiMessage msg = new GetuiMessage(GetuiMessage.DISP_TYPE, JSON.toJSONString("text test")); // /
		// sender.sendToCid(sender.makeAndroidTransmissionTemplate(msg),
		// "2c6c42b8ba4299562c175b8d4ed54ad8");
		sender.sendToCid(
				sender.makeIosTransmissionTemplate(msg,
						"ceshi中文花：\uD83D\uDC90礼物：\uD83C\uDF81\uD83C\uDF3C\ue415\uD83C\uDF84"),
				"7405a6897186ace6ea2b0779256ed6bb", true);

	}
}
