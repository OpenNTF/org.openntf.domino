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

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public final class ImageResource extends AbstractDesignFileResource implements org.openntf.domino.design.ImageResource, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	public ImageResource(final Document document) {
		super(document);
	}

	@Override
	protected boolean enforceRawFormat() {
		// RAW-format is set to false, otherwise it is difficult to determine file extension
		return false;
	}

	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new ByteArrayInputStream(getFileData()));
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public byte[] getFileData() {
		switch (getDxlFormat(true)) {
		case DXL:
			String rawData = getDxl().selectSingleNode("//jpeg|//gif|//png").getText();
			return parseBase64Binary(rawData);
		default:
			return getFileDataRaw("$ImageData");

		}
	}
}
