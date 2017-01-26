package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDocumentEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

/**
 * @author dtaieb
 *
 */
public class CopyToDatabaseEvent extends AbstractDocumentEvent {
	private static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.Noteid,
			EMBridgeEventParams.DestDbpath, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CopyToDatabaseEvent() {
		super(EMEventIds.EM_NSFDBCOPYNOTE.getId());
	}

	public String getDestPathName() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

}
