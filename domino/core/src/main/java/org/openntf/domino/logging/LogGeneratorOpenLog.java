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

/**
 * Used by the LogHandlerOpenLog to write log messages to an OpenLog database. This is a considerably simplified version of BaseOpenLogItem
 * for use in the new logging mechanism.
 *
 */
@SuppressWarnings("nls")
public class LogGeneratorOpenLog {

	/*-------------------------------------------------------------*/
	private static final String _logFormName = "LogEvent"; //$NON-NLS-1$

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
			ollr._userRoles = sess.evaluate("@UserRoles"); //$NON-NLS-1$
			String clVer = sess.getNotesVersion();
			if (clVer != null) {
				ollr._clientVersion = clVer.split("\\|"); //$NON-NLS-1$
				for (int i = 0; i < ollr._clientVersion.length; i++) {
					ollr._clientVersion[i] = ollr._clientVersion[i].trim();
				}
			}
		} catch (Exception e) {
			printException(e);
			ollr._clientVersion = new String[] { "Exception while collecting logg data!", "See next LogEntry for details." };
			localExc = e;
		}
		if (_olQueue != null) {
			_olQueue.add(new OL_EntryToWrite(this, ollr));
		} else {
			_olWriter.writeLogRecToDB(sess, ollr, _startTime);
		}
		if (localExc == null) {
			return;
		}
		logRec = new LogRecord(Level.SEVERE, "Exception in LogGenerator.log");
		logRec.setThrown(localExc);
		logRec.setMillis(System.currentTimeMillis());
		ollr = new OL_LogRecord(logRec, null, null);
		ollr._agentName = "LogGeneratorOpenLog"; //$NON-NLS-1$
		ollr._dbPath = _logDBPath;
		if (_olQueue != null) {
			_olQueue.add(new OL_EntryToWrite(this, ollr));
		} else {
			_olWriter.writeLogRecToDB(sess, ollr, _startTime);
		}
	}

	/*-------------------------------------------------------------*/
	private String getAccessLevel(final Database currDB) {
		int acl = currDB.getCurrentAccessLevel();
		String ret;
		if (acl <= 0) {
			ret = "0: No Access";
		} else if (acl == 1) {
			ret = "1: Depositor";
		} else if (acl == 2) {
			ret = "2: Reader";
		} else if (acl == 3) {
			ret = "3: Author";
		} else if (acl == 4) {
			ret = "4: Editor";
		} else if (acl == 5) {
			ret = "5: Designer";
		} else {
			ret = "6: Manager";
		}
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
				if (_logDB != null) {
					ret = _logDB.createDocument();
				}
			} catch (Throwable t) {
			}
			if (ret == null) {
				try {
					_logDB = s.getDatabase(_myDBPath);
					ret = _logDB.createDocument();
				} catch (Exception e) {
					printException(e);
				}
			}
			return ret;
		}

		/*-------------------------------------------------------------*/
		public void writeLogRecToDB(final Session sess, final OL_LogRecord ollr, final Date logStartTime) {
			Document olDoc = getEmptyDocument(sess);
			if (olDoc == null) {
				return;
			}
			try {
				olDoc.replaceItemValue("Form", _logFormName); //$NON-NLS-1$
				Throwable t = ollr._logRec.getThrown();
				if (t != null) {
					StackTraceElement[] sttr = t.getStackTrace();
					int interestingInd = (sttr.length == 0) ? -11 : 0;
					NotesException ne = null;
					if (t instanceof NotesException) {
						ne = (NotesException) t;
					} else if (t instanceof OpenNTFNotesException && t.getCause() instanceof NotesException) {
						ne = (NotesException) t.getCause();
						interestingInd++;
					}
					if (ne != null) {
						olDoc.replaceItemValue("LogErrorNumber", ne.id); //$NON-NLS-1$
						olDoc.replaceItemValue("LogErrorMessage", ne.text); //$NON-NLS-1$

					} else {
						olDoc.replaceItemValue("LogErrorMessage", getMessage(ollr._logRec)); //$NON-NLS-1$
					}
					if (interestingInd >= 0) {
						StackTraceElement ste = sttr[interestingInd];
						olDoc.replaceItemValue("LogErrorLine", ste.getLineNumber()); //$NON-NLS-1$
						olDoc.replaceItemValue("LogFromMethod", ste.getClassName() + "." + ste.getMethodName()); //$NON-NLS-1$
					}
				}
				olDoc.replaceItemValue("LogStackTrace", getStackTrace(t)); //$NON-NLS-1$
				Level l = ollr._logRec.getLevel();
				if (l == null) {
					l = Level.WARNING;
				}
				olDoc.replaceItemValue("LogSeverity", l.getName()); //$NON-NLS-1$
				olDoc.replaceItemValue("LogEventTime", new Date(ollr._logRec.getMillis())); //$NON-NLS-1$
				olDoc.replaceItemValue("LogEventType", "Log"); //$NON-NLS-1$ //$NON-NLS-2$
				olDoc.replaceItemValue("LogMessage", getMessage(ollr._logRec)); //$NON-NLS-1$
				olDoc.replaceItemValue("LogFromDatabase", ollr._dbPath); //$NON-NLS-1$
				olDoc.replaceItemValue("LogFromServer", ollr._serverName); //$NON-NLS-1$
				olDoc.replaceItemValue("LogFromAgent", ollr._agentName); //$NON-NLS-1$
				olDoc.replaceItemValue("LogAgentLanguage", "Java"); //$NON-NLS-1$ //$NON-NLS-2$
				olDoc.replaceItemValue("LogUserName", ollr._userName); //$NON-NLS-1$
				olDoc.replaceItemValue("LogEffectiveName", ollr._effectiveUserName); //$NON-NLS-1$
				olDoc.replaceItemValue("LogAccessLevel", ollr._accessLevel); //$NON-NLS-1$
				olDoc.replaceItemValue("LogUserRoles", ollr._userRoles); //$NON-NLS-1$
				olDoc.replaceItemValue("LogClientVersion", ollr._clientVersion); //$NON-NLS-1$
				olDoc.replaceItemValue("LogAgentStartTime", logStartTime); //$NON-NLS-1$
				if (ollr._exceptionDetails == null) {
					olDoc.replaceItemValue("LogExceptionDetails", "* Not available *"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					int sz = ollr._exceptionDetails.size();
					String[] excds = new String[sz];
					for (int i = 0; i < sz; i++) {
						excds[i] = ollr._exceptionDetails.get(i).toString();
					}
					olDoc.replaceItemValue("LogExceptionDetails", excds); //$NON-NLS-1$
				}
				if (ollr._lastWrappedDocs == null) {
					olDoc.replaceItemValue("LogLastWrappedDocuments", "* Not available *"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					olDoc.replaceItemValue("LogLastWrappedDocuments", ollr._lastWrappedDocs); //$NON-NLS-1$
				}
				olDoc.replaceItemValue("$PublicAccess", "1"); //$NON-NLS-1$ //$NON-NLS-2$
				olDoc.save(true);
			} catch (Exception e) {
				printException(e);
			}
		}

		String getMessage(final LogRecord logRec) {
			String ret = logRec.getMessage();
			if (ret != null && !ret.isEmpty()) {
				return ret;
			}
			Throwable t = logRec.getThrown();
			if (t != null) {
				ret = t.getMessage();
			}
			return (ret == null) ? "" : ret; //$NON-NLS-1$
		}

		private ArrayList<String> getStackTrace(final Throwable t) {
			ArrayList<String> v = new ArrayList<String>();
			if (t == null) {
				v.add("***NO STACK TRACE***");
			} else {
				StringWriter sw = new StringWriter();
				t.printStackTrace(new PrintWriter(sw));
				StringTokenizer st = new StringTokenizer(sw.toString(), "\n"); //$NON-NLS-1$
				while (st.hasMoreTokens()) {
					v.add(st.nextToken().trim());
				}
			}
			return v;
		}
	}
}
