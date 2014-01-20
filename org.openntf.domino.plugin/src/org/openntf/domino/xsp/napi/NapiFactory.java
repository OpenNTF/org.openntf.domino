/**
 * 
 */
package org.openntf.domino.xsp.napi;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;

import org.openntf.domino.Base;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.domino.napi.c.Nsf;
import com.ibm.domino.xsp.module.nsf.NotesContext;

/**
 * @author praml
 * 
 */
public class NapiFactory implements org.openntf.domino.napi.NapiFactory {
	private static final Logger log_ = Logger.getLogger(NapiFactory.class.getName());
	private static final long serialVersionUID = 1L;

	private static Constructor notesNoteConstructor;
	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					notesNoteConstructor = com.ibm.designer.domino.napi.NotesNote.class.getDeclaredConstructor(
							com.ibm.designer.domino.napi.NotesDatabase.class, int.class);
					notesNoteConstructor.setAccessible(true);
					System.out.println("got Constructor ");
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}
	}

	public static long getHandle(final Base base) {
		if (NotesContext.getCurrentUnchecked() == null) {
			// if context is not present, do not use napi
			return 0;
		}
		if (base instanceof org.openntf.domino.impl.Document) {
			return BackendBridge.getDocumentHandleRW((Document) base);
		} else if (base instanceof org.openntf.domino.impl.Database) {
			return BackendBridge.getDatabaseHandleRO((Database) base);

			// org.openntf.domino.impl.Base baseImpl = (org.openntf.domino.impl.Base) base;
			// long cpp_session = baseImpl.GetCppSession();
			// long cpp_object = baseImpl.GetCppObj();
			// try {
			// System.out.println("Trying to get handle #2");
			// return XSPNative.getDBHandle(new CppWrapper(cpp_object, cpp_session));
			// } catch (NotesException e) {
			// return 0;
			// }
		}

		return 0;
	}

	public NapiDocument getNapiDocument(final org.openntf.domino.Document doc) {

		long handle = getHandle(doc);
		if (handle == 0) {
			return null;
		}
		try {
			long dbHandle = getHandle(doc.getAncestorDatabase());
			System.out.println(Nsf.ACLGetAdminServer(dbHandle));
			//com.ibm.designer.domino.napi.NotesSession nSess = new NotesSession();
			//com.ibm.designer.domino.napi.NotesDatabase nullDB = nSess.getDatabase((int) getHandle(doc.getAncestorDatabase()));
			//System.out.println("nullDb: " + nullDB.getDatabasePath());
			//NotesNote napiNote = (NotesNote) notesNoteConstructor.newInstance(nullDB, (int) handle);

			//return new NapiDocument(napiNote);
			return null;
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}
