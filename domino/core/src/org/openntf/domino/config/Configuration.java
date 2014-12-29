package org.openntf.domino.config;

import java.util.HashMap;
import java.util.Map;

import lotus.domino.NotesThread;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.exceptions.PermissionDeniedException;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * This is the interface to the ODA-Database
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class Configuration {
	Database odaDb_;
	Document currentConfig_;
	public static String ODA_NSF = "oda.nsf"; // TODO: Notes.ini

	public Configuration() {
		this(Factory.getSession(SessionType.NATIVE));
	}

	public Configuration(final Session session) {
		super();
		try {
			odaDb_ = session.getDatabase(ODA_NSF);
			if (odaDb_ == null) {
				Factory.println("WARNING", "cannot find " + ODA_NSF + ". - using default values.");
				return;
			}
			if (!odaDb_.isOpen())
				throw new PermissionDeniedException("DB not open.");
		} catch (Exception e) {
			Factory.println("ERROR", "cannot open " + ODA_NSF + ": " + e.toString() + " - using default values.");
		}
		odaDb_ = null;
	}

	public Map<String, Object> getConfiguration(final String serverName) {
		if (odaDb_ == null)
			return new HashMap<String, Object>();

		if (currentConfig_ != null) {
			if (serverName.equals(currentConfig_.getItemValueString("ServerName")))
				return currentConfig_;
			if (currentConfig_.isDirty()) {
				currentConfig_.save();
			}
		}

		View luView = odaDb_.getView("$lookupConfiguration");
		currentConfig_ = luView.getFirstDocumentByKey(serverName);
		if (currentConfig_ == null) {
			currentConfig_ = odaDb_.createDocument();
			currentConfig_.replaceItemValue("Form", "Configuration");
			currentConfig_.replaceItemValue("ServerName", serverName);
			currentConfig_.save();
			luView.refresh();
		}
		return currentConfig_;
	}

	public Map<String, Object> getConfiguration() {
		return getConfiguration(Factory.getLocalServerName());
	}

	public void save() {
		if (currentConfig_ != null) {
			if (currentConfig_.isDirty()) {
				currentConfig_.save();
			}
		}
	}

	public int getConfigValueInt(final String name, final int def) {
		Map<String, Object> map = getConfiguration();
		Object ret = map.get(name);
		if (ret instanceof Number) {
			return ((Number) ret).intValue();
		}

		if (ret instanceof String) {
			try {
				return Integer.parseInt((String) ret);
			} catch (NumberFormatException e) {
			}
		}
		map.put(name, def);
		return def;
	}

	public static int sGetConfigValueInt(final String name, final int def) {
		if (Factory.isInitialized()) {
			Configuration cfg = new Configuration();
			try {
				return cfg.getConfigValueInt(name, def);
			} finally {
				cfg.save();
			}
		} else {
			NotesThread.sinitThread();
			try {
				Factory.initThread(Factory.STRICT_THREAD_CONFIG);
				try {
					return sGetConfigValueInt(name, def);
				} finally {
					Factory.termThread();
				}
			} finally {
				NotesThread.stermThread();
			}
		}
	}
}
