/*
 * © Copyright FOCONIS AG, 2014
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

package org.openntf.domino.xsp.model;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.acl.NoAccessSignal;
import com.ibm.xsp.domino.ResourceHandler;
import com.ibm.xsp.model.DocumentDataContainer;
import com.ibm.xsp.model.domino.DominoDocumentData;
import com.ibm.xsp.model.domino.DominoDocumentDataContainer;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * Here we inherit form the original XSP-datasource class ({@link DominoDocumentData}) and extend the doNew/doOpenDocument method to have
 * more control over some actions. Especially we want to replace the {@link DominoDocument} with our {@link OpenntfDominoDocument}
 * 
 */
public class OpenntfDominoDocumentData extends DominoDocumentData {
	private static Method openDatabaseMethod;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				// thanks for making this private :)
				@Override
				public Object run() throws Exception {
					openDatabaseMethod = DominoDocumentData.class.getDeclaredMethod("openDatabase", (Class<?>[]) null);
					openDatabaseMethod.setAccessible(true);
					return null;
				}
			});
		} catch (Exception e) {
			org.openntf.domino.utils.DominoUtils.handleException(e);

		}

	}

	/**
	 * this calls the "private" method in {@link DominoDocument}. Maybe IBM makes this protected, then we would not need this.
	 * 
	 * @return
	 */
	protected Database openDatabase() {
		try {
			return (Database) openDatabaseMethod.invoke(this, (Object[]) null);
		} catch (Exception e) {
			org.openntf.domino.utils.DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * Invoked if you call a xpage.xsp without a document ID
	 */
	@SuppressWarnings("nls")
	@Override
	public DocumentDataContainer doNewDocument(final FacesContext context) throws FacesExceptionEx {
		try {
			OpenntfDominoDocument ntfDoc = createDocument();
			return new DominoDocumentDataContainer(getBeanId(), getUniqueId(), ntfDoc);
		} catch (Exception localException) {
			throw new FacesExceptionEx("Unable to create document", localException);
		}
	}

	/**
	 * creates a new document and wraps it in an OpenntfDominoDocument
	 * 
	 * @return
	 * @throws NotesException
	 */
	protected OpenntfDominoDocument createDocument() throws NotesException {
		Database db = openDatabase();
		String server = com.ibm.xsp.model.domino.DominoUtils.getCurrentDatabase().getServer();

		if (!(StringUtil.isEmpty(server))) {
			String currentUser = com.ibm.xsp.model.domino.DominoUtils.getCurrentSession().getEffectiveUserName();
			int i = db.queryAccessPrivileges(currentUser);
			if (((i & Database.DBACL_CREATE_DOCS) == 0) && ((i & Database.DBACL_WRITE_PUBLIC_DOCS) == 0)) {
				throw new NoAccessSignal("User " + currentUser + " is has not enough privileges to create documents in "
						+ getDatabaseName());
			}
		}
		DominoDocument dominoDoc = DominoDocument.wrap(getDatabaseName(), db, getParentId(), getFormName(), getComputeWithForm(),
				getConcurrencyMode(), isAllowDeletedDocs(), getSaveLinksAs(), getWebQuerySaveAgent());

		OpenntfDominoDocument ntfDoc = wrap(dominoDoc, true);
		ntfDoc.setEditable(true);
		return ntfDoc;
	}

	/**
	 * Invoked if you call a xpage.xsp with a document ID
	 */
	@Override
	public DocumentDataContainer doOpenDocument(final FacesContext context) throws FacesExceptionEx {

		try {
			String noteId = getDocumentId();
			if (DominoUtils.isCategoryId(noteId)) {
				noteId = "";
			}
			if (StringUtil.isEmpty(noteId)) {
				return doNewDocument(context);
			}
			OpenntfDominoDocument ntfDoc = openDocument(noteId);
			return new DominoDocumentDataContainer(getBeanId(), getUniqueId(), ntfDoc);
		} catch (Exception localException) {
			throw new FacesExceptionEx(ResourceHandler.getString("DominoDocumentData.Couldnotopenthedocument"), localException);
		}
	}

	/**
	 * Opens the document with the given noteId
	 * 
	 * @param noteId
	 * @return
	 * @throws NotesException
	 */
	protected OpenntfDominoDocument openDocument(final String noteId) throws NotesException {

		Database db = openDatabase();
		boolean allowDelted = isAllowDeletedDocs();
		Document backendDoc = com.ibm.xsp.model.domino.DominoUtils.getDocumentById(db, noteId, allowDelted);

		if (backendDoc != null) {
			//				BackendBridge.setNoRecycle(backendDoc.getParentDatabase().getParent(), backendDoc, true);
		}

		DominoDocument dominoDoc = DominoDocument.wrap(getDatabaseName(), backendDoc, getComputeWithForm(), getConcurrencyMode(),
				allowDelted, getSaveLinksAs(), getWebQuerySaveAgent());
		OpenntfDominoDocument ntfDoc = wrap(dominoDoc, false);
		boolean editMode = "editDocument".equals(getEffectiveAction());
		ntfDoc.setEditable(editMode);
		return ntfDoc;
	}

	/**
	 * Wraps a {@link DominoDocument} in an {@link OpenntfDominoDocument} (or something else)
	 * 
	 * @param domino
	 * @param isNew
	 * @return
	 */
	protected OpenntfDominoDocument wrap(final DominoDocument domino, final boolean isNew) {
		return new OpenntfDominoDocument(domino);
	}

}
