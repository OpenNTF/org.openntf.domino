package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

// TODO MetaData
public final class ScriptLibraryLS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryLS, HasMetadata {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected ScriptLibraryLS(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		//return false;

		// it is complex to transform a script library from the "<code event='...'>" tags to a .lss file
		// The LSS file contains "header" for each code event like this:
		// '++LotusScript Development Environment:2:5:(Options):0:74
		// '++LotusScript Development Environment:2:5:(Declarations):0:10
		// '++LotusScript Development Environment:2:5:(Forward):0:1

		return true; // so that's why we force RAW format
	}

	public ScriptLibraryLS(final Database database) {
		super(database);
		//throw new UnsupportedOperationException("There is still something todo!");
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
	public void writeOnDiskFile(final File odpFile) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		PrintWriter pw = new PrintWriter(odpFile);
		for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) {
			pw.write(rawitemdata.getText());
		}
		pw.close();
		updateLastModified(odpFile);
	}

	protected void createScriptLibItem(final String text) {
		XMLNode documentNode = getDxl().selectSingleNode("//note");
		XMLNode fileDataNode = documentNode.addChildElement("item");
		fileDataNode.setAttribute("name", "$ScriptLib");
		fileDataNode = fileDataNode.addChildElement("text");
		fileDataNode.setAttribute("sign", "true");
		fileDataNode.setAttribute("summary", "false");
		fileDataNode.setText(text);
	}

	@Override
	public final void readOnDiskFile(final File file) {

		try {
			List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='$ScriptLib']");
			for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
				fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
			}

			StringBuilder fileContents = null;
			Scanner scanner = new Scanner(file);

			try {
				while (scanner.hasNextLine()) {
					if (fileContents == null) {
						fileContents = new StringBuilder(65000);
					}
					fileContents.append(scanner.nextLine());
					fileContents.append('\n');

					if (fileContents.length() > 60000) {
						createScriptLibItem(fileContents.toString());
						fileContents = null;
					}
				}
				if (fileContents != null) {
					createScriptLibItem(fileContents.toString());
				}
			} finally {
				scanner.close();
			}

		} catch (IOException e) {
			DominoUtils.handleException(e);
		}

	}
}
