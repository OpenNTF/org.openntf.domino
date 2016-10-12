package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCopyTemplateACLEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Manager, EMBridgeEventParams.DefaultAccess, EMBridgeEventParams.Username };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCopyTemplateACLEvent() {
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
