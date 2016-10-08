package com.blackbox.ids.services.correspondence;

import java.io.IOException;

import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mdm.NumberType;
import com.blackbox.ids.dto.ResponseDTO;
import com.blackbox.ids.dto.correspondence.CorrespondenceFormDto;

/**
 * The Interface ICorrespondenceService.
 * @author Nagarro
 */
public interface ICorrespondenceService {

	public abstract boolean isAtleastOneBookmark(String file) throws IOException;

	public abstract boolean isMultipleBookmark(String file) throws IOException;

	public abstract boolean isValidBookmarkType(String file) throws IOException;

	/**
	 * This API is used to save record in correspondence base table.
	 * @param correspondenceBase
	 *            the correspondence base
	 * @return the correspondence base
	 */
	public abstract CorrespondenceBase saveCorrespondenceBase(CorrespondenceBase correspondenceBase);

	public abstract ResponseDTO createApplicationRequest(CorrespondenceFormDto correspondenceFormDto);

	public abstract boolean isValidJurisdiction(String jurisdiction);

	public abstract ResponseDTO createManualCorrespondence(CorrespondenceFormDto correspondenceFormDto);

	public abstract ResponseDTO searchApplication(CorrespondenceFormDto correspondenceFormDto);

	public abstract Job addEntryToJobs(Job.Type jobType);

	boolean updateStatusInJobs(Job job);

	/**
	 * This API is used to fetch update request records in Correspondence Action Item based on specified filter and
	 * pagination related information.
	 * @param filter
	 *            the filter
	 * @param pageInfo
	 *            the page info
	 * @return the search result
	 */
	public SearchResult<CorrespondenceRecordDTO> fetchUpdateRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	/**
	 * This API is used to fetch upload request records in Correspondence Action Item based on specified filter and
	 * pagination related information.
	 * @param filter
	 *            the filter
	 * @param pageInfo
	 *            the page info
	 * @return the search result
	 */
	public SearchResult<CorrespondenceRecordDTO> fetchUploadRequestRecords(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	/**
	 * This API is used to fetch correspondence record dto for given correspondence id.
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the correspondence record dto
	 */
	public CorrespondenceRecordDTO getCorrespondenceRecordDTO(Long correspondenceId);

	/**
	 * This API is used to reject download office notification.
	 * @param notificationProcessId
	 *            the notification process id
	 */
	public void rejectDownloadOfficeNotification(Long notificationProcessId);

	public boolean isValidAssignee(String assignee);

	public ResponseDTO validateUSManualCorrespondenceData(CorrespondenceFormDto correspondenceFormDto,
			ApplicationBase applicationBase, String covertedApplicationNumber);

	/**
	 * This API is used to fetch all track applications in Correspondence Track Application on the basis of filter &
	 * pagination information.
	 * @param filter
	 *            the filter
	 * @param pageInfo
	 *            the page info
	 * @return the search result
	 */
	public SearchResult<CorrespondenceRecordDTO> fetchAllTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	/**
	 * This API is used to fetch my track applications in Correspondence Track Application on the basis of filter &
	 * pagination information.
	 * @param filter
	 *            the filter
	 * @param pageInfo
	 *            the page info
	 * @return the search result
	 */
	public SearchResult<CorrespondenceRecordDTO> fetchMyTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	SearchResult<CorrespondenceRecordDTO> filterActiveDocuments(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	SearchResult<CorrespondenceRecordDTO> filterInactiveDocuments(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);

	/**
	 * This API is used to get the correspondence record dto for specified correspondence id.
	 * @param correspondenceId
	 *            the correspondence id
	 * @return the correspondence record
	 */
	CorrespondenceRecordDTO getCorrespondenceRecord(Long correspondenceId);

	/**
	 * This API is used to soft delete the correspondence record with comments.
	 * @param correspondenceId
	 *            the correspondence id
	 * @param comments
	 *            the comments
	 * @return the long
	 */
	long deleteCorrespondenceRecord(Long correspondenceId, String comments);

	SearchResult<CorrespondenceRecordDTO> getActiveCorrespondences(CorrespondenceRecordsFilter filter,
			Long correspondenceId);

	SearchResult<CorrespondenceRecordDTO> getInactiveCorrespondences(CorrespondenceRecordsFilter filter,
			Long correspondenceId);

	/**
	 * This API is used to fetch action item count.
	 * @return the long
	 */
	public long fetchActionItemCount();

	/**
	 * This API is used to fetch update request count.
	 * @return the long
	 */
	public long fetchUpdateRequestCount();

	/**
	 * This API is used to fetch upload request count.
	 * @return the long
	 */
	public long fetchUploadRequestCount();

	public String getConvertedValueForWO(String originalValue, NumberType numberType, String jurisdiction);
}
