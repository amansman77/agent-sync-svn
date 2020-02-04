package com.ho.agent.svn.service;

import java.io.File;

import com.ho.agent.svn.property.AppProperty;

public abstract class SvnService {

	protected AppProperty appProperty = AppProperty.getInstance();
	
	protected Long checkedRevision;

	protected String sourceUrl;
	protected String targetPath;
	
	public SvnService() {
		checkedRevision = Long.valueOf(appProperty.getProperty("data.checkedRevision"));
		sourceUrl = appProperty.getProperty("svn.url");
		targetPath = appProperty.getProperty("svn.target-source-path");
	}
	
	public boolean isInitWorkingDirectory() {
		if (new File(targetPath).list() == null) {
			return false;
		}
		
		return true;
	}

	public abstract boolean hasNewRevision();

	public abstract void checkout();

	public abstract void initWorkingDirectory();

}
