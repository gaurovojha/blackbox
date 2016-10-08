package com.blackbox.ids.core.model.webcrawler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;

/**
 * N417 Treatment table
 */
@Entity
@Table(name = "BB_SCR_N417_TREATMENT")
public class N417TreatmentQueue { 
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_ID", referencedColumnName = "BB_APPLICATION_ID", nullable = false)
	private ApplicationBase application;
	
	/** The status. */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private QueueStatus status = QueueStatus.INITIATED;

	public N417TreatmentQueue() {
		super();
	}
	
	public N417TreatmentQueue(ApplicationBase application) {
		super();
		this.application = application;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ApplicationBase getApplication() {
		return application;
	}

	public void setApplication(ApplicationBase application) {
		this.application = application;
	}

	public QueueStatus getStatus() {
		return status;
	}

	public void setStatus(QueueStatus status) {
		this.status = status;
	}
	
}
