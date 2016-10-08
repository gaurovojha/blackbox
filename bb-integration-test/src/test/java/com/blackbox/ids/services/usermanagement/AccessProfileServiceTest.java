package com.blackbox.ids.services.usermanagement;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.repository.RoleRepository;
import com.blackbox.ids.dto.RoleDTO;
import com.blackbox.ids.dto.usermanagement.AccessProfileDTO;
import com.blackbox.ids.it.AbstractIntegrationTest;

/**
 * @author nagarro
 *
 */
public class AccessProfileServiceTest extends AbstractIntegrationTest {

	@Autowired
	private AccessProfileService accessProfileService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserService userService;
	
	List<Long> accessControlIds = new ArrayList<>();
	Role role = null;
	RoleDTO roleDTO = null;
	List<RoleDTO> roleDTOList = new ArrayList<>();
	
	@BeforeClass
	public void setup() {
		
		for(long i=0; i <= 262; i++ ) {
			accessControlIds.add(i);
		}
		
		role = roleRepository.findOne(2L);
		roleDTO = roleService.getRoleById(2L);
		roleDTOList.add(roleDTO);
	}
	
	
	@Test
	public void testCreateAccessProfile() {
		
		AccessProfileDTO dto = new AccessProfileDTO();
		dto.setAccessControlIds(accessControlIds);
		dto.setRoles(roleDTOList);
		dto.setActive(true);
		dto.setName("TEST_JUNIT_AP");
		dto.setDescription("TEST_JUNIT_AP_DESC");
		dto.setSeeded(false);
		dto.setCreatedBy("test@blackbox.com");
		dto.setCreatedDate("Jan 17, 2016");
		
		accessProfileService.createAccessProfile(dto);
		
		
	}

	@Test
	public void testGetAccessProfileByName() {
		AccessProfileDTO dto = accessProfileService.getAccessProfileByName("ALL_TABS");
		Assert.assertNotNull("Access Profile must not be null", dto);
	}
	
	@Test
	public void testGetAccessProfileById() {
		AccessProfileDTO dto = accessProfileService.getAccessProfileById(2L);
		Assert.assertNotNull("Access Profile must not be null", dto);
	}
	
	@Test
	public void testGetAllAccessProfileNames() {
		List<String> apNames = accessProfileService.getAllAccessProfileNames();
		Assert.assertNotNull("Access Profile name list must not be null", apNames);
		Assert.assertTrue("List must not be empty", apNames.size() > 0);
	}
	
	@Test
	public void testEditAccessProfile() {
		AccessProfileDTO dto = accessProfileService.getAccessProfileByName("TEST_JUNIT_AP");
		dto.setDescription("TEST_JUNIT_AP_DESC_EDIT");
		accessProfileService.updateAccessProfile(dto);
	}
	
	@Test
	public void testInactivateAccessProfile() {
		AccessProfileDTO dto = accessProfileService.getAccessProfileByName("TEST_JUNIT_AP");
		dto.setActive(false);
		//accessProfileService.deactivate(id)
	}
	
	@Test
	public void testActiveAccessProfile() {
		
	}
	
	@Test
	public void testDeleteAccessProfile() {
		
	}
	
	@Test
	public void testUserCount() {
		
	}
	
}