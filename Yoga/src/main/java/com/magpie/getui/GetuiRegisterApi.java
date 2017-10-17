package com.magpie.getui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.magpie.getui.getui.cache.GetuiMessage;
import com.magpie.getui.getui.cache.MsgClient;
import com.magpie.getui.getui.cache.MsgClientCacheService;
import com.magpie.getui.getui.service.GeTuiMsgSender;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/yoga/getui")
public class GetuiRegisterApi {

	@Autowired
	private MsgClientCacheService msgClientCacheService;

	@Autowired
	private GeTuiMsgSender geTuiMsgSender;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "个推客户端注册接口")
	public void register(@RequestBody MsgClient msgClient, @ActiveUser UserRef userRef) {
		msgClient.setUid(userRef.getId());
		msgClientCacheService.saveMsgClient(msgClient);
	}

	@RequestMapping(value = "/open/send/{uid}/{msg}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "测试接口")
	public void testMsg(@PathVariable String uid, @PathVariable String msg) {

		// 通知前端刷新支付页面
		GetuiMessage getuiMessage = new GetuiMessage(GetuiMessage.DISP_TYPE, msg);
		geTuiMsgSender.send(uid, getuiMessage, true);
	}

	@RequestMapping(value = "/open/sendall/{msg}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "测试接口")
	public void testAllMsg(@PathVariable String msg) {

		// 通知前端刷新支付页面
		GetuiMessage getuiMessage = new GetuiMessage(GetuiMessage.DISP_TYPE, msg);
		geTuiMsgSender.sendAll(getuiMessage, true);
	}

}
