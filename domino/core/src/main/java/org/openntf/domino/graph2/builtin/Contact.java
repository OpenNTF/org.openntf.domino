package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeField("form")
@TypeValue("Contact")
public interface Contact extends VertexFrame {
	@TypedProperty("firstName")
	public String getFirstName();

	public void setFirstName(String firstName);

	@TypedProperty("lastName")
	public String getLastName();

	public void setLastName(String lastName);

	@TypedProperty(derived = true, value = "firstName + \" \" + lastName")
	public String getFullName();
}
