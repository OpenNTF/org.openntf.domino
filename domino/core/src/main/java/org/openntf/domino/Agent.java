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

import java.io.Externalizable;
import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;

/**
 * An agent in a Notes database.
 * <p>
 * Calling one of the {@link #run()} methods fires an {@link org.openntf.domino.events.IDominoEvent} event. Check the method's documentation
 * for details about the events.
 * </p>
 * <p>
 * There are two ways to access an agent:
 * </p>
 * <ul>
 * <li>To access the agent that's currently running, use {@link AgentContext#getCurrentAgent()}.</li>
 * <li>To access all the agents in a database, use {@link Database#getAgents()}.</li>
 * </ul>
 * <p>
 * System.out writes to an Events document ("Miscellaneous Events" view) in the log.nsf database where the code executes for scheduled
 * agents and agents started by {@link #run()} or {@link #runOnServer()}.
 * </p>
 */
public interface Agent
		extends Base<lotus.domino.Agent>, lotus.domino.Agent, org.openntf.domino.ext.Agent, Design, DatabaseDescendant, Externalizable {

	public static class Schema extends FactorySchema<Agent, lotus.domino.Agent, Database> {
		@Override
		public Class<Agent> typeClass() {
			return Agent.class;
		}

		@Override
		public Class<lotus.domino.Agent> delegateClass() {
			return lotus.domino.Agent.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * The comment that describes an agent, as entered by the agent's designer.
	 */
	@Override
	public String getComment();

	/**
	 * The common name of the person who last modified and saved an agent.
	 * <p>
	 * If the owner's name is flat (non-hierarchical), the <code>CommonOwner</code> name is the same as the {@link #getOwner()} name.
	 * </p>
	 */
	@Override
	public String getCommonOwner();

	/**
	 * The Domino URL of an agent when HTTP protocols are in effect. If HTTP protocols are not available, this property returns an empty
	 * string.
	 */
	@Override
	public String getHttpURL();

	/**
	 * The date that an agent last ran. If the script has never been run before, this property returns null.
	 */
	@Override
	public DateTime getLastRun();

	/**
	 * The names of the holders of a lock.
	 * <p>
	 * If the agent is locked, the vector contains the names of the lock holders. The agent can be locked by one or more users or groups.
	 *
	 * If the agent is not locked, the vector contains one element whose value is an empty string ("").
	 * </p>
	 */
	@Override
	public Vector<String> getLockHolders();

	/**
	 * The name of an agent. Within a database, the name of an agent may not be unique.
	 */
	@Override
	public String getName();

	/**
	 * The Domino URL of an agent when Notes protocols are in effect. If Notes protocols are not available, this property returns an empty
	 * string.
	 */
	@Override
	public String getNotesURL();

	/**
	 * Name of the user under whose identity the agent runs.
	 * <p>
	 * This property reflects the value of "Run on behalf of" under the security tab of the agent builder.
	 *
	 * If this property is an empty string, the agent runs on behalf of the owner of the agent or the browser login user (if the agent is
	 * run from a browser and "Run as web user" is checked).
	 * </p>
	 */
	@Override
	public String getOnBehalfOf();

	/**
	 * The name of the person who last modified and saved an agent.
	 * <p>
	 * If the owner's name is hierarchical, this property returns the fully distinguished name.
	 *
	 * Saving the agent changes the owner immediately. However, if you subsequently call this method within the same <code>Session</code>,
	 * the previous owner's name will be returned. The ownership change is not reflected in properties until the next time a
	 * <code>Session</code> is obtained.
	 * </p>
	 */
	@Override
	public String getOwner();

	/**
	 * Returns the NoteID of a document passed in by {@link #run(String)} or {@link #runOnServer(String)}.
	 *
	 * @return the NoteID of document. Use {@link Database#getDocumentByID(int)} to get a document through its NoteID.
	 */
	@Override
	public String getParameterDocID();

	/**
	 * The database that contains this agent.
	 */
	@Override
	public Database getParent();

	/**
	 * The text of the query used by an agent to select documents. In the Agent Properties box, a query is defined by the searches added to
	 * the agent using the Add Search button.
	 * <p>
	 * If no query is defined with the Add Search button, this method returns an empty string, even if the agent runs a formula that has its
	 * own SELECT statement or a script that selects specific documents.
	 * </p>
	 * <p>
	 * Some sample results: <du>
	 * <dl>
	 * If an agent searches for documents that contain the word "tulip," Query returns:
	 * </dl>
	 * <dd>("tulip")</dd>
	 * <dl>
	 * If an agent searches for documents that contain the word tulip and were created on September 19, 1996, Query returns:
	 * </dl>
	 * <dd>("tulip") AND ([_CreationDate] = 09/19/96)</dd>
	 * <dl>
	 * If an agent searches for documents that use the Response form, Query returns:
	 * </dl>
	 * <dd>(([Form]="Response"))</dd>
	 * <dl>
	 * If an agent searches for documents that use the Response form and contain the phrase "mustard greens" in the Subject field, Query
	 * returns:
	 * </dl>
	 * <dd>(([Form]="Response")) AND ([Subject] CONTAINS (mustard greens))</dd>
	 * </p>
	 */
	@Override
	public String getQuery();

	/**
	 * The name of the server on which this agent runs.
	 * <p>
	 * The value returned by this method depends upon whether the agent is scheduled:
	 * </p>
	 * <ul>
	 * <li>If the agent is scheduled, the property returns the name of the server on which the scheduled agent runs. Since scheduled agents
	 * can only run on a single replica of a database, you designate a server name for the agent under Schedule in the Agent Properties box.
	 * Therefore, the <code>ServerName</code> property may represent the parent database's server, or it may represent a replica's
	 * server.</li>
	 * <li>If the agent is not scheduled, this property returns an empty string. You can set <code>ServerName</code> to the asterisk (*) to
	 * indicate that the agent can run on any server.</li>
	 * <li>A null ServerName means the local workstation.</li>
	 * </ul>
	 *
	 */
	@Override
	public String getServerName();

	/**
	 * Indicates on which documents this agent acts.This property corresponds to the Target option in the Runtime section of the Agent
	 * Properties box. The trigger limits the target possibilities. The TARGET_NONE targets are the only possibilities for their
	 * corresponding triggers.
	 *
	 * @return One of
	 *         <ul>
	 *         <li>Agent.TARGET_ALL_DOCS ("All documents in database")</li>
	 *         <li>Agent.TARGET_ALL_DOCS_IN_VIEW ("All documents in view")</li>
	 *         <li>Agent.TARGET_NEW_DOCS (Not used)</li>
	 *         <li>Agent.TARGET_NEW_OR_MODIFIED_DOCS ("All new & modified documents")</li>
	 *         <li>Agent.TARGET_NONE ("Each incoming mail document," "Newly received mail documents," or "Pasted documents")</li>
	 *         <li>Agent.TARGET_SELECTED_DOCS ("All selected documents")</li>
	 *         <li>Agent.TARGET_UNREAD_DOCS_IN_VIEW ("All unread documents in view")</li>
	 *         <li>Agent.TARGET_RUN_ONCE ("None")</li>
	 *         </ul>
	 */
	@Override
	public int getTarget();

	/**
	 * Indicates when this agent runs.
	 * <p>
	 * This property corresponds to the Trigger options in the Runtime section of the Agent Properties box.
	 *
	 * This property determines the target possibilities. The TRIGGER_AFTER_MAIL_DELIVERY, TRIGGER_BEFORE_MAIL_DELIVERY, TRIGGER_DOC_PASTED,
	 * and TRIGGER_DOC_UPDATE triggers have only one target, which returns TARGET_NONE.
	 * </p>
	 *
	 * @return one For the "On event" Trigger option:
	 *         <ul>
	 *         <li>Agent.TRIGGER_AFTER_MAIL_DELIVERY ("After new mail has arrived")</li>
	 *         <li>Agent.TRIGGER_BEFORE_MAIL_DELIVERY ("Before new mail arrives")</li>
	 *         <li>Agent.TRIGGER_DOC_PASTED ("When documents are pasted")</li>
	 *         <li>Agent.TRIGGER_DOC_UPDATE ("After documents are created or modified")</li>
	 *         <li>Agent.TRIGGER_MANUAL ("Action menu selection," "Agent list selection")</li>
	 *         <li>Agent.TRIGGER_NONE (Not used)</li>
	 *         <li>Agent.TRIGGER_SERVERSTART ("When the Domino server starts")</li>
	 *         </ul>
	 *         For the "On schedule" Trigger option:
	 *         <ul>
	 *         <li>Agent.TRIGGER_SCHEDULED ("More than once a day," "Daily," "Weekly," "Monthly," or "Never")</li>
	 *         </ul>
	 */
	@Override
	public int getTrigger();

	/**
	 * Returns the Domino URL for its parent object (the database).
	 */
	@Override
	public String getURL();

	/**
	 * Indicates whether user activation is in effect when enabling or disabling a scheduled agent.
	 * <p>
	 * This property, intended for use with scheduled agents, always returns true for hidden agents and agents run from a menu.
	 *
	 * </p>
	 * <p>
	 * "Allow editor level user activation" on the Security tab regulates this property:
	 * <ul>
	 * <li>If checked, user activation is in effect. Editor access is required to enable or disable an agent, and the agent signature does
	 * not change.</li>
	 * <li>If not checked, Designer access is required to enable or disable the agent, and the signature changes to that of the user
	 * enabling or disabling the agent.
	 * <li>
	 * </ul>
	 * Note: User activation is new with Release 6. R5.0.7 and earlier releases do not recognize changes made with "Allow editor level user
	 * activation" checked. You can enable and disable agents with {@link #setEnabled(boolean)}.
	 * </p>
	 */
	@Override
	public boolean isActivatable();

	/**
	 * Indicates whether an agent is able to run.
	 * <p>
	 * This property is intended for use with scheduled agents, which can be enabled and disabled. This property always returns true for
	 * hidden agents and agents that are run from a menu.
	 * </p>
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * If the agent is open in the UI, a change is not immediately reflected. The agent must be closed and reopened.
	 * </p>
	 * <p>
	 * Access privileges and agent signing depend on whether user or designer activation is in effect. See {@link #isActivatable()}.
	 * </p>
	 *
	 * @return true when the agent is able to run
	 */
	@Override
	public boolean isEnabled();

	/**
	 * Indicates whether this agent can run in the Notes client environment.
	 */
	@Override
	public boolean isNotesAgent();

	/**
	 * Indicates whether an agent is updated when the application design is refreshed or replaced.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * You cannot change the current agent.
	 * </p>
	 *
	 */
	@Override
	public boolean isProhibitDesignUpdate();

	/**
	 * Indicates whether an agent is shared (public) or private.
	 * <ul>
	 * <li>A shared (public) agent is accessible to all users of a database and is stored in the database.</li>
	 * <li>A private agent is accessible only to its owner and is stored in the owner's desktop file.</li>
	 * </ul>
	 */
	@Override
	public boolean isPublic();

	/**
	 * Indicates whether an agent can run in a Web browser environment.
	 */
	@Override
	public boolean isWebAgent();

	/**
	 * Locks an agent for the effective user.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock();

	/**
	 * Locks an agent for the effective user.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the second parameter is false.</li>
	 *
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final boolean provisionalOk);

	/**
	 * Locks an agent.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final String name);

	/**
	 * Locks an agent.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the second parameter is false.</li>
	 *
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/**
	 * Locks an agent.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Throws an exception if the administration server is not available.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/**
	 * Locks an agent.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>Places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>Throws an exception if the administration server is not available and the second parameter is false.</li>
	 *
	 * </ul>
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @param provisionalOk
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk);

	/**
	 * Locks an agent provisionally for the effective user.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lockProvisional();

	/**
	 * Locks an agent provisionally.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lockProvisional(final String name);

	/**
	 * Locks an agent provisionally.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the agent is not locked, this method places the lock and returns true.</li>
	 * <li>If the agent is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the agent is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the agent is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/**
	 * Permanently deletes this agent from a database.
	 * <p>
	 * Once you call this method, the Agent object is null and you can no longer use its methods or properties.
	 * </p>
	 * <p>
	 * It is possible to use this method to delete the agent that's currently running. Use this feature with care. You must refresh the
	 * Agents view in the user interface to see that the agent has been removed.
	 * </p>
	 *
	 */
	@Override
	public void remove();

	/**
	 * Runs this agent. Events {@link org.openntf.domino.ext.Database.Events} of type BEFORE_RUN_AGENT and AFTER_RUN_AGENT are fired before
	 * and after the agent is run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>Null</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 * </p>
	 */
	@Override
	public void run();

	/**
	 * Runs this agent with given NoteID as a parameter. Events {@link org.openntf.domino.ext.Database.Events} of type BEFORE_RUN_AGENT and
	 * AFTER_RUN_AGENT are fired before and after the agent is run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>Note ID passed as a parameter to the run method</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 *
	 * </p>
	 *
	 * @param noteid
	 *            Note ID of a document available to the running agent as a {@link #getParameterDocID() ParameterDocID} property
	 */
	@Override
	public void run(final String noteid);

	/**
	 * Runs this agent on the computer containing the database. Events {@link org.openntf.domino.ext.Database.Events} of type
	 * BEFORE_RUN_AGENT and AFTER_RUN_AGENT are fired before and after the agent is run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>Name of the server where the agent runs</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 *
	 * </p>
	 *
	 * @param noteid
	 *            Note ID of a document available to the running agent as a {@link #getParameterDocID() ParameterDocID} property
	 *
	 * @return Status of the operation where 0 indicates success.
	 */
	@Override
	public int runOnServer();

	/**
	 * Runs this agent on the computer containing the database with given NoteID as a parameter. Events
	 * {@link org.openntf.domino.ext.Database.Events} of type BEFORE_RUN_AGENT and AFTER_RUN_AGENT are fired before and after the agent is
	 * run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>An array of type Object with two Strings where the first element of the array is the name of the server where the agent runs and
	 * the second element being the Note ID.</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 * </p>
	 * <p>
	 * <h5>Example of a listener:</h5>
	 *
	 * <pre>
	 * public class AgentListener implements IDominoListener {
	 *
	 * 	&#64;Override
	 * 	public boolean eventHappened(IDominoEvent event) {
	 * 		Agent agent = (Agent) event.getSource();
	 *
	 * 		Object[] payload = (Object[]) event.getPayload();
	 * 		String server = (String) payload[0];
	 * 		String noteID = (String) payload[1];
	 *
	 * 		//do something based on the agent and the noteID
	 * 		return true;
	 * 	}
	 *
	 * 	&#64;Override
	 * 	public List<EnumEvent> getEventTypes() {
	 * 		ArrayList<EnumEvent> events = new ArrayList<EnumEvent>();
	 * 		events.add(Events.AFTER_RUN_AGENT);
	 * 		return events;
	 * 	}
	 * }
	 * </pre>
	 *
	 * Add the listener with
	 *
	 * <pre>
	 * AgentListener listener = new AgentListener();
	 * database.addListener(listener);
	 * </pre>
	 * </p>
	 *
	 * @return Status of the operation where 0 indicates success.
	 */
	@Override
	public int runOnServer(final String noteid);

	/**
	 * Runs this agent with the given document. Events {@link org.openntf.domino.ext.Database.Events} of type BEFORE_RUN_AGENT and
	 * AFTER_RUN_AGENT are fired before and after the agent is run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>Document passed to the agent as a document context</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 *
	 * </p>
	 *
	 * @param doc
	 *            Document that will be available to the agent as a result of {@link org.openntf.domino.AgentContext#getDocumentContext()}
	 */
	@Override
	public void runWithDocumentContext(final lotus.domino.Document doc);

	/**
	 * Runs this agent with the given document. Events {@link org.openntf.domino.ext.Database.Events} of type BEFORE_RUN_AGENT and
	 * AFTER_RUN_AGENT are fired before and after the agent is run.
	 * <p>
	 * Events will contain this data:
	 * <dl>
	 * <dt>Source</dt>
	 * <dd>This agent</dd>
	 * <dt>Target</dt>
	 * <dd>Database where this agent is stored</dd>
	 * <dt>Payload</dt>
	 * <dd>An array of type Object with two elements where the first element of the array is the document passed to the agent and the second
	 * element being the Note ID.</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <h5>Note:</h5> If any of the listeners returns false in response to the BEFORE_RUN_AGENT event, the agent will NOT run and no other
	 * listeners are notified about the event.<br/>
	 * If any of the listeners returns false in response to the AFTER_RUN_AGENT event, no other listeners are notified about the event.
	 *
	 * </p>
	 *
	 * @param doc
	 *            Document that will be available to the agent as a result of {@link org.openntf.domino.AgentContext#getDocumentContext()}
	 * @param noteid
	 *            Note ID that will be available to the agent via {@link org.openntf.domino.Agent#getParameterDocID()}
	 */
	@Override
	public void runWithDocumentContext(final lotus.domino.Document doc, final String noteid);

	/**
	 * Saves changes made to the agent.
	 * <p>
	 * Saving the agent changes the owner immediately. However, if you subsequently get the owner within the same Session, the previous
	 * owner's name is returned. The ownership change is not reflected in the properties until the next time a Session is obtained.
	 * </p>
	 * <p>
	 * You must call <code>save</code> after {@link #setServerName(String)} and {@link #setEnabled(boolean)}, or the new value is lost.
	 * </p>
	 * <p>
	 * To save an agent from an agent that runs on a server, the executing agent must either: be signed by someone listed under "Run
	 * unrestricted methods and operations" in the saved agent's Server document in the DominoÂ® Directory; be signed by someone listed
	 * under "Sign agents to run on behalf of someone else"; or must have the same effective user as the saved agent. The effective user may
	 * be the signer of an agent, the user listed under "Run on behalf of" in the agent properties, or the user invoking the agent if the
	 * agent is run from the Web the "Run as web user" agent property is in effect.
	 * </p>
	 *
	 */
	@Override
	public void save();

	/**
	 * Sets whether an agent is able to run.
	 * <p>
	 * This property is intended for use with scheduled agents, which can be enabled and disabled.
	 * </p>
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * If the agent is open in the UI, a change is not immediately reflected. The agent must be closed and reopened.
	 * </p>
	 * <p>
	 * Access privileges and agent signing depend on whether user or designer activation is in effect. See {@link #isActivatable()}.
	 * </p>
	 *
	 * @param flag
	 *            true to allow the agent to run
	 */
	@Override
	public void setEnabled(final boolean flag);

	/**
	 * Sets whether an agent is updated when the application design is refreshed or replaced.
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 * <p>
	 * You cannot change the current agent.
	 * </p>
	 *
	 * @param true
	 *
	 */
	@Override
	public void setProhibitDesignUpdate(final boolean flag);

	/**
	 * The name of the server on which an agent runs.
	 *
	 * <p>
	 * You must call {@link ACL#save()} if you want the modified ACL to be saved to disk.
	 * </p>
	 *
	 * @param server
	 *            the new server name
	 * @see #getServerName()
	 */
	@Override
	public void setServerName(final String server);

	/**
	 * Unlocks this agent.
	 * <p>
	 * {@link Database#isDesignLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method throws an exception if the current user is not one of the lock holders and does not have lock breaking authority.
	 * </p>
	 *
	 */
	@Override
	public void unlock();

}
