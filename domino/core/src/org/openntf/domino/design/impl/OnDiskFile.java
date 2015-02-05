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
	private ODPMapping odpMapping;
	private transient State state;
	private transient boolean processed;

	public OnDiskFile(final File parent, final File file) {
		file_ = file;
		setProcessed(false);
		for (ODPMapping mapping : ODPMapping.values()) {
			File odpFolder = new File(parent, mapping.getFolder());

			if (checkChild(file, odpFolder)) {
				String ext = mapping.getOnDiskFileExtension();
				String filePath = file.getAbsolutePath().replace("\\", "/");
				String folderPath = odpFolder.getAbsolutePath().replace("\\", "/");

				if (ext == null || filePath.endsWith(ext) || ext.equals("*")) {
					odpMapping = mapping;
					if (ext == null || ext.equals("*")) {
						name_ = filePath.substring(folderPath.length() + 1);
						return;
					} else if (ext.startsWith(".")) {
						if (filePath.endsWith(ext)) {
							name_ = filePath.substring(folderPath.length() + 1);
							return;
						}
					} else {
						name_ = ext;
						return;
					}
				}
			}
		}
		throw new IllegalArgumentException("Mapping not found for " + file.getAbsolutePath());

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
		return odpMapping.getInstanceClass();
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
		return odpMapping.getInstanceClass().getName() + ":" + getName();
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
