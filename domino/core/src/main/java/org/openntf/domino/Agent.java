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
import org.openntf.domino.utils.enums.DominoEnumUtil;
import org.openntf.domino.utils.enums.INumberEnum;

/**
 * The Interface Agent.
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

	public static enum Trigger implements INumberEnum<Integer> {
		AFTER_MAIL(Agent.TRIGGER_AFTER_MAIL_DELIVERY), BEFORE_MAIL(Agent.TRIGGER_BEFORE_MAIL_DELIVERY),
		DOC_PASTED(Agent.TRIGGER_DOC_PASTED), DOC_UPDATE(Agent.TRIGGER_DOC_UPDATE), MANUAL(Agent.TRIGGER_MANUAL), NONE(Agent.TRIGGER_NONE),
		SCHEDULED(Agent.TRIGGER_SCHEDULED), STARTUP(Agent.TRIGGER_SERVERSTART);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 *
		 * @param value
		 *            the value
		 */
		private Trigger(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		@Override
		public Integer getValue() {
			return value_;
		}

		public static Trigger valueOf(final int value) {
			return DominoEnumUtil.valueOf(Trigger.class, value);
		}
	}

	public static enum Target implements INumberEnum<Integer> {
		ALL(Agent.TARGET_ALL_DOCS), ALL_IN_VIEW(Agent.TARGET_ALL_DOCS_IN_VIEW), NEW(Agent.TARGET_NEW_DOCS),
		NEW_OR_MODIFIED(Agent.TARGET_NEW_OR_MODIFIED_DOCS), NONE(Agent.TARGET_NONE), SELECTED(Agent.TARGET_SELECTED_DOCS),
		UNREAD(Agent.TARGET_UNREAD_DOCS_IN_VIEW), RUN_ONCE(Agent.TARGET_RUN_ONCE);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 *
		 * @param value
		 *            the value
		 */
		private Target(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		@Override
		public Integer getValue() {
			return value_;
		}

		public static Target valueOf(final int value) {
			return DominoEnumUtil.valueOf(Target.class, value);
		}
	}

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

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#run()
	 */
	@Override
	public void run();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#run(java.lang.String)
	 */
	@Override
	public void run(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#runOnServer()
	 */
	@Override
	public int runOnServer();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#runOnServer(java.lang.String)
	 */
	@Override
	public int runOnServer(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#runWithDocumentContext(lotus.domino.Document)
	 */
	@Override
	public void runWithDocumentContext(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Agent#runWithDocumentContext(lotus.domino.Document, java.lang.String)
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
