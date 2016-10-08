package com.blackbox.ids.services.impl.referenceflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.reference.SourceReference;
import com.blackbox.ids.core.model.referenceflow.IDSSourceReferenceFlow;
import com.blackbox.ids.core.repository.referenceflow.IDSSourceReferenceFlowRepository;
import com.blackbox.ids.services.referenceflow.IDSSourceReferenceFlowService;

/**
 * Handles source reference flow related requests
 * 
 * @author nagarro
 *
 */
@Service
public class IDSSourceReferenceFlowServiceImpl implements IDSSourceReferenceFlowService {

	@Autowired
	private IDSSourceReferenceFlowRepository<IDSSourceReferenceFlow> iDSSourceReferenceFlowRepository;

	@Override
	public boolean duplicateCheck(final SourceReference sourceReference, final Long targetApplicationId) {
		return iDSSourceReferenceFlowRepository.duplicateCheck(sourceReference, targetApplicationId);
	}

	@Override
	public boolean dropSourceReferenceFlow(final Long sourcereferenceId, final Long sourceApplicationId) {
		return iDSSourceReferenceFlowRepository.dropSourceReferenceFlow(sourcereferenceId, sourceApplicationId);
	}

}