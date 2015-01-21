/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.openntf.domino.design.impl;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class StyleSheet extends FileResource implements org.openntf.domino.design.StyleSheet, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(StyleSheet.class.getName());

	public StyleSheet(final Document document) {
		super(document);
	}

	public StyleSheet(final Database database) {
		super(database, "/org/openntf/domino/design/impl/dxl_stylesheet.xml");
	}

	@Override
	public String getContent() {
		try {
			return new String(getFileData(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setContent(final String content) {
		try {
			if (content == null) {
				setFileData("".getBytes("UTF-8"));
			} else {
				setFileData(content.getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getOnDiskFolder() {
		return "Resources/StyleSheets";
	}

	@Override
	public String getOnDiskExtension() {
		return ".css";
	}

}