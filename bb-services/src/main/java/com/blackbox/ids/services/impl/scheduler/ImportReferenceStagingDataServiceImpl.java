package com.blackbox.ids.services.impl.scheduler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.model.correspondence.CorrespondenceBase;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceStagingData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.repository.reference.ReferenceStagingDataRepository;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.reference.ReferenceService;
import com.blackbox.ids.services.scheduler.ImportReferenceStagingDataService;

@Service
public class ImportReferenceStagingDataServiceImpl implements ImportReferenceStagingDataService {

	@Autowired
	ReferenceStagingDataRepository referenceStagingDataRepository;

	@Autowired
	ReferenceService referenceService;

	@Autowired
	private NotificationProcessService notificationProcessService;

	private Logger log = Logger.getLogger(ImportReferenceStagingDataServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void importStagingData() {

		Set<ReferenceStatus> referenceStatus = new HashSet<ReferenceStatus>();
		referenceStatus.add(ReferenceStatus.INPADOC_FAILED);
		referenceStatus.add(ReferenceStatus.IMPORT_READY);
		List<ReferenceStagingData> referenceStagingDataList = referenceStagingDataRepository
				.findByReferenceStatusIn(referenceStatus);

		if (referenceStagingDataList != null) {
			for (ReferenceStagingData referenceStagingData : referenceStagingDataList) {
				if (ReferenceStatus.INPADOC_FAILED.equals(referenceStagingData.getReferenceStatus())) {

					handleImpadocFailedReferences(referenceStagingData);

				} else {
					handleImportReadyReferences(referenceStagingData);
				}

			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleImpadocFailedReferences(ReferenceStagingData referenceStagingData) {
		try {
			Long correspondenceid = null;
			CorrespondenceBase correspondence = referenceStagingData.getCorrespondence();
			if(correspondence!=null){
				correspondenceid = correspondence.getId();
			}
			notificationProcessService.createNotification(referenceStagingData.getApplication(), 
					referenceStagingData.getReferenceStagingDataId(), correspondenceid, 
					referenceStagingData.getReferenceStagingDataId(), EntityName.REFERENCE_STAGING_DATA, 
					NotificationProcessType.INPADOC_FAILED, null, null);
			referenceStagingDataRepository.updateNotificationStatusByid(true, referenceStagingData.getReferenceStagingDataId());
		} catch (Exception exception) {
			log.info("Excdeption while handling Reference staging data with status as INPADOC_FAILED. " + exception);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleImportReadyReferences(ReferenceStagingData referenceStagingData) {

		try {
			referenceService.importReference(referenceStagingData);
			referenceStagingDataRepository.updateReferenceByid(ReferenceStatus.IMPORT_DONE, false,
					referenceStagingData.getReferenceStagingDataId());
		} catch (Exception exception) {
			log.info("Exception while importing the reference from Staging to Base. " + exception);
			handleImpadocFailedReferences(referenceStagingData);

		}

	}

}
