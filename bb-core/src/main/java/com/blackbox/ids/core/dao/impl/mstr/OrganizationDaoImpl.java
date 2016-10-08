/**
 *
 */
package com.blackbox.ids.core.dao.impl.mstr;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.mstr.OrganizationDAO;
import com.blackbox.ids.core.model.mstr.Organization;
import com.blackbox.ids.core.model.mstr.QOrganization;

/**
 * @author ajay2258
 *
 */
@Repository
public class OrganizationDaoImpl extends BaseDaoImpl<Organization, Long> implements OrganizationDAO {

	@Override
	public Map<String, Long> mapOrganizationNameIds(Collection<String> organizationNames) {
		Collection<String> upperNames = organizationNames.stream().map(String::toUpperCase).collect(Collectors.toSet());
		QOrganization organization = QOrganization.organization;
		return getJPAQuery().from(organization)
				.where(organization.name.upper().in(upperNames))
				.map(organization.name.upper(), organization.id);
	}

}
