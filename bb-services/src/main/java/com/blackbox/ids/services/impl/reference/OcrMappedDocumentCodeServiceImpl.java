package com.blackbox.ids.services.impl.reference;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.model.reference.OcrMappedDocumentCode;
import com.blackbox.ids.core.repository.reference.OcrMappedDocumentCodeRepository;
import com.blackbox.ids.services.reference.OcrMappedDocumentCodeService;

@Service
public class OcrMappedDocumentCodeServiceImpl implements OcrMappedDocumentCodeService {

	@Autowired
	private OcrMappedDocumentCodeRepository ocrMappedDocumentCodeRepository;

	private Logger log = Logger.getLogger(OcrMappedDocumentCodeServiceImpl.class);

	@Override
	@Transactional(readOnly = true)
	public OcrMappedDocumentCode findByDocumentCode(String documentCode) {
		OcrMappedDocumentCode ocrMappedDocumentCode = null;
		try {
			ocrMappedDocumentCode = ocrMappedDocumentCodeRepository.findByDocumentCode(documentCode);
		} catch (DataAccessException dataAccessException) {
			log.error(
					"Error while fetching the OCRMapped data from BB_OCR_MAPPED_DOCUMENT_CODE table for documentCode: ["
							+ documentCode + "]");
			throw new ApplicationException(dataAccessException);

		}

		return ocrMappedDocumentCode;
	}

}
