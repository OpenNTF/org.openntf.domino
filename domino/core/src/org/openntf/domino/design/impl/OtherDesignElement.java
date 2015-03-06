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

import java.io.File;
import java.io.IOException;

import org.openntf.domino.Document;

/**
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class OtherDesignElement extends AbstractDesignBase {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected OtherDesignElement(final Document document) {
		init(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return true;
	}

	@Override
	public boolean save() {
		//String unid = getDxl().selectSingleNode("//noteinfo").getAttribute("unid");
		//
		//		Document doc = getAncestorDatabase().getDocumentByUNID(unid);
		//		if (doc == null) {
		//			doc = getAncestorDatabase().createDocument();
		//			doc.setUniversalID(unid);
		//			doc.save();
		//		}
		System.out.println("Cannot import " + this.getClass().getName() + ":" + getName());
		return false;
		//		return super.save();
	}

	@Override
	public boolean writeOnDiskFile(final File file, final boolean useTransformer) throws IOException {
		System.out.println(this.getClass().getName() + ":" + getName() + " will not be exported.");
		return false;
	}

	@Override
	public boolean readOnDiskFile(final File file) throws IOException {
		System.out.println(this.getClass().getName() + ":" + file.getName() + " will not be imported.");
		return false;
	}
}
