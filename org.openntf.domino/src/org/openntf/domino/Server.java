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

import java.util.Collection;
import java.util.List;

import lotus.notes.addins.ServerAccess;
import lotus.notes.addins.ServerInfo;

/**
 * The Interface Server represents a selected Domino server.
 */
public interface Server {

	// TODO: ServerAccess import and definition
	/**
	 * Evaluates if the given user has the specified access level.
	 * 
	 * @param userName
	 *            The name of the user which should be checked.
	 * @param serverAccessLevel
	 *            A collection of ServerAccess settings.
	 * @return A boolean which indicates if the given user has the given access level.
	 */
	public boolean checkServerAccess(String userName, Collection<ServerAccess> serverAccessLevel);

	// TODO: ServerAccess import and definition
	/**
	 * Evaluates if the given user has the specified access level.
	 * 
	 * @param userName
	 *            The name of the user which should be checked.
	 * @param serverAccessLevel
	 *            A single ServerAccess setting
	 * @return A boolean which indicates if the given user has the given access level.
	 */
	public boolean checkServerAccess(String userName, ServerAccess serverAccessLevel);

	/**
	 * Returns the abbreviated name of the Domino server.
	 * 
	 * @return The abbreviated name of the Domino server.
	 */
	public String getAbbreviatedName();

	/**
	 * Returns the canonical name of the Domino server.
	 * 
	 * @return The canonical name of the Domino server.
	 */
	public String getCanonicalName();

	/**
	 * Checks if the current Domino server is member of a cluster and returns it's members.
	 * 
	 * @param includeCurrentServer
	 *            Defines if the current server should be included in the {@link java.util.Collection}.
	 * 
	 * @return A {@link java.util.Collection} of {@link org.openntf.domino.Server} for all cluster members. Return null if the server is not
	 *         in a cluster.
	 */
	public Collection<Server> getClusterMembers(boolean includeCurrentServer);

	/**
	 * Gets the name of the Domino servers cluster.
	 * 
	 * @return The name of the cluster. Returns null if the server is not in a cluster.
	 */
	public String getClusterName();

	/**
	 * Gets a database on the current Domino server specified by the databases file path.
	 * 
	 * @param filePath
	 *            The file path of the {@link org.openntf.domino.Database}
	 * @return A {@link org.openntf.domino.Database} for the given path. Returns null is the database isn't available.
	 */
	public Database getDatabaseByFilePath(String filePath);

	/**
	 * Gets a database on the current Domino server specified by the databases file path in failover mode.
	 * 
	 * @param filePath
	 *            The file path of the {@link org.openntf.domino.Database}
	 * @param failOver
	 *            Defines if the database should be opened from a cluster member if it's not available on the current server.
	 * 
	 * @return A {@link org.openntf.domino.Database} for the given path. Returns null is the database isn't available.
	 */
	public Database getDatabaseByFilePath(String filePath, boolean failOver);

	/**
	 * Gets a database on the current Domino server specified by the databases replica id.
	 * 
	 * @param replicaId
	 *            The replica id of the {@link org.openntf.domino.Database}
	 * @return A {@link org.openntf.domino.Database} for the given replica id. Returns null is the database isn't available.
	 */
	public Database getDatabaseByReplicaId(String replicaId);

	/**
	 * Gets a database on the current Domino server specified by the databases file path in failover mode.
	 * 
	 * @param replicaId
	 *            The replica id of the {@link org.openntf.domino.Database}
	 * @param failOver
	 *            Defines if the database should be opened from a cluster member if it's not available on the current server.
	 * 
	 * @return A {@link org.openntf.domino.Database} for the given path. Returns null is the database isn't available.
	 */
	public Database getDatabaseByReplicaId(String replicaId, boolean failOver);

	/**
	 * Gets the domain of the current Domino server.
	 * 
	 * @return The name of the domain as specified in the server document.
	 */
	public String getDomain();

	/**
	 * Gets the platform of the current Domino server.
	 * 
	 * @return The name of the platform as specified in the server document.
	 */
	public String getPlatform();

	/**
	 * Gets the name of any field in the server document.
	 * 
	 * @param fieldName
	 *            The field name which needs to be read.
	 * @return A {@link java.util.List} of all field values.
	 */
	public List<String> getServerDocField(String fieldName);

