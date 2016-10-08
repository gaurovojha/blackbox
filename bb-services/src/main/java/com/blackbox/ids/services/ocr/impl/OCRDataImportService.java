package com.blackbox.ids.services.ocr.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.ocr.OCRQueue;
import com.blackbox.ids.core.repository.correspondence.CorrespondenceBaseRepository;
import com.blackbox.ids.core.repository.ocr.OCRQueueRepository;
import com.blackbox.ids.services.mdm.IImportData;

@Service("ocrDataImportService")
public class OCRDataImportService implements IImportData {

	private static final Logger LOG = Logger.getLogger(OCRDataImportService.class);

	@Autowired
	private CorrespondenceBaseRepository correspondenceBaseRepository;

	@Autowired
	private OCRQueueRepository queueRepository;

	@Override
	@Transactional
	public void importData() {

		LOG.info("Importing Records from BB_CORRESPONDENCE ");
		List<CorrespondenceBase> correspondenceIdList = correspondenceBaseRepository.findOCRApplicableRecords();
		List<OCRQueue> queueRecords = new ArrayList<OCRQueue>(correspondenceIdList.size());
		for (CorrespondenceBase baseEntity : correspondenceIdList) {
			queueRecords.add(getOCRQueueData(baseEntity));
		}
		queueRepository.save(queueRecords);

		LOG.info("Count of records found :" + queueRecords.size());
	}

	private OCRQueue getOCRQueueData(CorrespondenceBase baseEntity) {
		OCRQueue entity = new OCRQueue();
		entity.setCorrespondenceBase(baseEntity);
		entity.setStatus(OCRQueue.Status.INITIATED);
		return entity;
	}

}
