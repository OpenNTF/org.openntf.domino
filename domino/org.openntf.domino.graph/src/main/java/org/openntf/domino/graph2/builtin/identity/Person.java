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
package org.openntf.domino.graph2.builtin.identity;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.Editor;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Person")
public interface Person extends Name, Editor {
	@TypedProperty("FirstName")
	public String getFirstName();

	@TypedProperty("FirstName")
	public void setFirstName(String firstName);

	@TypedProperty("LastName")
	public String getLastName();

	@TypedProperty("LastName")
	public void setLastName(String lastName);

	@TypedProperty("FullName")
	public String[] getFullNames();

	@TypedProperty("FullName")
	public void setFullNames(String[] fullName);

	@TypedProperty("Assistant")
	public String getAssistant();

	@TypedProperty("Assistant")
	public void setAssistant(String assistant);

	@TypedProperty("CellPhoneNumber")
	public String getCellPhoneNumber();

	@TypedProperty("CellPhoneNumber")
	public void setCellPhoneNumber(String cellnumber);

	@TypedProperty("City")
	public String getCity();

	@TypedProperty("City")
	public void setCity(String city);

	@TypedProperty("ClientType")
	public String getClientType();

	@TypedProperty("ClientType")
	public void setClientType(String clientType);

	@TypedProperty("Comment")
	public String getComment();

	@TypedProperty("Comment")
	public void setComment(String comment);

	@TypedProperty("CompanyName")
	public String getCompanyName();

	@TypedProperty("CompanyName")
	public void setCompanyName(String companyName);

	@TypedProperty("Manager")
	public String getManager();

	@TypedProperty("Manager")
	public void setManager(String manager);

	@TypedProperty("Country")
	public String getCountry();

	@TypedProperty("Country")
	public void setCountry(String country);

	@TypedProperty("Department")
	public String getDepartment();

	@TypedProperty("Department")
	public void setDepartment(String department);

	@TypedProperty("EmployeeID")
	public String getEmployeeId();

	@TypedProperty("EmployeeID")
	public void setEmployeeId(String employeeId);

	@TypedProperty("EmployeeNumber")
	public String getEmployeeNumber();

	@TypedProperty("EmployeeNumber")
	public void setEmployeeNumber(String employeeNumber);

	@TypedProperty("EncryptIncomingMail")
	public boolean getEncryptIncomingMail();

	@TypedProperty("EncryptIncomingMail")
	public void setEncryptIncomingMail(boolean encryptIncoming);

	@TypedProperty("HTTPPassword")
	public String getHttpPassword();

	@TypedProperty("HTTPPassword")
	public void setHttpPassword(String password);

	@TypedProperty("HTTPPasswordForceChange")
	public boolean getHttpPasswordForceChange();

	@TypedProperty("HTTPPasswordForceChange")
	public void setHttpPasswordForceChange(boolean forceChange);

	@TypedProperty("INetPublicKey")
	public String getInetPublicKey();

	@TypedProperty("INetPublicKey")
	public void setInetPublicKey(String publicKey);

	@TypedProperty("InternetAddress")
	public String getInternetAddress();

	@TypedProperty("InternetAddress")
	public void setInternetAddress(String internetAddress);

	@TypedProperty("JobTitle")
	public String getJobTitle();

	@TypedProperty("JobTitle")
	public void setJobTitle(String jobTitle);

	@TypedProperty("Location")
	public String getLocation();

	@TypedProperty("Location")
	public void setLocation(String location);

	@TypedProperty("MailAddress")
	public String getMailAddress();

	@TypedProperty("MailAddress")
	public void setMailAddress(String mailAddress);

	@TypedProperty("MailDomain")
	public String getMailDomain();

	@TypedProperty("MailDomain")
	public void setMailDomain(String mailDomain);

	@TypedProperty("MailFile")
	public String getMailFile();

	@TypedProperty("MailFile")
	public void setMailFile(String mailFile);

	@TypedProperty("MailServer")
	public String getMailServer();

	@TypedProperty("MailServer")
	public void setMailServer(String mailServer);

	@TypedProperty("MailSystem")
	public String getMailSystem();

	@TypedProperty("MailSystem")
	public void setMailSystem(String mailSystem);

	@TypedProperty("MiddleInitial")
	public String getMiddleInitial();

	@TypedProperty("MiddleInitial")
	public void setMiddleInitial(String initial);

	@TypedProperty("OfficePhoneNumber")
	public String getOfficePhoneNumber();

	@TypedProperty("OfficePhoneNumber")
	public void setOfficePhoneNumber(String officePhone);

	@TypedProperty("Owner")
	public String getOwner();

	@TypedProperty("Owner")
	public void setOwner(String owner);

