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

import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.utils.DominoUtils;

/**
 * A Virtual VFS Node that does not yet exist (needed to create subfolder)
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class VFSVirtualNode extends VFSAbstractNode<Void> {

	private static final long serialVersionUID = 1L;
	VFSNode delegate;
	private boolean isDir;

	public VFSVirtualNode(final VFSNode dbDirectoryNode, final String name) {
		super(dbDirectoryNode, name);
	}

	public VFSNode getDelegate() {
		return delegate;
	}

	@Override
	public boolean delete() {
		if (delegate != null) {
			return delegate.delete();
		}
		return false;
	}

	@Override
	public boolean exists() {
		if (delegate != null) {
			return delegate.exists();
		}
		return isDir; // it is mkDir'd
	}

	@Override
	public boolean isDirectory() {
		return isDir;
	}

	@Override
	public boolean isNote() {
		if (delegate != null) {
			return delegate.isNote();
		}
		return false;
	}

	@Override
	public boolean isDatabase() {
		return false;
	}

	@Override
	public long lastModified() {
		if (delegate != null) {
			return delegate.lastModified();
		}
		return 0;
	}

	@Override
	protected VFSNode newNode(final String dir) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected VFSNode newLeaf(final String name, final Void t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setContent(final DxlConverter converter, final InputStream is, final boolean sign) throws IOException {
		// TODO Auto-generated method stub
		if (isDir) {
			throw new UnsupportedOperationException();
		}
		if (delegate == null) {
			DesignFactory mapping = DesignFactory.valueOf(this);
			try {
				DesignBase design = mapping.getImplClass().newInstance();
				delegate = new VFSDatabaseDesignElementNode(getParent(), getName(), design);
				delegate.setContent(converter, is, sign);

			} catch (InstantiationException e) {
				DominoUtils.handleException(e);
			} catch (IllegalAccessException e) {
				DominoUtils.handleException(e);
			}
		}
	}

	@Override
	public void getContent(final DxlConverter converter, final OutputStream os) throws IOException {
		if (delegate != null) {
			delegate.getContent(converter, os);
		} else {
			super.getContent(converter, os);
		}
	}

	@Override
	public long getContentLength(final DxlConverter converter) {
		if (delegate != null) {
			return delegate.getContentLength(converter);
		}
		return super.getContentLength(converter);
	}

	@Override
	public boolean mkdir() {
		getVFSRootNode().getVirtualFolders().add(getPath());
		isDir = true;
		return true;
	}

}
