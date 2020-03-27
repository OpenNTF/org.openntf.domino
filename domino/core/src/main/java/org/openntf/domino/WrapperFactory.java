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
/**
 * 
 */
package org.openntf.domino;

import java.util.Collection;
import java.util.Vector;

import org.openntf.domino.types.FactorySchema;

/**
 * @author Roland Praml, Foconis AG
 * 
 */
public interface WrapperFactory extends Base<lotus.domino.Base> {

	/**
	 * Wraps the lotus object in the apropriate wrapper object
	 * 
	 * @param lotus
	 *            the object to wrap
	 * @param schema
	 *            the schema that ensures type safeness
	 * @param parent
	 *            the parent element
	 * @param <T>
	 *            the generic org.openntf.domino type (wrapper)
	 * @param <D>
	 *            the generic lotus.domino type (delegate)
	 * @param <P>
	 *            the generic org.openntf.domino type (parent)
	 * @return the wrapper
	 */
	@SuppressWarnings("rawtypes")
	<T extends Base, D extends lotus.domino.Base, P extends Base> T fromLotus(D lotus, FactorySchema<T, D, P> schema, P parent);

	/**
	 * Wraps a collection of lotus objects in a collection of appropriate wrapper objects
	 * 
	 * @param lotusColl
	 *            the object-collection to wrap
	 * @param schema
	 *            the schema that ensures type safeness
	 * @param parent
	 *            the parent element
	 * @param <T>
	 *            the generic org.openntf.domino type (wrapper)
	 * @param <D>
	 *            the generic lotus.domino type (delegate)
	 * @param <P>
	 *            the generic org.openntf.domino type (parent)
	 * 
	 * @return the wrapper-collection
	 */
	@SuppressWarnings("rawtypes")
	<T extends Base, D extends lotus.domino.Base, P extends Base> Collection<T> fromLotus(Collection<?> lotusColl,
			FactorySchema<T, D, P> schema, P parent);

	/**
	 * Wraps a collection of lotus objects in a Vector of apropriate wrapper objects
	 * 
	 * @param lotusColl
	 *            the object-collection to wrap
	 * @param schema
	 *            the schema that ensures type safeness
	 * @param parent
	 *            the parent element
	 * @param <T>
	 *            the generic org.openntf.domino type (wrapper)
	 * @param <D>
	 *            the generic lotus.domino type (delegate)
	 * @param <P>
	 *            the generic org.openntf.domino type (parent)
	 * 
	 * @return a vector with wrapper-objects
	 */
	@SuppressWarnings("rawtypes")
	<T extends Base, D extends lotus.domino.Base, P extends Base> Vector<T> fromLotusAsVector(final Collection<?> lotusColl,
			FactorySchema<T, D, P> schema, P parent);

	/**
	 * Wraps a collection of mixed objects in a Vector of apropriate objects. It ensures that Name/DateTime/DateRange objects are
	 * encapsulated correctly.
	 * 
	 * @param values
	 *            the objects to convert
	 * @param schema
	 *            the schema that ensures type safeness
	 * @param parent
	 *            the parent element
	 * @return a vector with wrapper-objects
	 */
	Vector<Object> wrapColumnValues(final Collection<?> values, final org.openntf.domino.Session session);

	/**
	 * Method to unwrap a object
	 * 
	 * @param the
	 *            object to unwrap
	 * @return the unwrapped object
	 */
	<T extends lotus.domino.Base> T toLotus(T base);

	/**
	 * shuts down the factory
	 */
	@Override
	void recycle();

	void recycle(lotus.domino.Base obj);

	@SuppressWarnings("rawtypes")
	@Override
	void recycle(Vector vec);

	/**
	 * Disables autorecycle for that element. By default, AutoRecycle for Session and AgentContext is always disabled
	 * 
	 * @param result
	 */
	void setNoRecycle(final Base<?> base, boolean value);

	/**
	 * This method is called if a Notes-object was recovered
	 * 
	 * @param newLotus
	 *            the new lotus object
	 * @param wrapper
	 *            the wrapper that wraps the new lotus object now
	 * @param parent
	 *            the parent object of the wrapper
	 */
	public void recacheLotusObject(final lotus.domino.Base newLotus, final Base<?> wrapper, final Base<?> parent);

	/**
	 * Returns the last Documents (max. 10) which where wrapped in the current thread
	 * 
	 * @return null if there aren't any, array of correct size, if there are
	 */
	public String[] getLastWrappedDocsInThread();

	@SuppressWarnings("rawtypes")
	public <T extends Base, P extends Base> T create(FactorySchema<T, ?, P> schema, P parent, Object metadata);

}
