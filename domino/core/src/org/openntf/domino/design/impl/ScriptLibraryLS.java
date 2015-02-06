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

/**
 * A LotusScript Library.
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
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
	public boolean writeOnDiskFile(final File odpFile, final boolean useTransformer) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		PrintWriter pw = new PrintWriter(odpFile, "UTF-8");
		for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) {
			pw.write(rawitemdata.getText());
		}
		pw.close();
		updateLastModified(odpFile);
		return true;
	}

	protected void createScriptLibItem(final String text) {
		XMLNode documentNode = getDxl().selectSingleNode("//note");
		XMLNode fileDataNode = documentNode.addChildElement("item");
		fileDataNode.setAttribute("name", "$ScriptLib");
		fileDataNode.setAttribute("sign", "true");
		fileDataNode.setAttribute("summary", "false");
		fileDataNode = fileDataNode.addChildElement("text");
		fileDataNode.setText(text);
	}

	@Override
	public boolean readOnDiskFile(final File file) {

		try {
			List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='$ScriptLib']");
			for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
				fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
			}

			StringBuilder fileContents = null;
			Scanner scanner = new Scanner(file, "UTF-8");

			try {
				while (scanner.hasNextLine()) {
					if (fileContents == null) {
						fileContents = new StringBuilder(32000);
					}
					fileContents.append(scanner.nextLine());
					fileContents.append('\n');

					if (fileContents.length() > 30000) {
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
		return true;
	}

	@Override
	public void setName(String title) {
		int ind = title.lastIndexOf(".lss");
		if (ind >= 0) {
			title = title.substring(0, ind);
		}
		super.setName(title);
	}
}
