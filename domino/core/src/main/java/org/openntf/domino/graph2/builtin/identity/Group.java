package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Group")
public interface Group extends Name {
	@TypedProperty("ListName")
	public String getListName();

	@TypedProperty("ListName")
	public void setListName(String listName);
}
