/**
 * 
 */
package org.openntf.domino;

/**
 * @author praml
 * 
 */
public interface Mapper {

	/**
	 * @param lotus
	 * @return
	 */
	org.openntf.domino.Document map(lotus.domino.Document nativeDocument, org.openntf.domino.Database database);

}
