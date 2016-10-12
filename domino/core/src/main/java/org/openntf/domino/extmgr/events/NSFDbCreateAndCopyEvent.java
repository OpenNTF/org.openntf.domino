package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCreateAndCopyEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.NoteClass, EMBridgeEventParams.Limit, EMBridgeEventParams.Flag, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCreateAndCopyEvent() {
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
