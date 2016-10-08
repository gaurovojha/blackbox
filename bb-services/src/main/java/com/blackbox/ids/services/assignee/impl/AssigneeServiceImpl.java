package com.blackbox.ids.services.assignee.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbox.ids.core.model.mstr.Assignee;
import com.blackbox.ids.core.repository.AssigneeRepository;
import com.blackbox.ids.services.assignee.IAssigneeService;

@Service
public class AssigneeServiceImpl implements IAssigneeService {

	@Autowired
	AssigneeRepository assigneeRepository;
	
	/* (non-Javadoc)
	 * @see com.blackbox.ids.services.assignee.IAssigneeService#getAssigneesByIds(java.util.Set)
	 */
	@Override
	public Set<Assignee> getAssigneesByIds(Set<Long> ids) {
		return assigneeRepository.findByIdIn(ids);
	}

	
	
}
