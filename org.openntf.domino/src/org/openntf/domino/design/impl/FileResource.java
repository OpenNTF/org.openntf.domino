package org.openntf.domino.design.impl;

import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Document;

public class FileResource extends AbstractDesignBase implements org.openntf.domino.design.FileResource {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());

	public FileResource(final Document document) {
		super(document);
	}

	public InputStream getInputStream() {
		return getDocument().getFirstItem("$FileData").getInputStream();
	}

	public String getMimeType() {
		return getDocument().getItemValueString("$MimeType");
	}

	@Override
	public boolean isReadOnly() {
		return getFlags().contains("&");
	}

}
