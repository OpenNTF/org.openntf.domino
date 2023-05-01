/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.nsfdata.impldxl.item;

import java.util.Base64;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.utils.xml.XMLNode;

public class DXLItemObjectFile extends DXLItemObject {
	private static final long serialVersionUID = 1L;

	public static enum HostType {
		MSDOS, BYTEARRAYPAGE, BYTEARRAYEXT, STREAM
	}

	public static enum CompressionType {
		NONE
	}

	public static enum Flag {
		SIGN, STOREDINDOC
	}

	private final HostType hostType_;
	private final CompressionType compressionType_;
	private final Set<Flag> flags_;
	private final String encoding_;
	private final String fileName_;
	private final long fileSize_;
	private final Date fileCreated_;
	private final Date fileModified_;
	private final byte[] value_;

	protected DXLItemObjectFile(final XMLNode node, final int dupItemId) {
		super(node, dupItemId);

		XMLNode objectNode = node.getFirstChildElement();
		XMLNode fileNode = objectNode.getFirstChildElement();

		String hostType = fileNode.getAttribute("hosttype"); //$NON-NLS-1$
		if (!hostType.isEmpty()) {
			hostType_ = HostType.valueOf(hostType.toUpperCase());
		} else {
			hostType_ = null;
		}
		String compressionType = fileNode.getAttribute("compression"); //$NON-NLS-1$
		if (!compressionType.isEmpty()) {
			compressionType_ = CompressionType.valueOf(compressionType.toUpperCase());
		} else {
			compressionType_ = null;
		}
		encoding_ = fileNode.getAttribute("encoding"); //$NON-NLS-1$
		fileName_ = fileNode.getAttribute("name"); //$NON-NLS-1$
		fileSize_ = Long.parseLong(fileNode.getAttribute("size"), 10); //$NON-NLS-1$

		flags_ = EnumSet.noneOf(Flag.class);
		for (String flag : fileNode.getAttribute("flags").split("[,\\s]")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (!flag.trim().isEmpty()) {
				flags_.add(Flag.valueOf(flag.toUpperCase()));
			}
		}

		XMLNode createdNode = fileNode.selectSingleNode("./created/datetime"); //$NON-NLS-1$
		if (!createdNode.getText().isEmpty()) {
			fileCreated_ = DXLItemFactory.createDateTime(createdNode).toDate();
		} else {
			fileCreated_ = null;
		}
		XMLNode modifiedNode = fileNode.selectSingleNode("./modified/datetime"); //$NON-NLS-1$
		if (!modifiedNode.getText().isEmpty()) {
			fileModified_ = DXLItemFactory.createDateTime(modifiedNode).toDate();
		} else {
			fileModified_ = null;
		}

		value_ = Base64.getDecoder().decode(fileNode.selectSingleNode("./filedata").getText()); //$NON-NLS-1$
	}

	@Override
	public byte[] getValue() {
		return value_;
	}

	@Override
	public byte[] getBytes() {
		return value_;
	}

	public HostType getHostType() {
		return hostType_;
	}

	public CompressionType getCompressionType() {
		return compressionType_;
	}

	public String getEncoding() {
		return encoding_;
	}

	public String getFileName() {
		return fileName_;
	}

	public long getFileSize() {
		return fileSize_;
	}

	public Date getFileCreated() {
		return fileCreated_;
	}

	public Date getFileModified() {
		return fileModified_;
	}
}
