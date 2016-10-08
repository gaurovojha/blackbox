package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import static com.blackbox.ids.util.constant.BBWebCrawerConstants.A_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.BACK_SLASH;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.N417_DATE_FORMAT;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.PDF_SUFFIX_WITH_DOT;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.TD_TAG;
import static com.blackbox.ids.util.constant.BBWebCrawerConstants.TR_TAG;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.correspondence.Correspondence.Source;
import com.blackbox.ids.core.model.correspondence.Correspondence.Status;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepStatus;
import com.blackbox.ids.core.model.correspondence.Correspondence.StepType;
import com.blackbox.ids.core.model.correspondence.Correspondence.SubSourceTypes;
import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.WebCrawlerJobAudit;
import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceStagingRepository;
import com.blackbox.ids.core.util.PDFUtil;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.exception.LoginHostNotFoundException;
import com.blackbox.ids.exception.WebCrawlerGenericException;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.util.constant.BBWebCrawerConstants;
import com.blackbox.ids.util.constant.BBWebCrawlerPropertyConstants;
import com.blackbox.ids.webcrawler.service.uspto.IBaseCrawlerService;
import com.blackbox.ids.webcrawler.service.uspto.login.IUSPTOLoginService;

@Configuration
@Component
@Service("acknowledgementDocCrawlerService")
public class AcknowledgementDocCrawlerService implements IBaseCrawlerService {

	private static final Log LOGGER = LogFactory.getLog(AcknowledgementDocCrawlerService.class);

	private static final String N417_CODE = "N417";

	@Value("${duplicate.lower.limit}")
	private int duplicateLowerLimit;

	@Value("${duplicate.upper.limit}")
	private int duplicateUpperLimit;

	@Autowired
	private IUSPTOLoginService loginService;

	@Autowired
	private DocumentCodeRepository documentCodeRepository;

	@Autowired
	private CorrespondenceStagingRepository correspondenceStagingRepository;

	@Autowired
	private CorrespondenceBaseRepository correspondenceBaseRepository;

	@Autowired
	private BBWebCrawlerPropertyConstants properties;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private JurisdictionRepository jurisdictionRepository;

	private WebDriver webDriver;

	@Override
	public Map<String, Object> execute(WebCrawlerJobAudit webCrawlerJobAudit) {

		fetchData(webCrawlerJobAudit);
		updateBaseTable();
		return updateSchedule(updateBaseTable());
	}

	private void fetchData(WebCrawlerJobAudit webCrawlerJobAudit) {

		String baseDownloadPathOfBrowser = properties.getCorrespondenceStagingRelativePath();
		long jobId = webCrawlerJobAudit.getId();
		String clientId = properties.getClientId();

		String defaultDir = BBWebCrawlerUtils.getCompleteFilePath(baseDownloadPathOfBrowser, jobId, clientId);
		String downloadDirName = "download";
		String dir = defaultDir + BACK_SLASH + downloadDirName;

		File downLoadDir = new File(dir);
		if (!downLoadDir.exists()) {
			downLoadDir.mkdir();
		}
		webDriver = BBWebCrawlerUtils.getWebDriver(downLoadDir.getAbsolutePath());

		String epfFilePath = properties.getCredentialLocation();
		String password = "India111";
		boolean isloggedIn = false;

		try {
			loginService.login(webDriver, epfFilePath, password);
			isloggedIn = true;
		} catch (LoginHostNotFoundException ex) {
			LOGGER.error(ex);
			throw ex;
		}

		startScrapping(downLoadDir.getAbsolutePath());

	}

	private void startScrapping(String downLoadDir) {
		String parentHandle = webDriver.getWindowHandle();
		String xPathExpression = null;
		WebElement element = null;

		long waitPeriod = 2 * 60 * 60;
		try {
			Thread.sleep(waitPeriod);

			xPathExpression = "//*[@id='PatenteBusiness0']/a";
			element = webDriver.findElement(By.xpath(xPathExpression));
			element.click();

			Thread.sleep(waitPeriod);

			xPathExpression = "//*[@id='PatenteBusiness0']/div/a[2]";
			element = webDriver.findElement(By.xpath(xPathExpression));
			element.click();

			for (String winHandle : webDriver.getWindowHandles()) {
				webDriver.switchTo().window(winHandle); // switch focus of
														// WebDriver to the next
														// found window handle
			}
			Thread.sleep(waitPeriod);

			xPathExpression = "//*[@id='My workplace']";
			element = webDriver.findElement(By.xpath(xPathExpression));
			element.click();

			Thread.sleep(waitPeriod);
			xPathExpression = "//*[@id='goN417List']";

			element = webDriver.findElement(By.xpath(xPathExpression));
			element.click();
		} catch (InterruptedException e) {
			LOGGER.error("Interrupted while sleeping the Thread " + e);
			throw new WebCrawlerGenericException("Interrupted while sleeping the Thread" + e);
		} catch (NoSuchElementException e) {
			LOGGER.error(MessageFormat.format("Web Element with XPath {0} could not found on Page", xPathExpression));
			throw new WebCrawlerGenericException("Web Element not Found" + e);
		}
		List<EFileData> dataList = getAcknowledgementTableData(downLoadDir);
		webDriver.close();
		webDriver.switchTo().window(parentHandle);

		insertDataInCorrespondance(dataList);

	}

