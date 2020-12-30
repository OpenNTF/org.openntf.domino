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

import org.openntf.domino.Document;
import org.openntf.domino.nsfdata.structs.ODSUtils;

/**
 * a Java - ScriptLibrary
 * 
 * @author Roland Praml
 * 
 */
public class ScriptLibraryCSJS extends AbstractDesignFileResource implements org.openntf.domino.design.ScriptLibraryCSJS {
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected ScriptLibraryCSJS(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.FileResource#getFileData(java.lang.String)
	 */
	@Override
	public byte[] getFileData() {
		if (enforceRawFormat())
			return getFileDataRaw("$JavaScriptLibrary"); //$NON-NLS-1$
		return getDxl().selectSingleNode("//code/javascript").getText().getBytes(); //$NON-NLS-1$
	}

	//	@Override
	//	protected boolean useNoteFormat() {
	//		return false;
	//	}
	//
	//	@Override
	@Override
	public void writeOnDiskFile(final Path odpFile) throws IOException {
		// TODO Check for $Scriptlib_error => throw exception if item exists
		String content;
		if (enforceRawFormat()) {
			content = ODSUtils.fromLMBCS(getFileData());
		} else {
			content = getDxl().selectSingleNode("//code/javascript").getText(); //$NON-NLS-1$
		}
		try(Writer pw = Files.newBufferedWriter(odpFile)) {
			pw.write(content);
		}
		Files.setLastModifiedTime(odpFile, FileTime.from(Instant.ofEpochMilli(getDocLastModified().getTime())));
	}
}
