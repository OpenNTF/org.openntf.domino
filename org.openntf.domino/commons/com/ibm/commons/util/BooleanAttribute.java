/*
 * © Copyright IBM Corp. 2012-2013
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


package com.ibm.commons.util;

/**
 * @ibm-not-published
 */
public class BooleanAttribute {
	private boolean b = false;

	/**
	 * Constructor for BooleanAttribute.
	 */
	public BooleanAttribute(String value, boolean bDefault) 
	{
		this.b = bDefault;
		if (value != null)
		{
			if (value.equalsIgnoreCase("true") || //$NON-NLS-1$
				value.equalsIgnoreCase("yes")) //$NON-NLS-1$
			{
				b = true;
			}
			else if(value.equalsIgnoreCase("false") || //$NON-NLS-1$
					 value.equalsIgnoreCase("no")) //$NON-NLS-1$
			{
				b = false;
			}
		}		
	}
	
	/**
	 * Method BooleanAttribute.
	 * @param value
	 */
	public BooleanAttribute(String value)
	{
		this(value, false);
	}
	
	public BooleanAttribute(boolean b)
	{
		this.b = b;
	}
	
	/**
	 * Method BooleanValue.
	 * @return boolean
	 */
	public boolean booleanValue()
	{
		return this.b;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return b ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
