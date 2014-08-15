/*
 * QueryStrings.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * QueryStrings
 * @author Eric Martin
 */
public class QueryStrings {

	/**
	 * Returns a Map from query parameter name to value
	 * @param request The HTTP servlet request
	 * @return Map from query parameter name to value
	 */
	public static Map<String, String> toMap(final String queryString) {
		Preconditions.checkNotNull(queryString);

		Map<String, String> queryMap = Maps.newHashMap();

		String decoded = queryString;
		
		try {
			decoded = URLDecoder.decode(queryString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing
		}
		
		String[] params = decoded.split("[&;]");
		
		if ((params != null) && (0 < params.length)) {
		
			for (String param : params) {

				String[] kv = param.split("=");
				
				if ((kv != null) && (kv.length == 2)) {
				
					String key = kv[0];
					String value = kv[1];
					
					if (queryMap.containsKey(key)) {
						value = queryMap.get(key) + "," + value;
					}
					
					queryMap.put(key, value);
				}
			}
		}
		
		return queryMap;
	}
	
	/**
	 * Hidden to prevent construction
	 */
	private QueryStrings() {
	}
}
