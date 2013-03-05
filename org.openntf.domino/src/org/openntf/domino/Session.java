/**
 * 
 */
package org.openntf.domino;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import lotus.domino.AdministrationProcess;
import lotus.domino.AgentContext;
import lotus.domino.Base;
import lotus.domino.ColorObject;
import lotus.domino.Database;
import lotus.domino.DateRange;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Directory;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DxlExporter;
import lotus.domino.DxlImporter;
import lotus.domino.International;
import lotus.domino.Log;
import lotus.domino.Name;
import lotus.domino.Newsletter;
import lotus.domino.NotesCalendar;
import lotus.domino.NotesException;
import lotus.domino.PropertyBroker;
import lotus.domino.Registration;
import lotus.domino.RichTextParagraphStyle;
import lotus.domino.RichTextStyle;
import lotus.domino.Stream;

/**
 * @author nfreeman
 * 
 */
public class Session implements lotus.domino.Session {
	private lotus.domino.local.Session delegate_;

	/**
	 * 
	 */
	public Session() {
		// TODO Auto-generated constructor stub
	}

	private lotus.domino.local.Session getDelegate() {
		// TODO add some protections here.
		return delegate_;
	}

	@Override
	public AdministrationProcess createAdministrationProcess(String arg0) throws NotesException {
		return getDelegate().createAdministrationProcess(arg0);
	}

	@Override
	public ColorObject createColorObject() throws NotesException {
		return getDelegate().createColorObject();
	}

	@Override
	public DateRange createDateRange() throws NotesException {
		return getDelegate().createDateRange();
	}

	@Override
	public DateRange createDateRange(Date arg0, Date arg1) throws NotesException {
		return getDelegate().createDateRange(arg0, arg1);
	}

	@Override
	public DateRange createDateRange(DateTime arg0, DateTime arg1) throws NotesException {
		return getDelegate().createDateRange(arg0, arg1);
	}

	@Override
	public DateTime createDateTime(Calendar arg0) throws NotesException {
		return getDelegate().createDateTime(arg0);
	}

	@Override
	public DateTime createDateTime(Date arg0) throws NotesException {
		return getDelegate().createDateTime(arg0);
	}

	@Override
	public DateTime createDateTime(String arg0) throws NotesException {
		return getDelegate().createDateTime(arg0);
	}

	@Override
	public DxlExporter createDxlExporter() throws NotesException {
		return getDelegate().createDxlExporter();
	}

	@Override
	public DxlImporter createDxlImporter() throws NotesException {
		return getDelegate().createDxlImporter();
	}

	@Override
	public Log createLog(String arg0) throws NotesException {
		return getDelegate().createLog(arg0);
	}

	@Override
	public Name createName(String arg0, String arg1) throws NotesException {
		return getDelegate().createName(arg0, arg1);
	}

	@Override
	public Name createName(String arg0) throws NotesException {
		return getDelegate().createName(arg0);
	}

	@Override
	public Newsletter createNewsletter(DocumentCollection arg0) throws NotesException {
		return getDelegate().createNewsletter(arg0);
	}

	@Override
	public Registration createRegistration() throws NotesException {
		return getDelegate().createRegistration();
	}

	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle() throws NotesException {
		return getDelegate().createRichTextParagraphStyle();
	}

	@Override
	public RichTextStyle createRichTextStyle() throws NotesException {
		return getDelegate().createRichTextStyle();
	}

	@Override
	public Stream createStream() throws NotesException {
		return getDelegate().createStream();
	}

	@Override
	public Vector evaluate(String arg0, Document arg1) throws NotesException {
		return getDelegate().evaluate(arg0, arg1);
	}

	@Override
	public Vector evaluate(String arg0) throws NotesException {
		return getDelegate().evaluate(arg0);
	}

	@Override
	public Vector freeTimeSearch(DateRange arg0, int arg1, Object arg2, boolean arg3) throws NotesException {
		return getDelegate().freeTimeSearch(arg0, arg1, arg2, arg3);
	}

	@Override
	public Vector getAddressBooks() throws NotesException {
		return getDelegate().getAddressBooks();
	}

	@Override
	public AgentContext getAgentContext() throws NotesException {
		return getDelegate().getAgentContext();
	}

	@Override
	public NotesCalendar getCalendar(Database arg0) throws NotesException {
		return getDelegate().getCalendar(arg0);
	}

	@Override
	public String getCommonUserName() throws NotesException {
		return getDelegate().getCommonUserName();
	}

	@Override
	public Object getCredentials() throws NotesException {
		return getDelegate().getCredentials();
	}

	@Override
	public Database getCurrentDatabase() throws NotesException {
		return getDelegate().getCurrentDatabase();
	}

	@Override
	public Database getDatabase(String arg0, String arg1, boolean arg2) throws NotesException {
		return getDelegate().getDatabase(arg0, arg1, arg2);
	}

	@Override
	public Database getDatabase(String arg0, String arg1) throws NotesException {
		return getDelegate().getDatabase(arg0, arg1);
	}

	@Override
	public DbDirectory getDbDirectory(String arg0) throws NotesException {
		return getDelegate().getDbDirectory(arg0);
	}

	@Override
	public Directory getDirectory() throws NotesException {
		return getDelegate().getDirectory();
	}

	@Override
	public Directory getDirectory(String arg0) throws NotesException {
		return getDelegate().getDirectory(arg0);
	}

	@Override
	public String getEffectiveUserName() throws NotesException {
		return getDelegate().getEffectiveUserName();
	}

