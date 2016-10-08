/**
 *
 */
package com.blackbox.ids.ui.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.auth.BlackboxSecurityContextHolder;
import com.blackbox.ids.core.model.EntityUser.EntityName;
import com.blackbox.ids.services.common.EntityUserService;
import com.blackbox.ids.ui.common.Constants;

/**
 * @author ajay2258
 */
@Component
public class ApplicationLockReleaseInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private EntityUserService entityUserService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws ApplicationException {

		HttpSession session = request.getSession();
		Boolean isLockAcquiredOnFile = (Boolean) session.getAttribute(Constants.APPLICATION_LOCK_ACQUIRED);

		if (isLockAcquiredOnFile != null && isLockAcquiredOnFile) {
			entityUserService.resetLocksHeldByUser(BlackboxSecurityContextHolder.getUserId(),
					EntityName.APPLICATION_BASE);
			session.setAttribute(Constants.APPLICATION_LOCK_ACQUIRED, false);
		}

		return true;
	}

}
