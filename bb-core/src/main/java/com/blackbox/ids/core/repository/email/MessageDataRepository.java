package com.blackbox.ids.core.repository.email;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.repository.base.BaseRepository;

public interface MessageDataRepository extends JpaRepository<MessageData, Long>,
BaseRepository<MessageData, Long> {
	
	List<MessageData> findByMessageId(final Long messageId);

}
