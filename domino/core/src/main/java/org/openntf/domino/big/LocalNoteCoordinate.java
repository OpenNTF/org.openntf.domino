package org.openntf.domino.big;

import java.io.Externalizable;

public interface LocalNoteCoordinate extends Externalizable, Comparable<LocalNoteCoordinate> {

	public NoteCoordinate toNoteCoordinate();

}
