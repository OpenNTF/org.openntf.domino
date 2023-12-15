/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.big;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
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

	public void setSourceColl(ViewEntryCollection coll);

}
