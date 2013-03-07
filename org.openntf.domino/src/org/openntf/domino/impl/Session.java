/**
 * 
 */
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import lotus.domino.AdministrationProcess;
import lotus.domino.AgentContext;
import lotus.domino.Base;
import lotus.domino.ColorObject;
import lotus.domino.Database;
import lotus.domino.DateRange;
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
import lotus.domino.NotesException;
import lotus.domino.PropertyBroker;
import lotus.domino.Registration;
import lotus.domino.RichTextParagraphStyle;
import lotus.domino.RichTextStyle;
import lotus.domino.Stream;

import org.openntf.domino.DateTime;
import org.openntf.domino.Name;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

//import lotus.domino.Name;

/**
 * @author nfreeman
 * 
 */
public class Session extends org.openntf.domino.impl.Base<org.openntf.domino.Session, lotus.domino.Session> implements
		org.openntf.domino.Session {

	/**
	 * 
	 */
	public Session() {
		// TODO come up with some static methods for finding a Session based on run context (XPages, Agent, DOTS, etc)
		this(null);
	}

	public Session(lotus.domino.Session lotus) {
		super(lotus);
	}

	@Override
	public AdministrationProcess createAdministrationProcess(String arg0) {
		try {
			return getDelegate().createAdministrationProcess(arg0);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);

		}
		return null;
	}

	@Override
	public ColorObject createColorObject() {
		try {
			return getDelegate().createColorObject();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	@Override
	public org.openntf.domino.DateRange createDateRange() {
		try {
			return Factory.fromLotus(getDelegate().createDateRange(), org.openntf.domino.DateRange.class);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	@Override
	public DateRange createDateRange(Date arg0, Date arg1) {
		try {
			return getDelegate().createDateRange(arg0, arg1);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return null;
	}

	public DateRange createDateRange(lotus.domino.DateTime arg0, lotus.domino.DateTime arg1) {
		try {
			return Factory.fromLotus(createDateRange(arg0, arg1), org.openntf.domino.DateRange.class);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DateTime createDateTime(Calendar arg0) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(arg0), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DateTime createDateTime(Date arg0) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(arg0), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DateTime createDateTime(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().createDateTime(arg0), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DxlExporter createDxlExporter() {
		try {
			return getDelegate().createDxlExporter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DxlImporter createDxlImporter() {
		try {
			return getDelegate().createDxlImporter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Log createLog(String arg0) {
		try {
			return getDelegate().createLog(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Name createName(String arg0, String arg1) {
		try {
			return Factory.fromLotus(getDelegate().createName(arg0, arg1), Name.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Name createName(String arg0) {
		try {
			return Factory.fromLotus(getDelegate().createName(arg0), Name.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Newsletter createNewsletter(DocumentCollection arg0) {
		try {
			return getDelegate().createNewsletter(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Registration createRegistration() {
		try {
			return getDelegate().createRegistration();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public RichTextParagraphStyle createRichTextParagraphStyle() {
		try {
			return getDelegate().createRichTextParagraphStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public RichTextStyle createRichTextStyle() {
		try {
			return getDelegate().createRichTextStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Stream createStream() {
		try {
			return getDelegate().createStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> evaluate(String arg0, Document arg1) {
		try {
			return getDelegate().evaluate(arg0, arg1); // TODO still needs Factory wrapper
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	@Legacy({ Legacy.INTERFACES_WARNING, Legacy.GENERICS_WARNING })
	public Vector<Object> evaluate(String arg0) {
		try {
			return getDelegate().evaluate(arg0); // TODO still needs Factory wrapper
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<org.openntf.domino.DateRange> freeTimeSearch(DateRange arg0, int arg1, Object arg2, boolean arg3) {
		try {
			return getDelegate().freeTimeSearch(arg0, arg1, arg2, arg3); // TODO still needs Factory wrapper
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	@Legacy(Legacy.INTERFACES_WARNING)
	public Vector<org.openntf.domino.Database> getAddressBooks() {
		try {
			return getDelegate().getAddressBooks(); // TODO still needs Factory wrapper
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public AgentContext getAgentContext() {
		try {
			return getDelegate().getAgentContext();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public NotesCalendar getCalendar(Database arg0) {
		try {
			return getDelegate().getCalendar(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getCommonUserName() {
		try {
			return getDelegate().getCommonUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Object getCredentials() {
		try {
			return getDelegate().getCredentials();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Database getCurrentDatabase() {
		try {
			return getDelegate().getCurrentDatabase();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Database getDatabase(String arg0, String arg1, boolean arg2) {
		try {
			return getDelegate().getDatabase(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Database getDatabase(String arg0, String arg1) {
		try {
			return getDelegate().getDatabase(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DbDirectory getDbDirectory(String arg0) {
		try {
			return getDelegate().getDbDirectory(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Directory getDirectory() {
		try {
			return getDelegate().getDirectory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Directory getDirectory(String arg0) {
		try {
			return getDelegate().getDirectory(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getEffectiveUserName() {
		try {
			return getDelegate().getEffectiveUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getEnvironmentString(String arg0, boolean arg1) {
		try {
			return getDelegate().getEnvironmentString(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getEnvironmentString(String arg0) {
		try {
			return getDelegate().getEnvironmentString(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Object getEnvironmentValue(String arg0, boolean arg1) {
		try {
			return getDelegate().getEnvironmentValue(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Object getEnvironmentValue(String arg0) {
		try {
			return getDelegate().getEnvironmentValue(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public International getInternational() {
		try {
			return getDelegate().getInternational();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getNotesVersion() {
		try {
			return getDelegate().getNotesVersion();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getOrgDirectoryPath() {
		try {
			return getDelegate().getOrgDirectoryPath();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getPlatform() {
		try {
			return getDelegate().getPlatform();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public PropertyBroker getPropertyBroker() {
		try {
			return getDelegate().getPropertyBroker();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getServerName() {
		try {
			return getDelegate().getServerName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getSessionToken() {
		try {
			return getDelegate().getSessionToken();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getSessionToken(String arg0) {
		try {
			return getDelegate().getSessionToken(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Database getURLDatabase() {
		try {
			return getDelegate().getURLDatabase();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Vector<org.openntf.domino.Name> getUserGroupNameList() {
		try {
			return getDelegate().getUserGroupNameList();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getUserName() {
		try {
			return getDelegate().getUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Vector<org.openntf.domino.Name> getUserNameList() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getUserNameList(), Name.class);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Name getUserNameObject() {
		try {
			return Factory.fromLotus(getDelegate().getUserNameObject(), Name.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getUserPolicySettings(String arg0, String arg1, int arg2, String arg3) {
		try {
			return getDelegate().getUserPolicySettings(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Document getUserPolicySettings(String arg0, String arg1, int arg2) {
		try {
			return getDelegate().getUserPolicySettings(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String hashPassword(String arg0) {
		try {
			return getDelegate().hashPassword(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public boolean isConvertMIME() {
		try {
			return getDelegate().isConvertMIME();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean isConvertMime() {
		try {
			return getDelegate().isConvertMime();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean isOnServer() {
		try {
			return getDelegate().isOnServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean isRestricted() {
		try {
			return getDelegate().isRestricted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return true;

		}
	}

	@Override
	public boolean isTrackMillisecInJavaDates() {
		try {
			return getDelegate().isTrackMillisecInJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return true;

		}
	}

	@Override
	public boolean isTrustedSession() {
		try {
			return getDelegate().isTrustedSession();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public boolean isValid() {
		return getDelegate().isValid();
	}

	@Override
	public boolean resetUserPassword(String arg0, String arg1, String arg2, int arg3) {
		try {
			return getDelegate().resetUserPassword(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean resetUserPassword(String arg0, String arg1, String arg2) {
		try {
			return getDelegate().resetUserPassword(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
		return false;
	}

	@Override
	public Base resolve(String arg0) {
		try {
			return getDelegate().resolve(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String sendConsoleCommand(String arg0, String arg1) {
		try {
			return getDelegate().sendConsoleCommand(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void setAllowLoopBack(boolean arg0) {
		try {
			getDelegate().setAllowLoopBack(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setConvertMIME(boolean arg0) {
		try {
			getDelegate().setConvertMIME(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setConvertMime(boolean arg0) {
		try {
			getDelegate().setConvertMime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setEnvironmentVar(String arg0, Object arg1, boolean arg2) {
		try {
			getDelegate().setEnvironmentVar(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setEnvironmentVar(String arg0, Object arg1) {
		try {
			getDelegate().setEnvironmentVar(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setTrackMillisecInJavaDates(boolean arg0) {
		try {
			getDelegate().setTrackMillisecInJavaDates(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public boolean verifyPassword(String arg0, String arg1) {
		try {
			return getDelegate().verifyPassword(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	@Override
	public Collection<String> getUserGroupNameCollection() {
		Collection<String> result = new ArrayList<String>();
		Vector<Name> v = this.getUserGroupNameList();
		if (!v.isEmpty()) {
			for (Name name : v) {
				result.add(name.getCanonical());
				DominoUtils.incinerate(name);
			}
		}
		return result;
	}

	@Override
	public Collection<String> getUserNameCollection() {
		Collection<String> result = new ArrayList<String>();
		Vector<Name> v = this.getUserNameList();
		if (!v.isEmpty()) {
			for (Name name : v) {
				result.add(name.getCanonical());
				DominoUtils.incinerate(name);
			}
		}
		return result;
	}

	@Override
	public Collection<org.openntf.domino.Database> getAddressBookCollection() {
		Collection<org.openntf.domino.Database> result = new ArrayList<org.openntf.domino.Database>();
		Vector<org.openntf.domino.Database> v = this.getAddressBooks();
		if (!v.isEmpty()) {
			for (org.openntf.domino.Database db : v) {
				result.add(db);
			}
		}
		return result;
	}

	@Override
	public Collection<org.openntf.domino.DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, String arg2, boolean arg3) {
		// TODO verify that we don't end up with an ambiguous method signature
		Collection<org.openntf.domino.DateRange> result = new ArrayList<org.openntf.domino.DateRange>();
		Vector<org.openntf.domino.DateRange> v = this.freeTimeSearch((lotus.domino.DateRange) arg0, arg1, (Object) arg2, arg3);
		if (!v.isEmpty()) {
			for (org.openntf.domino.DateRange dr : v) {
				result.add(dr);
			}
		}
		return result;
	}

	@Override
	public Collection<org.openntf.domino.DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, Collection<String> arg2,
			boolean arg3) {
		// TODO verify that we don't end up with an ambiguous method signature
		Collection<org.openntf.domino.DateRange> result = new ArrayList<org.openntf.domino.DateRange>();
		Vector<org.openntf.domino.DateRange> v = this.freeTimeSearch((lotus.domino.DateRange) arg0, arg1, (Object) arg2, arg3);
		if (!v.isEmpty()) {
			for (org.openntf.domino.DateRange dr : v) {
				result.add(dr);
			}
		}
		return result;
	}

}
