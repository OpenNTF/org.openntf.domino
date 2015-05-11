package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A LotusScript Library.
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public final class ScriptLibraryLS extends AbstractDesignDxlFileResource implements org.openntf.domino.design.ScriptLibraryLS, HasMetadata {
	private static final long serialVersionUID = 1L;

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

	@Override
	public void exportDesign(final DxlConverter converter, final OutputStream os) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		PrintWriter pw = new PrintWriter(os);
		try {
			for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) {
				pw.write(rawitemdata.getText());
			}
		} finally {
			pw.close();
		}
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
	public void importDesign(final DxlConverter converter, final InputStream is) throws IOException {
		importDesign(converter, new Scanner(is));
	}

	public void importDesign(final DxlConverter converter, final Scanner scanner) throws IOException {

		List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='$ScriptLib']");
		for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
			fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
		}
		StringBuilder fileContents = new StringBuilder();

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine());
				fileContents.append('\n');
			}
		} finally {
			scanner.close();
		}

		String content = fileContents.toString();
		while (content.length() > 30000) {
			createScriptLibItem(content.substring(0, 30000));
			content = content.substring(30000);
		}
		if (content.length() > 0) {
			createScriptLibItem(content);
		}

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
