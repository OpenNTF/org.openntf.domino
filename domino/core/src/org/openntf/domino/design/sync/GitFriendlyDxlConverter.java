package org.openntf.domino.design.sync;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;

public class GitFriendlyDxlConverter extends DefaultDxlConverter {

	public GitFriendlyDxlConverter(final boolean rawExportEnabled) {
		super(rawExportEnabled);
	}

	private Transformer importTransformer = XMLDocument.createTransformer(GitFriendlyDxlConverter.class
			.getResourceAsStream("importFilter.xslt"));
	private Transformer exportTransformer = XMLDocument.createTransformer(GitFriendlyDxlConverter.class
			.getResourceAsStream("exportFilter.xslt"));

	@Override
	public void writeDesignXML(final XMLDocument dxl, final File file) throws IOException {
		writeXml(dxl, exportTransformer, file);

	}

	@Override
	public void writeMetaXML(final XMLDocument dxl, final File metaFile) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDxlImportString(final XMLDocument dxl) throws IOException {
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(dxl.getNode());
		try {
			importTransformer.transform(source, result);
		} catch (TransformerException e) {
			DominoUtils.handleException(e);
		}
		return result.getWriter().toString();
	}

	@Override
	public boolean isMetadataEnabled() {
		return false;
	}
}
