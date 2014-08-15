/*
 * HttpServletRequests.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.stackify.api.WebRequestDetail;

/**
 * HttpServletRequests
 * @author Eric Martin
 */
public class HttpServletRequests {
	
	/**
	 * Comma Joiner
	 */
	private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
	
	/**
	 * Value Mask
	 */
	private static final String MASKED = "X-MASKED-X";
	
	/**
	 * Gets a WebRequestDetail from the servlet request
	 * @param request The servlet request
	 * @return The WebRequestDetail
	 */
	public static WebRequestDetail getWebRequest(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		WebRequestDetail.Builder wrdBuilder = WebRequestDetail.newBuilder();
				
		wrdBuilder.userIpAddress(request.getRemoteAddr());
		wrdBuilder.requestProtocol(request.getProtocol());
		wrdBuilder.requestUrlRoot(request.getServerName());
		
		wrdBuilder.httpMethod(request.getMethod());
		
		StringBuffer requestUrl = request.getRequestURL();
		
		if (requestUrl != null) {
			wrdBuilder.requestUrl(requestUrl.toString());
		}
		
		wrdBuilder.referralUrl(request.getHeader("referer"));
		
		Optional<Map<String, String>> headers = getHeaders(request);
		
		if (headers.isPresent()) {
			wrdBuilder.headers(headers.get());
		}
				
		Optional<Map<String, String>> cookies = getCookies(request);

		if (cookies.isPresent()) {
			wrdBuilder.cookies(cookies.get());
		}

		Optional<Map<String, String>> sessionAttributes = getSessionAttributes(request);

		if (sessionAttributes.isPresent()) {
			wrdBuilder.sessionData(sessionAttributes.get());
		}
		
		String queryString = request.getQueryString();
		
		if ((queryString != null) && (0 < queryString.length())) {
			wrdBuilder.queryString(QueryStrings.toMap(queryString));
		}
		
		WebRequestDetail wrd = wrdBuilder.build();
		
		return wrd;
	}

	/**
	 * Returns a Map from header name to value (comma separated list)
	 * @param request The HTTP servlet request
	 * @return Map from header name to value
	 */
	private static Optional<Map<String, String>> getHeaders(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		@SuppressWarnings("unchecked")
		Enumeration<String> headerNames = request.getHeaderNames();
		
		if ((headerNames != null) && (headerNames.hasMoreElements())) {
			
			Map<String, String> headerMap = Maps.newHashMap();
			
			while (headerNames.hasMoreElements()) {
				
				String name = headerNames.nextElement();
				
				if (name.equalsIgnoreCase("cookie")) {
					
					headerMap.put(name, MASKED);
					
				} else {

					@SuppressWarnings("unchecked")
					Enumeration<String> values = request.getHeaders(name);
					
					if (values != null) {
						
						String combinedValues = COMMA_JOINER.join(Iterators.forEnumeration(values));
						
						headerMap.put(name, combinedValues);
					}
				}
			}
			
			return Optional.of(headerMap);
		}
		
		return Optional.absent();
	}
	
	/**
	 * Returns a Map from cookie name to masked value
	 * @param request The HTTP servlet request
	 * @return Map from cookie name to masked value
	 */
	private static Optional<Map<String, String>> getCookies(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		Cookie[] cookies = request.getCookies();
		
		if ((cookies != null) && (0 < cookies.length)) {
			
			Map<String, String> cookieMap = Maps.newHashMap();
			
			for (int i = 0; i < cookies.length; ++i) {
				Cookie cookie = cookies[i];				
				String name = cookie.getName();
				cookieMap.put(name, MASKED);
			}

			return Optional.of(cookieMap);
		}
		
		return Optional.absent();
	}
	
	/**
	 * Returns a Map from session attribute name to masked value
	 * @param request The HTTP servlet request
	 * @return Map from session attribute name to masked value
	 */
	private static Optional<Map<String, String>> getSessionAttributes(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		HttpSession session = request.getSession(false);
		
		if (session != null) {
			
			@SuppressWarnings("unchecked")
			Enumeration<String> attributeNames = session.getAttributeNames();
			
			if ((attributeNames != null) && (attributeNames.hasMoreElements())) {
				
				Map<String, String> attributeMap = Maps.newHashMap();
				
				while (attributeNames.hasMoreElements()) {
					String name = attributeNames.nextElement();
					attributeMap.put(name, MASKED);
				}
				
				return Optional.of(attributeMap);
			}			
		}
		
		return Optional.absent();
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private HttpServletRequests() {
	}
}
