/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.thread;

import java.util.ArrayList;
import java.util.Collection;

import lotus.domino.Base;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoLockSet.
 * 
 * @author nfreeman
 */
public class DominoLockSet extends ThreadLocal<Collection<Base>> {

	/** The object set. */
	private final Collection<Base> objectSet = new ArrayList<Base>();

	/** The lock collection. */
	private final Collection<Base> lockCollection = new ArrayList<Base>();

	/**
	 * Instantiates a new domino reference set.
	 */
	public DominoLockSet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Unlock.
	 * 
	 * @param base
	 *            the base
	 */
	public void unlock(final Base base) {
		lockCollection.remove(base);
	}

	/**
	 * Lock.
	 * 
	 * @param base
	 *            the base
	 */
	public void lock(final Base base) {
		lockCollection.add(base);
	}

	/**
	 * Lock.
	 * 
	 * @param bases
	 *            the bases
	 */
	public void lock(final Base... bases) {
		for (Base base : bases) {
			lockCollection.add(base);
		}
	}

	/**
	 * Unlock.
	 * 
	 * @param bases
	 *            the bases
	 */
	public void unlock(final Base... bases) {
		for (Base base : bases) {
			lockCollection.remove(base);
		}
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is locked
	 */
	public boolean isLocked(final Base base) {
		return lockCollection.contains(base);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ThreadLocal#initialValue()
	 */
	@Override
	protected Collection<Base> initialValue() {
		return objectSet;
	}

}
