/*
 * QueryStringsTest.java
 * Copyright 2014 Stackify
 */
package com.stackify.log.servlet;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * QueryStringsTest
 * @author Eric Martin
 */
public class QueryStringsTest {

	/**
	 * testToMapWithAmpersands
	 */
	@Test
	public void testToMapWithAmpersands() {
		
		Map<String, String> qs = QueryStrings.toMap("n1=v1&n2=v2&n1=v3");

		Assert.assertNotNull(qs);
		Assert.assertEquals(2, qs.size());
		Assert.assertEquals("v1,v3", qs.get("n1"));
		Assert.assertEquals("v2", qs.get("n2"));
	}
	
	/**
	 * testToMapWithSemicolons
	 */
	@Test
	public void testToMapWithSemicolons() {
		
		Map<String, String> qs = QueryStrings.toMap("n1=v1;n2=v2;n1=v3");

		Assert.assertNotNull(qs);
		Assert.assertEquals(2, qs.size());
		Assert.assertEquals("v1,v3", qs.get("n1"));
		Assert.assertEquals("v2", qs.get("n2"));
	}
	
	/**
	 * testToMapWithDecoding
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testToMapWithDecoding() throws UnsupportedEncodingException {
		
		Map<String, String> qs = QueryStrings.toMap("n1%3Dv1%26n2%3Dv2%26n1%3Dv3");

		Assert.assertNotNull(qs);
		Assert.assertEquals(2, qs.size());
		Assert.assertEquals("v1,v3", qs.get("n1"));
		Assert.assertEquals("v2", qs.get("n2"));
	}
}
