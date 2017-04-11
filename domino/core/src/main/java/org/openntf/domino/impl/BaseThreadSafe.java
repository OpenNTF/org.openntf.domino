/*
 * Copyright 2014
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
package org.openntf.domino.impl;

import java.util.logging.Logger;

/**
 * A common Base class for those org.openntf.domino objects, which may be shared across threads.
 *
 * @param <T>
 *            the generic type
 * @param <D>
 *            the delegate type
 * @param <P>
 *            the parent type
 *
 */
public abstract class BaseThreadSafe<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base, P extends org.openntf.domino.Base<?>>
		extends Base<T, D, P> {

	/** The Constant log_. */
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BaseThreadSafe.class.getName());

	/** The delegate, here ThreadLocal */
	protected transient ThreadLocal<D> _delegateLocal;

	/**
	 * Instantiates a new base.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent (may be null)
	 * @param classId
	 *            the class id
	 */
	protected BaseThreadSafe(final D delegate, final P parent, final int classId) {
		super(delegate, parent, classId);
	}

	/**
	 * constructor for no arg child objects
	 */
	protected BaseThreadSafe(final int classId) {
		super(classId);
	}

	/**
	 * Sets the delegate on init or if resurrect occurred
	 *
	 * @param delegate
	 *            the delegate
	 */
	@Override
	final protected void setDelegate(final D delegate, final boolean fromResurrect) {
		if (_delegateLocal == null) {
			_delegateLocal = new ThreadLocal<D>();
		}
		_delegateLocal.set(delegate);
		if (fromResurrect) {
			getFactory().recacheLotusObject(delegate, this, parent);
		}
	}

	/**
	 * Gets the delegate without Resurrect
	 *
	 * @return the delegate
	 */

	@Override
	protected D getDelegate_unchecked() {
		if (_delegateLocal == null) {
			return null;
		}
		return _delegateLocal.get();
	}

	/**
	 * Gets the delegate without Resurrect
	 * 
	 * @return the delegate directly
	 */

	@Override
	protected D getDelegate_unchecked(final boolean fromIsDead) {
		if (_delegateLocal == null)
			return null;
		return _delegateLocal.get();
	}

}
