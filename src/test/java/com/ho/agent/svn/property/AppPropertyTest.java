package com.ho.agent.svn.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ho.agent.svn.property.AppProperty;

class AppPropertyTest {

	@Test
	void testGetProperty() {
		assertEquals("https://ho-dev-1/svn/Study_CI", AppProperty.getInstance().getProperty("svn.url"));
	}

}
