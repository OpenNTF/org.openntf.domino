/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.xsp.model.domino.DominoViewData;
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
		try {
			return (Document) doc.getDocument(true);
		} catch (Throwable e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * Gets the back-end View using a DominoViewData datasource and converts to the org.openntf.domino version.<br/>
	 * Avoids the need to catch a NotesException
	 * 
	 * @param view
	 *            DominoViewData view datasource variable, e.g. #{view1}
	 * @return View back-end view
	 */
	public static View getBEView(final DominoViewData view) {
		try {
			return (View) view.getView();
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
