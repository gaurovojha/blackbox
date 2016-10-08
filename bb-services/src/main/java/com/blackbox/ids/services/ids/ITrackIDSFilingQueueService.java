package com.blackbox.ids.services.ids;

import com.blackbox.ids.core.model.IDS.IDS.SubStatus;
import com.blackbox.ids.core.model.webcrawler.TrackIDSFilingQueue;

public interface ITrackIDSFilingQueueService {

	void updateIDSTrackingStatus(TrackIDSFilingQueue trackIDSQueue, SubStatus idsSubStatus);

	void updateTrackIDSFilingQueue(TrackIDSFilingQueue trackIDSQueue);

}
