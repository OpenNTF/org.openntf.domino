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
/**
 * 
 */
package org.openntf.domino.utils.xml;

import java.util.AbstractList;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;

/**
 * @author jgallagher
 * 
 */
public class ModifiableStringNodeList extends AbstractList<String> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ModifiableStringNodeList.class.getName());

	private final XMLNode xml_;
	private final String parentNodePath_;
	private final String nodeName_;
	private Callable<?> postAdd_;

	public ModifiableStringNodeList(final XMLNode xml, final String parentNodePath, final String nodeName) {
		xml_ = xml;
		parentNodePath_ = parentNodePath;
		nodeName_ = nodeName;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public String get(final int index) {
		return xml_.selectNodes(parentNodePath_ + "/" + nodeName_).get(index).getText();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return xml_.selectNodes(parentNodePath_ + "/" + nodeName_).size();
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(java.lang.Object)
	 */
	@Override
	public boolean add(final String e) {
		XMLNode parent = xml_.selectSingleNode(parentNodePath_);

		if (parent == null) {
			String[] parts = parentNodePath_.split("/");
			parent = xml_;
			for (String part : parts) {
				if (!part.isEmpty()) {
					XMLNode childNode = parent.selectSingleNode(part);
					if (childNode == null) {
						childNode = parent.addChildElement(part);
					}
					parent = childNode;
				}
			}
		}

		XMLNode node = parent.addChildElement(nodeName_);
		node.setText(e);

		if (postAdd_ != null) {
			try {
				postAdd_.call();
			} catch (Exception e1) {
				DominoUtils.handleException(e1);
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public String set(final int index, final String element) {
		String current = get(index);
		xml_.selectNodes(parentNodePath_ + "/" + nodeName_).get(index).setText(element);
		return current;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public String remove(final int index) {
		String current = get(index);
		xml_.selectNodes(parentNodePath_ + "/" + nodeName_).remove(index);
		return current;
	}

	public void setPostAdd(final Callable<?> proc) {
		postAdd_ = proc;
	}
}
