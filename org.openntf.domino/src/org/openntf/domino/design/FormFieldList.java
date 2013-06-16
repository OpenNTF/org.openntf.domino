/**
 * 
 */
package org.openntf.domino.design;

import java.util.List;

import org.openntf.domino.design.impl.FormField;

/**
 * @author jgallagher
 * 
 */
public interface FormFieldList extends List<FormField> {
	public FormField addField();

	public void swap(final int a, final int b);
}
