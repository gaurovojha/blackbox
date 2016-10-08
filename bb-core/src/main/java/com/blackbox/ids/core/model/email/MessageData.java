package com.blackbox.ids.core.model.email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.BaseEntity;

@Entity
@Table(name = "BB_MESSAGE_DATA")
public class MessageData extends BaseEntity {
	
	

	public MessageData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MessageData(String key, String value) {
		this(key, value, null);
	}

	public MessageData(String key, String value, Long messageId) {
		super();
		this.key = key;
		this.value = value;
		this.messageId = messageId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815043436927960333L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_MESSAGE_DATA_ID", unique = true, nullable = false, length = 50)
	private Long id;
	
	@Column(name = "DATA_KEY")
	private String key;
	
	@Column(name = "DATA_VALUE")
	private String value;
	
	@Column(name = "BB_MESSAGE_ID")
	private Long messageId;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getId() {
		return id;
	} 
	
}
