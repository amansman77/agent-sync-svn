package com.ho.agent.svn;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.command.service.impl.CheckoutServiceImpl;
import com.ho.agent.svn.command.service.impl.CleanServiceImpl;
import com.ho.agent.svn.command.service.impl.HelpServiceImpl;
import com.ho.agent.svn.command.service.impl.ListLocalRepositorysServiceImpl;
import com.ho.agent.svn.command.service.impl.ListRevisionsServiceImpl;
import com.ho.agent.svn.command.service.impl.RunServiceImpl;
import com.ho.agent.svn.constant.Constant.Command;

public class App {

	public static void main(String[] args) {
		CommandService commandService = null;
		String command = args[0];
		
		if (args.length < 1 || command.equals(Command.HELP)) {
			commandService = new HelpServiceImpl();
		} else if (command.equals(Command.LIST_LOCAL_REPOSITORYS)) {
			commandService = new ListLocalRepositorysServiceImpl();
		} else if (command.equals(Command.LIST_REVISIONS)) {
			if (args.length > 2) {
				String repositoryId = args[1];
				int lineCount = Integer.valueOf(args[2]);
				
				commandService = new ListRevisionsServiceImpl(repositoryId, lineCount);
			} else {
				System.out.println("No input parameter");
				commandService = new HelpServiceImpl();
				return ;
			}
		} else if (command.equals(Command.CHECKOUT)) {
			if (args.length > 2) {
				String repositoryId = args[1];
				Long revision = Long.valueOf(args[2]);
				commandService = new CheckoutServiceImpl(repositoryId, revision);
			} else {
				System.out.println("No input parameter");
				commandService = new HelpServiceImpl();
				return ;
			}
		} else if (command.equals(Command.RUN)) {
			commandService = new RunServiceImpl();
		} else if (command.equals(Command.CLEAN)) {
			commandService = new CleanServiceImpl();
		}
		
		commandService.run();
	}

}
