package org.openntf.domino.big;

import java.io.Externalizable;

import org.openntf.domino.ViewEntry;

public interface ViewEntryCoordinate extends Externalizable {

	public String getReplicaId();

	public Long getReplicaLong();

	public String getViewUNID();

	public String getPosition();

	public ViewEntry getViewEntry();

}
