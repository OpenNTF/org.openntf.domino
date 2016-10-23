package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCopyEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.SinceTimeDate, EMBridgeEventParams.NoteClass, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCopyEvent() {
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
