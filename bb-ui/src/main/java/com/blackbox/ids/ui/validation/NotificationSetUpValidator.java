package com.blackbox.ids.ui.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.blackbox.ids.common.constant.UIConstants;
import com.blackbox.ids.dto.notification.NotificationDTO;

/**
 * Validator Class for Notification {@link Validator} 
 * 
 * @author Nagarro
 *
 */

@Component("notificationSetUpValidator")
public class NotificationSetUpValidator implements Validator {

	private Logger logger = Logger.getLogger(NotificationSetUpValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return NotificationDTO.class.equals(clazz);
	}

	
	/**
	 *  Validates Notification Metadata
	 */
	
	@Override
	public void validate(Object target, Errors errors) {

		try {
			NotificationDTO notificationSetUp = (NotificationDTO) target;

			if (StringUtils.isBlank(notificationSetUp.getNotificationName())) {
				errors.rejectValue(UIConstants.NOTIFICATION_NAME, "notification.setup.notificationName");
			}
			if (StringUtils.isBlank(notificationSetUp.getNotificationMessage())) {
				errors.rejectValue(UIConstants.NOTIFICATION_MESSAGE, "notification.setup.notificationMessage");
			}
			if (notificationSetUp.getReminder() != null && notificationSetUp.getFrequencyOfSendingRemindes() == 0) {
				errors.rejectValue(UIConstants.NOTIFICATION_REMINDERS, "notification.setup.reminder");
			}
			if (notificationSetUp.getEscalationSelectedRoles().length() != 0
					&& notificationSetUp.getNoOfPastDueDays() == 0) {
				errors.rejectValue(UIConstants.NOTIFICATION_DUE_DAYS, "notification.setup.pastDuedays");
			}
			if ((notificationSetUp.getEscalation() != null) && notificationSetUp.getEscalation().equalsIgnoreCase("on")
					&& notificationSetUp.getEscalationSelectedRoles().length() == 0) {
				errors.rejectValue(UIConstants.NOTIFICATION_ESCALATION_ROLES, "notification.setup.esacalationRole");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
