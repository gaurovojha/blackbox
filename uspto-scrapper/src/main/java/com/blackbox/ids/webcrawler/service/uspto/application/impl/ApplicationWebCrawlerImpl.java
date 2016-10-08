package com.blackbox.ids.webcrawler.service.uspto.application.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.dto.mdm.dashboard.MdmRecordDTO;
import com.blackbox.ids.core.model.mdm.Application.Source;
import com.blackbox.ids.core.model.mdm.Application.SubSource;
import com.blackbox.ids.core.model.mdm.ApplicationDetails;
import com.blackbox.ids.core.model.mdm.ApplicationStage;
import com.blackbox.ids.core.model.mdm.ApplicationType;
import com.blackbox.ids.core.model.mdm.OrganizationDetails;
import com.blackbox.ids.core.model.mdm.PatentDetails;
import com.blackbox.ids.core.model.mdm.PublicationDetails;
import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.model.mstr.Assignee.Entity;
import com.blackbox.ids.core.model.mstr.ProsecutionStatus;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.webcrawler.service.uspto.application.IApplicationWebCrawler;

@Component
public class ApplicationWebCrawlerImpl implements IApplicationWebCrawler {

	private static final Logger LOGGER = Logger.getLogger(ApplicationWebCrawlerImpl.class);

	private static final String	FILED_ON_TEXT	= "filed on ";
	private static final String	BLANK_SPACE	= " ";
	private static final String UPDATE = "Update";
	private static final String ALL_INVENTORS = "all inventors";

	private static final String	SELECTOR_TR_ID_ROW	= "tr[id*='row']";
	private static final String	SELECTOR_FOREIGN_PRIORITY_TABLE	= "td#forpriority div div:nth-child(2) table";
	private static final String	SELECTOR_FOREIGN_PRIORITY_TAB	= "a[href*='foreignPrioritiestab']";
	private static final String	SELECTOR_PARENT_DATA_ROW	= "tr[id='parentdata0']";
	private static final String	SELECTOR_PARENT_DATA	= "continuityparent";
	private static final String	SELECTOR_TABLE_COLUMN	= "td";
	private static final String	SELECTOR_TABLE_ROW	= "tr";
	private static final String	SELECTOR_CHILD_DATA	= "continuitychild";
	private static final String	SELECTOR_CONTINUITY_CHILD_DATA	= "childcntdatarecNF";
	private static final String	SELECTOR_CONTINUITY_PARENT_DATA	= "table#continuityparent tr#pcdthrow:nth-child(2) td";
	private static final String	SELECTOR_CONTINUITYTAB	= "a[href*='continuitytab']";
	private static final String	SELECTOR_SUBMIT_PAIR	= "SubmitPAIR";
	private static final String	SELECTOR_APPLICATION_NUMBER_INPUT	= "number_id";
	private static final String	SELECTOR_PCTSEARCH_RADIOBUTTON	= "pctsearch_number_radiobutton";
	private static final String	SELECTOR_APPLICATION_NUMBER_RADIOBUTTON	= "application_number_radiobutton";
	private static final String	SELECTOR_NEW_CASE	= "a[href*=pair_search]";
	private static final String	SELECTOR_ID_ERRORDIV	= "[id^='ERRORDIV']";
	private static final String	SELECTOR_APP_INVENTION_TXT	= "table#bibviewTitle tr td:nth-child(2)";
	private static final String	SELECTOR_CORRESPONDENCE_TXT	= "td[headers=r1c2] table tr td";
	private static final String	SELECTOR_APP_DATA_REGEX	= "td[headers=r{0}c{1}]";
	
