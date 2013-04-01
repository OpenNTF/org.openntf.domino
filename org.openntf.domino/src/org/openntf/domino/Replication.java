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

// TODO: Auto-generated Javadoc
/**
 * The Interface Replication.
 */
public interface Replication extends Base<lotus.domino.Replication>, lotus.domino.Replication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#clearHistory()
	 */
	@Override
	public int clearHistory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getCutoffDate()
	 */
	@Override
	public DateTime getCutoffDate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getCutoffInterval()
	 */
	@Override
	public long getCutoffInterval();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getDontSendLocalSecurityUpdates()
	 */
	@Override
	public boolean getDontSendLocalSecurityUpdates();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getEntries()
	 */
	@Override
	public Vector<ReplicationEntry> getEntries();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getEntry(java.lang.String, java.lang.String)
	 */
	@Override
	public ReplicationEntry getEntry(String source, String destination);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getEntry(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ReplicationEntry getEntry(String source, String destination, boolean createFlag);

	public Database getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#getPriority()
	 */
	@Override
	public int getPriority();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#isAbstract()
	 */
	@Override
	public boolean isAbstract();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#isCutoffDelete()
	 */
	@Override
	public boolean isCutoffDelete();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#isDisabled()
	 */
	@Override
	public boolean isDisabled();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#isIgnoreDeletes()
	 */
	@Override
	public boolean isIgnoreDeletes();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#isIgnoreDestDeletes()
	 */
	@Override
	public boolean isIgnoreDestDeletes();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#reset()
	 */
	@Override
	public int reset();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#save()
	 */
	@Override
	public int save();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setAbstract(boolean)
	 */
	@Override
	public void setAbstract(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setCutoffDelete(boolean)
	 */
	@Override
	public void setCutoffDelete(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setCutoffInterval(long)
	 */
	@Override
	public void setCutoffInterval(long interval);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setDisabled(boolean)
	 */
	@Override
	public void setDisabled(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setDontSendLocalSecurityUpdates(boolean)
	 */
	@Override
	public void setDontSendLocalSecurityUpdates(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setIgnoreDeletes(boolean)
	 */
	@Override
	public void setIgnoreDeletes(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setIgnoreDestDeletes(boolean)
	 */
	@Override
	public void setIgnoreDestDeletes(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Replication#setPriority(int)
	 */
	@Override
	public void setPriority(int priority);

}
