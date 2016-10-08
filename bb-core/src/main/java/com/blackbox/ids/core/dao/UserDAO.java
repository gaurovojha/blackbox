/**
 *
 */
package com.blackbox.ids.core.dao;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.user.UserAccessDTO;
import com.blackbox.ids.core.model.User;

/**
 * @author ajay2258
 *
 */
public interface UserDAO extends BaseDAO<User, Long> {

	boolean enableAccess(final Long userId);

	boolean unlockAccess(final Long userId, String password);
	
	UserAccessDTO fetchUserAccessDTO(final Long userId);

}
