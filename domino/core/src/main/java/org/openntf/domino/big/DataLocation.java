package org.openntf.domino.big;

import java.io.Externalizable;

public interface DataLocation extends Externalizable {

	public NoteCoordinate getNoteCoordinate();

	public void setNoteCoordinate(NoteCoordinate coordinate);

	public String getItemName();

	public void setItemName(String itemname);

	public String getAttachmentName();

	public void setAttachmentName(String attachmentName);

	public boolean isSecure();

	public int getSecurityType();

	public void setSecurityType(int securityType);

	public int getBegins();

	public void setBegins(int begins);

	public int getEnds();

	public void setEnds(int ends);

}
