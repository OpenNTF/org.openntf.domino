/*
 * Considerably simplified version of BaseOpenLogItem for use in the new logging mechanism.
 */

package org.openntf.domino.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.Factory;

public class LogGeneratorOpenLog {

	/*-------------------------------------------------------------*/
	private static final String _logFormName = "LogEvent";

	//	private static final String _xotsDemonClassName = "org.openntf.domino.xots.XotsDaemon";
	//	private static final String _xotsDemonToQueueMethodName = "addToQueue";

	/*-------------------------------------------------------------*/
	class OL_LogRecord {
		LogRecord _logRec;
		List<ExceptionDetails.Entry> _exceptionDetails;
		String[] _lastWrappedDocs;
		String _serverName;
		String _agentName;
		String _dbPath;
		String _accessLevel;
		String _userName;
		String _effectiveUserName;
		Vector<Object> _userRoles;
		String[] _clientVersion;

		OL_LogRecord(final LogRecord logRec, final List<ExceptionDetails.Entry> exceptionDetails, final String[] lastWrappedDocs) {
			_logRec = logRec;
			_exceptionDetails = exceptionDetails;
			_lastWrappedDocs = lastWrappedDocs;
		}
	}

	public class OL_EntryToWrite {
		public LogGeneratorOpenLog _logGenerator;
		public OL_LogRecord _logRec;

		OL_EntryToWrite(final LogGeneratorOpenLog logGenerator, final OL_LogRecord logRec) {
			_logGenerator = logGenerator;
			_logRec = logRec;
		}
	}

	/*-------------------------------------------------------------*/
	private String _logDBPath;
	public Date _startTime;
	public OL_Writer _olWriter;

	/*-------------------------------------------------------------*/
	public LogGeneratorOpenLog(final String logDBPath) {
		_startTime = new Date();
		_logDBPath = logDBPath;
		_olWriter = new OL_Writer(_logDBPath);
	}

	/*-------------------------------------------------------------*/
	public static LinkedBlockingQueue<LogGeneratorOpenLog.OL_EntryToWrite> _olQueue = null;

	//	private static boolean _xInitDone = false;

	/*-------------------------------------------------------------*/
	//	private static void doStaticStartUp() {
	//		ClassLoader loader = Thread.currentThread().getContextClassLoader();
	//		try {
	//			Class<?> xotsDemonClass = Class.forName(_xotsDemonClassName, true, loader);
	//			Method xotsDemonToQueueMethod = xotsDemonClass.getMethod(_xotsDemonToQueueMethodName, Runnable.class);
	//			LogTaskletOpenLog tol = new LogTaskletOpenLog();
	//			_olQueue = new LinkedBlockingQueue<LogGeneratorOpenLog.OL_EntryToWrite>();
	//			xotsDemonToQueueMethod.invoke(null, tol);
	//		} catch (Exception e) {
	//			System.out.println("Can't make Xots-LogDB-Thread: " + e.getMessage());
	//			_olQueue = null;
	//		} finally {
	//			_xInitDone = true;
	//		}
	//	}

	/*-------------------------------------------------------------*/
	// TODO RPR: remove synchronized
	synchronized void log(final Session sess, LogRecord logRec, final LogRecordAdditionalInfo lrai) {
		//		if (!_xInitDone)
		//			doStaticStartUp();
		OL_LogRecord ollr = new OL_LogRecord(logRec, lrai.getExceptionDetails(), lrai.getLastWrappedDocs());
		Exception localExc = null;
		try {
			ollr._serverName = sess.getServerName();
			ollr._agentName = Factory.getRunContext().name();
			Database currDB = sess.getCurrentDatabase();
			if (currDB != null) {
				ollr._dbPath = currDB.getFilePath();
				ollr._accessLevel = getAccessLevel(currDB);
			}
			ollr._userName = sess.getUserName();
			ollr._effectiveUserName = sess.getEffectiveUserName();
			ollr._userRoles = sess.evaluate("@UserRoles");
			String clVer = sess.getNotesVersion();
			if (clVer != null) {
				ollr._clientVersion = clVer.split("\\|");
				for (int i = 0; i < ollr._clientVersion.length; i++)
					ollr._clientVersion[i] = ollr._clientVersion[i].trim();
			}
		} catch (Exception e) {
			printException(e);
			ollr._clientVersion = new String[] { "Exception while collecting logg data!", "See next LogEntry for details." };
			localExc = e;
		}
		if (_olQueue != null)
			_olQueue.add(new OL_EntryToWrite(this, ollr));
		else
			_olWriter.writeLogRecToDB(sess, ollr, _startTime);
		if (localExc == null)
			return;
		logRec = new LogRecord(Level.SEVERE, "Exception in LogGenerator.log");
		logRec.setThrown(localExc);
		logRec.setMillis(System.currentTimeMillis());
		ollr = new OL_LogRecord(logRec, null, null);
		ollr._agentName = "LogGeneratorOpenLog";
		ollr._dbPath = _logDBPath;
		if (_olQueue != null)
			_olQueue.add(new OL_EntryToWrite(this, ollr));
		else
			_olWriter.writeLogRecToDB(sess, ollr, _startTime);
	}

