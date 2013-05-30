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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface PropertyBroker.
 */
public interface PropertyBroker extends Base<lotus.domino.PropertyBroker>, lotus.domino.PropertyBroker,
		org.openntf.domino.ext.PropertyBroker, SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#clearProperty(java.lang.String)
	 */
	@Override
	public void clearProperty(String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getInputPropertyContext()
	 */
	@Override
	public Vector<NotesProperty> getInputPropertyContext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getProperty(java.lang.String)
	 */
	@Override
	public NotesProperty getProperty(String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getPropertyValue(java.lang.String)
	 */
	@Override
	public Vector<Object> getPropertyValue(String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getPropertyValueString(java.lang.String)
	 */
	@Override
	public String getPropertyValueString(String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#hasProperty(java.lang.String)
	 */
	@Override
	public boolean hasProperty(String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#setPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public NotesProperty setPropertyValue(String propertyName, Object propertyValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#publish()
	 */
	@Override
	public void publish();
}
