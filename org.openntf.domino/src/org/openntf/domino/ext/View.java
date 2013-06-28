/**
 * 
 */
package org.openntf.domino.ext;

import org.openntf.domino.DocumentCollection;

/**
 * @author withersp
 * 
 */
public interface View {

	/**
	 * @return DocumentCollection
	 */
	public DocumentCollection getAllDocuments();

	/**
	 * @return NoteCollection
	 */
	public NoteCollection getNoteCollection();

}
