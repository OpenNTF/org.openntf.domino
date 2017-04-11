package org.openntf.domino.extmgr.events;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class AbstractDocumentEvent extends AbstractDatabaseEvent {

	public AbstractDocumentEvent(final int eventId) {
		super(eventId);
	}

	public String getNoteId() {
		try {
			Integer noteIdInt = Integer.valueOf((String) getEventValuesMap().get(EMBridgeEventParams.Noteid));
			return Integer.toHexString(noteIdInt);
		} catch (Exception e) {
			return (String) getEventValuesMap().get(EMBridgeEventParams.Noteid);
		}
	}

	public int getNoteIdAsInt() {
		return Integer.valueOf((String) getEventValuesMap().get(EMBridgeEventParams.Noteid));
	}

	public Document getDocument(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getDocumentByID(getNoteId());
	}

	public String getUnid(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getUNID(getNoteId());
	}

}
