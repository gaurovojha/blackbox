/**
 *
 */
package com.blackbox.ids.core.dao.mstr;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.blackbox.ids.core.dao.base.BaseDAO;
import com.blackbox.ids.core.model.mstr.Customer;

/**
 * @author ajay2258
 *
 */
public interface CustomerDAO extends BaseDAO<Customer, Long> {

	public Map<String, Long> mapCustomerNameIds(final Collection<String> customerNumbers);

	public List<String> findCustomerNumbersByIdsIn(final Collection<Long> customerIds);

}
