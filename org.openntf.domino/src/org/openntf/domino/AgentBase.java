package org.openntf.domino;

import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentBase.
 */
public class AgentBase extends lotus.domino.AgentBase {
	//	static {
	//		NotesThread nt = new NotesThread(new LogSetupRunnable());
	//		nt.start();
	//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentBase#getSession()
	 */
	@Override
	public Session getSession() {
		return Factory.fromLotus(super.getSession(), Session.class, null);
	}

	/**
	 * Gets the agent session.
	 * 
	 * @return the agent session
	 */
	public static Session getAgentSession() {
		return Factory.fromLotus(lotus.domino.AgentBase.getAgentSession(), Session.class, null);
	}
}
