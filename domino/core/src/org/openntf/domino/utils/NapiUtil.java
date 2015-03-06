/*
 * © Copyright FOCONIS AG, 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.openntf.domino.Document;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesNote;
import com.ibm.designer.domino.napi.NotesObject;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.domino.napi.c.BackendBridge;

/**
 * Utility class to handle NAPI access
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public enum NapiUtil {
	;

	private static Method setHandleMethod;
	private static Constructor<NotesNote> notesNoteConstructor;
	private static ThreadLocal<NotesSession> napiSession_ = new ThreadLocal<NotesSession>();

	/**
	 * Initializes the NAPI - this is a little bit hackish. To understand what's going on here, you have to know some facts:<br/>
	 * <ul>
	 * <li>NotesSession itself is NOT user dependent (as lotus.domino.Session is). If you create a new NotesSession there are no C/C++
	 * objects or resources allocated.</li>
	 * <li>There are no C++ objects created at all. Only handles are allocated/freed. Handle is an integer and you should know this from the
	 * Lotus-C-API.</li>
	 * <li>If you recycle the NotesSession, it recycles automatically all child elements</li>
	 * <li>Recycling is also done in finalize, to recycle dangling elements</li>
	 * <li>To get the C-Handle from a {@link lotus.domino.Base}-Object, you can use the methods
	 * {@link BackendBridge#getDatabaseHandleRO(lotus.domino.Database)} or {@link BackendBridge#getDocumentHandleRW(lotus.domino.Document)}.
	 * (compare to the undocumented LS-Property NotesDocument.handle)</li>
	 * <li>To get a {@link NotesDatabase} from a {@link lotus.domino.Database}, you can use {@link NotesSession#getDatabase(int)} and pass
	 * the handle from above to the object. This type of NotesDatabase has the "ownsHandle" property set to false, that means the passed
	 * handle will NOT be recycled, which is good, because we MUST NOT recycle the handle, we get from a {@link lotus.domino.Database}. (If
	 * we do, the process will crash!)</li>
	 * <li>Unfortunately, there is no way out of the box to do the same for {@link NotesDocument}
	 * </ul>
	 * 
	 * @throws Exception
	 */
	public static void init() throws Exception {
		com.ibm.domino.napi.c.C.initLibrary(null);
		notesNoteConstructor = com.ibm.designer.domino.napi.NotesNote.class.getDeclaredConstructor(NotesDatabase.class, int.class);
		notesNoteConstructor.setAccessible(true);

		setHandleMethod = com.ibm.designer.domino.napi.NotesHandle.class.getDeclaredMethod("setHandle", int.class);
		setHandleMethod.setAccessible(true);

		Factory.addTerminateHook(RECYCLER, true);
	}

	public static Runnable RECYCLER = new Runnable() {
		@Override
		public void run() {
			NotesSession sess = napiSession_.get();
			napiSession_.set(null);
			if (sess != null) {
				System.out.println("Recycling NAPI Session");
				try {
					sess.recycle();
				} catch (NotesAPIException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private static class RecyclePreventer extends NotesObject {

		public RecyclePreventer(final NotesNote paramNotesObject) {
			super(paramNotesObject);
		}

		@Override
		protected boolean mustBeClosed() {
			return true;
		}

		@Override
		protected void doRecycle() throws NotesAPIException {
			// TODO Auto-generated method stub

			//System.out.println("Recycle");
			try {
				setHandleMethod.invoke(getParent(), new Object[] { 0 });
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

	}

	public static NotesNote createNotesNote(final Document doc) throws NotesAPIException {
		NotesDatabase db = doc.getParentDatabase().getNapiDatabase();

		//		if (dummyDXLImporter == null) {
		//			dummyDXLImporter = new DXLImporter(db);
		//			dummyDXLImporter.open();
		//		}
		//		NotesNote ret = NotesNote.constructNote(dummyDXLImporter, db, doc.getNapiHandle());
		try {
			NotesNote ret = notesNoteConstructor.newInstance(db, doc.getNapiHandle());
			new RecyclePreventer(ret);
			return ret;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static NotesSession getNapiSession() {
		// TODO Auto-generated method stub
		NotesSession ret = napiSession_.get();
		if (ret == null) {
			ret = new NotesSession();
			napiSession_.set(ret);
		}
		return ret;
	}

}
