package com.blackbox.ids.core.model.mstr;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The entity class {@code Assignee} represents a business client of blackbox client i.e. a law firm.
 *
 * @author ajay2258
 */
@Entity
@Table(name = "BB_ASSIGNEE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Assignee implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -2749872365483638224L;

	public static final String NAME_DEFAULT_ASSIGNEE = "DEFAULT";
	public static final Long ID_DEFAULT_ASSIGNEE = -1L;

	/** Enumerates the entity types for <tt>LAW_FIRM</tt> client. */
	public enum Entity {
		MICRO,
		SMALL,
		UNDISCOUNTED;

		public static Entity fromString(String strEntity) {
			Entity entity = null;
			for (final Entity e : Entity.values()) {
				if (e.name().equalsIgnoreCase(strEntity)) {
					entity = e;
					break;
				}
			}
			return entity;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BB_ASSIGNEE_ID", nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	/** Required only if {@link Client#getClientType() clientType} is <tt>LAW_FIRM</tt>. */
	@Column(name = "ENTITY")
	@Enumerated(EnumType.STRING)
	private Entity entity;

	@Column(name = "BB_CLIENT")
	private Long clientId;

	/*- ---------------------------- Constructor -- */
	public Assignee() {
		super();
	}

	public Assignee(Long id) {
		super();
		this.id = id;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

}