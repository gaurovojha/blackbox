package com.blackbox.ids.core.dao.ids.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.ids.IDashboardDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.IDS.dashboard.InitiateIDSRecordDTO;
import com.blackbox.ids.core.model.IDS.IDS;
import com.blackbox.ids.core.model.IDS.IDSTrigger;
import com.blackbox.ids.core.repository.IDSTriggerRepository;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository
public class DashboardDAOImpl extends BaseDaoImpl<IDS, Long>implements IDashboardDAO{

	private static final String TRIGGER1 = "TRIGGER1";
	private static final String TRIGGER2 = "TRIGGER2";
	private static final String TRIGGER3 = "TRIGGER3";
	private static final String TRIGGER4 = "TRIGGER4";
	private static final String TRIGGER5 = "TRIGGER5";
	private static final String ACTIVE = "ACTIVE";
	
	private static final String QUERY_FOR_TRIGGER1= "idsRefFlow.targetApplication in (select irf.targetApplication from IDSReferenceFlow irf where irf.referenceFlowStatus in ('UNCITED') group by irf.targetApplication having count(irf.referenceFlowStatus)>=1)"
			+ "and app.idsRelevantStatus in ('Issue notification received', 'Issue Fee paid', 'Notice of Allowance Received', 'RCE filed', 'Advisory OA received', 'Final OA received', 'Ex-parte Quayle OA received') ";
	
	private static final String QUERY_FOR_TRIGGER2= "idsRefFlow.targetApplication in (select irf.targetApplication from IDSReferenceFlow irf where irf.referenceFlowStatus in ('UNCITED') group by irf.targetApplication having count(irf.referenceFlowStatus)>=1"
			+"and app.OAResponseDate is not null and app.OAResponseDate > idsRefFlow.filingDate and app.OAResponse in"
			+"('Amendment/Req. Reconsideration-After Non-Final Reject', 'Amendment after Notice of Allowance (Rule 312)'," 
			+"'Response After Final Action', 'Preliminary Amendment', 'Response after Ex Parte Quayle Action'," 
			+"'Amendment Submitted/Entered with Filing of CPA/RCE', 'Response to Election / Restriction Filed'," 
			+"'Applicant summary of interview with examiner', 'Request for Continued Examination (RCE)'," 
			+"'Applicant Arguments/Remarks Made in an Amendment')";
	
	private static final String QUERY_FOR_TRIGGER3= "dateadd(DAY, -60, getdate()) >= idsRefFlow.createdDate";
	
	private static final String QUERY_FOR_TRIGGER4= "dateadd(DAY, -75, getdate()) >= app.filingDate and NOT EXISTS (select in.applicationId from internal in where in.applicationId=idsRefFlow.targetApplication) "
			+"and NOT EXISTS (select ex.applicationId from external ex where ex.applicationId=idsRefFlow.targetApplication)";
	
	private static final String QUERY_FOR_TRIGGER5= "(dateadd(DAY, -75, getdate()) >= app.filingDate) and (EXISTS (select in.applicationId from internal in where in.applicationId=idsRefFlow.targetApplication) "
			+"or EXISTS (select ex.applicationId from external ex where ex.applicationId=idsRefFlow.targetApplication)) "
			+"and irf.referenceFlowStatus in ('UNCITED') and irf.referenceFlowSubStatus in ('CITED_IN_PARENT')";
	
	
	@Autowired
	private NamedParameterJdbcTemplate	jdbcTemplate;

	@Autowired
	private IDSTriggerRepository idsTriggerRepository;
	
	@Autowired
	@Qualifier("idsSqlQueries")
	private Properties	idsSqlQueries;

	private static final Logger	LOGGER	= Logger.getLogger(DashboardDAOImpl.class);
	
	@Override
	public List<InitiateIDSRecordDTO> getUrgentApplicationsInIDS(){
		JPAQuery query = getJPAQuery();
		LOGGER.debug("fetching Create-Application queue data.");
		StringBuilder queryForAllIdsApplications = new StringBuilder(idsSqlQueries.getProperty("all.ids.application.fetch"));
		StringBuilder triggerQuery = getQueryForTriggers();
		queryForAllIdsApplications.append(triggerQuery);
				
		List<InitiateIDSRecordDTO> queryResult = jdbcTemplate.query(queryForAllIdsApplications.toString(), new RowMapper<InitiateIDSRecordDTO>() {

					@Override
					public InitiateIDSRecordDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
						Long id = rs.getLong(1);
						String familyId = rs.getString(2);
						String jurisdiction = rs.getString(3);
						String applicationNumber = rs.getString(4);
						java.util.Date filingDate = rs.getDate(5);
						long uncitedReferences = rs.getLong(6);
						String prosecutionStatus = rs.getString(7);
						long noOfDaysLastOAFiled = rs.getLong(8);
						long lastOAResponse = rs.getLong(9);
						Calendar filingDateCal = BlackboxDateUtil.toCalendar(filingDate);
						
						InitiateIDSRecordDTO urgentRecordDTO = new InitiateIDSRecordDTO(id,familyId, jurisdiction, applicationNumber,
								filingDateCal, uncitedReferences, noOfDaysLastOAFiled, prosecutionStatus, lastOAResponse);
						return urgentRecordDTO;
					}
				});
		LOGGER.debug("Application queue record count :" + queryResult.size());
		return queryResult;
		
	}
	
	private StringBuilder getQueryForTriggers(){
		
		List<IDSTrigger> triggers = idsTriggerRepository.findAll();
		StringBuilder condition = new StringBuilder();
		int conditionCount = 0;
		for(IDSTrigger trigger:triggers){
			String name = trigger.getTriggerTypeName().name();
			if(trigger.getStatus().equals(ACTIVE)){
				if(conditionCount>0){
					condition.append("or");
				}else{
					condition.append("and");
				}
				condition.append("(");
				switch(name){
					case TRIGGER1: 
							condition.append(QUERY_FOR_TRIGGER1);
							break;		
					case TRIGGER2: 
							condition.append(QUERY_FOR_TRIGGER2);
							break;
					case TRIGGER3: 
							condition.append(QUERY_FOR_TRIGGER3);
							break;
					case TRIGGER4: 
							condition.append(QUERY_FOR_TRIGGER4);
							break;
					case TRIGGER5:
							condition.append(QUERY_FOR_TRIGGER5);
							break;
				}
				condition.append(")");
				conditionCount++;
			}
		}
		return condition;
	}

	public List<InitiateIDSRecordDTO> getAllApplicationsInIDS() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
