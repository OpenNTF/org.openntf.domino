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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.openntf.domino.Document;
import org.openntf.domino.design.DxlConverter;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.commons.util.io.ByteStreamCache;

/**
 * @author jgallagher
 * 
 */
public final class ImageResource extends AbstractDesignNapiFileResource implements org.openntf.domino.design.ImageResource, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	@Override
	public BufferedImage getImage() {
		try {
			ByteStreamCache bsc = new ByteStreamCache();
			getFileData(bsc.getOutputStream());
			return ImageIO.read(bsc.getInputStream());
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	protected String getDefaultFlags() {
		return "D34CiQ";
	}

	@Override
	protected String getDefaultFlagsExt() {
		return "D";
	}

	@Override
	protected void saveData(final DxlConverter converter, final Document doc) throws IOException {
		if (fileData != null) {
			nSaveImageData(doc, fileData);
		}
	}

}
