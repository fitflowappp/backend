package com.magpie.getui.getui;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;

/**
 * IPushResult pushMessageToSingle(SingleMessage message, Target target)
 * 
 * @author Kevin
 * 
 */
public class PushAPNS {

	static String appId = "a46eJKi0Wj5eTax1Tlfk03";
	static String appKey = "4V5z8ggEbp9nvXpxtG2xJA";
	static String master = "jGZSJpw4OWAsWU5poc3UZA";
	static String CID = "d9ca5f1cfcec433299aa9ab817d2369f";

	static String masterSecret = "jGZSJpw4OWAsWU5poc3UZA";

	static String dt = "b315513d3e7ea197760f1d33234150233702bb5a77166f90f70c40b811e71d27";
	static String url = "http://sdk.open.api.igexin.com/apiex.htm";

	public static void main(String[] args) {
		apnpush();
	}

	public static void apnpush() {
		IGtPush p = new IGtPush(url, appKey, masterSecret);
		APNTemplate template = new APNTemplate();
		template.setPushInfo("", 2, "", "");

		SingleMessage SingleMessage = new SingleMessage();
		SingleMessage.setData(template);
		IPushResult ret = p.pushAPNMessageToSingle(appId, dt, SingleMessage);
		System.out.println(ret.getResponse());

		// ListMessage lm = new ListMessage();
		// lm.setData(template);
		// String contentId = p.getAPNContentId(appId, lm);
		// List<String> dtl = new ArrayList<String>();
		// dtl.add(dt);
		// System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		// IPushResult ret = p.pushAPNMessageToList(appId, contentId, dtl);
		// System.out.println(ret.getResponse());
	}
}
