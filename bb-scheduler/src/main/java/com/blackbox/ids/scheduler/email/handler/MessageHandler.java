package com.blackbox.ids.scheduler.email.handler;

import java.util.Collection;

import com.blackbox.ids.core.model.email.Message;

/**
 * The Interface MessageHandler.
 * @author Nagarro
 */
public interface MessageHandler {

	/**
	 * This API is used to fetch messages from Message Table.
	 * @return the collection
	 */
	Collection<Message> queryMessages();

	/**
	 * This API is used to handle the email processing.
	 * @param message
	 *            the message
	 * @throws Exception
	 *             the exception
	 */
	void handle(Message message) throws Exception;

	/**
	 * This API is used to mark the message status "ERROR" if any unspecified scenario occurs.
	 * @param message
	 *            the message
	 */
	void markAsError(Message message);

	/**
	 * This API is used to mark the message status "SENT" if message sent successfully.
	 * @param message
	 *            the message
	 */
	void markAsComplete(Message message);

	/**
	 * This API is used to lock the message.
	 * @param mesage
	 *            the mesage
	 * @return the int
	 */
	int lock(Message mesage);

}
