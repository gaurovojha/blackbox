package com.blackbox.ids.services.impl.common;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.repository.email.MessageDataRepository;
import com.blackbox.ids.core.repository.email.MessageRepository;
import com.blackbox.ids.services.common.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	protected MessageRepository messageRepository;

	@Autowired
	protected MessageDataRepository messageDataRepository;

	@Override
	@Transactional
	public void send(Message message, Collection<MessageData> messageData) {
		// to save message and message data
		message = messageRepository.save(message);
		if (!CollectionUtils.isEmpty(messageData)) {
			for (MessageData msgData : messageData) {
				msgData.setMessageId(message.getId());
				messageDataRepository.save(msgData);
			}
		}
	}

}
