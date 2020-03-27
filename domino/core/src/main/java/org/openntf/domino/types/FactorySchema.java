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
package org.openntf.domino.types;

/**
 * Type used for generic, to ensure that the correct parameters are applied
 * 
 * @author Roland Praml, Foconis AG
 * 
 * @param <T>
 *            the org.openntf.domino type
 * @param <D>
 *            the lotus.domino type
 * @param <P>
 *            the parent type (Base when object can have different parents)
 */
@SuppressWarnings("rawtypes")
public abstract class FactorySchema<T extends org.openntf.domino.Base, D extends lotus.domino.Base, P extends org.openntf.domino.Base> {

	/** don't know it this is ever been used. */
	public abstract Class<T> typeClass();

	public abstract Class<D> delegateClass();

	public abstract Class<P> parentClass();
}
