package org.openntf.domino.ext;

import java.util.List;

/**
 * OpenNTF Domino API extensions for the {@code DominoQuery} class.
 * 
 * @author Jesse Gallagher
 * @since 12.0.1
 */
public interface DominoQuery {

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemNames the names of the items to index
	 * @param visible whether the view should be visible in the Notes client (e.g. created
	 *                without parentheses in the name)
	 * @param nobuild whether to skip building the view immediately
	 * @since V12.0.1
	 */
	void createIndex(String indexName, List<String> itemNames, boolean visible, boolean nobuild);

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemName the names of the items to index
	 * @since V12.0.1
	 */
	void createIndex(String indexName, List<String> itemNames);
}
