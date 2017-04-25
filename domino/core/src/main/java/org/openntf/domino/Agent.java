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

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getComment()
	 */
	@Override
	public String getComment();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getCommonOwner()
	 */
	@Override
	public String getCommonOwner();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getLastRun()
	 */
	@Override
	public DateTime getLastRun();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getLockHolders()
	 */
	@Override
	public Vector<String> getLockHolders();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getNotesURL()
	 */
	@Override
	public String getNotesURL();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getOnBehalfOf()
	 */
	@Override
	public String getOnBehalfOf();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getOwner()
	 */
	@Override
	public String getOwner();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getParameterDocID()
	 */
	@Override
	public String getParameterDocID();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getParent()
	 */
	@Override
	public Database getParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getQuery()
	 */
	@Override
	public String getQuery();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getServerName()
	 */
	@Override
	public String getServerName();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getTarget()
	 */
	@Override
	public int getTarget();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getTrigger()
	 */
	@Override
	public int getTrigger();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isActivatable()
	 */
	@Override
	public boolean isActivatable();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isEnabled()
	 */
	@Override
	public boolean isEnabled();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isNotesAgent()
	 */
	@Override
	public boolean isNotesAgent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isProhibitDesignUpdate()
	 */
	@Override
	public boolean isProhibitDesignUpdate();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isPublic()
	 */
	@Override
	public boolean isPublic();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#isWebAgent()
	 */
	@Override
	public boolean isWebAgent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock()
	 */
	@Override
	public boolean lock();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lockProvisional()
	 */
	@Override
	public boolean lockProvisional();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#remove()
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
	 *
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

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#save()
	 */
	@Override
	public void save();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#setProhibitDesignUpdate(boolean)
	 */
	@Override
	public void setProhibitDesignUpdate(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#setServerName(java.lang.String)
	 */
	@Override
	public void setServerName(final String server);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#unlock()
	 */
	@Override
	public void unlock();

}
