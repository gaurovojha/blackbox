package com.blackbox.ids.core.model.notification.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification Process Type.
 *
 * @author nagarro
 */
public enum EntityName {

	/** The reference base data. */
	REFERENCE_BASE_DATA("ReferenceBaseData", "BB_REFERENCE_BASE_DATA"),
	/** The download office action queue. */
	DOWNLOAD_OFFICE_ACTION_QUEUE("DownloadOfficeActionQueue", "BB_SCR_DOWNLOAD_OFFICE_ACTION"),
	/** The create application queue. */
	CREATE_APPLICATION_QUEUE("CreateApplicationQueue", "BB_SCR_CREATE_APPLICATION"),
	
	APPLICATION_STAGE("ApplicationStage", "BB_APPLICATION_STAGE"),
	/** The update family linkage. */
	FIND_FAMILY_QUEUE("FindFamilyQueue", "BB_SCR_FIND_FAMILY"),
	/** The correspondence staging. */
	CORRESPONDENCE_STAGING("CreateApplicationReuest", "BB_CORRESPONDENCE_STAGING"),
	/** The application base. */
	APPLICATION_BASE("ChangeStatusRequest", "BB_APPLICATION_BASE"),
	/** The ocr data. */
	OCR_DATA("OCRDATA", "BB_OCR_DATA"),
	/** The reference staging data. */
	REFERENCE_STAGING_DATA("ReferenceStagingData", "BB_REFERENCE_STAGING_DATA"),
	/**IDS Base*/
	IDS("IDSBaseData", "IDS"),
	/**IDS Filling INFO*/
	IDS_FILING_INFO("IDSFilingInfo","IDS_FILING_INFO"),
	/** The IDS filing tracking queue. */
	TRACK_IDS_FILING_QUEUE("TrackIDSFilingQueue", "BB_SCR_TRACK_IDS_FILING"),;

	/** The entity model name. */
	private String entityModelName;

	/** The entity table name. */
	private String entityTableName;

	/**
	 * Instantiates a new entity name.
	 *
	 * @param entityModelName
	 *            the entity model name
	 * @param entityTableName
	 *            the entity table name
	 */
	private EntityName(final String entityModelName, final String entityTableName) {
		this.entityModelName = entityModelName;
		this.entityTableName = entityTableName;
	}

	/**
	 * Gets the entity model name list.
	 *
	 * @return the entity model name list
	 */
	public List<String> getEntityModelNameList() {
		List<String> entityNameList = new ArrayList<String>();
		EntityName[] entityNames = EntityName.values();

		for (EntityName entityName : entityNames) {
			entityNameList.add(entityName.entityModelName);
		}

		return entityNameList;
	}

	/**
	 * Gets the entity table name list.
	 *
	 * @return the entity table name list
	 */
	public List<String> getEntityTableNameList() {
		List<String> entityNameList = new ArrayList<String>();
		EntityName[] entityNames = EntityName.values();

		for (EntityName entityName : entityNames) {
			entityNameList.add(entityName.entityTableName);
		}

		return entityNameList;
	}

	/**
	 * Gets the entity name by model name.
	 *
	 * @param modelName
	 *            the model name
	 * @return the entity name by model name
	 */
	public static EntityName getEntityNameByModelName(final String modelName) {
		EntityName[] entityNameList = EntityName.values();
		EntityName entityName = null;

		for (EntityName entyName : entityNameList) {
			if (entyName.getEntityModelName().equalsIgnoreCase(modelName)) {
				entityName = entyName;
				break;
			}
		}

		return entityName;
	}

	/**
	 * Gets the entity name by table name.
	 *
	 * @param tableName
	 *            the table name
	 * @return the entity name by table name
	 */
	public static EntityName getEntityNameByTableName(final String tableName) {
		EntityName[] entityNames = EntityName.values();
		EntityName entityName = null;

		for (EntityName entyName : entityNames) {
			if (entyName.entityTableName.equalsIgnoreCase(tableName)) {
				entityName = entyName;
				break;
			}
		}

		return entityName;
	}

	/**
	 * From string.
	 *
	 * @param strEnumName
	 *            the str enum name
	 * @return the entity name
	 */
	public static EntityName fromString(final String strEnumName) {
		EntityName entityName = null;
		for (final EntityName e : EntityName.values()) {
			if (e.name().equalsIgnoreCase(strEnumName)) {
				entityName = e;
				break;
			}
		}
		return entityName;
	}

	/**
	 * Gets the entity model name.
	 *
	 * @return the entity model name
	 */
	public String getEntityModelName() {
		return entityModelName;
	}

	/**
	 * Gets the entity table name.
	 *
	 * @return the entity table name
	 */
	public String getEntityTableName() {
		return entityTableName;
	}
}