	private static final String XPATH_CUSTOMER_NUMBER = "//*[@id='bibview']/tbody/tr[1]/td[4]/table/tbody/tr/td[1]";
	private static final String XPATH_FILING_DATE = "//*[@id='bibview']/tbody/tr[2]/td[2]";
	private static final String XPATH_STATUS = "//*[@id='bibview']/tbody/tr[2]/td[4]";
	private static final String XPATH_EXAMINER_NAME = "//*[@id='bibview']/tbody/tr[4]/td[2]";
	private static final String XPATH_GROUP_ART_UNIT = "//*[@id='bibview']/tbody/tr[5]/td[2]";
	private static final String XPATH_CONFIRMATION_NO = "//*[@id='bibview']/tbody/tr[6]/td[2]";
	private static final String XPATH_A_DOCKET_NO = "//*[@id='bibview']/tbody/tr[7]/td[2]";
	private static final String XPATH_PUBLICATION_NUMBER = "//*[@id='bibview']/tbody/tr[6]/td[4]";
	private static final String XPATH_PUBLICATION_DATE = "//*[@id='bibview']/tbody/tr[7]/td[4]";
	private static final String XPATH_PATENT_NUMBER = "//*[@id='bibview']/tbody/tr[8]/td[4]";
	private static final String XPATH_PATENT_ISSUE_DATE = "//*[@id='bibview']/tbody/tr[9]/td[4]";
	private static final String XPATH_FIRST_INVENTOR = "//*[@id='bibview']/tbody/tr[9]/td[2]";
	private static final String XPATH_ENTITY_STATUS = "//*[@id='bibview']/tbody/tr[11]/td[2]";
	private static final String XPATH_TITLE_INVENTION = "//*[@id='bibviewTitle']/tbody/tr/td[2]";
	private static final String XPATH_ERROR = "//*[@id='ERRORDIV']/table/tbody/tr/td";

	@Override
	public void selectNewCase(final WebDriver webDriver) {
		By cssSelector = By.cssSelector(SELECTOR_NEW_CASE);
		if (BBWebCrawlerUtils.isElementPresent(webDriver, cssSelector)) {
			webDriver.findElement(cssSelector).click();
			BBWebCrawlerUtils.waitForSpecifiedDuration(5, webDriver);
		}
	}

	@Override
	public boolean checkApplicationDetailsPageStatus(final WebDriver webDriver, final String appNumber,
			String jurisdiction, Calendar filingDate) {
		boolean isFindApplicationSuccess = true;
		boolean isErrorPresent = BBWebCrawlerUtils.isElementPresent(webDriver, By.cssSelector(SELECTOR_ID_ERRORDIV));

		if (isErrorPresent) {
			LOGGER.info("Application details not found.");
			isFindApplicationSuccess = false;
			if (BBWebCrawerConstants.WO_CODE.equals(jurisdiction)) {
				// try with different application number format for PCT
				String applicationNumber = BBWebCrawlerUtils.convertToOtherForm(appNumber, filingDate);

				if (StringUtils.isNotEmpty(applicationNumber)) {
					LOGGER.info("Trying with different format :" + applicationNumber);
					searchApplicationNumber(webDriver, applicationNumber, jurisdiction);
					isErrorPresent = BBWebCrawlerUtils.isElementPresent(webDriver, By.cssSelector(SELECTOR_ID_ERRORDIV));
					if (!isErrorPresent) {
						LOGGER.info("Application details still not found.");
						isFindApplicationSuccess = true;
					}
				} else {
					LOGGER.error("Invalid application number format " + applicationNumber);
				}
			}
		}
		return isFindApplicationSuccess;
	}

	@Override
	public void searchApplicationNumber(final WebDriver webDriver, final String applicationNumber,
			final String jurisdiction) {
		LOGGER.info("Searching application number..");

		// choose application type
		if (BBWebCrawerConstants.US_CODE.equals(jurisdiction)) {
			BBWebCrawlerUtils.webClick(By.id(SELECTOR_APPLICATION_NUMBER_RADIOBUTTON), webDriver);
		} else {
			BBWebCrawlerUtils.webClick(By.id(SELECTOR_PCTSEARCH_RADIOBUTTON), webDriver);
		}

		// enter application number
		webDriver.findElement(By.id(SELECTOR_APPLICATION_NUMBER_INPUT)).sendKeys(applicationNumber);

		// click search
		webDriver.findElement(By.id(SELECTOR_SUBMIT_PAIR)).click();
		BBWebCrawlerUtils.waitForSpecifiedDuration(10, webDriver);
	}

