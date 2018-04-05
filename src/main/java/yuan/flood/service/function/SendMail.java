package yuan.flood.service.function;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.Properties;

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
	     props.put("mail.password", Password);//�°��126����ͻ�����Ȩ���룬���ǵ�¼���룬��Ҫ���������п�ͨsmtp�ȷ���
	     Authenticator authenticator = new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
					// �û���������
	                String userName = props.getProperty("mail.user");
	                String password = props.getProperty("mail.password");
	                return new PasswordAuthentication(userName, password);
	            }
	        };
		// ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
	        Session mailSession = Session.getInstance(props, authenticator);
		// �����ʼ���Ϣ
	        MimeMessage message = new MimeMessage(mailSession);
		// ���÷�����
	        InternetAddress from = new InternetAddress(
	                props.getProperty("mail.user"));
	        message.setFrom(from);

		// �����ռ���
	        InternetAddress to = new InternetAddress(acceptaddress);
	        message.setRecipient(RecipientType.TO, to);

		// �����ʼ�����
	        message.setSubject(subject);

		// �����ʼ���������
	        message.setText(sendcontent,"UTF-8");

		// �����ʼ�
	        Transport.send(message);
	}

}
