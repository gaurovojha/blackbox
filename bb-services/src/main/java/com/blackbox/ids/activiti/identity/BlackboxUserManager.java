package com.blackbox.ids.activiti.identity;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackbox.ids.services.usermanagement.UserService;

public class BlackboxUserManager extends AbstractManager implements UserIdentityManager {

	private static Logger logger = LoggerFactory.getLogger(BlackboxUserManager.class);

	private final UserService userService;

	public BlackboxUserManager(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public Boolean checkPassword(final String userId, final String password) {
		com.blackbox.ids.core.model.User user = userService.getUserByUsername(userId);
		final String uPwd = user.getPassword();
		if (password != null && password.equals(uPwd)) {
			return true;
		}
		return false;
	}

	@Override
	public User createNewUser(final String userId) {
		throw new ActivitiException("Blackbox Activiti user manager doesn't support creating a new user");
	}

	@Override
	public UserQuery createNewUserQuery() {
		return new UserQueryImpl(Context.getProcessEngineConfiguration().getCommandExecutor());
	}

	@Override
	public void deleteUser(final String userId) {
		throw new ActivitiException("Blackbox Activiti user manager doesn't support deleting a user");
	}

	@Override
	public List<Group> findGroupsByUser(final String userId) {
		//TODO
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public List<User> findPotentialStarterUsers(final String proceDefId) {
		throw new ActivitiException("Blackbox Activiti user manager doesn't support querying");
	}

	@Override
	public UserEntity findUserById(final String userId) {
		final com.blackbox.ids.core.model.User userModel = userService.getUserByUsername(userId);
		final UserEntity userEntity = new UserEntity(userId);
		userEntity.setFirstName(userModel.getLoginId());
		userEntity.setPassword(userModel.getPassword());
		return userEntity;
	}

	@Override
	public List<User> findUserByQueryCriteria(final UserQueryImpl query, final Page page) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public long findUserCountByNativeQuery(final Map<String, Object> paramMap) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public long findUserCountByQueryCriteria(final UserQueryImpl query) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public IdentityInfoEntity findUserInfoByUserIdAndKey(final String userId, final String key) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public List<String> findUserInfoKeysByUserIdAndType(final String userId, final String type) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public List<User> findUsersByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public void insertUser(final User user) {
		throw new ActivitiException("Blackbox Activiti manager doesn't support inserting a new user");
	}

	@Override
	public void updateUser(User user) {

	}

	@Override
	public boolean isNewUser(User user) {
		return false;
	}

	@Override
	public Picture getUserPicture(String s) {
		return null;
	}

	@Override
	public void setUserPicture(String s, Picture picture) {

	}
}
