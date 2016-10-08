package com.blackbox.ids.services.crawler;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.enums.FolderRelativePathEnum;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IIFWDownloadDoCScrapper;

public class MockIFWDownloadDocWebCrawlerScrapper implements IIFWDownloadDoCScrapper{

	@Autowired
	private DownloadOfficeActionQueueRepository downloadOfficeActionQueueRepository ;
	
	@Override
	public boolean checkAndScrapApplicationNumberDetails(WebDriver webDriver,
			DownloadOfficeActionQueue actionQueue,
			Long jobId, Map<String, DownloadOfficeActionQueue> pdfPathToDownloadQueueEntity, List<DocumentCode> documentCodes, int maxRecordCount) throws InterruptedException {

		boolean isValidAppplicationNumber = true ;
		List<DownloadOfficeActionQueue> downloadOfficeActionQueueList = downloadOfficeActionQueueRepository.findByCustomerNumber("136388");
		String path =  FolderRelativePathEnum.CRAWLER.getAbsolutePath("") + "Correspondence_12-30-2015_06_04_15.pdf" ;
		pdfPathToDownloadQueueEntity.put(path, downloadOfficeActionQueueList.get(0)) ;
	    path =  FolderRelativePathEnum.CRAWLER.getAbsolutePath("") + "invalid.pdf" ;
		pdfPathToDownloadQueueEntity.put(path, downloadOfficeActionQueueList.get(0)) ;
		return isValidAppplicationNumber;
	}
	
}
