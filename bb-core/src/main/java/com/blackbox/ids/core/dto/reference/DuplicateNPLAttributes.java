package com.blackbox.ids.core.dto.reference;

import com.blackbox.ids.core.model.reference.ReferenceStatus;

public class DuplicateNPLAttributes {
	
	private boolean createFYANotification;
	
	private ReferenceStatus referenceStatus;
	
	private boolean referenceFlowFlag;

	public boolean isCreateFYANotification() {
		return createFYANotification;
	}

	public void setCreateFYANotification(boolean createFYANotification) {
		this.createFYANotification = createFYANotification;
	}

	public ReferenceStatus getReferenceStatus() {
		return referenceStatus;
	}

	public void setReferenceStatus(ReferenceStatus referenceStatus) {
		this.referenceStatus = referenceStatus;
	}

	public boolean isReferenceFlowFlag() {
		return referenceFlowFlag;
	}

	public void setReferenceFlowFlag(boolean referenceFlowFlag) {
		this.referenceFlowFlag = referenceFlowFlag;
	}
	
	
	

}
