package com.blackbox.ids.util.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class BBWebCrawlerPropertyConstants.
 */
@Configuration
@PropertySource("classpath:path.properties")
@Component
public class BBWebCrawlerPropertyConstants extends BBWebCrawerConstants {

	/** The credential location. */
	@Value("${uspto.crawler.login.authentcation.file.path}")
	public String credentialLocation;

	/** The login authentication wait time in seconds. */
	@Value("${uspto.crawler.login.wait.time}")
	public String loginAuthenticationTimeInSeconds;

	/** The xml download wait time in seconds. */
	@Value("${uspto.crawler.xml.download.wait.time}")
	public String xmlDownloadTime;

	/** The pdf download wait time in seconds. */
	@Value("${uspto.crawler.pdf.download.wait.time}")
	public String pdfDownloadTime;

	/** The admin email id. */
	@Value("${uspto.crawler.admin.emailId}")
	public String adminEmailId;

	/** The base download path. */
	@Value("${uspto.crawler.base.download.relative.path}")
	public String baseDownloadPath;

	/** The USPT o_ logi n_ ur l_1. */
	@Value("${uspto.login.url1}")
	public String usptoLoginURL1;

	/** The USPT o_ logi n_ ur l_2. */
	@Value("${uspto.login.url2}")
	public String usptoLoginURL2;

	/** The uspto login frame name. */
	@Value("${uspto.login.frame.name}")
	public String usptoLoginFrameName;

	/** The browse button. */
	@Value("${uspto.login.browse.button.name}")
	public String browseButton;

	/** The authentication failure text. */
	@Value("${uspto.login.authenticate.failure.text.xpath}")
	public String authenticationFailureText;

	/** The password name. */
	@Value("${uspto.login.password.name}")
	public String passwordName;

	/** The accept checkbox name. */
	@Value("${uspto.login.accept.name}")
	public String acceptCheckBoxName;

	/** The authenticate button name. */
	@Value("${uspto.login.authenticate.button.name}")
	public String authenticateButtonName;

	/** The customer search name. */
	@Value("${uspto.search.customer.name}")
	public String customerSearchName;

	/** The view outgoing correspondence id. */
	@Value("${uspto.view.outgoing.correspondence.id}")
	public String viewOutgoingCorrespondenceId;

	/** The filter past days name. */
	@Value("${uspto.filter.past.days.name}")
	public String filterPastDaysName;

	/** The filter past days value. */
	@Value("${uspto.filter.past.days.value}")
	public String filterPastDaysValue;

	/** The filter sort by name. */
	@Value("${uspto.outgoing.correspondence.sort.by.name}")
	public String filterSortByName;

	/** The filter sort by value. */
	@Value("${uspto.outgoing.correspondence.sort.by.value}")
	public String filterSortByValue;

	/** The filter search type name. */
	@Value("${uspto.ocn.radio.button.name}")
	public String filterSearchRadioButtonName;

	/** The filter customer number value. */
	@Value("${uspto.filter.customer.number.value}")
	public String filterCustomerNumberValue;

	/** The submit customer name. */
	@Value("${uspto.view.outgoing.correspondence.search.button.id}")
	public String submitCustomerName;

	/** The xml download link name. */
	@Value("${uspto.search.xml.button.text.link}")
	public String xmlDownloadLinkPath;

	/** The correspondence count in ui. */
	@Value("${uspto.search.ui.results.text.xpath}")
	public String correspondenceUICount;

	/** The notification text selector. */
	@Value("${uspto.home.page.updation.notification.text.selector}")
	public String notificationTextSelector;

	/** The PAGINATIO n_ ro w_ typ e_1_ cs s_ selector. */
	@Value("${uspto.pagination.row.type.1.css.selector}")
	public String paginationRowType1Selector;

	/** The PAGINATIO n_ ro w_ typ e_2_ cs s_ selector. */
	@Value("${uspto.pagination.row.type.2.css.selector}")
	public String paginationRowType2Selector;

	/** The PAGINATIO n_ ro w_ typ e_1_ documen t_ cod e_ prefix. */
	@Value("${uspto.pagination.row.type.1.document.code.css.selector.prefix}")
	public String paginationRowType1DocumentPrefix;

	/** The pagination row document code suffix. */
	@Value("${uspto.pagination.document.code.css.selector.suffix}")
	public String paginationRowDocumentCodePrefix;

	/** The pagination application number prefix. */
	@Value("${uspto.pagination.application.number.css.selector.prefix}")
	public String paginationApplicationNumberPrefix;

	/** The pagination application number suffix. */
	@Value("${uspto.pagination.application.number.css.selector.suffix}")
	public String paginationApplicationNumberSuffix;

	@Value("${uspto.pagination.customer.number.xpath.suffix}")
	public String paginationCustomerNumberSuffix;

	/** The pagination checkbox prefix. */
	@Value("${uspto.pagination.checkbox.css.selector.prefix}")
	public String paginationCheckBoxPrefix;

