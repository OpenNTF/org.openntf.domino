/**
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
import com.ibm.domino.napi.c.BackendBridge;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.acl.NoAccessSignal;
import com.ibm.xsp.domino.ResourceHandler;
import com.ibm.xsp.model.DocumentDataContainer;
import com.ibm.xsp.model.domino.DominoDocumentData;
import com.ibm.xsp.model.domino.DominoDocumentDataContainer;
import com.ibm.xsp.model.domino.DominoUtils;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * @author praml
 *
 */
/**
 * Hier wird von der Original-XSP-Datatasource (DominoDocumentData) abgeleitet damit wir �ber alle Aktionen Kontrolle haben. doNewDocument
 * und doOpenDocument werden bei Verwendung der Datasource "Foconis Document" von der XPage automatisch aufgerufen und m�ssen einen
 * DominoDocumentDataContainer zur�ckliefern. Unser Trick besteht darin, dass wir in diesen DominoDocumentDataContainer anstatt eines
 * DominoDocument ein (davon abgeleitetes) FocDominoDocument packen, das sich f�r uns dann um die Ausleitung der Events k�mmert.
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

	protected Database openDatabase() {
		try {
			return (Database) openDatabaseMethod.invoke(this, (Object[]) null);
		} catch (Exception e) {
			org.openntf.domino.utils.DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * Wird aufgerufen, wenn ein neues Dokument erzeugt werden soll. Um das Anlegen des Dokumentes k�mmert sich die Eltern-Methode. Das
	 * Dokument wird hier lediglich umgepackt
	 */
	@SuppressWarnings("nls")
	@Override
	public DocumentDataContainer doNewDocument(final FacesContext paramFacesContext) throws FacesExceptionEx {
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
	 * Wird aufgerufen, wenn ein vorhandenes Dokument ge�ffnet werden soll. Um das Holen des Dokumentes k�mmert sich die Eltern-Methode. Das
	 * Dokument wird hier lediglich umgepackt
	 */
	@Override
	public DocumentDataContainer doOpenDocument(final FacesContext paramFacesContext) throws FacesExceptionEx {

		try {
			System.out.println("doOpenDocument in plugin");
			String noteId = getDocumentId();
			if (DominoUtils.isCategoryId(noteId)) {
				noteId = "";
			}
			if (StringUtil.isEmpty(noteId)) {
				return doNewDocument(paramFacesContext);
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
			BackendBridge.setNoRecycle(backendDoc.getParentDatabase().getParent(), backendDoc, true);
		}

		DominoDocument dominoDoc = DominoDocument.wrap(getDatabaseName(), backendDoc, getComputeWithForm(), getConcurrencyMode(),
				allowDelted, getSaveLinksAs(), getWebQuerySaveAgent());
		OpenntfDominoDocument ntfDoc = wrap(dominoDoc, false);
		boolean editMode = "editDocument".equals(getEffectiveAction());
		ntfDoc.setEditable(editMode);
		return ntfDoc;
	}

	protected OpenntfDominoDocument wrap(final DominoDocument domino, final boolean isNew) {
		return new OpenntfDominoDocument(domino);
	}

}
