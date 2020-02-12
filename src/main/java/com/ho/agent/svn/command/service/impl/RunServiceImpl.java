package com.ho.agent.svn.command.service.impl;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.mapper.RepositoryMapper;
import com.ho.agent.svn.property.AppProperty;
import com.ho.agent.svn.service.MultiRepositoryService;
import com.ho.agent.svn.service.impl.MultiSvnClientServiceImpl;

public class RunServiceImpl implements CommandService {
	
	@Override
	public void run() {
		Runnable runnable = new Runnable() {
			MultiRepositoryService repositoryService = new MultiSvnClientServiceImpl();
			
			public void run() {
				List<RepositoryMapper> repositoryMappers = repositoryService.getRepositorys();
				for (RepositoryMapper repositoryMapper : repositoryMappers) {
					if (!repositoryService.isInitWorkingDirectory()) {
						repositoryService.initWorkingDirectory(repositoryMapper.getPropertyId());
					} else if (repositoryService.hasNewRevision(repositoryMapper.getPropertyId())) {
						repositoryService.checkout(repositoryMapper.getPropertyId(), null);
					}
				}
			}
		};
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, Long.valueOf(AppProperty.getInstance().getProperty("svn.poll-interval")), TimeUnit.SECONDS);
	}

}
