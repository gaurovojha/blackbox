package com.blackbox.ids.core.repository.correspondence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
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
public interface CorrespondenceStagingRepository extends BaseRepository<CorrespondenceStaging, Long> {

	/**
	 * Find staging data by status and source.
	 *
	 * @param applicationStatus
	 *            the application status
	 * @param source
	 *            the source
	 * @return the list
	 * @throws ApplicationException
	 *             the application exception
	 */
	@Query("SELECT staging from CorrespondenceStaging staging where lower(staging.status) = lower(:status) and lower(staging.source) = lower(:source)")
	public List<CorrespondenceStaging> findStagingDataByStatusAndSource(@Param("status") String status,
			@Param("source") String source) throws ApplicationException;

	/**
	 * Find staging data with status either Pending or Create_New_Application_Queue_Initiated
	 * 
	 * @return the list
	 * @throws ApplicationException
	 */
	@Query("SELECT staging from CorrespondenceStaging staging where lower(staging.status) in ('PENDING','CREATE_APPLICATION_QUEUE_INITIATED') and (staging.documentCode !=:docCode OR staging.documentCode is NULL )")
	public List<CorrespondenceStaging> findStagingDataToBeImported(@Param("docCode") String docCode)
			throws ApplicationException;

	@Query("SELECT st from CorrespondenceStaging st where st.documentCode =:documentCode and st.status ='PENDING')")
	public List<CorrespondenceStaging> findByDocumentCodeAndStatusActive(@Param("documentCode") String documentCod);

}