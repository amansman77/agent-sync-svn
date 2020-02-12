package com.ho.agent.svn.command.service.impl;

import com.ho.agent.svn.command.service.CommandService;
import com.ho.agent.svn.constant.Constant.Command;

public class HelpServiceImpl implements CommandService {

	@Override
	public void run() {
		System.out.println("Command List : ");
		System.out.println(Command.HELP + "\tprint this help message to the output stream");
		System.out.println(Command.LIST_LOCAL_REPOSITORYS);
		System.out.println("\tlist local repositorys");
		System.out.println(Command.LIST_REVISIONS + " [repository id] [lines]");
		System.out.println("\tlist revisions");
		System.out.println(Command.CHECKOUT + " [revision]");
		System.out.println("\tcheckout specify revision");
		System.out.println(Command.RUN + "\trun program. Continuously check a remote repository for new revision");
		System.out.println(Command.CLEAN + "\tclean program resources");
	}

}
