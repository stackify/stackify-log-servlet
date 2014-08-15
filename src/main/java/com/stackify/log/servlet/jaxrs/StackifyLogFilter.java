/*
 * StackifyLogFilter.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet.jaxrs;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stackify.api.WebRequestDetail;
import com.stackify.api.common.log.ServletLogContext;
import com.stackify.log.servlet.HttpServletRequests;

/**
 * StackifyLogFilter
 * @author Eric Martin
 */
@Provider
public class StackifyLogFilter implements ContainerRequestFilter, ContainerResponseFilter {

	/**
	 * The filter logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StackifyLogFilter.class);

	/**
	 * The injected HTTP servlet request
	 */
    @Context
    private HttpServletRequest httpRequest;
    
	/**
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(final ContainerRequestContext requestContext) {
		
		long start = System.currentTimeMillis();

		// generate a UUID for the transaction
		// get details from the servlet request

		try {

			String transactionId = UUID.randomUUID().toString();
			ServletLogContext.putTransactionId(transactionId);
						
			if (httpRequest != null) {
				
				String user = httpRequest.getRemoteUser();
				ServletLogContext.putUser(user);

				WebRequestDetail webRequest = HttpServletRequests.getWebRequest(httpRequest);				
				ServletLogContext.putWebRequest(webRequest);
			}

		} catch (Throwable t) {
			LOGGER.debug("Unable to populate MDC details", t);
		}
		
		LOGGER.debug("StackifyLogFilter.filter(request): {}ms", System.currentTimeMillis() - start);
	}

	/**
	 * @see javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
	 */
	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
		
		long start = System.currentTimeMillis();

		// clear out our MDC properties if they exist
		
		try {
			ServletLogContext.clear();
		} catch (Throwable t) {
			LOGGER.debug("Unable to clear MDC details", t);
		}
		
		LOGGER.debug("StackifyLogFilter.filter(response): {}ms", System.currentTimeMillis() - start);
	}

	/**
	 * @param httpRequest the httpRequest to set
	 */
	public void setHttpRequest(final HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
}
