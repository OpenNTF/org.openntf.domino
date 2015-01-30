package org.openntf.domino.config;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * This is the interface to the ODA-Database
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public enum Configuration {
	;
	private static DominoExecutor executor_;
	private static Database odaDb_;
	private static boolean inititalized;

	public static String ODA_NSF = "oda.nsf"; // TODO: Notes.ini

	//
	//	public Configuration() {
	//		this(Factory.getSession(SessionType.NATIVE));
	//	}

	protected static synchronized DominoExecutor getExecutor() {
		if (executor_ == null) {
			if (Factory.isStarted()) {
				executor_ = new DominoExecutor(1);
			}
		}
		return executor_;
	}

	protected static Database getOdaDb() {
		if (inititalized)
			return odaDb_;
		return initOdaDb();
	}

	private synchronized static Database initOdaDb() {
		if (!inititalized) {
			inititalized = true;
			try {
				// ODA-DB is thread safe, so we may cahce it :)
				Session sess = Factory.getSession(SessionType.CURRENT);
				Database db = sess.getDatabase(ODA_NSF);
				if (db == null) {
					Factory.println("WARNING", "cannot find " + ODA_NSF + " as user " + sess.getEffectiveUserName()
							+ ". - using default values.");
				} else if (!db.isOpen()) {
					Factory.println("ERROR", "cannot open " + ODA_NSF + " as user " + sess.getEffectiveUserName()
							+ ". - using default values.");
				} else {
					odaDb_ = db;
					Factory.println(Configuration.class, "Using " + db + " as configuration database.");
				}
			} catch (Exception e) {
				Factory.println("ERROR", "cannot open " + ODA_NSF + ": " + e.toString() + " - using default values.");
			}
		}
		return odaDb_;
	}

	public static ConfigurationProperties getServerConfiguration(final String serverName) {
		return new ServerConfigurationProperties(serverName);
	}

	public static ConfigurationProperties getServerConfiguration() {
		return getServerConfiguration(Factory.getLocalServerName());
	}

}
