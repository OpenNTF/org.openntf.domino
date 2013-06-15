/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

/**
 * @author jgallagher
 * 
 */
public interface DesignForm extends DesignBaseNamed {
	public List<FormField> getFields();

	public void removeField(int index);

	public void swapFields(int a, int b);

	public FormField addField();
}
