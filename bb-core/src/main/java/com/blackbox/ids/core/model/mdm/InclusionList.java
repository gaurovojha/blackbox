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

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The entity class {@code InclusionList} maintains the application numbers which are to be meant to automatic processing tasks.
 * <p/>
 * An application, either not present in the list or marked as <tt>inactive</tt> won't be picked for web crawling activities.
 *
 * @author ajay2258
 */
@Entity
@Table(name =  "BB_INCLUSION_LIST")
public class InclusionList extends BaseEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = 2262691291612793853L;

	public static final String VALUE_LIST_ON = "ON";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Embedded
	private ApplicationNumberList applications;

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
