package com.blackbox.ids.core.model.IDS;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.AuditableEntity;

@Entity
@Table(name = "BB_IDS_TRIGGER")
public class IDSTrigger extends AuditableEntity{
	
	private static final long serialVersionUID = 168389417470190000L;
	
	public enum Status {
		ACTIVE,
		INACTIVE;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_TRIGGER_ID")
	private Long id;
	
	@Column(name = "BB_TRIGGER_NAME")
	@Enumerated(EnumType.STRING)
	private TriggerType triggerTypeName;
	
	@Column(name = "BB_STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "BB_DAYS_PARAMETER")
	private int days;
	
	@Column(name = "BB_DESCRIPTION")
	private String description;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TriggerType getTriggerTypeName() {
		return triggerTypeName;
	}

	public void setTriggerTypeName(TriggerType triggerTypeName) {
		this.triggerTypeName = triggerTypeName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
