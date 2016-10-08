package com.blackbox.ids.core.dao.ids.impl;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.core.dao.ids.IDSDao;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingInProgressDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.FilingReadyDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSAttorneyApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSDrillDownInfoDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSFilingPackageDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSManuallyFiledDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSPendingApprovalDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSPrivatePairKeyDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.IDSReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449DetailDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.N1449PendingDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.ValidateRefStatusDTO;
import com.blackbox.ids.core.dto.IDS.notification.NotificationBaseDTO;
import com.blackbox.ids.core.dto.datatable.PaginationInfo;
import com.blackbox.ids.core.dto.datatable.SearchResult;
import com.blackbox.ids.core.dto.mdm.dashboard.ActiveRecordsFilter;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.IDS.CertificationStatement;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.IDS.Sequence;
import com.blackbox.ids.core.model.IDS.IDS.Status;
import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.IDS.IDSTrigger;
import com.blackbox.ids.core.model.IDS.QIDS;
import com.blackbox.ids.core.model.IDS.QIDSFilingInfo;
import com.blackbox.ids.core.model.IDS.QIDSFilingPrivatePair;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.enums.MDMRecordStatus;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.mstr.IDSRelevantStatus;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.notification.process.NotificationStatus;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.reference.QReferenceBaseData;
import com.blackbox.ids.core.model.reference.QReferenceBaseFPData;
import com.blackbox.ids.core.model.reference.QReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.QReferenceBasePUSData;
import com.blackbox.ids.core.model.reference.QSourceReference;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.repository.IDSTriggerRepository;
import com.blackbox.ids.core.repository.notification.process.NotificationProcessRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.group.GroupBy;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.sql.SQLExpressions;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.QTuple;

@Repository
public class IDSDaoImpl extends BaseDaoImpl<IDS, Long> implements IDSDao{

	private static final Logger LOGGER = Logger.getLogger(IDSDaoImpl.class);

	private static final String TRIGGER1 = "TRIGGER1";
	private static final String TRIGGER2 = "TRIGGER2";
	private static final String TRIGGER3 = "TRIGGER3";
	private static final String TRIGGER4 = "TRIGGER4";
	private static final String TRIGGER5 = "TRIGGER5";
	private static final String TRIGGER6 = "TRIGGER6";
	private static final String ACTIVE = "ACTIVE";
	private static final String URGENT = "URGENT";
	private static final String	APPLICATION	= "application";
	private static final String	FAMILY		= "family";

	/**
	 * OA resonse code description
	 */
	private static final String OAESPONSECODE_DESC_A = "Desc A…";
	private static final String OAESPONSECODE_DESC_A_NA 		 = "Desc A.NA";
	private static final String OAESPONSECODE_DESC_A_NE 		 = "Desc A.NE";
	private static final String OAESPONSECODE_DESC_A_PE 		 = "Desc A.PE";
	private static final String OAESPONSECODE_DESC_A_QU 		 = "Desc A.QU";
	private static final String OAESPONSECODE_DESC_AMSB 		 = "Desc AMSB";
	private static final String OAESPONSECODE_DESC_ELC 			 = "Desc ELC.";
	private static final String OAESPONSECODE_DESC_INTERVIEW_APP = "Desc INTERVIEW.APP";
	private static final String OAESPONSECODE_DESC_RCEX          = "Desc RCEX";
	private static final String OAESPONSECODE_DESC_REM 			 = "Desc REM";
	private static final String OAESPONSECODE_DESC_IFEE 		 = "Desc IFEE";
	private static final String OAESPONSECODE_DESC_IDS 			 = "Desc IDS";

	@Autowired
	private NotificationProcessRepository	notificationProcessRepository;

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@Autowired
	private IDSTriggerRepository idsTriggerRepository;

