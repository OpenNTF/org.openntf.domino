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

import org.openntf.domino.Document;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.NoteCollection;

/**
 * The ACL Note of a database. Note: You cannot export the ACL-Note itself. It returns in an empty XML. So we have to export it as
 * NoteCollection. In order to be compatible to the Designer Ondisk project, we export also the design note
 * 
 * @author jgallagher
 * 
 */
public final class ACLNote extends AbstractDesignBase implements org.openntf.domino.design.ACLNote {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ACLNote.class.getName());

	/**
	 * @param document
	 */
	protected ACLNote(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	/**
	 * Special case for ACL
	 */
	@Override
	protected String doExport(final DxlExporter exporter) {
		// The About-note contains two documents
		NoteCollection nnc = getAncestorDatabase().createNoteCollection(false);
		nnc.setSelectAcl(true);
		nnc.setSelectIcon(true);
		nnc.buildCollection();
		return exporter.exportDxl(nnc);
	}
}
