/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents a single property in the Composite Applications properties of a Domino application. Properties are data transmitted to or from
 * the Property Broker, which routes your application's communication with other components in a composite application. Properties are
 * defined in a Wiring Properties design element.
 * <p>
 * All methods are inactive when called by applications running on the Domino server, or running on the client without Notes standard
 * configuration. The Publish method is used to transmit the value of one of your application's output properties to the Property Broker,
 * which then passes the value on to the actions of any other components that are wired to receive that property. In addition to using this
 * method, property value changes can be automatically published by associating a property to a view or folder column.
 * </p>
 */
public interface NotesProperty
		extends Base<lotus.domino.NotesProperty>, lotus.domino.NotesProperty, org.openntf.domino.ext.NotesProperty, SessionDescendant {
	public static class Schema extends FactorySchema<NotesProperty, lotus.domino.NotesProperty, PropertyBroker> {
		@Override
		public Class<NotesProperty> typeClass() {
			return NotesProperty.class;
		}

		@Override
		public Class<lotus.domino.NotesProperty> delegateClass() {
			return lotus.domino.NotesProperty.class;
		}

		@Override
		public Class<PropertyBroker> parentClass() {
			return PropertyBroker.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Purges the new or modified values of the property from the Notes® backend where they are temporarily stored before publishing via the
	 * property broker.
	 */
	@Override
	public void clear();

	/**
	 * The description of the NotesProperty.
	 */
	@Override
	public String getDescription();

	/**
	 * The name of the NotesProperty.
	 */
	@Override
	public String getName();

	/**
	 * The namespace of the datatype associated with the NotesProperty.
	 */
	@Override
	public String getNamespace();

	/**
	 * The title of the NotesProperty.
	 */
	@Override
	public String getTitle();

	/**
	 * The type name of the NotesProperty.
	 */
	@Override
	public String getTypeName();

	/**
	 * The NotesProperty values.
	 * <p>
	 * The vector must be homogeneous. Input properties cannot be set. After setting this property, you must call publish for the changes to
	 * take effect. If you do not call publish, your changes are lost.
	 * </p>
	 *
	 */
	@Override
	public Vector<Object> getValues();

	/**
	 * The value of the current NotesProperty as a single text value.
	 */
	@Override
	public String getValueString();

	/**
	 * Indicates whether this property is an input property.
	 * <p>
	 * All input properties are read-only, and can not be cleared. Output properties can be read-write, and can be cleared.
	 * </p>
	 *
	 * @return true if an input property or false if an output property, created by the application or user activity.
	 */
	@Override
	public boolean isInput();

	/**
	 * Publishes new values for NotesProperty properties if modified.
	 */
	@Override
	public void publish();

}
