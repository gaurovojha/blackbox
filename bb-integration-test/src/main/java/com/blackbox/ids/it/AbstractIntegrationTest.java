package com.blackbox.ids.it;

import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blackbox.ids.core.auth.BlackboxUser;
import com.blackbox.ids.core.common.BbxApplicationContextUtil;
import org.springframework.security.core.userdetails.UserDetailsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/integration-test.xml")
@Category(IntegrationTest.class)
@ActiveProfiles("integration")
@Transactional
public abstract class AbstractIntegrationTest {

	protected static final String TEST_LOGIN_ID = "junit@bbx.com";
	protected static final Long TEST_USER_ID = 2L;

	protected void setTestUser() {
		UserDetailsService detailsService = (UserDetailsService) BbxApplicationContextUtil.getBean("userDetailService");
		BlackboxUser userDetails = (BlackboxUser) detailsService.loadUserByUsername(TEST_LOGIN_ID);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
