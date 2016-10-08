package com.blackbox.ids.scheduler.email.handler;

import static com.blackbox.ids.core.constant.Constant.FILE_ATTACHMENT_NAME;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;

public class NotificationEmailHandler extends AbstractMessageHandler {

	public NotificationEmailHandler(TemplateType templateType) {
		super(templateType);
	}

	@Override
	protected void doHandleInternal(Message message, MimeMessageHelper messageHelper, Map<String, String> map)
			throws ApplicationException {
		List<String> filePaths = new ArrayList<>();
		Set<MessageData> messageDatas = message.getMessageData();
		
		if (!CollectionUtils.isEmpty(messageDatas)) {
			for (MessageData messageData : messageDatas) {
				if (messageData.getKey().equals(FILE_ATTACHMENT_NAME)) {
					String fileName = messageData.getValue();
					filePaths.add(fileName);
				}
			}
		}
		
		if (!filePaths.isEmpty()) {
			for (String each : filePaths) {
				File file = new File(each);
				try {
					messageHelper.addAttachment(file.getName(), file);
				} catch (MessagingException e) {
					throw new ApplicationException(e);
				}
			}
		}
	}
}
