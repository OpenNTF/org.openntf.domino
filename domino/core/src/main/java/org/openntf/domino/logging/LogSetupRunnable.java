/**
 * 
 */
package org.openntf.domino.logging;

import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import lotus.domino.NotesFactory;

/**
 * @author Nathan T. Freeman
 * @deprecated Leaving the code here in case anyone wants an example of how to do this. But the functional code has been moved to an inner
 *             class in the Factory called SetupJob.
 * 
 */
@Deprecated
public class LogSetupRunnable implements Runnable {

	/**
	 * Constructor
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public LogSetupRunnable() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		System.out.println("Initializing OpenNTF logging...");
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					String datadir = null;
					try {
						lotus.domino.Session session = NotesFactory.createSession();
						datadir = session.getEnvironmentString("DIRECTORY", true);
						session.recycle();
					} catch (Throwable t) {
						t.printStackTrace();
					}
					String pattern = datadir + "/IBM_TECHNICAL_SUPPORT/org.openntf.%u.%g.log";
					Logger oodLogger = Logger.getLogger("org.openntf.domino");
					oodLogger.setLevel(Level.WARNING);

					DefaultFileHandler dfh = new DefaultFileHandler(pattern, 50000, 100, true);
					dfh.setFormatter(new FileFormatter());
					dfh.setLevel(Level.WARNING);
					oodLogger.addHandler(dfh);

					DefaultConsoleHandler dch = new DefaultConsoleHandler();
					dch.setFormatter(new ConsoleFormatter());
					dch.setLevel(Level.WARNING);
					oodLogger.addHandler(dch);

					OpenLogHandler olh = new OpenLogHandler();
					olh.setLogDbPath("OpenLog.nsf");
					olh.setLevel(Level.WARNING);
					oodLogger.addHandler(olh);

					LogManager manager = LogManager.getLogManager();
					manager.addLogger(oodLogger);
					return null;
				}
			});
			System.out.println("Completed OpenNTF logging initialization.");
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
	}
}
