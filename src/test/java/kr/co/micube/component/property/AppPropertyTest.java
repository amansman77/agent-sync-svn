package kr.co.micube.component.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppPropertyTest {

	@Test
	void testGetProperty() {
		assertEquals("https://micube-dev-1/svn/Study_CI", AppProperty.getInstance().getProperty("svn.url"));
	}

}
