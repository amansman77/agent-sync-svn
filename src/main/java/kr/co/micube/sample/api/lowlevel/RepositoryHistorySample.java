package kr.co.micube.sample.api.lowlevel;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class RepositoryHistorySample {

	public void printRepositoryHistory() {
		DAVRepositoryFactory.setup();

		String url = "https://micube-dev-1/svn/Study_CI";
		String name = "id";
		String password = "passwod";
		long startRevision = 0;
		long endRevision = -1;// HEAD (the latest) revision

		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		} catch (SVNException svne) {
			System.err
					.println("error while creating an SVNRepository for location '" + url + "': " + svne.getMessage());
			System.exit(1);
		}

		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name,
				password.toCharArray());
		repository.setAuthenticationManager(authManager);

		try {
			endRevision = repository.getLatestRevision();
		} catch (SVNException svne) {
			System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
			System.exit(1);
		}

		Collection logEntries = null;
		try {
			logEntries = repository.log(new String[] { "" }, null, startRevision, endRevision, true, true);
		} catch (SVNException svne) {
			System.out.println("error while collecting log information for '" + url + "': " + svne.getMessage());
			System.exit(1);
		}

		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			/*
			 * gets a next SVNLogEntry
			 */
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("---------------------------------------------");
			/*
			 * gets the revision number
			 */
			System.out.println("revision: " + logEntry.getRevision());
			/*
			 * gets the author of the changes made in that revision
			 */
			System.out.println("author: " + logEntry.getAuthor());
			/*
			 * gets the time moment when the changes were committed
			 */
			System.out.println("date: " + logEntry.getDate());
			/*
			 * gets the commit log message
			 */
			System.out.println("log message: " + logEntry.getMessage());
			/*
			 * displaying all paths that were changed in that revision; cahnged path
			 * information is represented by SVNLogEntryPath.
			 */
			if (logEntry.getChangedPaths().size() > 0) {
				System.out.println();
				System.out.println("changed paths:");
				/*
				 * keys are changed paths
				 */
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
					/*
					 * obtains a next SVNLogEntryPath
					 */
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
					/*
					 * SVNLogEntryPath.getPath returns the changed path itself;
					 * 
					 * SVNLogEntryPath.getType returns a charecter describing how the path was
					 * changed ('A' - added, 'D' - deleted or 'M' - modified);
					 * 
					 * If the path was copied from another one (branched) then
					 * SVNLogEntryPath.getCopyPath & SVNLogEntryPath.getCopyRevision tells where it
					 * was copied from and what revision the origin path was at.
					 */
					System.out.println(" " + entryPath.getType() + "	" + entryPath.getPath()
							+ ((entryPath.getCopyPath() != null) ? " (from " + entryPath.getCopyPath() + " revision "
									+ entryPath.getCopyRevision() + ")" : ""));
				}
			}
		}
	}
}
