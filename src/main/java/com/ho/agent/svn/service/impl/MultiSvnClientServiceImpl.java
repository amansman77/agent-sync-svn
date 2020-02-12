package com.ho.agent.svn.service.impl;

import java.util.List;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.ho.agent.svn.constant.Constant.PropertyKey;
import com.ho.agent.svn.mapper.RepositoryMapper;
import com.ho.agent.svn.service.MultiRepositoryService;

public class MultiSvnClientServiceImpl extends MultiRepositoryService {

	public MultiSvnClientServiceImpl() {
	}

	@Override
	public void initWorkingDirectory(String repositoryId) {
		RepositoryMapper repositoryMapper = findByRepositoryId(repositoryId);
		SvnClientServiceImpl svnClientServiceImpl = new SvnClientServiceImpl(repositoryMapper);
		long updateRevision = svnClientServiceImpl.initWorkingDirectory();
		
		svnClientServiceImpl.updateRevision(
				repositoryMapper.getPropertyKey(PropertyKey.DATA_CHECKED_REVISION)
				, updateRevision);
		repositoryMapper.setCheckedRevision(updateRevision);
	}

	@Override
	public boolean hasNewRevision(String repositoryId) {
		RepositoryMapper repositoryMapper = findByRepositoryId(repositoryId);
		SvnClientServiceImpl svnClientServiceImpl = new SvnClientServiceImpl(repositoryMapper);
		return svnClientServiceImpl.hasNewRevision();
	}

	@Override
	public void checkout(String repositoryId, Long revision) {
		RepositoryMapper repositoryMapper = findByRepositoryId(repositoryId);
		SvnClientServiceImpl svnClientServiceImpl = new SvnClientServiceImpl(repositoryMapper);
		svnClientServiceImpl.checkout(revision);
		long updateRevision = svnClientServiceImpl.updateRevision(
				repositoryMapper.getPropertyKey(PropertyKey.DATA_CHECKED_REVISION)
				, revision);
		
		repositoryMapper.setCheckedRevision(updateRevision);
	}

	@Override
	public List<SVNLogEntry> getRevisions(String repositoryId, int lineCount) {
		RepositoryMapper repositoryMapper = findByRepositoryId(repositoryId);
		SvnClientServiceImpl svnClientServiceImpl = new SvnClientServiceImpl(repositoryMapper);
		return svnClientServiceImpl.getRevisions(lineCount);
	}

	@Override
	public Long getCheckedRevision(String repositoryId) {
		RepositoryMapper repositoryMapper = findByRepositoryId(repositoryId);
		SvnClientServiceImpl svnClientServiceImpl = new SvnClientServiceImpl(repositoryMapper);
		return svnClientServiceImpl.getCheckedRevision();
	}

}
