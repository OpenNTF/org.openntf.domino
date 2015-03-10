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
import java.util.logging.Logger;

import org.openntf.domino.design.OnDiskConverter;

/**
 * @author jgallagher
 * 
 */
public class XPage extends AbstractXspResource implements org.openntf.domino.design.XPage, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(XPage.class.getName());

	@Override
	protected boolean enforceRawFormat() {
		// XPage is exported in RAW-format. There is no DXL representation
		return true;
	}

	@Override
	public void writeOnDiskFile(final File file, final OnDiskConverter odsConverter) throws IOException {
		odsConverter.writeXspFile(file, getFileData());
	}

	@Override
	public void readOnDiskFile(final File file, final OnDiskConverter odsConverter) throws IOException {
		setFileData(odsConverter.readXspFile(file));
	}
}
