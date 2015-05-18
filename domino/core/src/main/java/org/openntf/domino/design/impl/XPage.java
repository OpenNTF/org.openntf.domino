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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.openntf.domino.design.DxlConverter;

/**
 * @author jgallagher
 * 
 */
public class XPage extends AbstractXspResource implements org.openntf.domino.design.XPage, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(XPage.class.getName());

	@Override
	public void exportDesign(final DxlConverter converter, final OutputStream os) throws IOException {
		getFileData(converter.writeXspFile(os));
	}

	@Override
	public void importDesign(final DxlConverter converter, final InputStream is) throws IOException {
		setFileData(toBytes(converter.readXspFile(is)));
	}

	@Override
	protected String getDefaultFlags() {
		return "gC~4K";
	}

	@Override
	protected String getDefaultFlagsExt() {
		return "";
	}

}
