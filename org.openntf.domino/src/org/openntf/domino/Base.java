package org.openntf.domino;

public interface Base<D extends lotus.domino.Base> extends lotus.domino.Base {

	public abstract D getDelegate();

}
