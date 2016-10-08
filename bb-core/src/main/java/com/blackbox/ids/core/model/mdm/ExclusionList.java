/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.blackbox.ids.core.model.base.AuditableEntity;

/**
 * The entity class {@code ExclusionList} maintains the application numbers, that are to be excluded from all the
 * automatic processing /scheduled tasks.<p/>
 * Please note that application numbers in list with status <tt>INACTIVE</tt> can be picked for auto-processing.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_EXCLUSION_LIST")
public class ExclusionList extends AuditableEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = -4113762508888875848L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Embedded
	private ApplicationNumberList applications;
	
	

	public ExclusionList() {
		super();
		applications = new ApplicationNumberList();
	}

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ApplicationNumberList getApplications() {
		return applications;
	}

	public void setApplications(ApplicationNumberList applications) {
		this.applications = applications;
	}

}
