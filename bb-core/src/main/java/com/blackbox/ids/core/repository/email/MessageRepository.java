package com.blackbox.ids.core.repository.email;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.repository.base.BaseRepository;

public interface MessageRepository extends JpaRepository<Message, Long>,
		BaseRepository<Message, Long> {

	  @Query("select message from Message message where message.templateType = :templateType and message.status = :status")
	  List<Message> findByTemplateTypeAndStatus(@Param("templateType") final TemplateType templateType, @Param("status") final String status);
	  
	  @Query("update Message message set message.status=:status where message.id = :id")
	  @Modifying
	  void markStatus(@Param("id") final Long id, @Param("status") final String status);
	  
	  @Query("update Message message set message.status=:status, message.version=:version + 1 where message.id = :id and message.version=:version")
	  @Modifying
	  int lockMessage(@Param("id") final Long id, @Param("version") final int version, @Param("status") final String status);

}
