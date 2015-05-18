package org.openntf.domino.design.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.xml.sax.SAXException;

/**
 * The Default DXL-Transformer. Writes DXL (nearly) the same way as IBM Domino Designer On Disk Project
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public class DefaultDxlConverter implements DxlConverter {

	protected Transformer defaultTransformer = XMLDocument.createTransformer(null);
	protected Transformer metaTransformer = XMLDocument
			.createTransformer(DefaultDxlConverter.class.getResourceAsStream("metatFilter.xslt"));
	private boolean rawExportEnabled_;

	/**
	 * Create a new DxlTransformer
	 * 
	 * @param rawExportEnabled
	 *            true if Notes shout be exported in raw format.
	 */
	public DefaultDxlConverter(final boolean rawExportEnabled) {
		super();
		rawExportEnabled_ = rawExportEnabled;
	}

	/**
	 * Writes the DXL to outputFile
	 * 
	 * @param dxl
	 *            the DXL-DOM
	 * @param transformer
	 *            the transformer that should be used
	 * @param outputFile
	 *            the outputStream
	 * @throws IOException
	 */

	protected void writeXml(final XMLDocument dxl, final Transformer transformer, final OutputStream outputStream) throws IOException {
		try {
			StreamResult result = new StreamResult(outputStream);
			DOMSource source = new DOMSource(dxl.getNode());
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the dxl from the file
	 * 
	 * @param file
	 *            the file
	 * @return the DXL
	 * @throws IOException
	 */
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

	/**
	 * Reads the dxl from the file
	 * 
	 * @param file
	 *            the file
	 * @return the DXL
	 * @throws IOException
	 */
	protected XMLDocument readXML(final InputStream is) throws IOException {
		// TODO Auto-generated method stub
		try {
			XMLDocument ret = new XMLDocument();
			try {
				ret.loadInputStream(is);
				return ret;
			} catch (SAXException e) {
				DominoUtils.handleException(e);
			} catch (ParserConfigurationException e) {
				DominoUtils.handleException(e);
			}
			return null;
		} finally {
			is.close();
		}
	}

	@Override
	public void writeDesignXML(final XMLDocument dxl, final OutputStream outputStream) throws IOException {
		writeXml(dxl, defaultTransformer, outputStream);

	}

	@Override
	public XMLDocument readDesignXML(final InputStream inputStream) throws IOException {
		return readXML(inputStream);
	}

	@Override
	public void writeMetaXML(final XMLDocument dxl, final OutputStream os) throws IOException {
		writeXml(dxl, metaTransformer, os);
	}

	@Override
	public XMLDocument readMetaXML(final InputStream is) throws IOException {
		return readXML(is);
	}

	//	protected void writeBinaryFile(final byte[] fileData, final File file) throws IOException {
	//		FileOutputStream fo = new FileOutputStream(file);
	//		try {
	//			fo.write(fileData);
	//		} finally {
	//			fo.close();
	//		}
	//	}

	//	protected InputStream readBinaryFile(final File file) throws IOException {
	//		return new FileInputStream(file);
	//	}

	@Override
	public OutputStream writeXspFile(final OutputStream outputStream) throws IOException {
		return outputStream;

	}

	@Override
	public InputStream readXspFile(final InputStream inputStream) throws IOException {
		return inputStream; // Default = pass-thru
	}

	@Override
	public OutputStream writeXspConfigFile(final OutputStream os) throws IOException {
		return os;
	}

	@Override
	public InputStream readXspConfigFile(final InputStream is) throws IOException {
		return is; // passthru
	}

	@Override
	public String getDxlImportString(final XMLDocument dxl) throws IOException {
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
	public boolean isMetadataEnabled() {
		return true;
	}

	@Override
	public boolean isRawExportEnabled() {
		return rawExportEnabled_;
	}
}
