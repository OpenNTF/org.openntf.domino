/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino;


import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents the configuration and execution of a DQL query.
 *
 * @since Domino 10.0.1
 */
public interface DominoQuery extends lotus.domino.DominoQuery, Base<lotus.domino.DominoQuery>, DatabaseDescendant,
	org.openntf.domino.ext.DominoQuery {
	
	public static class Schema extends FactorySchema<DominoQuery, lotus.domino.DominoQuery, Database> {

		@Override
		public Class<DominoQuery> typeClass() {
			return DominoQuery.class;
		}

		@Override
		public Class<lotus.domino.DominoQuery> delegateClass() {
			return lotus.domino.DominoQuery.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	}

	public static final Schema SCHEMA = new Schema();

	public static final int MAX_SCAN_DOCS_DEFAULT = 500000;
	public static final int MAX_SCAN_ENTRIES_DEFAULT = 200000;
	public static final int TIMEOUT_SEC_DEFAULT = 300;

	/**
	 * Executes a query string passed in according to set parameters and returns a
	 * {@link DocumentCollection}.
	 *
	 * @param query the DQL query to execute
	 * @return a {@link DocumentCollection} of matching documents from the database
	 */
	@Override
	DocumentCollection execute(String query);

	/**
	 * Executes a query string passed in according to set parameters and returns a
	 * {@link DocumentCollection}.
	 *
	 * @param query the DQL query to execute
	 * @param resultName the name to use to store the result in the database for suture use
	 * @param replace whether to overwrite an existing named index if it exists
	 * @param expireHours a duration in hours before the result should expire
	 * @return a {@link DocumentCollection} of matching documents from the database
	 * @since 12.0.1
	 */
	@Override
	DocumentCollection execute(String query, String resultName, boolean replace, int expireHours);

	/**
	 * Executes a query string passed in according to set parameters and returns a
	 * {@link DocumentCollection}.
	 *
	 * @param query the DQL query to execute
	 * @param resultName the name to use to store the result in the database for suture use
	 * @return a {@link DocumentCollection} of matching documents from the database
	 * @since 12.0.1
	 */
	@Override
	DocumentCollection execute(String query, String resultName);

	/**
	 * Executes a query string passed in according to set parameters and returns an "explain" string to
	 * indicate how the query was executed.
	 *
	 * <p>This method is useful in determining the efficiency of a given query.</p<
	 *
	 * @param query the DQL query to execute
	 */
	@Override
	String explain(String query);

	/**
	 * Specifies the maximum allowable number of documents scanned across all query terms. DQL
	 * execution returns an error when exceeded. Default is {@value #MAX_SCAN_DOCS_DEFAULT}.
	 */
	@Override
	int getMaxScanDocs();

	/**
	 * Specifies the maximum allowable number of view entries scanned across all query terms.
	 * DQL execution return an error when exceeded. Default is {@value #MAX_SCAN_ENTRIES_DEFAULT}.
	 */
	@Override
	int getMaxScanEntries();

	/**
	 * Specifies the maximum allowable seconds a DQL query is allowed to run. DQL execution
	 * returns an error when exceeded. Default is {@value #TIMEOUT_SEC_DEFAULT}.
	 */
	@Override
	int getTimeoutSec();

	/**
	 * Specifies not to perform any view processing in satisfying a query. Default is {@code false}.
	 */
	@Override
	boolean isNoViews();

	/**
	 * Specifies whether DQL processing should refresh every view it opens to perform any view processing to
	 * satisfy a query. Default is {@code false}.
	 */
	@Override
	boolean isRefreshViews();

	/**
	 * Parses a DQL query string for correct syntax.
	 *
	 * @param query the DQL query to parse
	 * @return any error messages produced by a malformed query
	 */
	@Override
	String parse(String query);

	/**
	 * Removes all previously set named variables and values.
	 */
	@Override
	void resetNamedVariables();

	/**
	 * Sets the maximum allowable number of documents scanned across all query terms.
	 *
	 * @param maxScanDocs the maximum number of documents to scan
	 */
	@Override
	void setMaxScanDocs(int maxScanDocs);

	/**
	 * Specifies the maximum allowable number of view entries scanned across all query terms.
	 *
	 * @param maxScanEntries the maximum number of view entries to scan
	 */
	@Override
	void setMaxScanEntries(int maxScanEntries);

	/**
	 * Assigns a value to a named substitution variable in a DQL query.
	 *
	 * <p>Substitution variables can be specified in the query syntax as "?varName".</p>
	 *
	 * @param varName the name of a substitution variable in a DQL query
	 * @param value the value to be processed as Text, Number, or DateTime in DQL
	 * @see <a href="https://www.ibm.com/support/knowledgecenter/SSVRGU_10.0.1/basic/H_SETNAMEDVARIABLE_METHOD_JAVA.html">SetNamedVariable method (DominoQuery - Java)</a>
	 */
	@Override
	void setNamedVariable(String varName, Object value);

	/**
	 * Sets whether or not to perform any view processing in satisfying a query.
	 *
	 * @param noViews whether or not to perform view processing
	 */
	@Override
	void setNoViews(boolean noViews);

	/**
	 * Sets whether or not DQL processing refreshes every view it opens to perform any view processing to
	 * satisfy a query.
	 *
	 * @param refreshViews whether or not to refresh views during processing
	 */
	@Override
	void setRefreshViews(boolean refreshViews);

	/**
	 * Sets the maximum allowable seconds a DQL query is allowed to run.
	 *
	 * @param timeoutSec the timeout threshold in seconds
	 */
	@Override
	void setTimeoutSec(int timeoutSec);
	
	/**
	 * Specifies to DQL processing to completely rebuild the Design Catalog before processing a query 
	 * using Explain or Execute.
	 * 
	 * @return boolean
	 * @since 11.0.0
	 */
	@Override
	boolean isRebuildDesignCatalog();
	
	/**
	 * Specifies to DQL processing to completely rebuild the Design Catalog before processing a query 
	 * using Explain or Execute.
	 * 
	 * While use of this property can be costly and contentious, it forces a 
	 * complete rebuild of the Design Catalog prior to running a query.
	 * 
	 * @param rebuildCatalog whether to rebuild design catalog
	 * @since 11.0.0
	 */
	@Override
	void setRebuildDesignCatalog(boolean rebuildCatalog);
	
	/**
	 * Specifies to DQL processing to refresh the Design Catalog before processing a query using Explain 
	 * or Execute.
	 * 
	 * @return boolean
	 * @since 11.0.0
	 */
	@Override
	boolean isRefreshDesignCatalog();
	
	/**
	 * Specifies to DQL processing to refresh the Design Catalog before processing a query using Explain 
	 * or Execute.
	 * 
	 * While use of this property is not free in resource usage or time, it is less costly than 
	 * RebuildDesignCatalog and it ensures the state of current database design is reflected accurately 
	 * in the Design Catalog documents for the current database.
	 * 
	 * @param boolean
	 * @since 11.0.0
	 */
	@Override
	void setRefreshDesignCatalog(boolean refreshCatalog);
	
	/**
	 * Specifies to DQL processing to refresh the full text index for a database prior to query processing 
	 * using Explain or Execute.
	 * 
	 * @return boolean
	 * @since 11.0.0
	 */
	@Override
	boolean isRefreshFullText();
	
	/**
	 * Specifies to DQL processing to refresh the full text index for a database prior to query processing 
	 * using Explain or Execute.
	 * 
	 * While use of this property is not free in resource usage or time, it is only slightly contentious 
	 * and guarantees the latest updates are full text indexed prior to query processing.
	 * 
	 * @param refresh whether to refresh the full-text index before execution
	 * @since 11.0.0
	 */
	@Override
	void setRefreshFullText(boolean refresh);

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemName the name of the item to index
	 * @param visible whether the view should be visible in the Notes client (e.g. created
	 *                without parentheses in the name)
	 * @param nobuild whether to skip building the view immediately
	 * @since 12.0.0
	 */
	@Override
	void createIndex(String indexName, String itemName, boolean visible, boolean nobuild);

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemName the name of the item to index
	 * @since 12.0.0
	 */
	@Override
	void createIndex(String indexName, String itemName);
	
	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemNames the names of the items to index
	 * @param visible whether the view should be visible in the Notes client (e.g. created
	 *                without parentheses in the name)
	 * @param nobuild whether to skip building the view immediately
	 * @since 12.0.0
	 */
	@Override
	@SuppressWarnings("rawtypes")
	void createIndex(String indexName, Vector itemNames, boolean visible, boolean nobuild);

	/**
	 * Creates an index view optimized for DQL query terms.
	 * 
	 * @param indexName the name of the index to create
	 * @param itemName the names of the items to index
	 * @since 12.0.0
	 */
	@Override
	@SuppressWarnings("rawtypes")
	void createIndex(String indexName, Vector itemNames);

	/**
	 * Lists the DQL-optimized indexes in the database
	 * 
	 * @return a string containing a JSON object describing the indexes
	 * @since 12.0.0
	 */
	@Override
	String listIndexes();

	/**
	 * Removes the specified named index.
	 * 
	 * @param indexName the name of the index to remove
	 * @since 12.0.0
	 */
	@Override
	void removeIndex(String indexName);

	/**
	 * Removes the specified named result.
	 * 
	 * @param resultName the name of the stored result to remove
	 * @since 12.0.0
	 */
	@Override
	void removeNamedResult(String resultName);

}
