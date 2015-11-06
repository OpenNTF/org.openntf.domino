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
		//		System.out.println("Getting subnavigator from view " + view.getName());
		ViewEntry entry = view.getEntryAtPosition(getProperty("position", String.class));
		if (entry != null) {
			//			System.out.println("Found entry at " + entry.getPosition());
		} else {
			//			System.out.println("Entry is null!");
		}
		ViewNavigator result = view.createViewNavFromChildren(entry, 100);
		//		System.out.println("Subnavigator has " + result.getCount() + " entries");
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
