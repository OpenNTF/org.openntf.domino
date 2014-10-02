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

/**
 * @author jgallagher
 * 
 */
public interface DesignForm extends DesignBaseNamed {
	public FormField addField();

	public FormFieldList getFields();

	public String getXPageAlt();

	public void setXPageAlt(String xpageAlt);

	public String getXPageAltClient();

	public void setXPageAltClient(String xpageAltClient);

	public void swapFields(final int a, final int b);
}
