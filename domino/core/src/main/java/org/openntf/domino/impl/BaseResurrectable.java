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
