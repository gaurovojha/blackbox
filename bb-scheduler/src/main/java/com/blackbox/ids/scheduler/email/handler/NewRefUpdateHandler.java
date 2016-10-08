package com.blackbox.ids.scheduler.email.handler;

import java.util.Map;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;

public class NewRefUpdateHandler extends AbstractMessageHandler {
	
	public NewRefUpdateHandler(final TemplateType templateType)
	{
		super(templateType);
	}

	@Override
	protected void doHandleInternal(Message message, MimeMessageHelper messageHelper, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}


}
