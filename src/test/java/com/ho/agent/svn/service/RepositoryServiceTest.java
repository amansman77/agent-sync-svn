package com.ho.agent.svn.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ho.agent.svn.service.impl.SvnClientServiceImpl;

class RepositoryServiceTest {

	@Test
	void testHasNewRevision() {
		RepositoryService svnService = new SvnClientServiceImpl();
		assertTrue(svnService.hasNewRevision());
	}

}
