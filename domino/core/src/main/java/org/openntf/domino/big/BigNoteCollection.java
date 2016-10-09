/**
 * 
 */
package org.openntf.domino.big;

import java.io.Externalizable;

import org.openntf.domino.annotations.Incomplete;

/**
 * @author nfreeman
 * 
 */
@Incomplete
public interface BigNoteCollection extends Externalizable, Iterable<String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int)
	 */
	public void add(final String filepath, final int additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(int[])
	 */
	public void add(final String filepath, final int[] additionSpecifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NoteCollection#add(java.lang.String)
	 */
	public void add(final String filepath, final String noteid);
}
