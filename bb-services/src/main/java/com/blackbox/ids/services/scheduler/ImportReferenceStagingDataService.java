package com.blackbox.ids.services.scheduler;

import com.blackbox.ids.core.model.reference.ReferenceStagingData;

public interface ImportReferenceStagingDataService {

	void importStagingData();

	void handleImpadocFailedReferences(ReferenceStagingData referenceStagingData);
	
	void handleImportReadyReferences(ReferenceStagingData referenceStagingData);
}
