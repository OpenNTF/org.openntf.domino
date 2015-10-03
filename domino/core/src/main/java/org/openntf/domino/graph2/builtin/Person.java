package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.social.Socializer;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Person")
public interface Person extends DVertexFrame, Socializer, Editor {
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

}
