package com.blackbox.ids.services.document;

import java.util.List;

import com.blackbox.ids.core.model.DocumentCode;

public interface IDocumentService {

	List<DocumentCode> findAllDocumentCodes();

}
