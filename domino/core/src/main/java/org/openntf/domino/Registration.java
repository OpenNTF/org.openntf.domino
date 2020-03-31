/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the creation or administration of an ID file.
 */
public interface Registration
		extends Base<lotus.domino.Registration>, lotus.domino.Registration, org.openntf.domino.ext.Registration, SessionDescendant {

	public static class Schema extends FactorySchema<Registration, lotus.domino.Registration, Session> {
		@Override
		public Class<Registration> typeClass() {
			return Registration.class;
		}

		@Override
		public Class<lotus.domino.Registration> delegateClass() {
			return lotus.domino.Registration.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Creates a Certifier record in a Domino Directory based on a certifier ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewCertifier(String, String, String) registerNewCertifier()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The certifier ID file to be added to the Domino Directory; specify the full path, for example, c:\notes\data\cert.id.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addCertifierToAddressBook(final String idFile);

	/**
	 * Creates a Certifier record in a Domino Directory based on a certifier ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewCertifier(String, String, String) registerNewCertifier()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The certifier ID file to be added to the Domino Directory; specify the full path, for example, c:\notes\data\cert.id.
	 * @param password
	 *            The password for the certifier ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addCertifierToAddressBook(final String idFile, final String password);

	/**
	 * Creates a Certifier record in a Domino Directory based on a certifier ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer()
	 * registerNewCertifier()}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewCertifier(String, String, String) registerNewCertifier()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The certifier ID file to be added to the Domino Directory; specify the full path, for example, c:\notes\data\cert.id.
	 * @param password
	 *            The password for the certifier ID file.
	 * @param location
	 *            A value for the location field in the Certifier record.
	 * @param comment
	 *            A value for the comment field in the Certifier record.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addCertifierToAddressBook(final String idFile, final String password, final String location, final String comment);

	/**
	 * Creates a Server record in a Domino Directory based on a server ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with
	 * {@link #registerNewServer(String, String, String, String, String, String, String, String, String, String) registerNewServer()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file for the server to be added to the Domino Directory; specify the full path, for example,
	 *            c:\notes\data\server.id.
	 * @param server
	 *            The name of the server to be added to the Domino Directory.
	 * @param domain
	 *            The name of the server to be added to the Domino Directory.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addServerToAddressBook(final String idFile, final String server, final String domain);

	/**
	 * Creates a Server record in a Domino Directory based on a server ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with
	 * {@link #registerNewServer(String, String, String, String, String, String, String, String, String, String) registerNewServer()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file for the server to be added to the Domino Directory; specify the full path, for example,
	 *            c:\notes\data\server.id.
	 * @param server
	 *            The name of the server to be added to the Domino Directory.
	 * @param domain
	 *            The name of the server to be added to the Domino Directory.
	 * @param userPassword
	 *            The password for the server ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addServerToAddressBook(final String idFile, final String server, final String domain, final String userPassword);

	/**
	 * Creates a Server record in a Domino Directory based on a server ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method occurs automatically with
	 * {@link #registerNewServer(String, String, String, String, String, String, String, String, String, String) registerNewServer()} if
	 * {@link #getUpdateAddressBook() UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file for the server to be added to the Domino Directory; specify the full path, for example,
	 *            c:\notes\data\server.id.
	 * @param server
	 *            The name of the server to be added to the Domino Directory.
	 * @param domain
	 *            The name of the server to be added to the Domino Directory.
	 * @param userPassword
	 *            The password for the server ID file.
	 * @param network
	 *            The notes named network (NNN) on which the server runs.
	 * @param adminName
	 *            The full name of the administrator of the server.
	 * @param title
	 *            A value for the title field of the Domino Directory record.
	 * @param location
	 *            A value for the location field of the Domino Directory record.
	 * @param comment
	 *            A value for the comment field of the Domino Directory record.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addServerToAddressBook(final String idFile, final String server, final String domain, final String userPassword,
			final String network, final String adminName, final String title, final String location, final String comment);

	/**
	 * Adds the name of a setup profile to the user's Person record in the Domino Directory.
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param username
	 *            The name of the user.
	 * @param profile
	 *            The name of the setup profile to be added.
	 */
	@Override
	public void addUserProfile(final String userName, final String profile);

	/**
	 * Creates a Person record in a Domino Directory based on a user ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * {@link #getStoreIDInAddressBook() StoreIDInAddressBook} must be true.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewUser(String, String, String) registerNewUser()} if
	 * {@link #setUpdateAddressBook(boolean) UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file for the user to be added to the Domino Directory; specify the full path, for example,
	 *            c:\\notes\\data\\user.id.
	 * @param fullname
	 *            The user's full name.
	 * @param lastName
	 *            The user's last name.
	 * @return true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addUserToAddressBook(final String idFile, final String fullName, final String lastName);

	/**
	 * Creates a Person record in a Domino Directory based on a user ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * {@link #getStoreIDInAddressBook() StoreIDInAddressBook} must be true.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewUser(String, String, String) registerNewUser()} if
	 * {@link #setUpdateAddressBook(boolean) UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file for the user to be added to the Domino Directory; specify the full path, for example,
	 *            c:\\notes\\data\\user.id.
	 * @param fullname
	 *            The user's full name.
	 * @param lastName
	 *            The user's last name.
	 * @param userPassword
	 *            The password for the user ID file.
	 * @return true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addUserToAddressBook(final String idFile, final String fullName, final String lastName, final String userPassword);

	/**
	 * Creates a Person record in a Domino Directory based on a user ID file and optionally attaches the ID file.
	 * <p>
	 * Set {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * {@link #getStoreIDInAddressBook() StoreIDInAddressBook} must be true.
	 * </p>
	 * <p>
	 * This method occurs automatically with {@link #registerNewUser(String, String, String) registerNewUser()} if
	 * {@link #setUpdateAddressBook(boolean) UpdateAddressBook} is true.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file for the user to be added to the Domino Directory; specify the full path, for example,
	 *            c:\\notes\\data\\user.id.
	 * @param fullname
	 *            The user's full name.
	 * @param lastName
	 *            The user's last name.
	 * @param userPassword
	 *            The password for the user ID file.
	 * @param firstName
	 *            The user's first name.
	 * @param middleName
	 *            The user's middle name.
	 * @param mailServer
	 *            The name of the user's mail server specified in canonical format.
	 * @param mailFilePath
	 *            The relative pathname for the user's mail file, for example, mail\jyip.nsf.
	 * @param forwardingAddress
	 *            The user's forwarding mail address.
	 * @param location
	 *            A value for the location field in the Domino Directory record.
	 * @param comment
	 *            A value for the comment field in the Domino Directory record.
	 * @return true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean addUserToAddressBook(final String idFile, final String fullName, final String lastName, final String userPassword,
			final String firstName, final String middleName, final String mailServer, final String mailFilePath,
			final String forwardingAddress, final String location, final String comment);

	/**
	 * Cross-certifies an ID file.
	 * <p>
	 * This method requires that {@link #setCertifierIDFile(String) CertifierIDFile} be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 10 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file to be cross-certified. Specify the full path, for example, c:\\notes\\data\\user.id.
	 * @return true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean crossCertify(final String idFile);

	/**
	 * Cross-certifies an ID file.
	 * <p>
	 * This method requires that {@link #setCertifierIDFile(String) CertifierIDFile} be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 10 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file to be cross-certified. Specify the full path, for example, c:\\notes\\data\\user.id.
	 * @param certPassword
	 *            The password for the certifier ID file.
	 * @return true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean crossCertify(final String idFile, final String certPassword);

	/**
	 * Cross-certifies an ID file.
	 * <p>
	 * This method requires that {@link #setCertifierIDFile(String) CertifierIDFile} be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 10 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file to be cross-certified. Specify the full path, for example, c:\\notes\\data\\user.id.
	 * @param certPassword
	 *            The password for the certifier ID file.
	 * @param comment
	 *            A value for the comment field in the user's Domino Directory record.
	 * @return Returns true if the operation is successful; false otherwise.
	 */
	@Override
	public boolean crossCertify(final String idFile, final String certPassword, final String comment);

	/**
	 * Cross-certifies an ID file.
	 * <p>
	 * This method requires that {@link #setCertifierIDFile(String) CertifierIDFile} be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 10 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param idfile
	 *            The ID file to be cross-certified. Specify the full path, for example, c:\\notes\\data\\user.id.
	 * @param certPassword
	 *            The password for the certifier ID file.
	 * @param comment
	 *            A value for the comment field in the user's Domino Directory record.
	 * @param idpw
	 *            The password for the ID file to be cross-certified
	 * @return Returns true if the operation is successful; false otherwise.
	 * @since Domino V10
	 */
	@Override
	public boolean crossCertify(final String idFile, final String certPassword, final String comment, final String idPassword);

	/**
	 * Deletes the user ID attachment from a Person or Server record in a Domino Directory.
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by
	 *
	 * @{link {@link #getRegistrationServer() RegistrationServer}, or names.nsf in the local Domino data directory if this property is not
	 *        set.
	 *        </p>
	 *
	 * @param username
	 *            The full name of the user.
	 * @param isserverid
	 *            True if the name represents a server ID; false if the name represents a person.
	 */
	@Override
	public void deleteIDOnServer(final String userName, final boolean isServerID);

	/**
	 * Alternate names for the organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewCertifier()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return Alternate names for the organizational unit to use when creating ID files.
	 */
	@Override
	public Vector<String> getAltOrgUnit();

	/**
	 * Alternate names for the organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewCertifier()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return Alternate names for the organizational unit to use when creating ID files.
	 */
	@Override
	public Vector<String> getAltOrgUnitLang();

	/**
	 * The complete file specification of the certifier ID to use when creating IDs.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return The complete file specification of the certifier ID to use when creating IDs.
	 */
	@Override
	public String getCertifierIDFile();

	/**
	 * The certifier when a Notes ID is not being used.
	 * <p>
	 * Optionally set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * If {@link #isUseCertificateAuthority() UseCertificateAuthority} is true, this property must specify the name of a CA-enabled
	 * certifier.
	 * </p>
	 * <p>
	 * If you are working in a hosted environment and registering a user to a hosted organization, use this property to specify a certifier
	 * created for the hosted organization.
	 * </p>
	 *
	 * @return The certifier when a Notes ID is not being used.
	 */
	@Override
	public String getCertifierName();

	/**
	 * Indicates whether a mail database is created with the ID file when calling {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * <p>
	 * Set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return Indicates whether a mail database is created with the ID file when calling registerNewUser.
	 */
	@Override
	public boolean getCreateMailDb();

	/**
	 * The expiration date to use when creating ID files.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * This property can be null.
	 * </p>
	 *
	 * @return The expiration date to use when creating ID files or null when not set.
	 */
	@Override
	public DateTime getExpiration();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Registration#getForeignDN()
	 */
	@Override
	public String getForeignDN();

	/**
	 * The groups to which a user is assigned during registration.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return The groups to which a user is assigned during registration.
	 */
	@Override
	public Vector<String> getGroupList();

	/**
	 * Detaches the user ID file from a Person or Server record in a Domino Directory.
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param userName
	 *            The full name of the user.
	 * @param filePath
	 *            A name for the ID file; specify a full pathname, for example, c:\\notes\\user.id.
	 * @param isServerID
	 *            True if the name represents a server ID, false if the name represents a person.
	 */
	@Override
	public void getIDFromServer(final String userName, final String filePath, final boolean isServerID);

	/**
	 * The type of ID file to create.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return The type of ID file to create. One of:
	 *         <ul>
	 *         <li>Registration.ID_FLAT to create a flat ID</li>
	 *         <li>Registration.ID_HIERARCHICAL to create a hierarchical ID</li>
	 *         <li>Registration.ID_CERTIFIER to create an ID that depends on whether the certifier ID is flat or hierarchical</li>
	 *         </ul>
	 */
	@Override
	public int getIDType();

	/**
	 * A name that is assigned Manager access in the mail database ACL.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * The mail database owner may or may not be a Manager depending on {@link #getMailOwnerAccess() MailOwnerAccess}.
	 * </p>
	 *
	 * @return A name that is assigned Manager access in the mail database ACL.
	 */
	@Override
	public String getMailACLManager();

	/**
	 * The user's Internet name for sending and receiving mail.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property remains an empty string, the registration process generates an Internet name.
	 * </p>
	 *
	 * @return The user's Internet name for sending and receiving mail.
	 */
	@Override
	public String getMailInternetAddress();

	/**
	 * The mail database ACL setting for the owner.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return The mail database ACL setting for the owner.
	 */
	@Override
	public int getMailOwnerAccess();

	/**
	 * The maximum size of the user's mail database, in megabytes.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * A value of 0 (default) sets no size limit.
	 * </p>
	 *
	 * @return The maximum size of the user's mail database, in megabytes.
	 */
	@Override
	public int getMailQuotaSizeLimit();

	/**
	 * The size, in megabytes, at which the user's mail database issues a warning that it is getting too large.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * A value of 0 (default) sets no warning threshold.
	 * </p>
	 *
	 * @return The size, in megabytes, at which the user's mail database issues a warning that it is getting too large.
	 */
	@Override
	public int getMailQuotaWarningThreshold();

	/**
	 * The names of servers to which the mail database will replicate.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * This property applies only to clustered servers.
	 * </p>
	 *
	 * @return The names of servers to which the mail database will replicate.
	 */
	@Override
	public Vector<String> getMailReplicaServers();

	/**
	 * The user's mail system.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return The user's mail system with possible values:
	 *         <ul>
	 *         <li>Registration.REG_MAILSYSTEM_IMAP (2)</li>
	 *         <li>Registration.REG_MAILSYSTEM_INOTES (3)</li>
	 *         <li>Registration.REG_MAILSYSTEM_INTERNET (4)</li>
	 *         <li>Registration.REG_MAILSYSTEM_NONE (6)</li>
	 *         <li>Registration.REG_MAILSYSTEM_NOTES (0) (default)</li>
	 *         <li>Registration.REG_MAILSYSTEM_OTHER (5)</li>
	 *         <li>Registration.REG_MAILSYSTEM_POP (1)</li>
	 *         </ul>
	 */
	@Override
	public int getMailSystem();

	/**
	 * The name of the template for the design of the mail file.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property remains an empty string, a standard template is used.
	 * </p>
	 *
	 * @return The name of the template for the design of the mail file.
	 */
	@Override
	public String getMailTemplateName();

	/**
	 * The strength assigned to a password in an ID file.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * This property defines the password strength required for subsequent changes to the password by the user. A password assigned in the
	 * methods listed above is not initially checked against the strength.
	 * </p>
	 * <p>
	 * Domino measures a password's strength and security according to the level assigned on its password quality scale. The scale ranges
	 * from Weak to Strong or from 0 (lowest - no password required) to 16 (highest). For more information about the password quality scale,
	 * see "Understanding the password quality scale" in <em class="ph i">Administering the Domino System</em>.
	 * </p>
	 * <div>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left" valign="bottom">
	 * <tr>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e120">
	 * <p>
	 * Password quality scale
	 * </p>
	 * </th>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e125">
	 * <p>
	 * Description
	 * </p>
	 * </th>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e130">
	 * <p>
	 * Example
	 * </p>
	 * </th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 0
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Password is optional.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * n/a
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 1
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Allow any password.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "b", "3"
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 2-6
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Allow a weak password, even though you might be able to guess it by trial and error.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "password", "doughnut" (password quality scale 3)
	 * </p>
	 * <p>
	 * "lightferret", "b 4D" (password quality scale 6)
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 7-12
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Require a password that is difficult to guess, but might be vulnerable to an automated attack.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "pqlrtmxr", "wefourkings" (password quality scale 8)
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 13-16
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Require a strong password, even though the user may have difficulty remembering it.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "4891spyONu" (password quality scale 13)
	 * </p>
	 * <p>
	 * "lakestreampondriverocean", "stRem2pO()" (password quality scale 15)
	 * </p>
	 * <p>
	 * "stream8pond1river7lake2ocean" (password quality scale 16)
	 * </p>
	 * </td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * </div>
	 *
	 * @return The strength assigned to a password in an ID file.
	 */
	@Override
	public int getMinPasswordLength();

	/**
	 * The organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return The organizational unit to use when creating ID files.
	 */
	@Override
	public String getOrgUnit();

	/**
	 * The session that contains a Registration object.
	 */
	@Override
	public Session getParent();

	/**
	 * The name of an explicit policy.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return The name of an explicit policy.
	 */
	@Override
	public String getPolicyName();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Registration#getPublicKeySize()
	 */
	@Override
	public int getPublicKeySize();

	/**
	 * The log file to use when creating IDs.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}. No logging occurs if this parameter is null. If this parameter is anything other than null, logging goes to the
	 * file named cerlog.nsf in the Domino data directory on the registration server.
	 * </p>
	 *
	 */
	@Override
	public String getRegistrationLog();

	/**
	 * The server to use when creating IDs and performing other registration functions.
	 * <p>
	 * Set this property before calling any method except {@link #switchToID(String, String)} method.
	 * </p>
	 * <p>
	 * If you don't set this property, the local Domino or Notes directory is assumed.
	 * </p>
	 * <p>
	 * This property is used to:
	 * </p>
	 * <ul>
	 * <li>Attach an ID to the user, server, or certifier document in the Domino Directory.</li>
	 * <li>Create a mail database for a new user.</li>
	 * </ul>
	 *
	 * @return The server to use when creating IDs and performing other registration functions.
	 */
	@Override
	public String getRegistrationServer();

	/**
	 * The interval in days for cleaning up data on Notes clients set up for roaming users.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 * <p>
	 * {@link #getRoamingCleanupSetting() RoamingCleanupSetting} must be Registration.REG_ROAMING_CLEANUP_EVERY_NDAYS.
	 * </p>
	 *
	 * @return The interval in days for cleaning up data on Notes clients set up for roaming users.
	 */
	@Override
	public int getRoamingCleanupPeriod();

	/**
	 * Indicates the clean-up process for data on Notes clients set up for roaming users.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 * *
	 * <p>
	 * Legal values:
	 * </p>
	 * <dl>
	 * <dt>Registration.REG_ROAMING_CLEANUP_AT_SHUTDOWN (2)</dt>
	 * <dd>Indicates that roaming data is deleted upon Notes shutdown.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_EVERY_NDAYS (1)</dt>
	 * <dd>Indicates that roaming data is deleted every so many days as specified by {@link #getRoamingCleanupPeriod()
	 * RoamingCleanupPeriod}.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_NEVER (0) (default)</dt>
	 * <dd>indicates that roaming data is never deleted.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_PROMPT (3)</dt>
	 * <dd>Indicates that a user is prompted upon exiting Notes. The user can choose an individual clean-up or not. The user can also
	 * decline being prompted in the future.</dd>
	 * </dl>
	 *
	 * @return Indicates the clean-up process for data on Notes clients set up for roaming users.
	 */
	@Override
	public int getRoamingCleanupSetting();

	/**
	 * The server on which the user's roaming data is stored.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 * <p>
	 * If this property is an empty string, the roaming server defaults to the user's mail server.
	 * </p>
	 *
	 * @return The server on which the user's roaming data is stored.
	 */
	@Override
	public String getRoamingServer();

	/**
	 * The subdirectory that contains the user's roaming data.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 *
	 * @return The subdirectory that contains the user's roaming data.
	 */
	@Override
	public String getRoamingSubdir();

	/**
	 * The short name when creating user IDs.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * By default the short name is the first letter of the first name followed by the last name.
	 * </p>
	 *
	 * @return The short name when creating user IDs.
	 */
	@Override
	public String getShortName();

	/**
	 * Indicates whether the ID file is stored in the server's Domino Directory.
	 * <p>
	 * Set this property before calling {@link #addCertifierToAddressBook(String)}, {@link #addServerToAddressBook(String, String, String)},
	 * {@link #addUserToAddressBook(String, String, String)}, {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return Indicates whether the ID file is stored in the server's Domino Directory.
	 */
	@Override
	public boolean getStoreIDInAddressBook();

	/**
	 * Indicates whether an ID document in the Domino Directory is created when the ID file is created.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}. You can create a Domino Directory document later with {@link #addCertifierToAddressBook(String)
	 * addCertifierToAddressBook()}, {@link #addServerToAddressBook(String, String, String) addServerToAddressBook()}, or
	 * {@link #addUserToAddressBook(String, String, String) addCertifierToAddressBook()}.
	 * </p>
	 */
	@Override
	public boolean getUpdateAddressBook();

	/**
	 * Gets information about a user from a Domino Directory.
	 * <p>
	 * The parameters, except for the first, are return values.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 *
	 * @param userName
	 *            The name of the user.
	 * @param mailServer
	 *            Returns the name of the user's mail server.
	 * @param mailFile
	 *            Returns the name of the user's mail file.
	 * @param mailDomain
	 *            Returns the name of the user's mail domain.
	 * @param mailSystem
	 *            Returns the name of the user's mail system.
	 * @param profile
	 *            Returns the name of the User Setup Profile document.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getUserInfo(final String userName, final StringBuffer mailServer, final StringBuffer mailFile,
			final StringBuffer mailDomain, final StringBuffer mailSystem, final Vector profile);

	/**
	 * Indicates whether a short name must be unique.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return True when a short name must be unique.
	 */
	@Override
	public boolean isEnforceUniqueShortName();

	/**
	 * Indicates the creation of a full-text index for the mail database.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @return True if the full-text index should be created for the mail database.
	 */
	@Override
	public boolean isMailCreateFTIndex();

	/**
	 * Indicates that no ID file is created during registration.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If you set this property true, leave {@link #getStoreIDInAddressBook() StoreIDInAddressBook} false.
	 * </p>
	 *
	 * @return Indicates that no ID file is created during registration.
	 */
	@Override
	public boolean isNoIDFile();

	/**
	 * Indicates whether an ID file is North American.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @return Indicates whether an ID file is North American.
	 */
	@Override
	public boolean isNorthAmerican();

	/**
	 * Indicates whether a user is a roaming user.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property is true, you can apply the following properties: {@link #getRoamingCleanupPeriod() RoamingCleanupPeriod},
	 * {@link #getRoamingCleanupSetting() RoamingCleanupSetting}, {@link #getRoamingServer() RoamingServer}, and {@link #getRoamingSubdir()
	 * RoamingSubdir}.
	 * </p>
	 *
	 * @return Indicates whether a user is a roaming user.
	 */
	@Override
	public boolean isRoamingUser();

	/**
	 * Indicates whether the ID file is stored in the user's mail file.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * This property applies only to Domino Web Access and allows Notes users to read their encrypted mail while using Domino Web Access.
	 * </p>
	 *
	 * @return Indicates whether the ID file is stored in the user's mail file.
	 */
	@Override
	public boolean isStoreIDInMailfile();

	/**
	 * Synchronizes the user's Internet password with the password for the Notes client ID.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * The Internet password is in the user's Person document in the Domino Directory.
	 * </p>
	 * <p>
	 * If the user changes the password for the Notes client ID, the Internet password automatically (but not immediately) changes to match
	 * it.
	 * </p>
	 *
	 * @return Synchronizes the user's Internet password with the password for the Notes client ID.
	 */
	@Override
	public boolean isSynchInternetPassword();

	/**
	 * Indicates that the certificate authority (CA) is used for registration.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property is true, {@link #getCertifierName() CertifierName} must specify the name of a CA-enabled certifier.
	 * </p>
	 * <p>
	 * If this property is true, the certifier ID and password are not needed for registration.
	 * </p>
	 * <p>
	 * If you are working in a hosted environment and are registering a user to a hosted organization, be sure to register the user with a
	 * certifier created for that hosted organization.
	 * </p>
	 *
	 * @return Indicates that the certificate authority (CA) is used for registration.
	 */
	@Override
	public boolean isUseCertificateAuthority();

	/**
	 * Recertifies an ID file.
	 * <p>
	 * {@link #getCertifierIDFile() CertifierIDFile} must be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 2 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method prompts for the ID password.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file to be recertified; specify the full path, for example, c:\\notes\\data\\user.id.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean recertify(final String idFile);

	/**
	 * Recertifies an ID file.
	 * <p>
	 * {@link #getCertifierIDFile() CertifierIDFile} must be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 2 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method prompts for the ID password.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file to be recertified; specify the full path, for example, c:\\notes\\data\\user.id.
	 * @param certPassword
	 *            The password for the certifier ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean recertify(final String idFile, final String certPassword);

	/**
	 * Recertifies an ID file.
	 * <p>
	 * {@link #getCertifierIDFile() CertifierIDFile} must be set.
	 * </p>
	 * <p>
	 * {@link #getExpiration() Expiration} defaults to 2 years from now.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 * <p>
	 * This method prompts for the ID password.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file to be recertified; specify the full path, for example, c:\\notes\\data\\user.id.
	 * @param certPassword
	 *            The password for the certifier ID file.
	 * @param comment
	 *            A value for the comment field in the Domino Directory record for the user.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean recertify(final String idFile, final String certPassword, final String comment);

	/**
	 * Creates a certifier ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #getCertifierIDFile() CertifierIDFile}, {@link #getExpiration() Expiration},
	 * {@link #getIDType() IDType}, and {@link #getMinPasswordLength() MinPasswordLength}. {@link #getExpiration() Expiration} defaults to
	 * 100 years from now for organizations and organizational units, and cannot be changed for organizations.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} true to add a Certifier record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} true to attach the ID to the Domino Directory record. These operations
	 * can be performed separately with {@link #addCertifierToAddressBook(String)}.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #setRegistrationServer(String)
	 * RegistrationServer}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param org
	 *            The organization to which the new certifier ID belongs.
	 * @param idFile
	 *            The ID file to be created; specify the full path, for example, c:\notes\data\cert.id.
	 * @param certPassword
	 *            A password for the certifier ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewCertifier(final String org, final String idFile, final String certPassword);

	/**
	 * Creates a certifier ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #getCertifierIDFile() CertifierIDFile}, {@link #getExpiration() Expiration},
	 * {@link #getIDType() IDType}, and {@link #getMinPasswordLength() MinPasswordLength}. {@link #getExpiration() Expiration} defaults to
	 * 100 years from now for organizations and organizational units, and cannot be changed for organizations.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} true to add a Certifier record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} true to attach the ID to the Domino Directory record. These operations
	 * can be performed separately with {@link #addCertifierToAddressBook(String)}.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #setRegistrationServer(String)
	 * RegistrationServer}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param org
	 *            The organization to which the new certifier ID belongs.
	 * @param idFile
	 *            The ID file to be created; specify the full path, for example, c:\notes\data\cert.id.
	 * @param certPassword
	 *            A password for the certifier ID file.
	 * @param country
	 *            A value for the country field in the Domino Directory record.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewCertifier(final String org, final String idFile, final String certPassword, final String country);

	/**
	 * Creates a server ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 100 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int)
	 * MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} true to add a Server record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} true to attach the ID to the Domino Directory record. These operations
	 * can be performed separately with {@link #addServerToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #setRegistrationServer(String)
	 * RegistrationServer}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param server
	 *            A server name for the server ID file.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:\notes\data\server.id.
	 * @param domain
	 *            The domain to which the server belongs.
	 * @param password
	 *            A password for the server ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewServer(final String server, final String idFile, final String domain, final String password);

	/**
	 * Creates a server ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 100 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int)
	 * MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} true to add a Server record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} true to attach the ID to the Domino Directory record. These operations
	 * can be performed separately with {@link #addServerToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #setRegistrationServer(String)
	 * RegistrationServer}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param server
	 *            A server name for the server ID file.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:\notes\data\server.id.
	 * @param domain
	 *            The domain to which the server belongs.
	 * @param serverPassword
	 *            A password for the server ID file.
	 * @param certPassword
	 *            The password of the certifier ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewServer(final String server, final String idFile, final String domain, final String serverPassword,
			final String certPassword);

	/**
	 * Creates a server ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 100 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int)
	 * MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} true to add a Server record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} true to attach the ID to the Domino Directory record. These operations
	 * can be performed separately with {@link #addServerToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #setRegistrationServer(String)
	 * RegistrationServer}, or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param server
	 *            A server name for the server ID file.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:\notes\data\server.id.
	 * @param domain
	 *            The domain to which the server belongs.
	 * @param serverPassword
	 *            A password for the server ID file.
	 * @param certPassword
	 *            The password of the certifier ID file.
	 * @param location
	 *            A value for the location field in the Domino Directory record.
	 * @param comment
	 *            A value for the comment field in the Domino Directory record.
	 * @param network
	 *            The Notes named network (NNN) on which the server runs.
	 * @param admiNname
	 *            The full name of the server administrator.
	 * @param title
	 *            A value for the title field in the Domino Directory record.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewServer(final String server, final String idFile, final String domain, final String serverPassword,
			final String certPassword, final String location, final String comment, final String network, final String adminName,
			final String title);

	/**
	 * Creates a user ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 2 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int) MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} to true to add a Person record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record. These
	 * operations can be performed separately with {@link #addUserToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setCreateMailDb(boolean) CreateMailDb} to true to create a mail file during registration.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name. Optionally set {@link #setAltOrgUnit(Vector)
	 * AltOrgUnit} and {@link #setAltOrgUnitLang(Vector) AltOrgUnitLang}.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param lastName
	 *            A last name for the user to be registered.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:&bsol;notes&bsol;data&bsol;user.id.
	 * @param server
	 *            The canonical name of the server containing the user's mail file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewUser(final String lastName, final String idFile, final String server);

	/**
	 * Creates a user ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 2 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int) MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} to true to add a Person record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record. These
	 * operations can be performed separately with {@link #addUserToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setCreateMailDb(boolean) CreateMailDb} to true to create a mail file during registration.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name. Optionally set {@link #setAltOrgUnit(Vector)
	 * AltOrgUnit} and {@link #setAltOrgUnitLang(Vector) AltOrgUnitLang}.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param lastName
	 *            A last name for the user to be registered.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:&bsol;notes&bsol;data&bsol;user.id.
	 * @param server
	 *            The canonical name of the server containing the user's mail file.
	 * @param firstName
	 *            A first name for the user.
	 * @param middleName
	 *            A middle initial for the user.
	 * @param certPassword
	 *            The password of the certifier ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewUser(final String lastName, final String idFile, final String server, final String firstName,
			final String middleName, final String certPassword);

	/**
	 * Creates a user ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 2 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int) MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} to true to add a Person record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record. These
	 * operations can be performed separately with {@link #addUserToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setCreateMailDb(boolean) CreateMailDb} to true to create a mail file during registration.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name. Optionally set {@link #setAltOrgUnit(Vector)
	 * AltOrgUnit} and {@link #setAltOrgUnitLang(Vector) AltOrgUnitLang}.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param lastName
	 *            A last name for the user to be registered.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:&bsol;notes&bsol;data&bsol;user.id.
	 * @param server
	 *            The canonical name of the server containing the user's mail file.
	 * @param firstName
	 *            A first name for the user.
	 * @param middleName
	 *            A middle initial for the user.
	 * @param certPassword
	 *            The password of the certifier ID file.
	 * @param location
	 *            A value for the location field in the Domino Directory record.
	 * @param comment
	 *            A value for the comment field in the Domino Directory record.
	 * @param mailDBPath
	 *            The path of the user's mail file relative to the mail directory; for example, mail\jones.nsf.
	 * @param forward
	 *            The forwarding domain for the user's mail file.
	 * @param userPassword
	 *            A password for the user ID file.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewUser(final String lastName, final String idFile, final String server, final String firstName,
			final String middleName, final String certPassword, final String location, final String comment, final String mailDBPath,
			final String forward, final String userPassword);

	/**
	 * Creates a user ID and optionally adds it to a Domino Directory.
	 * <p>
	 * Before calling this method, set {@link #setCertifierIDFile(String) CertifierIDFile}, {@link #setExpiration(lotus.domino.DateTime)
	 * Expiration} (defaults to 2 years from now), {@link #setIDType(int) IDType}, and {@link #setMinPasswordLength(int) MinPasswordLength}.
	 * </p>
	 * <p>
	 * Set {@link #setUpdateAddressBook(boolean) UpdateAddressBook} to true to add a Person record to the Domino Directory. Set
	 * {@link #setStoreIDInAddressBook(boolean) StoreIDInAddressBook} to true to attach the ID to the Domino Directory record. These
	 * operations can be performed separately with {@link #addUserToAddressBook(String, String, String)}.
	 * </p>
	 * <p>
	 * Set {@link #setCreateMailDb(boolean) CreateMailDb} to true to create a mail file during registration.
	 * </p>
	 * <p>
	 * Set {@link #setOrgUnit(String) OrgUnit} to add an organizational unit to the ID name. Optionally set {@link #setAltOrgUnit(Vector)
	 * AltOrgUnit} and {@link #setAltOrgUnitLang(Vector) AltOrgUnitLang}.
	 * </p>
	 * <p>
	 * Optionally set {@link #setNorthAmerican(boolean) NorthAmerican} and {@link #setRegistrationLog(String) RegistrationLog}.
	 * </p>
	 * <p>
	 * This method acts on the Domino Directory (names.nsf) for the server specified by {@link #getRegistrationServer() RegistrationServer},
	 * or names.nsf in the local Domino data directory if this property is not set.
	 * </p>
	 *
	 * @param lastName
	 *            A last name for the user to be registered.
	 * @param idFile
	 *            The ID file to be created; specify the complete path, for example, c:&bsol;notes&bsol;data&bsol;user.id.
	 * @param server
	 *            The canonical name of the server containing the user's mail file.
	 * @param firstName
	 *            A first name for the user.
	 * @param middleName
	 *            A middle initial for the user.
	 * @param certPassword
	 *            The password of the certifier ID file.
	 * @param location
	 *            A value for the location field in the Domino Directory record.
	 * @param comment
	 *            A value for the comment field in the Domino Directory record.
	 * @param mailDBPath
	 *            The path of the user's mail file relative to the mail directory; for example, mail&bsol;jones.nsf.
	 * @param forward
	 *            The forwarding domain for the user's mail file.
	 * @param userPassword
	 *            A password for the user ID file.
	 * @param altName
	 *            An alternate user name.
	 * @param altNameLang
	 *            The language for the alternate user name. See &commat;Locale in the formula language for a list of language codes.
	 * @return True if the operation is successful; false otherwise.
	 */
	@Override
	public boolean registerNewUser(final String lastName, final String idFile, final String server, final String firstName,
			final String middleName, final String certPassword, final String location, final String comment, final String mailDBPath,
			final String forward, final String userPassword, final String altName, final String altNameLang);

	/**
	 * Sets the alternate names for the organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param names
	 *            new alternate names for the organizational unit
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setAltOrgUnit(final Vector names);

	/**
	 * Sets the alternate name language for the organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param languages
	 *            new alternate name languages
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setAltOrgUnitLang(final Vector languages);

	/**
	 * Sets the complete file specification of the certifier ID to use when creating IDs.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param idFile
	 *            complete path to the ID file, for example, c:&bsol;notes&bsol;data&bsol;cert.id.
	 *
	 */
	@Override
	public void setCertifierIDFile(final String idFile);

	/**
	 * Sets the certifier when a Notes ID is not being used.
	 * <p>
	 * Optionally set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * If {@link #isUseCertificateAuthority() UseCertificateAuthority} is true, this property must specify the name of a CA-enabled
	 * certifier.
	 * </p>
	 * <p>
	 * If you are working in a hosted environment and registering a user to a hosted organization, use this property to specify a certifier
	 * created for the hosted organization.
	 * </p>
	 *
	 * @param name
	 *            The certifier when a Notes ID is not being used.
	 */
	@Override
	public void setCertifierName(final String name);

	/**
	 * Sets whether a mail database is created with the ID file when calling {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * <p>
	 * Set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param flag
	 *            specify true if the mail database should be created
	 */
	@Override
	public void setCreateMailDb(final boolean flag);

	/**
	 * Sets whether a short name must be unique.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser}.
	 * </p>
	 *
	 * @param flag
	 *            specify true when a short name must be unique.
	 */
	@Override
	public void setEnforceUniqueShortName(final boolean flag);

	/**
	 * Sets the expiration date to use when creating ID files.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param expiration
	 *            The expiration date to use when creating ID files, can be null.
	 */
	@Override
	public void setExpiration(final lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Registration#setForeignDN(java.lang.String)
	 */
	@Override
	public void setForeignDN(final String dn);

	/**
	 * Sets the groups to which a user is assigned during registration.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param groups
	 *            The groups to which a user is assigned during registration.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setGroupList(final Vector groups);

	/**
	 * Sets the type of ID file to create.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param type
	 *            The type of ID file to create. One of:
	 *            <ul>
	 *            <li>Registration.ID_FLAT to create a flat ID</li>
	 *            <li>Registration.ID_HIERARCHICAL to create a hierarchical ID</li>
	 *            <li>Registration.ID_CERTIFIER to create an ID that depends on whether the certifier ID is flat or hierarchical</li>
	 *            </ul>
	 */
	@Override
	public void setIDType(final int type);

	/**
	 * Sets a name that is assigned Manager access in the mail database ACL.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * The mail database owner may or may not be a Manager depending on {@link #getMailOwnerAccess() MailOwnerAccess} property.
	 * </p>
	 *
	 * @param name
	 *            A name that is assigned Manager access in the mail database ACL.
	 */
	@Override
	public void setMailACLManager(final String name);

	/**
	 * Sets whether a full-text index is created for the mail database.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param flag
	 *            specify true if the full-text index should be created for the mail database.
	 */
	@Override
	public void setMailCreateFTIndex(final boolean flag);

	/**
	 * Sets the user's Internet name for sending and receiving mail.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property remains an empty string, the registration process generates an Internet name.
	 * </p>
	 *
	 * @param address
	 *            The user's Internet name for sending and receiving mail.
	 */
	@Override
	public void setMailInternetAddress(final String address);

	/**
	 * Sets the mail database ACL setting for the owner.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param access
	 *            The mail database ACL setting for the owner.
	 */
	@Override
	public void setMailOwnerAccess(final int access);

	/**
	 * Sets the maximum size of the user's mail database, in megabytes.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param limit
	 *            The maximum size of the user's mail database, in megabytes or 0 for no limit
	 */
	@Override
	public void setMailQuotaSizeLimit(final int limit);

	/**
	 * Sets the size, in megabytes, at which the user's mail database issues a warning that it is getting too large.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param threshold
	 *            The size, in megabytes, or 0 for no warning threshold
	 */
	@Override
	public void setMailQuotaWarningThreshold(final int threshold);

	/**
	 * Sets the names of servers to which the mail database will replicate.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * This property applies only to clustered servers.
	 * </p>
	 *
	 * @param servers
	 *            The names of servers to which the mail database will replicate.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setMailReplicaServers(final Vector servers);

	/**
	 * Sets the user's mail system.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param system
	 *            The user's mail system. One of:
	 *            <ul>
	 *            <li>Registration.REG_MAILSYSTEM_IMAP (2)</li>
	 *            <li>Registration.REG_MAILSYSTEM_INOTES (3)</li>
	 *            <li>Registration.REG_MAILSYSTEM_INTERNET (4)</li>
	 *            <li>Registration.REG_MAILSYSTEM_NONE (6)</li>
	 *            <li>Registration.REG_MAILSYSTEM_NOTES (0) (default)</li>
	 *            <li>Registration.REG_MAILSYSTEM_OTHER (5)</li>
	 *            <li>Registration.REG_MAILSYSTEM_POP (1)</li>
	 *            </ul>
	 */
	@Override
	public void setMailSystem(final int system);

	/**
	 * Sets the name of the template for the design of the mail file.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param name The name of the template for the design of the mail file or an empty string for a standard template
	 */
	@Override
	public void setMailTemplateName(final String name);

	/**
	 * Sets the strength assigned to a password in an ID file.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * This property defines the password strength required for subsequent changes to the password by the user. A password assigned in the
	 * methods listed above is not initially checked against the strength.
	 * </p>
	 * <p>
	 * Domino measures a password's strength and security according to the level assigned on its password quality scale. The scale ranges
	 * from Weak to Strong or from 0 (lowest - no password required) to 16 (highest). For more information about the password quality scale,
	 * see "Understanding the password quality scale" in <em class="ph i">Administering the Domino System</em>.
	 * </p>
	 * <div>
	 * <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 * <thead align="left" valign="bottom">
	 * <tr>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e120">
	 * <p>
	 * Password quality scale
	 * </p>
	 * </th>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e125">
	 * <p>
	 * Description
	 * </p>
	 * </th>
	 * <th align="left" valign="top" width="NaN%" id="d1990005e130">
	 * <p>
	 * Example
	 * </p>
	 * </th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 0
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Password is optional.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * n/a
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 1
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Allow any password.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "b", "3"
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 2-6
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Allow a weak password, even though you might be able to guess it by trial and error.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "password", "doughnut" (password quality scale 3)
	 * </p>
	 * <p>
	 * "lightferret", "b 4D" (password quality scale 6)
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 7-12
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Require a password that is difficult to guess, but might be vulnerable to an automated attack.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "pqlrtmxr", "wefourkings" (password quality scale 8)
	 * </p>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e120 ">
	 * <p>
	 * 13-16
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e125 ">
	 * <p>
	 * Require a strong password, even though the user may have difficulty remembering it.
	 * </p>
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d1990005e130 ">
	 * <p>
	 * "4891spyONu" (password quality scale 13)
	 * </p>
	 * <p>
	 * "lakestreampondriverocean", "stRem2pO()" (password quality scale 15)
	 * </p>
	 * <p>
	 * "stream8pond1river7lake2ocean" (password quality scale 16)
	 * </p>
	 * </td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * </div>
	 *
	 * @param length
	 *            The strength assigned to a password in an ID file.
	 */
	@Override
	public void setMinPasswordLength(final int length);

	/**
	 * Sets whether to create an ID file during registration or not
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If you set this property true, leave {@link #getStoreIDInAddressBook() StoreIDInAddressBook} false.
	 * </p>
	 *
	 * @param flag
	 *            true if no ID file should be created during registration.
	 */
	@Override
	public void setNoIDFile(final boolean flag);

	/**
	 * Sets whether an ID file is North American.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param flag
	 *            true if an ID file is North American.
	 */
	@Override
	public void setNorthAmerican(final boolean flag);

	/**
	 * Sets the organizational unit to use when creating ID files.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param unit
	 *            The organizational unit to use when creating ID files.
	 */
	@Override
	public void setOrgUnit(final String unit);

	/**
	 * Sets the name of an explicit policy.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 *
	 * @param name
	 *            The name of an explicit policy.
	 */
	@Override
	public void setPolicyName(final String name);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Registration#setPublicKeySize(int)
	 */
	@Override
	public void setPublicKeySize(final int size);

	/**
	 * The log file to use when creating IDs.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}. No logging occurs if this parameter is null. If this parameter is anything other than null, logging goes to the
	 * file named cerlog.nsf in the Domino data directory on the registration server.
	 * </p>
	 *
	 * @param name
	 *            log file or null
	 */
	@Override
	public void setRegistrationLog(final String name);

	/**
	 * Sets the server to use when creating IDs and performing other registration functions.
	 * <p>
	 * Set this property before calling any method except {@link #switchToID(String, String)} method.
	 * </p>
	 * <p>
	 * If you don't set this property, the local Domino or Notes directory is assumed.
	 * </p>
	 * <p>
	 * This property is used to:
	 * </p>
	 * <ul>
	 * <li>Attach an ID to the user, server, or certifier document in the Domino Directory.</li>
	 * <li>Create a mail database for a new user.</li>
	 * </ul>
	 *
	 * @param server
	 *            The server to use when creating IDs and performing other registration functions.
	 */
	@Override
	public void setRegistrationServer(final String server);

	/**
	 * Sets the interval in days for cleaning up data on Notes clients set up for roaming users.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 * <p>
	 * {@link #getRoamingCleanupSetting() RoamingCleanupSetting} must be Registration.REG_ROAMING_CLEANUP_EVERY_NDAYS.
	 * </p>
	 *
	 * @param period
	 *            The interval in days for cleaning up data on Notes clients set up for roaming users.
	 */
	@Override
	public void setRoamingCleanupPeriod(final int period);

	/**
	 * Sets the clean-up process for data on Notes clients set up for roaming users.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 * <p>
	 * Legal values:
	 * </p>
	 * <dl>
	 * <dt>Registration.REG_ROAMING_CLEANUP_AT_SHUTDOWN (2)</dt>
	 * <dd>Indicates that roaming data is deleted upon Notes shutdown.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_EVERY_NDAYS (1)</dt>
	 * <dd>Indicates that roaming data is deleted every so many days as specified by {@link #getRoamingCleanupPeriod()
	 * RoamingCleanupPeriod}.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_NEVER (0) (default)</dt>
	 * <dd>Indicates that roaming data is never deleted.</dd>
	 * <dt>Registration.REG_ROAMING_CLEANUP_PROMPT (3)</dt>
	 * <dd>Indicates that a user is prompted upon exiting Notes. The user can choose an individual clean-up or not. The user can also
	 * decline being prompted in the future.</dd>
	 * </dl>
	 *
	 * @param setting
	 *            the clean-up process for data on Notes clients set up for roaming users.
	 */
	@Override
	public void setRoamingCleanupSetting(final int setting);

	/**
	 * Sets the server on which the user's roaming data is stored.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 *
	 * @param server
	 *            The server on which the user's roaming data is stored or an empty string to use the user's mail server
	 */
	@Override
	public void setRoamingServer(final String server);

	/**
	 * Sets the subdirectory that contains the user's roaming data.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * {@link #isRoamingUser() RoamingUser} must be true.
	 * </p>
	 *
	 * @param dirPath
	 *            The subdirectory that contains the user's roaming data.
	 */
	@Override
	public void setRoamingSubdir(final String dirPath);

	/**
	 * Sets whether a user is a roaming user.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property is true, you can apply the following properties: {@link #getRoamingCleanupPeriod() RoamingCleanupPeriod},
	 * {@link #getRoamingCleanupSetting() RoamingCleanupSetting}, {@link #getRoamingServer() RoamingServer}, and {@link #getRoamingSubdir()
	 * RoamingSubdir}.
	 * </p>
	 *
	 * @param flag
	 *            True if a user is a roaming user.
	 */
	@Override
	public void setRoamingUser(final boolean flag);

	/**
	 * Sets the short name when creating user IDs.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * By default the short name is the first letter of the first name followed by the last name.
	 * </p>
	 *
	 * @param shortName
	 *            The short name when creating user IDs.
	 */
	@Override
	public void setShortName(final String shortName);

	/**
	 * Sets whether the ID file is stored in the server's Domino Directory.
	 * <p>
	 * Set this property before calling {@link #addCertifierToAddressBook(String)}, {@link #addServerToAddressBook(String, String, String)},
	 * {@link #addUserToAddressBook(String, String, String)}, {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 *
	 * @param flag
	 *            true if the ID file will be stored in the server's Domino Directory.
	 */
	@Override
	public void setStoreIDInAddressBook(final boolean flag);

	/**
	 * Sets whether the ID file is stored in the user's mail file.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * This property applies only to Domino Web Access and allows Notes users to read their encrypted mail while using Domino Web Access.
	 * </p>
	 *
	 * @param flag
	 *            True if the ID file is stored in the user's mail file.
	 */
	@Override
	public void setStoreIDInMailfile(final boolean flag);

	/**
	 * Sets if the the user's Internet password should be synchronized with the password for the Notes client ID.
	 * <p>
	 * Optionally set this property before calling {@link #registerNewUser(String, String, String) registerNewUser()}.
	 * </p>
	 * <p>
	 * The Internet password is in the user's Person document in the Domino Directory.
	 * </p>
	 * <p>
	 * If the user changes the password for the Notes client ID, the Internet password automatically (but not immediately) changes to match
	 * it.
	 * </p>
	 *
	 * @param flag
	 *            True if the user's Internet password will be synchronized with the password for the Notes client ID.
	 */
	@Override
	public void setSynchInternetPassword(final boolean flag);

	/**
	 * Sets whether an ID document in the Domino Directory is created when the ID file is created.
	 * <p>
	 * Set this property before calling {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}. You can create a Domino Directory document later with {@link #addCertifierToAddressBook(String)},
	 * {@link #addServerToAddressBook(String, String, String)}, or {@link #addUserToAddressBook(String, String, String)}.
	 * </p>
	 *
	 * @param flag
	 *            True If an ID document in the Domino Directory is created when the ID file is created.
	 */
	@Override
	public void setUpdateAddressBook(final boolean flag);

	/**
	 * Sets whether the certificate authority (CA) is used for registration.
	 * <p>
	 * Set this property before calling {@link #crossCertify(String)}, {@link #recertify(String)},
	 * {@link #registerNewCertifier(String, String, String) registerNewCertifier()},
	 * {@link #registerNewServer(String, String, String, String) registerNewServer()}, or {@link #registerNewUser(String, String, String)
	 * registerNewUser()}.
	 * </p>
	 * <p>
	 * If this property is true, {@link #getCertifierName() CertifierName} must specify the name of a CA-enabled certifier.
	 * </p>
	 * <p>
	 * If this property is true, the certifier ID and password are not needed for registration.
	 * </p>
	 * <p>
	 * If you are working in a hosted environment and are registering a user to a hosted organization, be sure to register the user with a
	 * certifier created for that hosted organization.
	 * </p>
	 *
	 * @param True
	 *            if the certificate authority (CA) is used for registration.
	 */
	@Override
	public void setUseCertificateAuthority(final boolean flag);

	/**
	 * Switches to a different ID file.
	 * <p>
	 * This method works only on a client.
	 * </p>
	 *
	 * @param idFile
	 *            The ID file to which you want to switch; specify the complete path, for example, c:\\notes\\data\\user.id.
	 * @param userPassword
	 *            The user's password.
	 * @return The user name that corresponds to the ID file to which you have switched.
	 */
	@Override
	public String switchToID(final String idFile, final String userPassword);
}
