package org.openntf.domino.config;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

public class ServerConfiguration extends ConfigurationObject {

	private String serverName_;

	public ServerConfiguration(final String serverName) {
		serverName_ = serverName;
	}

	@Override
	protected String[] keys() {
		return new String[] { "ServerName", // the name of the server. e.g. CN=srv-01-xdev/O=FOCONIS 
				"ServerAlias", // the alias. e.g. $CLUSTER1
				"XotsTasks", // count of Xots-Tasks
				"XotsStopDelay" // seconds to wait for stop
		};
	}

	/**
	 * Returns the document for this server
	 * 
	 * @return
	 */
	@Override
	protected Document getDocument(final boolean create) {
		Database odaDb = Configuration.getOdaDb();
		if (odaDb == null)
			return null;

		String unid = Configuration.computeUNID("ServerConfig:".concat(serverName_), odaDb);

		Document currentConfig_ = odaDb.getDocumentByUNID(unid);
		if (currentConfig_ == null) {
			if (!create)
				return null;
			currentConfig_ = odaDb.createDocument();
			currentConfig_.setUniversalID(unid);
			currentConfig_.replaceItemValue("Form", "Configuration");
			currentConfig_.replaceItemValue("ServerName", serverName_).setNames(true);
			currentConfig_.replaceItemValue("$ConflictAction", "3"); // merge - no conflicts
			currentConfig_.save();
		}
		return currentConfig_;
	}

	public int getXotsTasks() {
		return getConfigValueInt("XotsTasks", 10);
	}

	public int getXotsStopDelay() {
		return getConfigValueInt("XotsStopDelay", 15);
	}

}
