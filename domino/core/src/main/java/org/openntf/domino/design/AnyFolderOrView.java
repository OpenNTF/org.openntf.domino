/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.design;

import org.openntf.domino.design.impl.DesignColumn;

/**
 * @author jgallagher
 *
 */
public interface AnyFolderOrView extends DesignBaseNamed {

	public enum OnRefreshType {
		DISPLAY_INDICATOR("displayindicator"), REFRESH_DISPLAY("refresh"), REFRESH_FROM_TOP("refreshtop"),
		REFRESH_FROM_BOTTOM("refreshbottom");

		String propName;

		private OnRefreshType(final String onRefreshUISetting) {
			propName = onRefreshUISetting;
		}

		public String getPropertyName() {
			return propName;
		}
	}

	public DesignColumn addColumn();

	public DesignColumnList getColumns();

	public void swapColumns(int a, int b);

	public org.openntf.domino.View getView();

	public OnRefreshType getOnRefreshUISetting();

	public void setOnRefreshUISetting(OnRefreshType onRefreshUISetting);
}
