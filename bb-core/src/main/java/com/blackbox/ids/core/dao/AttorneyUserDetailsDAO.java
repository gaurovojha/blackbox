/**
 *
 */
package com.blackbox.ids.core.dao;

import java.util.List;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.dto.IDS.UserDetails;
import com.blackbox.ids.core.model.AttorneyUserDetails;

/**
 * Defines databases APIs over entity class {@link AttorneyUserDetails}.
 *
 * @author ajay2258
 */
public interface AttorneyUserDetailsDAO extends BaseDAO<AttorneyUserDetails, Long> {

	public List<UserDetails> fetchUserDetails(final List<Long> userIds);

}
