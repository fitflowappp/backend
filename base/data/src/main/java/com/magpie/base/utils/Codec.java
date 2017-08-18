package com.magpie.base.utils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class Codec {

	public static final String key = "mingshidian+JJJJ+2014";
	private static final String CONNECT_KEY = "appgszx2014";

	public static String generateSessionId(String name, String password) {
		return DigestUtils.sha1Hex(name + System.currentTimeMillis() + password + key);
	}

	public static String generateToken(String val) {
		return DigestUtils.sha1Hex(val + key);
	}

	public static String md5Digester(String val) {
		return DigestUtils.md5Hex(val);
	}

	public static String sha1Digester(String val) {
		return DigestUtils.sha1Hex(val);
	}

	public static String wechatSHA1(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(str.getBytes());
		byte[] digest = md.digest();

		StringBuffer hexstr = new StringBuffer();
		String shaHex = "";
		for (int i = 0; i < digest.length; i++) {
			shaHex = Integer.toHexString(digest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		return hexstr.toString();
	}

	public static String generateConnectToken(String timestamp) {
		return DigestUtils.md5Hex(timestamp + CONNECT_KEY);
	}

	public static void main(String[] args) {
		System.out
				.println(sha1Digester("jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VNOiNGmUej6VpoYeA2yJTgRsZth1_PRKcaXesj0xknAZIEpCilFFnxCi92GJpV_d-A&noncestr=-4186183367373202285&timestamp=1425195526506&url=http://www.huaya.me/register.html"));
	}

	public static String simpleXOR(String input, String key) {
		char[] keyArr = key.toCharArray();
		char[] inputCharArr = input.toCharArray();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		for (int i = 0; i < inputCharArr.length; i++) {
			bytes.write(inputCharArr[i] ^ keyArr[i % keyArr.length]);
		}
		return new String(Base64.encodeBase64(bytes.toByteArray()));
	}
}
