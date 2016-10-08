package com.blackbox.ids.services.impl.scheduler;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.dto.reference.PendingNPLReferenceDTO;
import com.blackbox.ids.core.model.notification.process.EntityName;
import com.blackbox.ids.core.model.notification.process.NotificationProcessType;
import com.blackbox.ids.core.model.reference.ReferenceBaseNPLData;
import com.blackbox.ids.core.model.reference.ReferenceStatus;
import com.blackbox.ids.core.model.reference.ReferenceType;
import com.blackbox.ids.core.repository.reference.ReferenceBaseDataRepository;
import com.blackbox.ids.services.notification.process.NotificationProcessService;
import com.blackbox.ids.services.scheduler.NplReferenceNotificationService;

@Service
public class NplReferenceNotificationServiceImpl implements NplReferenceNotificationService {

	@Autowired
	private ReferenceBaseDataRepository<ReferenceBaseNPLData> referenceBaseDataRepository;

	@Autowired
	private NotificationProcessService notificationProcessService;

	private Logger log = Logger.getLogger(NplReferenceNotificationServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createNotificationForNPLReference() {

		List<PendingNPLReferenceDTO> pendingNPLReferences = referenceBaseDataRepository
				.getAllPendingNPLReferences(ReferenceStatus.PENDING, ReferenceType.NPL, true);

		if (pendingNPLReferences != null && !pendingNPLReferences.isEmpty()) {
			log.info("Number of Pending NPL Notifications is : " + pendingNPLReferences.size());
			log.info("creating FYA notifications");

			for (PendingNPLReferenceDTO nplReference : pendingNPLReferences) {
				notificationProcessService.createNotification(nplReference.getApplicationBase(), 
						nplReference.getReferenceBaseDataId(), nplReference.getCorrespondenceId(), 
						nplReference.getReferenceBaseDataId(), EntityName.REFERENCE_BASE_DATA, 
						NotificationProcessType.NPL_DUPLICATE_CHECK, null, null);
				referenceBaseDataRepository.updateNotificationFlagById(nplReference.getReferenceBaseDataId(), true);
			}

		} else {
			log.info("No Pending NPL Notification found.");
		}

	}

}
