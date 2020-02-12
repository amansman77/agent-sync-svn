package com.ho.agent.svn.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ho.agent.svn.mapper.RepositoryMapper;

class AppPropertyTest {

	@Test
	void testGetProperty() {
		assertEquals("https://ho-dev-1/svn/Study_CI", AppProperty.getInstance().getProperty("svn.url"));
	}

	@Test
	void testGetRepositorys() {
		List<RepositoryMapper> repositorys = RepositoryMapper.getRepositorys();
		
		assertNotNull(repositorys);
		assertEquals(3, repositorys.size());
	}
	
}
