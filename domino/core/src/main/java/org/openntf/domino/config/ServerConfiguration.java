package org.openntf.domino.config;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

public class ServerConfiguration extends ConfigurationObject {

	private String serverName_;

	public ServerConfiguration(final String serverName) {
		serverName_ = serverName;
	}

	@Override
	protected Object[] schema() {
		// @formatter:off
		return new Object[] {
				"ServerName", 		String.class, 
				"ServerAlias", 		String.class,
				"XotsTasks", 		Integer.class,
				"XotsStopDelay", 	Integer.class
		};
		// @formatter:on
	}

	public String getServerName() {
		return get("ServerName");
	}

	public String getServerAlias() {
		return get("ServerAlias");
	}

	public int getXotsTasks() {
		return get("XotsTasks", 10);
	}

	public int getXotsStopDelay() {
		return get("XotsStopDelay", 15);
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

}
