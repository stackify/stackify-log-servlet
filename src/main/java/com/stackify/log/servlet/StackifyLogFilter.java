/*
 * StackifyLogFilter.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stackify.api.WebRequestDetail;
import com.stackify.api.common.log.ServletLogContext;

/**
 * StackifyLogFilter
 * @author Eric Martin
 */
public class StackifyLogFilter implements Filter {

	/**
	 * The filter logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StackifyLogFilter.class);

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig config) {
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		long start = System.currentTimeMillis();

		// generate a UUID for the transaction
		// get details from the servlet request

		try {
			
			String transactionId = UUID.randomUUID().toString();
			ServletLogContext.putTransactionId(transactionId);
						
			if (request instanceof HttpServletRequest) {
			
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				
				String user = httpRequest.getRemoteUser();
				ServletLogContext.putUser(user);
				
				WebRequestDetail webRequest = HttpServletRequests.getWebRequest((HttpServletRequest) httpRequest);
				ServletLogContext.putWebRequest(webRequest);
			}
						
		} catch (Throwable t) {
			LOGGER.debug("Unable to populate MDC details", t);
		}
		
		LOGGER.debug("StackifyLogFilter.filter(request): {}ms", System.currentTimeMillis() - start);

		// send the request to the next filter in the chain
		
		try {
			
			chain.doFilter(request, response);
			
		} finally {
			
			start = System.currentTimeMillis();
			
			// clear out our MDC properties if they exist
			
			try {
				ServletLogContext.clear();
			} catch (Throwable t) {
				LOGGER.debug("Unable to clear MDC details", t);
			}
			
			LOGGER.debug("StackifyLogFilter.filter(response): {}ms", System.currentTimeMillis() - start);
		}
	}
}
