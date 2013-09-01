/**
 * 
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public class DesignView extends AbstractFolder implements org.openntf.domino.design.DesignView {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignView.class.getName());

	/**
	 * @param document
	 */
	protected DesignView(final Document document) {
		super(document);
	}

	protected DesignView(final Database database) {
		super(database);

		try {
			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_view.xml");
			loadDxl(is);
			is.close();
		} catch (IOException e) {
			DominoUtils.handleException(e);
		}
	}

	public String getSelectionFormula() {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if (formula != null) {
			return formula.getText();
		}
		return null;
	}

	public void setSelectionFormula(final String selectionFormula) {
		XMLNode formula = getDxl().selectSingleNode("/view/code[@event='selection']/formula");
		if (formula != null) {
			formula.setTextContent(selectionFormula);
		}
	}
}
