package com.ho.agent.svn.command.service.impl;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.service.MultiRepositoryService;
import com.ho.agent.svn.service.impl.MultiSvnClientServiceImpl;

public class CleanServiceImpl implements CommandService {

	@Override
	public void run() {
		MultiRepositoryService repositoryService = new MultiSvnClientServiceImpl();
		repositoryService.cleanWorkingDirectory();
		System.out.println("Delete " + repositoryService.getWorkingDirectoryPaths());
	}

}
