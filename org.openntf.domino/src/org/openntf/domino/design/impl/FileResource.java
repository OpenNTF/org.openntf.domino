package org.openntf.domino.design.impl;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Document;

public class FileResource extends AbstractDesignBase implements org.openntf.domino.design.FileResource {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());

	public FileResource(final Document document) {
		super(document);
	}

	@Override
	public void setName(final String name) {
		if (name.contains("|")) {
			String[] bits = name.split("\\|");
			getDxlNode("/fileresource").setAttribute("name", name);

			List<String> aliases = getAliases();
			for (int i = 1; i < bits.length; i++) {
				aliases.add(bits[1]);
			}
			setAliases(aliases);
		}
	}

	@Override
	public void setAlias(final String alias) {
		getDxlNode("/fileresource").setAttribute("alias", alias);
	}

	@Override
	public void setAliases(final Iterable<String> aliases) {
		StringBuilder result = new StringBuilder();
		boolean appended = false;
		for (String alias : aliases) {
			if (alias != null) {
				if (appended) {
					result.append('|');
				} else {
					appended = true;
				}
				result.append(alias);
			}
		}

		setAlias(result.toString());
	}

	@Override
	public byte[] getFileData() {
		return parseBase64Binary(getDxlNode("/fileresource/filedata").getTextContent());
	}

	@Override
	public void setFileData(final byte[] fileData) {
		String base64 = printBase64Binary(fileData);
		getDxlNode("/fileresource/filedata").setTextContent(base64);
	}

	@Override
	public String getMimeType() {
		return getDocument().getItemValueString("$MimeType");
	}

	@Override
	public void setMimeType(final String mimeType) {
		getDxlNode("/fileresource").setAttribute("mimetype", mimeType);
	}

	@Override
	public boolean isReadOnly() {
		return getFlags().contains("&");
	}

}
