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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openntf.domino.Document;

@SuppressWarnings("nls")
public class TemplateBuildSharedFieldNote extends SharedField implements org.openntf.domino.design.TemplateBuildSharedFieldNote {
	private static final long serialVersionUID = 1L;

	protected TemplateBuildSharedFieldNote(final Document document) {
		super(document);
	}

	@Override
	public String getTemplateBuildName() {
		return getItemValueString("$TemplateBuildName");
	}

	@Override
	public void setTemplateBuildName(final String templateBuildName) {
		Set<ItemFlag> flags = new HashSet<ItemFlag>();
		flags.add(ItemFlag._SIGN);
		setItemValue("$TemplateBuildName", templateBuildName, flags);
	}

	@Override
	public String getTemplateBuildVersion() {
		return getItemValueString("$TemplateBuild");
	}

	@Override
	public void setTemplateBuildVersion(final String templateBuildVersion) {
		Set<ItemFlag> flags = new HashSet<ItemFlag>();
		flags.add(ItemFlag._SIGN);
		setItemValue("$TemplateBuild", templateBuildVersion, flags);
	}

	@Override
	public Date getTemplateBuildDate() {
		return null;
	}

	@Override
	public void setTemplateBuildDate(final Date date) {
		// TODO Auto-generated method stub

	}

}
