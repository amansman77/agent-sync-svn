package com.ho.agent.svn.command.service.impl;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.service.MultiRepositoryService;
import com.ho.agent.svn.service.impl.MultiSvnClientServiceImpl;

public class ListLocalRepositorysServiceImpl implements CommandService {

	@Override
	public void run() {
		MultiRepositoryService repositoryService = new MultiSvnClientServiceImpl();
		repositoryService.getRepositorys().forEach(
				r -> System.out.println("Repository ID: " + r.getPropertyId()
				+ ", Remote URI: " + r.getRemoteUri()
				+ ", Local path: " + r.getLocalPath()
				+ ", Current revision: " + r.getCheckedRevision()
		));
	}

}
