package com.blackbox.ids.core.dao.correspondence;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordDTO;
import com.blackbox.ids.core.dto.correspondence.dashboard.CorrespondenceRecordsFilter;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;

public interface ICorrespondenceStageDAO extends BaseDAO<CorrespondenceStaging, Long> {

	public abstract SearchResult<CorrespondenceRecordDTO> fetchAllTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);
	
	public abstract SearchResult<CorrespondenceRecordDTO> fetchMyTrackApplications(CorrespondenceRecordsFilter filter,
			PaginationInfo pageInfo);
	long rejectApplicationRequest(long entityId);

	public abstract long createApplicationRequest(long entityId);

}
