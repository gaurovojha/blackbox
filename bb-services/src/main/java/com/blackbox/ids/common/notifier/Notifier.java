/*
 * @Copyright Nagarro Software Pvt. Ltd
 * 
 */
package com.blackbox.ids.common.notifier;

import java.util.List;

/**
 * This is generalization for sending email with or without attachment.
 * 
 * @author Nagarro
 *
 */
public interface Notifier {

	/**
	 * Sends notification without attachment.
	 *
	 * @param emails
	 *            the emails
	 */
	public void notification(List<String> emails, String subjectMessage);

	/**
	 * Sends notification with attachment.
	 *
	 * @param emails
	 *            the emails
	 * @param attachment
	 *            the attachment
	 * @param subject
	 *            the subject
	 * @param emailContent
	 *            the email content
	 */
	public void notificationWithAttachment(List<String> emails, String subjectMessage, String emailContent,
			String attachmentName, String attachmentDescription, String attachmentPath);

	/**
	 * Generate email content.
	 *
	 * @param reportList
	 *            the report list
	 * @param content
	 *            the content
	 * @return the string
	 */
	public String createEmailContent(String content);
}
