package com.blackbox.ids.dto.reference;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.correspondence.QCorrespondenceBase;
import com.blackbox.ids.core.model.mdm.QApplicationBase;
import com.blackbox.ids.core.model.notification.process.QNotificationProcess;
import com.blackbox.ids.core.model.reference.QReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.QReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.model.referenceflow.QIDSReferenceFlow;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowStatus;
import com.blackbox.ids.core.model.referenceflow.ReferenceFlowSubStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import com.mysql.jdbc.StringUtils;

public final class ReferencePredicate {
    
    private static final Logger log = Logger.getLogger(ReferencePredicate.class);
    
    private static final String US = "US";
    private static final String FOREIGN = "Foreign";
    private static final String NPL = "NPL";
    private static final String CITED_IN_IDS = "cited";
    private static final String UNCITED = "uncited";
    private static final String EXAMINER_CITED = "examinerCited";
    private static final String CITED_IN_PARENT = "citedInParent";
    private static final String DO_NOT_FILE = "doNotFile";
    private static final String DELETED = "deleted";
    
    private ReferencePredicate() throws InstantiationException {
        throw new InstantiationException("Predicate class: can't instantiate.");
    }
    
    public static Predicate getReferencePredicate(final Long loggedInUser, final String jurisdiction, final String dateRange) {
        
        QCorrespondenceBase correspondenceBaseData = QCorrespondenceBase.correspondenceBase;
        QApplicationBase applicationBaseData = QApplicationBase.applicationBase;
        
        BooleanBuilder predicate = new BooleanBuilder();
        
        Calendar startCalDate = null;
        Calendar endCalDate = null;
        
        try{
            Date startDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[0];
            Date endDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[1];
            startCalDate = BlackboxDateUtil.convertDateToCal(startDate);
            endCalDate = BlackboxDateUtil.convertDateToCal(endDate);
        } catch(Exception exception) {
            log.error("Reference Predicate :: Unable to parse Date passed in search filter."  + exception);
        }
        
        if(!StringUtils.isNullOrEmpty(jurisdiction)) {
            if (!"ALL".equalsIgnoreCase(jurisdiction)) {
                if ("US".equalsIgnoreCase(jurisdiction)) {
                    predicate.and(applicationBaseData.jurisdiction.code.equalsIgnoreCase(jurisdiction));
                } else {
                    predicate.and(applicationBaseData.jurisdiction.code.ne("US"));
                }
            }
        }
        
        if (startCalDate != null) {
            predicate.and(correspondenceBaseData.mailingDate.between(startCalDate, endCalDate));
        }
        
        if (loggedInUser != null) {
            predicate.and(correspondenceBaseData.createdByUser.eq(loggedInUser));
        }
        
        return predicate;
    }
    
    public static Predicate getUpdateReferencePredicate(String juris, String myRecords, String dateRange) {
        Calendar startCalDate = null;
        Calendar endCalDate = null;
        
        try{
            Date startDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[0];
            Date endDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[1];
            startCalDate = BlackboxDateUtil.convertDateToCal(startDate);
            endCalDate = BlackboxDateUtil.convertDateToCal(endDate);
        } catch(Exception exception) {
            log.error("Reference Predicate :: Unable to parse Date passed in search filter. " + exception);
        }
        
        QReferenceStagingData refData = QReferenceStagingData.referenceStagingData;
        QNotificationProcess np = QNotificationProcess.notificationProcess;
        BooleanBuilder predicate = new BooleanBuilder();
        
        if (Boolean.valueOf(myRecords)) {
            predicate.and(refData.createdByUser.eq(BlackboxSecurityContextHolder.getUserId()));
        }
        
        if (!"All".equalsIgnoreCase(juris)) {
            if ("US".equalsIgnoreCase(juris)) {
                predicate.and(refData.jurisdiction.code.equalsIgnoreCase(juris));
            } else {
                predicate.and(refData.jurisdiction.code.ne("US"));
            }
        }
        
        if (startCalDate != null) {
            predicate.and(np.updatedDate.between(startCalDate, endCalDate));
        }
        
        return predicate;
    }
    
    public static Predicate getDuplicateRefCheckPredicate(String juris, String myRecords, String dateRange) {
        Calendar startCalDate = null;
        Calendar endCalDate = null;
        
        try{
            Date startDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[0];
            Date endDate = BlackboxDateUtil.convertStringToDateRange(dateRange, TimestampFormat.MMMDDYYYY)[1];
            startCalDate = BlackboxDateUtil.convertDateToCal(startDate);
            endCalDate = BlackboxDateUtil.convertDateToCal(endDate);
        } catch(Exception exception) {
            log.error("Reference Predicate :: Unable to parse Date passed in search filter." + exception);
        }
        
        QReferenceBaseNPLData refData = QReferenceBaseNPLData.referenceBaseNPLData;
        QNotificationProcess np = QNotificationProcess.notificationProcess;
        BooleanBuilder predicate = new BooleanBuilder();
        
        if (Boolean.valueOf(myRecords)) {
            predicate.and(refData.createdByUser.eq(BlackboxSecurityContextHolder.getUserId()));
        }
        
        if (!"All".equalsIgnoreCase(juris)) {
            if ("US".equalsIgnoreCase(juris)) {
                predicate.and(refData.jurisdiction.code.equalsIgnoreCase(juris));
            } else {
                predicate.and(refData.jurisdiction.code.ne("US"));
            }
        }
        
        if (startCalDate != null) {
            predicate.and(np.updatedDate.between(startCalDate, endCalDate));
        }
        
        return predicate;
    }
    
