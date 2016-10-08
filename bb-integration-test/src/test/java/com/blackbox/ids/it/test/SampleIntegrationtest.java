package com.blackbox.ids.it.test;

import static org.junit.Assert.assertNotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import com.blackbox.ids.it.AbstractIntegrationTest;

public class SampleIntegrationtest extends AbstractIntegrationTest {
	
	@Autowired NamedParameterJdbcOperations jdbcTemplate;
	
	@Test
	public void sampleTest() {
		
		Integer val = jdbcTemplate.query("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		}).iterator().next();
		
		assertNotNull(val);
	}
	
	@Test
	public void sampleTest2() {
		
		Integer val = jdbcTemplate.query("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		}).iterator().next();
		
		assertNotNull(val);
	}

}
