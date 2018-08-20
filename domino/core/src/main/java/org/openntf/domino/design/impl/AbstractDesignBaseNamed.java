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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.design.DesignBaseNamed;
import org.openntf.formula.function.TextFunctions;

import com.ibm.commons.util.StringUtil;

/**
 * A named DesignNote
 *
 * @author jgallagher
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignBaseNamed extends AbstractDesignBase implements DesignBaseNamed {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBaseNamed.class.getName());

	public AbstractDesignBaseNamed(final Database database) {
		super(database);
	}

	public AbstractDesignBaseNamed(final Database database, final InputStream is) {
		super(database, is);
	}

	public AbstractDesignBaseNamed(final Document document) {
		super(document);
	}

	protected List<String> getTitlesRaw() {
		String titles = getItemValueStrings(TITLE_ITEM, "|");
		return Arrays.asList(titles.split("\\|"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		String aliases;
		switch (getDxlFormat(false)) {
		case DXL:
			aliases = getDxl().getAttribute("alias");
			break;
		default:
			// Aliases are all the $TITLE values after the first
			aliases = getItemValueStrings(TITLE_ITEM, "|");
			aliases = TextFunctions.atRight(aliases, "|");
			break;

		}

		if (StringUtil.isEmpty(aliases)) {
			return new ArrayList<String>();
		} else {
			return Arrays.asList(aliases.split("\\|"));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#getAlias()
	 */
	@Override
	public String getAlias() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDxl().getAttribute("alias");
		default:
			String[] aliases = getAliases().toArray(new String[] {});
			return StringUtil.concatStrings(aliases, '|', false);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#getName()
	 */
	@Override
	public String getName() {
		switch (getDxlFormat(false)) {
		case DXL:
			return getDocumentElement().getAttribute("name");
		default:
			String title = getItemValueString(TITLE_ITEM);
			int pos = title.indexOf('|');
			if (pos < 0) {
				return title;
			}
			return title.substring(0, pos);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("alias", alias);
			break;
		default:
			List<String> result = new ArrayList<String>(2);
			result.add(getName());
			result.add(alias);
			setItemValue(TITLE_ITEM, result, FLAG_SIGN_SUMMARY);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(final Iterable<String> aliases) {
		switch (getDxlFormat(true)) {
		case DXL:
			StringBuilder sb = new StringBuilder();
			for (String alias : aliases) {
				if (sb.length() > 0) {
					sb.append('|');
				}
				sb.append(alias);
			}
			getDocumentElement().setAttribute("alias", sb.toString());
			break;
		default:
			List<String> titles = getItemValueStrings(TITLE_ITEM);
			List<String> result = new ArrayList<String>(2);
			result.add(titles.size() > 0 ? titles.get(0) : "");
			for (String alias : aliases) {
				result.add(alias);
			}
			setItemValue(TITLE_ITEM, titles, FLAG_SIGN_SUMMARY);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.design.DesignBaseNamed#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name) {
		switch (getDxlFormat(true)) {
		case DXL:
			getDocumentElement().setAttribute("name", name);
			break;
		default:
			List<String> result = getItemValueStrings(TITLE_ITEM);
			if (result.size() > 0) {
				result.set(0, name);
			} else {
				result.add(name);
			}
			setItemValue(TITLE_ITEM, result, FLAG_SIGN_SUMMARY);
			break;
		}
	}

}
