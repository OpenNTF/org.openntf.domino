package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class DeleteNotesEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.NoteArray,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public DeleteNotesEvent() {
		super(EMEventIds.EM_NSFDBDELETENOTES.getId());
	}

	public String[] getNoteids() {
		return (String[]) getEventValuesMap().get(EMBridgeEventParams.NoteArray);
	}

	public int getCount() {
		return getNoteids().length;
	}

}
