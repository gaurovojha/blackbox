package com.blackbox.ids.core.dao.correspondence.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.dao.correspondence.IJobDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.model.correspondence.Job;
import com.blackbox.ids.core.model.correspondence.Job.Status;
import com.blackbox.ids.core.model.correspondence.Job.Type;
import com.blackbox.ids.core.model.correspondence.QJob;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAUpdateClause;

@Repository
public class JobDAOImpl extends BaseDaoImpl<Job, Long>implements IJobDAO {

	@Override
	@Transactional
	public List<Job> findPairAuditNewEntries() {
		QJob job = QJob.job;

		BooleanBuilder predicate = new BooleanBuilder().and(job.type.eq(Type.PAIR)).and(job.status.eq(Status.NEW));
		return getJPAQuery().from(job).where(predicate).list(job);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Job> findBulkUploadNewEntries() {
		QJob job = QJob.job;

		BooleanBuilder predicate = new BooleanBuilder().and(job.type.eq(Type.BULK)).and(job.status.eq(Status.NEW));
		return getJPAQuery().from(job).where(predicate).list(job);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateJobStatus(Job job) {
		Calendar updatedDate = Calendar.getInstance();
		QJob jobUpdate = QJob.job;
		JPAUpdateClause updateClause = getJPAUpdateClause(jobUpdate);
		updateClause.where(jobUpdate.id.eq(job.getId())).set(jobUpdate.status, job.getStatus())
				.set(jobUpdate.updatedDate, updatedDate);

		long rowUpdate = updateClause.execute();
		boolean updateRecord = (rowUpdate > 0) ? true : false;
		return updateRecord;
	}

}