	@Override
	public ApplicationStage readApplicationDetails(final WebDriver webDriver) {
		try {
			String applicationNumber = getText(webDriver, getApplicationDataSelector(1, 1));
			String filingDateTxt = getText(webDriver, getApplicationDataSelector(2, 1));

			String examinerName = getText(webDriver, getApplicationDataSelector(4, 1));
			String groupArtUnit = getText(webDriver, getApplicationDataSelector(5, 1));
			String confirmationNumber = getText(webDriver, getApplicationDataSelector(6, 1));
			String attorneyDocketNumber = getText(webDriver, getApplicationDataSelector(7, 1));
			attorneyDocketNumber = StringUtils.removeEndIgnoreCase(attorneyDocketNumber, UPDATE);
			attorneyDocketNumber = StringUtils.trimToNull(attorneyDocketNumber);

			String firstNamedInventor = getText(webDriver, getApplicationDataSelector(9, 1));
			firstNamedInventor = StringUtils.removeEndIgnoreCase(firstNamedInventor, ALL_INVENTORS);
			String entityStatus = getText(webDriver, getApplicationDataSelector(11, 1));
			entityStatus = StringUtils.removeEndIgnoreCase(entityStatus, UPDATE);
			entityStatus = StringUtils.trimToNull(entityStatus);

			String correspondenceAddressCustomerNumber = getText(webDriver, SELECTOR_CORRESPONDENCE_TXT);
			String status = getText(webDriver, getApplicationDataSelector(2, 2));
			String publicationNo = getText(webDriver, getApplicationDataSelector(6, 2));
			if (publicationNo != null) {
				String[] publicationTxt = StringUtils.split(publicationNo, BLANK_SPACE);
				publicationNo = publicationTxt[1];
			}
			String publicationDate = getText(webDriver, getApplicationDataSelector(7, 2));
			String patentNumber = getText(webDriver, getApplicationDataSelector(8, 2));
			String patentIssueDate = getText(webDriver, getApplicationDataSelector(9, 2));
			String inventionTitle = getText(webDriver, SELECTOR_APP_INVENTION_TXT);

			Calendar issuedOn = BlackboxDateUtil.getCalendar(patentIssueDate);
			Calendar publishedOn = BlackboxDateUtil.getCalendar(publicationDate);
			Calendar filingDate = BlackboxDateUtil.getCalendar(filingDateTxt);

			ProsecutionStatus prosecutionStatus = null;
			if (StringUtils.containsIgnoreCase(status, ProsecutionStatus.ABANDONED.name())) {
				prosecutionStatus = ProsecutionStatus.ABANDONED;
			}

			PatentDetails patentDetails = new PatentDetails(firstNamedInventor, examinerName, groupArtUnit,
					inventionTitle, patentNumber, issuedOn);
			PublicationDetails publicationDetails = new PublicationDetails(publicationNo, null, publishedOn);
			ApplicationDetails appDetails = new ApplicationDetails(applicationNumber, ApplicationType.FIRST_FILING,
					confirmationNumber, filingDate);
			OrganizationDetails organizationDetails = new OrganizationDetails(prosecutionStatus, null, true,
					Entity.fromString(entityStatus));

			return new ApplicationStage(null, Assignee.NAME_DEFAULT_ASSIGNEE, correspondenceAddressCustomerNumber, null,
					attorneyDocketNumber, null, Constant.DEFAULT_FAMILY, appDetails, publicationDetails, patentDetails,
					organizationDetails, Source.AUTOMATIC, SubSource.USPTO, null);
		} catch (Exception e) {
			LOGGER.error("Error while scraping application details from the web page.", e);
			return null;
		}
	}

	private String getApplicationDataSelector(int row, int col) {
		return MessageFormat.format(SELECTOR_APP_DATA_REGEX, row, col);
	}

