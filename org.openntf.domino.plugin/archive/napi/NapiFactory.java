/**
 * 
 */
package org.openntf.domino.xsp.napi;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;

import com.ibm.domino.napi.c.BackendBridge;
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
					// System.out.println("got Constructor ");
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			DominoUtils.handleException(e);
		}
	}

	public NapiDocument getNapiDocument(final lotus.domino.Document doc) {
		if (NotesContext.getCurrentUnchecked() == null) {
			// if context is not present, do not use napi
			return null;
		}
		int handle = (int) BackendBridge.getDocumentHandleRW(doc);
		if (handle == 0) {
			return null;
		}
		return new NapiDocument(handle);
	}
}
