package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Server")
public interface Server extends Name {
	@TypedProperty("ServerName")
	public String getServerName();

	@TypedProperty("ServerName")
	public void setServerName(String serverName);
}
