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
package org.openntf.domino.graph2.impl;

import java.util.Map;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.graph2.DGraph;

@SuppressWarnings("nls")
public class DCategoryVertex extends DVertex {
	private static final long serialVersionUID = 1L;
	//		private ViewNavigator nav_;
	private View view_;

	DCategoryVertex(final DGraph parent, final Map<String, Object> delegate, final View sourceView) {
		super(parent, delegate);
		view_ = sourceView;
		//		System.out.println("TEMP DEBUG Created a new DCategoryVertex");
	}

	@Override
	public Object getId() {
		return super.getId();
	}

	@Override
	public View getView() {
		return view_;
	}

	public void setView(final View view) {
		view_ = view;
	}

	public ViewNavigator getSubNavigator() {
		View view = getView();
		ViewEntry entry = view.getEntryAtPosition(getProperty("position", String.class)); //$NON-NLS-1$
		ViewNavigator result = view.createViewNavFromDescendants(entry, 100);
		if (getParent().getConfiguration().isSuppressSingleValueCategories()) {
			while (checkSkipCategory(result)) {
				//			System.out.println("TEMP DEBUG SubNavigator skipping category level...");
				result = dropSingleValueCategory(view, result);
			}
		}
		return result;
	}

	public static boolean checkSkipCategory(final ViewNavigator nav) {
		//		System.out.println("TEMP DEBUG Checking Skip Category...");
		boolean result = false;
		ViewEntry first = nav.getFirst();
		if (first == null) {
			return false;
		}
		if (first.isCategory()) {
			ViewEntry second = nav.getNextSibling(first);
			if (second == null) {
				result = true;
			}
		}
		nav.gotoFirst();
		return result;
	}

	public static ViewNavigator dropSingleValueCategory(final View view, final ViewNavigator nav) {
		ViewEntry first = nav.getFirst();
		ViewNavigator result = view.createViewNavFromDescendants(first);
		return result;
	}

	@Override
	public void applyChanges() {
		throw new UnsupportedOperationException("Category vertices cannot be updated. They are read-only.");
	}

	@Override
	public void commit() {
		throw new UnsupportedOperationException("Category vertices cannot be updated. They are read-only.");
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Category vertices cannot be updated. They are read-only.");
	}

	@Override
	protected boolean writeEdges() {
		throw new UnsupportedOperationException("Category vertices cannot be updated. They are read-only.");
	}
}
