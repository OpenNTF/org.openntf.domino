/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
