package com.ho.agent.svn.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnLog;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnRevisionRange;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import com.ho.agent.svn.constant.Constant.PropertyKey;
import com.ho.agent.svn.mapper.RepositoryMapper;
import com.ho.agent.svn.service.LockService;
import com.ho.agent.svn.service.RepositoryService;

public class SvnClientServiceImpl extends RepositoryService {

	private SVNClientManager svnClientManager;
	private LockService lockService;
	
	public SvnClientServiceImpl() {
		super();
		
		DAVRepositoryFactory.setup();
		
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		
		svnClientManager = SVNClientManager.newInstance(options , authManager);
		lockService = new LockService();
	}
	
	public SvnClientServiceImpl(RepositoryMapper repositoryMapper) {
		super(repositoryMapper.getRemoteUri(), repositoryMapper.getLocalPath(), repositoryMapper.getCheckedRevision());
		
		DAVRepositoryFactory.setup();
		
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		
		svnClientManager = SVNClientManager.newInstance(options , authManager);
		lockService = new LockService(repositoryMapper.getLocalPath());
	}

	@Override
	public long initWorkingDirectory() {
		SVNURL sourceSvnurl = null;
        try {
        	sourceSvnurl = SVNURL.parseURIEncoded(sourceUrl);
        } catch (SVNException e) {
            throw new RuntimeException("error while parse URI: " + e.getMessage());
        }
        
        long checkoutRevisionCount;
		try {
			System.out.println("Start init working-directory, Remote URI: " + sourceUrl + ", Local path: " + targetPath);
			checkoutRevisionCount = svnClientManager.getUpdateClient().doCheckout(sourceSvnurl, new File(targetPath), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.UNKNOWN, false);
			System.out.println("Finish init working-directory");
		} catch (SVNException e) {
			throw new RuntimeException("error while checkout: " + e.getMessage());
		}
		
		return checkoutRevisionCount;
	}

	@Override
	public boolean hasNewRevision() {
		long latestRevision = this.getLatestRevision();
//			System.out.println("Checked Revision: " + checkedRevision + ", Latest Working Revision: " + svnStatus.getRevision().getNumber());
//			System.out.println("Checked Revision: " + checkedRevision + ", Latest Remote Revision: " + svnStatus.getRemoteRevision().getNumber());
		System.out.println("Checked Revision: " + checkedRevision + ", Latest Revision: " + latestRevision
				+ ", Remote URI: " + sourceUrl + ", Local path: " + targetPath);
		
		if (checkedRevision < latestRevision) {
			System.out.println("Has new revision");
			return true;
		}
		return false;
	}

	@Override
	public void checkout(Long revision) {
		if (lockService.isLocked()) {
			System.out.println("Directory is using othre service: " + targetPath);
			System.out.println("if locking directory for a while, check lock file: " + lockService.getLockFilePath());
			return;
		} else {
			lockService.lock();
		}
		
		SVNURL sourceSvnurl = null;
        try {
        	sourceSvnurl = SVNURL.parseURIEncoded(sourceUrl);
        } catch (SVNException e) {
        	lockService.unLock();
            throw new RuntimeException("error while parse URI: " + e.getMessage());
        }
        
		try {
			SVNRevision targetRevision = SVNRevision.HEAD;
			if (revision != null) {
				targetRevision = SVNRevision.create(revision);
			}
			System.out.println("Start checkout");
			svnClientManager.getUpdateClient()
				.doCheckout(
						sourceSvnurl, 
						new File(targetPath), 
						SVNRevision.HEAD, 
						targetRevision, 
						SVNDepth.UNKNOWN, 
						false);
			System.out.println("Finish checkout");
		} catch (SVNException e) {
			lockService.unLock();
			throw new RuntimeException("error while checkout: " + e.getMessage());
		}
		
		lockService.unLock();
	}

	@Override
	public List<SVNLogEntry> getRevisions(int lineCount) {
		SvnOperationFactory operationFactory = new SvnOperationFactory();
		SvnLog logOperation = operationFactory.createLog();
		logOperation.setSingleTarget(
		        SvnTarget.fromFile(new File(targetPath))
		);
		logOperation.setRevisionRanges( Collections.singleton(
		        SvnRevisionRange.create(
		                SVNRevision.create(this.getLatestRevision() - lineCount + 1),
		                SVNRevision.HEAD
		        )
		) );
		Collection<SVNLogEntry> logEntries = null;
		try {
			logEntries = logOperation.run(null);
		} catch (SVNException e) {
			throw new RuntimeException("error while get revision list: " + e.getMessage());
		}

		return new ArrayList<>(logEntries);
	}

	@Override
	public Long getCheckedRevision() {
		if (checkedRevision < 0) {
			SvnOperationFactory operationFactory = new SvnOperationFactory();
			SvnLog logOperation = operationFactory.createLog();
			logOperation.setSingleTarget(
			        SvnTarget.fromFile(new File(targetPath))
			);
			logOperation.setRevisionRanges( Collections.singleton(
			        SvnRevisionRange.create(
			        		SVNRevision.HEAD,
			                SVNRevision.HEAD
			        )
			) );
			Collection<SVNLogEntry> logEntries = null;
			try {
				logEntries = logOperation.run(null);
			} catch (SVNException e) {
				throw new RuntimeException("error while get revision list: " + e.getMessage());
			}
			
			List<SVNLogEntry> svnLogEntries = new ArrayList<>(logEntries);
			checkedRevision = svnLogEntries.get(0).getRevision();
		}
		return checkedRevision;
	}

	private void setCheckedRevision(String propertyKey, long latestRevision) {
		checkedRevision = latestRevision;
		appProperty.setProperty(
				propertyKey == null?PropertyKey.DATA_CHECKED_REVISION:propertyKey
						, String.valueOf(checkedRevision));
	}

	private long getLatestRevision() {
		long latestRevision = -1;
		try {
			SvnOperationFactory operationFactory = new SvnOperationFactory();
			SvnLog logOperation = operationFactory.createLog();
			logOperation.setSingleTarget(
			        SvnTarget.fromFile(new File(targetPath))
			);
			logOperation.setRevisionRanges( Collections.singleton(
			        SvnRevisionRange.create(
			        		SVNRevision.HEAD,
			                SVNRevision.HEAD
			        )
			) );
			Collection<SVNLogEntry> logEntries = null;
			logEntries = logOperation.run(null);
			
			List<SVNLogEntry> svnLogEntries = new ArrayList<>(logEntries);
			latestRevision = svnLogEntries.get(0).getRevision();
		} catch (SVNException e) {
			System.err.println("error while fetching the latest repository revision: " + e.getMessage());
		}
		
		return (latestRevision < 0)?checkedRevision:latestRevision;
	}

	public SVNClientManager getSvnClientManager() {
		return svnClientManager;
	}

	public Long updateRevision(String propertyKey, Long revision) {
		long latestRevision = this.getLatestRevision();
		if (revision != null) {
			this.setCheckedRevision(propertyKey, revision);
			return revision;
		} else {
			this.setCheckedRevision(propertyKey, latestRevision);
			return latestRevision;
		}
	}

}
