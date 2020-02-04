package kr.co.micube.component.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kr.co.micube.component.service.impl.SvnClientServiceImpl;

class SvnServiceTest {

	@Test
	void testHasNewRevision() {
		SvnService svnService = new SvnClientServiceImpl();
		assertTrue(svnService.hasNewRevision());
	}

}
