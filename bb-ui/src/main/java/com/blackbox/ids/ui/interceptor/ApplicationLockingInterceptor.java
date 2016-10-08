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
import com.blackbox.ids.core.model.EntityUser.EntityName;
import com.blackbox.ids.services.common.EntityUserService;
import com.blackbox.ids.ui.common.Constants;

/**
 * @author ajay2258
 *
 */
@Component
public class ApplicationLockingInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private EntityUserService entityUserService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws ApplicationException {

		HttpSession session = request.getSession();
		final String requestURI = request.getRequestURI();
		final String pathVariable = requestURI.substring(requestURI.lastIndexOf('/') + 1);

		if (isNumeric(pathVariable)) {
			long applicationId = Long.parseLong(pathVariable);
			Boolean lockAcquired = entityUserService.checkAndGetLockOnEntity(EntityName.APPLICATION_BASE, applicationId);
			session.setAttribute(Constants.APPLICATION_LOCK_ACQUIRED, lockAcquired);
			return lockAcquired;
		}
		return true;
	}

	private static boolean isNumeric(String str) {
		return str.matches("\\d+"); // "-?\\d+(\\.\\d+)?"
	}

}
