package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public abstract class Base<T extends org.openntf.domino.Base<D>, D extends lotus.domino.Base> implements org.openntf.domino.Base<D> {

	protected boolean recycled_;
	protected D delegate_; // NTF final???

	protected Base() {
		// TODO Auto-generated constructor stub
		recycled_ = false;
	}

	protected Base(D delegate) {
		this();
		delegate_ = delegate;
	}

	public D getDelegate() {
		return delegate_;
	}

	public void recycle() {
		DominoUtils.incinerate(getDelegate());
		recycled_ = true;
	}

	public boolean isRecycled() {
		return recycled_;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	public void recycle(Vector arg0) throws NotesException {
		DominoUtils.incinerate(arg0);
	}

}
