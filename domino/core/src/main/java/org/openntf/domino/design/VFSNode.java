/*
 * Copyright 2015 - FOCONIS AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express o 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.design;

import java.io.IOException;
import java.util.Collection;

/**
 * A VirtualFileSystem Node. This Datastructure is used by the FOCONIS WebDAV Servlet, which exposes the whole Server Database Design.
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public interface VFSNode extends Comparable<VFSNode> {

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(VFSNode pathname);

	/**
	 * Deletes the VirtualFile. Currently, only DesigneElements can be deleted
	 * 
	 * @return true if object was deleted
	 */
	public boolean delete();

	/**
	 * Checks if the path exists
	 * 
	 * @return true if the path exists
	 */
	public boolean exists();

	/**
	 * Get the name of this node
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * Get the full path of this node
	 * 
	 * @return the full path
	 */
	public String getPath();

	/**
	 * Get the path in NSF (relative path to parent database)
	 * 
	 * @return the relative path
	 */
	public String getPathInNSF();

	/**
	 * Returns the parent node
	 * 
	 * @return the parent node
	 */
	public VFSNode getParent();

	/**
	 * Checks if this is a directory. I.e. has children
	 * 
	 * @return <code>true</code> if this is a directory
	 */
	public boolean isDirectory();

	/**
	 * Checks if this is a note. i.e. a design element
	 * 
	 * @return <code>true</code> if this is a note
	 */
	public boolean isNote();

	/**
	 * Checks if this is a database node.
	 * 
	 * @return <code>true</code> if this is a database node
	 */
	public boolean isNSF();

	/**
	 * Returns the LastModified timestamp of this resource
	 * 
	 * @return the LastModified timestamp.
	 */
	public long lastModified();

	/**
	 * List all children of this node
	 * 
	 * @return the children of this node
	 */
	public Collection<VFSNode> list();

	/**
	 * Change directory
	 * 
	 * @param dir
	 *            the directory to change to
	 * @return the VFSNode in this directory
	 */
	public VFSNode cd(String dir);

	public boolean mkdir();

	/**
	 * Returns the binary content of the DesignElement
	 * 
	 * @param converter
	 *            the DXL-Converter that should be used
	 * @return the binary content
	 */
	public byte[] getContent(DxlConverter converter) throws IOException;

	/**
	 * Set the binary content
	 * 
	 * @param converter
	 *            the DXL-Converter to use
	 * @param buf
	 *            the binary content
	 */
	public void setContent(DxlConverter converter, final byte[] buf) throws IOException;

	/**
	 * Returns the ContentLength of this entry
	 * 
	 * @param converter
	 *            the DXL-Converter to use
	 * @return the content length
	 */
	public long getContentLength(DxlConverter converter);

	/**
	 * Refresh this node to ensure that the content is actual
	 */
	public void refresh();

}
