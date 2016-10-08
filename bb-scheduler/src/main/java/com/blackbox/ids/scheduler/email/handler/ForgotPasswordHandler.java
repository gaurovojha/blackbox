package com.blackbox.ids.scheduler.email.handler;

import java.util.Map;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;

public class ForgotPasswordHandler extends AbstractMessageHandler {

	public ForgotPasswordHandler(final TemplateType templateType)
	{
		super(templateType);
	}

	@Override
	protected void doHandleInternal(final Message message, final MimeMessageHelper messageHelper,
			final Map<String, String> map) {
		// No need to do anything
	}

}
