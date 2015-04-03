package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.social.Socializer;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Person")
public interface User extends DVertexFrame, Socializer, Editor {
	@TypedProperty("FirstName")
	public String getFirstName();

	@TypedProperty("FirstName")
	public void setFirstName(String firstName);

	@TypedProperty("LastName")
	public String getLastName();

	@TypedProperty("LastName")
	public void setLastName(String lastName);

	@TypedProperty("FullName")
	public String[] getFullName();

	@TypedProperty("FullName")
	public void setFullName(String[] fullName);
}
