package com.blackbox.ids.core.model.email;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.base.BaseEntity;

@Entity
@Table(name = "BB_MESSAGE")
public class Message extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7265118508557031687L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_MESSAGE_ID", unique = true, nullable = false, length = 50)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "TEMPLATE_TYPE")
	private TemplateType templateType;

	@Column(name = "RECEIVER_LIST")
	private String receiverList;

	@Column(name = "STATUS")
	private String status;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "BB_MESSAGE_ID", referencedColumnName = "BB_MESSAGE_ID")
	private Set<MessageData> messageData;

	public String getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(String receiverList) {
		this.receiverList = receiverList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public Set<MessageData> getMessageData() {
		return messageData;
	}

	public void setMessageData(Set<MessageData> messageData) {
		this.messageData = messageData;
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}
}
