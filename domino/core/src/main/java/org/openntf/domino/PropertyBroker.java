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

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface PropertyBroker.
 */
public interface PropertyBroker extends Base<lotus.domino.PropertyBroker>, lotus.domino.PropertyBroker,
		org.openntf.domino.ext.PropertyBroker, SessionDescendant {

	public static class Schema extends FactorySchema<PropertyBroker, lotus.domino.PropertyBroker, Session> {
		@Override
		public Class<PropertyBroker> typeClass() {
			return PropertyBroker.class;
		}

		@Override
		public Class<lotus.domino.PropertyBroker> delegateClass() {
			return lotus.domino.PropertyBroker.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#clearProperty(java.lang.String)
	 */
	@Override
	public void clearProperty(final String propertyName);

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
	public NotesProperty getProperty(final String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getPropertyValue(java.lang.String)
	 */
	@Override
	public Vector<Object> getPropertyValue(final String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#getPropertyValueString(java.lang.String)
	 */
	@Override
	public String getPropertyValueString(final String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#hasProperty(java.lang.String)
	 */
	@Override
	public boolean hasProperty(final String propertyName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#setPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public NotesProperty setPropertyValue(final String propertyName, final Object propertyValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.PropertyBroker#publish()
	 */
	@Override
	public void publish();
}
