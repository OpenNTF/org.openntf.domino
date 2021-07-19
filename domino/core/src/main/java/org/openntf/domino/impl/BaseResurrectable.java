/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.impl;

import org.openntf.domino.types.Resurrectable;

public abstract class BaseResurrectable<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base, P extends org.openntf.domino.Base<?>>
		extends BaseThreadSafe<T, D, P> implements Resurrectable {
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
	protected BaseResurrectable(final D delegate, final P parent, final int classId) {
		super(delegate, parent, classId);
	}

	/**
	 * constructor for no arg child objects
	 */
	protected BaseResurrectable(final int classId) {
		super(classId);
	}

	@Override
	protected abstract void resurrect();
}