	// TODO: Check second parameter
	/**
	 * Gets the name of any field in the server document.
	 * 
	 * @param fieldName
	 *            The field name which needs to be read.
	 * @param unknown
	 *            TBD
	 * @return A {@link java.util.List} of all field values.
	 */
	public List<String> getServerDocField(String fieldName, String unknown);

	// TODO: Check server info
	/**
	 * TBD
	 * 
	 * @param serverInfo
	 *            TBD
	 * @param unknown
	 *            TBD
	 * @return TBD
	 */
	public List<String> getServerInfo(ServerInfo serverInfo);

	/**
	 * Gets the title of the current Domino server.
	 * 
	 * @return The title of the current Domino server.
	 */
	public String getTitle();

	/**
	 * Gets all names for the passed in usernames.
	 * 
	 * @param userNames
	 *            A collection of usernames. Could be any name (even if they don't exist in the Domino Directory).
	 * @return A {@link java.util.Collection} of groups and hierarchies of which the users are members of.
	 */
	public Collection<String> getUserNamesList(Collection<String> userNames);

	/**
	 * Gets all names for the passed in username.
	 * 
	 * @param userName
	 *            A username. Could be any name (even if they don't exist in the Domino Directory).
	 * @return A {@link java.util.Collection} of groups and hierarchies of which the user is a member of.
	 */
	public Collection<String> getUserNamesList(String userName);

	/**
	 * Gets the version of the current Domino server.
	 * 
	 * @return The version of the Domino server.
	 */
	public String getVersion();

	/**
	 * Checks if the current Domino server is a member of a cluster.
	 * 
	 * @return True if the Domino server is a member of a cluster, otherwise returns false.
	 */
	public boolean isCluster();

	/**
	 * Checks if the current Domino server is a Notes client or a real Domino server.
	 * 
	 * @return True if the current server is a Notes client, otherwise returns false.
	 */
	public boolean isLocal();

	/**
	 * Checks if the current Domino server runs the Social Edition (SE) addons.
	 * 
	 * @return True if the Social Edition components are installed, otherwise returns false.
	 */
	public boolean isSocial();

	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database}
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 */
	public void refreshDesign(Database db);

	// TODO: Check second parameter.
	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database}
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesign(Database db, boolean refresh);

	/**
	 * Refreshes the design of the {@link org.openntf.domino.Database} on the given file path.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 */
	public void refreshDesign(String dbFilePath);

	// TODO: Check second parameter.
	/**
	 * Refreshes the design of the {@link org.openntf.domino.Database} on the given file path.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesign(String dbFilePath, boolean refresh);

	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} from a local template.
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 */
	public void refreshDesignFromLocal(Database db);

	// TODO: Check second parameter.
	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} from a local template.
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesignFromLocal(Database db, boolean refresh);

	/**
	 * Refreshes the design of the {@link org.openntf.domino.Database} on the given file path from a local template.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 */
	public void refreshDesignFromLocal(String dbFilePath);

	// TODO: Check second parameter.
	/**
	 * Refreshes the design of the {@link org.openntf.domino.Database} on the given file path from a local template.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesignFromLocal(String dbFilePath, boolean refresh);

	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} from a server template.
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param serverName
	 *            The name of the template server
	 */
	public void refreshDesignFromServer(Database db, String serverName);

	// TODO: Check third parameter.
	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} from a server template.
	 * 
	 * @param db
	 *            The {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param serverName
	 *            The name of the template server
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesignFromServer(Database db, String serverName, boolean refresh);

	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} on the given file path from a server template.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param serverName
	 *            The name of the template server
	 */
	public void refreshDesignFromServer(String dbFilePath, String serverName);

	// TODO: Check third parameter.
	/**
	 * Refreshes the design of the given {@link org.openntf.domino.Database} on the given file path from a server template.
	 * 
	 * @param dbFilePath
	 *            The file path {@link org.openntf.domino.Database} which's design should be refreshed.
	 * @param serverName
	 *            The name of the template server
	 * @param refresh
	 *            True if the database refresh should enforced, otherwise false.
	 */
	public void refreshDesignFromServer(String dbFilePath, String serverName, boolean refresh);

	/**
	 * Sends a console command to the current Domino server.
	 * 
	 * @param command
	 *            The server command which should be issued.
	 * @return The server's response after issuing the command.
	 */
	public String sendConsoleCommand(String command);

}
