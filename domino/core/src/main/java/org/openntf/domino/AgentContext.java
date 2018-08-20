/*
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the agent environment of the current program, if an agent is running it.
 * <h3>Creation and access</h3>
 * <p>
 * Use {@link Session#getAgentContext()} to get the <code>AgentContext</code> object for the current agent.
 * </p>
 */
public interface AgentContext
		extends Base<lotus.domino.AgentContext>, lotus.domino.AgentContext, org.openntf.domino.ext.AgentContext, SessionDescendant {

	public static class Schema extends FactorySchema<AgentContext, lotus.domino.AgentContext, Session> {
		@Override
		public Class<AgentContext> typeClass() {
			return AgentContext.class;
		}

		@Override
		public Class<lotus.domino.AgentContext> delegateClass() {
			return lotus.domino.AgentContext.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * The agent that's currently running.
	 */
	@Override
	public Agent getCurrentAgent();

	/**
	 * The database in which the current agent resides.
	 * <p>
	 * This property allows you to access a database without having to specify its server and file name. Using
	 * <code>getCurrentDatabase</code> can make programs more portable from one database to another.
	 * </p>
	 *
	 * @return The database in which the current agent resides.
	 *
	 */
	@Override
	public Database getCurrentDatabase();

	/**
	 * The in-memory document when an agent starts.
	 * <p>
	 * For an agent activated in a view through the Notes client UI, the in-memory document is the document highlighted in the view.
	 * </p>
	 * <p>
	 * For an agent activated "Before New Mail Arrives," the in-memory document is the email that is about to be delivered. Because the
	 * agent is activated instantly for each email as it's about to be saved into the mail database, each time the agent runs you are
	 * working with a single unsaved document. The {@link #getUnprocessedDocuments()} property will not return any documents for this agent
	 * type.
	 * </p>
	 * <p>
	 * For an agent run from a browser with the <code>OpenAgent</code> URL command, the in-memory document is a new document containing an
	 * item for each CGI (Common Gateway Interface) variable supported by Domino. Each item has the name and current value of a supported
	 * CGI variable. (No design work on your part is needed; the CGI variables are available automatically.)
	 * </p>
	 * <p>
	 * For an agent run from a browser with <code>&commat;Command([RunAgent])</code> or <code>&commat;Command[ToolsRunMacro]</code>, the
	 * in-memory document is the current document. In the case of WebQueryOpen, this is the document before Domino converts it to HTML and
	 * sends it to the browser; in the case of WebQuerySave, this is the document before Domino saves it. If the form on which the document
	 * is based contains a field named the same as a Domino-supported CGI variable, the in-memory document also contains the value of that
	 * variable. (You must explicitly design the CGI variables into the form, for example, as hidden fields.)
	 * </p>
	 * <p>
	 * See Table of CGI Variables in <em>Application Development with Domino Designer</em> for a list of the Domino-supported CGI variables.
	 * </p>
	 * <p>
	 * For scheduled agents, before mail arrives, after mail arrives, or any arent running on a Domino server and not invoked through a web
	 * browser, this property returns Nothing because there is no current document. For such agents, use {@link #getUnprocessedDocuments()}
	 * instead.
	 * </p>
	 * <p>
	 * Through the C or C++ API, an external program can create an in-memory document, then run an agent. The agent can use this property to
	 * access the in-memory document.
	 * </p>
	 * <p>
	 * The {@link Document#save()} method immediately updates the document represented by the Document object returned by DocumentContext.
	 * For an agent working on a selected document in the Notes client, you must save before exiting to preserve any changes. For an agent
	 * called from a browser, any changes go back to the browser when the agent exits; you probably do not want to save before exiting, but
	 * let the browser handle the changes.
	 * </p>
	 * <p>
	 * You cannot use the {@link Document#encrypt()} and {@link Document#remove(boolean)} methods on the Document object returned by
	 * <code>getDocumentContext</code>, nor use the compact method on the Database object that contains the Document object returned by
	 * <code>getDocumentContext</code>.
	 * </p>
	 *
	 * @return The in-memory document when an agent starts.
	 */
	@Override
	public Document getDocumentContext();

	/**
	 * The user name that is in effect for the current program.
	 * <p>
	 * The identity of the person under whose identity the program is running depends on whether you are running an agent, an XPage, a
	 * servlet, or a stand alone program.
	 * </p>
	 * <p>
	 * For an agent, selecting 'run as web user' will make this property use the identity of the logged in web user. If 'run as web user' is
	 * not selected, this property will use the identity of the agent signer.
	 * </p>
	 * <p>
	 * For everything else the identity is the identity established by the Session or automatically assigned by the environment.
	 * </p>
	 * <p>
	 * This property returns the fully distinguished name.
	 * </p>
	 * <p>
	 * For Java agents, this property will return the same value as the {@link Session#getEffectiveUserName()} property.
	 * </p>
	 *
	 * @return The user name that is in effect for the current program.
	 *
	 */
	@Override
	public String getEffectiveUserName();

	/**
	 * The exit status code returned by the Agent Manager the last time the current agent ran.
	 * <p>
	 * This property returns 0 if the agent ran without errors.
	 * </p>
	 *
	 * @return The exit status code returned by the Agent Manager the last time the current agent ran.
	 *
	 */
	@Override
	public int getLastExitStatus();

	/**
	 * The date and time when the current agent was last executed.
	 *
	 * @return The date and time when the current agent was last executed or null if the agent never ran before
	 *
	 */
	@Override
	public DateTime getLastRun();

	/**
	 * Gets the parent session.
	 *
	 * @return the parent session
	 */
	@Override
	public Session getParentSession();

	/**
	 * A document that an agent uses to store information between invocations. The agent can use the information in this document the next
	 * time the agent runs.
	 * <p>
	 * The <code>getSavedData</code> document is created when you save an agent, and it is stored in the same database as the agent. The
	 * document replicates, but is not displayed in views.
	 * </p>
	 * <p>
	 * Each time you edit and re-save an agent, its <code>getSavedData</code> document is deleted and a new, blank one is created. When you
	 * delete an agent, its <code>getSavedData</code> document is deleted.
	 * </p>
	 *
	 * @return A document that an agent uses to store information between invocations.
	 *
	 */
	@Override
	public lotus.domino.Document getSavedData();

	/**
	 * The documents in a database that the current agent considers to be "unprocessed." The type of agent determines which documents are
	 * considered unprocessed.
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 *
	 * @return The documents in a database that the current agent considers to be "unprocessed."
	 *
	 */
	@Override
	public DocumentCollection getUnprocessedDocuments();

	/**
	 * Given a full-text query, returns documents in a database.
	 * <ul>
	 * <li>That the current agent considers to be unprocessed</li>
	 * <li>And that match the query</li>
	 * </ul>
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 * <h5>How does it work?</h5>
	 * <p>
	 * This method works in two parts:
	 * </p>
	 * <ul>
	 * <li>First, it finds a collection of documents that the agent considers to be "unprocessed." The type of agent determines which
	 * documents are considered to be unprocessed. This document collection is identical to that returned by
	 * {@link #getUnprocessedDocuments()}.</li>
	 * <li>Second, it conducts a full-text search on the unprocessed documents and returns a collection of those documents that match the
	 * query.</li>
	 * </ul>
	 * <p>
	 * For example, in an agent that runs on all selected documents in a view, <code>getUnprocessedFTSearch</code> searches only the
	 * selected documents and returns those that match the query. In an agent that runs on all new and modified documents since the last
	 * run, <code>getUnprocessedFTSearch</code> searches only the documents that were not marked by
	 * {@link #updateProcessedDoc(lotus.domino.Document)}, and returns those that match the query.
	 * </p>
	 * <h5>What documents are returned?</h5>
	 * <p>
	 * The following table describes the documents that are returned by <code>unprocessedFTSearch</code>. Document selection occurs once,
	 * before the agent runs.
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" class="table" rules="all" frame="border" border="1">
	 * <thead class="thead" align="left" valign="bottom">
	 * <tr >
	 * <th align="left" valign="top" width="NaN%" id="d3016224e219">
	 * <p >
	 * Agent runs on:
	 * </p>
	 * </th>
	 * <th align="left" valign="top" width="NaN%" id="d3016224e224">
	 * <p >
	 * unprocessedFTSearch returns documents that meet all of these requirements:
	 * </p>
	 * </th>
	 * </tr>
	 * </thead> <tbody class="tbody">
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * All documents in database
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Meet the search criteria specified in Agent Properties box
	 * </p>
	 * <p >
	 * Meet the full-text search critieria specified in this method
	 * </p>
	 * </td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * All new &amp; modified documents
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Not processed by this agent with updateProcessedDoc
	 * </p>
	 * <p >
	 * Created or modified since the agent last ran
	 * </p>
	 * <p >
	 * Meet the search criteria specified in Agent Properties box
	 * </p>
	 * <p >
	 * Meet the full-text search critieria specified in this method
	 * </p>
	 * </td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * All unread documents in view
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Are unread and in the view
	 * </p>
	 * <p >
	 * Meet the search criteria specified in Agent Properties box
	 * </p>
	 * <p >
	 * Meet the full-text search criteria specified in this method
	 * </p>
	 * </td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * All documents in view
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Are in the view
	 * </p>
	 * <p >
	 * Meet the search criteria specified in Agent Properties box
	 * </p>
	 * <p >
	 * Meet the full-text search criteria specified in this method
	 * </p>
	 * </td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * All selected documents
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Are selected in the view
	 * </p>
	 * <p >
	 * Meet the search criteria specified in Agent Properties box
	 * </p>
	 * <p >
	 * Meet the full-text search criteria specified in this method
	 * </p>
	 * </td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e219 ">
	 * <p >
	 * None
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d3016224e224 ">
	 * <p >
	 * Is the current document only
	 * </p>
	 * </td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <h5>Using updateProcessedDoc</h5>
	 * <p>
	 * For agents that run on new and modified documents, you must use {@link #updateProcessedDoc(lotus.domino.Document)} to mark each
	 * document as "processed," which ensures that a document gets processed by the agent only once (unless it's modified again). If you do
	 * not call this method for each document, the agent processes the same documents the next time it runs.
	 * </p>
	 * <p>
	 * The {@link #updateProcessedDoc(lotus.domino.Document)} method marks a document as processed only for the particular agent from which
	 * it is called. Using {@link #updateProcessedDoc(lotus.domino.Document)} in one agent has no effect on the documents that another agent
	 * processes.
	 * </p>
	 * <p>
	 * In all other agents and view actions, {@link #updateProcessedDoc(lotus.domino.Document)} has no effect.
	 * </p>
	 * <h5>View actions</h5>
	 * <p>
	 * When used in a view action, <code>unprocessedFTSearch</code> returns the same documents as an agent that runs on selected documents.
	 * </p>
	 * <h5>Full-text indexes</h5>
	 * <p>
	 * If the database is not full-text indexed, this method works, but less efficiently. To test for an index, use
	 * {@link Database#isFTIndexed()}. To create an index on a local database, use {@link Database#updateFTIndex(boolean)}.
	 * </p>
	 *
	 * <p>
	 * This method returns a maximum of 5,000 documents by default. The Notes .ini variable FT_MAX_SEARCH_RESULTS overrides this limit for
	 * indexed databases or databases that are not indexed but that are running in an agent on the client. For a database that is not
	 * indexed and is running in an agent on the server, you must set the TEMP_INDEX_MAX_DOC Notes.ini variable as well. The absolute
	 * maximum is 2,147,483,647.
	 * </p>
	 *
	 * <h5>Options</h5>
	 * <p>
	 * If you don't specify any sort options, the documents are sorted by relevance score. When the collection is sorted by relevance the
	 * highest relevance appears first. To access the relevance score of each document in the collection, use
	 * {@link Document#getFTSearchScore()}.
	 * </p>
	 *
	 * <p>
	 * If you ask for a sort by date, you don't get relevance scores. If you pass the resulting {@link DocumentCollection} to a
	 * {@link Newsletter} object, it formats its doclink report with either the document creation date or the relevance score, depending on
	 * the sort options.
	 * </p>
	 * <h5>Query syntax</h5>
	 * <p>
	 * To search for a word or phrase, enter the word or phrase as is, except that search keywords must be enclosed in quotes. Remember to
	 * escape quotes if you are inside a literal.
	 * </p>
	 *
	 * <p>
	 * Wildcards, operators, and other syntax are permitted. For the complete syntax rules, see "Refining a search query using operators" in
	 * Notes Help. Search for "query syntax" in the Domino Designer Eclipse help system or information center (for example,
	 * http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp), both of which include Notes.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned; 0 means all matching documents.
	 * @return A sorted collection of documents that are not yet processed and match the query.
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs);

	/**
	 * Given a full-text query, returns documents in a database.
	 * <ul>
	 * <li>That the current agent considers to be unprocessed</li>
	 * <li>And that match the query</li>
	 * </ul>
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned; 0 means all matching documents.
	 * @param sortOpt
	 *            Use one of these to specify a sorting option:
	 *            <ul>
	 *            <li>Database.FT_SCORES (default) sorts by relevance score with highest relevance first.</li>
	 *            <li>Database.FT_DATECREATED_DES sorts by document creation date in descending order.</li>
	 *            <li>Database.FT_DATECREATED_ASC sorts by document creation date in ascending order.</li>
	 *            <li>Database.FT_DATE_DES sorts by document date in descending order.</li>
	 *            <li>Database.FT_DATE_ASC sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical OR
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_DATABASE includes Domino® databases in the search scope.</li>
	 *            <li>Database.FT_FILESYSTEM includes files other than Domino databases in the search scope.</li>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @return A sorted collection of documents that are not yet processed and match the query.
	 * @see AgentContext#unprocessedFTSearch(String, int) unprocessedFTSearch(String, int) for detailed description
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt);

	/**
	 * Given a full-text query, returns documents in a database.
	 * <ul>
	 * <li>That the current agent considers to be unprocessed</li>
	 * <li>And that match the query</li>
	 * </ul>
	 * <p>
	 * This method is the same as {@link #unprocessedFTSearch(String, int)} plus the start parameter.
	 * </p>
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned; 0 means all matching documents.
	 * @param start
	 *            The starting document to return.
	 * @return A sorted collection of documents that are not yet processed and match the query.
	 * @see AgentContext#unprocessedFTSearch(String, int) unprocessedFTSearch(String, int) for detailed description
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int start);

	/**
	 * Given a full-text query, returns documents in a database.
	 * <ul>
	 * <li>That the current agent considers to be unprocessed</li>
	 * <li>And that match the query</li>
	 * </ul>
	 * <p>
	 * This method is the same as This method is the same as {@link #unprocessedFTSearch(String, int)} plus the start parameter.
	 * </p>
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 *
	 * @param query
	 *            The full-text query.
	 * @param maxDocs
	 *            The maximum number of documents you want returned; 0 means all matching documents.
	 * @param sortOpt
	 *            Use one of these to specify a sorting option:
	 *            <ul>
	 *            <li>Database.FT_SCORES (default) sorts by relevance score with highest relevance first.</li>
	 *            <li>Database.FT_DATECREATED_DES sorts by document creation date in descending order.</li>
	 *            <li>Database.FT_DATECREATED_ASC sorts by document creation date in ascending order.</li>
	 *            <li>Database.FT_DATE_DES sorts by document date in descending order.</li>
	 *            <li>Database.FT_DATE_ASC sorts by document date in ascending order.</li>
	 *            </ul>
	 * @param otherOpt
	 *            Use the following constants to specify additional search options. To specify more than one option, use a logical OR
	 *            operation.
	 *            <ul>
	 *            <li>Database.FT_DATABASE includes Domino® databases in the search scope.</li>
	 *            <li>Database.FT_FILESYSTEM includes files other than Domino databases in the search scope.</li>
	 *            <li>Database.FT_FUZZY specifies a fuzzy search.</li>
	 *            <li>Database.FT_STEMS uses stem words as the basis of the search.</li>
	 *            </ul>
	 * @param start
	 *            The starting document to return.
	 * @return A sorted collection of documents that are not yet processed and match the query.
	 * @see AgentContext#unprocessedFTSearch(String, int) unprocessedFTSearch(String, int) for detailed description
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt,
			final int start);

	/**
	 * Given selection criteria, returns documents in a database.
	 * <ul>
	 * <li>The current agent considers to be unprocessed</li>
	 * <li>Meet the selection criteria</li>
	 * <li>Were created or modified since the cutoff date</li>
	 * </ul>
	 * <p>
	 * This method is valid only for agents.
	 * </p>
	 *
	 * @param formula
	 *            A Domino formula that defines the selection criteria.
	 * @param limit
	 *            A cutoff date.
	 * @param maxDocs
	 *            The maximum number of documents you want returned; 0 means all matching documents.
	 * @return A collection of documents that are not yet processed, match the selection criteria, and were created or modified after the
	 *         cutoff date. The collection is sorted by relevance with highest relevance first.
	 */
	@Override
	public DocumentCollection unprocessedSearch(final String formula, final lotus.domino.DateTime limit, final int maxDocs);

	/**
	 * Marks a document as processed by an agent.
	 * <p>
	 * Use this method in conjunction with {@link #getUnprocessedDocuments()}, {@link #unprocessedFTSearch(String, int)}, and
	 * {@link #unprocessedSearch(String, lotus.domino.DateTime, int)} in an agent that runs on all new and modified documents.
	 * </p>
	 * <p>
	 * This method marks a document so subsequent invocations of the same agent recognize the document as processed. You must explicitly
	 * mark a document with this method. No implicit marking occurs in a Java agent.
	 * </p>
	 * <p>
	 * If a marked document is modified, it is unmarked and will be processed by the next invocation of the agent.
	 * </p>
	 * <p>
	 * This method marks a document as processed only for the particular agent from which it is called. Using this method in one agent has
	 * no effect on the documents that another agent processes.
	 * </p>
	 *
	 * @param doc
	 *            The document to be marked as processed. Cannot be null.
	 */
	@Override
	public void updateProcessedDoc(final lotus.domino.Document doc);

}
