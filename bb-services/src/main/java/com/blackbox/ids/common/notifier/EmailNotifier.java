package com.blackbox.ids.common.notifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * This class sends notification and test report to the user after test
 * completion.
 * 
 * @author Nagarro
 *
 */
@Service("emailNotifier")
public class EmailNotifier implements Notifier {

	// to send email and do authentication
	/** The email. */
	private HtmlEmail email = null;

	/**
	 * Constructor to initialize basic email information.
	 */

	private EmailAttachment getAttachmentDetail(String attachmentName, String attachmentDescription,
			String attachmentPath) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(attachmentPath);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setDescription(attachmentDescription);
		attachment.setName(attachmentName);

		return attachment;
	}

	public EmailNotifier() {

		// Will be fetched from property file
		/*
		 * email = new HtmlEmail(); email.setHostName("smtp.gmail.com");
		 * email.setSmtpPort(587); email.setSSLCheckServerIdentity(true);
		 * email.setDebug(true); email.setStartTLSRequired(true);
		 */

	}

	public void notification(List<String> emails) {

	}

	public String createEmailContent(String content) {
		return null;
	}

	public void notification(List<String> emails, String subjectMessage) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nagarro.email.poc.service.Notifier#notificationWithAttachment(java.
	 * util.List, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void notificationWithAttachment(List<String> emails, String subjectMessage, String emailContent,
			String attachmentName, String attachmentDescription, String attachmentPath) {
		try {
			// email = new HtmlEmail();
			email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setSSLCheckServerIdentity(true);
			email.setDebug(true);
			email.setStartTLSRequired(true);
			email.setFrom("servicetesttool@gmail.com");
			email.setAuthentication("servicetesttool@gmail.com", "!Password");
			email.setDebug(true);
			Collection<InternetAddress> recipients = new ArrayList<InternetAddress>(emails.size());

			// recipient list
			for (String email : emails) {
				recipients.add(new InternetAddress(email));
			}

			email.setTo(recipients);
			email.setSubject(subjectMessage);
			email.setMsg(emailContent);

			if (!StringUtils.isEmpty(attachmentName)) {
				email.attach(getAttachmentDetail(attachmentName, attachmentDescription, attachmentPath));
			}
			email.send();

		} catch (EmailException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		}

	}
}