package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CreateAndCopyEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.NoteClass, EMBridgeEventParams.Limit, EMBridgeEventParams.Flag, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CreateAndCopyEvent() {
		super(EMEventIds.EM_NSFDBCREATEANDCOPY.getId());
	}

	public String getDestDbpath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

	public int getNoteClass() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.NoteClass);
	}

	public int getLimit() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Limit);
	}

	public long getFlags() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.Flag);
	}
}