	/**
	 * Scraps the table' row and column and return the captured Data
	 */
	private List<EFileData> getAcknowledgementTableData(String downLoadDir) throws WebCrawlerGenericException {

		String xPathExpression = null;

		xPathExpression = "//*[@id='lasapp']/table/tbody/tr";
		int rowCount = webDriver.findElements(By.xpath(xPathExpression)).size();

		List<EFileData> lst = new ArrayList<AcknowledgementDocCrawlerService.EFileData>(rowCount - 1);

		WebElement elementTable = null;

		xPathExpression = "//*[@id='lasapp']/table";
		elementTable = webDriver.findElement(By.xpath(xPathExpression));

		List<WebElement> tableRows = elementTable.findElements(By.tagName(TR_TAG));

		try {
			for (int i = 1; i < rowCount; i++) {

				List<WebElement> rowCols = tableRows.get(i).findElements(By.tagName(TD_TAG));
				EFileData data = new EFileData();

				String efsId = null;

				for (int j = 5; j >= 0; j--) {
					String colValue = rowCols.get(j).getText();
					if (j == 0) {
						WebElement link = rowCols.get(j).findElement(By.tagName(A_TAG));
						link.click();
						Thread.sleep(2 * 60 * 60);
						String filePath = getDownloadedFile(downLoadDir, efsId);
						if (null == filePath) {
							LOGGER.info("Downloaded file could not be found in " + downLoadDir);
							throw new WebCrawlerGenericException(
									"Downloaded file could not be found in " + downLoadDir);
						}
						data.setPdfLink(filePath);
					}
					if (j == 1) {
						efsId = colValue;
						data.seteFSId(efsId);
					}
					if (j == 2) {
						data.setApplicationNumber(colValue);
					}
					if (j == 3) {
						data.setReceiptDate(colValue);
					}
					if (j == 5) {
						data.setDocketNumber(colValue);
					}
				}
				LOGGER.info(" Parsed N417 record : " + data);
				lst.add(data);
			}
		} catch (NoSuchElementException e) {
			LOGGER.error("Web Element with XPath could not found on Page", e);
			throw new WebCrawlerGenericException("Web Element not Found", e);
		} catch (InterruptedException e) {
			LOGGER.error("InterruptedException", e);
			throw new WebCrawlerGenericException("Web Element not Found", e);
		}
		return lst;

	}

	@Transactional
	private void insertDataInCorrespondance(List<EFileData> dataList) {

		List<CorrespondenceStaging> entityList = new ArrayList<CorrespondenceStaging>(dataList.size());
		for (EFileData data : dataList) {
			entityList.add(getCorrespondanceEntity(data));
		}
		try {
			correspondenceStagingRepository.save(entityList);
		} catch (Exception e) {
			LOGGER.error("Not able to persist Staging Data", e);
			throw new WebCrawlerGenericException("Failed in Persisting Data", e);
		}

	}

	private CorrespondenceStaging getCorrespondanceEntity(EFileData data) {

		DocumentCode code = documentCodeRepository.findByCode(N417_CODE);
		CorrespondenceStaging entity = new CorrespondenceStaging();

		entity.setApplicationNumber(data.getApplicationNumber());
		entity.setAttachmentName(data.getPdfLink());

		entity.setEfsId(data.geteFSId());
		try {
			entity.setPageCount(PDFUtil.getPDFPageCount(data.getPdfLink()));

		} catch (IOException e) {
			LOGGER.error("Not able to calculate Pdf Page Count : Link " + data.getPdfLink(), e);
			throw new WebCrawlerGenericException("Not able to calculate Pdf Page Count : Link " + data.getPdfLink(), e);
		} catch (Exception e) {
			LOGGER.error("Not able to calculate Pdf Page Count : Link " + data.getPdfLink(), e);
			throw new WebCrawlerGenericException("Not able to calculate Pdf Page Count : Link " + data.getPdfLink(), e);
		}
		try {
			entity.setAttachmentSize(PDFUtil.getPDFsize(data.getPdfLink()));

		} catch (IOException e) {

			LOGGER.error("Not able to calculate Pdf size : Link " + data.getPdfLink(), e);
			throw new WebCrawlerGenericException("Not able to  calculate Pdf size : Link " + data.getPdfLink(), e);
		}

		entity.setDocumentDescription(code.getCode());
		entity.setDocumentDescription(code.getDescription());
		entity.setStatus(Status.PENDING);
		String jurisdictionCode = data.getApplicationNumber().startsWith("PCT/US") ? "WO" : "US";
		entity.setJurisdictionCode(jurisdictionCode);
		entity.setCreatedByUser(new Long(1));
		entity.setMailingDate(BlackboxDateUtil.getCalendar(data.getReceiptDate(), N417_DATE_FORMAT));
		entity.setCreatedDate(Calendar.getInstance());
		entity.setSource(Source.AUTOMATIC);
		entity.setSubSource(SubSourceTypes.N417);

		return entity;
	}

