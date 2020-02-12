package com.ho.agent.svn.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ho.agent.svn.constant.Constant.PropertyKey;
import com.ho.agent.svn.constant.Constant.Service;
import com.ho.agent.svn.property.AppProperty;

public class LockService {

	private AppProperty appProperty = AppProperty.getInstance();
	
	private File lockFile;
	
	public LockService() {
		String targetPath = appProperty.getProperty(PropertyKey.SVN_TARGET_SOURCE_PATH);
		lockFile = new File(targetPath + "/" + Service.LOCK_FILE_NAME);
	}
	
	public LockService(String targetPath) {
		lockFile = new File(targetPath + "/" + Service.LOCK_FILE_NAME);
	}
	
	public boolean isLocked() {
		if (lockFile.exists()) {
			return true;
		}
		return false;
	}

	public void lock() {
		try {
			lockFile.getParentFile().mkdirs();
			lockFile.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("error while create lock file: " + lockFile.getAbsolutePath());
		}
		
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(lockFile);
			fileWriter.write("SvnService" + System.lineSeparator());
			fileWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
		} catch (IOException e) {
			throw new RuntimeException("error while write lock info: " + lockFile.getAbsolutePath());
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				throw new RuntimeException("error while close lock file: " + lockFile.getAbsolutePath());
			}
		}
	}

	public void unLock() {
		lockFile.delete();
	}

	public String getLockFilePath() {
		return lockFile.getAbsolutePath();
	}
}
