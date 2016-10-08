package com.blackbox.ids.core.webcrawler.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue.FamilyType;

public interface IWebCrawlerApplicationDAO {

	public Map<String, List<CreateApplicationQueue>> getCreateApplicationList();

	public Map<String, List<FindFamilyQueue>> getFindFamilyList(
			FamilyType type);

	public Map<String, Map<Long, List<FindFamilyQueue>>> getFindParentList();

	public void updateFamilyStatus(Long baseCaseId, String familyId, QueueStatus status);

	public void updateCreateApplicationQueueStatus(Long id,
			QueueStatus status, int retryCount);

	public void updateApplicationCrawlerErrorStatus(Long id,
			QueueStatus status, int retryCount);

	public Long createApplicationStageEntry(ApplicationStage app);

	public void createFindFamilyEntry(FindFamilyQueue findParentEntry);

	public void createDownloadOfficeActionEntry(
			DownloadOfficeActionQueue downloadOfficeAction);

	public boolean checkApplicationInExclustionList(
			String applicationNumber, String jurisdiction);

	public void createInclusionListEntry(String applicationNumber,
			String applicationNumberRaw, String jurisdiction, String customer,
			Calendar filingDate);

	public void updateFindFamilyQueueStatus(String ids,
			QueueStatus status);

	public Long createApplicationRequestEntry(
			CreateApplicationQueue app);

	public String findJurisdictionByCountry(String country);

	public CreateApplicationQueue getCreateApplicationQueueEntry(
			Long id);

	public boolean checkIfApplicationQueueEntryExists(String applicationNumber, String jurisdiction, Long baseCaseId);

	void updateFindFamilyQueueStatus(Long id, QueueStatus status);
	
	/**
	 * Get list of application numbers from create application queue for validation
	 * @param customerNos
	 */
	List<CreateApplicationQueue> getCreateApplicationQueueList(List<String> customerNos);

	void updateFamilyApplicationsStatus(Long baseCaseId);

	Long getFindParentEntityByApplicationStage(long applicationStageId);

}