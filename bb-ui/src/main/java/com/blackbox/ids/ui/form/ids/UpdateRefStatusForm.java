package com.blackbox.ids.ui.form.ids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.blackbox.ids.core.dto.IDS.dashboard.CitedReferenceDTO;
import com.blackbox.ids.core.dto.IDS.dashboard.UpdateRefStatusDTO;
import com.blackbox.ids.core.util.date.BlackboxDateUtil;
import com.blackbox.ids.core.util.date.TimestampFormat;

public class UpdateRefStatusForm {
	
	private List<ReferenceForm> usPatentReference;
	private List<ReferenceForm> usPublicationReference;
	private List<ReferenceForm> fpReference;
	private List<ReferenceForm> nplReference;
	private Long idsId;
	private Long notificationProcessId;
	public static final String MODEL_KEY = "idsUpdateRefForm";
	
	public UpdateRefStatusDTO toDTO() {
		UpdateRefStatusDTO dto = new UpdateRefStatusDTO();
		List<Long> notCitedRefs = new ArrayList<Long>();
		List<CitedReferenceDTO>  citedRefs = new ArrayList<>();
		if (usPatentReference.size()>0) {
			for(ReferenceForm usPatentRef : usPatentReference) {
				if(usPatentRef.isCited()) {
					CitedReferenceDTO citedRef = new CitedReferenceDTO();
					if(usPatentRef.getFilingDate()!=null) {
						citedRef.setFilingDate(BlackboxDateUtil.strToCalendar(usPatentRef.getFilingDate(), TimestampFormat.MMMDDYYYY));
					}
					citedRef.setRefFlowId(usPatentRef.getRefFlowId());
					citedRefs.add(citedRef);
				}

				if(usPatentRef.isNotCited()) {
					notCitedRefs.add(usPatentRef.getRefFlowId());
				}
			}
		}
		if(usPublicationReference.size()>0) {
			for(ReferenceForm usPubRef : usPublicationReference) {
				if(usPubRef.isCited()) {
					CitedReferenceDTO citedRef = new CitedReferenceDTO();
					if(usPubRef.getFilingDate()!=null) {
						citedRef.setFilingDate(BlackboxDateUtil.strToCalendar(usPubRef.getFilingDate(), TimestampFormat.MMMDDYYYY));
					}
					citedRef.setRefFlowId(usPubRef.getRefFlowId());
					citedRefs.add(citedRef);
				}

				if(usPubRef.isNotCited()) {
					notCitedRefs.add(usPubRef.getRefFlowId());
				}
			}
		}
		if(fpReference.size()>0) {
			for(ReferenceForm fpRef : fpReference) {
				if(fpRef.isCited()) {
					CitedReferenceDTO citedRef = new CitedReferenceDTO();
					if(fpRef.getFilingDate()!=null) {
						citedRef.setFilingDate(BlackboxDateUtil.strToCalendar(fpRef.getFilingDate(), TimestampFormat.MMMDDYYYY));
					}
					citedRef.setRefFlowId(fpRef.getRefFlowId());
					citedRefs.add(citedRef);
				}

				if(fpRef.isNotCited()) {
					notCitedRefs.add(fpRef.getRefFlowId());
				}
			}
		}
		if(nplReference.size()>0) {
			for(ReferenceForm nplRef : nplReference) {
				if(nplRef.isCited()) {
					CitedReferenceDTO citedRef = new CitedReferenceDTO();
					if(nplRef.getFilingDate()!=null) {
						citedRef.setFilingDate(BlackboxDateUtil.strToCalendar(nplRef.getFilingDate(), TimestampFormat.MMMDDYYYY));
					}
					citedRef.setRefFlowId(nplRef.getRefFlowId());
					citedRefs.add(citedRef);
				}

				if(nplRef.isNotCited()) {
					notCitedRefs.add(nplRef.getRefFlowId());
				}
			}
		}
		dto.setCitedRef(citedRefs);
		dto.setNotCitedRef(notCitedRefs);
		dto.setNotificationProcessId(notificationProcessId);
		dto.setIdsFilingInfoId(idsId);
		return dto;
	}
	
	public List<ReferenceForm> getUsPatentReference() {
		return usPatentReference;
	}
	public void setUsPatentReference(List<ReferenceForm> usPatentReference) {
		this.usPatentReference = usPatentReference;
	}
	public List<ReferenceForm> getUsPublicationReference() {
		return usPublicationReference;
	}
	public void setUsPublicationReference(List<ReferenceForm> usPublicationReference) {
		this.usPublicationReference = usPublicationReference;
	}
	public List<ReferenceForm> getFpReference() {
		return fpReference;
	}
	public void setFpReference(List<ReferenceForm> fpReference) {
		this.fpReference = fpReference;
	}
	public List<ReferenceForm> getNplReference() {
		return nplReference;
	}
	public void setNplReference(List<ReferenceForm> nplReference) {
		this.nplReference = nplReference;
	}
	public Long getIdsId() {
		return idsId;
	}
	public void setIdsId(Long idsId) {
		this.idsId = idsId;
	}
	public Long getNotificationProcessId() {
		return notificationProcessId;
	}
	public void setNotificationProcessId(Long notificationProcessId) {
		this.notificationProcessId = notificationProcessId;
	}
}
