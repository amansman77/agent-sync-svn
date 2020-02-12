package com.ho.agent.svn.command.service.impl;

import java.util.List;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.service.MultiRepositoryService;
import com.ho.agent.svn.service.impl.MultiSvnClientServiceImpl;

public class ListRevisionsServiceImpl implements CommandService {

	private final String repositoryId;
	private final int lineCount;
	
	public ListRevisionsServiceImpl(String repositoryId, int lineCount) {
		this.repositoryId = repositoryId;
		this.lineCount = lineCount;
	}

	@Override
	public void run() {
		MultiRepositoryService repositoryService = new MultiSvnClientServiceImpl();
		if (!repositoryService.isInitWorkingDirectory()) {
			System.out.println("ERROR : Checkout repository need to getting revision list");
			return;
		}
		
		long checkedRevision = repositoryService.getCheckedRevision(repositoryId);
		List<SVNLogEntry> logEntries = repositoryService.getRevisions(repositoryId, lineCount);
		System.out.println("Current Revision : " + repositoryService.getCheckedRevision(repositoryId) + "\n");
		
		for (int i = logEntries.size()-1; i >= 0; i--) {
			SVNLogEntry logEntrie = logEntries.get(i);
			if (checkedRevision == logEntrie.getRevision()) {
				System.out.print("Checkout >>> ");
			}
			System.out.println("Revision : " + logEntrie.getRevision() + ", Commit log : " + logEntrie.getMessage());
		}
	}

}
