package com.blackbox.ids.core.webcrawler.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.enums.ApplicationNumberStatus;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.OrganizationDetails;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.webcrawler.CreateApplicationQueue;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue;
import com.blackbox.ids.core.model.webcrawler.FindFamilyQueue.FamilyType;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.webcrawler.rowmapper.CreateApplicationQueueRowMapper;

@Repository
public class WebCrawlerApplicationDAOImpl implements IWebCrawlerApplicationDAO {

	@Autowired
	private NamedParameterJdbcTemplate	jdbcTemplate;

	@Autowired
	@Qualifier("sqlQueries")
	private Properties					sqlQueries;

	private static final Logger			LOGGER	= Logger.getLogger(WebCrawlerApplicationDAOImpl.class);

	@Override
	public Map<String, List<CreateApplicationQueue>> getCreateApplicationList() {
		LOGGER.debug("fetching Create-Application queue data.");
		String fetchApplicationQueueQuery = sqlQueries.getProperty("create.application.queue.fetch");
		Map<String, List<CreateApplicationQueue>> queryResult = jdbcTemplate.query(fetchApplicationQueueQuery,
				new ResultSetExtractor<Map<String, List<CreateApplicationQueue>>>() {

					@Override
					public Map<String, List<CreateApplicationQueue>> extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						Map<String, List<CreateApplicationQueue>> map = new HashMap<String, List<CreateApplicationQueue>>();
						CreateApplicationQueueRowMapper mapper = new CreateApplicationQueueRowMapper();

						while (rs.next()) {
							CreateApplicationQueue createRequest = mapper.mapRow(rs, rs.getRow());
							List<CreateApplicationQueue> applicationList = map.get(createRequest.getCustomerNumber());
							if (applicationList == null) {
								applicationList = new ArrayList<CreateApplicationQueue>();
								map.put(createRequest.getCustomerNumber(), applicationList);
							}

							applicationList.add(createRequest);
						}

						return map;
					}
				});
		LOGGER.debug(MessageFormat.format("Application queue record count : {0}", queryResult.values().size()));
		return queryResult;
	}

	@Override
	public Map<String, List<FindFamilyQueue>> getFindFamilyList(final FamilyType type) {
		LOGGER.debug("fetching Create-Application queue data.");
		String findFamilyQueueQuery = sqlQueries.getProperty("find.family.fetch");
		Map<String, String> params = new HashMap<String, String>();
		params.put("familyType", type.name());

		Map<String, List<FindFamilyQueue>> queryResult = jdbcTemplate.query(findFamilyQueueQuery, params,
				new ResultSetExtractor<Map<String, List<FindFamilyQueue>>>() {

					@Override
					public Map<String, List<FindFamilyQueue>> extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						Map<String, List<FindFamilyQueue>> map = new HashMap<String, List<FindFamilyQueue>>();

						while (rs.next()) {
							Long id = rs.getLong(1);
							String applicationNumber = rs.getString(2);
							String customer = rs.getString(3);
							String jurisdiction = rs.getString(4);
							java.util.Date filingDate = rs.getDate(5);
							String familyId = rs.getString(6);
							Calendar filingDateCal = BlackboxDateUtil.toCalendar(filingDate);
							int retryCount = rs.getInt(7);
							String applicationNumberRaw = rs.getString(9);
							FindFamilyQueue familyEntry = new FindFamilyQueue(id, applicationNumber, customer,
									jurisdiction, filingDateCal, familyId, type, null, retryCount);
							familyEntry.setAppNumberRaw(applicationNumberRaw);
							List<FindFamilyQueue> findFamilyList = map.get(customer);
							if (findFamilyList == null) {
								findFamilyList = new ArrayList<FindFamilyQueue>();
								map.put(customer, findFamilyList);
							}

							findFamilyList.add(familyEntry);
						}

						return map;
					}
				});
		LOGGER.debug(MessageFormat.format("Find family queue({0}) record count : {1}.", type, queryResult.values()
				.size()));
		return queryResult;
	}

	@Override
	public Map<String, Map<Long, List<FindFamilyQueue>>> getFindParentList() {
		LOGGER.debug("fetching Create-Application queue data.");
		String findParentQueueQuery = sqlQueries.getProperty("find.family.fetch");
		Map<String, String> params = new HashMap<String, String>();
		params.put("familyType", FamilyType.PARENT.name());

		Map<String, Map<Long, List<FindFamilyQueue>>> result = jdbcTemplate.query(findParentQueueQuery, params,
				new ResultSetExtractor<Map<String, Map<Long, List<FindFamilyQueue>>>>() {
					public Map<String, Map<Long, List<FindFamilyQueue>>> extractData(ResultSet rs) throws SQLException {
						Map<String, Map<Long, List<FindFamilyQueue>>> cutomerMap = new HashMap<String, Map<Long, List<FindFamilyQueue>>>();
						while (rs.next()) {
							Long id = rs.getLong(1);
							String applicationNumber = rs.getString(2);
							String customerNumber = rs.getString(3);
							String jurisdiction = rs.getString(4);
							java.util.Date filingDate = rs.getDate(5);
							String familyId = rs.getString(6);
							Calendar filingDateCal = BlackboxDateUtil.toCalendar(filingDate);
							int retryCount = rs.getInt(7);
							Long baseCaseId = rs.getLong(8);
							String applicationNumberRaw = rs.getString(9);
							if (baseCaseId == 0) {
								baseCaseId = null;
							}
							FindFamilyQueue element = new FindFamilyQueue(id, applicationNumber, customerNumber,
									jurisdiction, filingDateCal, familyId, FamilyType.PARENT, null, retryCount);
							element.setBaseCaseApplicationQueueId(baseCaseId);
							element.setAppNumberRaw(applicationNumberRaw);
							Map<Long, List<FindFamilyQueue>> baseCaseMap = cutomerMap.get(customerNumber);

							if (baseCaseMap == null) {
								baseCaseMap = new HashMap<Long, List<FindFamilyQueue>>();
								cutomerMap.put(customerNumber, baseCaseMap);
							}

							List<FindFamilyQueue> applicationList = baseCaseMap.get(baseCaseId);
							if (applicationList == null) {
								applicationList = new ArrayList<FindFamilyQueue>();
								baseCaseMap.put(baseCaseId, applicationList);
							}
							applicationList.add(element);
						}
						return cutomerMap;
					};
				});
		LOGGER.debug(MessageFormat.format("Application queue record count :{0}", result.size()));
		return result;
	}

	@Override
	@Transactional
	public void updateFamilyStatus(final Long baseCaseId, final String familyId, final QueueStatus status) {
		String query = sqlQueries.getProperty("find.parent.queue.family.update");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("familyId", familyId);
		params.put("baseCaseId", baseCaseId);
		params.put("status", status.name());
		params.put("systemUser", User.SYSTEM_ID.toString());
		jdbcTemplate.update(query, params);
	}

	@Override
	@Transactional
	public void updateCreateApplicationQueueStatus(final Long id, final QueueStatus status, final int retryCount) {
		LOGGER.info("Updating status and retry count for Create-Application-Queue record");
		String query = sqlQueries.getProperty("create.application.queue.update");

		Map<String, String> params = new HashMap<String, String>();
		params.put("retryCount", String.valueOf(retryCount));
		params.put("id", id.toString());
		params.put("status", status.name());
		params.put("systemUser", User.SYSTEM_ID.toString());
		jdbcTemplate.update(query, params);
	}

	@Override
	@Transactional
	public void updateFamilyApplicationsStatus(final Long baseCaseId) {
		LOGGER.info(MessageFormat.format(
				"Updating error status for all the family applications[BaseCase Id :{0}] in Create-Application-Queue",
				baseCaseId));
		String query = sqlQueries.getProperty("create.application.queue.family.update");

		Map<String, String> params = new HashMap<String, String>();
		params.put("baseCaseId", baseCaseId.toString());
		params.put("status", QueueStatus.CRAWLER_ERROR.name());
		params.put("systemUser", User.SYSTEM_ID.toString());
		jdbcTemplate.update(query, params);
	}

	@Override
	@Transactional
	public void updateApplicationCrawlerErrorStatus(Long id, QueueStatus status, int retryCount) {
		String query = sqlQueries.getProperty("create.application.queue.update.retry");
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id.toString());
		params.put("status", status.name());
		params.put("retryCount", String.valueOf(retryCount));
		jdbcTemplate.update(query, params);
	}

	@Override
	@Transactional
	public Long createApplicationStageEntry(ApplicationStage app) {
		LOGGER.info("Creating entry in Application-Stage table.");
		PatentDetails patent = app.getPatentDetails();
		PublicationDetails publication = app.getPublicationDetails();
		ApplicationDetails appDetails = app.getAppDetails();
		OrganizationDetails organization = app.getOrganizationDetails();

		String insert = sqlQueries.getProperty("application.stage.insert");
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("systemUser", User.SYSTEM_ID.toString());
		parameters.addValue("applicationNumberRaw", appDetails.getApplicationNumberRaw());
		parameters.addValue("childApplicationType", getEnumValue(appDetails.getChildApplicationType()));
		parameters.addValue("confirmationNumber", appDetails.getConfirmationNumber());
		parameters.addValue("filingDate", getDate(appDetails.getFilingDate()));
		parameters.addValue("applicationNumber", app.getApplicationNumber());
		parameters.addValue("familyId", app.getFamilyId());
		parameters.addValue("entity", getEnumValue(organization.getEntity()));
		parameters.addValue("exportControl", organization.isExportControl());
		parameters.addValue("prosecutionStatus", getEnumValue(organization.getProsecutionStatus()));
		parameters.addValue("artUnit", patent.getArtUnit());
		parameters.addValue("examiner", patent.getExaminer());
		parameters.addValue("firstNameInventor", patent.getFirstNameInventor());
		parameters.addValue("issuedOn", getDate(patent.getIssuedOn()));
		parameters.addValue("patentNumberRaw", patent.getPatentNumberRaw());
		parameters.addValue("title", patent.getTitle());
		parameters.addValue("publicationNumber", publication.getPublicationNumber());
		parameters.addValue("publicationNumberRaw", publication.getPublicationNumberRaw());
		parameters.addValue("publishedOn", getDate(publication.getPublishedOn()));
		parameters.addValue("source", getEnumValue(app.getSource()));
		parameters.addValue("assignee", app.getAssignee());
		parameters.addValue("attorneyDocket", app.getAttorneyDocketNumber());
		parameters.addValue("customer", app.getCustomer());
		parameters.addValue("jurisdiction", app.getJurisdiction());
		parameters.addValue("organization", app.getOrganization());
		parameters.addValue("subSource", getEnumValue(app.getSubSource()));
		parameters.addValue("status", app.getStatus().name());
		parameters.addValue("patentNumber", patent.getPatentNumber());
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(insert, parameters, keyHolder, new String[] { "ID" });
			return keyHolder.getKey().longValue();
		} catch (DuplicateKeyException e) {
			LOGGER.info("Application already exists in apllication stage table[Application : "
					+ appDetails.getApplicationNumberRaw() + "].");
			return null;
		}
	}

	@Override
	@Transactional
	public void createFindFamilyEntry(FindFamilyQueue findParentEntry) {
		String findFamilyInsert = sqlQueries.getProperty("find.family.queue.insert");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("systemUser", BlackboxSecurityContextHolder.getUserId());
		params.put("applicationNumber", findParentEntry.getApplicationNumberFormatted());
		params.put("applicationNumberRaw", findParentEntry.getAppNumberRaw());
		params.put("filingDate", getDate(findParentEntry.getFilingDate()));
		params.put("currentDate", new Timestamp(Calendar.getInstance().getTimeInMillis()));
		params.put("jurisdiction", findParentEntry.getJurisdictionCode());
		params.put("customer", findParentEntry.getCustomerNumber());
		params.put("status", getEnumValue(findParentEntry.getStatus()));
		params.put("familyType", getEnumValue(findParentEntry.getType()));
		params.put("familyId", findParentEntry.getFamilyId());
		params.put("applicationStageId", findParentEntry.getApplicationStageId());
		params.put("baseCaseId", findParentEntry.getBaseCaseApplicationQueueId());
		jdbcTemplate.update(findFamilyInsert, params);
	}

	@Override
	@Transactional
	public void createDownloadOfficeActionEntry(DownloadOfficeActionQueue downloadOfficeAction) {
		String insert = sqlQueries.getProperty("download.office.action.queue.insert");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("systemUser", User.SYSTEM_ID.toString());
		params.put("applicationNumber", downloadOfficeAction.getApplicationNumberFormatted());
		params.put("applicationNumberRaw", downloadOfficeAction.getAppNumberRaw());
		params.put("filingDate", getDate(downloadOfficeAction.getFilingDate()));
		params.put("jurisdiction", downloadOfficeAction.getJurisdictionCode());
		params.put("customer", downloadOfficeAction.getCustomerNumber());
		params.put("status", getEnumValue(downloadOfficeAction.getStatus()));
		params.put("correspondenceCode", downloadOfficeAction.getDocumentCode());
		params.put("retryCount", downloadOfficeAction.getRetryCount());
		jdbcTemplate.update(insert, params);
	}

	@Override
	public boolean checkApplicationInExclustionList(String applicationNumber, String jurisdiction) {
		String query = sqlQueries.getProperty("exclusionlist.application.fetch");
		Map<String, String> params = new HashMap<String, String>();
		params.put("applicationNumber", applicationNumber);
		params.put("jurisdiction", jurisdiction);
		int count = jdbcTemplate.queryForObject(query, params, Integer.class);
		return count != 0;
	}

	@Override
	public void createInclusionListEntry(String applicationNumber, String applicationNumberRaw, String jurisdiction,
			String customer, Calendar filingDate) {
		// check if active entry of application in inclusion list exists
		String query = sqlQueries.getProperty("inclusionlist.application.fetch");
		Map<String, String> params = new HashMap<String, String>();
		params.put("applicationNumber", applicationNumber);
		params.put("jurisdiction", jurisdiction);
		params.put("activeStatus", ApplicationNumberStatus.ACTIVE.name());
		int count = jdbcTemplate.queryForObject(query, params, Integer.class);

		// if active entry not found for the application
		if (count == 0) {
			// try update inactive entry
			int rowsUpdated = updateInclusionListEntry(applicationNumber, jurisdiction);

			// if inactive entry not found, insert a new entry in inclusion list
			if (rowsUpdated == 0) {
				insertInclusionListEntry(applicationNumber, applicationNumberRaw, jurisdiction, customer, filingDate);
			}
		}
	}

	@Transactional
	private int updateInclusionListEntry(String applicationNumber, String jurisdiction) {
		String query = sqlQueries.getProperty("inclusionlist.application.update");
		Map<String, String> params = new HashMap<String, String>();
		params.put("applicationNumber", applicationNumber);
		params.put("jurisdiction", jurisdiction);
		params.put("modifiedBy", User.SYSTEM_ID.toString());
		params.put("activeStatus", ApplicationNumberStatus.ACTIVE.name());
		params.put("inactiveStatus", ApplicationNumberStatus.INACTIVE.name());
		return jdbcTemplate.update(query, params);
	}

	@Transactional
	private void insertInclusionListEntry(String applicationNumber, String applicationNumberRaw, String jurisdiction,
			String customer, Calendar filingDate) {
		String inclusionListInsert = sqlQueries.getProperty("inclusionlist.application.insert");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applicationNumber", applicationNumber);
		params.put("applicationNumberRaw", applicationNumberRaw);
		params.put("jurisdiction", jurisdiction);
		params.put("customerNumber", customer);
		params.put("filingDate", getDate(filingDate));
		params.put("status", ApplicationNumberStatus.ACTIVE.name());
		params.put("systemUser", User.SYSTEM_ID.toString());
		jdbcTemplate.update(inclusionListInsert, params);
	}

	@Override
	@Transactional
	public void updateFindFamilyQueueStatus(String ids, QueueStatus status) {
		String query = sqlQueries.getProperty("find.family.queue.status.update");
		Map<String, String> params = new HashMap<String, String>();
		params.put("ids", ids);
		params.put("status", status.name());
		params.put("systemUser", User.SYSTEM_ID.toString());
		jdbcTemplate.update(query, params);
	}

	@Override
	@Transactional
	public void updateFindFamilyQueueStatus(Long id, QueueStatus status) {
		updateFindFamilyQueueStatus(String.valueOf(id), status);
	}

	@Override
	@Transactional
	public Long createApplicationRequestEntry(CreateApplicationQueue app) {
		String insert = sqlQueries.getProperty("create.application.queue.insert");

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("applicationNumber", app.getApplicationNumberFormatted());
		parameters.addValue("applicationNumberRaw", app.getAppNumberRaw());
		parameters.addValue("baseApplicationQueueId", app.getBaseCaseApplicationQueueId());
		parameters.addValue("jurisdiction", app.getJurisdictionCode());
		parameters.addValue("customer", app.getCustomerNumber());
		parameters.addValue("filingDate", getDate(app.getFilingDate()));
		parameters.addValue("status", QueueStatus.INITIATED.name());
		parameters.addValue("retryCount", app.getRetryCount());
		parameters.addValue("familyId", app.getFamilyId());
		parameters.addValue("systemUser", BlackboxSecurityContextHolder.getUserId());

		jdbcTemplate.update(insert, parameters, keyHolder, new String[] { "ID" });
		return keyHolder.getKey().longValue();
	}

	@Override
	public String findJurisdictionByCountry(final String country) {
		String query = sqlQueries.getProperty("find.jurisdiction.by.country");
		Map<String, String> params = new HashMap<String, String>();
		params.put("country", country);
		return queryForObject(query, params, String.class);
	}

	@Override
	public CreateApplicationQueue getCreateApplicationQueueEntry(final Long id) {
		LOGGER.debug("fetching Create-Application queue entry for id :" + id);
		String fetchApplicationQueueQuery = sqlQueries.getProperty("create.application.queue.fetch.by.id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return jdbcTemplate.queryForObject(fetchApplicationQueueQuery, params, new CreateApplicationQueueRowMapper());
	}

	@Override
	public boolean checkIfApplicationQueueEntryExists(final String applicationNumber, final String jurisdiction,
			final Long baseCaseId) {
		LOGGER.debug(MessageFormat
				.format("Checking if Create-Application Queue entry already exists for (application number :{0} , jurisdiction : {1})",
						applicationNumber, jurisdiction));
		String fetchApplicationQueueQuery = sqlQueries.getProperty("create.application.queue.fetch.by.application");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("applicationNumber", applicationNumber);
		params.put("jurisdiction", jurisdiction);
		params.put("baseCaseId", baseCaseId);
		int count = jdbcTemplate.queryForObject(fetchApplicationQueueQuery, params, Integer.class);

		return count > 0;
	}
	
	@Override
	public Long getFindParentEntityByApplicationStage(final long applicationStageId) {
		LOGGER.debug(MessageFormat.format("fetching find-parent entry by application stage(id:{0}).",
				applicationStageId));
		String query = sqlQueries.getProperty("find.parent.fetch.by.applicationStage");
		Map<String, Long> params = new HashMap<>();
		params.put("applicationStageId", applicationStageId);
		return jdbcTemplate.queryForObject(query, params, Long.class);
	}

	/**
	 * Get list of application numbers from create application queue for validation
	 * @param customerNos
	 */
	public List<CreateApplicationQueue> getCreateApplicationQueueList(List<String> customerNos) {
		LOGGER.debug("fetching Create-Application queue data.");
		Map<String, Object> params = new HashMap<String, Object>();
		String fetchApplicationQueueQuery = sqlQueries.getProperty("create.application.queue.aplicationnumbers.fetch");
		params.put("customerNos", customerNos);
		List<CreateApplicationQueue> queryResult = jdbcTemplate.query(fetchApplicationQueueQuery, params,
				new RowMapper<CreateApplicationQueue>() {

					@Override
					public CreateApplicationQueue mapRow(ResultSet rs, int rownumber) throws SQLException {
						Long id = rs.getLong(1);
						String appNoRaw = rs.getString(2);
						String applicationNumber = rs.getString(3);
						String customerNumber = rs.getString(4);
						String jurisdiction = rs.getString(5);
						java.util.Date filingDate = rs.getDate(6);
						String correspondanceLink = rs.getString(7);
						int retryCount = rs.getInt(8);
						java.util.Date modifiedOn = rs.getDate(9);
						Calendar filingDateCal = BlackboxDateUtil.toCalendar(filingDate);
						Calendar modifiedDateCal = BlackboxDateUtil.toCalendar(modifiedOn);
						CreateApplicationQueue createApplicationQueueObj = new CreateApplicationQueue(id, applicationNumber, customerNumber, jurisdiction,
								filingDateCal, correspondanceLink, retryCount, modifiedDateCal);
						createApplicationQueueObj.setAppNumberRaw(appNoRaw);
						return createApplicationQueueObj;
					}
				});
		LOGGER.debug("Application queue record count :" + queryResult.size());
		return queryResult;
	}

	private <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> requiredType) {
		try {
			return jdbcTemplate.queryForObject(sql, paramMap, requiredType);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private Date getDate(Calendar calendar) {
		Date date = null;
		if (calendar != null) {
			date = new Date(calendar.getTimeInMillis());
		}
		return date;
	}

	private String getEnumValue(@SuppressWarnings("rawtypes") Enum obj) {
		String result = null;
		if (obj != null) {
			result = obj.name();
		}
		return result;
	}

}
