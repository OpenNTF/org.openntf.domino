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

import org.openntf.domino.WrapperFactory;
import org.openntf.domino.types.Resurrectable;

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
		extends Base<T, D, P> implements Resurrectable {

	/** The Constant log_. */
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(BaseThreadSafe.class.getName());

	/** Class holding the delegate-s */
	private class DelegateStruct {
		D _delegate = null;
		long _cppObject = 0;
		long _cppSession = 0;
	}

	/** The delegate, here ThreadLocal */
	private transient ThreadLocal<DelegateStruct> _delegateStruct;

	/**
	 * returns the cpp_id. DO NOT REMOVE. Otherwise native functions won't work
	 * 
	 * @return the cpp_id
	 */
	@Override
	public long GetCppObj() {
		return getDelegateStruct()._cppObject;
	}

	/**
	 * returns the cpp-session id. Needed for some BackendBridge functions
	 * 
	 * @return the cpp_id of the session
	 */
	@Override
	public long GetCppSession() {
		return getDelegateStruct()._cppSession;
	}

	/**
	 * Instantiates a new base.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent (may be null)
	 * @param wf
	 *            the wrapperFactory
	 * @param cppId
	 *            the cpp-id
	 * @param classId
	 *            the class id
	 */
	protected BaseThreadSafe(final D delegate, final P parent, final WrapperFactory wf, final long cppId, final int classId) {
		super(delegate, parent, wf, cppId, classId);
	}

	protected BaseThreadSafe(final P parent, final WrapperFactory wf, final int classId) {
		super(parent, wf, classId);
	}

	private DelegateStruct getDelegateStruct() {
		if (_delegateStruct == null) {
			_delegateStruct = new ThreadLocal<DelegateStruct>() {
				@Override
				protected DelegateStruct initialValue() {
					return new DelegateStruct();
				}
			};
		}
		return _delegateStruct.get();
	}

	/**
	 * Sets the delegate on init or if resurrect occurred
	 * 
	 * @param delegate
	 *            the delegate
	 * @param cppId
	 *            the cpp-id
	 */
	@Override
	void setDelegate(final D delegate, long cppId, final boolean fromResurrect) {
		if (cppId == 0)
			cppId = getLotusId(delegate);
		DelegateStruct ds = getDelegateStruct();
		ds._delegate = delegate;
		ds._cppObject = cppId;
		if (fromResurrect)
			getFactory().recacheLotusObject(delegate, this, parent_);
	}

	/**
	 * Gets the delegate without Resurrect
	 * 
	 * @return the delegate
	 */

	@Override
	protected D getDelegate_unchecked() {
		return getDelegateStruct()._delegate;
	}

	@Override
	void setCppSession() {
		DelegateStruct ds = getDelegateStruct();
		long cppSession;
		if (ds._delegate instanceof lotus.domino.Session)
			cppSession = ds._cppObject;
		else if (parent_ instanceof Base)
			cppSession = ((Base<?, ?, ?>) parent_).GetCppSession();
		else
			cppSession = 0;
		ds._cppSession = cppSession;
	}

	@Override
	protected abstract void resurrect();
}
