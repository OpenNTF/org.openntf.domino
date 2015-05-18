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
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.design.VFSRootNode;

/**
 * Abstract implementation of a VFS-Node
 * 
 * @author Roland Praml, FOCONIS AG
 *
 * @param <T>
 *            the encapsulated type
 */
public abstract class VFSAbstractNode<T> implements VFSNode, Serializable {
	private static final long serialVersionUID = 1L;

	private VFSNode parent;
	private String name;
	private boolean initialized;
	private Map<String, VFSNode> children = new ConcurrentHashMap<String, VFSNode>();

	/**
	 * Constructor. Create new node
	 * 
	 * @param parent
	 *            the parent of this node
	 * @param name
	 *            the new name
	 */
	public VFSAbstractNode(final VFSNode parent, final String name) {
		this.parent = parent;
		this.name = name;
	}

	/**
	 * Compare this node with an other
	 */
	@Override
	public int compareTo(final VFSNode other) {
		return getPath().compareTo(other.getPath());
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof VFSNode)) {
			return false;
		}
		return getPath().equals(((VFSNode) obj).getPath());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final VFSNode getParent() {
		return parent;
	}

	@Override
	public Collection<VFSNode> list() {
		List<VFSNode> ret = new ArrayList<VFSNode>(getChildren().values());
		Collections.sort(ret);
		return ret;
	}

	//	protected abstract void init();

	@Override
	public String getPath() {
		if (getParent() == null) {
			return getName();
		}
		String parentDir = getParent().getPath();
		if (!parentDir.isEmpty()) {
			if (parentDir.endsWith("/")) {
				return parentDir + getName();
			} else {
				return parentDir + "/" + getName();
			}
		}
		return getName();
	}

	/**
	 * Used during construction to build the datastructure
	 * 
	 * @param components
	 *            the components of the path
	 * @param t
	 *            the object that should be encapsulated in the leaf
	 * @param level
	 *            the level of recursive calls
	 */
	protected void add(final String[] components, final T t, final int level) {

		if (components.length == level + 1) {
			children.put(components[level], newLeaf(components[level], t));
		} else {
			String dir = components[level];
			VFSNode child = children.get(dir);

			if (child == null) {
				child = newNode(dir);
				children.put(dir, child);
			}
			((VFSAbstractNode<T>) child).add(components, t, level + 1);
		}
	}

	/**
	 * Create a new (directory) node.
	 * 
	 * @param dir
	 *            the directory name
	 * @return the new node
	 */
	protected abstract VFSNode newNode(String dir);

	/**
	 * Create a new (file) node.
	 * 
	 * @param name
	 *            the name
	 * @param t
	 *            the object to encapsulate (nsf or designelement)
	 * @return
	 */
	protected abstract VFSNode newLeaf(String name, T t);

	/**
	 * Create a new virtual node (node that does not yet exist, but describes the path to an object)
	 * 
	 * @param dir
	 *            the dir
	 * @return the new virtual node
	 */
	protected VFSNode newVirtual(final String dir) {
		return new VFSVirtualNode(this, dir);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.VFSNode#cd(java.lang.String)
	 */
	@Override
	public VFSNode cd(String dir) {
		if (dir.startsWith("./")) { // ignore
			dir = dir.substring(2);
		}
		if ("".equals(dir)) { // same dir
			return this;
		}
		if ("..".equals(dir)) { // upper dir 
			return this.parent == null ? this : this.parent;
		}
		if (dir.startsWith("/")) { // root dir
			VFSNode ret = this;
			while (ret.getParent() != null) {
				ret = ret.getParent();
			}
			return ret.cd(dir.substring(1));
		}

		// process the rest
		int pos = dir.indexOf('/');

		String newDir = pos < 0 ? dir : dir.substring(0, pos);

		VFSNode child = getChildren().get(newDir);
		if (child == null) {
			child = newVirtual(newDir);
			getChildren().put(newDir, child);
		}
		if (pos >= 0) {
			return child.cd(dir.substring(pos + 1));
		}
		return child;
	}

	public synchronized void clear() {
		children.clear();
		initialized = false;
	}

	protected void init() {

	}

	/**
	 * Returns the children. Initialization is done on demand
	 * 
	 * @return the children
	 */
	protected Map<String, VFSNode> getChildren() {
		if (!initialized) {
			synchronized (this) {
				if (!initialized) {
					initialized = true;
					init();
				}
			}
		}
		return children;
	}

	//	/**
	//	 * Flush this node.
	//	 */
	//	protected synchronized void flush() {
	//		children.clear();
	//		initialized = false;
	//	}

	@Override
	public boolean mkdir() {
		return false;
	}

	@Override
	public void getContent(final DxlConverter converter, final OutputStream os) throws IOException {
		throw new UnsupportedOperationException();
	};

	@Override
	public long getContentLength(final DxlConverter converter) {
		return 0;
	}

	@Override
	public void setContent(final DxlConverter converter, final InputStream is, final boolean sign) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRelativePath() {
		if (getParent() == null) {
			throw new IllegalStateException(this + " is not a valid NSF Resource");
		}
		if (getParent().isDatabase()) {
			return getName();
		}
		return getParent().getRelativePath() + "/" + getName();
	}

	protected VFSRootNode getVFSRootNode() {
		VFSNode ret = this;
		while (ret.getParent() != null) {
			ret = ret.getParent();
		}
		return (VFSRootNode) ret;
	}

	protected VFSDatabaseNode getVFSDatabaseNode() {
		VFSNode ret = this;
		while (ret != null && !ret.isDatabase()) {
			ret = ret.getParent();
		}
		return (VFSDatabaseNode) ret;
	}

	@Override
	public String toString() {
		return getPath();
	}
}
