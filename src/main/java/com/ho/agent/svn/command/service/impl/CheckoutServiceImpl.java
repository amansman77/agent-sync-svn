package com.ho.agent.svn.command.service.impl;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.service.MultiRepositoryService;
import com.ho.agent.svn.service.impl.MultiSvnClientServiceImpl;

public class CheckoutServiceImpl implements CommandService {

	private final String repositoryId;
	private final Long revision;
	
	public CheckoutServiceImpl(String repositoryId, Long revision) {
		this.repositoryId = repositoryId;
		this.revision = revision;
	}

	@Override
	public void run() {
		MultiRepositoryService repositoryService = new MultiSvnClientServiceImpl();
		repositoryService.checkout(repositoryId, revision);
		System.out.println("Checkout revision : " + revision);
	}

}
