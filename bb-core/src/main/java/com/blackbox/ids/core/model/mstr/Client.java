package com.blackbox.ids.core.model.mstr;

import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.blackbox.ids.core.model.base.BaseEntity;

/**
 * The class <tt>Client</tt> represents a business entity client. It also enumerates the possible client attributes.
 * <p/>
 * Please note that there will be only 1 client per installation.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_CLIENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client extends BaseEntity {

	/** The serial version UID. */
	private static final long serialVersionUID = 6128898959783118598L;

	/** Database identifier for single client instance. */
	public static final Long ID_CLIENT = -1L;

	/** Enumerates the possible types of client. */
	public enum ClientType {
		CORPORATE,
		LAW_FIRM;
	}

	/** Enumerates the possible validation means for master data. */
	public enum ValidationType {
		OCR_VALIDATION,
		PRIVATE_PAIR_VALIDATION;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_CLIENT_ID", nullable = false)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ORGANIZATION_TYPE", nullable = false)
	private String orgType;

	@Column(name = "CLIENT_TYPE")
	@Enumerated(EnumType.STRING)
	private ClientType clientType;

	@Column(name = "VALIDATION_TYPE")
	@Enumerated(EnumType.STRING)
	private ValidationType validationType;

	@Column(name = "EXPORT_FLAG")
	private boolean exportFlag;

	@OneToOne
	@PrimaryKeyJoinColumn
	private AttorneyDocketFormat attorneyDocketFormat;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CLIENT")
	private List<TechnologyGroup> techGroups;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CLIENT")
	private List<Organization> organizations;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "BB_CLIENT")
	private List<Assignee> assignees;

	/*- ---------------------------- getter-setters -- */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public ValidationType getValidationType() {
		return validationType;
	}

	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
	}

	public boolean isExportFlag() {
		return exportFlag;
	}

	public void setExportFlag(boolean exportFlag) {
		this.exportFlag = exportFlag;
	}

	public AttorneyDocketFormat getAttorneyDocketFormat() {
		return attorneyDocketFormat;
	}

	public void setAttorneyDocketFormat(AttorneyDocketFormat attorneyDocketFormat) {
		this.attorneyDocketFormat = attorneyDocketFormat;
	}

}
