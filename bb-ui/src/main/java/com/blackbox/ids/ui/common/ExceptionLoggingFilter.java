package com.blackbox.ids.ui.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.LoggerFactory;

public class ExceptionLoggingFilter implements Filter {
	
	protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
			ServletException {
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw e;
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
