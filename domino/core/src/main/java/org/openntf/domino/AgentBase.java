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
		// RPr: Startup and init is probably wrong here
		// Factory.startup();
		// Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		return Factory.getWrapperFactory().fromLotus(super.getSession(), Session.SCHEMA, null);
	}

	/**
	 * The NotesMain should not longer be overwritten. Use DominoMain instead.
	 */
	@Override
	public void NotesMain() {
		boolean doShutdown = false;
		if (!Factory.isStarted()) {
			Factory.startup();
			doShutdown = true;
		}
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);
		try {
			DominoMain();
		} finally {
			Factory.termThread();
			if (doShutdown) {
				Factory.shutdown();
			}
		}

	}

	/**
	 * Implement your code here
	 */
	public void DominoMain() {
	}

	/**
	 * Gets the agent session.
	 * 
	 * @return the agent session
	 */
	public static Session getAgentSession() {
		return Factory.getWrapperFactory().fromLotus(lotus.domino.AgentBase.getAgentSession(), Session.SCHEMA, null);
	}
}
