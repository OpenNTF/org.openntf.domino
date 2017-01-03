package org.openntf.domino.extmgr.events.database;

import org.openntf.domino.extmgr.EMBridgeEventParams;
import org.openntf.domino.extmgr.events.AbstractDatabaseEvent;
import org.openntf.domino.extmgr.events.EMEventIds;

public class CreateACLFromTemplateEvent extends AbstractDatabaseEvent {
	public static EMBridgeEventParams[] params = { EMBridgeEventParams.SourceDbpath, EMBridgeEventParams.DestDbpath,
			EMBridgeEventParams.Manager, EMBridgeEventParams.DefaultAccess };

	@Override
	public EMBridgeEventParams[] getParams() {
		return params;
	}

	public CreateACLFromTemplateEvent() {
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