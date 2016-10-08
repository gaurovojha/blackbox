package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.blackbox.ids.core.dto.correspondence.xmlmapping.OutgoingCorrespondence;
import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.model.mstr.AuthenticationData;
import com.blackbox.ids.dto.CorrespondenceCrawlerDTO;

/**
 * The Interface {@link IOutgoingCorrespondenceScrapper}
 * contains the scrapper method for Outgoing Correspondence Web Crawler.
 * 
 * @author nagarro
 * 
 *
 */
public interface IOutgoingCorrespondenceScrapper {


	/**
	 * Scrap and prepare data.
	 *
	 * @param webDriver the web driver
	 * @param correspondences the correspondences
	 * @param pdfNames the pdf names
	 * @param documentCodes the document codes
	 * @param crawlerId the crawler id
	 * @param correspondenceCrawlerDTO the correspondence crawler dto
	 * @param epfFile the epf file
	 * @return true, if successful
	 */
	boolean scrapAndPrepareData(WebDriver webDriver, List<OutgoingCorrespondence> correspondences, List<String> pdfNames,
			List<DocumentCode> documentCodes, Long crawlerId, CorrespondenceCrawlerDTO correspondenceCrawlerDTO, AuthenticationData epfFile);
	
}
