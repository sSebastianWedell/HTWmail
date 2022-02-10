package mailsenden;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	
	protected Session mailSession;
	
	public void login(String smtpHost, String smtpPort, String username, String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.socketFactory.port", smtpPort);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", smtpPort);
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		
		this.mailSession = Session.getDefaultInstance(props, auth);
		System.out.println("Eingeloggt.");
		
	}
	
	public void send(String senderMail, String senderName, String receiverAddresses, String subject, String message) 
		throws MessagingException, IllegalStateException, UnsupportedEncodingException {
	if (mailSession == null) {
		throw new IllegalStateException("Du musst dich zuerst einloggen (login()-Methode)");
	}
	
	MimeMessage msg = new MimeMessage(mailSession);
	msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	msg.addHeader("format", "flowed");
	msg.addHeader("Content-Transfer-Encoding", "8bit");

	msg.setFrom(new InternetAddress(senderMail, senderName));
	msg.setReplyTo(InternetAddress.parse(senderMail, false));
	msg.setSubject(subject, "UTF-8");
	msg.setText(message, "UTF-8");
	msg.setSentDate(new Date());

	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAddresses, false));

	System.out.println("Versende E-Mail...");
	Transport.send(msg);
	System.out.println("E-Mail versendet.");

	}
}