package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbDeleteNotesEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.NoteArray,
			EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbDeleteNotesEvent() {
		super(EMEventIds.EM_NSFDBDELETENOTES.getId());
	}

	public String[] getNoteids() {
		return (String[]) getEventValuesMap().get(EMBridgeEventParams.NoteArray);
	}

	public int getCount() {
		return getNoteids().length;
	}

}
