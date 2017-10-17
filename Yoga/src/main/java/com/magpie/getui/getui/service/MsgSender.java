package com.magpie.getui.getui.service;

import com.magpie.getui.getui.cache.GetuiMessage;

public interface MsgSender {

	public boolean send(String uid, GetuiMessage msg, boolean sendOffline);

	public boolean sendAll(GetuiMessage msg, boolean sendOffline);

}
