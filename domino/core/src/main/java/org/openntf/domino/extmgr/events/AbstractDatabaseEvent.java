package org.openntf.domino.extmgr.events;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class AbstractDatabaseEvent extends AbstractEMBridgeEvent implements IEMBridgeEvent {

	public AbstractDatabaseEvent(final int eventId) {
		super(eventId);
	}

	public String getDbPath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.SourceDbpath);
	}

	public Database getDatabase(final Session session, String serverName) {
		if (serverName == null) {
			serverName = "";
		}
		return session.getDatabase(serverName, getDbPath());
	}

}
