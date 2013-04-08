package org.openntf.domino;

import java.io.InputStream;

public interface FileResource extends Base<lotus.domino.Base>, org.openntf.domino.types.Design, org.openntf.domino.types.DatabaseDescendant {
	public InputStream getInputStream();

	public String getMimeType();

	public String getName();

	public Database getParent();

	public boolean isHideFromWeb();

	public boolean isHideFromNotes();

	public boolean isNeedsRefresh();

	public boolean isPreventChanges();

	public boolean isPropagatePreventChanges();

	public boolean isReadOnly();

}