	@Override
	public String getEnvironmentString(String arg0, boolean arg1) throws NotesException {
		return getDelegate().getEnvironmentString(arg0, arg1);
	}

	@Override
	public String getEnvironmentString(String arg0) throws NotesException {
		return getDelegate().getEnvironmentString(arg0);
	}

	@Override
	public Object getEnvironmentValue(String arg0, boolean arg1) throws NotesException {
		return getDelegate().getEnvironmentValue(arg0, arg1);
	}

	@Override
	public Object getEnvironmentValue(String arg0) throws NotesException {
		return getDelegate().getEnvironmentValue(arg0);
	}

	@Override
	public String getHttpURL() throws NotesException {
		return getDelegate().getHttpURL();
	}

	@Override
	public International getInternational() throws NotesException {
		return getDelegate().getInternational();
	}

	@Override
	public String getNotesVersion() throws NotesException {
		return getDelegate().getNotesVersion();
	}

	@Override
	public String getOrgDirectoryPath() throws NotesException {
		return getDelegate().getOrgDirectoryPath();
	}

	@Override
	public String getPlatform() throws NotesException {
		return getDelegate().getPlatform();
	}

	@Override
	public PropertyBroker getPropertyBroker() throws NotesException {
		return getDelegate().getPropertyBroker();
	}

	@Override
	public String getServerName() throws NotesException {
		return getDelegate().getServerName();
	}

	@Override
	public String getSessionToken() throws NotesException {
		return getDelegate().getSessionToken();
	}

	@Override
	public String getSessionToken(String arg0) throws NotesException {
		return getDelegate().getSessionToken(arg0);
	}

	@Override
	public String getURL() throws NotesException {
		return getDelegate().getURL();
	}

	@Override
	public Database getURLDatabase() throws NotesException {
		return getDelegate().getURLDatabase();
	}

	@Override
	public Vector getUserGroupNameList() throws NotesException {
		return getDelegate().getUserGroupNameList();
	}

	@Override
	public String getUserName() throws NotesException {
		return getDelegate().getUserName();
	}

	@Override
	public Vector getUserNameList() throws NotesException {
		return getDelegate().getUserNameList();
	}

	@Override
	public Name getUserNameObject() throws NotesException {
		return getDelegate().getUserNameObject();
	}

	@Override
	public Document getUserPolicySettings(String arg0, String arg1, int arg2, String arg3) throws NotesException {
		return getDelegate().getUserPolicySettings(arg0, arg1, arg2, arg3);
	}

	@Override
	public Document getUserPolicySettings(String arg0, String arg1, int arg2) throws NotesException {
		return getDelegate().getUserPolicySettings(arg0, arg1, arg2);
	}

	@Override
	public String hashPassword(String arg0) throws NotesException {
		return getDelegate().hashPassword(arg0);
	}

	@Override
	public boolean isConvertMIME() throws NotesException {
		return getDelegate().isConvertMIME();
	}

	@Override
	public boolean isConvertMime() throws NotesException {
		return getDelegate().isConvertMime();
	}

	@Override
	public boolean isOnServer() throws NotesException {
		return getDelegate().isOnServer();
	}

	@Override
	public boolean isRestricted() throws NotesException {
		return getDelegate().isRestricted();
	}

	@Override
	public boolean isTrackMillisecInJavaDates() throws NotesException {
		return getDelegate().isTrackMillisecInJavaDates();
	}

	@Override
	public boolean isTrustedSession() throws NotesException {
		return getDelegate().isTrustedSession();
	}

	@Override
	public boolean isValid() {
		return getDelegate().isValid();
	}

	@Override
	public void recycle() throws NotesException {
		getDelegate().recycle();
	}

	@Override
	public void recycle(Vector arg0) throws NotesException {
		getDelegate().recycle(arg0);
	}

	@Override
	public boolean resetUserPassword(String arg0, String arg1, String arg2, int arg3) throws NotesException {
		return getDelegate().resetUserPassword(arg0, arg1, arg2, arg3);
	}

	@Override
	public boolean resetUserPassword(String arg0, String arg1, String arg2) throws NotesException {
		return getDelegate().resetUserPassword(arg0, arg1, arg2);
	}

	@Override
	public Base resolve(String arg0) throws NotesException {
		return getDelegate().resolve(arg0);
	}

	@Override
	public String sendConsoleCommand(String arg0, String arg1) throws NotesException {
		return getDelegate().sendConsoleCommand(arg0, arg1);
	}

	@Override
	public void setAllowLoopBack(boolean arg0) throws NotesException {
		getDelegate().setAllowLoopBack(arg0);
	}

	@Override
	public void setConvertMIME(boolean arg0) throws NotesException {
		getDelegate().setConvertMIME(arg0);
	}

	@Override
	public void setConvertMime(boolean arg0) throws NotesException {
		getDelegate().setConvertMime(arg0);
	}

	@Override
	public void setEnvironmentVar(String arg0, Object arg1, boolean arg2) throws NotesException {
		getDelegate().setEnvironmentVar(arg0, arg1, arg2);
	}

	@Override
	public void setEnvironmentVar(String arg0, Object arg1) throws NotesException {
		getDelegate().setEnvironmentVar(arg0, arg1);
	}

	@Override
	public void setTrackMillisecInJavaDates(boolean arg0) throws NotesException {
		getDelegate().setTrackMillisecInJavaDates(arg0);
	}

	@Override
	public boolean verifyPassword(String arg0, String arg1) throws NotesException {
		return getDelegate().verifyPassword(arg0, arg1);
	}

}
