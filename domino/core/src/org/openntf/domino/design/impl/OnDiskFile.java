/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

import org.openntf.domino.design.SyncObject;

/**
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public class OnDiskFile implements SyncObject, Serializable {

	private static final long serialVersionUID = -3298261314433290242L;

	public enum State {
		EXPORT, IMPORT, SYNC, CREATE, DELETE, WAS_CREATED, WAS_DELETED;
	}

	private File file_;
	private String name_;
	private long timeStamp_;
	private DesignMapping odpMapping;
	private transient State state;
	private transient boolean processed;

	public OnDiskFile(final File parent, final File file) {
		file_ = file;
		setProcessed(false);

		// example:
		// parent 	= C:\documents\odp\
		// file 	= C:\documents\odp\Code\Scriptlibraries\lib.lss
		// odpFolder= C:\documents\odp\Code\Scriptlibraries
		// relUri 	= lib.lss

		odpMapping = DesignMapping.valueOf(parent, file);
		File odpFolder = new File(parent, odpMapping.getOnDiskFolder());
		URI relUri = odpFolder.toURI().relativize(file.toURI());

		String ext = odpMapping.getOnDiskFileExtension();

		if (ext == null) {
			// no extension, so use the relative file uri
			name_ = relUri.getPath();
		} else if (ext.equals("*")) {
			// name is "*", so use the unescaped part.
			name_ = relUri.getPath();
		} else if (ext.startsWith(".")) {
			name_ = relUri.getPath();
		} else {
			name_ = ext;
		}

	}

	protected static boolean checkChild(final File maybeChild, final File possibleParent) {
		URI parentURI = possibleParent.toURI();
		URI childURI = maybeChild.toURI();
		return !parentURI.relativize(childURI).isAbsolute();
	}

	public String getName() {
		return name_;
	}

	public File getFile() {
		return file_;
	}

	public Class<?> getImplementingClass() {
		return odpMapping.getImplClass();
	}

	public State getState() {
		return state;
	}

	public long getTimeStamp() {
		return timeStamp_;
	}

	public void setState(final State state) {
		this.state = state;
	}

	public String getFullName() {
		return getImplementingClass().getName() + ":" + getName();
	}

	public void setTimeStamp(final long timeStamp) {
		this.timeStamp_ = timeStamp;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(final boolean processed) {
		this.processed = processed;
	}
}
