/*
 * StackifyLogFilterTest.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.stackify.api.WebRequestDetail;
import com.stackify.api.common.log.ServletLogContext;

/**
 * StackifyLogFilterTest
 * @author Eric Martin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StackifyLogFilter.class, ServletLogContext.class, HttpServletRequests.class})
public class StackifyLogFilterTest {
		
	/**
	 * testFilterWithoutHttpServletRequest
	 * @throws ServletException 
	 * @throws IOException 
	 */
	@Test
	public void testFilterWithoutHttpServletRequest() throws IOException, ServletException {
				
		PowerMockito.mockStatic(ServletLogContext.class);
		
		ServletRequest request = Mockito.mock(ServletRequest.class);
		ServletResponse response = Mockito.mock(ServletResponse.class);
		FilterChain chain = Mockito.mock(FilterChain.class);
		
		StackifyLogFilter filter = new StackifyLogFilter();	
		filter.init(Mockito.mock(FilterConfig.class));
		filter.doFilter(request, response, chain);
		filter.destroy();
		
		PowerMockito.verifyStatic();
		ServletLogContext.putTransactionId(Mockito.anyString());
		
		PowerMockito.verifyStatic(Mockito.times(0));
		ServletLogContext.putUser(Mockito.anyString());
		
		PowerMockito.verifyStatic(Mockito.times(0));
		ServletLogContext.putWebRequest(Mockito.any(WebRequestDetail.class));
		
		Mockito.verify(chain).doFilter(request, response);
		
		PowerMockito.verifyStatic();
		ServletLogContext.clear();
	}
	
	/**
	 * testFilterRequest
	 * @throws ServletException 
	 * @throws IOException 
	 */
	@Test
	public void testFilterRequest() throws IOException, ServletException {
		
		PowerMockito.mockStatic(ServletLogContext.class);
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		ServletResponse response = Mockito.mock(ServletResponse.class);
		FilterChain chain = Mockito.mock(FilterChain.class);
		
		StackifyLogFilter filter = new StackifyLogFilter();		
		filter.init(Mockito.mock(FilterConfig.class));
		filter.doFilter(request, response, chain);
		filter.destroy();
		
		PowerMockito.verifyStatic();
		ServletLogContext.putTransactionId(Mockito.anyString());
		
		PowerMockito.verifyStatic();
		ServletLogContext.putUser(Mockito.anyString());
		
		PowerMockito.verifyStatic();
		ServletLogContext.putWebRequest(Mockito.any(WebRequestDetail.class));
		
		Mockito.verify(chain).doFilter(request, response);
		
		PowerMockito.verifyStatic();
		ServletLogContext.clear();
	}
}
