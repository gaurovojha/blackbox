package com.blackbox.ids.services.impl.reference;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.reference.OcrData;
import com.blackbox.ids.core.repository.reference.OcrDataRepository;
import com.blackbox.ids.services.reference.OcrDataService;

@Service
public class OcrDataServiceImpl implements OcrDataService {

	private static final Logger LOG = Logger.getLogger(OcrDataServiceImpl.class);

	@Autowired
	private OcrDataRepository ocrDataRepository;

	@Override
	@Transactional(readOnly = true)
	public List<OcrData> getOcrDataForProcessing() throws ApplicationException {
		LOG.info("Getting UnProcessed OCR Data.");

		List<OcrData> ocrDataList = null;
		try {
			
			ocrDataList = ocrDataRepository.findByProcessed(false);

		} catch (DataAccessException dataAccessException) {
			LOG.error("Error while fetching UnProcessed OCR Data." + dataAccessException);
			throw new ApplicationException(dataAccessException);

		}

		if (ocrDataList != null) {
			LOG.info("Total no. of UnProcessed OCR Data : " + ocrDataList.size());
		} else {
			LOG.info("Total no. of UnProcessed OCR Data : 0 ");
		}

		return ocrDataList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateOcrDataAsProcessed(Long ocrDataId)
			throws ApplicationException {
		try {
			ocrDataRepository.updateOcrDataAsProcessed(ocrDataId);
			LOG.info("Data in BB_OCR_Data_Extraction table is processed successfully for Id: [" + ocrDataId + "]");
		} catch (DataAccessException dataAccessException) {
			LOG.error("Failed to update the processed flag in BB_OCR_Data_Extraction table for Id [" + ocrDataId + "]");
			throw new ApplicationException(dataAccessException);
		}

	}

}
