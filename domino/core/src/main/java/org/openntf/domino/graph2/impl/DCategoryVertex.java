package org.openntf.domino.graph2.impl;

import java.util.Map;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.graph2.DGraph;

public class DCategoryVertex extends DVertex {
	//	private ViewNavigator nav_;
	private View view_;

	DCategoryVertex(final DGraph parent, final Map<String, Object> delegate) {
		super(parent, delegate);
	}

	@Override
	public Object getId() {
		return super.getId();
	}

	public View getView() {
		return view_;
	}

	public void setView(final View view) {
		view_ = view;
	}

	public ViewNavigator getSubNavigator() {
		ViewEntry entry = getView().getEntryAtPosition(getProperty("position", String.class));
		ViewNavigator result = getView().createViewNavFrom(entry, 100);
		return result;
	}

}
