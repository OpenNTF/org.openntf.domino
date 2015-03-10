package org.openntf.domino.design.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openntf.domino.design.OnDiskConverter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.xml.sax.SAXException;

public class DefaultConverter implements OnDiskConverter {

	protected Transformer defaultTransformer = XMLDocument.createTransformer(null);
	private Transformer metaTransformer = XMLDocument.createTransformer(GitFriendlyConverter.class.getResourceAsStream("metatFilter.xslt"));
	private boolean rawExportEnabled_;

	public DefaultConverter(final boolean rawExportEnabled) {
		super();
		rawExportEnabled_ = rawExportEnabled;
	}

	//	public Transformer getImportTransformer() {
	//		if (importTransformer_ == null) {
	//			importTransformer_ = createTransformer(OnDiskDesignSync.class.getResourceAsStream("importFilter.xslt"));
	//		}
	//		return importTransformer_;
	//	}
	//
	//	public Transformer getExportTransformer() {
	//		if (exportTransformer_ == null) {
	//			exportTransformer_ = createTransformer(OnDiskDesignSync.class.getResourceAsStream("exportFilter.xslt"));
	//		}
	//		return exportTransformer_;
	//	}

	protected void writeXml(final XMLDocument dxl, final Transformer transformer, final File outputFile) throws IOException {
		try {
			// StreamResult result = new StreamResult(out); - This constructor has problems with german umlauts
			// See: http://comments.gmane.org/gmane.text.xml.saxon.help/6790
			StreamResult result = new StreamResult(outputFile.toURI().toString());
			DOMSource source = new DOMSource(dxl.getNode());
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	protected XMLDocument readXML(final File file) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(file);
		try {
			XMLDocument ret = new XMLDocument();
			try {
				ret.loadInputStream(fis);
				return ret;
			} catch (SAXException e) {
				DominoUtils.handleException(e);
			} catch (ParserConfigurationException e) {
				DominoUtils.handleException(e);
			}
			return null;
		} finally {
			fis.close();
		}
	}

	@Override
	public void writeDesignXML(final XMLDocument dxl, final File file) throws IOException {
		writeXml(dxl, defaultTransformer, file);

	}

	@Override
	public XMLDocument readDesignXML(final File file) throws IOException {
		return readXML(file);
	}

	@Override
	public void writeMetaXML(final XMLDocument dxl, final File metaFile) throws IOException {
		writeXml(dxl, metaTransformer, metaFile);

	}

	@Override
	public XMLDocument readMetaXML(final File metaFile) throws IOException {
		return readXML(metaFile);
	}

	@Override
	public String getDxlString(final XMLDocument dxl) throws IOException {
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(dxl.getNode());
		try {
			defaultTransformer.transform(source, result);
		} catch (TransformerException e) {
			DominoUtils.handleException(e);
		}
		return result.getWriter().toString();
	}

	@Override
	public void writeBinaryFile(final File file, final byte[] fileData) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		try {
			fo.write(fileData);
		} finally {
			fo.close();
		}
	}

	@Override
	public byte[] readBinaryFile(final File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			return data;
		} finally {
			fis.close();
		}
	}

	@Override
	public void writeXspFile(final File file, final byte[] fileData) throws IOException {
		writeBinaryFile(file, fileData);

	}

	@Override
	public byte[] readXspFile(final File file) throws IOException {
		return readBinaryFile(file);
	}

	@Override
	public void writeXspConfigFile(final File file, final byte[] fileData) throws IOException {
		writeBinaryFile(file, fileData);

	}

	@Override
	public byte[] readXspConfigFile(final File file) throws IOException {
		return readBinaryFile(file);
	}

	@Override
	public void writeTextFile(final String content, final File odpFile) throws IOException {
		PrintWriter pw = new PrintWriter(odpFile, "UTF-8");
		try {
			pw.write(content);
		} finally {
			pw.close();
		}

	}

	@Override
	public String readTextFile(final File file) throws IOException {
		StringBuilder fileContents = new StringBuilder();
		Scanner scanner = new Scanner(file);

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine());
				fileContents.append('\n');
			}
		} finally {
			scanner.close();
		}
		return fileContents.toString();
	}

	@Override
	public boolean isMetadataEnabled() {
		return true;
	}

	@Override
	public boolean isRawExportEnabled() {
		return rawExportEnabled_;
	}

}
