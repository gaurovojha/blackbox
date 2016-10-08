/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.blackbox.ids.core.model.base.AuditableEntity;

/**
 * The {@code Application} serves as a base class for a patent application. It holds all the common flat attributes
 * for an application. <p/>
 * Entity classes {@link ApplicationBase} and {@link ApplicationStage} extends this class to add business specific fields to it.
 *
 * @author ajay2258
 */
@MappedSuperclass
public class Application extends AuditableEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = 2858271149652905869L;

	/** Enumerates all possible sources for an application. */
	public enum Source {
		AUTOMATIC,
		MANUAL,
		DRAFT;
	}

	/** Enumerates all possible sub-sources for a parent application. */
	public enum SubSource {
		MANUAL,
		USPTO,
		THIRD_PARTY;
	}

	@Column(name = "APPLICATION_NUMBER")
	private String applicationNumber;

	@Column(name = "FAMILY_ID")
	private String familyId;

	@Embedded
	private ApplicationDetails appDetails = new ApplicationDetails();

	@Embedded
	private PublicationDetails publicationDetails = new PublicationDetails();

	@Embedded
	private PatentDetails patentDetails = new PatentDetails();

	@Embedded
	private OrganizationDetails organizationDetails = new OrganizationDetails();

	@Column(name = "SOURCE")
	@Enumerated(EnumType.STRING)
	private Source source;

	@Column(name = "SUB_SOURCE")
	@Enumerated(EnumType.STRING)
	private SubSource subSource;

	@Column(name = "DESCRIPTION")
	private String description;

	/*- ---------------------------- JPA Callbacks -- */
	@Override
	@PrePersist
	public void prePersist() {
		this.familyId = this.familyId == null ? null : this.familyId.toUpperCase();
		super.prePersist();
	}

	@Override
	@PreUpdate
	public void preUpdate() {
		this.familyId = this.familyId == null ? null : this.familyId.toUpperCase();
		super.preUpdate();
	}

	/*- ---------------------------- getter-setters -- */
	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public ApplicationDetails getAppDetails() {
		return appDetails;
	}

	public void setAppDetails(ApplicationDetails appDetails) {
		this.appDetails = appDetails;
	}

	public PublicationDetails getPublicationDetails() {
		return publicationDetails;
	}

	public void setPublicationDetails(PublicationDetails publicationDetails) {
		this.publicationDetails = publicationDetails;
	}

	public PatentDetails getPatentDetails() {
		return patentDetails;
	}

	public void setPatentDetails(PatentDetails patentDetails) {
		this.patentDetails = patentDetails;
	}

	public OrganizationDetails getOrganizationDetails() {
		return organizationDetails;
	}

	public void setOrganizationDetails(OrganizationDetails organizationDetails) {
		this.organizationDetails = organizationDetails;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public SubSource getSubSource() {
		return subSource;
	}

	public void setSubSource(SubSource subSource) {
		this.subSource = subSource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