	@Override
	public boolean checkContinuityTab(final WebDriver webDriver, final boolean isParents) {
		boolean isContinuityDataPresent = true;
		By continuityTabCssSelector = By.cssSelector(SELECTOR_CONTINUITYTAB);

		boolean isContinuityTabPresent = BBWebCrawlerUtils.isElementPresent(webDriver, continuityTabCssSelector);
		if (isContinuityTabPresent) {
			webDriver.findElement(continuityTabCssSelector).click();
			BBWebCrawlerUtils.waitForSpecifiedDuration(10, webDriver);

			By dataSelector = null;
			if (isParents) {
				dataSelector = By.cssSelector(SELECTOR_CONTINUITY_PARENT_DATA);
			} else {
				dataSelector = By.id(SELECTOR_CONTINUITY_CHILD_DATA);
			}
			boolean isNoDataFoundElementPresent = BBWebCrawlerUtils.isElementPresent(webDriver, dataSelector);
			if (isNoDataFoundElementPresent) {
				isContinuityDataPresent = false;
			}
		} else {
			isContinuityDataPresent = false;
		}

		return isContinuityDataPresent;
	}

	@Override
	public Map<String, String> readChildApplications(final WebDriver webDriver) {
		Map<String, String> childApplications = new HashMap<String, String>();
		WebElement baseTable = webDriver.findElement(By.id(SELECTOR_CHILD_DATA));
		List<WebElement> tableRows = baseTable.findElements(By.tagName(SELECTOR_TABLE_ROW));

		for (WebElement childRow : tableRows) {
			String text = childRow.findElement(By.tagName(SELECTOR_TABLE_COLUMN)).getText();
			String[] data = StringUtils.split(text, FILED_ON_TEXT);
			String childAppNo = data[0];
			String filedOn = StringUtils.substringBefore(data[1], BLANK_SPACE);
			childApplications.put(childAppNo, filedOn);
		}
		return childApplications;
	}

	@Override
	public Map<String, String> readParentApplications(final WebDriver webDriver) {
		Map<String, String> parentApplications = new HashMap<String, String>();
		WebElement baseTable = webDriver.findElement(By.id(SELECTOR_PARENT_DATA));
		List<WebElement> tableRows = baseTable.findElements(By.cssSelector(SELECTOR_PARENT_DATA_ROW));

		for (WebElement childRow : tableRows) {
			List<WebElement> columns = childRow.findElements(By.tagName(SELECTOR_TABLE_COLUMN));
			String parentAppNo = columns.get(1).getText();
			String filedOn = columns.get(2).getText();
			parentApplications.put(parentAppNo, filedOn);
		}
		return parentApplications;
	}

	@Override
	public boolean checkForeignPriorityTab(final WebDriver webDriver) {
		By foreignPriorityTabCssSelector = By.cssSelector(SELECTOR_FOREIGN_PRIORITY_TAB);
		boolean isForeignPriorityTabPresent = BBWebCrawlerUtils.isElementPresent(webDriver,
				foreignPriorityTabCssSelector);
		if (isForeignPriorityTabPresent) {
			webDriver.findElement(foreignPriorityTabCssSelector).click();
			BBWebCrawlerUtils.waitForSpecifiedDuration(10, webDriver);
		}

		return isForeignPriorityTabPresent;
	}

	@Override
	public List<List<String>> readForeignPriorities(final WebDriver webDriver) {
		List<List<String>> foreignPriorityApplications = new ArrayList<List<String>>();
		WebElement baseTable = webDriver.findElement(By.cssSelector(SELECTOR_FOREIGN_PRIORITY_TABLE));
		List<WebElement> tableRows = baseTable.findElements(By.cssSelector(SELECTOR_TR_ID_ROW));

		for (WebElement childRow : tableRows) {
			List<WebElement> columns = childRow.findElements(By.tagName(SELECTOR_TABLE_COLUMN));
			String appNo = columns.get(1).getText();
			String country = columns.get(0).getText();
			String filedOn = columns.get(2).getText();

			List<String> foreignPriorityAttributes = new ArrayList<String>();
			foreignPriorityAttributes.add(appNo);
			foreignPriorityAttributes.add(country);
			foreignPriorityAttributes.add(filedOn);
			foreignPriorityApplications.add(foreignPriorityAttributes);
		}
		return foreignPriorityApplications;
	}

