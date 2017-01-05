package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CopyEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.SinceTimeDate, EMBridgeEventParams.NoteClass, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CopyEvent() {
		super(EMEventIds.EM_NSFDBCOPY.getId());
	}

	public String getDestDbpath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

	public String getSinceTimeDate() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.SinceTimeDate);
	}

	public int getNoteClassMask() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.NoteClass);
	}

}
