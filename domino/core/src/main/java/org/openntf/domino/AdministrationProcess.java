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

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the administration process.
 * <h3>Creation and access</h3>
 * <p>
 * To create an <code>AdministrationProcess</code> object, call {@link Session#createAdministrationProcess(String)}.
 * </p>
 *
 * <p>
 * All <code>AdministrationProcess</code> methods require unrestricted server access.
 * </p>
 *
 * <h3>Usage</h3>
 * <p>
 * The administration process is a program that automates routine administrative tasks. For more information, see "The Administration
 * Process" in Administration Help.
 * </p>
 */
public interface AdministrationProcess extends Base<lotus.domino.AdministrationProcess>, lotus.domino.AdministrationProcess,
		org.openntf.domino.ext.AdministrationProcess, SessionDescendant {

	public static class Schema extends FactorySchema<AdministrationProcess, lotus.domino.AdministrationProcess, Session> {
		@Override
		public Class<AdministrationProcess> typeClass() {
			return AdministrationProcess.class;
		}

		@Override
		public Class<lotus.domino.AdministrationProcess> delegateClass() {
			return lotus.domino.AdministrationProcess.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enters a request in the Administration Requests database to add members to a new or existing a group.
	 * <p>
	 * This method enters the following administration request: Add or Modify Group in Domino Directory.
	 * </p>
	 * <p>
	 * If the group exists, the members are added to that group.
	 * </p>
	 * <p>
	 * If the group does not exist, a new multi-purpose group is created containing the members.
	 * </p>
	 *
	 * @param group
	 *            The name of the group.
	 * @param members
	 *            The names of the members in the group.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String addGroupMembers(final String group, final Vector members);

	/**
	 * Enters a request in the Administration Requests database to add an Internet certificate to a user ID.
	 * <p>
	 * The administration process is a program that automates routine administrative tasks. For more information, see "Add Internet
	 * Certificate to Person Record" in Administration Help.
	 * </p>
	 * <p>
	 * If this method is used with the certifier ID process, it requires an Internet certificate in a key ring file. See "Creating a server
	 * key ring file" in Administration Help. If this method is used with the CA process, no server key ring file is needed, the
	 * <code>keyringname$</code> and <code>keyringpassword$</code> parameters should be called with null text values "", and the
	 * {@link #setUseCertificateAuthority(boolean) UseCertificateAuthority} property must be set to True prior to calling the method.
	 * </p>
	 *
	 * @param user
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param keyringFile
	 *            The pathname of the key ring file containing the Internet certificate.
	 * @param keyringPassword
	 *            The password of the key ring file.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword);

	/**
	 * Enters a request in the Administration Requests database to add an Internet certificate to a user ID.
	 * <p>
	 * The administration process is a program that automates routine administrative tasks. For more information, see "Add Internet
	 * Certificate to Person Record" in Administration Help.
	 * </p>
	 * <p>
	 * If this method is used with the certifier ID process, it requires an Internet certificate in a key ring file. See "Creating a server
	 * key ring file" in Administration Help. If this method is used with the CA process, no server key ring file is needed, the
	 * <code>keyringname$</code> and <code>keyringpassword$</code> parameters should be called with null text values "", and the
	 * {@link #setUseCertificateAuthority(boolean) UseCertificateAuthority} property must be set to True prior to calling the method.
	 * </p>
	 *
	 * @param user
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param keyringFile
	 *            The pathname of the key ring file containing the Internet certificate.
	 * @param keyringPassword
	 *            The password of the key ring file.
	 * @param expiration
	 *            The expiration date for the certificate.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword,
			final lotus.domino.DateTime expiration);

	/**
	 * Enters a request in the Administration Requests database to add a server to a cluster.
	 * <p>
	 * If the cluster does not exist, a new cluster is created and the server added to it.
	 * </p>
	 *
	 * @param server
	 *            The full hierarchical name (can be abbreviated) of the server.
	 * @param cluster
	 *            The name of the cluster.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String addServerToCluster(final String server, final String cluster);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveDeletePersonInDirectory(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveDeleteServerInDirectory(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveDesignElementDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.null
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveHostedOrgStorageDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveMailFileDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveMovedReplicaDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveNameChangeRetraction(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveRenamePersonInDirectory(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveRenameServerInDirectory(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveReplicaDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to continue a previously initiated request that is pending approval.
	 *
	 * @param noteid
	 *            The note ID of the entry pending approval.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String approveResourceDeletion(final String noteid);

	/**
	 * Enters a request in the Administration Requests database to change a user's Internet password.
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param oldPassword
	 *            The existing password.
	 * @param newPassword
	 *            The new password.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String changeHTTPPassword(final String userName, final String oldPassword, final String newPassword);

	/**
	 * Enters a request in the Administration Requests database to configure an agent in a mail database on a user's home server.
	 * <p>
	 * This method specifies <code>username</code> as the value of "Run on behalf of" under the Security tab in the Agent Properties box.
	 * This is useful to allow scheduled agents on a mail database to run under the authority of the mail user. If activatable is true,
	 * users with Editor access can enable and disable the agent without resigning it.
	 * </p>
	 * <p>
	 * After the agent is configured, you can use the {@link Agent} class to reset activatable and enable.
	 * </p>
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param agentName
	 *            The name of the agent. The agent will be enabled.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName);

	/**
	 * Enters a request in the Administration Requests database to configure an agent in a mail database on a user's home server.
	 * <p>
	 * This method specifies username as the value of "Run on behalf of" under the Security tab in the Agent Properties box. This is useful
	 * to allow scheduled agents on a mail database to run under the authority of the mail user. If activatable is true, users with Editor
	 * access can enable and disable the agent without resigning it.
	 * </p>
	 * <p>
	 * After the agent is configured, you can use the {@link Agent} class</a> to reset activatable and enable.
	 * </p>
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param agentName
	 *            The name of the agent.
	 * @param activatable
	 *            true (default) to make the agent activatable false to make the agent not activatable
	 * @param enable
	 *            true to enable the agent, false to disable the agent
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName, final boolean activatable, final boolean enable);

	/**
	 * Enters a request in the Administration Requests database to create a replica. The ACL of the database will be copied, the full-text
	 * index will not be created.
	 * <p>
	 * This method triggers "Check access" and "Create replica" administration process requests.
	 * </p>
	 *
	 * @param sourceServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the database being replicated. An empty string
	 *            ("") means the local server.
	 * @param sourceDBFile
	 *            The pathname of the database being replicated relative to the data directory.
	 * @param destServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the replica. An empty string ("") means the local
	 *            server.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer);

	/**
	 * Enters a request in the Administration Requests database to create a replica.
	 * <p>
	 * This method triggers "Check access" and "Create replica" administration process requests.
	 * </p>
	 *
	 * @param sourceServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the database being replicated. An empty string
	 *            ("") means the local server.
	 * @param sourceDBFile
	 *            The pathname of the database being replicated relative to the data directory.
	 * @param destServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the replica. An empty string ("") means the local
	 *            server.
	 * @param destDBFile
	 *            The pathname of the new database relative to the data directory. Defaults to the same pathname as the source file.
	 * @param copyACL
	 *            true to copy the ACL, false to not copy the ACL
	 * @param createFTIndex
	 *            true to creates a full-text index, false to not create a full-text index
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer, final String destDBFile,
			final boolean copyACL, final boolean createFTIndex);

	/**
	 * Enters a request in the Administration Requests database to delete a group.
	 * <p>
	 * This method triggers "Delete group in Domino Directory," "Delete in Person documents," "Delete in Access Control List," and "Delete
	 * in Reader / Author fields" administration process requests.
	 * </p>
	 * <p>
	 * The corresponding Windows group will not be deleted.
	 * </p>
	 *
	 * @param groupName
	 *            The name of the group.
	 * @param immediate
	 *            true to delete all references to the group in the Domino Directory before issuing an administration process request, false
	 *            to let the administration process make all deletions. Note: A true setting may impact performance.
	 * @return
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate);

	/**
	 * Enters a request in the Administration Requests database to delete a group.
	 * <p>
	 * This method triggers "Delete group in Domino Directory," "Delete in Person documents," "Delete in Access Control List," and "Delete
	 * in Reader / Author fields" administration process requests.
	 * </p>
	 *
	 * @param groupName
	 *            The name of the group.
	 * @param immediate
	 *            true to delete all references to the group in the Domino Directory before issuing an administration process request, false
	 *            to let the administration process make all deletions. Note: A true setting may impact performance.
	 * @param deleteWindowsGroup
	 *            true to delete the corresponding Windows group, false to not delete the corresponding Windows group
	 * @return
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate, final boolean deleteWindowsGroup);

	/**
	 * Enters a request in the Administration Requests database to delete all replicas of a database.
	 * <p>
	 * This method triggers "Get replica information for deletion" and "Delete replica" administration process requests.
	 * </p>
	 *
	 * @param serverName
	 *            The name of a server containing one of the replicas.
	 * @param fileName
	 *            The name of the database.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String deleteReplicas(final String serverName, final String fileName);

	/**
	 * Enters a request in the Administration Requests database to delete a server.
	 * <p>
	 * This method triggers "Delete server in Domino Directory," "Delete in Person documents," "Delete in Access Control List," and "Delete
	 * in Reader / Author fields" administration process requests.
	 * </p>
	 *
	 * @param serverName
	 *            The name of the server.
	 * @param immediate
	 *            true to delete all references to the server in the Domino Directory before issuing an administration process request,
	 *            false to let the administration process make all deletions. Note: A true setting may impact performance.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String deleteServer(final String serverName, final boolean immediate);

	/**
	 * Enters a request in the Administration Requests database to delete a user.
	 * <p>
	 * This method triggers "Delete person in Domino Directory," "Delete in Person documents," "Delete in Access Control List," "Delete in
	 * Reader / Author fields," "Get information for deletion," "Approve file deletion," "Request file deletion," "Delete mail file,"
	 * "Delete unlinked mail file," "Approve deletion of Private Design Elements," "Request to delete Private Design Elements," and "Delete
	 * Private Design Elements" administration process requests.
	 * </p>
	 * <p>
	 * The corresponding Windows user will not be deleted.
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param immediate
	 *            true to delete all references to the user in the Domino Directory before issuing an administration process request, false
	 *            to let the administration process make all deletions. Note: A true setting may impact performance.
	 * @param mailFileAction
	 *            Indicates the disposition of the user's mail file:
	 *            <ul>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_ALL deletes the mail file on the user's home server and all replicas.</li>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_HOME deletes the mail file on the user's home server.</li>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_NONE leaves the user's mail file.</li>
	 *            </ul>
	 * @param denyGroup
	 *            The name of an existing group of type "Deny List Only" to which the name of the deleted user is added. The empty string
	 *            means do not add the user name to any group.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String deleteUser(final String userName, final boolean immediate, final int mailFileAction, final String denyGroup);

	/**
	 * Enters a request in the Administration Requests database to delete a user.
	 * <p>
	 * This method triggers "Delete person in Domino Directory," "Delete in Person documents," "Delete in Access Control List," "Delete in
	 * Reader / Author fields," "Get information for deletion," "Approve file deletion," "Request file deletion," "Delete mail file,"
	 * "Delete unlinked mail file," "Approve deletion of Private Design Elements," "Request to delete Private Design Elements," and "Delete
	 * Private Design Elements" administration process requests.
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param immediate
	 *            true to delete all references to the user in the DominoÂ® Directory before issuing an administration process request false
	 *            to let the administration process make all deletions Note: A true setting may impact performance.
	 * @param mailFileAction
	 *            Indicates the disposition of the user's mail file:
	 * @param denyGroup
	 *            The name of an existing group of type "Deny List Only" to which the name of the deleted user is added. The empty string
	 *            means do not add the user name to any group.
	 * @param deleteWindowsUser
	 *            true to delete the corresponding Windows user
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String deleteUser(final String userName, final boolean immediate, final int mailFileAction, final String denyGroup,
			final boolean deleteWindowsUser);

	/**
	 * Enters a request in the Administration Requests database to find all occurrences of a group in a domain.
	 * <p>
	 * This method triggers the "Find Name in Domain" administration process request.
	 * </p>
	 * <p>
	 * Results are posted to response documents in the Administration Requests database.
	 * </p>
	 *
	 * @param group
	 *            The name of the group.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String findGroupInDomain(final String group);

	/**
	 * Enters a request in the Administration Requests database to find all occurrences of a server in a domain.
	 * <p>
	 * This method triggers the "Find Name in Domain" administration process request.
	 * </p>
	 * <p>
	 * Results are posted to response documents in the Administration Requests database.
	 * </p>
	 *
	 * @param server
	 *            The name of the server.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String findServerInDomain(final String server);

	/**
	 * Enters a request in the Administration Requests database to find all occurrences of a user name in a domain.
	 * <p>
	 * This method triggers the "Find Name in Domain" administration process request.
	 * </p>
	 * <p>
	 * Results are posted to response documents in the Administration Requests database.
	 * </p>
	 *
	 * @param userName
	 *            The name of the user.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String findUserInDomain(final String userName);

	/**
	 * The certifier when making requests to the certificate authority.
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority()} is true, a CA process is available, and a certifier is required.
	 * </p>
	 *
	 * @return The certifier when making requests to the certificate authority.
	 *
	 */
	@Override
	public String getCertificateAuthorityOrg();

	/**
	 * The expiration date for the certifier ID.
	 * <p>
	 * If this property is not set, the expiration date is two years from the current date.
	 * </p>
	 *
	 *
	 */
	@Override
	public DateTime getCertificateExpiration();

	/**
	 * The file specification of the certifier ID file.
	 * <p>
	 * The file specification can be a complete file specification or can be relative to the Domino data directory.
	 * </p>
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority()} is false and a certifier is required.
	 * </p>
	 *
	 * @return The file specification of the certifier ID file.
	 *
	 */
	@Override
	public String getCertifierFile();

	/**
	 * The password of the certifier ID file.
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority()} is false and a certifier is required.
	 * </p>
	 *
	 * @return The password of the certifier ID file.
	 *
	 */
	@Override
	public String getCertifierPassword();

	/**
	 * The session that created the AdministrationProcess object.
	 *
	 */
	@Override
	public Session getParent();

	/**
	 * Indicates whether a certificate authority certifier is available.
	 *
	 */
	@Override
	public boolean isCertificateAuthorityAvailable();

	/**
	 * Indicates whether to use the certificate authority for certification.
	 * <p>
	 * If you set this property to true, specify {@link #setCertificateAuthorityOrg(String)}. You can ensure that the certifier is available
	 * with {@link #isCertificateAuthorityAvailable()}.
	 * </p>
	 * <p>
	 * If you set this property to false, specify {@link #setCertifierFile(String)} and {@link #setCertifierPassword(String)}.
	 * </p>
	 *
	 * @return true a certificate authority is used for certification.
	 *
	 */
	@Override
	public boolean isUseCertificateAuthority();

	/**
	 * Enters a request in the Administration Requests database to move a user's mail file.
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param newHomeServer
	 *            The full hierarchical name (can be abbreviated) of the new home server for the user's mail file.
	 * @param newHomeServerMailPath
	 *            The path name of the new directory or folder for the user's mail file relative to the server's data directory. The path
	 *            name does not include the file name.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath);

	/**
	 * Enters a request in the Administration Requests database to move a user's mail file.
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param newHomeServer
	 *            The full hierarchical name (can be abbreviated) of the new home server for the user's mail file.
	 * @param newHomeServerMailPath
	 *            The path name of the new directory or folder for the user's mail file relative to the server's data directory. The path
	 *            name does not include the file name.
	 * @param useSCOS
	 *            true if the new mail file makes use of the shared mail feature, false if the new mail file does not make use of the shared
	 *            mail feature
	 * @param newClusterReplicas
	 *            Replicas of the mail file in the same cluster as the new home server. The vector must consist of pairs of elements. The
	 *            first element of the pair specifies the name of a server in the same cluster as the new home server. The second element
	 *            specifies the path in the same form as the new home server mail path.
	 * @param deleteOldClusterReplicas
	 *            Used only if the user is being moved off a cluster.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath, final boolean useSCOS,
			final Vector newClusterReplicas, final boolean deleteOldClusterReplicas);

	/**
	 * Enters a request in the Administration Requests database to move a replica. The ACL will be copied with the replica, the full-text
	 * index will not be copied.
	 * <p>
	 * This method triggers "Check access" and "Non-cluster move replica" administration process requests.
	 * </p>
	 *
	 * @param sourceServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the database being moved. An empty string ("")
	 *            means the local server.
	 * @param sourceDBFile
	 *            The pathname of the database being moved relative to the data directory.
	 * @param destServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the moved replica. An empty string ("") means the
	 *            local server.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDBFile, final String destServer);

	/**
	 * Enters a request in the Administration Requests database to move a replica.
	 * <p>
	 * This method triggers "Check access" and "Non-cluster move replica" administration process requests.
	 * </p>
	 *
	 * @param sourceServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the database being moved. An empty string ("")
	 *            means the local server.
	 * @param sourceDBFile
	 *            The pathname of the database being moved relative to the data directory.
	 * @param destServer
	 *            The full hierarchical name (can be abbreviated) of the server containing the moved replica. An empty string ("") means the
	 *            local server.
	 * @param destDBFile
	 *            The pathname of the moved database relative to the data directory. Defaults to the same pathname as the source file.
	 * @param copyACL
	 *            true to copy the ACL, false to not copy the ACL
	 * @param createFTIndex
	 *            true to create a full-text index, false to not create a full-text index
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDBFile, final String destServer, final String destDBFile,
			final boolean copyACL, final boolean createFTIndex);

	/**
	 * Enters a request in the Administration Requests database to move a user's roaming files to another server.
	 * <p>
	 * The user must be configured as a roaming user. The administrator calling this method must have Create Replica rights on the
	 * destination server.
	 * </p>
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param destServer
	 *            The full hierarchical name (can be abbreviated) of the server to receive the user's roaming files.
	 * @param destServerPath
	 *            The path name of the new directory or folder for the roaming files relative to the server's data directory. The path name
	 *            does not include the file name.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String moveRoamingUser(final String userName, final String destServer, final String destServerPath);

	/**
	 * Enters a request in the Administration Requests database to rename a user based on a previous request to move the user in the
	 * hierarchy.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * This is phase 2 of the move. See {@link #moveUserInHierarchyRequest(String, String)} for phase 1.
	 * </p>
	 *
	 * @param requestNoteid
	 *            The note ID of the previous request to move the user in the hierarchy.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid);

	/**
	 * Enters a request in the Administration Requests database to rename a user based on a previous request to move the user in the
	 * hierarchy.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * This is phase 2 of the move. See {@link #moveUserInHierarchyRequest(String, String)} for phase 1.
	 * </p>
	 *
	 * @param requestNoteid
	 *            The note ID of the previous request to move the user in the hierarchy.
	 * @param lastName
	 *            New last name. An empty string means just that. An asterisk (*) means no change.
	 * @param firstName
	 *            New first name. An empty string means just that. An asterisk (*) means no change.
	 * @param middleInitial
	 *            New middle initial. An empty string means just that. An asterisk (*) means no change.
	 * @param orgUnit
	 *            New organizational unit. An empty string means just that. An asterisk (*) means no change.
	 * @param altCommonName
	 *            New alternate common name. An empty string means just that. An asterisk (*) means no change.
	 * @param altOrgUnit
	 *            New alternate common name. An empty string means just that. An asterisk (*) means no change.
	 * @param altLanguage
	 *            New alternate organizational unit. An empty string means just that. An asterisk (*) means no change.
	 * @param renameWindowsUser
	 *            New alternate language. An empty string means just that. An asterisk (*) means no change.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid, final String lastName, final String firstName,
			final String middleInitial, final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser);

	/**
	 * Enters a request in the Administration Requests database to move a user's name in the hierarchy.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * This is phase 1 of the move. See {@link #moveUserInHierarchyComplete(String)} for phase 2.
	 * </p>
	 * <p>
	 * The user's primary name will not be changed when the name is moved
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param targetCertifier
	 *            The name of the new certifier.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier);

	/**
	 * Enters a request in the Administration Requests database to move a user's name in the hierarchy.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * This is phase 1 of the move. See {@link #moveUserInHierarchyComplete} for phase 2.
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param targetCertifier
	 *            The name of the new certifier.
	 * @param allowPrimaryNameChange
	 *            true if the user's primary name can be changed when the name is moved. False if the user's primary name cannot be changed
	 *            when the name is moved
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier, final boolean allowPrimaryNameChange);

	/**
	 * Enters a request in the Administration Requests database to recertify a Domino server.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 *
	 * @param server
	 *            The full hierarchical name (can be abbreviated) of the server.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String recertifyServer(final String server);

	/**
	 * Enters a request in the Administration Requests database to recertify a Notes user.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String recertifyUser(final String userName);

	/**
	 * Enters a request in the Administration Requests database to remove a server from a cluster.
	 *
	 * @param server
	 *            The name of the server.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String removeServerFromCluster(final String server);

	/**
	 * Enters a request in the Administration Requests database to rename a group.
	 * <p>
	 * This method triggers "Rename group in Domino Directory," "Rename group in Person documents," "Rename group in Access Control List,"
	 * and "Rename group in Reader / Author fields" administration process requests.
	 * </p>
	 *
	 * @param group
	 *            The name of the group.
	 * @param newGroup
	 *            The new name of the group.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String renameGroup(final String group, final String newGroup);

	/**
	 * Enters a request in the Administration Requests database to rename a user.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * You must specify at least one of these parameters: <code>lastname</code>, <code>firstname</code>, <code>middleinitial</code>,
	 * <code>orgunit</code>, <code>altcommonname</code>, <code>altorgunit</code>, or <code>altlanguage</code>.
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param lastName
	 *            New last name. An empty string means just that. An asterisk (*) means no change.
	 * @param firstName
	 *            New first name. An empty string means just that. An asterisk (*) means no change.
	 * @param middleInitial
	 *            New middle initial. An empty string means just that. An asterisk (*) means no change.
	 * @param orgUnit
	 *            New organizational unit. An empty string means just that. An asterisk (*) means no change.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit);

	/**
	 * Enters a request in the Administration Requests database to rename a user.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 * <p>
	 * You must specify at least one of these parameters: <code>lastname</code>, <code>firstname</code>, <code>middleinitial</code>,
	 * <code>orgunit</code>, <code>altcommonname</code>, <code>altorgunit</code>, or <code>altlanguage</code>.
	 * </p>
	 *
	 * @param userName
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param lastName
	 *            New last name. An empty string means just that. An asterisk (*) means no change.
	 * @param firstName
	 *            New first name. An empty string means just that. An asterisk (*) means no change.
	 * @param middleInitial
	 *            New middle initial. An empty string means just that. An asterisk (*) means no change.
	 * @param orgUnit
	 *            New organizational unit. An empty string means just that. An asterisk (*) means no change.
	 * @param altCommonName
	 *            New alternate common name. An empty string means just that. An asterisk (*) means no change.
	 * @param altOrgUnit
	 *            New alternate organizational unit. An empty string means just that. An asterisk (*) means no change.
	 * @param altLanguage
	 *            New alternate language. An empty string means just that. An asterisk (*) means no change.
	 * @param renameWindowsUser
	 *            true to change the corresponding Windows user account full name. The Domino user's net user name, if defined, or short
	 *            name is used as the Windows user name. The new values of lastname, firstname, and middleinitial (the common name in
	 *            Domino) are used to specify the Window user's new full name. You must specify at least one of these parameters;
	 *            unspecified parameters default to the user entry in the Domino Directory. False to not change the corresponding Windows
	 *            user account full name
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser);

	/**
	 * Enters a request in the Administration Requests database to rename a Web user.
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param newFullName
	 *            New full hierarchical name (can be abbreviated).
	 * @param newLastName
	 *            New last name. An empty string means just that. An asterisk (*) means no change.
	 * @param newFirstName
	 *            New first name. An empty string means just that. An asterisk (*) means no change.
	 * @param newMiddleInitial
	 *            New middle initial. An empty string means just that. An asterisk (*) means no change.
	 * @param newShortName
	 *            New short name. An empty string means just that. An asterisk (*) means no change.
	 * @param newInternetAddress
	 *            New internet address. An empty string means just that. An asterisk (*) means no change.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String renameWebUser(final String userName, final String newFullName, final String newLastName, final String newFirstName,
			final String newMiddleInitial, final String newShortName, final String newInternetAddress);

	/**
	 * The certifier when making requests to the certificate authority.
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority()} is true, a CA process is available, and a certifier is required.
	 * </p>
	 *
	 * @param org
	 *            The certifier
	 */
	@Override
	public void setCertificateAuthorityOrg(final String org);

	/**
	 * The expiration date for the certifier ID.
	 * <p>
	 * If this property is not set, the expiration date is two years from the current date.
	 * </p>
	 *
	 * @param expiration
	 *            the expiration date
	 */
	@Override
	public void setCertificateExpiration(final lotus.domino.DateTime expiration);

	/**
	 * The file specification of the certifier ID file.
	 * <p>
	 * The file specification can be a complete file specification or can be relative to the Domino data directory.
	 * </p>
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority()} is false and a certifier is required.
	 * </p>
	 *
	 * @param fileSpec
	 */
	@Override
	public void setCertifierFile(final String fileSpec);

	/**
	 * The password of the certifier ID file.
	 * <p>
	 * This property applies when {@link #isUseCertificateAuthority} is false and a certifier is required.
	 * </p>
	 *
	 * @param password
	 */
	@Override
	public void setCertifierPassword(final String password);

	/**
	 * Enters a request in the Administration Requests database to set the Directory Assistance field in a server document.
	 *
	 * @param server
	 *            The name of the server on which to change the settings.
	 * @param dbFile
	 *            The pathname of the Directory Assistance replica on the server.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String setServerDirectoryAssistanceSettings(final String server, final String dbFile);

	/**
	 * Sets whether to use the certificate authority for certification.
	 * <p>
	 * If you set this property to true, specify {@link #setCertificateAuthorityOrg(String)}. You can ensure that the certifier is available
	 * with {@link #isCertificateAuthorityAvailable()}.
	 * </p>
	 * <p>
	 * If you set this property to false, specify {@link #setCertifierFile(String)} and {@link #setCertifierPassword(String)}.
	 * </p>
	 *
	 * @param flag
	 *            specify true to use the certificate authority
	 */
	@Override
	public void setUseCertificateAuthority(final boolean flag);

	/**
	 * Enters a request in the Administration Requests database to change the password management settings on the Administration tab of the
	 * user's person document.
	 * <p>
	 * Specify null to take the default for the optional parameters 2-5.
	 * </p>
	 * <p>
	 * At least one optional parameter must be specified.
	 * </p>
	 *
	 * @param userName
	 *            The full hierarchical name (can be abbreviated) of the user.
	 * @param notesPasswordCheckSetting
	 *            <ul>
	 *            <li>AdministrationProcess.PWD_CHK_CHECKPASSWORD requires the user to enter a password when authenticating with servers
	 *            that have password checking enabled</li>
	 *            <li>AdministrationProcess.PWD_CHK_DONTCHECKPASSWORD does not require the user to enter a password when authenticating with
	 *            other servers (default)</li>
	 *            <li>AdministrationProcess.PWD_CHK_LOCKOUT prevents the user from accessing servers that have password checking
	 *            enabled</li>
	 *            </ul>
	 * @param notesPasswordChangeInterval
	 *            Required change interval - number of days at which the user must supply a new password. Defaults to zero.
	 * @param notesPasswordGracePeriod
	 *            Grace period (in days) for changing the password. Defaults to zero.
	 * @param internetPasswordForceChange
	 *            true to force the user to change the password on next login, false to not force the user to change the password (default)
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String setUserPasswordSettings(final String userName, final Integer notesPasswordCheckSetting,
			final Integer notesPasswordChangeInterval, final Integer notesPasswordGracePeriod, final Boolean internetPasswordForceChange);

	/**
	 * Enters a request in the Administration Requests database to sign a database. Signs existing signatures only.
	 *
	 * @param server
	 *            The full hierarchical name (can be abbreviated) of the server containing the database to be signed. The empty string ("")
	 *            means the local server.
	 * @param dbFile
	 *            New pathname of the database to be signed.
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile);

	/**
	 * Enters a request in the Administration Requests database to sign a database.
	 *
	 * @param server
	 *            The full hierarchical name (can be abbreviated) of the server containing the database to be signed. The empty string ("")
	 *            means the local server.
	 * @param dbFile
	 *            New pathname of the database to be signed.
	 * @param updateOnly
	 *            true to sign only existing signatures (faster), false to always sign the database
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile, final boolean updateOnly);

	/**
	 * Enters a request in the Administration Requests database to upgrade a user from a flat ID to a hierarchical ID.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 *
	 * @param userName
	 *            The flat name of the user.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName);

	/**
	 * Enters a request in the Administration Requests database to upgrade a user from a flat ID to a hierarchical ID.
	 * <p>
	 * The AdministrationProcess properties must specify a certifier.
	 * </p>
	 *
	 * @param userName
	 *            The flat name of the user.
	 * @param orgUnit
	 *            New organizational unit.
	 * @param altCommonName
	 *            New alternate common name.
	 * @param altOrgUnit
	 *            New alternate organizational unit.
	 * @param altLanguage
	 *            New alternate language.
	 * @return The note ID of an entry created in the Administration Requests database, or an empty string if the certificate authority
	 *         processes the request.
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName, final String orgUnit, final String altCommonName,
			final String altOrgUnit, final String altLanguage);

	/**
	 * Enters a request in the Administration Requests database to delete a user.
	 * <p>
	 * This method triggers "Delete person in Domino Directory," "Delete in Person documents," "Delete in Access Control List," "Delete in
	 * Reader / Author fields," "Get information for deletion," "Approve file deletion," "Request file deletion," "Delete mail file,"
	 * "Delete unlinked mail file," "Approve deletion of Private Design Elements," "Request to delete Private Design Elements," and "Delete
	 * Private Design Elements" administration process requests.
	 * </p>
	 *
	 * @param arg0
	 *            The hierarchical name of the user in canonical or abbreviated form.
	 * @param arg1
	 *            true to delete all references to the user in the Domino Directory before issuing an administration process request, false
	 *            to let the administration process make all deletions. Note: A true setting may impact performance.
	 * @param arg2
	 *            Indicates the disposition of the user's mail file:
	 *            <ul>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_ALL deletes the mail file on the user's home server and all replicas.</li>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_HOME deletes the mail file on the user's home server.</li>
	 *            <li>AdministrationProcess.MAILFILE_DELETE_NONE leaves the user's mail file.</li>
	 *            </ul>
	 * @param arg3
	 *            The name of an existing group of type "Deny List Only" to which the name of the deleted user is added. The empty string
	 *            means do not add the user name to any group.
	 * @param arg4
	 *            true to delete the corresponding Windows user
	 * @param arg5
	 *
	 * @return The note ID of an entry created in the Administration Requests database.
	 */
	@Override
	public String deleteUser(String arg0, boolean arg1, int arg2, String arg3, boolean arg4, int arg5);

}
