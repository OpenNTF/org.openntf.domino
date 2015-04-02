package org.openntf.domino.design.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * A LotusScript Library.
 * 
 * @author Alexander Wagner, FOCONIS AG
 * 
 */
public final class ScriptLibraryLS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryLS, HasMetadata {
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
	public void exportDesign(final DxlConverter converter, final File odpFile) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		StringBuilder sb = new StringBuilder();
		for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) {
			sb.append(rawitemdata.getText());
		}
		converter.writeTextFile(sb.toString(), odpFile);
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
	public void importDesign(final DxlConverter converter, final File file) throws IOException {

		List<XMLNode> fileDataNodes = getDxl().selectNodes("//item[@name='$ScriptLib']");
		for (int i = fileDataNodes.size() - 1; i >= 0; i--) {
			fileDataNodes.get(i).getParentNode().removeChild(fileDataNodes.get(i));
		}

		String content = converter.readTextFile(file);
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
