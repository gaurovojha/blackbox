/**
 *
 */
package com.blackbox.ids.core.dao.impl.mstr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.blackbox.ids.core.dao.impl.base.BaseDaoImpl;
import com.blackbox.ids.core.dao.mstr.CustomerDAO;
import com.blackbox.ids.core.model.mstr.Customer;
import com.blackbox.ids.core.model.mstr.QCustomer;

/**
 * @author ajay2258
 *
 */
@Repository
public class CustomerDaoImpl extends BaseDaoImpl<Customer, Long> implements CustomerDAO {

	@Override
	public Map<String, Long> mapCustomerNameIds(Collection<String> customerNumbers) {
		Collection<String> upperNames = customerNumbers.stream().map(String::toUpperCase).collect(Collectors.toSet());
		QCustomer customer = QCustomer.customer;
		return getJPAQuery().from(customer)
				.where(customer.customerNumber.upper().in(upperNames))
				.map(customer.customerNumber.upper(), customer.id);
	}

	@Override
	public List<String> findCustomerNumbersByIdsIn(final Collection<Long> customerIds) {
		QCustomer customer = QCustomer.customer;
		return getJPAQuery().from(customer)
				.where(customer.id.in(customerIds))
				.list(customer.customerNumber);
	}

}
