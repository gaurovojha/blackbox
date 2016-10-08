/**
 *
 */
package com.blackbox.ids.core.dao.mstr;

import java.util.Collection;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.mstr.Jurisdiction;

/**
 * @author ajay2258
 *
 */
public interface JurisdictionDAO extends BaseDAO<Jurisdiction, Long> {

	public Map<String, Long> mapJurisdictionNameIds(final Collection<String> jurisdictionCodes);

	public Long getIdByCode(final String jurisdictionCode);

}
