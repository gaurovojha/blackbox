/**
 *
 */
package com.blackbox.ids.services.common;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.dao.ApplicationBaseDAO;
import com.blackbox.ids.it.AbstractIntegrationTest;

/**
 * @author ajay2258
 *
 */
public class MasterDataServiceTest extends AbstractIntegrationTest {

	@Autowired
	private ApplicationBaseDAO applicationBaseDAO;

	@Test(expected = NoResultException.class)
	public void testGetAttroneyDocketFormat() {
		Assert.assertNotNull("Attorney docket number format must be defined in setup.",
				applicationBaseDAO.getAttorneyDocketFormat());
	}

}
