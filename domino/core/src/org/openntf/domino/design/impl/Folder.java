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

import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
public class Folder extends AbstractFolder implements org.openntf.domino.design.Folder {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(Folder.class.getName());

	protected Folder(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	protected Folder(final Database database) {
		super(database);

		//		try {
		//			InputStream is = DesignView.class.getResourceAsStream("/org/openntf/domino/design/impl/dxl_folder.xml");
		//			loadDxl(is);
		//			is.close();
		//		} catch (IOException e) {
		//			DominoUtils.handleException(e);
		//		}
	}

	@Override
	public void setName(String title) {
		int ind = title.lastIndexOf(".folder");
		if (ind >= 0) {
			title = title.substring(0, ind);
		}
		super.setName(title);
	}
}
