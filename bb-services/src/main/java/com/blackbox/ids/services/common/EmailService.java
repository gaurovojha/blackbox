package com.blackbox.ids.services.common;

import java.util.Collection;

import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;

public interface EmailService {
	
	public abstract void send(Message message, Collection<MessageData> messageData);

}
