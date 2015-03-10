package org.openntf.domino.design;

import java.io.File;
import java.io.IOException;

import org.openntf.domino.utils.xml.XMLDocument;

public interface OnDiskConverter {

	boolean isMetadataEnabled();

	void writeDesignXML(XMLDocument dxl, File file) throws IOException;

	XMLDocument readDesignXML(File file) throws IOException;

	void writeMetaXML(XMLDocument dxl, File metaFile) throws IOException;

	XMLDocument readMetaXML(File file) throws IOException;

	String getDxlString(XMLDocument dxl) throws IOException;

	void writeBinaryFile(File file, byte[] fileData) throws IOException;

	byte[] readBinaryFile(File file) throws IOException;

	void writeXspFile(File file, byte[] fileData) throws IOException;

	byte[] readXspFile(File file) throws IOException;

	void writeXspConfigFile(File file, byte[] fileData) throws IOException;

	byte[] readXspConfigFile(File file) throws IOException;

	void writeTextFile(String content, File odpFile) throws IOException;

	String readTextFile(File file) throws IOException;

	boolean isRawExportEnabled();
}