	@TypedProperty("officeZip")
	public String getOfficeZip();

	@TypedProperty("officeZip")
	public void setOfficeZip(String officeZip);

	@TypedProperty("officeStreetAddress")
	public String getOfficeStreetAddress();

	@TypedProperty("officeStreetAddress")
	public void setOfficeStreetAddress(String officeStreetAddress);

	@TypedProperty("officeState")
	public String getOfficeState();

	@TypedProperty("officeState")
	public void setOfficeState(String officeState);

	@TypedProperty("officeCity")
	public String getOfficeCity();

	@TypedProperty("officeCity")
	public void setOfficeCity(String officeCity);

	@TypedProperty("officeCountry")
	public String getOfficeCountry();

	@TypedProperty("officeCountry")
	public void setOfficeCountry(String officeCountry);

	@TypedProperty("PhoneNumber")
	public String getPhoneNumber();

	@TypedProperty("PhoneNumber")
	public void setPhoneNumber(String phoneNumber);

	@TypedProperty("PasswordChangeInterval")
	public int getPasswordChangeInterval();

	@TypedProperty("PasswordChangeInterval")
	public void setPasswordChangeInterval(int changeInterval);

	@TypedProperty("PasswordGracePeriod")
	public int getPasswordGracePeriod();

	@TypedProperty("PasswordGracePeriod")
	public void setPasswordGracePeriod(int gracePeriod);

	@TypedProperty("PhotoURL")
	public String getPhotoUrl();

	@TypedProperty("PhotoURL")
	public void setPhotoUrl(String url);

	//TODO NTF this should be a relationship, not a property
	@TypedProperty("Policy")
	public String getPolicy();

	@TypedProperty("Policy")
	public void setPolicy(String policy);

	//TODO NTF this should be a relationship, not a property
	@TypedProperty("Profiles")
	public String getProfiles();

	@TypedProperty("Profiles")
	public void setProfiles(String profiles);

	@TypedProperty("PostalAddress")
	public String getPostalAddress();

	@TypedProperty("PostalAddress")
	public void setPostalAddress(String postalAddress);

	@TypedProperty("PublicKey")
	public String getPublicKey();

	@TypedProperty("PublicKey")
	public void setPublicKey(String publicKey);

	@TypedProperty("roomNumber")
	public String getRoomNumber();

	@TypedProperty("roomNumber")
	public void setRoomNumber(String roomNumber);

	@TypedProperty("SametimeServer")
	public String getSametimeServer();

	@TypedProperty("SametimeServer")
	public void setSametimeServer(String sametimeServer);

	@TypedProperty("ShortName")
	public String getShortName();

	@TypedProperty("ShortName")
	public void setShortName(String shortName);

	@TypedProperty("State")
	public String getState();

	@TypedProperty("State")
	public void setState(String state);

	@TypedProperty("Street")
	public String getStreet();

	@TypedProperty("Street")
	public void setStreet(String street);

	@TypedProperty("StreetAddress")
	public String getStreetAddress();

	@TypedProperty("StreetAddress")
	public void setStreetAddress(String streetAddress);

	@TypedProperty("Suffix")
	public String getSuffix();

	@TypedProperty("Suffix")
	public void setSuffix(String suffix);

	@TypedProperty("Title")
	public String getTitle();

	@TypedProperty("Title")
	public void setTitle(String title);

	@TypedProperty("WebSite")
	public String getWebSite();

	@TypedProperty("WebSite")
	public void setWebSite(String website);

	@TypedProperty("ZIP")
	public String getZip();

	@TypedProperty("ZIP")
	public void setZip(String ZIP);

	@TypeValue(ReportsTo.LABEL)
	public abstract static interface ReportsTo extends DEdgeFrame {
		public static final String LABEL = "ReportsTo"; //$NON-NLS-1$

		@OutVertex
		public Person getSupervisor();

		@InVertex
		public Person getWorker();
	}

	@AdjacencyUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public Person getReportsToSupervisor();

	@AdjacencyUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public ReportsTo addReportsToSupervisor(Person supervisor);

	@AdjacencyUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public void removeReportsToSupervisor(Person supervisor);

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public ReportsTo getReportsTo();

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public int countReportsTo();

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public void removeReportsTo(ReportsTo supervisor);

	@AdjacencyUnique(label = ReportsTo.LABEL)
	public List<Person> getReportsToWorkers();

	@AdjacencyUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public ReportsTo addReportsToWorker(Person worker);

	@AdjacencyUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public void removeReportsToWorker(Person worker);

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public List<ReportsTo> getReportsToSubs();

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public int countReportsToSubs();

	@IncidenceUnique(label = ReportsTo.LABEL, direction = Direction.OUT)
	public void removeReportsToSub(ReportsTo supervisor);

}
