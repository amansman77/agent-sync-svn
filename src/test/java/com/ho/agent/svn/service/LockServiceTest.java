package com.ho.agent.svn.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

class LockServiceTest {

	private final File lockFile = new File("C:/test-svn-directory/Study_CI/ho-ci.lock");
	
	@Test
	void testIsLocked() {
		try {
			lockFile.createNewFile();
		} catch (IOException e) {
			fail("Cannot create file: " + lockFile.getAbsolutePath());
		}
		
		LockService lockService = new LockService();
		assertTrue(lockService.isLocked());
		
		lockFile.delete();
	}
	
	@Test
	public void testLock() {
		LockService lockService = new LockService();
		lockService.lock();
		
		lockFile.delete();
	}
	
	@Test
	public void testUnLock() {
		try {
			lockFile.createNewFile();
		} catch (IOException e) {
			fail("Cannot create file: " + lockFile.getAbsolutePath());
		}
		
		LockService lockService = new LockService();
		lockService.unLock();
	}
	
	@Test
	public void testGetLockFilePath() {
		LockService lockService = new LockService();
		assertEquals(lockFile.getAbsolutePath(), lockService.getLockFilePath());
	}

}
