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
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLNode;

/**
 * @author jgallagher
 * 
 */
public final class ImageResource extends AbstractDesignFileResource implements org.openntf.domino.design.ImageResource, HasMetadata {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ImageResource.class.getName());

	protected ImageResource(final Document document) {
		super(document);
	}

	protected ImageResource(final Database database) {
		super(database);
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
			String rawData = getDxl().selectSingleNode("//jpeg|//gif").getText();
			return parseBase64Binary(rawData);
		default:
			return getFileDataRaw("$ImageData");
		}
	}

	@Override
	public void setFileData(final byte[] fileData) {
		switch (getDxlFormat(true)) {
		case DXL:

			XMLNode node = getDxl().selectSingleNode("//imageresource");
			XMLNode itemNode = node.selectSingleNode("item");
			XMLNode imgNode = null;

			ImageInputStream iis;
			try {
				iis = ImageIO.createImageInputStream(new ByteArrayInputStream(fileData));

				// get all currently registered readers that recognize the image format
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

				if (!iter.hasNext()) {
					throw new RuntimeException("No readers found!");
				}

				// get the first reader
				ImageReader reader = iter.next();
				iis.close();

				String imgFormat = reader.getFormatName();
				//png images have to be in a <jpeg></jpeg> - node!
				imgFormat = "png".equals(imgFormat) ? "jpeg" : imgFormat.toLowerCase();

				//remove old nodes
				List<XMLNode> imgDataNodes = getDxl().selectNodes("//" + imgFormat);
				for (int i = imgDataNodes.size() - 1; i >= 0; i--) {
					imgDataNodes.get(i).getParentNode().removeChild(imgDataNodes.get(i));
				}

				if (itemNode != null) {
					imgNode = node.insertChildElementBefore(imgFormat, itemNode);
				} else {
					imgNode = node.addChildElement(imgFormat);
				}

				imgNode.setText(printBase64Binary(fileData));

			} catch (IOException e) {
				DominoUtils.handleException(e);
			}
			break;
		default:
			setFileDataRaw("$ImageData", fileData);
		}
	}
}
