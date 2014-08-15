/*
 * StackifyLogFilterTest.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet.jaxrs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.WebRequestDetail;
import com.stackify.api.common.log.ServletLogContext;
import com.stackify.log.servlet.HttpServletRequests;

/**
 * StackifyLogFilterTest
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StackifyLogFilter.class, ServletLogContext.class, HttpServletRequests.class})
public class StackifyLogFilterTest {

	/**
	 * testFilterRequestWithoutHttpServletRequest
	 */
	@Test
	public void testFilterRequestWithoutHttpServletRequest() {
				
		PowerMockito.mockStatic(ServletLogContext.class);
		
		StackifyLogFilter filter = new StackifyLogFilter();
		filter.filter(Mockito.mock(ContainerRequestContext.class));
		
		PowerMockito.verifyStatic();
		ServletLogContext.putTransactionId(Mockito.anyString());
		
		PowerMockito.verifyStatic(Mockito.times(0));
		ServletLogContext.putUser(Mockito.anyString());
		
		PowerMockito.verifyStatic(Mockito.times(0));
		ServletLogContext.putWebRequest(Mockito.any(WebRequestDetail.class));
	}
	
	/**
	 * testFilterRequest
	 */
	@Test
	public void testFilterRequest() {
		
		PowerMockito.mockStatic(ServletLogContext.class);
		PowerMockito.mockStatic(HttpServletRequests.class);
		
		HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
		
		StackifyLogFilter filter = new StackifyLogFilter();	
		filter.setHttpRequest(httpRequest);
		filter.filter(Mockito.mock(ContainerRequestContext.class));
		
		PowerMockito.verifyStatic();
		ServletLogContext.putTransactionId(Mockito.anyString());
		
		PowerMockito.verifyStatic();
		ServletLogContext.putUser(Mockito.anyString());
		
		PowerMockito.verifyStatic();
		ServletLogContext.putWebRequest(Mockito.any(WebRequestDetail.class));
	}
	
	/**
	 * testFilterResponse
	 */
	@Test
	public void testFilterResponse() {

		PowerMockito.mockStatic(ServletLogContext.class);

		StackifyLogFilter filter = new StackifyLogFilter();
		filter.filter(Mockito.mock(ContainerRequestContext.class), Mockito.mock(ContainerResponseContext.class));

		PowerMockito.verifyStatic();
		ServletLogContext.clear();
	}
}
