package org.openntf.domino.design.impl;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

public class OnDiskFile implements Serializable {

	private static final long serialVersionUID = -3298261314433290242L;

	public enum State {
		EXPORT, IMPORT, SYNC, CREATE, DELETE, WAS_CREATED, WAS_DELETED;
	}

	private File file_;
	private String name_;
	private long timeStamp_;
	private ODPMapping odpMapping;
	private transient State state = State.WAS_DELETED;

	public OnDiskFile(final File parent, final File file) {
		file_ = file;
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
}
