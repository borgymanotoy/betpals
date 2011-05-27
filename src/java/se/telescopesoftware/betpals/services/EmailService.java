package se.telescopesoftware.betpals.services;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface EmailService {

	void sendEmail(String fromAddress, String fromName, String toAddress, String subject, String text) throws AddressException, MessagingException;

	void sendEmail(Long fromUserId, Long toUserId, String subject, String text) throws AddressException, MessagingException;

	void sendEmail(Long toUserId, String subject, String text) throws AddressException, MessagingException;
}
