/*
 * © Copyright FOCONIS AG, 2014
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
 * 
 */
package org.openntf.domino.formula.impl;

import java.lang.reflect.Array;
import java.util.Iterator;

import org.openntf.domino.DateTime;
import org.openntf.domino.formula.ValueHolder;

public class ParameterCollectionObject<T> extends ParameterCollectionAbstract<T[]> {
	protected Class<T> clazz;
	private T[] ret = null;

	@SuppressWarnings("unchecked")
	public ParameterCollectionObject(final ValueHolder[] params, final Class<T> clazz, final boolean permutative) {
		super(params, permutative);
		this.clazz = clazz;
		ret = (T[]) Array.newInstance(clazz, params == null ? 0 : params.length);
	}

	@Override
	public Iterator<T[]> iterator() {
		return new ParameterIterator();
	}

	@SuppressWarnings("unchecked")
	protected class ParameterIterator extends ParameterIteratorAbstract {

		@SuppressWarnings("deprecation")
		@Override
		protected T[] getNext() {
			if (params == null) {
				return null;
			}
			if (clazz.equals(String.class)) {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].getString(getIndex(i));
				}
			} else if (clazz.equals(DateTime.class)) {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].getDateTime(getIndex(i));
				}
			} else {
				for (int i = 0; i < ret.length; i++) {
					ret[i] = (T) params[i].get(getIndex(i));
				}
			}
			return ret;
		}
	}

}
