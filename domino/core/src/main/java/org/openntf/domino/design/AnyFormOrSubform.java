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

import java.util.List;

import org.openntf.domino.utils.xml.XMLNodeList;

/**
 * @author jgallagher
 *
 */
public interface AnyFormOrSubform extends DesignBaseNamed {
	public FormField addField();

	public FormFieldList getFields();

	public void swapFields(final int a, final int b);

	/**
	 * @return list of subform nodes identified by subformref XML element
	 * @since ODA 4.1.0
	 */
	public XMLNodeList getSubformNodes();

	/**
	 * Gets names of coded subforms included the Form / Subform design. Computed subforms don't have a name attribute.
	 *
	 * @return list of name attributes from subform nodes
	 * @since ODA 4.1.0
	 */
	public List<String> getExplicitSubforms();

	/**
	 * Iterates recursively through explicit subforms to retrieve all explicit descendant subforms
	 *
	 * @param existingSubforms
	 *            list of subforms previously collected for this Form / Subform or ancestors. This ensures the same subform is not added and
	 *            inspected again.
	 * @return the modified existingSubforms list
	 * @since ODA 4.1.0
	 */
	public List<String> getExplicitSubformsRecursive(List<String> existingSubforms);

	/**
	 * Gets names of formula for any computed subforms included the Form / Subform design
	 *
	 * @return list of formulas from subform nodes
	 * @since ODA 4.1.0
	 */
	public List<String> getComputedSubforms();
}
