/**
 *
 */
package com.blackbox.ids.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.AttorneyUserDetailsDAO;
import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dto.IDS.QUserDetails;
import com.blackbox.ids.core.dto.IDS.UserDetails;
import com.blackbox.ids.core.model.AttorneyUserDetails;
import com.blackbox.ids.core.model.QAttorneyUserDetails;
import com.blackbox.ids.core.model.QUser;

/**
 * @author ajay2258
 *
 */
@Repository
public class AttorneyUserDetailsDaoImpl extends BaseDaoImpl<AttorneyUserDetails, Long>
implements AttorneyUserDetailsDAO {

	@Override
	public List<UserDetails> fetchUserDetails(List<Long> userIds) {
		QAttorneyUserDetails attorney = QAttorneyUserDetails.attorneyUserDetails;
		QUser user = QUser.user;
		return getJPAQuery().from(attorney).innerJoin(attorney.user, user).where(user.id.in(userIds))
				.list(new QUserDetails(user.id, user.person.firstName.concat(" ").concat(user.person.lastName),
						attorney.registrationNo));
	}

}
