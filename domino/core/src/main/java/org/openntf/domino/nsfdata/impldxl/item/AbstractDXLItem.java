/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.nsfdata.impldxl.item;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.nsfdata.NSFItem;
import org.openntf.domino.utils.xml.XMLNode;

@SuppressWarnings({ "serial", "nls" })
public abstract class AbstractDXLItem implements NSFItem, Serializable {
	private final String name_;
	private final Set<Flag> flags_;
	private final boolean seal_;
	private final int dupItemId_;

	protected AbstractDXLItem(final XMLNode node, final int dupItemId) {
		name_ = node.getAttribute("name");

		seal_ = "true".equals(node.getAttribute("seal"));

		// Translate the item node attributes to item flags
		flags_ = EnumSet.noneOf(Flag.class);
		if("true".equals(node.getAttribute("sealed"))) {
			flags_.add(Flag.SEAL);
		}
		if("true".equals(node.getAttribute("authors"))) {
			flags_.add(Flag.READWRITERS);
		}
		if("true".equals(node.getAttribute("names"))) {
			flags_.add(Flag.NAMES);
		}
		if("true".equals(node.getAttribute("readers"))) {
			flags_.add(Flag.READERS);
		}
		if("true".equals(node.getAttribute("protected"))) {
			flags_.add(Flag.PROTECTED);
		}
		if("true".equals(node.getAttribute("summary"))) {
			flags_.add(Flag.SUMMARY);
		}
		if("true".equals(node.getAttribute("sign"))) {
			flags_.add(Flag.SIGN);
		}

		// May as well aim to be similar to the API
		flags_.add(Flag.UNCHANGED);

		dupItemId_ = dupItemId;
	}

	@Override
	public String getName() {
		return name_;
	}

	@Override
	public Set<Flag> getFlags() {
		return Collections.unmodifiableSet(flags_);
	}

	@Override
	public int getSequence() {
		// DXL does not contain sequence information for items
		// TODO Change to throw UnsupportedOperationException()? Seems harsh, though
		return -1;
	}

	@Override
	public int getDupItemId() {
		return dupItemId_;
	}

	@Override
	public boolean isSeal() {
		return seal_;
	}

	@Override
	public abstract Type getType();

	@Override
	public abstract byte[] getBytes();

}
