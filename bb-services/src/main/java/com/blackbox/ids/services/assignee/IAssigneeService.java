package com.blackbox.ids.services.assignee;

import java.util.Set;

import com.blackbox.ids.core.model.mstr.Assignee;

public interface IAssigneeService {

	Set<Assignee> getAssigneesByIds(Set<Long> id);
}
