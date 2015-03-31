package org.openntf.domino.design;

import java.io.File;
import java.io.IOException;

import org.openntf.domino.utils.xml.XMLDocument;

/**
 * DXL-Converter specifies several rules, how to write certain DXL files
 * 
 * @author Roland Praml, FOCONIS AG
 *
 */
public interface DxlConverter {

	/**
	 * Writes the dxl as "Design" file (e.g. *.form, *.view)
	 * 
	 * @param dxl
	 *            the DXL-Document
	 * @param file
	 *            the OutputFile
	 * @throws IOException
	 */
	void writeDesignXML(XMLDocument dxl, File file) throws IOException;

	/**
	 * Reads a DesignFile
	 * 
	 * @param file
	 *            the source
	 * @return the DXL-Document
	 * @throws IOException
	 */
	XMLDocument readDesignXML(File file) throws IOException;

	/**
	 * Writes the dxl as "meta" file (e.g. *.metadata)
	 * 
	 * @param dxl
	 *            the DXL-Document
	 * @param file
	 *            the OutputFile
	 * @throws IOException
	 */
	void writeMetaXML(XMLDocument dxl, File metaFile) throws IOException;

	/**
	 * Reads a MetaDataFile
	 * 
	 * @param file
	 *            the source
	 * @return the DXL-Document
	 * @throws IOException
	 */
	XMLDocument readMetaXML(File file) throws IOException;

	/**
	 * Returns the DXL-String required for importing the dxl back in DB
	 * 
	 * @param dxl
	 * @return the DXL-String
	 * @throws IOException
	 */
	String getDxlImportString(XMLDocument dxl) throws IOException;

	/**
	 * Writes a binary file - TODO remove
	 * 
	 * @param file
	 * @param fileData
	 * @throws IOException
	 */
	void writeBinaryFile(File file, byte[] fileData) throws IOException; // TODO Remove this

	byte[] readBinaryFile(File file) throws IOException; // TODO Remove this

	/**
	 * Writes an XPages-XML file (ending: *.xsp)
	 * 
	 * @param file
	 *            the XSP-File
	 * @param fileData
	 *            the fileData
	 * @throws IOException
	 */
	void writeXspFile(File file, byte[] fileData) throws IOException;

	/**
	 * Reads an XPages-XML file
	 * 
	 * @param file
	 *            the XSP-File
	 * @return byte Array with fileData
	 * @throws IOException
	 */
	byte[] readXspFile(File file) throws IOException;

	/**
	 * Writes an XSP-Config-file (ending *.xsp-config)
	 * 
	 * @param file
	 *            the XSP-Config file
	 * @param fileData
	 *            the filedata
	 * @throws IOException
	 */
	void writeXspConfigFile(File file, byte[] fileData) throws IOException;

	/**
	 * Reads an XSP-Config-file
	 * 
	 * @param content
	 * @param file
	 * @throws IOException
	 */
	byte[] readXspConfigFile(File file) throws IOException;

	/**
	 * Writes a TextFile
	 * 
	 * @param content
	 *            the content
	 * @param file
	 *            the output file
	 * @throws IOException
	 */
	void writeTextFile(String content, File file) throws IOException;

	/**
	 * Reads a TextFile
	 * 
	 * @param file
	 * @return the content
	 * @throws IOException
	 */
	String readTextFile(File file) throws IOException;

	/**
	 * Is Raw export enabled (required for 100% DXL compatibility)
	 * 
	 * @return boolean
	 */
	boolean isRawExportEnabled();

	/**
	 * Is MetaData export enabled (required to preserver some less important properties)
	 * 
	 * @return boolean
	 */
	boolean isMetadataEnabled();
}
