package com.blackbox.ids.core.repository.webcrawler;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.repository.base.BaseRepository;

/**
 * A DAO for the entity User is simply created by extending the JpaRepository interface provided by spring and custom
 * repository. The following methods are some of the ones available from former interface: save, delete, deleteAll,
 * findOne and findAll. The magic is that such methods must not be implemented, and moreover it is possible create new
 * query methods working only by defining their signature! Custom interface can be used to define custom query methods
 * that can not be implemented by Spring.
 *
 * @author abhay2566
 */

@Repository("downloadOfficeActionQueueRepository")
public interface DownloadOfficeActionQueueRepository extends BaseRepository<DownloadOfficeActionQueue, Long> {

	/**
	 * Find all DownloadOfficeActioQueue values.
	 *
	 * @return the list
	 */
	public List<DownloadOfficeActionQueue> findByCustomerNumber(String customerNumber);

	/**
	 * Find by status.
	 *
	 * @param status
	 *            the status
	 * @return the list
	 */
	@Query("SELECT officeAction FROM DownloadOfficeActionQueue officeAction where"
			+ " officeAction.status != com.blackbox.ids.core.model.enums.QueueStatus.CRAWLER_ERROR"
			+ " and officeAction.status != com.blackbox.ids.core.model.enums.QueueStatus.SUCCESS"
			+ " and officeAction.jurisdictionCode= :jurisdictionCode")
	public List<DownloadOfficeActionQueue> findByDownloadQueueStatusAndJursidictionCode(
			@Param("jurisdictionCode") String jurisdictionCode);
	
	/**
	 * Find by download queue status by jursidiction codes.
	 *
	 * @param jurisdictionCodes the jurisdiction codes
	 * @return the list
	 */
	@Query("SELECT officeAction FROM DownloadOfficeActionQueue officeAction where"
			+ " officeAction.status != com.blackbox.ids.core.model.enums.QueueStatus.CRAWLER_ERROR"
			+ " and officeAction.status != com.blackbox.ids.core.model.enums.QueueStatus.SUCCESS"
			+ " and officeAction.jurisdictionCode in (:jurisdictionCodes)")
	public List<DownloadOfficeActionQueue> findByDownloadQueueStatusByJursidictionCodes(
			@Param("jurisdictionCodes") List<String> jurisdictionCodes);
	
	
	
}