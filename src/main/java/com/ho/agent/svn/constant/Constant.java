package com.ho.agent.svn.constant;

public class Constant {
	
	public class Service {
		public static final String LOCK_FILE_NAME = "ho-ci.lock";
	}
	
	public class Command {
		public static final String HELP = "--help";
		public static final String LIST_LOCAL_REPOSITORYS = "--list-local-repositorys";
		public static final String LIST_REVISIONS = "--list-revisions";
		public static final String RUN = "--run";
		public static final String CLEAN = "--clean";
		public static final String CHECKOUT = "--checkout";
	}
	
	public class PropertyKey {
		public static final String SVN_URL = "svn.repository.url";
		public static final String SVN_TARGET_SOURCE_PATH = "svn.repository.target-source-path";
		
		public static final String DATA_CHECKED_REVISION = "data.checkedRevision";
	}
	
}
