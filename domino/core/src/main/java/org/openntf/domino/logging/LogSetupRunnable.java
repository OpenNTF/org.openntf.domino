/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@SuppressWarnings("nls")
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
						datadir = session.getEnvironmentString("DIRECTORY", true); //$NON-NLS-1$
						session.recycle();
					} catch (Throwable t) {
						t.printStackTrace();
					}
					String pattern = datadir + "/IBM_TECHNICAL_SUPPORT/org.openntf.%u.%g.log"; //$NON-NLS-1$
					Logger oodLogger = Logger.getLogger("org.openntf.domino"); //$NON-NLS-1$
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
					olh.setLogDbPath("OpenLog.nsf"); //$NON-NLS-1$
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
