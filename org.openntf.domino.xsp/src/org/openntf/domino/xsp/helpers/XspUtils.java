/**
 * 
 */
package org.openntf.domino.xsp.helpers;

import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.openntf.domino.Base;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.XSPUtil;

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
			if (OpenntfDominoImplicitObjectFactory2.isAppGodMode(FacesContext.getCurrentInstance())) {
				return (org.openntf.domino.View) view.getView();
			} else {
				return Factory.fromLotus(view.getView(), org.openntf.domino.ViewEntry.class, (Base) view.getView().getParent());
			}
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