	private static String getText(final WebDriver webDriver, final String cssSelector) {
		String result = null;
		WebElement element = webDriver.findElement(By.cssSelector(cssSelector));
		if (element != null) {
			result = element.getText();
			result = StringUtils.trimToNull(result);
			if ("-".equals(result)) {
				result = null;
			}
		}
		return result;
	}

	/**
	 * Returns the text value of the HTML element identified by the
	 * xPathExpression
	 *
	 * @param webDriver
	 * @param xPathExpression
	 */
	private static String getTextByXpath(final WebDriver webDriver, final String xpathExpression) {
		String value = null;
		WebElement element = webDriver.findElement(By.xpath(xpathExpression));
		value = element.getText();
		if (null != value && !StringUtils.isBlank(value) && !StringUtils.isEmpty(value)
				&& !Constant.HYPHEN_STRING.equals(value)) {
			return value;
		}
		return null;
	}

	@Override
	public MdmRecordDTO getAppMetaData(WebDriver webDriver, String applicationumber) {
		MdmRecordDTO applicationRecord = new MdmRecordDTO();
		String status = null;

		applicationRecord.setApplicationNumber(applicationumber);
		applicationRecord.setCustomerNumber(getTextByXpath(webDriver, XPATH_CUSTOMER_NUMBER));

		applicationRecord.setFilingDate(BlackboxDateUtil.parseDate((getTextByXpath(webDriver, XPATH_FILING_DATE)),
				BlackboxDateUtil.DATE_FORMAT));

		applicationRecord.setPublicationDate(BlackboxDateUtil
				.parseDate((getTextByXpath(webDriver, XPATH_PUBLICATION_DATE)), BlackboxDateUtil.DATE_FORMAT));
		applicationRecord.setExaminerName(getTextByXpath(webDriver, XPATH_EXAMINER_NAME));

		status = getTextByXpath(webDriver, XPATH_STATUS);

		ProsecutionStatus prosecutionStatus = null;
		if (StringUtils.containsIgnoreCase(status, ProsecutionStatus.ABANDONED.name())) {
			prosecutionStatus = ProsecutionStatus.ABANDONED;
		}

		applicationRecord.setProsecutionStatus(prosecutionStatus);

		applicationRecord.setGroupArtUnit(getTextByXpath(webDriver, XPATH_GROUP_ART_UNIT));
		applicationRecord.setConfirmationNumber((getTextByXpath(webDriver, XPATH_CONFIRMATION_NO)));

		applicationRecord.setPublicationNumber((getTextByXpath(webDriver, XPATH_PUBLICATION_NUMBER)));
		applicationRecord.setPatentNumber((getTextByXpath(webDriver, XPATH_PATENT_NUMBER)));
		applicationRecord.setTitleOfInvention((getTextByXpath(webDriver, XPATH_TITLE_INVENTION)));
		applicationRecord.setAttorneyDocket(
				StringUtils.removeEndIgnoreCase(getTextByXpath(webDriver, XPATH_A_DOCKET_NO), UPDATE));
		applicationRecord.setPatentDate(BlackboxDateUtil.parseDate(getTextByXpath(webDriver, XPATH_PATENT_ISSUE_DATE),
				BlackboxDateUtil.DATE_FORMAT));
		applicationRecord.setFirstNamedInventor(
				StringUtils.removeEndIgnoreCase(getTextByXpath(webDriver, XPATH_FIRST_INVENTOR), ALL_INVENTORS));
		applicationRecord.setEntityStatus(StringUtils
				.trim(StringUtils.removeEndIgnoreCase(getTextByXpath(webDriver, XPATH_ENTITY_STATUS), UPDATE)));

		return applicationRecord;
	}

	@Override
	public boolean isErrorFound(WebDriver webDriver) {
		return BBWebCrawlerUtils.isElementPresent(webDriver, By.xpath(XPATH_ERROR));
	}

}
