package org.openntf.domino.extmgr.events.document;

import org.openntf.domino.extmgr.events.EMEventIds;

public class UpdateMailBoxEvent extends UpdateExtendedEvent {

	public UpdateMailBoxEvent() {
		super(EMEventIds.EM_NSFNOTEUPDATEMAILBOX);
	}

}
