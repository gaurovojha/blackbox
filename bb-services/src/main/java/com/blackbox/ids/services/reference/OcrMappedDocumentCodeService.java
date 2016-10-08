package com.blackbox.ids.services.reference;

import com.blackbox.ids.core.model.reference.OcrMappedDocumentCode;

public interface OcrMappedDocumentCodeService {
    
    public abstract OcrMappedDocumentCode findByDocumentCode(String documentCode);

}
