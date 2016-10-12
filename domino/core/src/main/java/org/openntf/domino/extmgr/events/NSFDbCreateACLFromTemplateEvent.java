package org.openntf.domino.extmgr.events;

import org.openntf.domino.extmgr.EMBridgeEventParams;

public class NSFDbCreateACLFromTemplateEvent extends DatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Manager, EMBridgeEventParams.DefaultAccess };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public NSFDbCreateACLFromTemplateEvent() {
		super(EMEventIds.EM_NSFDBCREATEACLFROMTEMPLATE.getId());
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