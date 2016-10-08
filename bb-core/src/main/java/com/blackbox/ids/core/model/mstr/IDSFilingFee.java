/**
 *
 */
package com.blackbox.ids.core.model.mstr;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The entity class {@code IDSFilingFee} maintains the IDS filing fee amount as per the {@link Assignee.Entity} type.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_MSTR_IDS_FILING_FEE")
public class IDSFilingFee extends BaseEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = -8483121428756338363L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_FILING_FEE_ID")
	private Long id;

	@Column(name = "ENTITY")
	@Enumerated(EnumType.STRING)
	private Assignee.Entity entity;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Calendar startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Calendar endDate;

	@Column(name = "FEE")
	private Double fee;

	/*- ----------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Assignee.Entity getEntity() {
		return entity;
	}

	public void setEntity(Assignee.Entity entity) {
		this.entity = entity;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

}