	public static Predicate getReferenceFlowRulePredicate(String applicationNo, String jurisdiction, String attorneyDoc,
			String familyId, String uploadedOn, String docDesc, String uploadedBy) {
		
		QApplicationBase applicationBase = QApplicationBase.applicationBase;
		QUser user = QUser.user;
		
		BooleanBuilder predicate = new BooleanBuilder();
		
		Calendar uploadCalDate = null;
		
		try {
			Date uploadDate = BlackboxDateUtil.convertStringToDateRange(uploadedOn, TimestampFormat.MMMDDYYYY)[0];
			uploadCalDate = BlackboxDateUtil.convertDateToCal(uploadDate);
			
		} catch (Exception exception) {
			log.error("Reference Predicate :: Unable to parse Date passed in search filter."  + exception);
		}
		
		if (!StringUtils.isNullOrEmpty(applicationNo)) {
			predicate.and(applicationBase.applicationNumber.equalsIgnoreCase(applicationNo));
		}
		
		if (!StringUtils.isNullOrEmpty(jurisdiction)) {
			predicate.and(applicationBase.jurisdiction.code.equalsIgnoreCase(jurisdiction));
		}
		
		if (!StringUtils.isNullOrEmpty(attorneyDoc)) {
			predicate.and(applicationBase.attorneyDocketNumber.segment.equalsIgnoreCase(attorneyDoc));
		}
		
		if (!StringUtils.isNullOrEmpty(familyId)) {
			predicate.and(applicationBase.familyId.equalsIgnoreCase(familyId));
		}
		
		if (uploadCalDate != null) {
			predicate.and(applicationBase.createdDate.eq(uploadCalDate));
		}
		
		if (uploadedBy != null && !StringUtils.isNullOrEmpty(uploadedBy)) {
			predicate.and(user.person.firstName.startsWithIgnoreCase(uploadedBy));
		}
		
		if (!StringUtils.isNullOrEmpty(docDesc)) {
			predicate.and(applicationBase.description.equalsIgnoreCase(docDesc));
		}
		
		return predicate;
	}
	
	public static Predicate getIDSReferenceRecordPredicate(String familyId,String applicationNo,String filingDate, String jurisdiction, String refType,
			String activeTab, String status, String refFlowSubStatus) {
		
		QApplicationBase applicationBase = QApplicationBase.applicationBase;
		QIDSReferenceFlow referenceFlow = QIDSReferenceFlow.iDSReferenceFlow;
		
		//QUser user = QUser.user;
		
		BooleanBuilder predicate = new BooleanBuilder();
		
		Calendar calFilingDate = null;
		
		try {
			Date filingDates = BlackboxDateUtil.convertStringToDateRange(filingDate, TimestampFormat.MMMDDYYYY)[0];
			calFilingDate = BlackboxDateUtil.convertDateToCal(filingDates);
			
		} catch (Exception exception) {
			log.error("Reference Predicate :: Unable to parse Date passed in search filter."  + exception);
		}
		
		if (!StringUtils.isNullOrEmpty(applicationNo)) {
			predicate.and(referenceFlow.sourceApplication.applicationNumber.equalsIgnoreCase(applicationNo));
		}
		
		if (!StringUtils.isNullOrEmpty(jurisdiction)) {
			predicate.and(referenceFlow.referenceBaseData.jurisdiction.code.equalsIgnoreCase(jurisdiction));
		}
	
		if (!StringUtils.isNullOrEmpty(familyId)) {
			predicate.and(referenceFlow.sourceApplication.familyId.equalsIgnoreCase(familyId));
		}
		
		if (calFilingDate != null) {
			predicate.and(referenceFlow.createdDate.eq(calFilingDate));
		}
		
		if (!StringUtils.isNullOrEmpty(refType)){
			switch(refType){
				case US:
					predicate.and(referenceFlow.referenceBaseData.referenceType.eq((ReferenceType.US_PUBLICATION)).or(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.PUS)));
					break;
				case FOREIGN :
					predicate.and(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.FP));
					break;
				case NPL :
					predicate.and(referenceFlow.referenceBaseData.referenceType.eq(ReferenceType.NPL));
					break;
				default :
					predicate.and(referenceFlow.referenceBaseData.referenceType.ne(ReferenceType.NPL));
					break;
			}
		}
		
		if (!StringUtils.isNullOrEmpty(activeTab)){
			switch(activeTab){
				case CITED_IN_IDS:
					predicate.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.CITED));
					break;
				case UNCITED :
					predicate.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.UNCITED));
					break;
				case EXAMINER_CITED :
					predicate.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.EXAMINER_CITED));
					break;
				case CITED_IN_PARENT :
					predicate.and(referenceFlow.referenceFlowSubStatus.eq(ReferenceFlowSubStatus.CITED_IN_PARENT));
					break;
				case DO_NOT_FILE :
					predicate.and(referenceFlow.doNotFile.eq(true));
					break;
				case DELETED :
					predicate.and(referenceFlow.referenceFlowStatus.eq(ReferenceFlowStatus.DROPPED));
					break;
			}
		}
		
/*		if (refFlowStatus != null && !StringUtils.isNullOrEmpty(refFlowStatus)) {
			predicate.and(referenceFlow.referenceFlowStatus.eq(refFlowStatus));
		}
		
		if (!StringUtils.isNullOrEmpty(refFlowSubStatus)) {
			predicate.and(referenceFlow.referenceFlowSubStatus.eq(refFlowSubStatus));
		}*/
		
		return predicate;
	}
}
