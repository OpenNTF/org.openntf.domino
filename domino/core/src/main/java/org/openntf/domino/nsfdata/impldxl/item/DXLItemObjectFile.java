package org.openntf.domino.nsfdata.impldxl.item;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

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

		String hostType = fileNode.getAttribute("hosttype");
		if (!hostType.isEmpty()) {
			hostType_ = HostType.valueOf(hostType.toUpperCase());
		} else {
			hostType_ = null;
		}
		String compressionType = fileNode.getAttribute("compression");
		if (!compressionType.isEmpty()) {
			compressionType_ = CompressionType.valueOf(compressionType.toUpperCase());
		} else {
			compressionType_ = null;
		}
		encoding_ = fileNode.getAttribute("encoding");
		fileName_ = fileNode.getAttribute("name");
		fileSize_ = Long.parseLong(fileNode.getAttribute("size"), 10);

		flags_ = EnumSet.noneOf(Flag.class);
		for (String flag : fileNode.getAttribute("flags").split("[,\\s]")) {
			if (!flag.trim().isEmpty()) {
				flags_.add(Flag.valueOf(flag.toUpperCase()));
			}
		}

		XMLNode createdNode = fileNode.selectSingleNode("./created/datetime");
		if (!createdNode.getText().isEmpty()) {
			fileCreated_ = DXLItemFactory.createDateTime(createdNode).toDate();
		} else {
			fileCreated_ = null;
		}
		XMLNode modifiedNode = fileNode.selectSingleNode("./modified/datetime");
		if (!modifiedNode.getText().isEmpty()) {
			fileModified_ = DXLItemFactory.createDateTime(modifiedNode).toDate();
		} else {
			fileModified_ = null;
		}

		value_ = parseBase64Binary(fileNode.selectSingleNode("./filedata").getText());
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
