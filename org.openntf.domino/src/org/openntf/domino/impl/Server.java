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
package org.openntf.domino.impl;

import java.util.Collection;
import java.util.List;

import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;
import lotus.notes.addins.ServerAccess;
import lotus.notes.addins.ServerInfo;

import org.openntf.domino.Database;
import org.openntf.domino.utils.DominoUtils;

public class Server implements org.openntf.domino.Server {

	private DominoServer server_;

	public Server(String serverName) {
		try {
			server_ = new DominoServer(serverName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#checkServerAccess(java.lang.String, java.util.Collection)
	 */
	@Override
	public boolean checkServerAccess(String userName, Collection<ServerAccess> serverAccessLevel) {
		try {
			return server_.checkServerAccess(userName, serverAccessLevel);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#checkServerAccess(java.lang.String, lotus.notes.addins.ServerAccess)
	 */
	@Override
	public boolean checkServerAccess(String userName, ServerAccess serverAccessLevel) {
		try {
			return server_.checkServerAccess(userName, serverAccessLevel);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getAbbreviatedName()
	 */
	@Override
	public String getAbbreviatedName() {
		return server_.getAbbreviatedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getCanonicalName()
	 */
	@Override
	public String getCanonicalName() {
		return server_.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getClusterMembers(boolean)
	 */
	@Override
	public Collection<org.openntf.domino.Server> getClusterMembers(boolean includeCurrentServer) {
		// TODO: Add method to read all cluster members
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getClusterName()
	 */
	@Override
	public String getClusterName() {
		try {
			return server_.getCluster();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getDatabaseByFilePath(java.lang.String)
	 */
	@Override
	public Database getDatabaseByFilePath(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getDatabaseByFilePath(java.lang.String, boolean)
	 */
	@Override
	public Database getDatabaseByFilePath(String filePath, boolean failOver) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getDatabaseByReplicaId(java.lang.String)
	 */
	@Override
	public Database getDatabaseByReplicaId(String replicaId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getDatabaseByReplicaId(java.lang.String, boolean)
	 */
	@Override
	public Database getDatabaseByReplicaId(String replicaId, boolean failOver) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getDomain()
	 */
	@Override
	public String getDomain() {
		try {
			return server_.getDomain();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getPlatform()
	 */
	@Override
	public String getPlatform() {
		try {
			return server_.getPlatform();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getServerDocField(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getServerDocField(String fieldName) {
		try {
			return server_.lookupServerRecord(fieldName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getServerDocField(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getServerDocField(String fieldName, String unknown) {
		try {
			return server_.lookupServerRecord(fieldName, unknown);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getServerInfo(lotus.notes.addins.ServerInfo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getServerInfo(ServerInfo serverInfo) {
		try {
			return server_.getInfo(serverInfo);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getTitle()
	 */
	@Override
	public String getTitle() {
		try {
			return server_.getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getUserNamesList(java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getUserNamesList(Collection<String> userNames) {
		try {
			return server_.getNamesList(userNames);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getUserNamesList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getUserNamesList(String userName) {
		try {
			return server_.getNamesList(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#isCluster()
	 */
	@Override
	public boolean isCluster() {
		if (getClusterName() != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#isLocal()
	 */
	@Override
	public boolean isLocal() {
		return server_.isLocal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#isSocial()
	 */
	@Override
	public boolean isSocial() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesign(org.openntf.domino.Database)
	 */
	@Override
	public void refreshDesign(Database db) {
		try {
			server_.refreshDesign(db.getFilePath());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesign(org.openntf.domino.Database, boolean)
	 */
	@Override
	public void refreshDesign(Database db, boolean refresh) {
		try {
			server_.refreshDesign(db.getFilePath(), refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesign(java.lang.String)
	 */
	@Override
	public void refreshDesign(String dbFilePath) {
		try {
			server_.refreshDesign(dbFilePath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesign(java.lang.String, boolean)
	 */
	@Override
	public void refreshDesign(String dbFilePath, boolean refresh) {
		try {
			server_.refreshDesign(dbFilePath, refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromLocal(org.openntf.domino.Database)
	 */
	@Override
	public void refreshDesignFromLocal(Database db) {
		try {
			server_.refreshDesignFromLocal(db.getFilePath());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromLocal(org.openntf.domino.Database, boolean)
	 */
	@Override
	public void refreshDesignFromLocal(Database db, boolean refresh) {
		try {
			server_.refreshDesignFromLocal(db.getFilePath(), refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromLocal(java.lang.String)
	 */
	@Override
	public void refreshDesignFromLocal(String dbFilePath) {
		try {
			server_.refreshDesignFromLocal(dbFilePath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromLocal(java.lang.String, boolean)
	 */
	@Override
	public void refreshDesignFromLocal(String dbFilePath, boolean refresh) {
		try {
			server_.refreshDesignFromLocal(dbFilePath, refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromServer(org.openntf.domino.Database, java.lang.String)
	 */
	@Override
	public void refreshDesignFromServer(Database db, String serverName) {
		try {
			server_.refreshDesignFromServer(db.getFilePath(), serverName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromServer(org.openntf.domino.Database, java.lang.String, boolean)
	 */
	@Override
	public void refreshDesignFromServer(Database db, String serverName, boolean refresh) {
		try {
			server_.refreshDesignFromServer(db.getFilePath(), serverName, refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromServer(java.lang.String, java.lang.String)
	 */
	@Override
	public void refreshDesignFromServer(String dbFilePath, String serverName) {
		try {
			server_.refreshDesignFromServer(dbFilePath, serverName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#refreshDesignFromServer(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void refreshDesignFromServer(String dbFilePath, String serverName, boolean refresh) {
		try {
			server_.refreshDesignFromServer(dbFilePath, serverName, refresh);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Server#sendConsoleCommand(java.lang.String)
	 */
	@Override
	public String sendConsoleCommand(String command) {
		try {
			return server_.sendConsoleCommand(command);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
