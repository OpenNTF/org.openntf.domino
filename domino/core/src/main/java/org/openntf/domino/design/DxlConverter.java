/*
 * Copyright 2015 - FOCONIS AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express o 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 */
package org.openntf.domino.design;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openntf.domino.utils.xml.XMLDocument;

/**
 * DXL-Converter specifies several rules, how to write certain DXL files. It can control how the resulting XML looks like (filter certain
 * Attributes, reindent the XML)
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
	 * @param outputStream
	 *            the OutputStream
	 * @throws IOException
	 */
	void writeDesignXML(XMLDocument dxl, OutputStream outputStream) throws IOException;

	/**
	 * Reads a DesignFile
	 * 
	 * @param inputStream
	 *            the source
	 * @return the DXL-Document
	 * @throws IOException
	 */
	XMLDocument readDesignXML(InputStream inputStream) throws IOException;

	/**
	 * Writes the dxl as "meta" file (e.g. *.metadata)
	 * 
	 * @param dxl
	 *            the DXL-Document
	 * @param os
	 *            the OutputStream
	 * @throws IOException
	 */
	void writeMetaXML(XMLDocument dxl, OutputStream os) throws IOException;

	/**
	 * Reads a MetaDataFile
	 * 
	 * @param is
	 *            the source
	 * @return the DXL-Document
	 * @throws IOException
	 */
	XMLDocument readMetaXML(InputStream is) throws IOException;

	/**
	 * Returns the DXL-String required for importing the dxl back in DB
	 * 
	 * @param dxl
	 * @return the DXL-String
	 * @throws IOException
	 */
	String getDxlImportString(XMLDocument dxl) throws IOException;

	/**
	 * Writes an XPages-XML file (ending: *.xsp)
	 * 
	 * @param outputStream
	 *            the OutputStream
	 * @return OutputStream to write to
	 * @throws IOException
	 */
	OutputStream writeXspFile(OutputStream outputStream) throws IOException;

	/**
	 * Reads an XPages-XML file (may indent the result)
	 * 
	 * @param file
	 *            the XSP-File
	 * @return InputStream with fileData
	 * @throws IOException
	 */
	InputStream readXspFile(InputStream inputStream) throws IOException;

	/**
	 * Writes an XSP-Config-file (ending *.xsp-config)
	 * 
	 * @param fileData
	 *            the filedata
	 * @param file
	 *            the XSP-Config stream
	 * @throws IOException
	 */
	OutputStream writeXspConfigFile(OutputStream os) throws IOException;

	/**
	 * Reads an XSP-Config-file
	 * 
	 * @param content
	 * @param is
	 * @throws IOException
	 */
	InputStream readXspConfigFile(InputStream is) throws IOException;

	/**
	 * Is Raw export enabled (required for 100% DXL compatibility)
	 * 
	 * @return boolean
	 */
	boolean isRawExportEnabled();

	/**
	 * Is MetaData export enabled (required to preserve some less important properties)
	 * 
	 * @return boolean
	 */
	boolean isMetadataEnabled();

}
