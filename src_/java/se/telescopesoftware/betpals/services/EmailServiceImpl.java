package se.telescopesoftware.betpals.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.telescopesoftware.betpals.domain.UserProfile;

public class EmailServiceImpl implements EmailService {

	private UserService userService;
    private String smtphost = "localhost";
    private String encoding = "UTF-8";
    private String adminEmail;

    private Log logger = LogFactory.getLog(EmailServiceImpl.class);

    
    public void setUserService(UserService userService) {
    	this.userService = userService;
    }
    
    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public void setAdminEmail(String email) {
    	this.adminEmail = email;
    }

    
    public void sendEmail(String fromAddress, String toAddress, String subject, String text) throws AddressException, MessagingException {
        InternetAddress internetAddress = new InternetAddress(toAddress);

        if (internetAddress != null) {
        	Properties props = System.getProperties();
        	props.put("mail.smtp.host", smtphost);
        	Session msession = Session.getDefaultInstance(props, null);
	        MimeMessage msg = new MimeMessage(msession);
	        msg.addRecipient(
	                Message.RecipientType.TO,
	                internetAddress);
	
	        msg.setSubject(subject, encoding);
	        msg.setText(text, encoding);
	        msg.setFrom(new InternetAddress(fromAddress));
	        msg.setSentDate(new java.util.Date());
	        logger.info("Sending email to " + toAddress + " from " + fromAddress);
	        Transport.send(msg);
        }
    }

	public void sendEmail(Long fromUserId, Long toUserId, String subject, String text) throws AddressException, MessagingException {
		UserProfile fromUserProfile = userService.getUserProfileByUserId(fromUserId);
		UserProfile toUserProfile = userService.getUserProfileByUserId(toUserId);
		sendEmail(fromUserProfile.getEmail(), toUserProfile.getEmail(), subject, text);
	}

	public void sendEmail(Long toUserId, String subject, String text) throws AddressException, MessagingException {
		UserProfile toUserProfile = userService.getUserProfileByUserId(toUserId);
		sendEmail(adminEmail, toUserProfile.getEmail(), subject, text);
	}
	

}
