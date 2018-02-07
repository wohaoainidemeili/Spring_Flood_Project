package yuan.flood.phase.function;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.java.swing.plaf.windows.resources.windows_zh_TW;

public class SendMail {
	
	public static void send(String fromaddtress,String Password,String acceptaddress, String subject, String sendcontent) throws MessagingException
	{
		final Properties props = new Properties();
		 props.put("mail.smtp.auth", "true");
		 if(fromaddtress.equals("wenying3413ying@126.com"))
		 {
			  props.put("mail.smtp.host", "smtp.126.com");
		 }
		 else
	     props.put("mail.smtp.host", "smtp.qq.com");
	     props.put("mail.user", fromaddtress);
	     props.put("mail.password", Password);//新版的126邮箱客户端授权密码，不是登录密码，需要先在邮箱中开通smtp等服务
	     Authenticator authenticator = new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
	                String userName = props.getProperty("mail.user");
	                String password = props.getProperty("mail.password");
	                return new PasswordAuthentication(userName, password);
	            }
	        };
		// 使用环境属性和授权信息，创建邮件会话
	        Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
	        MimeMessage message = new MimeMessage(mailSession);
		// 设置发件人
	        InternetAddress from = new InternetAddress(
	                props.getProperty("mail.user"));
	        message.setFrom(from);

		// 设置收件人
	        InternetAddress to = new InternetAddress(acceptaddress);
	        message.setRecipient(RecipientType.TO, to);

		// 设置邮件标题
	        message.setSubject(subject);

		// 设置邮件的内容体
	        message.setText(sendcontent,"UTF-8");

		// 发送邮件
	        Transport.send(message);
	}

}
