package org.openntf.domino.design;

import java.util.Set;

import org.openntf.domino.DbDirectory;

public interface VFSRootNode extends VFSNode {

	public Set<String> getVirtualFolders();

	public void refresh(DbDirectory directory);

}
