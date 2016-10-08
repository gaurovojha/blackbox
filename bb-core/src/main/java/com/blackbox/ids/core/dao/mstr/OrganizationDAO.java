/**
 *
 */
package com.blackbox.ids.core.dao.mstr;

import java.util.Collection;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.mstr.Organization;

/**
 * @author ajay2258
 *
 */
public interface OrganizationDAO extends BaseDAO<Organization, Long> {

	public Map<String, Long> mapOrganizationNameIds(final Collection<String> organizationNames);

}
