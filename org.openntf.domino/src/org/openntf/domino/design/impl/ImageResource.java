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
public class ImageResource extends AbstractDesignBaseNamed implements org.openntf.domino.design.ImageResource {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	public ImageResource(final Document document) {
		super(document);
	}

	@Override
	public byte[] getImageData() {
		return parseBase64Binary(getDxl().selectSingleNode("//jpeg").getText());
	}

	@Override
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new ByteArrayInputStream(getImageData()));
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}
