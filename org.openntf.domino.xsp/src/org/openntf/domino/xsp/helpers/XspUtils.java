/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.XSPUtil;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * @author Nathan T. Freeman
 * 
 */
public class XspUtils {
	private static final Logger log_ = Logger.getLogger(XspUtils.class.getName());

	private XspUtils() {

	}

	/**
	 * Gets the back-end Document using a DominoDocument datasource, applying changes in front end, and converts to org.openntf.domino
	 * version.<br/>
	 * Avoids the need to catch a NotesException
	 * 
	 * @param doc
	 *            DominoDocument datasource
	 * @return Document back-end document with front-end values applied, using doc.getDocument(true)
	 */
	public static Document getBEDoc(final DominoDocument doc) {
		Document beDoc;
		try {
			if (OpenntfDominoImplicitObjectFactory2.isAppGodMode(FacesContext.getCurrentInstance())) {
				beDoc = (Document) doc.getDocument(true);
			} else {
				beDoc = XSPUtil.wrap(doc.getDocument(true));
			}
			System.out.println(beDoc.getUniversalID());
		} catch (Throwable e) {
			DominoUtils.handleException(e);
			return null;
		}
		return beDoc;
	}

}
