package org.openntf.domino.big;

import java.io.Externalizable;
import java.util.List;

public interface NoteList extends List<NoteCoordinate>, Externalizable {
	public void loadByteArray(final byte[] bytes);

	public void sortBy(final String key);

	public byte[] toByteArray();
}
