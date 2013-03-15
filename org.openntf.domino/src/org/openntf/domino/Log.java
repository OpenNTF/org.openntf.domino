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
 * The Interface Log.
 */
public interface Log extends Base<lotus.domino.Log>, lotus.domino.Log {

	/* (non-Javadoc)
	 * @see lotus.domino.Log#close()
	 */
	@Override
	public void close();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#getNumActions()
	 */
	@Override
	public int getNumActions();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#getNumErrors()
	 */
	@Override
	public int getNumErrors();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#getParent()
	 */
	@Override
	public Session getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#getProgramName()
	 */
	@Override
	public String getProgramName();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#isLogActions()
	 */
	@Override
	public boolean isLogActions();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#isLogErrors()
	 */
	@Override
	public boolean isLogErrors();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#isOverwriteFile()
	 */
	@Override
	public boolean isOverwriteFile();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#logAction(java.lang.String)
	 */
	@Override
	public void logAction(String action);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#logError(int, java.lang.String)
	 */
	@Override
	public void logError(int code, String text);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#logEvent(java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public void logEvent(String text, String queue, int event, int severity);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#openAgentLog()
	 */
	@Override
	public void openAgentLog();

	/* (non-Javadoc)
	 * @see lotus.domino.Log#openFileLog(java.lang.String)
	 */
	@Override
	public void openFileLog(String filePath);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#openMailLog(java.util.Vector, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void openMailLog(Vector recipients, String subject);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#openNotesLog(java.lang.String, java.lang.String)
	 */
	@Override
	public void openNotesLog(String server, String database);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#setLogActions(boolean)
	 */
	@Override
	public void setLogActions(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#setLogErrors(boolean)
	 */
	@Override
	public void setLogErrors(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#setOverwriteFile(boolean)
	 */
	@Override
	public void setOverwriteFile(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Log#setProgramName(java.lang.String)
	 */
	@Override
	public void setProgramName(String name);

}
