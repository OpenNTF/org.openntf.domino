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
/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xots.Tasklet;

/**
 * An observable callable implementation. In your implementation you should check {@link #shouldStop()} periodically
 * 
 * @author Nathan T. Freeman
 */
@SuppressWarnings("unused")
public abstract class AbstractDominoCallable<T> extends Observable implements Tasklet.Interface, Callable<T>, Serializable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoCallable.class.getName());
	private static final long serialVersionUID = 1L;

	private boolean shouldStop_ = false;
	private Thread runningThread_;

	@Override
	public ISessionFactory getSessionFactory() {
		return null;
	}

	@Override
	public Tasklet.Context getContext() {
		return null;
	}

	@Override
	public Tasklet.Scope getScope() {
		return null;
	}

	@Override
	public String[] getDynamicSchedule() {
		return null;
	}

	/**
	 * Returns true Method should be queried in loops to determine if we should stop
	 * 
	 * @return
	 */
	protected synchronized boolean shouldStop() {
		return shouldStop_;
	}

	@Override
	public synchronized void stop() {
		shouldStop_ = true;
	}

	@Override
	public Factory.ThreadConfig getThreadConfig() {
		return null;
	}

	@Override
	public String getDescription() {
		return getClass().getSimpleName();
	}
}
