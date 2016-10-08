package com.blackbox.ids.services.usermanagement;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.blackbox.ids.core.model.Nationality;
import com.blackbox.ids.core.model.Person;
import com.blackbox.ids.core.model.Role;
import com.blackbox.ids.core.model.User;
import com.blackbox.ids.core.model.mstr.PersonClassification;
import com.blackbox.ids.core.util.date.TimestampFormat;
import com.blackbox.ids.it.AbstractIntegrationTest;

public class UserServiceTest extends AbstractIntegrationTest {

	private String TEST_PASSWORD = "Test@1234";
	public static final TimestampFormat DATE_FORMAT = TimestampFormat.MMDDYYYY;

	@Autowired
	private UserService userService;

	/** Tests the Generate and Send Password : Positive Scenario */
	@Test
	public void testGenerateAndSendPassword() {
		String loginId = TEST_LOGIN_ID;
		boolean isSent = userService.generateAndSendPassword(loginId);
		Assert.assertEquals(true, isSent);
	}

	/** Tests the Generate and Send Password : Login Id is null - Negative Scenario */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAndSendPassword_1() {
		String loginId = null;
		boolean isSent = userService.generateAndSendPassword(loginId);
		Assert.assertEquals(true, isSent);
	}

	/** Tests the Send OTP : Positive Scenario */
	@Test
	public void testSendOTP() {
		String loginId = TEST_LOGIN_ID;
		boolean isSent = userService.sendOTP(loginId);
		Assert.assertEquals(true, isSent);
	}

	/** Tests the Send OTP : Login Id is null - Negative Scenario */
	@Test(expected = IllegalArgumentException.class)
	public void testSendOTP_1() {
		String loginId = null;
		boolean isSent = userService.sendOTP(loginId);
		Assert.assertEquals(true, isSent);
	}

	/** Tests the User Full Name : Positive Scenario */
	@Test
	public void testUserFullName() {
		String loginId = TEST_LOGIN_ID;
		String fullName = userService.getUserFullName(loginId);
		Assert.assertTrue(!StringUtils.isEmpty(fullName));
	}

	/** Tests the User Full Name : Negative Scenario */
	@Test
	public void testUserFullName_1() {
		String loginId = null;
		String fullName = userService.getUserFullName(loginId);
		Assert.assertTrue(StringUtils.isEmpty(fullName));
	}

	/** Tests the Save Password : Positive Scenario */
	@Test
	public void testSavePassword() {
		String loginId = TEST_LOGIN_ID;
		String password = TEST_PASSWORD;
		userService.savePassword(loginId, password);
	}

	/** Tests the Save Password : Login Id is null - Negative Scenario */
	@Test(expected = IllegalArgumentException.class)
	public void testSavePassword_1() {
		String loginId = null;
		String password = TEST_PASSWORD;
		userService.savePassword(loginId, password);
	}

	/** Tests the Save Password : Login Id is null - Negative Scenario */
	@Test(expected = IllegalArgumentException.class)
	public void testSavePassword_2() {
		String loginId = TEST_LOGIN_ID;
		String password = null;
		userService.savePassword(loginId, password);
	}

	/** Tests the Fetch User by user id : Positive Scenario */
	@Test
	public void testFetchUserByUserName() {
		String loginId = TEST_LOGIN_ID;
		User user = userService.getUserByUsername(loginId);
		Assert.assertNotNull(user);
	}

	/** Tests the Fetch User by user id : User Id is null - Negative Scenario */
	@Test
	public void testFetchUserByUserName_1() {
		String loginId = null;
		User user = userService.getUserByUsername(loginId);
		Assert.assertNull(user);
	}

	/** Tests the Lock User Account : Positive Scenario */
	@Test
	public void testLockUserAccount() {
		String loginId = TEST_LOGIN_ID;
		userService.lockUserAccount(loginId);
	}

	/** Tests the Lock User Account :  Login Id is null - Negative Scenario */
	@Test(expected = IllegalArgumentException.class)
	public void testLockUserAccount_1() {
		String loginId = null;
		userService.lockUserAccount(loginId);
	}

	/** Tests the Lock User Account :  Login Id is null - Negative Scenario */
	@Test
	@Transactional
	@Rollback
	public void createNewUser() {
		User user = new User();
		// Set personal details
				user.setId(1L);
				user.setLoginId("test@bbx.com");
				user.setPassword(TEST_PASSWORD);
				user.setIsLocked(false);
				user.setLoginAttempts(0);
				PersonClassification pc = new PersonClassification(1L);
				user.setClassification(pc);
				// Set 'Person' Details
				final Person person = new Person();
				person.setTitle("test designation");
				person.setFirstName("firstName");
				person.setMiddleName("middleName");
				person.setLastName("lastName");
				person.setEmail("test@bbx.com");
				person.setEmployeeId("11111");
				Nationality nationality = new Nationality(1L);
				person.setNationality(nationality);
				user.setPerson(person);

				// Set 'UserRoles'
				Role role = new Role(1L);
				user.getUserRoles().add(role);

		userService.createNewUser(user);
		User createdUser = userService.findByLoginId("test@bbx.com");
		Assert.assertTrue("test@bbx.com".equals(createdUser.getLoginId()));
	}

}
