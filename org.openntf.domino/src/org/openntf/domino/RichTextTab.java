package org.openntf.domino;

import java.util.Vector;

public interface RichTextTab extends Base<lotus.domino.RichTextTab>, lotus.domino.RichTextTab {

	@Override
	public void clear();

	public lotus.domino.RichTextTab getDelegate();

	public int getPosition();

	@Override
	public int getType();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
