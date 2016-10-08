/**
 *
 */
package com.blackbox.ids.core.dao.impl.mstr;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.mstr.JurisdictionDAO;
import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.model.mstr.QJurisdiction;

/**
 * @author ajay2258
 *
 */
@Repository
public class JurisdictionDaoImpl extends BaseDaoImpl<Jurisdiction, Long> implements JurisdictionDAO {

	@Override
	public Map<String, Long> mapJurisdictionNameIds(Collection<String> jurisdictionCodes) {
		Collection<String> upperCodes = jurisdictionCodes.stream().map(String::toUpperCase).collect(Collectors.toSet());
		QJurisdiction jurisdiction = QJurisdiction.jurisdiction;
		return getJPAQuery().from(jurisdiction)
				.where(jurisdiction.code.upper().in(upperCodes))
				.map(jurisdiction.code.upper(), jurisdiction.id);
	}

	@Override
	public Long getIdByCode(final String jurisdictionCode) {
		QJurisdiction jurisdiction = QJurisdiction.jurisdiction;
		return getJPAQuery().from(jurisdiction)
				.where(jurisdiction.code.equalsIgnoreCase(jurisdictionCode))
				.uniqueResult(jurisdiction.id);
	}

}
