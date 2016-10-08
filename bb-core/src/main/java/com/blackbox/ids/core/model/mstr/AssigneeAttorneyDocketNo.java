package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BB_MSTR_ASSIGNEE_ATTORNEY_DOCKET")
public class AssigneeAttorneyDocketNo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 9209377093627178497L;

	@Id
	@Column(name = "ASSIGNEE_ATTORNEY_DOCKET_ID")
	private Long id;

	@Column(name = "ASSIGNEE_ID", nullable = false)
	private Long assigneeId;

	@Column(name = "SEGMENT_VALUE_TO_MATCH", nullable = false)
	private String segmentValueToMatch;

	@Column(name = "STATUS", nullable = false)
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public String getSegmentValueToMatch() {
		return segmentValueToMatch;
	}

	public void setSegmentValueToMatch(String segmentValueToMatch) {
		this.segmentValueToMatch = segmentValueToMatch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
