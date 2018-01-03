package com.magpie.user.utils;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailUtils {
	public static boolean sendEmail(String toEmail, String fromEmail, String title, String body,
			String fromEmailPassWord) {
		return sendEmail(toEmail, fromEmail, title, title, body, fromEmailPassWord);
	}
	public static boolean sendEmail(String toEmail, String fromEmail, String title,String subject, String body,
			String fromEmailPassWord) {
		Properties props = new Properties(); // 参数配置
		props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", "smtp.exmail.qq.com"); // 发件人的邮箱的
		// SMTP
		// 服务器地址
		props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
		props.setProperty("mail.smtp.port", "465");
		
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sf);
		
		Session session = Session.getInstance(props);
		session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
		
		MimeMessage message = new MimeMessage(session);
		
		try {
			
			javax.mail.internet.MimeMultipart myMultipart = new javax.mail.internet.MimeMultipart();
			javax.mail.internet.MimeBodyPart myBodyPart = new javax.mail.internet.MimeBodyPart();
			
			myBodyPart.setContent(body, "text/html");
			myMultipart.addBodyPart(myBodyPart);
			message.setHeader("Content-Type", "multipart");
			message.setContent(myMultipart);
			
			message.setFrom(new InternetAddress(fromEmail, subject, "UTF-8"));
			
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toEmail, "USER_CC", "UTF-8"));
			
			message.setSubject(title, "UTF-8");
			
			
			message.setSentDate(new Date());
			
			message.saveChanges();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// 4. 根据 Session 获取邮件传输对象
		Transport transport = null;
		try {
			transport = session.getTransport();
			transport.connect(fromEmail, fromEmailPassWord);
			
			transport.sendMessage(message, message.getAllRecipients());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}

	public static boolean validEmail(String email) {
		if (email != null && email.length() > 0) {
			String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			// 字符串是否与正则表达式相匹配
			return matcher.matches();
		}
		return false;
	}
}
