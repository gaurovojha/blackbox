package com.blackbox.ids.services.document.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.DocumentCode;
import com.blackbox.ids.core.repository.DocumentCodeRepository;
import com.blackbox.ids.services.document.IDocumentService;

@Service
public class DocumentServiceImpl  implements IDocumentService{
	@Autowired
	DocumentCodeRepository documentCodeRepository;
	@Override
	public List<DocumentCode> findAllDocumentCodes() {
		return documentCodeRepository.findAll();
	}

}
