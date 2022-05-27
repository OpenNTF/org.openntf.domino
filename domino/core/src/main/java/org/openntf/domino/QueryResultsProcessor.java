package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;

import lotus.domino.Base;
import lotus.domino.DominoQuery;

/**
 * Aggregates, computes, sorts, and formats collections of documents across any
 * set of Domino databases
 * 
 * @since 12.0.1
 */
public interface QueryResultsProcessor extends org.openntf.domino.Base<lotus.domino.QueryResultsProcessor>,
	lotus.domino.QueryResultsProcessor, DatabaseDescendant {

	/**
	 * Adds a pre-created DocumentCollection or ViewEntryCollection of documents
	 * to be included in the sorted and formatted results. The collection can be
	 * created by any means.
	 * 
	 * @param collection The pre-created {@link DocumentCollection} or
	 *        {@link ViewEntryCollection} object to be included
	 *        in QueryResultsProcessor execution
	 * @param referenceName A unique name (to the QueryResultsProcessor instance)
	 *        of the input collection. This name is used in returned
	 *        entries when the origin of results is desired and in
	 *        {@link #addFormula} method calls to override the data
	 *        used to create sorted column values.
	 */
	@Override
	void addCollection(Base collection, String referenceName);

	/**
	 * Creates a single column of values to be returned when QueryResultsProcessor is
	 * executed. Column values can be generated from a field or a formula. Sorting order,
	 * categorization and hidden attributes determine the returned stream of results
	 * entries. Columns span all input result sets and databases taking part in the
	 * QueryResultsProcessor. Execute calls in the QueryResultsProcessor require at least
	 * one column to be specified.
	 * 
	 * @param name The unique programmatic name of the column within a QueryResultsProcessor
	 *        instance. If there is no override using the addFormula method call,
	 *        the name provided is treated as a field name in each database involved
	 *        in the QueryResultsProcessor object.
	 * @param title The display title of the column. Used only in generated views, the title
	 *        is the UI column header.
	 * @param formula Formula language string to serve as the default means of computing
	 *        values for the column
	 * @param sortOrder A constant to indicate how to sort the column. Values are sorted
	 *        case and accent insensitively, by default. Legal values are
	 *        {@link #SORT_UNORDERED}, {@link #SORT_ASCENDING}, and {@link #SORT_DESCENDING}
	 * @param isHidden Specify this to sort by a column value but not return it. If
	 *        true, the column cannot have a sortOrder of {@code SORT_UNORDERED}
	 *        and cannot have an isCategorized value of {@code true}.
	 * @param isCategorized Categorized columns have a single entry returned for each
	 *        unique value with entries containing that value nested under it
	 */
	@Override
	void addColumn(String name, String title, String formula, int sortOrder, boolean isHidden, boolean isCategorized);

	/**
	 * Creates a single column of values to be returned when QueryResultsProcessor is
	 * executed. Column values can be generated from a field or a formula. Columns span
	 * all input result sets and databases taking part in the
	 * QueryResultsProcessor. Execute calls in the QueryResultsProcessor require at least
	 * one column to be specified.
	 * 
	 * @param name The unique programmatic name of the column within a QueryResultsProcessor
	 *        instance. If there is no override using the addFormula method call,
	 *        the name provided is treated as a field name in each database involved
	 *        in the QueryResultsProcessor object.
	 */
	@Override
	void addColumn(String name);

	/**
	 * Executes and adds the output set of documents from a {@link DominoQuery} to those included in
	 * QueryResultsProcessor instance. All {@code DominoQuery} parameters remain in force for the
	 * DQL query execution.
	 * 
	 * @param query A created and ready-to-execute {@link DominoQuery} class instance
	 * @param queryString The DQL query to be executed. It can include substitution variables and all
	 *        legal DQL syntax.
	 * @param referenceName A unique name (to the QueryResultsProcessor instance) of the input set of documents.
	 *        This name is used in returned entries when the origin of results is desired and in
	 *        {@link #addFormula} method calls to override the data used to create sorted column values.
	 */
	@Override
	void addDominoQuery(DominoQuery query, String queryString, String referenceName);

	/**
	 * Provides Domino formula language to override the data used to generate values for a particular sort
	 * column and an input collection or set of collections. Since input collections can be created from
	 * different databases, design differences can be adjusted using {@code addFormula} to have homogeneous
	 * values in the output.
	 * 
	 * @param formula Formula language string to be evaluated in order to supply the values for a sort column
	 * @param columnName String value responding the programmatic name of the sort column whose values are to
	 *        be generated using the formula language supplied
	 * @param referenceName Used to specify the input collection names to which will use the formula language
	 *        to generate sort column values. Wildcards may be specified to map to multiple input collection
	 *        names.
	 */
	@Override
	void addFormula(String formula, String columnName, String referenceName);

	/**
	 * Processes the input collections in the manner specified by the Sort Columns, overriding field values
	 * with formulas specified via addFormula calls, and returns JSON output.
	 * 
	 * @return the processed results as a JSON string
	 */
	@Override
	String executeToJSON();

	/**
	 * Saves QueryResultsProcesser results to a "results view" in a database. Processes the input collections
	 * in the manner specified by the Sort Columns, overriding field values with formulas specified via
	 * {@link #addFormula} calls. Creates a results view in a host database and returns a {@link View} object.
	 * 
	 * @param name The name of the results view to create and populate
	 * @param expireHours The time, in hours, for the view to be left in the host database. If not specified,
	 *        the view expires in 24 hours. You can extend the expiration time using the updall or dbmt tasks. 
	 * @param reader The name of one additional user or group to be granted read access to the results view.
	 *        The value must be in canonical format.
	 * @return a {@link View} object representing the created results view
	 */
	@Override
	View executeToView(String name, int expireHours, String reader);

	/**
	 * Saves QueryResultsProcesser results to a "results view" in a database. Processes the input collections
	 * in the manner specified by the Sort Columns, overriding field values with formulas specified via
	 * {@link #addFormula} calls. Creates a results view in a host database and returns a {@link View} object.
	 * 
	 * @param name The name of the results view to create and populate
	 * @param expireHours The time, in hours, for the view to be left in the host database. If not specified,
	 *        the view expires in 24 hours. You can extend the expiration time using the updall or dbmt tasks. 
	 * @param reader A string {@link Vector} of names to be granted read access to the results view. The values
	 *        must be in canonical format.
	 * @return a {@link View} object representing the created results view
	 */
	@Override
	View executeToView(String name, int expireHours, @SuppressWarnings("rawtypes") Vector readerList);

	/**
	 * Saves QueryResultsProcesser results to a "results view" in a database. Processes the input collections
	 * in the manner specified by the Sort Columns, overriding field values with formulas specified via
	 * {@link #addFormula} calls. Creates a results view in a host database and returns a {@link View} object.
	 * 
	 * @param name The name of the results view to create and populate
	 * @param expireHours The time, in hours, for the view to be left in the host database. If not specified,
	 *        the view expires in 24 hours. You can extend the expiration time using the updall or dbmt tasks.
	 * @return a {@link View} object representing the created results view
	 */
	@Override
	View executeToView(String name, int expireHours);

	/**
	 * Saves QueryResultsProcesser results to a "results view" in a database. Processes the input collections
	 * in the manner specified by the Sort Columns, overriding field values with formulas specified via
	 * {@link #addFormula} calls. Creates a results view in a host database and returns a {@link View} object.
	 * 
	 * @param name The name of the results view to create and populate
	 * @return a {@link View} object representing the created results view
	 */
	@Override
	View executeToView(String name);

	/**
	 * Returns the current setting of the maximum number of documents to fetch into the QueryResultsProcessor
	 * engine before an execute call ({@link #executeToJSON} or {@link #executeToView}) is terminated to prevent
	 * runaway processing.
	 * 
	 * @return the max number of documents to fetch during execution
	 */
	@Override
	int getMaxEntries();

	/**
	 * Returns the current setting of the time in seconds allowed before an execute call
	 * ({@link #executeToJSON} or {@link #executeToView}) is terminated to prevent runaway processing.
	 * 
	 * @return the max number of seconds to process during execution
	 */
	@Override
	int getTimeoutSec();

	/**
	 * Sets of the maximum number of documents to fetch into the QueryResultsProcessor
	 * engine before an execute call ({@link #executeToJSON} or {@link #executeToView}) is terminated to prevent
	 * runaway processing.
	 * 
	 * @param maxEntries the max number of documents to fetch during execution
	 */
	@Override
	void setMaxEntries(int maxEntries);

	/**
	 * Sets the time in seconds allowed before an execute call
	 * ({@link #executeToJSON} or {@link #executeToView}) is terminated to prevent runaway processing.
	 * 
	 * @param timeoutSec the max number of seconds to process during execution
	 */
	@Override
	void setTimeoutSec(int timeoutSec);

}
