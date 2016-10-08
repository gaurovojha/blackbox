package com.blackbox.ids.services.crawler;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;
import com.blackbox.ids.util.BBWebCrawlerUtils;
import com.blackbox.ids.webcrawler.service.uspto.correspondence.IOutgoingCorrespondenceScrapper;

public class MockOutgoingCorrespondenceScrapper implements IOutgoingCorrespondenceScrapper {

	@Override
	public boolean scrapAndPrepareData(WebDriver webDriver, List<OutgoingCorrespondence> correspondences,
			List<String> pdfNames, List<DocumentCode> documentCodes, Long crawlerId, CorrespondenceCrawlerDTO correspondenceCrawlerDTO,
			AuthenticationData epfFile) {

		boolean isAnyRecordsSelectedInUI = true;
		
		File inputFile1 = new File(getClass().getClassLoader().getResource("META-INF/test-data/crawler/Correspondence_12-30-2015_06_04_15.pdf").getFile());
		pdfNames.add(inputFile1.getAbsolutePath());
		File inputFile = new File(getClass().getClassLoader().getResource("META-INF/test-data/crawler/Correspondence_01-11-2016_00_10_55.xml").getFile());
		OutgoingCorrespondence outgoingCorrespondence = BBWebCrawlerUtils.getMarshalledCorrespondenceObject(inputFile);
		correspondences.add(outgoingCorrespondence) ;
		return isAnyRecordsSelectedInUI;
	}
	
}
