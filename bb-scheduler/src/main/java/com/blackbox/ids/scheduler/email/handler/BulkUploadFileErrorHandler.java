package com.blackbox.ids.scheduler.email.handler;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;

public class BulkUploadFileErrorHandler extends AbstractMessageHandler {

	public BulkUploadFileErrorHandler(TemplateType templateType) {
		super(templateType);
	}

	@Override
	protected void doHandleInternal(Message message, MimeMessageHelper messageHelper, Map<String, String> map)
			throws ApplicationException {
		StringBuffer stringBuffer =  null;
		Set<MessageData> messageDatas = message.getMessageData();
		if(!CollectionUtils.isEmpty(messageDatas)) {
			stringBuffer = new StringBuffer();
			for(MessageData messageData : messageDatas) {
				if(!stringBuffer.toString().equals("")) {
					stringBuffer.append(", ");
				}
				stringBuffer.append(messageData.getValue());
			}
			map.put("invalidFiles", stringBuffer.toString());
		}
	}

}
