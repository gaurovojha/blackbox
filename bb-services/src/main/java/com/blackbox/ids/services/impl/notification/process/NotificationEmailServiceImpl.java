package com.blackbox.ids.services.impl.notification.process;

import static com.blackbox.ids.core.constant.Constant.FILE_ATTACHMENT_NAME;
import static com.blackbox.ids.core.constant.Constant.NOTIFICATION_BODY;
import static com.blackbox.ids.core.constant.Constant.NOTIFICATION_SUBJECT;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.model.notification.Notification;
import com.blackbox.ids.core.repository.UserRepository;
import com.blackbox.ids.services.common.EmailService;
import com.blackbox.ids.services.notification.process.NotificationEmailService;

/**
 * The Class NotificationEmailServiceImpl.
 */
@Service
public class NotificationEmailServiceImpl implements NotificationEmailService {

	/** The user repo. */
	@Autowired
	private UserRepository userRepo;

	/** The email service. */
	@Autowired
	private EmailService emailService;

	/** The logger. */
	private static Logger LOGGER = Logger.getLogger(NotificationEmailServiceImpl.class);

	/** The app no. */
	// email subject parameters
	private final String APP_NO = "<application_number>";

	/** The user. */
	private final String USER = "<user_name>";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blackbox.ids.services.notification.process.NotificationEmailService#
	 * sendEmailOnNotification(com.blackbox.ids.core.model.notification.
	 * Notification, java.util.List, java.util.List, java.lang.String)
	 */
	@Override
	public void sendEmailOnNotification(final Notification notification, final List<Long> userIds,
			final List<String> filePaths, final String applicationNumber) {
		List<String> recievers = getReceiverEmailIds(userIds);
		String userName = userRepo.getFirstNameByLoginId(BlackboxSecurityContextHolder.getUserDetails().getUsername());
		String subject = notification.getSubject();
		String body = notification.getMessage();

		if (subject.indexOf(APP_NO) != -1 && applicationNumber != null) {
			subject = subject.replace(APP_NO, applicationNumber);
		}

		if (subject.indexOf(USER) != -1) {
			subject = subject.replace(USER, userName);
		}

		createAndSaveMessage(TemplateType.NOTIFICATION_EMAIL, recievers, createMessageData(subject, body, filePaths));
	}

	/**
	 * Gets the receiver email ids.
	 *
	 * @param userIds
	 *            the user ids
	 * @return the receiver email ids
	 */
	private List<String> getReceiverEmailIds(final List<Long> userIds) {
		List<String> recievers = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(userIds)) {
			for (Long userId : userIds) {
				String recieverName = userRepo.getEmailId(userId);
				recievers.add(recieverName);
			}
		}

		return recievers;
	}

	/**
	 * Creates the message data.
	 *
	 * @param subject
	 *            the subject
	 * @param body
	 *            the body
	 * @param filePaths
	 *            the file paths
	 * @return the list
	 */
	private List<MessageData> createMessageData(final String subject, final String body, final List<String> filePaths) {
		List<MessageData> messageDatas = new ArrayList<>();

		messageDatas.add(new MessageData(NOTIFICATION_SUBJECT, subject));
		messageDatas.add(new MessageData(NOTIFICATION_BODY, body));

		if (CollectionUtils.isNotEmpty(filePaths)) {
			for (String each : filePaths) {
				messageDatas.add(new MessageData(FILE_ATTACHMENT_NAME, each));
			}
		}

		return messageDatas;
	}

	/**
	 * Creates the and save message.
	 *
	 * @param templateType
	 *            the template type
	 * @param recipients
	 *            the recipients
	 * @param messageDatas
	 *            the message datas
	 */
	private void createAndSaveMessage(final TemplateType templateType, List<String> recipients,
			List<MessageData> messageDatas) {
		if (templateType != null && CollectionUtils.isNotEmpty(recipients)) {
			Message message = new Message();
			StringBuilder strBuilder = new StringBuilder();

			for (String recipient : recipients) {
				strBuilder.append(recipient).append(Constant.SEMI_COLON);
			}

			message.setTemplateType(templateType);
			message.setReceiverList(strBuilder.toString());
			message.setStatus(Constant.MESSAGE_STATUS_NOT_SENT);

			emailService.send(message, messageDatas);

			LOGGER.info("Notification email successfully saved into queue");
		}
	}
}
