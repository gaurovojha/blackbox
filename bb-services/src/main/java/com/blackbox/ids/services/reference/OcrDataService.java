package com.blackbox.ids.services.reference;

import java.util.List;

import com.blackbox.ids.core.model.reference.OcrData;

public interface OcrDataService {

    public abstract List<OcrData> getOcrDataForProcessing();

    public abstract void updateOcrDataAsProcessed(Long ocrDataExtractionId);
}
