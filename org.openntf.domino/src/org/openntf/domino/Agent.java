/*
 * Copyright OpenNTF 2013
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

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;

// TODO: Auto-generated Javadoc
/**
 * The Interface Agent.
 */
public interface Agent extends Base<lotus.domino.Agent>, lotus.domino.Agent, org.openntf.domino.ext.Agent, Design, DatabaseDescendant {

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
	public boolean lock(boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#lock(java.lang.String)
	 */
	@Override
	public boolean lock(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(String name, boolean provisionalOk);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#lock(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names, boolean provisionalOk);

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
	public boolean lockProvisional(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(Vector names);

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
	public void run(String noteid);

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
	public int runOnServer(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#runWithDocumentContext(lotus.domino.Document)
	 */
	@Override
	public void runWithDocumentContext(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#runWithDocumentContext(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void runWithDocumentContext(lotus.domino.Document doc, String noteid);

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
	public void setEnabled(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#setProhibitDesignUpdate(boolean)
	 */
	@Override
	public void setProhibitDesignUpdate(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#setServerName(java.lang.String)
	 */
	@Override
	public void setServerName(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Agent#unlock()
	 */
	@Override
	public void unlock();

}
