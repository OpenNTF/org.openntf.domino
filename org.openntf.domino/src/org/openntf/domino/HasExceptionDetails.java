/**
 * 
 */
package org.openntf.domino;

import java.util.List;

/**
 * The interface HasExceptionDetails; useful to add further information to an OpenNTFNotesException. Of course, Document, Item, Database,
 * Session, ... implement this interface
 * 
 */
public interface HasExceptionDetails {

	public void fillExceptionDetails(List<String> result);
}
