/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.design.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.xml.XMLNode;

// TODO MetaData
public final class ScriptLibraryLS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryLS,
		HasMetadata {
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

	protected ScriptLibraryLS(final Database database) {
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
	public void writeOnDiskFile(final Path odpFile) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		try(Writer pw = Files.newBufferedWriter(odpFile)) {
			for (XMLNode rawitemdata : getDxl().selectNodes("//item[@name='$ScriptLib']/text")) { //$NON-NLS-1$
				pw.write(rawitemdata.getText());
			}
		}
		Files.setLastModifiedTime(odpFile, FileTime.from(Instant.ofEpochMilli(getDocLastModified().getTime())));
	}

}
