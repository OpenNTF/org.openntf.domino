package org.openntf.domino;


import org.openntf.domino.DocumentCollection;
import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents the configuration and execution of a DQL query.
 *
 * @since Domino 10.0.1
 */
public interface DominoQuery extends lotus.domino.DominoQuery, Base<lotus.domino.DominoQuery>, DatabaseDescendant {
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

}
