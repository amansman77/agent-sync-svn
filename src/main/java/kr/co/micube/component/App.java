package kr.co.micube.component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kr.co.micube.component.property.AppProperty;
import kr.co.micube.component.service.SvnService;
import kr.co.micube.component.service.impl.SvnClientServiceImpl;

public class App {

	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			SvnService svnService = new SvnClientServiceImpl();
			
			public void run() {
				if (!svnService.isInitWorkingDirectory()) {
					svnService.initWorkingDirectory();
				} else if (svnService.hasNewRevision()) {
					svnService.checkout();
				}
			}
		};
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, Long.valueOf(AppProperty.getInstance().getProperty("svn.poll-interval")), TimeUnit.SECONDS);
	}

}
