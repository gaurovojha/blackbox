package com.blackbox.ids.services.impl.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.OcrData;
import com.blackbox.ids.core.model.reference.OcrMappedDocumentCode;
import com.blackbox.ids.core.model.reference.OcrStatus;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.OcrDataService;
import com.blackbox.ids.services.reference.OcrMappedDocumentCodeService;
import com.blackbox.ids.services.scheduler.OcrDataSchedulerService;

@Service
public class OcrDataSchedulerServiceImpl implements OcrDataSchedulerService {

	@Autowired
	private OcrDataService ocrDataService;

	@Autowired
	private OcrMappedDocumentCodeService ocrMappedDocumentCodeService;

	@Autowired
	private NotificationProcessService notificationProcessService;

	private Logger log = Logger.getLogger(OcrDataSchedulerServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processOcrData() {

		log.info("Processing OCR data.");
		List<OcrData> ocrDataList = ocrDataService.getOcrDataForProcessing();
		if (ocrDataList != null && !ocrDataList.isEmpty()) {
			for (OcrData ocrData : ocrDataList) {
				processsIndividualOcrRecords(ocrData);
			}

		} else {
			log.info("No OCR data found to be processed.");
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processsIndividualOcrRecords(OcrData ocrData) {

		/*
		 * Checking OCR Status to process accordingly.
		 */
		if (OcrStatus.DONE.equals(ocrData.getOcrStatus())) {
			log.info("OCR status is DONE for OcrDataID: [" + ocrData.getOcrDataId()
					+ "]. Initiating Validation process for this OCR.");
			processOcrDoneDocument(ocrData);
		} else {
			log.info("OCR status is FAILED for OCRDataExtractionID: [" + ocrData.getOcrDataId()
					+ "]. Initiating Manual process for this OCR.");
			processOcrFailedDocument(ocrData);
		}

		/*
		 * update the processed flag in BB_OCR_Data_Extraction table so that the
		 * same OCR is not picked next time by the scheduler.
		 */

		ocrDataService.updateOcrDataAsProcessed(ocrData.getOcrDataId());

	}

	private void processOcrDoneDocument(OcrData ocrData) {

		boolean valid = this.validateOCR(ocrData);
		if (valid) {
			log.info("OCD data is VALID for OCRDataExtractionID: [" + ocrData.getOcrDataId()
					+ "] Initiating Automatic Processing.");
			// TODO initiate Automatic processing. To be done in next sprint.

		} else {
			log.info("OCD data is INVALID for OCRDataExtractionID: [" + ocrData.getOcrDataId()
					+ "] Initiating Manual Notification.");
			createManualNotification(ocrData);
		}

	}

	private void processOcrFailedDocument(OcrData ocrData) {
		createManualNotification(ocrData);

	}

	private void createManualNotification(OcrData ocrData) {

		log.info("Creating manual Notification for OcrDataId: " + ocrData.getOcrDataId());
		ApplicationBase applicationBase = ocrData.getCorrespondenceId().getApplication();
		Long notificationProcessId = notificationProcessService.createNotification(applicationBase, null, 
				ocrData.getCorrespondenceId().getId(), ocrData.getOcrDataId(), EntityName.OCR_DATA, 
				NotificationProcessType.REFERENCE_MANUAL_ENTRY, null, null);
		log.info("Manual Notification created successfully for OcrDataId :[" + ocrData.getOcrDataId()
				+ "], NotificationId : [" + notificationProcessId + "]");

	}

	private boolean validateOCR(OcrData ocrData) {

		String documentCode = ocrData.getCorrespondenceId().getDocumentCode().getCode();
		String jurisdictionCode = ocrData.getJurisdictionCode();
		log.info("Validating Document Code for OcrDataId: [" + ocrData.getOcrDataId() + "] documentId :[" + documentCode
				+ "], Jurisdiction : [" + jurisdictionCode + "]");
		boolean valid = false;

		/*
		 * fetching the documentCode based on documentID from BB_DOCUMENT_CODE
		 * table.
		 */
		if (documentCode != null && jurisdictionCode != null) {

			/*
			 * fetching OCR mapped document from BB_OCR_MAPPED_DOCUMENT_CODE
			 * table using documentCode.
			 */
			OcrMappedDocumentCode ocrMappedDocumentCode = ocrMappedDocumentCodeService.findByDocumentCode(documentCode);
			if (ocrMappedDocumentCode != null && ocrMappedDocumentCode.getJurisdictionCode() != null) {

				/*
				 * Checking if the jurisdiction code present in
				 * BB_OCR_MAPPED_DOCUMENT_CODE table matches with the
				 * jurisdiction code in BB_DOCUMENT_CODE table for a particular
				 * documentID
				 */
				if (ocrMappedDocumentCode.getJurisdictionCode().equalsIgnoreCase(jurisdictionCode)) {
					log.info("Document code matched for OcrDataId : " + ocrData.getOcrDataId());
					valid = true;
				} else {
					log.info(
							"Jurisdiction code mismatch. OCR Validation failed. Jurisdiction code in BB_OCR_MAPPED_DOCUMENT_CODE is ["
									+ ocrMappedDocumentCode.getJurisdictionCode()
									+ "] whereas Jurisdiction code in BB_OCR_Data_Extraction is [" + jurisdictionCode
									+ "]");
				}

			}
			log.info(" No mapping found for documentCode :[" + documentCode + "] in BB_OCR_MAPPED_DOCUMENT_CODE table");
		}
		log.info("DocumentCode or Jurisdiction not found for OcrDataId : [" + ocrData.getOcrDataId() + "]");
		return valid;
	}

}
