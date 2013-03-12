package org.openntf.domino;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import lotus.domino.AdministrationProcess;
import lotus.domino.AgentContext;
import lotus.domino.ColorObject;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Directory;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.DxlImporter;
import lotus.domino.International;
import lotus.domino.Log;
import lotus.domino.Newsletter;
import lotus.domino.NotesCalendar;
import lotus.domino.PropertyBroker;
import lotus.domino.Registration;
import lotus.domino.RichTextParagraphStyle;
import lotus.domino.RichTextStyle;
import lotus.domino.Stream;

import org.openntf.domino.annotations.Legacy;

public interface Session extends lotus.domino.Session, Base<lotus.domino.Session> {
	@Override
	public Vector<Object> evaluate(String formula);

	@Override
	public Vector<Object> evaluate(String formula, Document doc);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Database> getAddressBooks();

	public Collection<Database> getAddressBookCollection();

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<DateRange> freeTimeSearch(lotus.domino.DateRange window, int duration, Object names, boolean firstFit);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange window, int duration, String names, boolean firstFit);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange window, int duration, Collection<String> names,
			boolean firstFit);

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so

	// there's no recycle burden?

	public Collection<String> getUserGroupNameCollection();

	@Override
	@Deprecated
	@Legacy( { Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's

	// no recycle burden?

	public Collection<String> getUserNameCollection();

	@Override
	public AdministrationProcess createAdministrationProcess(String server);

	@Override
	public ColorObject createColorObject();

	@Override
	public DateRange createDateRange();

	@Override
	public DateRange createDateRange(Date startTime, Date endTime);

	@Override
	public DateRange createDateRange(DateTime startTime, DateTime endTime);

	@Override
	public DateTime createDateTime(Calendar date);

	@Override
	public DateTime createDateTime(Date date);

	@Override
	public DateTime createDateTime(String date);

	@Override
	public DxlExporter createDxlExporter();

	@Override
	public DxlImporter createDxlImporter();

	@Override
	public Log createLog(String name);

	@Override
	public Name createName(String name);

	@Override
	public Name createName(String name, String lang);

	@Override
	public Newsletter createNewsletter(DocumentCollection collection);

	@Override
	public Registration createRegistration();

	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle();

	@Override
	public RichTextStyle createRichTextStyle();

	@Override
	public Stream createStream();

	@Override
	public AgentContext getAgentContext();

	@Override
	public NotesCalendar getCalendar(lotus.domino.Database db);

	@Override
	public String getCommonUserName();

	@Override
	public Object getCredentials();

	@Override
	public Database getCurrentDatabase();

	@Override
	public Database getDatabase(String server, String db);

	@Override
	public Database getDatabase(String server, String db, boolean createOnFail);

	@Override
	public DbDirectory getDbDirectory(String server);

	@Override
	public Directory getDirectory();

	@Override
	public Directory getDirectory(String server);

	@Override
	public String getEffectiveUserName();

	@Override
	public String getEnvironmentString(String vname);

	@Override
	public String getEnvironmentString(String vname, boolean isSystem);

	@Override
	public Object getEnvironmentValue(String vname);

	@Override
	public Object getEnvironmentValue(String vname, boolean isSystem);

	@Override
	public String getHttpURL();

	@Override
	public International getInternational();

	@Override
	public String getNotesVersion();

	@Override
	public String getOrgDirectoryPath();

	@Override
	public String getPlatform();

	@Override
	public PropertyBroker getPropertyBroker();

	@Override
	public String getServerName();

	@Override
	public String getSessionToken();

	@Override
	public String getSessionToken(String serverName);

	@Override
	public String getURL();

	@Override
	public Database getURLDatabase();

	@Override
	public String getUserName();

	@Override
	public Name getUserNameObject();

	@Override
	public Document getUserPolicySettings(String server, String name, int type);

	@Override
	public Document getUserPolicySettings(String server, String name, int type, String explicitPolicy);

	@Override
	public String hashPassword(String password);

	@Override
	public boolean isConvertMime();

	@Override
	public boolean isConvertMIME();

	@Override
	public boolean isOnServer();

	@Override
	public boolean isRestricted();

	@Override
	public boolean isTrackMillisecInJavaDates();

	@Override
	public boolean isTrustedSession();

	@Override
	public boolean isValid();

	@Override
	public boolean resetUserPassword(String serverName, String userName, String password);

	@Override
	public boolean resetUserPassword(String serverName, String userName, String password, int downloadCount);

	@Override
	public Base resolve(String url);

	@Override
	public String sendConsoleCommand(String serverName, String consoleCommand);

	@Override
	public void setAllowLoopBack(boolean flag);

	@Override
	public void setConvertMime(boolean flag);

	@Override
	public void setConvertMIME(boolean flag);

	@Override
	public void setEnvironmentVar(String vname, Object value);

	@Override
	public void setEnvironmentVar(String vname, Object value, boolean isSystem);

	@Override
	public void setTrackMillisecInJavaDates(boolean flag);

	@Override
	public boolean verifyPassword(String password, String hashedPassword);
}
