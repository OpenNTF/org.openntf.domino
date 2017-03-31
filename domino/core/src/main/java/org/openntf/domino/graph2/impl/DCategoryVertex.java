package org.openntf.domino.graph2.impl;

import java.util.Map;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.graph2.DGraph;

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
		ViewEntry entry = view.getEntryAtPosition(getProperty("position", String.class));
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
