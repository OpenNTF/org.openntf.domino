package org.openntf.domino.nsfdata;

import java.util.Set;

public interface NSFDatabase {

	public abstract Set<NSFNote> getNotes();

	public abstract NSFNote getNoteById(int noteId);

	public abstract NSFNote getNoteByUniversalId(String universalId);

}