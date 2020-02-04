package com.ho.agent.svn.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.ho.agent.svn.service.SvnService;
import com.ho.agent.svn.service.impl.SvnClientServiceImpl;

class SvnServiceTest {

	@Test
	void testHasNewRevision() {
		SvnService svnService = new SvnClientServiceImpl();
		assertTrue(svnService.hasNewRevision());
	}

}
