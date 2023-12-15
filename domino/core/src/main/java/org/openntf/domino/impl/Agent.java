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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.ext.Database.Events;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Strings;

import com.ibm.commons.util.StringUtil;

import lotus.domino.NotesException;

/**
 * The Class Agent.
 */
@SuppressWarnings("nls")
public class Agent extends BaseResurrectable<org.openntf.domino.Agent, lotus.domino.Agent, Database> implements org.openntf.domino.Agent {
	private static final Logger log_ = Logger.getLogger(Agent.class.getName());
	private String names_;

	/**
	 * Instantiates a new agent.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Agent(final lotus.domino.Agent delegate, final org.openntf.domino.Database parent) {
		super(delegate, parent, NOTES_MACRO);
		initialize(delegate);

	}

	protected void initialize(final lotus.domino.Agent delegate) {

		try {
			names_ = delegate.getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getComment()
	 */
	@Override
	public String getComment() {
		try {
			return getDelegate().getComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getCommonOwner()
	 */
	@Override
	public String getCommonOwner() {
		try {
			return getDelegate().getCommonOwner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public Document getDocument() {
		return parent.getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getLastRun()
	 */
	@Override
	public DateTime getLastRun() {
		try {
			return fromLotus(getDelegate().getLastRun(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getLockHolders()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		NoteCollection notes = parent.createNoteCollection(false);
		notes.add(this);
		return notes.getFirstNoteID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getOnBehalfOf()
	 */
	@Override
	public String getOnBehalfOf() {
		try {
			return getDelegate().getOnBehalfOf();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getOwner()
	 */
	@Override
	public String getOwner() {
		try {
			return getDelegate().getOwner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getParameterDocID()
	 */
	@Override
	public String getParameterDocID() {
		try {
			return getDelegate().getParameterDocID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Database getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getQuery()
	 */
	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getServerName()
	 */
	@Override
	public String getServerName() {
		try {
			return getDelegate().getServerName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getTarget()
	 */
	@Override
	public int getTarget() {
		try {
			return getDelegate().getTarget();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getTrigger()
	 */
	@Override
	public int getTrigger() {
		try {
			return getDelegate().getTrigger();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		NoteCollection notes = parent.createNoteCollection(false);
		notes.add(this);
		return notes.getUNID(notes.getFirstNoteID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isActivatable()
	 */
	@Override
	public boolean isActivatable() {
		try {
			return getDelegate().isActivatable();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		try {
			return getDelegate().isEnabled();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isNotesAgent()
	 */
	@Override
	public boolean isNotesAgent() {
		try {
			return getDelegate().isNotesAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isProhibitDesignUpdate()
	 */
	@Override
	public boolean isProhibitDesignUpdate() {
		try {
			return getDelegate().isProhibitDesignUpdate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isPublic()
	 */
	@Override
	public boolean isPublic() {
		try {
			return getDelegate().isPublic();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#isWebAgent()
	 */
	@Override
	public boolean isWebAgent() {
		try {
			return getDelegate().isWebAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock()
	 */
	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk) {
		try {
			return getDelegate().lock(provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name) {
		try {
			return getDelegate().lock(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk) {
		try {
			return getDelegate().lock(name, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names) {
		try {
			return getDelegate().lock(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk) {
		try {
			return getDelegate().lock(names, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lockProvisional()
	 */
	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name) {
		try {
			return getDelegate().lockProvisional(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names) {
		try {
			return getDelegate().lockProvisional(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#run()
	 */
	@Override
	public void run() {
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, null));
		if (go) {
			try {
				getDelegate().run();
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, null));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#run(java.lang.String)
	 */
	@Override
	public void run(final String noteid) {
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, noteid));
		if (go) {
			try {
				getDelegate().run(noteid);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, noteid));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#runOnServer()
	 */
	@Override
	public int runOnServer() {
		int result = -1;
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, getAncestorDatabase().getServer()));
		if (go) {
			try {
				result = getDelegate().runOnServer();
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, getAncestorDatabase().getServer()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#runOnServer(java.lang.String)
	 */
	@Override
	public int runOnServer(final String noteid) {
		int result = -1;
		boolean go = true;
		Object[] payload = new Object[2];
		payload[0] = getAncestorDatabase().getServer();
		payload[1] = noteid;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, payload));
		if (go) {
			try {
				result = getDelegate().runOnServer(noteid);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, payload));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#runWithDocumentContext(lotus.domino.Document)
	 */
	@Override
	public void runWithDocumentContext(final lotus.domino.Document doc) {
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, doc));
		if (go) {
			try {
				getDelegate().runWithDocumentContext(toLotus(doc));
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, doc));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#runWithDocumentContext(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void runWithDocumentContext(final lotus.domino.Document doc, final String noteid) {
		boolean go = true;
		Object[] payload = new Object[2];
		payload[0] = doc;
		payload[1] = noteid;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_RUN_AGENT, payload));
		if (go) {
			try {
				getDelegate().runWithDocumentContext(toLotus(doc), noteid);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_RUN_AGENT, payload));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#save()
	 */
	@Override
	public void save() {
		try {
			getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(final boolean flag) {
		try {
			getDelegate().setEnabled(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#setProhibitDesignUpdate(boolean)
	 */
	@Override
	public void setProhibitDesignUpdate(final boolean flag) {
		try {
			getDelegate().setProhibitDesignUpdate(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#setServerName(java.lang.String)
	 */
	@Override
	public void setServerName(final String server) {
		try {
			getDelegate().setServerName(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Agent#unlock()
	 */
	@Override
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public final Database getAncestorDatabase() {
		return parent;
	}

	@Override
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	@Override
	protected final WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	private IDominoEvent generateEvent(final EnumEvent event, final Object payload) {
		return getAncestorDatabase().generateEvent(event, this, payload);
	}

	@Override
	protected void resurrect() { // should only happen if the delegate has been destroyed somehow.
		try {
			lotus.domino.Agent agent = resurrectAgent();
			setDelegate(agent, true);
			/* No special logging, since by now View is a BaseThreadSafe */
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	//-------------- Externalize  stuff ------------------
	private static final int EXTERNALVERSIONUID = 20141205;

	/**
	 * @deprecated needed for {@link Externalizable} - do not use!
	 */
	@Deprecated
	public Agent() {
		super(NOTES_MACRO);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(EXTERNALVERSIONUID); // data version

		out.writeObject(names_);

	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);

		int version = in.readInt();
		if (version != EXTERNALVERSIONUID) {
			throw new InvalidClassException("Cannot read dataversion " + version);
		}

		names_ = (String) in.readObject();

	}

	private lotus.domino.Agent resurrectAgent() throws NotesException {
		lotus.domino.Database d = toLotus(getAncestorDatabase());
		String[] agNames = StringUtil.splitString(names_, '|');

		lotus.domino.Agent ret = d.getAgent(agNames[0]);
		if (ret == null) {
			throw new IllegalStateException("agent '" + agNames[0] + "' not found in " + getAncestorDatabase());
		}
		if (!names_.equals(ret.getName())) {
			// try aliases!
			lotus.domino.Agent candidate = null;
			for (String agName : agNames) {
				lotus.domino.Agent ag = d.getAgent(agName);
				if (candidate == null && ag != null && names_.equals(ag.getName())) {
					candidate = ag;
					break;
				} else if (!ret.equals(ag)) {
					// wrap every view, so that it gets recycled
					getFactory().fromLotus(ag, Agent.SCHEMA, getAncestorDatabase());
					// we MUST iterate to the end and not break!
				}
			}
			if (candidate != null) {
				log_.log(Level.WARNING, "The agent name '" + agNames[0] + "' is not unique in " + getAncestorDatabase() + ". View1: "
						+ candidate.getName() + ", View2:" + ret.getName());
				getFactory().fromLotus(ret, Agent.SCHEMA, getAncestorDatabase());
				ret = candidate;
			}

		}
		return ret;
	}

	protected Object readResolve() {
		// TODO: The Agent name may not be accurate enough, if the name is not unique
		try {
			return fromLotus(resurrectAgent(), Agent.SCHEMA, getAncestorDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return this;
		}
	}

	private transient String metaversalid_;

	@Override
	public String getMetaversalID() {
		if (metaversalid_ == null) {
			metaversalid_ = getAncestorDatabase().getReplicaID() + getUniversalID();
		}
		return metaversalid_;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Agent#isProfiled()
	 */
	@Override
	public boolean isProfiled() {
		String flags = getDocument().getItemValueString("$FlagsExt");
		return flags.contains("F");
	}

	@Override
	public org.openntf.domino.Document getProfileResults() {
		try {
			if (isProfiled()) {
				NoteCollection nc = getParent().createNoteCollection(false);
				String subject = " Profile";
				if (getName().contains("|")) {
					subject = Strings.left(getName(), "|") + subject;
				} else {
					if (getName().contains("(")) {
						subject = Strings.right(Strings.left(getName(), ")"), "(") + subject;
					} else {
						subject = getName() + subject;
					}
				}
				nc.setSelectProfiles(true);
				nc.setSelectionFormula("Form=\"$BEProfileR7\" & Subject=\"" + subject + "\"");
				nc.buildCollection();
				if (!Strings.isBlankString(nc.getFirstNoteID())) {
					return getParent().getDocumentByID(nc.getFirstNoteID());
				}
			}
			return null;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Agent#getTargetEx()
	 */
	@Override
	public Target getTargetEx() {
		return Target.valueOf(getTarget());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Agent#getTriggerEx()
	 */
	@Override
	public Trigger getTriggerEx() {
		return Trigger.valueOf(getTrigger());
	}

	@Override
	public Date getLastRunDate() {
		if (null == getLastRun()) {
			return null;
		} else {
			return getLastRun().toJavaDate();
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Agent#getActualName()
	 */
	@Override
	public String getActualName() {
		String fullName = getParent().getDocumentByID(getNoteID()).getItemValueString("$TITLE");
		if (fullName.contains("|")) {
			return Strings.left(fullName, "|");
		} else {
			return fullName;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Agent#getAlias()
	 */
	@Override
	public String getAlias() {
		String fullName = getName();	// getName() is inconsistent for actual name, but fine for alias
		if (fullName.contains("|")) {
			return Strings.right(fullName, "|");
		} else {
			return "";
		}
	}

}
