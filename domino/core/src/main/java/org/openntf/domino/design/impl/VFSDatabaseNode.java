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

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignBase;
import org.openntf.domino.design.DesignCollection;
import org.openntf.domino.design.VFSNode;
import org.openntf.domino.design.sync.OnDiskDesign;
import org.openntf.domino.helpers.DatabaseMetaData;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.designer.domino.napi.NotesSession;

/**
 * A node representing a Database. Children of this node are design elements
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class VFSDatabaseNode extends VFSAbstractNode<DesignBase> implements org.openntf.domino.design.VFSDatabaseNode {
	public static final Logger log_ = Logger.getLogger(VFSDatabaseNode.class.getName());
	private static final long serialVersionUID = 1L;
	private DatabaseMetaData metaData;

	public VFSDatabaseNode(final VFSNode parent, final String name, final DatabaseMetaData md) {
		super(parent, name);
		this.metaData = md;
	}

	public Database getDatabase() {
		Session session = SessionType.CURRENT.get();
		return session.getDatabase(metaData.getApiPath());
	}

	@Override
	public boolean delete() {
		// DELETE not yet supported
		return false;
	}

	@Override
	public boolean exists() {
		// DB exists as long as DELETE is not supported
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
		return true;
	}

	long oldLastModified = -1;

	@Override
	protected Map<String, VFSNode> getChildren() {
		long l = lastModified();
		if (l != oldLastModified && oldLastModified != -1) {
			refresh();
			log_.info("DesignChange detected");
		}
		oldLastModified = l;
		return super.getChildren();
	}

	@Override
	public long lastModified() {
		try {
			return lastModifiedNAPI();
		} catch (Throwable t) {
			// no napi
		}
		Date lm = metaData.getLastModifiedDate();
		if (lm == null) {
			return 0;
		} else {
			return lm.getTime();
		}
	}

	private long lastModifiedNAPI() throws Exception {
		return NotesSession.getLastNonDataModificationDateByName(metaData.getServer(), metaData.getFilePath());
	}

	@Override
	protected void init() {
		DesignCollection<DesignBase> coll = getDatabase().getDesign().getDesignElements();
		for (DesignBase el : coll) {
			String fileName = OnDiskDesign.getOnDiskPath(el);
			String[] components = fileName.split("/");
			add(components, el, 0);
		}
		Set<String> folders = getVFSRootNode().getVirtualFolders();
		Iterator<String> it = folders.iterator();
		while (it.hasNext()) {
			String folderPath = it.next();
			if (folderPath.startsWith(getPath())) {
				VFSNode folder = getVFSRootNode().cd(folderPath);
				if (folder.exists()) {
					it.remove();
				} else {
					folder.mkdir();
				}
			}
		}
	}

	@Override
	public void refresh() {
		getDatabase().getDesign().flush();
		clear();
	}

	@Override
	protected VFSNode newNode(final String dir) {
		return new VFSDatabaseDirNode(this, dir);
	}

	@Override
	protected VFSNode newLeaf(final String name, final DesignBase t) {
		return new VFSDatabaseDesignElementNode(this, name, t);
	}

}
