/*
 * HttpServletRequests.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.stackify.api.WebRequestDetail;
import com.stackify.api.common.util.Preconditions;

/**
 * HttpServletRequests
 * @author Eric Martin
 */
public class HttpServletRequests {
	
	/**
	 * Value Mask
	 */
	private static final String MASKED = "X-MASKED-X";

    /**
     * Value Mask set
     */
	private static final Set<String> MASKED_SET = initializeMaskSet();
    
    /**
     * Returns an initialized set with all headers that need to be masked.
     * @return Initialized Set of Header Key Names
     */
    public static Set<String> initializeMaskSet() {
        Set<String> maskSet = new HashSet<String>();

        maskSet.add("cookie");
        maskSet.add("authorization");

        return maskSet;
    }
	
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
		wrdBuilder.requestUrlRoot(request.getRequestURI());
		
		wrdBuilder.httpMethod(request.getMethod());
		
		StringBuffer requestUrl = request.getRequestURL();
		
		if (requestUrl != null) {
			wrdBuilder.requestUrl(requestUrl.toString());
		}
		
		wrdBuilder.referralUrl(request.getHeader("referer"));
		
		Map<String, String> headers = getHeaders(request);
		
		if (headers != null) {
			wrdBuilder.headers(headers);
		}
				
		Map<String, String> cookies = getCookies(request);

		if (cookies != null) {
			wrdBuilder.cookies(cookies);
		}

		Map<String, String> sessionAttributes = getSessionAttributes(request);

		if (sessionAttributes != null) {
			wrdBuilder.sessionData(sessionAttributes);
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
	private static Map<String, String> getHeaders(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		@SuppressWarnings("unchecked")
		Enumeration<String> headerNames = request.getHeaderNames();
		
		if ((headerNames != null) && (headerNames.hasMoreElements())) {
			
			Map<String, String> headerMap = new HashMap<String, String>();
			
			while (headerNames.hasMoreElements()) {
				
				String name = headerNames.nextElement();
				
				if (MASKED_SET.contains(name)) {
					
					headerMap.put(name, MASKED);
					
				} else {

					@SuppressWarnings("unchecked")
					Enumeration<String> values = request.getHeaders(name);
					
					if (values != null) {
						
						StringBuilder sb = new StringBuilder();
						
						while (values.hasMoreElements()) {
							
							String value = values.nextElement();
							
							if (value != null) {
								sb.append(value);
								
								if (values.hasMoreElements()) {
									sb.append(",");
								}
							}
						}
						
						String combinedValues = sb.toString();
												
						headerMap.put(name, combinedValues);
					}
				}
			}
			
			return headerMap;
		}
		
		return null;
	}
	
	/**
	 * Returns a Map from cookie name to masked value
	 * @param request The HTTP servlet request
	 * @return Map from cookie name to masked value
	 */
	private static Map<String, String> getCookies(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		Cookie[] cookies = request.getCookies();
		
		if ((cookies != null) && (0 < cookies.length)) {
			
			Map<String, String> cookieMap = new HashMap<String, String>();
			
			for (int i = 0; i < cookies.length; ++i) {
				Cookie cookie = cookies[i];				
				String name = cookie.getName();
				cookieMap.put(name, MASKED);
			}

			return cookieMap;
		}
		
		return null;
	}
	
	/**
	 * Returns a Map from session attribute name to masked value
	 * @param request The HTTP servlet request
	 * @return Map from session attribute name to masked value
	 */
	private static Map<String, String> getSessionAttributes(final HttpServletRequest request) {
		Preconditions.checkNotNull(request);

		HttpSession session = request.getSession(false);
		
		if (session != null) {
			
			@SuppressWarnings("unchecked")
			Enumeration<String> attributeNames = session.getAttributeNames();
			
			if ((attributeNames != null) && (attributeNames.hasMoreElements())) {
				
				Map<String, String> attributeMap = new HashMap<String, String>();
				
				while (attributeNames.hasMoreElements()) {
					String name = attributeNames.nextElement();
					attributeMap.put(name, MASKED);
				}
				
				return attributeMap;
			}			
		}
		
		return null;
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private HttpServletRequests() {
	}
}
