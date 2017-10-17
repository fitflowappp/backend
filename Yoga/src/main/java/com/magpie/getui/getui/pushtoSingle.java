package com.magpie.getui.getui;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.PopupTransmissionTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class pushtoSingle {
	static String appId = "a46eJKi0Wj5eTax1Tlfk03";
	static String appkey = "4V5z8ggEbp9nvXpxtG2xJA";
	static String master = "jGZSJpw4OWAsWU5poc3UZA";
	static String CID = "d9ca5f1cfcec433299aa9ab817d2369f";
	static String Alias = "请输入别名"; // 根据别名推送，需要先绑定别名，请参考alias_bind_unbind.java

	static String host = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void main(String[] args) throws Exception {
		IGtPush push = new IGtPush(host, appkey, master);
		push.connect();

		TransmissionTemplate template = TransmissionTemplateDemo();
		// LinkTemplate template = linkTemplateDemo();
		// NotificationTemplate template = NotificationTemplateDemo();
		// NotyPopLoadTemplate template =NotyPopLoadTemplateDemo();

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		message.setOfflineExpireTime(1 * 1000 * 3600);
		message.setData(template);
		// message.setPushNetWorkType(1); //根据WIFI推送设置

		List<Target> targets = new ArrayList<Target>();
		Target target1 = new Target();
		Target target2 = new Target();
		target1.setAppId(appId);
		target1.setClientId(CID);
		target2.setAppId(appId);
		target2.setClientId("3b7f0ecd6d78f2fb7584300d70124611");
		// target1.setAlias(Alias); //根据别名推送设置，CID与Alias可二选一进行推送

		IPushResult ret = push.pushMessageToSingle(message, target2);
		System.out.println(ret.getResponse().toString());
	}

	public static PopupTransmissionTemplate PopupTransmissionTemplateDemo() {
		PopupTransmissionTemplate template = new PopupTransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setText("xxx");
		template.setTitle("popup");
		template.setImg("");
		template.setConfirmButtonText("ok");
		template.setCancelButtonText("cancel");
		template.setTransmissionContent("111dddddd");
		template.setTransmissionType(1);

		return template;
	}

	public static TransmissionTemplate TransmissionTemplateDemo() throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTransmissionType(2);
		template.setTransmissionContent("OS-TOSingle");
		// template.setPushInfo("dd", 1, "ddd", "com.gexin.ios.silence", "", "",
		// "", "");
		// template.setPushInfo("", 1, "通知栏内容", "default", "透传消息", "", "", "");

		return template;
	}

	public static LinkTemplate linkTemplateDemo() throws Exception {
		LinkTemplate template = new LinkTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTitle("");
		template.setText("");
		template.setLogo("text.png");
		// template.setLogoUrl("");
		// template.setIsRing(true);
		// template.setIsVibrate(true);
		// template.setIsClearable(true);
		template.setUrl("http://www.baidu.com");
		// template.setPushInfo("actionLocKey", 1, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}

	public static NotificationTemplate NotificationTemplateDemo() throws Exception {
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		template.setTitle("内容");
		template.setText("标题");
		template.setLogo("icon.png");
		// template.setLogoUrl("");
		// template.setIsRing(true);
		// template.setIsVibrate(true);
		// template.setIsClearable(true);
		template.setTransmissionType(1);
		template.setTransmissionContent("dddd");
		// template.setPushInfo("actionLocKey", 2, "message", "sound",
		// "payload", "locKey", "locArgs", "launchImage");
		return template;
	}

	public static NotyPopLoadTemplate NotyPopLoadTemplateDemo() {
		NotyPopLoadTemplate template = new NotyPopLoadTemplate();
		// 填写appid与appkey
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 填写通知标题和内容
		template.setNotyTitle("标题");
		template.setNotyContent("内容");
		// template.setLogoUrl("");
		// 填写图标文件名称
		template.setNotyIcon("text.png");
		// 设置响铃，震动，与可清除
		// template.setBelled(false);
		// template.setVibrationed(false);
		// template.setCleared(true);

		// 设置弹框标题与内容
		template.setPopTitle("弹框标题");
		template.setPopContent("弹框内容");
		// 设置弹框图片
		template.setPopImage("http://www-igexin.qiniudn.com/wp-content/uploads/2013/08/logo_getui1.png");
		template.setPopButton1("打开");
		template.setPopButton2("取消");

		// 设置下载标题，图片与下载地址
		template.setLoadTitle("下载标题");
		template.setLoadIcon("file://icon.png");
		template.setLoadUrl("http://gdown.baidu.com/data/wisegame/c95836e06c224f51/weixinxinqing_5.apk");
		template.setActived(true);
		template.setAutoInstall(true);
		template.setAndroidMark("");
		return template;
	}
}