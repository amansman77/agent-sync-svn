package com.ho.agent.svn.service.impl;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.ho.agent.svn.service.SvnService;

public class SvnClientServiceImpl extends SvnService {

	private SVNClientManager svnClientManager;
	
	public SvnClientServiceImpl() {
		DAVRepositoryFactory.setup();
		
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
		
		svnClientManager = SVNClientManager.newInstance(options , authManager);
	}

	@Override
	public void initWorkingDirectory() {
		SVNURL sourceSvnurl = null;
        try {
        	sourceSvnurl = SVNURL.parseURIEncoded(sourceUrl);
        } catch (SVNException e) {
            throw new RuntimeException("error while parse URI: " + e.getMessage());
        }
        
		try {
			System.out.println("Start init working-directory");
			checkedRevision = svnClientManager.getUpdateClient().doCheckout(sourceSvnurl, new File(targetPath), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.UNKNOWN, false);
			System.out.println("Finish init working-directory");
		} catch (SVNException e) {
			throw new RuntimeException("error while checkout: " + e.getMessage());
		}
		
		appProperty.setProperty("data.checkedRevision", String.valueOf(checkedRevision));
	}

	@Override
	public boolean hasNewRevision() {
		long latestRevision = this.getLatestRevision();
//			System.out.println("Checked Revision: " + checkedRevision + ", Latest Working Revision: " + svnStatus.getRevision().getNumber());
//			System.out.println("Checked Revision: " + checkedRevision + ", Latest Remote Revision: " + svnStatus.getRemoteRevision().getNumber());
		System.out.println("Checked Revision: " + checkedRevision + ", Latest Revision: " + latestRevision);
		
		if (checkedRevision < latestRevision) {
			System.out.println("Has new revision: " + latestRevision);
			return true;
		}
		return false;
	}

	@Override
	public void checkout() {
		SVNURL sourceSvnurl = null;
        try {
        	sourceSvnurl = SVNURL.parseURIEncoded(sourceUrl);
        } catch (SVNException e) {
            throw new RuntimeException("error while parse URI: " + e.getMessage());
        }
        
        long latestRevision = this.getLatestRevision();
		try {
			System.out.println("Start checkout");
			svnClientManager.getUpdateClient().doCheckout(sourceSvnurl, new File(targetPath), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.UNKNOWN, false);
			System.out.println("Finish checkout");
		} catch (SVNException e) {
			throw new RuntimeException("error while checkout: " + e.getMessage());
		}
		
		checkedRevision = latestRevision;
		appProperty.setProperty("data.checkedRevision", String.valueOf(checkedRevision));
	}

	private long getLatestRevision() {
		long latestRevision = checkedRevision;
		try {
			SVNStatus svnStatus = svnClientManager.getStatusClient().doStatus(new File(targetPath), true);
			latestRevision = svnStatus.getRemoteRevision().getNumber();
		} catch (SVNException e) {
			System.err.println("error while fetching the latest repository revision: " + e.getMessage());
		}
		
		return latestRevision;
	}

}
