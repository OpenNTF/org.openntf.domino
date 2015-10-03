package org.openntf.domino.big;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

public interface ViewEntryCoordinate extends NoteCoordinate {
	public static enum Utils {
		;
		public static org.openntf.domino.big.ViewEntryCoordinate getViewEntryCoordinate(final CharSequence metaversalid) {
			return new org.openntf.domino.big.impl.ViewEntryCoordinate(metaversalid);
		}
	}

	public String getPosition();

	public String getEntryType();

	public ViewEntry getViewEntry();

	public View getView();

	public Document getViewDocument();

	public void setSourceNav(ViewNavigator nav);

}
