package org.openntf.domino;


public interface Name extends lotus.domino.Name, org.openntf.domino.Base<lotus.domino.Name> {
	@Override
	public String getCanonical();

	@Override
	public void recycle();
}
