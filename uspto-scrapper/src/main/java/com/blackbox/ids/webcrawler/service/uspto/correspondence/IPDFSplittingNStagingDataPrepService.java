package com.blackbox.ids.webcrawler.service.uspto.correspondence;

import java.io.IOException;
import java.util.List;

import com.blackbox.ids.core.dto.webcrawler.PDFSplittingAndStagingDataDTO;
import com.blackbox.ids.core.model.correspondence.CorrespondenceStaging;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;

public interface IPDFSplittingNStagingDataPrepService {

	/**
	 * Split pdf and prepars the staging data for each splitted file.
	 *
	 * @param pdfPath
	 *            the pdf path
	 * @param stagingDataPerPDF
	 *            the staging data per pdf
	 * @param targetFolder
	 *            the target folder
	 * @param bookmarkDescriptions
	 *            the bookmark descriptions
	 * @param isCorrespondenceCall
	 *            the is correspondence call
	 * @param pdfPathToDownloadQueueData
	 *            the pdf path to download queue data
	 * @param descriptionToCodeMap
	 *            the description to code map
	 * @param bookMarkText 
	 * @param correspondenceStaging 
	 * @param documentData 
	 * @param applicationCorrespondenceData 
	 * @param downloadOfficeActionQueues 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	void splitPDFAndPrepareStagingData(PDFSplittingAndStagingDataDTO pdfSplittingAndStagingDataDTO)
					throws IOException;

	/**
	 * Split PDF document as per bookmarks and prepares correspondence staging data for each splited file.
	 * @param ids
	 *            the IDS tracking queue record
	 * @param correspondenceStageList
	 *            correspondence List, new object will be added to this list
	 * @param pdfPath
	 *            path of the IDS pdf document
	 * @param targetFolder
	 *            target folder to store splited pdf documents
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	void splitIDSDocumentAndPrepareStagingData(TrackIDSFilingQueue ids,
			List<CorrespondenceStaging> correspondenceStageList, String pdfPath, String targetFolder,
			String idsDocumentDescription) throws IOException;

}
