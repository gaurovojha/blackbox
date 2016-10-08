package com.blackbox.ids.it.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.mstr.Jurisdiction;
import com.blackbox.ids.core.repository.JurisdictionRepository;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class JuridictionIntegrationTest extends AbstractIntegrationTest {
	
	@Autowired JurisdictionRepository jurisdictionRepository;
	
	@Test
	public void sampleTest() {
		Jurisdiction jurisdiction = jurisdictionRepository.findOne(1l);
		assertNotNull(jurisdiction);
	}

}
