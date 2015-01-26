/*
 * HttpServletRequestsTest.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.stackify.api.WebRequestDetail;

/**
 * HttpServletRequestsTest
 * @author Eric Martin
 */
public class HttpServletRequestsTest {

	/**
	 * testUserIpAddress
	 */
	@Test
	public void testUserIpAddress() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);
		Assert.assertEquals("127.0.0.1", wrd.getUserIpAddress());
	}
	
	/**
	 * testRequestProtocol
	 */
	@Test
	public void testRequestProtocol() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getProtocol()).thenReturn("HTTP");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);
		Assert.assertEquals("HTTP", wrd.getRequestProtocol());
	}
	
	/**
	 * testRequestUrlRoot
	 */
	@Test
	public void testRequestUrlRoot() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRequestURI()).thenReturn("/path/something");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);
		Assert.assertEquals("/path/something", wrd.getRequestUrlRoot());
	}
	
	/**
	 * testHttpMethod
	 */
	@Test
	public void testHttpMethod() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getMethod()).thenReturn("GET");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);
		Assert.assertEquals("GET", wrd.getHttpMethod());
	}
	
	/**
	 * testRequestUrl
	 */
	@Test
	public void testRequestUrl() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/test"));
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals("http://localhost/test", wrd.getRequestUrl());
	}
	
	/**
	 * testReferralUrl
	 */
	@Test
	public void testReferralUrl() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("referer")).thenReturn("http://localhost/test");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals("http://localhost/test", wrd.getReferralUrl());
	}
	
	/**
	 * testHeaders
	 */
	@Test
	public void testHeaders() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		List<String> headerNames = new ArrayList<String>();
		headerNames.add("cookie");
		headerNames.add("h");
		headerNames.add("multitest");
		
		Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(headerNames));
		Mockito.when(request.getHeaders("h")).thenReturn(Collections.enumeration(Collections.singletonList("v")));

		List<String> multitest = new ArrayList<String>();
		multitest.add("a");
		multitest.add("b");
		multitest.add("c");
		Mockito.when(request.getHeaders("multitest")).thenReturn(Collections.enumeration(multitest));

		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals(3, wrd.getHeaders().size());
		Assert.assertEquals("X-MASKED-X", wrd.getHeaders().get("cookie"));
		Assert.assertEquals("v", wrd.getHeaders().get("h"));
		Assert.assertEquals("a,b,c", wrd.getHeaders().get("multitest"));
	}
	
	/**
	 * testCookies
	 */
	@Test
	public void testCookies() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("name", "value")});
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals(1, wrd.getCookies().size());
		Assert.assertEquals("X-MASKED-X", wrd.getCookies().get("name"));
	}
	
	/**
	 * testSessionData
	 */
	@Test
	public void testSessionData() {
		HttpSession session = Mockito.mock(HttpSession.class);
		Mockito.when(session.getAttributeNames()).thenReturn(Collections.enumeration(Collections.singletonList("attr")));
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getSession(false)).thenReturn(session);

		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals(1, wrd.getSessionData().size());
		Assert.assertEquals("X-MASKED-X", wrd.getSessionData().get("attr"));
	}
	
	/**
	 * testQueryString
	 */
	@Test
	public void testQueryString() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getQueryString()).thenReturn("k=v");
		
		WebRequestDetail wrd = HttpServletRequests.getWebRequest(request);		
		Assert.assertEquals(1, wrd.getQueryString().size());
		Assert.assertEquals("v", wrd.getQueryString().get("k"));
	}
}
