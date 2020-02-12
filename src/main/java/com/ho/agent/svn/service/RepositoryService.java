package com.ho.agent.svn.service;

import java.io.File;
import java.util.List;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.ho.agent.svn.constant.Constant.PropertyKey;
import com.ho.agent.svn.property.AppProperty;

public abstract class RepositoryService {

	protected AppProperty appProperty = AppProperty.getInstance();
	
	protected Long checkedRevision;

	protected String sourceUrl;
	protected String targetPath;
	
	public RepositoryService() {
		checkedRevision = Long.valueOf(appProperty.getProperty(PropertyKey.DATA_CHECKED_REVISION));
		sourceUrl = appProperty.getProperty(PropertyKey.SVN_URL);
		targetPath = appProperty.getProperty(PropertyKey.SVN_TARGET_SOURCE_PATH);
	}
	
	public RepositoryService(String sourceUrl, String targetPath, Long checkedRevision) {
		this.sourceUrl = sourceUrl;
		this.targetPath = targetPath;
		this.checkedRevision = checkedRevision;
	}
	
	public boolean isInitWorkingDirectory() {
		if (new File(targetPath).list() == null) {
			return false;
		}
		
		return true;
	}

	public void cleanWorkingDirectory() {
		File workingDirectory = new File(targetPath);
		if (workingDirectory.exists()) {
			deleteDirectory(workingDirectory);
		}
	}

	public String getWorkingDirectoryPath() {
		return targetPath;
	}

	private void deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			} else {
				file.delete();
			}
		}
		directory.delete();
	}

	public abstract boolean hasNewRevision();

	public abstract void checkout(Long revision);

	public abstract long initWorkingDirectory();

	public abstract List<SVNLogEntry> getRevisions(int lineCount);
	
	public abstract Long getCheckedRevision();

}