	/**
	 * Returns the file path if found in downLoadDir
	 * 
	 * @param downLoadDir
	 * @param efsId
	 * @return
	 */
	private String getDownloadedFile(String downLoadDir, String efsId) {
		LOGGER.info("Checking  " + downLoadDir + " for file ending with " + efsId);

		File baseDir = new File(downLoadDir);
		String fileName = null;
		int sleepCounter = 3;
		int i = 0;
		while (i != sleepCounter) {
			if (baseDir.listFiles().length != 0) {
				for (File file : baseDir.listFiles()) {
					if (file.getName().endsWith(efsId + PDF_SUFFIX_WITH_DOT)) {
						fileName = file.getAbsolutePath();
						i = 3;
					}
				}
			} else {
				try {
					Thread.sleep(2 * 60 * 60);
				} catch (InterruptedException e) {
					LOGGER.error("InterruptedException Occured ", e);
					throw new WebCrawlerGenericException("InterruptedException Occured ", e);
				}
				i++;
			}
		}
		return fileName;
	}

	// Inner Class Used only for Capturing data from HTML
	class EFileData {

		String eFSId;

		String docketNumber;

		String applicationNumber;

		String pdfLink;

		String receiptDate;

		public String geteFSId() {
			return eFSId;
		}

		public void seteFSId(String eFSId) {
			this.eFSId = eFSId;
		}

		public String getDocketNumber() {
			return docketNumber;
		}

		public void setDocketNumber(String docketNumber) {
			this.docketNumber = docketNumber;
		}

		public String getApplicationNumber() {
			return applicationNumber;
		}

		public void setApplicationNumber(String applicationNumber) {
			this.applicationNumber = applicationNumber;
		}

		public String getPdfLink() {
			return pdfLink;
		}

		public void setPdfLink(String pdfLink) {
			this.pdfLink = pdfLink;
		}

		public String getReceiptDate() {
			return receiptDate;
		}

		public void setReceiptDate(String receiptDate) {
			this.receiptDate = receiptDate;
		}

		@Override
		public String toString() {
			return "EFileData [eFSId=" + eFSId + ", docketNumber=" + docketNumber + ", applicationNumber="
					+ applicationNumber + ", pdfLink=" + pdfLink + ", receiptDate=" + receiptDate + "]";
		}

	}

	private int updateBaseTable() {

		int duplicateCounter = 0;
		List<CorrespondenceStaging> stagingList = correspondenceStagingRepository
				.findByDocumentCodeAndStatusActive(N417_CODE);

		for (CorrespondenceStaging entity : stagingList) {
			entity.getEfsId();

			// List<CorrespondenceBase> baseList =
			// correspondenceBaseRepository.findByEFSId(entity.getEfsId());
			// To DO
			List<CorrespondenceBase> baseList = new ArrayList<>();
			if (baseList.size() > 0) {
				duplicateCounter++;
			} else {
				CorrespondenceBase base = prepareBaseEnity(entity);
				correspondenceBaseRepository.save(base);
			}
		}
		return duplicateCounter;
	}

	private CorrespondenceBase prepareBaseEnity(CorrespondenceStaging staging) {

		CorrespondenceBase base = new CorrespondenceBase();
		base.setAttachmentSize(staging.getAttachmentSize());
		Jurisdiction jurisdiction = jurisdictionRepository.findByJurisdictionValue(staging.getJurisdictionCode());
		ApplicationBase applicationBase = applicationRepository.findApplicationBaseByApn(staging.getApplicationNumber(),
				jurisdiction);
		base.setPrivatePairUploadDate(staging.getPrivatePairUploadDate());
		base.setMailingDate(staging.getMailingDate());
		base.setCreatedByUser(User.SYSTEM_ID);

		DocumentCode document = documentCodeRepository.findByDocumentCode(staging.getDocumentCode());
		base.setDocumentCode(document);
		base.setSource(staging.getSource());
		base.setSubSource(staging.getSubSource());
		base.setStatus(Status.IMPORTED_FROM_STAGING_JOB);
		base.setApplication(applicationBase);
		base.setPageCount(staging.getPageCount());
		base.setStep(StepType.OCR);
		base.setStepStatus(StepStatus.NEW);
		return base;
	}

	private Map<String, Object> updateSchedule(int duplicateCounter) {
		Map<String, Object> returnVal = new HashMap<String, Object>();

		returnVal.put(BBWebCrawerConstants.COUNTER_LOWER_VALUE, Integer.valueOf(duplicateLowerLimit));
		returnVal.put(BBWebCrawerConstants.COUNTER_UPPER_VALUE, Integer.valueOf(duplicateUpperLimit));
		returnVal.put(BBWebCrawerConstants.COUNTER_VAL, Integer.valueOf(duplicateCounter));

		return returnVal;
	}

}
