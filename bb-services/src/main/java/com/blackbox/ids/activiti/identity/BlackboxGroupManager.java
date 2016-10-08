package com.blackbox.ids.activiti.identity;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackbox.ids.services.usermanagement.UserService;

public class BlackboxGroupManager extends AbstractManager implements GroupIdentityManager {

	private static Logger logger = LoggerFactory.getLogger(BlackboxGroupManager.class);

	private final UserService userService;

	public BlackboxGroupManager(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public Group createNewGroup(final String groupId) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support creating a new group");
	}

	@Override
	public void insertGroup(final Group group) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support inserting a group");
	}

	@Override
	public void deleteGroup(final String groupId) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public GroupQuery createNewGroupQuery() {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public List<Group> findGroupByQueryCriteria(final GroupQueryImpl query, final Page page) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public long findGroupCountByNativeQuery(final Map<String, Object> paramMap) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public long findGroupCountByQueryCriteria(final GroupQueryImpl query) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public List<Group> findGroupsByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public List<Group> findGroupsByUser(final String userId) {
		// TODO
		throw new ActivitiException("Blackbox Activiti group manager doesn't support deleting a group");
	}

	@Override
	public void updateGroup(Group group) {

	}

	@Override
	public boolean isNewGroup(Group group) {
		return false;
	}
}
