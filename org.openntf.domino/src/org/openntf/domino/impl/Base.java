package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public abstract class Base<T extends org.openntf.domino.Base> implements org.openntf.domino.Base {
	protected boolean recycled_;
	protected T delegate_;

	protected Base() {
		// TODO Auto-generated constructor stub
		recycled_ = false;
	}

	T getDelegate() {
		return delegate_;
	}

	public void recycle() {
		DominoUtils.incinerate(getDelegate());
		recycled_ = true;
	}

	public boolean isRecycled() {
		return recycled_;
	}

	@SuppressWarnings("rawtypes")
	public void recycle(Vector arg0) throws NotesException {
		DominoUtils.incinerate(arg0);
	}

}
