package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

/**
 * @author dtaieb
 * 
 */
public class NSFDbCopyNoteEvent extends DocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.DestDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCopyNoteEvent() {
		super(EMEventIds.EM_NSFDBCOPYNOTE.getId());
	}

	public String getDestPathName() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

}
