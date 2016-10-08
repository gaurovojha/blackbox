package com.blackbox.ids.services.mdm;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.enums.QueueStatus;
import com.blackbox.ids.core.model.mdm.ApplicationBase;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.webcrawler.DownloadOfficeActionQueue;
import com.blackbox.ids.core.repository.ApplicationRepository;
import com.blackbox.ids.core.repository.webcrawler.DownloadOfficeActionQueueRepository;

@Service("ePOfficeActionImportService")
public class EPOfficeActionImportService implements IImportData {

	private static Log logger = LogFactory.getLog(EPOfficeActionImportService.class);

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private DownloadOfficeActionQueueRepository officeActionQueueRepository;

	@Override
	@Transactional
	public void importData() {
		logger.info("Fetching Data from BB_APPLICATION_BASE table");
		try {
			List<ApplicationBase> entityList = applicationRepository.findEPOfficeRecords(Jurisdiction.Code.EUROPE.value,
					Jurisdiction.Code.US.value);

			logger.debug("Found " + entityList.size() + " Records");
			List<DownloadOfficeActionQueue> officeActionList = new ArrayList<>(entityList.size());

			for (ApplicationBase baseEnitity : entityList) {
				DownloadOfficeActionQueue officeEntity = new DownloadOfficeActionQueue();
				officeEntity.setApplicationNumberFormatted(baseEnitity.getApplicationNumber());
				officeEntity.setDocumentCode(baseEnitity.getJurisdiction().getCode());
				officeEntity.setMailingDate(null);
				officeEntity.setDocumentCode(Constant.ALL_DOCUMENT_CODE);
				officeEntity.setStatus(QueueStatus.INITIATED);
				officeEntity.setCustomerNumber(baseEnitity.getCustomer().getCustomerNumber());
				officeEntity.setJurisdictionCode(baseEnitity.getJurisdiction().getCode());
				officeActionList.add(officeEntity);
			}
			officeActionQueueRepository.save(officeActionList);
		} catch (Exception e) {
			logger.error("Failed to import data from  BB_APPLICATION_BASE table", e);
		}

	}
}
