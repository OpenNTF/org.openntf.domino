package org.openntf.domino.extmgr.events;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.extmgr.EMBridgeEventParams;

public abstract class ViewEvent extends AbstractDatabaseEvent {

	public ViewEvent(final int eventId) {
		super(eventId);
	}

	public String getNoteId() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.Noteid);
	}

	public Document getViewDocument(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getDocumentByID(getNoteId());
	}

	public String getViewUnid(final Session session, final String serverName) {
		Database db = getDatabase(session, serverName);
		return db.getUNID(getNoteId());
	}

	public View getView(final Session session, final String serverName) {
		Document document = getViewDocument(session, serverName);
		return document.getAncestorDatabase().getView(document);
	}

}