	/*-------------------------------------------------------------*/
	private String getAccessLevel(final Database currDB) {
		int acl = currDB.getCurrentAccessLevel();
		String ret;
		if (acl <= 0)
			ret = "0: No Access";
		else if (acl == 1)
			ret = "1: Depositor";
		else if (acl == 2)
			ret = "2: Reader";
		else if (acl == 3)
			ret = "3: Author";
		else if (acl == 4)
			ret = "4: Editor";
		else if (acl == 5)
			ret = "5: Designer";
		else
			ret = "6: Manager";
		return ret;
	}

	static void printException(final Exception e) {
		System.err.println("LogGeneratorOpenLog: Caught Exception " + e.getClass().getName());
		e.printStackTrace();
	}

	/*-------------------------------------------------------------*/
	public class OL_Writer {

		private String _myDBPath;
		private Database _logDB = null;

		OL_Writer(final String dbPath) {
			_myDBPath = dbPath;
		}

		/*-------------------------------------------------------------*/
		private Document getEmptyDocument(final Session s) {
			Document ret = null;
			try {
				if (_logDB != null)
					ret = _logDB.createDocument();
			} catch (Throwable t) {
			}
			if (ret == null)
				try {
					_logDB = s.getDatabase(_myDBPath);
					ret = _logDB.createDocument();
				} catch (Exception e) {
					printException(e);
				}
			return ret;
		}

		/*-------------------------------------------------------------*/
		public void writeLogRecToDB(final Session sess, final OL_LogRecord ollr, final Date logStartTime) {
			Document olDoc = getEmptyDocument(sess);
			if (olDoc == null)
				return;
			try {
				olDoc.replaceItemValue("Form", _logFormName);
				Throwable t = ollr._logRec.getThrown();
				if (t != null) {
					StackTraceElement[] sttr = t.getStackTrace();
					int interestingInd = (sttr.length == 0) ? -11 : 0;
					NotesException ne = null;
					if (t instanceof NotesException)
						ne = (NotesException) t;
					else if (t instanceof OpenNTFNotesException && t.getCause() instanceof NotesException) {
						ne = (NotesException) t.getCause();
						interestingInd++;
					}
					if (ne != null) {
						olDoc.replaceItemValue("LogErrorNumber", ne.id);
						olDoc.replaceItemValue("LogErrorMessage", ne.text);

					} else
						olDoc.replaceItemValue("LogErrorMessage", getMessage(ollr._logRec));
					if (interestingInd >= 0) {
						StackTraceElement ste = sttr[interestingInd];
						olDoc.replaceItemValue("LogErrorLine", ste.getLineNumber());
						olDoc.replaceItemValue("LogFromMethod", ste.getClassName() + "." + ste.getMethodName());
					}
				}
				olDoc.replaceItemValue("LogStackTrace", getStackTrace(t));
				Level l = ollr._logRec.getLevel();
				if (l == null)
					l = Level.WARNING;
				olDoc.replaceItemValue("LogSeverity", l.getName());
				olDoc.replaceItemValue("LogEventTime", new Date(ollr._logRec.getMillis()));
				olDoc.replaceItemValue("LogEventType", "Log");
				olDoc.replaceItemValue("LogMessage", getMessage(ollr._logRec));
				olDoc.replaceItemValue("LogFromDatabase", ollr._dbPath);
				olDoc.replaceItemValue("LogFromServer", ollr._serverName);
				olDoc.replaceItemValue("LogFromAgent", ollr._agentName);
				olDoc.replaceItemValue("LogAgentLanguage", "Java");
				olDoc.replaceItemValue("LogUserName", ollr._userName);
				olDoc.replaceItemValue("LogEffectiveName", ollr._effectiveUserName);
				olDoc.replaceItemValue("LogAccessLevel", ollr._accessLevel);
				olDoc.replaceItemValue("LogUserRoles", ollr._userRoles);
				olDoc.replaceItemValue("LogClientVersion", ollr._clientVersion);
				olDoc.replaceItemValue("LogAgentStartTime", logStartTime);
				if (ollr._exceptionDetails == null)
					olDoc.replaceItemValue("LogExceptionDetails", "* Not available *");
				else {
					int sz = ollr._exceptionDetails.size();
					String[] excds = new String[sz];
					for (int i = 0; i < sz; i++)
						excds[i] = ollr._exceptionDetails.get(i).toString();
					olDoc.replaceItemValue("LogExceptionDetails", excds);
				}
				if (ollr._lastWrappedDocs == null)
					olDoc.replaceItemValue("LogLastWrappedDocuments", "* Not available *");
				else
					olDoc.replaceItemValue("LogLastWrappedDocuments", ollr._lastWrappedDocs);
				olDoc.replaceItemValue("$PublicAccess", "1");
				olDoc.save(true);
			} catch (Exception e) {
				printException(e);
			}
		}

		String getMessage(final LogRecord logRec) {
			String ret = logRec.getMessage();
			if (ret != null && !ret.isEmpty())
				return ret;
			Throwable t = logRec.getThrown();
			if (t != null)
				ret = t.getMessage();
			return (ret == null) ? "" : ret;
		}

		private ArrayList<String> getStackTrace(final Throwable t) {
			ArrayList<String> v = new ArrayList<String>();
			if (t == null)
				v.add("***NO STACK TRACE***");
			else {
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				StringTokenizer st = new StringTokenizer(sw.toString(), "\n");
				while (st.hasMoreTokens())
					v.add(st.nextToken().trim());
			}
			return v;
		}
	}
}
