package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CopyTemplateACLEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Manager, EMBridgeEventParams.DefaultAccess, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CopyTemplateACLEvent() {
		super(EMEventIds.EM_NSFDBCOPYTEMPLATEACL.getId());
	}

	public String getDestDbpath() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.DestDbpath);
	}

	public String getManager() {
		return (String) getEventValuesMap().get(EMBridgeEventParams.Manager);
	}

	public int getDefaultAccessLevel() {
		return (Integer) getEventValuesMap().get(EMBridgeEventParams.DefaultAccess);
	}

}