	@Override
	public String generateSequenceId(Sequence sequence) {
		return sequence.prefix + String.format("%09d", getNextSequence(sequence.seqName));

	}
	@Override
	public SearchResult<InitiateIDSRecordDTO> getInitiateRecords(ActiveRecordsFilter filter, PaginationInfo pageInfo,
			String tabType) {

		Calendar startFillingDate = null;
		Calendar endFillingDate = null;

		if(filter.getStartDate()!=null || filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QApplicationBase targetApp = QApplicationBase.applicationBase;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDS ids = QIDS.iDS;

		QIDSReferenceFlow r = new QIDSReferenceFlow("r");
		QIDS i = new QIDS("i");

		BooleanBuilder predicate = null;
		// List<Tuple> resultSet = getJPAQuery().from(refFlow).groupBy(refFlow.targetApplication.id).list(new
		// QTuple(refFlow.targetApplication.id,refFlow.count(),refFlow.createdDate.min()));
		predicate =  getInitiateRecordsPredicate(refFlow,targetApp,ids);


		if(tabType.equals(URGENT)){
			predicate =  getUrgentRecordsPredicate(refFlow, targetApp);
		} else {
			predicate =  getInitiateRecordsPredicate(refFlow,targetApp,ids);
		}

		predicate = predicate.or(targetApp.id.eq(ids.application.id));

		JPAQuery query = getJPAQuery().from(refFlow, ids).innerJoin(refFlow.targetApplication, targetApp).distinct();

		long totalRecords = query.clone().where(predicate).groupBy(refFlow.targetApplication.id)
				.list(SQLExpressions.countDistinct(refFlow.referenceFlowId)).size();
		//long totalRecords = counts.size();

		long filteredRecords = 0L;
		List<InitiateIDSRecordDTO> items = Collections.emptyList();

		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				//TODO :date predicate
			}
			filteredRecords = query.clone().where(predicate).groupBy(refFlow.targetApplication.id)
					.list(SQLExpressions.countDistinct(refFlow.referenceFlowId)).size();
			if(filteredRecords > 0L) {
				//TODO :sort Attribute
				LOGGER.info("display query with where clause"+(query.where(predicate)).toString());
				items = query.where(predicate).limit(pageInfo.getLimit()).offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(InitiateIDSRecordDTO.class, targetApp.id, targetApp.familyId,
								targetApp.jurisdiction.code, targetApp.applicationNumber, ids.lastFiledOn,
								new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id))
								.count(),
								new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id))
								.unique(r.createdDate.min()),
								targetApp.organizationDetails.prosecutionStatus,
								targetApp.organizationDetails.oaResponseDate));
			}
		}
		return new SearchResult<InitiateIDSRecordDTO>(totalRecords, filteredRecords, items);
	}



	@Override
	public SearchResult<InitiateIDSRecordDTO> allFamilyApplications(ActiveRecordsFilter filter, String familyId,
			long appId) {
		QApplicationBase targetApp = QApplicationBase.applicationBase;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDS ids = QIDS.iDS;
		QIDSReferenceFlow r = new QIDSReferenceFlow("r");
		QIDS i = new QIDS("i");

		BooleanBuilder predicate =  getInitiateRecordsPredicate(refFlow,targetApp,ids);
		predicate = predicate.and(targetApp.familyId.eq(familyId)).and(targetApp.id.ne(appId));

		JPAQuery query = getJPAQuery().from(refFlow, ids).innerJoin(refFlow.targetApplication, targetApp).distinct();

		long totalRecords = query.clone().where(predicate).count();

		long filteredRecords = 0L;
		List<InitiateIDSRecordDTO> items = Collections.emptyList();
		if (totalRecords > 0L) {
			filteredRecords = totalRecords;
			if(filteredRecords > 0L) {
				items = query.where(predicate)
						.list(ConstructorExpression.create(InitiateIDSRecordDTO.class, targetApp.id, targetApp.familyId,
								targetApp.jurisdiction.code, targetApp.applicationNumber, ids.lastFiledOn,
								new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id))
								.count(),
								new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id))
								.unique(r.createdDate.min()),
								targetApp.organizationDetails.prosecutionStatus,
								targetApp.organizationDetails.oaResponseDate));
			}
		}
		return new SearchResult<InitiateIDSRecordDTO>(totalRecords, filteredRecords, items);
	}

	/**
	 * Based on the active triggers, predicate for urgent tab is created
	 *
	 * @return condition BooleanBuilder
	 */
	private BooleanBuilder getUrgentRecordsPredicate(QIDSReferenceFlow refFlow,QApplicationBase targetApp){
		List<IDSTrigger> triggers = idsTriggerRepository.findAll();
		BooleanBuilder condition1 = new BooleanBuilder();
		for(IDSTrigger trigger:triggers){
			String name = trigger.getTriggerTypeName().name();
			int days = trigger.getDays();
			if(trigger.getStatus().equals(IDSTrigger.Status.ACTIVE)){
				BooleanBuilder condition = getPredicateForTrigger(name, days, refFlow, targetApp);
				condition1.or(condition);
			}
		}
		return condition1;
	}

	/**
	 * Based on the trigger value, corresponding static predicate is returned
	 * 
	 * @param refFlow
	 * @param targetApp
	 * @return
	 */
	private BooleanBuilder getPredicateForTrigger(String name, int days, QIDSReferenceFlow refFlow,
			QApplicationBase targetApp) {
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;

		Calendar cal= Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		BooleanBuilder condition = new BooleanBuilder();
		switch(name){
		case TRIGGER1:
			condition.and(
					targetApp.organizationDetails.idsRelevantStatus.in(Arrays.asList(IDSRelevantStatus.ISSUE_FEE_PAID,
							IDSRelevantStatus.ISSUE_NOTIFICATION_RECEIVED, IDSRelevantStatus.NOA_RECEIVED,
							IDSRelevantStatus.RCE_FILED, IDSRelevantStatus.ADVISORY_OA_RECEIVED,
							IDSRelevantStatus.FINAL_OA_RECEIVED, IDSRelevantStatus.EX_PARTE_QUAYLE_OA_RECEIVED)))
			.and(refFlow.targetApplication.id.in(new JPASubQuery().from(refFlow)
					.where(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.UNCITED)))
					.groupBy(refFlow.targetApplication.id).list(refFlow.targetApplication.id)));
			break;
		case TRIGGER2:
			condition
			.and(refFlow.targetApplication.id.in(new JPASubQuery().from(refFlow)
					.where(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.UNCITED)))
					.groupBy(refFlow.targetApplication.id).list(refFlow.targetApplication.id)))
			.and(targetApp.organizationDetails.oaResponseDate.isNotNull())
			.and(targetApp.organizationDetails.oaResponseDate.goe(refFlow.filingDate))
			.and(targetApp.organizationDetails.oaResponseDesc.isNotNull())
			.and(targetApp.organizationDetails.oaResponseDesc
					.in(Arrays.asList(OAESPONSECODE_DESC_A, OAESPONSECODE_DESC_A_NA, OAESPONSECODE_DESC_A_NE,
							OAESPONSECODE_DESC_A_PE, OAESPONSECODE_DESC_A_QU, OAESPONSECODE_DESC_AMSB,
							OAESPONSECODE_DESC_ELC, OAESPONSECODE_DESC_INTERVIEW_APP, OAESPONSECODE_DESC_RCEX,
							OAESPONSECODE_DESC_REM, OAESPONSECODE_DESC_IFEE, OAESPONSECODE_DESC_IDS)));
			break;
		case TRIGGER3:
			condition
			.and(targetApp.organizationDetails.idsRelevantStatus
					.notIn(Arrays.asList(IDSRelevantStatus.FIRST_OA_PENDING,
							IDSRelevantStatus.RESTRICTION_REQUIREMENT_RECEIVED, IDSRelevantStatus.RCE_FILED)))
			.and(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.UNCITED)))
			.and(refFlow.targetApplication.id.ne(refFlow.sourceApplication.id))
			.and(refFlow.targetApplication.familyId.eq(refFlow.sourceApplication.familyId))
			.and(refFlow.correspondence.mailingDate.after(cal));
			break;
		case TRIGGER4:
			condition
			.and(targetApp.organizationDetails.idsRelevantStatus
					.notIn(Arrays.asList(IDSRelevantStatus.FIRST_OA_PENDING,
							IDSRelevantStatus.RESTRICTION_REQUIREMENT_RECEIVED, IDSRelevantStatus.RCE_FILED)))
			.and(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.UNCITED)))
			.and(refFlow.correspondence.mailingDate.after(cal));
			break;
		case TRIGGER5:
			condition.and(targetApp.appDetails.filingDate.after(cal)).and(
					targetApp.id.notIn(new JPASubQuery().from(idsFilingInfo).list(idsFilingInfo.ids.application.id)));
			break;
		case TRIGGER6:
			condition.and(targetApp.appDetails.filingDate.after(cal))
			.and(targetApp.id.in(new JPASubQuery().from(idsFilingInfo).list(idsFilingInfo.ids.application.id)))
			.and(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.UNCITED)))
			.and(refFlow.referenceFlowSubStatus.in(Arrays.asList(ReferenceFlowSubStatus.CITED_IN_PARENT)));
			break;
		}
		return condition;
	}

	private BooleanBuilder getInitiateRecordsPredicate(QIDSReferenceFlow refFlow, QApplicationBase targetApp,
			QIDS ids) {

		return new BooleanBuilder()
				.and(targetApp.recordStatus.in(Arrays.asList(MDMRecordStatus.DROPPED,
						MDMRecordStatus.ALLOWED_TO_ABANDON, MDMRecordStatus.TRANSFERRED)))
				.and(targetApp.jurisdiction.code.eq(Jurisdiction.Code.US.name()))
				.and(targetApp.patentDetails.patentNumberRaw.isNotNull())
				.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED));
	}

	@Override
	public List<IDSPendingApprovalDTO> getPendingApprovalRecords() {

		QIDS ids = QIDS.iDS;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QUser user = QUser.user ;
		QIDSReferenceFlow r = new QIDSReferenceFlow("r");

		BooleanBuilder predicate =  new BooleanBuilder().and(ids.status.eq(Status.PENDING_APPROVAL)
				.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED))
				.and(ids.approvedBy.eq(user.id)));

		JPAQuery query = getJPAQuery().from(referenceFlow, user).innerJoin(referenceFlow.ids, ids);

		List<IDSPendingApprovalDTO> items = Collections.emptyList();


		items =  query
				.where(predicate).list(
						ConstructorExpression
						.create(IDSPendingApprovalDTO.class, ids.id, ids.application.familyId,
								ids.application.jurisdiction, ids.application.applicationNumber,
								ids.application.organizationDetails.idsRelevantStatus, ids.approvalRequestedOn,
								user.person.firstName.concat(user.person.lastName),
								new JPASubQuery().from(r)
								.where(r.targetApplication.id.eq(referenceFlow.targetApplication.id)
										.and(referenceFlow.referenceFlowStatus
												.eq(ReferenceFlowStatus.DROPPED)
												.and(referenceFlow.doNotFile.eq(true))))
								.count()));

		return items;
	}
	
	
	private BooleanBuilder getPendingApprovalPredicate(QIDS ids  , QIDSReferenceFlow referenceFlow, QUser user) {
		
		 return new BooleanBuilder().and(ids.status.eq(Status.PENDING_APPROVAL)
					.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED))
					.and(ids.approvedBy.eq(user.id)));
	}

	@Override
	public List<IDSPendingApprovalDTO> getPendingResponseRecords() {
		QUser user = QUser.user;
		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		JPAQuery query = getJPAQuery().from(ids, user, notificationProcess);
		BooleanBuilder predicate = null;
		predicate = getPendingResponsePredicate(ids , notificationProcess , user);
		List<IDSPendingApprovalDTO> items = Collections.emptyList();
		items =  query.where(predicate)
				.list(ConstructorExpression.create(IDSPendingApprovalDTO.class, ids.id,
						notificationProcess.notificationProcessId, ids.application.id, ids.application.familyId,
						ids.application.jurisdiction, ids.application.applicationNumber,
						ids.application.organizationDetails.idsRelevantStatus, ids.approvalRequestedOn,
						user.person.firstName.concat(" ").concat(user.person.lastName), ids.attorneyComments));
		return items;
	}
	
	private BooleanBuilder getPendingResponsePredicate(QIDS ids  , QNotificationProcess notificationProcess, QUser user) {
		
		 return new BooleanBuilder()
					.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.IDS_APPROVAL_REQUEST)
							.and(ids.id.eq(notificationProcess.entityId)).and(ids.approvedBy.eq(user.id)));
	}
	

	private Long getUncitedReferenceCount(Long appId) {
		// TODO Auto-generated method stub
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		return getJPAQuery().from(referenceFlow).where(referenceFlow.targetApplication.id.eq(appId)
				.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED))).count();
	}

	@Override
	public List<FilingReadyDTO> getFilingReadyRecords() {

		QIDS ids = QIDS.iDS;
		QUser user = QUser.user ;

		BooleanBuilder predicate = new BooleanBuilder()
				.and(ids.status.eq(Status.APPROVED).and(ids.approvedBy.eq(user.id)));


		JPAQuery query = getJPAQuery().from(ids ,  user);

		List<FilingReadyDTO> items = Collections.emptyList();
		items = query.where(predicate)
				.list(ConstructorExpression.create(FilingReadyDTO.class, ids.id, ids.application.id,
						ids.application.familyId, ids.application.jurisdiction, ids.application.applicationNumber,
						ids.application.organizationDetails.idsRelevantStatus,
						user.person.firstName.concat(user.person.lastName), ids.attorneyComments));
		return items;

	}


	/*Get Filing in progress records*/

	@Override
	public List<FilingInProgressDTO> getFilingInProgressRecords(Calendar dateCheck) {

		QIDS ids = QIDS.iDS;
		QUser user = QUser.user ;

		BooleanBuilder predicate = new BooleanBuilder(ids.status
				.in(Status.GENERATING_FILE_PACKAGE, Status.FILE_GENERATION_FAILED, Status.FILE_PACKAGE_GENERATED,
						Status.FILED, Status.FILING_FAILED)
				.and(ids.fileGeneratedOn.before(dateCheck)).and(ids.fileGeneratedBy.eq(user.id)));

		/*
		 * //CASE MANUAL .and(ids.filingChannel.eq(FilingChannel.MANUAL)
		 * .and(ids.status.eq(Status.GENERATING_FILE_PACKAGE) .or(ids.status.eq(Status.FILE_PACKAGE_GENERATED) //remove
		 * date check and move to jsp .and(ids.fileGeneratedOn.before(dateCheck)) ) ) ) //CASE SYSTEM/AUTOMATIC -If
		 * Filing Channel is AUTOMATIC – Once the File Generation is initiated, Display the record here for view only
		 * (and Download action not available). Record is available for view, ONLY till the Filing Package Upload Status
		 * is SUCCESS and IDS Status is IDS Filed. .or(ids.filingChannel.eq(FilingChannel.SYSTEM)
		 * .and(ids.status.eq(Status.GENERATING_FILE_PACKAGE) .or(ids.status.eq(Status.FILE_PACKAGE_GENERATED)
		 * .and(ids.fileGeneratedOn.before(dateCheck)) ) ) )
		 */
		//add FILE UPLOAD STATUS

		JPAQuery query = getJPAQuery().from(ids , user).where(predicate);


		List<FilingInProgressDTO> items = Collections.emptyList();

		items = query.list(ConstructorExpression.create(FilingInProgressDTO.class, ids.id, ids.application.familyId,
				ids.application.jurisdiction, ids.application.applicationNumber,
				ids.application.organizationDetails.idsRelevantStatus,
				user.person.firstName.concat(user.person.lastName), ids.fileGeneratedOn, ids.filingFee,
				ids.filingChannel, ids.status, ids.subStatus));
		return items;
	}

	@Override
	public List<IDSManuallyFiledDTO> getManuallyFiledIDSRecords() {
		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		JPAQuery query = getJPAQuery().from(ids, notificationProcess);
		BooleanBuilder predicate = new BooleanBuilder().and(notificationProcess.notificationProcessType
				.eq(NotificationProcessType.UPLOAD_MANUALLY_FILED_IDS).and(ids.id.eq(notificationProcess.entityId)));

		List<IDSManuallyFiledDTO> items = Collections.emptyList();

				//TODO :sort Attribute
				//Long dbId ,  Jurisdiction jurisdiction ,String applicationNo	,Date mailingDate,
				//String documentDesc, Date notified

				items = query.where(predicate)
						.list(ConstructorExpression.create(IDSManuallyFiledDTO.class, ids.id,notificationProcess.notificationProcessId,
								ids.application.jurisdiction, ids.application.applicationNumber, ids.filingInstructedOn,
								notificationProcess.notifiedDate));
		return items ;
	}
	/*
	 * To create this view, following query has to be run on the database: List of open approval notifications that have
	 * been sent to the user / attorney Reference count = total references (US patents, US publications, Foreign
	 * patents, NPL & selected source references [if any]) in the IDS which is pending approval. IDS prepared by – name
	 * of the paralegal who had sent the IDS for approval Comments – this will be the comments that paralegal has sent
	 * for the attorney in the notification The user can switch to Family View using the view toggle – system will group
	 * ‘IDS pending approval’ by family ID. Expand button will ONLY appear if another IDS is pending approval.
	 */

	/** Implementation for fetching attorney approval records */
	@Override
	public SearchResult<IDSAttorneyApprovalDTO> getAttorneyApprovalRecords(ActiveRecordsFilter filter,
			PaginationInfo pageInfo) {

		Calendar startFillingDate = null;
		Calendar endFillingDate = null;


		if(filter.getStartDate()!=null && filter.getEndDate()!=null) {
			startFillingDate = BlackboxDateUtil.convertDateToCal(filter.getStartDate());
			endFillingDate = BlackboxDateUtil.convertDateToCal(filter.getEndDate());
		}

		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow"); 
		QApplicationBase application = QApplicationBase.applicationBase;

		BooleanBuilder predicate =  new BooleanBuilder()
				.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.IDS_APPROVAL_REQUEST)
						.and(notificationProcess.entityName.eq(EntityName.IDS)).and(notificationProcess.active.eq(true))
						.and(notificationProcess.status.eq(NotificationStatus.PENDING)));

		JPAQuery query = getJPAQuery().from(notificationProcess, ids).innerJoin(ids.application, application)
				.where(predicate.and(notificationProcess.entityId.eq(ids.id)));

		long totalRecords = query.clone().where(predicate).count();

		long filteredRecords = 0L;
		List<IDSAttorneyApprovalDTO> items = Collections.emptyList();

		if (totalRecords > 0L) {
			if(startFillingDate!=null || endFillingDate!=null) {
				//TODO :date predicate
			}
			filteredRecords = query.clone().where(predicate).count();


			if(filteredRecords > 0L) {

				items = query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset())
						.list(ConstructorExpression.create(IDSAttorneyApprovalDTO.class, ids.id,
								ids.application.familyId, ids.application.jurisdiction.code,
								ids.application.applicationNumber,
								ids.application.organizationDetails.idsRelevantStatus,ids.lastFiledOn,
								new JPASubQuery().from(refFlow).where(refFlow.ids.eq(ids)).count(), ids.createdByUser,
								ids.certificate.comment,ids.application.id, notificationProcess.notificationProcessId));
			}
		}
		return new SearchResult<IDSAttorneyApprovalDTO>(totalRecords, filteredRecords, items);
	}




	@Override
	public List<ValidateRefStatusDTO> getValidateRefStatusIDSRecords() {

		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow");
		QApplicationBase application = QApplicationBase.applicationBase;


		BooleanBuilder predicate =  new BooleanBuilder()
				.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.VALIDATE_REFERENCE_STATUS)
						.and(notificationProcess.entityName.eq(EntityName.IDS)));

		JPAQuery query = getJPAQuery().from(notificationProcess, refFlow).innerJoin(refFlow.ids, ids).distinct()
				.where(predicate.and(notificationProcess.entityId.eq(ids.id)));

		List<ValidateRefStatusDTO> items = Collections.emptyList();

		items = query.list(ConstructorExpression.create(ValidateRefStatusDTO.class, ids.id, ids.application.familyId,
				ids.application.jurisdiction, ids.application.applicationNumber,
				new JPASubQuery().from(refFlow)
				.where(refFlow.ids.eq(ids)
						.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED)
								.and(refFlow.referenceFlowSubStatus
										.eq(ReferenceFlowSubStatus.PENDING_USPTO_FILING))))
				.count(),
				notificationProcess.notifiedDate, notificationProcess.notificationProcessId));
		return items;
	}

	@Override
	public boolean updateIDSStatus(final long idsID, final Status newStatus, final long updatedBy) {
		final QIDS ids = QIDS.iDS;
		return getJPAUpdateClause(ids).set(ids.status, newStatus).set(ids.updatedDate, Calendar.getInstance())
				.set(ids.updatedByUser, updatedBy).where(ids.id.eq(idsID)).execute() == 1L;
	}
	@Override
	public Long getApplicationIdForIDSId(Long idsId) {
		return getJPAQuery().from(QIDS.iDS).where(QIDS.iDS.id.eq(idsId)).uniqueResult(QIDS.iDS.application.id);
	}

	@Override
	public List<IDSFilingPackageDTO> getIDSFilingPackageRecords(){

		QApplicationBase application = QApplicationBase.applicationBase;
		QIDS ids = QIDS.iDS;
		QUser user = QUser.user;

		JPAQuery query = getJPAQuery().from(ids, user).innerJoin(ids.application, application);

		BooleanBuilder predicate = new BooleanBuilder()
				.and(ids.status.in(Arrays.asList(Status.FILED, Status.FILE_GENERATION_FAILED,
						Status.FILE_PACKAGE_GENERATED, Status.WITHDRAWN, Status.FILING_FAILED)))
				.and(ids.fileGeneratedBy.eq(user.id));

		return query.where(predicate)
				.list(ConstructorExpression.create(IDSFilingPackageDTO.class, application.id, application.familyId,
						application.jurisdiction.code, application.applicationNumber, ids.idsBuildId,
						user.person.firstName.concat(user.person.lastName), ids.fileGeneratedOn, ids.filingFee,
						ids.filingChannel, ids.status));

	}

	@Override
	public List<N1449PendingDTO> get1449PendingIDS() {
		QNotificationProcess notification = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QApplicationBase application = QApplicationBase.applicationBase;
		QIDSReferenceFlow r = new QIDSReferenceFlow("r");

		JPAQuery query = getJPAQuery().from(notification,refFlow,idsFilingInfo).distinct();

		BooleanBuilder predicate = new BooleanBuilder().and(notification.applicationId.eq(refFlow.targetApplication.id))
				.and(notification.notificationProcessType.eq(NotificationProcessType.N1449))
				.and(refFlow.internalFinalIDSId.eq(idsFilingInfo.idsFinalId)
						.or(refFlow.externalFinalIDSId.eq(idsFilingInfo.idsFinalId))
						.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED))
						.and(refFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.PENDING_1449)));

		Map<Long, Map<String, Calendar>> idsPending1449List = query.where(predicate)
				.transform(GroupBy.groupBy(refFlow.targetApplication.id)
						.as(GroupBy.map(idsFilingInfo.idsFinalId, idsFilingInfo.filingDate)));

		List<Tuple> values = getJPAQuery().from(notification, application)
				.where(get1449PendingIDSPredicate(notification, application))
				.list(new QTuple(application.id, application.familyId, application.jurisdiction.code,
						application.applicationNumber, notification.notifiedDate));

		ArrayList<N1449PendingDTO> items = new ArrayList<N1449PendingDTO>();

		for (Tuple tuple : values) {
			items.add(new N1449PendingDTO(tuple.get(application.id), tuple.get(application.familyId),
					tuple.get(application.jurisdiction.code), tuple.get(application.applicationNumber),
					idsPending1449List.get(tuple.get(application.id)), tuple.get(notification.notifiedDate)));
		}
		return items;
	}

	@Override
	public List<N1449DetailDTO> get1449Details(Long appId) {
		QApplicationBase application = QApplicationBase.applicationBase;
		QCorrespondenceBase correspondence = QCorrespondenceBase.correspondenceBase;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;


		JPAQuery query = getJPAQuery().from(refFlow,idsFilingInfo).distinct();
		BooleanBuilder predicate = new BooleanBuilder().and(refFlow.targetApplication.id.eq(appId))
				.and(refFlow.internalFinalIDSId.eq(idsFilingInfo.idsFinalId)
						.or(refFlow.externalFinalIDSId.eq(idsFilingInfo.idsFinalId))
						.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED))
						.and(refFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.PENDING_1449)));

		Map<Long, Map<String, Calendar>> idsPending1449List = query.where(predicate)
				.transform(GroupBy.groupBy(refFlow.targetApplication.id)
						.as(GroupBy.map(idsFilingInfo.idsFinalId, idsFilingInfo.filingDate)));

		List<Tuple> values = getJPAQuery().from(application, correspondence)
				.where(application.id.eq(correspondence.application.id).and(application.id.eq(appId)))
				.list(new QTuple(application.id, application.jurisdiction.code, application.applicationNumber,
						correspondence.mailingDate, correspondence.documentCode.description));
		List<N1449DetailDTO> items = new ArrayList<N1449DetailDTO>();
		for (Tuple tuple : values) {
			items.add(new N1449DetailDTO(tuple.get(application.id), tuple.get(application.jurisdiction.code),
					tuple.get(application.applicationNumber), idsPending1449List.get(tuple.get(application.id)),
					tuple.get(correspondence.mailingDate), tuple.get(correspondence.documentCode.description)));
		}
		return items;

	}

	private BooleanBuilder get1449PendingIDSPredicate(QNotificationProcess notification, QApplicationBase application) {
		return new BooleanBuilder().and(notification.notificationProcessType.eq(NotificationProcessType.N1449)
				.and(notification.applicationId.eq(application.id)));
	}



	@Override
	public SearchResult<NotificationBaseDTO> getAppNotifications(final long applicationId,
			final PaginationInfo pageInfo, final List<Long> skipNotificationList) {

		List<NotificationProcessType> notificationListApp = appNotificationTypeList();
		List<Long> appIdList = new ArrayList<Long>();
		appIdList.add(applicationId);

		SearchResult<NotificationBaseDTO> baseDTOsList = notificationProcessRepository
				.getPendingNotifications(appIdList, pageInfo, notificationListApp, skipNotificationList);
		return baseDTOsList;
	}

	@Override
	public SearchResult<NotificationBaseDTO> getFamilyNotifications(final long applicationId,
			final PaginationInfo pageInfo, final List<Long> skipNotificationList) {

		List<NotificationProcessType> notificationListFamily = familyNotificationTypeList();
		List<Long> appIdList = getFamilyMembersOfApplication(applicationId);
		SearchResult<NotificationBaseDTO> baseDTOsList = notificationProcessRepository
				.getPendingNotifications(appIdList, pageInfo, notificationListFamily, skipNotificationList);

		return baseDTOsList;

	}

	private List<Long> getFamilyMembersOfApplication(final long applicationId) {

		List<Long> applicationIdList = new ArrayList<Long>();

		String familyId = applicationBaseDAO.findFamilyOfApplication(applicationId);
		applicationIdList = applicationBaseDAO.findFamilyMembers(applicationId, familyId);
		return applicationIdList;
	}

	@Override
	public Map<String, Long> countNotifications(final long applicationId, final List<Long> skipAppNotificationList,
			final List<Long> skipFamilyNotificationList) {

		List<NotificationProcessType> notificationTypeAppList = appNotificationTypeList();
		List<NotificationProcessType> notificationTypeFamilyList = familyNotificationTypeList();
		List<Long> appIdListApp = new ArrayList<Long>();
		appIdListApp.add(applicationId);
		List<Long> appIdListFamily = getFamilyMembersOfApplication(applicationId);

		long appNotification = notificationProcessRepository.countPendingNotifications(appIdListApp,
				notificationTypeAppList, skipAppNotificationList);
		long familyNotification = notificationProcessRepository.countPendingNotifications(appIdListFamily,
				notificationTypeFamilyList, skipFamilyNotificationList);

		Map<String, Long> notificationCountMap = new HashMap<String, Long>();
		notificationCountMap.put(APPLICATION, appNotification);
		notificationCountMap.put(FAMILY, familyNotification);
		return notificationCountMap;

	}

	private List<NotificationProcessType> familyNotificationTypeList() {
		return Arrays.asList(NotificationProcessType.UPDATE_FAMILY_LINKAGE, NotificationProcessType.NPL_DUPLICATE_CHECK,
				NotificationProcessType.CREATE_SML, NotificationProcessType.INPADOC_FAILED,
				NotificationProcessType.REFERENCE_MANUAL_ENTRY,
				NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE,
				NotificationProcessType.UPLOAD_MANUALLY_FILED_IDS);
	}

	private List<NotificationProcessType> appNotificationTypeList() {
		return Arrays.asList(NotificationProcessType.UPDATE_FAMILY_LINKAGE,
				NotificationProcessType.DASHBOARD_ACTION_STATUS, NotificationProcessType.NPL_DUPLICATE_CHECK,
				NotificationProcessType.CREATE_SML, NotificationProcessType.INPADOC_FAILED,
				NotificationProcessType.REFERENCE_MANUAL_ENTRY,
				NotificationProcessType.CORRESPONDENCE_RECORD_FAILED_IN_DOWNLOAD_QUEUE,
				NotificationProcessType.REFERENCE_MANUAL_ENTRY, NotificationProcessType.VALIDATE_REFERENCE_STATUS,
				NotificationProcessType.UPLOAD_MANUALLY_FILED_IDS, NotificationProcessType.N1449);
	}

	@Override
	public boolean updateIDSForApproval(long idsID, long attorney) {
		final QIDS ids = QIDS.iDS;
		long currentUser = BlackboxSecurityContextHolder.getUserId();
		Calendar sysdate = Calendar.getInstance();

		return getJPAUpdateClause(ids).set(ids.status, IDS.Status.PENDING_APPROVAL)
				.set(Arrays.asList(ids.approvalRequestedBy, ids.approvalRequestedOn, ids.approvedBy),
						Arrays.asList(currentUser, sysdate, attorney))
				.set(Arrays.asList(ids.updatedByUser, ids.updatedDate), Arrays.asList(currentUser, sysdate))
				.execute() == 1L;
	}

	/*
	 * @Override public Map<ReferenceType, IDSReferenceDTO> getIDSReferences() { // TODO Auto-generated method stub
	 * QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo; QIDS ids = QIDS.iDS; QNotificationProcess
	 * notificationProcess = QNotificationProcess.notificationProcess; QIDSReferenceFlow refFlow = new
	 * QIDSReferenceFlow("refFlow"); QApplicationBase application = QApplicationBase.applicationBase; QReferenceBaseData
	 * refBase = QReferenceBaseData.referenceBaseData;
	 * 
	 * JPAQuery query = getJPAQuery().from(idsFilingInfo);
	 * query.join(refFlow).on(idsFilingInfo.idsFinalId.eq(refFlow.internalFinalIDSId).and(idsFilingInfo.source.eq(
	 * IDS_Source.INTERNAL))); query.join(application).on(refFlow.targetApplication.id.eq(application.id));
	 * //query.join(refBase).on(refFlow.referenceBaseDataId.eq(refBase.referenceBaseDataId));
	 * query.list(ConstructorExpression.create(IDSReferenceDTO.class,idsFilingInfo.id, application.jurisdiction,
	 * application.publicationDetails.publicationNumber, refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus));
	 * return null; }
	 */

	@Override
	public List<IDSReferenceDTO> getIDSReferencesForRefType(ReferenceType refType, Long idsId) {
		// TODO Auto-generated method stub
		QIDS ids = QIDS.iDS;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow");
		QApplicationBase application = QApplicationBase.applicationBase;
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData; 
		QReferenceBasePUSData refBasePUS = QReferenceBasePUSData.referenceBasePUSData;
		QReferenceBaseFPData refBaseFP = QReferenceBaseFPData.referenceBaseFPData;
		QReferenceBaseNPLData refBaseNPL = QReferenceBaseNPLData.referenceBaseNPLData;

		List<IDSReferenceDTO> items = Collections.emptyList();


		//query.join(refFlow).on(refFlow.internalFinalIDSId.eq(idsFilingInfo.idsFinalId));
		BooleanBuilder predicate = new BooleanBuilder().and(ids.id.eq(idsId));

		switch(refType) {

		case PUS :
			JPAQuery queryPUS = getJPAQuery().from(refFlow,refBasePUS).innerJoin(refFlow.ids, ids).distinct();
			predicate.and(refBasePUS.referenceType.eq(ReferenceType.PUS))
			.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBasePUS.referenceBaseDataId));
			items = queryPUS.where(predicate)
					.list(ConstructorExpression.create(IDSReferenceDTO.class, ids.id, refFlow.referenceFlowId,
							refBasePUS.publicationNumber, refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus,
							refBasePUS.kindCode, refBasePUS.applicantName));
			break;

		case US_PUBLICATION :
			JPAQuery queryUSPub = getJPAQuery().from(ids, refFlow,refBasePUS).distinct();
			predicate.and(refBasePUS.referenceType.eq(ReferenceType.US_PUBLICATION))
			.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBasePUS.referenceBaseDataId));
			items = queryUSPub.where(predicate)
					.list(ConstructorExpression.create(IDSReferenceDTO.class, ids.id, refFlow.referenceFlowId,
							refBasePUS.publicationNumber, refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus,
							refBasePUS.kindCode, refBasePUS.applicantName));
			break;

		case FP :
			JPAQuery queryFP = getJPAQuery().from(ids, refFlow,refBaseFP).distinct();
			predicate.and(refBaseFP.referenceType.eq(ReferenceType.FP))
			.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBaseFP.referenceBaseDataId));
			items = queryFP.where(predicate)
					.list(ConstructorExpression.create(IDSReferenceDTO.class, ids.id, refFlow.referenceFlowId,
							refBaseFP.jurisdiction, refBaseFP.publicationNumber, refFlow.referenceFlowStatus,
							refFlow.referenceFlowSubStatus, refBaseFP.kindCode, refBaseFP.applicantName));
			break;

		case NPL :
			JPAQuery queryNPL = getJPAQuery().from(ids, refFlow,refBaseNPL).distinct();
			predicate.and(refFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL))
			.and(refFlow.referenceBaseData.referenceBaseDataId.eq(refBaseNPL.referenceBaseDataId));
			items = queryNPL.where(predicate)
					.list(ConstructorExpression.create(IDSReferenceDTO.class, ids.id, refFlow.referenceFlowId,
							refBaseNPL.stringData, refFlow.referenceFlowStatus, refFlow.referenceFlowSubStatus));
			break;

		default :
			break;
		}


		return items;
	}
	@Override
	public Map<ReferenceType, Long> getIDSReferenceCount(Long idsId) {
		// TODO Auto-generated method stub
		Map<ReferenceType, Long> refCounts = new HashMap<>();
		QIDS ids = QIDS.iDS;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow");

		JPAQuery query = getJPAQuery().from(refFlow).innerJoin(refFlow.ids,ids);
		//query.join(refFlow).on(refFlow.internalFinalIsDSId.eq(idsFilingInfo.idsFinalId));
		BooleanBuilder predicate = new BooleanBuilder().and(ids.id.eq(idsId));

		// Map<Long, Map<String, Calendar>> idsPending1449List =
		// query.where(predicate).transform(GroupBy.groupBy(refFlow.targetApplication.id).as(GroupBy.map(idsFilingInfo.idsFinalId,idsFilingInfo.filingDate)));
		refCounts = query.where(predicate).groupBy(refFlow.referenceBaseData.referenceType).transform(GroupBy
				.groupBy(refFlow.referenceBaseData.referenceType).as(refFlow.referenceBaseData.referenceType.count()));
		//transform(GroupBy.groupBy(refFlow.referenceBaseData.referenceType).as(refFlow.referenceBaseData.referenceType.count()));
		//reference type US count
		/*
		 * BooleanBuilder refCountPUS = new
		 * BooleanBuilder().and(refFlow.referenceBaseData.referenceType.eq(ReferenceType.PUS));
		 * refCounts.put(ReferenceType.PUS.name(),query.clone().where(predicate.and(refCountPUS)).count());
		 * 
		 * //reference type US PUBLICATION count BooleanBuilder refCountUsPub = new
		 * BooleanBuilder().and(refFlow.referenceBaseData.referenceType.eq(ReferenceType.US_PUBLICATION));
		 * refCounts.put(ReferenceType.US_PUBLICATION.name(),query.clone().where(predicate.and(refCountUsPub)).count());
		 * 
		 * //reference type FOREIGN PATENT count BooleanBuilder refCountFP = new
		 * BooleanBuilder().and(refFlow.referenceBaseData.referenceType.eq(ReferenceType.FP));
		 * refCounts.put(ReferenceType.FP.name(),query.clone().where(predicate.and(refCountFP)).count());
		 * 
		 * //reference type NPL count BooleanBuilder refCountNPL = new
		 * BooleanBuilder().and(refFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
		 * refCounts.put(ReferenceType.NPL.name(),query.clone().where(predicate.and(refCountNPL)).count());
		 */

		return refCounts;
	}

	@Override
	public Map<ReferenceType, Long> getIDSReferenceCount(String finalIdsId) {
		Map<ReferenceType, Long> refCounts = new HashMap<>();
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		refCounts = getJPAQuery().from(refFlow)
				.where(refFlow.internalFinalIDSId.eq(finalIdsId).or(refFlow.externalFinalIDSId.eq(finalIdsId))
						.and(refFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED))
						.and(refFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.PENDING_1449)))
				.groupBy(refFlow.referenceBaseData.referenceType)
				.transform(GroupBy.groupBy(refFlow.referenceBaseData.referenceType)
						.as(refFlow.referenceBaseData.referenceType.count()));
		return refCounts;
	}



	/**
	 * IDS drill down header section data query
	 *
	 * @param applicationId
	 */
	@Override
	public IDSDrillDownInfoDTO getIDSDrillDownInfo(Long applicationId){
		QIDS ids = QIDS.iDS;
		QApplicationBase application = QApplicationBase.applicationBase;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;

		BooleanBuilder predicate = new BooleanBuilder().and(ids.application.id.eq(applicationId));
		JPAQuery query = getJPAQuery().from(ids).innerJoin(ids.application, application);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		List<IDSDrillDownInfoDTO> items = Collections.emptyList();
		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				items =  query.where(predicate).distinct()
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class, ids.application.id,
								ids.application.familyId, ids.application.jurisdiction.description,
								ids.application.applicationNumber, ids.application.appDetails.filingDate,
								ids.application.attorneyDocketNumber.segment));
			}
		}
		return items.get(0);
	}
	@Override
	public List<InitiateIDSRecordDTO> getUrgentIDSRecords(final String urgent) {

		QApplicationBase targetApp = QApplicationBase.applicationBase;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QIDS ids = QIDS.iDS;

		QIDSReferenceFlow r = new QIDSReferenceFlow("r");
		QIDS i = new QIDS("i");

		BooleanBuilder predicate = null;
		predicate =  getInitiateRecordsPredicate(refFlow,targetApp,ids);
		predicate =  getUrgentRecordsPredicate(refFlow, targetApp);

		predicate = predicate.or(targetApp.id.eq(ids.application.id));

		JPAQuery query = getJPAQuery().from(refFlow, ids).innerJoin(refFlow.targetApplication, targetApp).distinct();

		List<InitiateIDSRecordDTO> items = Collections.emptyList();

		LOGGER.info("display query with where clause"+(query.where(predicate)).toString());
		items = query.where(predicate).list(ConstructorExpression.create(InitiateIDSRecordDTO.class, targetApp.id,
				targetApp.familyId, targetApp.jurisdiction.code, targetApp.applicationNumber, ids.lastFiledOn,
				new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id)).count(),
				new JPASubQuery().from(r).where(r.targetApplication.id.eq(refFlow.targetApplication.id))
				.unique(r.createdDate.min()),
				targetApp.organizationDetails.prosecutionStatus, targetApp.organizationDetails.oaResponseDate));

		return items;
	}

	@Override
	public boolean updateIDSSubStatus(long idsID, SubStatus newStatus) {
		final QIDS ids = QIDS.iDS;
		return getJPAUpdateClause(ids).set(ids.subStatus, newStatus).set(ids.updatedDate, Calendar.getInstance())
				.set(ids.updatedByUser, BlackboxSecurityContextHolder.getUserId()).where(ids.id.eq(idsID))
				.execute() == 1L;
	}


	/**
	 * IDS drill down filing dates section data query
	 *
	 * @param applicationId
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownFilingDates(Long applicationId){
		QIDS ids = QIDS.iDS;
		QIDSFilingInfo idsFilingInfo = QIDSFilingInfo.iDSFilingInfo;

		BooleanBuilder predicate = new BooleanBuilder().and(ids.application.id.eq(applicationId));
		JPAQuery query = getJPAQuery().from(idsFilingInfo).innerJoin(idsFilingInfo.ids, ids);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		List<IDSDrillDownInfoDTO> filingDatesForApplication = Collections.emptyList();

		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				filingDatesForApplication =  query.where(predicate)
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class
								,ids.application.id, ids.id, idsFilingInfo.ids.id
								,new JPASubQuery().from(idsFilingInfo).where(ids.id.eq(idsFilingInfo.ids.id)).unique(idsFilingInfo.filingDate)
								,new JPASubQuery().from(idsFilingInfo).where(ids.id.eq(idsFilingInfo.ids.id)).groupBy(idsFilingInfo.filingDate).count()
								));
			}
		}
		return filingDatesForApplication;
	}

	@Override
	public CertificationStatement findCertificationStatement(long idsID) {
		QIDS ids = QIDS.iDS;
		return getJPAQuery().from(ids).where(ids.id.eq(idsID)).uniqueResult(ids.certificate);
	}
	@Override
	public boolean updateIDSReferenceStatus(UpdateRefStatusDTO updateRefs) {
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * IDS drill down IDS Reference Types section data query
	 * 
	 * @param applicationId
	 * @return IDSDrillDownInfoDTO list
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypeCounts(Long applicationId, Long IDSId){
		List<IDSDrillDownInfoDTO> idsReferencesForApplication = Collections.emptyList();
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;

		BooleanBuilder predicate = new BooleanBuilder().and(refFlow.targetApplication.id.eq(applicationId))
				.and(refFlow.ids.id.eq(IDSId))
				.and(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.CITED)));

		JPAQuery query = getJPAQuery().from(refFlow).innerJoin(refFlow.referenceBaseData, refBase);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				idsReferencesForApplication =  query.where(predicate).groupBy(refBase.referenceType)
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class
								,refBase.referenceType
								,refBase.referenceType.count()
								));
			}
		}
		return idsReferencesForApplication;
	}

	/** 
	 * IDS drill down IDS Reference Types section data query
	 * 
	 * @param applicationId
	 * @return IDSDrillDownInfoDTO list
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceTypes(Long applicationId, Long IDSId){
		QIDS ids = QIDS.iDS;
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QSourceReference sourceRef = QSourceReference.sourceReference;

		BooleanBuilder predicate = new BooleanBuilder().and(refFlow.targetApplication.id.eq(applicationId))
				.and(refFlow.ids.id.eq(IDSId))
				.and(refFlow.referenceFlowStatus.in(Arrays.asList(ReferenceFlowStatus.CITED)));

		JPAQuery query = getJPAQuery().from(refFlow, sourceRef).innerJoin(refFlow.referenceBaseData, refBase);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		List<IDSDrillDownInfoDTO> idsReferencesForApplication = Collections.emptyList();

		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				idsReferencesForApplication =  query.where(predicate).distinct()
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class
								,refFlow.targetApplication.id
								,refFlow.ids.id
								,refFlow.referenceFlowId
								,refBase.referenceBaseDataId
								,refBase.referenceType
								,refFlow.citeId
								,refBase.jurisdiction.code
								,refBase.publicationNumber
								,new JPASubQuery().from(sourceRef).where(refBase.sourceReference.id.eq(sourceRef.id)).unique(sourceRef.nplString)
								));
			}
		}
		return idsReferencesForApplication;
	}


	/** 
	 * IDS drill down IDS Reference Source document section query
	 * 
	 * @param reference_id
	 * @return IDSDrillDownInfoDTO list
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSource(Long referenceBaseId){
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;
		QCorrespondenceBase corrBase =QCorrespondenceBase.correspondenceBase;


		BooleanBuilder predicate = new BooleanBuilder().and(refBase.referenceBaseDataId.eq(referenceBaseId));
		JPAQuery query = getJPAQuery().from(refBase).innerJoin(refBase.correspondence, corrBase);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		List<IDSDrillDownInfoDTO> idsReferencesSource = Collections.emptyList();

		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				idsReferencesSource =  query.where(predicate)
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class
								,corrBase.id
								,refBase.jurisdiction.code
								,refBase.application.applicationNumber
								,refBase.familyId
								,corrBase.documentCode.description
								,refBase.mailingDate
								));
			}
		}
		return idsReferencesSource;
	}


	/** 
	 * IDS drill down IDS Reference Source's Other Documents section query
	 * 
	 * @param reference_id
	 * @return IDSDrillDownInfoDTO list
	 */
	@Override
	public List<IDSDrillDownInfoDTO> getIDSDrillDownReferenceSourceOthers(Long corrId){
		QReferenceBaseData refBase = QReferenceBaseData.referenceBaseData;
		QIDSReferenceFlow refFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QSourceReference sourceRef = QSourceReference.sourceReference;
		
		BooleanBuilder predicate = new BooleanBuilder().and(refBase.correspondence.id.eq(corrId));

		JPAQuery query = getJPAQuery().from(refFlow, sourceRef).innerJoin(refFlow.referenceBaseData, refBase);
		long totalRecords = query.clone().where(predicate).count();
		long filteredRecords = 0L;

		List<IDSDrillDownInfoDTO> idsReferencesSourceOthers = Collections.emptyList();

		if (totalRecords > 0L) {

			filteredRecords = query.clone().where(predicate).count();
			if(filteredRecords > 0L) {
				idsReferencesSourceOthers =  query.where(predicate).distinct()
						.list(ConstructorExpression.create(IDSDrillDownInfoDTO.class
								,refBase.referenceBaseDataId
								,refBase.jurisdiction.code
								,refBase.publicationNumber
						,refBase.referenceType
								,refFlow.referenceFlowStatus
						,new JPASubQuery().from(sourceRef).where(refBase.sourceReference.id.eq(sourceRef.id)).unique(sourceRef.nplString)
								,refFlow.filingDate
								));
			}
		}
		return idsReferencesSourceOthers;
	}

	@Override
	public List<Long> findWithStatusEq(long applicationID, Status status) {
		QIDS ids = QIDS.iDS;
		return getJPAQuery().from(ids).where(ids.application.id.eq(applicationID).and(ids.status.eq(status)))
				.list(ids.id);
	}


	@Override
	public List<Long> findWithStatusEq(long applicationID, Status status1, Status status2) {
		QIDS ids = QIDS.iDS;
		return getJPAQuery().from(ids).where(ids.application.id.eq(applicationID).and(ids.status.in(status1, status2)))
				.list(ids.id);
	}

	@Override
	public boolean submitEmailResponse(IDS ids, final Long updatedByUser ,final String comment) {
		return  false;
	}

	@Override
	public IDS getIDSWithEarliestFileGenerationDate(List<Long> idsList) {
		QIDS ids = QIDS.iDS;
		JPAQuery query = getJPAQuery().from(ids);
		query.where(ids.id.in(idsList));
		query.orderBy(ids.fileGeneratedOn.asc());
		return query.list(ids).get(0);
	}

	@Override
	public String generateExternalIDSId() {
		return IDS.Sequence.EXTERNAL_FINAL_ID.prefix
				+ String.format("%06d", getNextSequence(IDS.Sequence.EXTERNAL_FINAL_ID.seqName));
	}

	@Override
	public String generateInternalIDSId() {
		return IDS.Sequence.INTERNAL_FINAL_ID.prefix
				+ String.format("%06d", getNextSequence(IDS.Sequence.INTERNAL_FINAL_ID.seqName));
	}

	@Override
	public String getCurrentExternalIDSId() {
		return IDS.Sequence.EXTERNAL_FINAL_ID.prefix
				+ String.format("%06d", getCurrentSequence(IDS.Sequence.EXTERNAL_FINAL_ID.seqName));
	}

	@Override
	public String getCurrentInternalIDSId() {
		return IDS.Sequence.INTERNAL_FINAL_ID.prefix
				+ String.format("%06d", getCurrentSequence(IDS.Sequence.INTERNAL_FINAL_ID.seqName));
	}
	
	@Override
	public SearchResult<IDSAttorneyApprovalDTO> getAttorneyApprovalAllFamilyRecords(String familyID, Long applicationID, Long notificationId) {
	
	
		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		QIDSReferenceFlow refFlow = new QIDSReferenceFlow("refFlow"); 
		QApplicationBase application = QApplicationBase.applicationBase;

		BooleanBuilder predicate =  new BooleanBuilder()
				.and(notificationProcess.notificationProcessType.eq(NotificationProcessType.IDS_APPROVAL_REQUEST)
						.and(notificationProcess.entityName.eq(EntityName.IDS)).and(notificationProcess.active.eq(true)).and(notificationProcess.status.eq(NotificationStatus.PENDING)));
		
		JPAQuery query = getJPAQuery().from(notificationProcess, ids).innerJoin(ids.application, application)
				.where(predicate.and(notificationProcess.entityId.eq(ids.id)).and(application.id.eq(applicationID).and(application.familyId.eq(familyID))).and(notificationProcess.notificationProcessId.ne(notificationId)));
	
		long totalRecords = query.clone().where(predicate).count();

		long filteredRecords = 0L;
		List<IDSAttorneyApprovalDTO> items = Collections.emptyList();

		if (totalRecords > 0L) {

				
				items =  query
						.list(ConstructorExpression.create(IDSAttorneyApprovalDTO.class
								,ids.id,  ids.application.familyId , ids.application.jurisdiction.code, ids.application.applicationNumber ,
								ids.application.organizationDetails.idsRelevantStatus,ids.lastFiledOn,
								new JPASubQuery().from(refFlow)
								.where(refFlow.ids.eq(ids)).count(), 
								ids.createdByUser, ids.certificate.comment,ids.application.id ,notificationProcess.notificationProcessId));
		
		}
		return new SearchResult<IDSAttorneyApprovalDTO>(totalRecords, filteredRecords, items);
	}
	
	@Override
	public List<IDSPrivatePairKeyDTO> getPrivatePairKeys() {
		// TODO Auto-generated method stub
		QIDSFilingPrivatePair privatePairKey = QIDSFilingPrivatePair.iDSFilingPrivatePair;
		JPAQuery query = getJPAQuery().from(privatePairKey);
		
		List<IDSPrivatePairKeyDTO> idsFilingPrivatePair = Collections.emptyList();

		idsFilingPrivatePair =  query.list(ConstructorExpression.create(IDSPrivatePairKeyDTO.class
								,privatePairKey.id
								,privatePairKey.keyType
								,privatePairKey.filingPKIName
								));
		
		return idsFilingPrivatePair;

		}
	
	/*
	 * (Get Count of IDS which are Pending Approval  from ATTORNEY)
	 * @see com.blackbox.ids.core.dao.ids.IDSDao#getPendingApprovalRecordsCount()
	 */
	@Override
	public Long getPendingApprovalRecordsCount() {
		QIDS ids = QIDS.iDS;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		QUser user = QUser.user ;

		BooleanBuilder predicate = null;
		predicate = getPendingApprovalPredicate(ids, referenceFlow, user);
		JPAQuery query = getJPAQuery().from(referenceFlow, user).innerJoin(referenceFlow.ids, ids);
		
		return query.where(predicate).distinct().count();
	}
	
	/*
	 * (Get count of IDS records which are pending response from PARALLEGAL)
	 * @see com.blackbox.ids.core.dao.ids.IDSDao#getPendingResponseRecordsCount()
	 */
	@Override
	public Long getPendingResponseRecordsCount() {
		QUser user = QUser.user;
		QIDS ids = QIDS.iDS;
		QNotificationProcess notificationProcess = QNotificationProcess.notificationProcess;
		BooleanBuilder predicate = null;
		predicate = getPendingResponsePredicate(ids , notificationProcess , user);
		JPAQuery query = getJPAQuery().from(ids, user, notificationProcess);
		return query.where(predicate).distinct().count();
	}

}
