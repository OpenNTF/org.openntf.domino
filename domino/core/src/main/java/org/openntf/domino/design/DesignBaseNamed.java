/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface DesignBaseNamed extends DesignBase {
	/**
	 * @return the design element's name
	 */
	public String getName();

	/**
	 * @param name
	 *            The new name for the design element; any |-delimited values will be appended to the aliases
	 */
	public void setName(final String name);

	/**
	 * @return a List of the design element's aliases
	 */
	public List<String> getAliases();

	/**
	 * @return the design element's aliases, |-delimited
	 */
	public String getAlias();

	/**
	 * 
	 * @param alias
	 *            The new alias(es) for the design element, |-delimited
	 */
	public void setAlias(final String alias);

	/**
	 * 
	 * @param aliases
	 *            The new aliases for the design element; any |-delimited values will be exploded into the final list
	 */
	public void setAliases(final Iterable<String> aliases);

	/**
	 * @return The design-element-specific template name.
	 */
	public String getDesignTemplateName();

	/**
	 * 
	 * @param designTemplateName
	 *            The new design template to assign to this specific design element.
	 */
	public void setDesignTemplateName(String designTemplateName);
}
