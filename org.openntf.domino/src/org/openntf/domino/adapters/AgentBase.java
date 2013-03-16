package org.openntf.domino.adapters;

import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

public class AgentBase extends lotus.domino.AgentBase {
	@Override
	public Session getSession() {
		return Factory.fromLotus(super.getSession(), Session.class, null);
	}

	public static Session getAgentSession() {
		return Factory.fromLotus(lotus.domino.AgentBase.getAgentSession(), Session.class, null);
	}
}
