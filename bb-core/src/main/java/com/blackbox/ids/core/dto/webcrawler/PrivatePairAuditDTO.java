package com.blackbox.ids.core.dto.webcrawler;

import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.model.correspondence.PairAudit;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class PrivatePairAuditDTO.
 */
public class PrivatePairAuditDTO {
	
	/** The pair audit records. */
	private List<PairAudit> pairAuditRecords;
	
	/** The new download queue records. */
	private List<DownloadOfficeActionQueue> newDownloadQueueRecords;
	
	/** The un matched xml keys. */
	private List<String> unMatchedXMLKeys;
	
	/** The user id. */
	private Long userId;
	
	/** The download queue key to entity map. */
	private Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap;

	
	/**
	 * Instantiates a new private pair audit dto.
	 *
	 * @param pairAuditRecords the pair audit records
	 * @param newDownloadQueueRecords the new download queue records
	 * @param unMatchedXMLKeys the un matched xml keys
	 * @param userId the user id
	 * @param downloadQueueKeyToEntityMap the download queue key to entity map
	 */
	public PrivatePairAuditDTO(List<PairAudit> pairAuditRecords,
			List<DownloadOfficeActionQueue> newDownloadQueueRecords, List<String> unMatchedXMLKeys,Long userId, Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap)
	{
		super();
		this.pairAuditRecords = pairAuditRecords;
		this.newDownloadQueueRecords = newDownloadQueueRecords;
		this.unMatchedXMLKeys = unMatchedXMLKeys;
		this.userId = userId;
		this.downloadQueueKeyToEntityMap = downloadQueueKeyToEntityMap;
	}

	/**
	 * Gets the pair audit records.
	 *
	 * @return the pair audit records
	 */
	public List<PairAudit> getPairAuditRecords() {
		return pairAuditRecords;
	}

	/**
	 * Sets the pair audit records.
	 *
	 * @param pairAuditRecords the new pair audit records
	 */
	public void setPairAuditRecords(List<PairAudit> pairAuditRecords) {
		this.pairAuditRecords = pairAuditRecords;
	}

	/**
	 * Gets the new download queue records.
	 *
	 * @return the new download queue records
	 */
	public List<DownloadOfficeActionQueue> getNewDownloadQueueRecords() {
		return newDownloadQueueRecords;
	}

	/**
	 * Sets the new download queue records.
	 *
	 * @param newDownloadQueueRecords the new new download queue records
	 */
	public void setNewDownloadQueueRecords(List<DownloadOfficeActionQueue> newDownloadQueueRecords) {
		this.newDownloadQueueRecords = newDownloadQueueRecords;
	}

	/**
	 * Gets the un matched xml keys.
	 *
	 * @return the un matched xml keys
	 */
	public List<String> getUnMatchedXMLKeys() {
		return unMatchedXMLKeys;
	}

	/**
	 * Sets the un matched xml keys.
	 *
	 * @param unMatchedXMLKeys the new un matched xml keys
	 */
	public void setUnMatchedXMLKeys(List<String> unMatchedXMLKeys) {
		this.unMatchedXMLKeys = unMatchedXMLKeys;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Map<String, DownloadOfficeActionQueue> getDownloadQueueKeyToEntityMap() {
		return downloadQueueKeyToEntityMap;
	}

	public void setDownloadQueueKeyToEntityMap(Map<String, DownloadOfficeActionQueue> downloadQueueKeyToEntityMap) {
		this.downloadQueueKeyToEntityMap = downloadQueueKeyToEntityMap;
	}

	
}
