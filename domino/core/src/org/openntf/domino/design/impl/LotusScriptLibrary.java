package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

// TODO MetaData
public class LotusScriptLibrary extends AbstractDesignFileResource implements org.openntf.domino.design.LotusScriptLibrary, HasMetadata {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected LotusScriptLibrary(final Document document) {
		super(document);
	}

	protected LotusScriptLibrary(final Database database) {
		super(database);
		throw new UnsupportedOperationException("There is still something todo!");
		//		try {
		//			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_lotusscriptlibrary.xml");
		//			loadDxl(is);
		//			is.close();
		//
		//			// Set some defaults
		//			Session session = getAncestorSession();
		//			String dataDirectory = session.getEnvironmentString("Directory", true);
		//			XMLDocument dxl = getDxl();
		//			dxl.selectSingleNode("/scriptlibrary/code/javaproject").setAttribute("codepath", dataDirectory);
		//
		//		} catch (IOException e) {
		//			DominoUtils.handleException(e);
		//		}
	}

	@Override
	public String getOnDiskFolder() {
		return "Code/ScriptLibraries";
	}

	@Override
	public String getOnDiskExtension() {
		return ".lss";
	}

	@Override
	public void writeOnDiskFile(final File odsFile) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		PrintWriter pw = new PrintWriter(odsFile);
		for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) {
			pw.write(rawitemdata.getText());
		}
		pw.close();
	}

}
