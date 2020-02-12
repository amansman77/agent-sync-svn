package com.ho.agent.svn.service.impl;

import java.io.File;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.replicator.SVNRepositoryReplicator;

import com.ho.agent.svn.service.RepositoryService;

public class SvnRepositoryServiceImpl extends RepositoryService {

	private SVNRepository remoteRepository = null;
	
	public SvnRepositoryServiceImpl() {
		DAVRepositoryFactory.setup();
		
		try {
			remoteRepository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(sourceUrl));
		} catch (SVNException e) {
			throw new RuntimeException("error while creating an SVNRepository for location '" + sourceUrl + "': " + e.getMessage());
		}
	}

	@Override
	public long initWorkingDirectory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasNewRevision() {
		try {
			long latestRevision = remoteRepository.getLatestRevision();
			if (checkedRevision < latestRevision) {
				System.out.println("Has new revision: " + latestRevision);
				return true;
			}
		} catch (SVNException svne) {
			System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
			return false;
		}

		return false;
	}

	@Override
	public void checkout(Long revision) {
		try {
			SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(new File(targetPath) , true , false);
			SVNRepository tgtRepository = SVNRepositoryFactory.create(tgtURL);
			SVNRepositoryReplicator svnRepositoryReplicator = SVNRepositoryReplicator.newInstance();
			
			System.out.println("Start update revision");
			long count = svnRepositoryReplicator.replicateRepository(remoteRepository, tgtRepository, true);
			System.out.println("Finish update revision: " + count);
		} catch (SVNException e) {
			throw new RuntimeException("error while replicate repository: " + e.getMessage());
		}
		
	}

	@Override
	public List<SVNLogEntry> getRevisions(int lineCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCheckedRevision() {
		// TODO Auto-generated method stub
		return null;
	}

}