	/** The PAGINATIO n_ ro w_ typ e_2_ documen t_ cod e_ prefix. */
	@Value("${uspto.pagination.row.type.2.document.code.css.selector.prefix}")
	public String paginationRowType2DocPrefix;

	/** The pdf download link. */
	@Value("${uspto.search.pdf.button.link}")
	public String pdfDownloadLinkPath;

	/** The pagination traversal prefix xpath. */
	@Value("${uspto.pagination.nextpage.link.xpath.prefix}")
	public String paginationTraversalPrefixXPath;

	/** The pagination traversal suffix xpath. */
	@Value("${uspto.pagination.nextpage.link.xpath.suffix}")
	public String paginationTraversalSuffixXPath;

	/*- ------------------------ Properties for IFW Download Doc Code Crawler -- */

	/** The application number us radio button xpath. */
	@Value("${uspto.search.applicationNumber.us.radio.xpath}")
	public String applicationNumberUSRadioButtonXPath;

	/** The application number wo radio button xpath. */
	@Value("${uspto.search.applicationNumber.wo.radio.xpath}")
	public String applicationNumberWORadioButtonXPath;

	/** The application number text id. */
	@Value("${uspto.search.applicationNumber.text.id}")
	public String applicationNumberTextId;

	/** The search application number text xpath. */
	@Value("${uspto.search.applicationNumber.submit.xpath}")
	public String searchApplicationNumberXPath;

	/** The image file wrapper xpath. */
	@Value("${uspto.search.applicationNumber.imageFileWrapper.xpath}")
	public String imageFileWrapperXPath;

	/** The select new case xpath. */
	@Value("${uspto.search.applicationNumber.selectNewCase.xpath}")
	public String selectNewCaseXPath;

	/** The css selector prefix. */
	@Value("${uspto.document.css.selector.prefix}")
	public String cssSelectorPrefix;

	/** The PAGINATIO n_ ro w_ typ e_1_ xpat h_ selector. */
	@Value("${uspto.document.selector.row1.xpath}")
	public String paginationRowType1XPath;

	/** The PAGINATIO n_ ro w_ typ e_2_ xpat h_ selector. */
	@Value("${uspto.document.selector.row2.xpath}")
	public String paginationRowType2XPath;

	/** The ifw pdf download link. */
	@Value("${uspto.pdf.download.selector.link}")
	public String ifwPDFDownloadLink;

	/** The ifw mail and doc code xpath prefix. */
	@Value("${uspto.ifw.maildate.xpath.prefix}")
	public String ifwMailDatePrefixXPath;

	/** The ifw mail xpath suffix. */
	@Value("${uspto.ifw.maildate.xpath.suffix}")
	public String ifwMailDateSuffixXPath;

	/** The ifw doc code xpath suffix. */
	@Value("${uspto.ifw.doccode.xpath.suffix}")
	public String ifwDocCodeSuffixXPath;

	/** The correspondence staging relative path. */
	@Value("${uspto.correspondece.staging.relative.path}")
	public String correspondenceStagingRelativePath;

	/** The correspondence base relative path. */
	@Value("${uspto.correspondece.base.relative.path}")
	public String correspondenceBaseRelativePath;

	/** The filter past days value private pair. */
	@Value("${uspto.filter.past.days.privatepair.value}")
	public String filterPastDaysPrivatePaiValue;

	/** The client name. */
	@Value("${uspto.crawler.client.id}")
	public String clientId;
	
	/** The authentication success text. */
	@Value("${uspto.login.authenticate.success.text.xpath}")
	public String authenticationSuccessText;
	
	/** The ifw application search time. */
	@Value("${uspto.crawler.ifwdownload.application.search.wait.time}")
	public String ifwApplicationSearchTime;

	/** The ifw image file wrapper wait time. */
	@Value("${uspto.crawler.ifwdownload.imageFileWrapper.wait.time}")
	public String ifwImageFileWrapperWaitTime;
	
	@Value("${job.rescheduling.time}")
	private int rechedulingTime;
	
	/** The Constant REED TECH base url. */
	@Value("${uspto.reed.tech.url}")
	public String reedTechBaseURL;
	
	@Value("${ifw.zip.download.check.count}")
	public int ifwZipDownloadRetryCount;
	
	@Value("${ifw.zip.download.check.duration.seconds}")
	public long ifwZipDownloadRetryDuration;
	
	@Value("${track.ids.filing.nextrundate.days}")
	public int idsTrackFilingNextRunDateDays;
	
	@Value("${uspto.authentication.failure.email.content}")
	public String authenticationFailureEmailContent;
	
	public String getClientId() {
		return clientId;
	}

	public String getBaseDownloadPath() {
		return baseDownloadPath;
	}

	public String getCorrespondenceStagingRelativePath() {
		return correspondenceStagingRelativePath;
	}

	public String getCredentialLocation() {
		return credentialLocation;
	}

	public int getRechedulingTime() {
		return rechedulingTime;
	}

}
