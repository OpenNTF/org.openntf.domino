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
	public Vector<Object> evaluate(String arg0);

	@Override
	public Vector<Object> evaluate(String arg0, Document arg1);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Database> getAddressBooks();

	public Collection<Database> getAddressBookCollection();

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<DateRange> freeTimeSearch(lotus.domino.DateRange arg0, int arg1, Object arg2, boolean arg3);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, String arg2, boolean arg3);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, Collection<String> arg2, boolean arg3);

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so
												// there's no recycle burden?

	public Collection<String> getUserGroupNameCollection();

	@Override
	@Deprecated
	@Legacy({ Legacy.INTERFACES_WARNING })
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's
											// no recycle burden?

	public Collection<String> getUserNameCollection();

	@Override
	public AdministrationProcess createAdministrationProcess(String paramString);

	@Override
	public ColorObject createColorObject();

	@Override
	public lotus.domino.DateRange createDateRange();

	@Override
	public lotus.domino.DateRange createDateRange(Date paramDate1, Date paramDate2);

	@Override
	public lotus.domino.DateRange createDateRange(DateTime paramDateTime1, DateTime paramDateTime2);

	@Override
	public DateTime createDateTime(Calendar paramCalendar);

	@Override
	public DateTime createDateTime(Date paramDate);

	@Override
	public DateTime createDateTime(String paramString);

	@Override
	public DxlExporter createDxlExporter();

	@Override
	public DxlImporter createDxlImporter();

	@Override
	public Log createLog(String paramString);

	@Override
	public lotus.domino.Name createName(String paramString);

	@Override
	public lotus.domino.Name createName(String paramString1, String paramString2);

	@Override
	public Newsletter createNewsletter(DocumentCollection paramDocumentCollection);

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
	public NotesCalendar getCalendar(lotus.domino.Database paramDatabase);

	@Override
	public String getCommonUserName();

	@Override
	public Object getCredentials();

	@Override
	public lotus.domino.Database getCurrentDatabase();

	@Override
	public org.openntf.domino.Database getDatabase(String paramString1, String paramString2);

	@Override
	public lotus.domino.Database getDatabase(String paramString1, String paramString2, boolean paramBoolean);

	@Override
	public DbDirectory getDbDirectory(String paramString);

	@Override
	public Directory getDirectory();

	@Override
	public Directory getDirectory(String paramString);

	@Override
	public String getEffectiveUserName();

	@Override
	public String getEnvironmentString(String paramString);

	@Override
	public String getEnvironmentString(String paramString, boolean paramBoolean);

	@Override
	public Object getEnvironmentValue(String paramString);

	@Override
	public Object getEnvironmentValue(String paramString, boolean paramBoolean);

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
	public String getSessionToken(String paramString);

	@Override
	public String getURL();

	@Override
	public lotus.domino.Database getURLDatabase();

	@Override
	public String getUserName();

	@Override
	public lotus.domino.Name getUserNameObject();

	@Override
	public Document getUserPolicySettings(String paramString1, String paramString2, int paramInt);

	@Override
	public Document getUserPolicySettings(String paramString1, String paramString2, int paramInt, String paramString3);

	@Override
	public String hashPassword(String paramString);

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
	public boolean resetUserPassword(String paramString1, String paramString2, String paramString3);

	@Override
	public boolean resetUserPassword(String paramString1, String paramString2, String paramString3, int paramInt);

	@Override
	public lotus.domino.Base resolve(String paramString);

	@Override
	public String sendConsoleCommand(String paramString1, String paramString2);

	@Override
	public void setAllowLoopBack(boolean paramBoolean);

	@Override
	public void setConvertMime(boolean paramBoolean);

	@Override
	public void setConvertMIME(boolean paramBoolean);

	@Override
	public void setEnvironmentVar(String paramString, Object paramObject);

	@Override
	public void setEnvironmentVar(String paramString, Object paramObject, boolean paramBoolean);

	@Override
	public void setTrackMillisecInJavaDates(boolean paramBoolean);

	@Override
	public boolean verifyPassword(String paramString1, String paramString2);
}
