package com.blackbox.ids.core.dao.correspondence;

import java.util.List;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.correspondence.Job;

public interface IJobDAO extends BaseDAO<Job, Long> {

	/* fetch pair audit new entries */
	List<Job> findPairAuditNewEntries();

	/* find bulk upload new entries */
	List<Job> findBulkUploadNewEntries();

	boolean updateJobStatus(Job job);

}
