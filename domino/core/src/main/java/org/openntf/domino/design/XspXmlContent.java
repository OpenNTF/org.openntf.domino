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

package org.openntf.domino.design;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.xml.XMLDocument;
import org.openntf.domino.utils.xml.XMLNode;
import org.xml.sax.SAXException;

/**
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class XspXmlContent {
	// The XML File content (for faces.config, xpages/customControls)
	private XMLDocument xml_;
	private AnyFileResource container_;

	public void setContainer(final AnyFileResource container) {
		container_ = container;
	}

	protected static class ModifiableObjectNodeList<E> extends AbstractList<E> {
		@SuppressWarnings("unused")
		private static final Logger log_ = Logger.getLogger(ModifiableObjectNodeList.class.getName());

		private final Object context_;

		private final XMLNode xml_;

		private final String parentNodePath_;
		private final String nodeName_;
		private final Class<? extends E> clazz_;

		public ModifiableObjectNodeList(final Object context, final XMLNode xml, final String parentNodePath, final String nodeName,
				final Class<? extends E> clazz) {
			context_ = context;
			xml_ = xml;
			parentNodePath_ = parentNodePath;
			nodeName_ = nodeName;
			clazz_ = clazz;
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractList#get(int)
		 */
		@Override
		public E get(final int index) {
			List<XMLNode> nodes = xml_.selectNodes(parentNodePath_ + "/" + nodeName_);
			try {
				return clazz_.getConstructor(context_.getClass(), XMLNode.class).newInstance(context_, nodes.get(index));
			} catch (IllegalArgumentException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (SecurityException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (InstantiationException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (IllegalAccessException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (InvocationTargetException e) {
				DominoUtils.handleException(e);
				return null;
			} catch (NoSuchMethodException e) {
				DominoUtils.handleException(e);
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return xml_.selectNodes(parentNodePath_ + "/" + nodeName_).size();
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractList#remove(int)
		 */
		@Override
		public E remove(final int index) {
			E current = get(index);
			xml_.selectNodes(parentNodePath_ + "/" + nodeName_).remove(index);
			return current;
		}
	}

	protected final XMLDocument getXml() {
		if (xml_ == null) {
			xml_ = new XMLDocument();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				container_.getFileData(bos);
				String fileData = new String(bos.toByteArray());
				if (fileData.startsWith("<?xml")) {
					try {
						xml_.loadString(fileData);
					} catch (SAXException e) {
						DominoUtils.handleException(e);
					} catch (IOException e) {
						DominoUtils.handleException(e);
					} catch (ParserConfigurationException e) {
						DominoUtils.handleException(e);
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return xml_;
	}

	public boolean save(final DxlConverter converter) throws IOException {
		if (xml_ != null) {
			container_.setFileData(xml_.getXml().getBytes("UTF-8"));
			xml_ = null; // CHECKME: good idea to set to null?
		}
		return container_.save(converter);
	}
}
