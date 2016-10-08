/**
 *
 */
package com.blackbox.ids.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.UserDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.user.UserAccessDTO;
import com.blackbox.ids.core.model.QUser;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.repository.UserRepository;
import com.mysema.query.types.ConstructorExpression;

/**
 * @author ajay2258
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDAO {

	 /** The user repository instance. */
    @Autowired
    private UserRepository userRepository;

    /**
     * Instantiates a new user DAO Impl.
     */
    public UserDaoImpl() {
        setBaseRepository(userRepository);
    }

	@Override
	public boolean enableAccess(final Long userId) {
		final QUser user = QUser.user;
		return getJPAUpdateClause(user).set(user.isEnabled, true).where(user.id.eq(userId)).execute() == 1L;
	}

	@Override
	public boolean unlockAccess(final Long userId, String password) {
		final QUser user = QUser.user;
		return getJPAUpdateClause(user).set(user.isLocked, false).set(user.password, password).set(user.isFirstLogin, true).where(user.id.eq(userId)).execute() == 1L;
	}

	@Override
	public UserAccessDTO fetchUserAccessDTO(final Long userId) {
		final QUser user = QUser.user;
		return getJPAQuery().from(user)
			.where(user.id.eq(userId))
			.uniqueResult(ConstructorExpression.create(UserAccessDTO.class,
				user.id, user.isEnabled, user.isLocked, user.isActive));
	}
}
