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

import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.VFSNode;

/**
 * A Virtual directory for "Code" and so on
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class VFSDatabaseDirNode extends VFSAbstractNode<DesignBase> {
	private static final long serialVersionUID = 1L;

	public VFSDatabaseDirNode(final VFSNode parent, final String name) {
		super(parent, name);
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

	@Override
	public boolean isNote() {
		return false;
	}

	@Override
	public boolean isDatabase() {
		return false;
	}

	@Override
	public long lastModified() {
		return 0;
	}

	@Override
	protected VFSNode newNode(final String name) {
		return new VFSDatabaseDirNode(this, name);
	}

	@Override
	protected VFSNode newLeaf(final String name, final DesignBase t) {
		return new VFSDatabaseDesignElementNode(this, name, t);
	}

}
