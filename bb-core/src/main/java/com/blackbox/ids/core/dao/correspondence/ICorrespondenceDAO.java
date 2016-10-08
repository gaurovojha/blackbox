/**
 *
 */
package com.blackbox.ids.core.dao.correspondence;


import java.util.Calendar;
import java.util.List;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.correspondence.CorrespondenceBaseKeysDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;

/**
 * The {@code ICorrespondenceDAO} exposes APIs to perform database operations on {@link CorrespondenceBase} derived
 * entity classes.
 * <p/>
 * All child {@link CorrespondenceBase} entities can be accessed through this single DAO bean.
 *
 * @author abhay2566
 */
public interface ICorrespondenceDAO extends BaseDAO<CorrespondenceBase, Long> {

	public SearchResult<CorrespondenceRecordDTO> fetchUpdateRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	public SearchResult<CorrespondenceRecordDTO> fetchUploadRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	public CorrespondenceRecordDTO getCorrespondenceRecordDTO(Long correspondenceId);
	
    SearchResult<CorrespondenceRecordDTO> filterActiveDocuments(CorrespondenceRecordsFilter filter, PaginationInfo pageInfo);

	SearchResult<CorrespondenceRecordDTO> filterInActiveDocuments(CorrespondenceRecordsFilter filter, PaginationInfo pageInfo);

	CorrespondenceRecordDTO getCorrespondenceRecord(Long correspondenceId);

	long deleteCorrespondenceRecord(Long correspondenceId, String comments);
	
	SearchResult<CorrespondenceRecordDTO> getActiveCorrespondences(CorrespondenceRecordsFilter filter,Long correspondenceId);

	SearchResult<CorrespondenceRecordDTO> getInactiveCorrespondences(CorrespondenceRecordsFilter filter,Long correspondenceId);
	
	
	/**
	 * Gets the correspondence record by duplicate check parameters.
	 *
	 * @param applicationId the application id
	 * @param documentId the document id
	 * @param mailingDate the mailing date
	 * @param size the size
	 * @param pageCount the page count
	 * @return the correspondence record by duplicate check parameters
	 */
	List<CorrespondenceBase> getCorrespondenceRecordByDuplicateCheckParameters(Long applicationId, Long documentId,
			Calendar mailingDate, Long size, Integer pageCount);
	
	public long fetchUpdateRequestCount();
	
	public long fetchUploadRequestCount();

	/**
	 * Gets all correspondenceBase keys
	 * @return List<CorrespondenceBaseKeysDTO> 
	 */
	public List<CorrespondenceBaseKeysDTO> getCorrespondenceBaseKeysDTOs();

}
