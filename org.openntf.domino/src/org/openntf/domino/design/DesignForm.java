/**
 * 
 */
package org.openntf.domino.design;

import org.openntf.domino.design.impl.FormField;

/**
 * @author jgallagher
 * 
 */
public interface DesignForm extends DesignBaseNamed {
	public FormField addField();

	public FormFieldList getFields();
}
