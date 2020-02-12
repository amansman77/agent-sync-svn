package com.ho.agent.svn.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.ho.agent.svn.mapper.RepositoryMapper;
import com.ho.agent.svn.property.AppProperty;

public abstract class MultiRepositoryService {

	protected AppProperty appProperty = AppProperty.getInstance();
	
	protected final List<RepositoryMapper> repositorys;
	
	public MultiRepositoryService() {
		repositorys = RepositoryMapper.getRepositorys();
	}
	
	public boolean isInitWorkingDirectory() {
		for (RepositoryMapper repositoryMapper : repositorys) {
			String targetPath = repositoryMapper.getLocalPath();
			if (new File(targetPath).list() == null) {
				return false;
			}
		}
		
		return true;
	}

	public void cleanWorkingDirectory() {
		for (RepositoryMapper repositoryMapper : repositorys) {
			String targetPath = repositoryMapper.getLocalPath();
			File workingDirectory = new File(targetPath);
			if (workingDirectory.exists()) {
				deleteDirectory(workingDirectory);
			}
			
		}
	}

	public List<String> getWorkingDirectoryPaths() {
		return repositorys.stream().map(RepositoryMapper::getLocalPath).collect(Collectors.toList());
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
	
	protected RepositoryMapper findByRepositoryId(String repositoryId) {
		return repositorys.stream().filter(r -> r.getPropertyId().equals(repositoryId)).findAny().get();
	}
	
	public abstract boolean hasNewRevision(String repositoryId);

	public abstract void checkout(String repositoryId, Long revision);

	public abstract void initWorkingDirectory(String repositoryId);

	public abstract List<SVNLogEntry> getRevisions(String repositoryId, int lineCount);
	
	public abstract Long getCheckedRevision(String repositoryId);

	public List<RepositoryMapper> getRepositorys() {
		return repositorys;
	}

}
