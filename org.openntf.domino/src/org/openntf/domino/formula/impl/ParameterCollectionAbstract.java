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

import java.util.AbstractCollection;
import java.util.Iterator;

import org.openntf.domino.formula.ValueHolder;

public abstract class ParameterCollectionAbstract<T> extends AbstractCollection<T> {

	protected ValueHolder[] params;
	protected boolean permutative;
	protected int size;

	public ParameterCollectionAbstract(final ValueHolder[] params, final boolean permutative) {
		this.params = params;
		this.permutative = permutative;
		if (params == null) {
			this.size = 1; // we have at least ONE element
			return;
		}
		this.size = params[0].size();
		if (permutative) {
			for (int i = 1; i < params.length; i++) {
				this.size *= params[i].size();
			}
		} else {
			for (int i = 1; i < params.length; i++) {
				this.size = Math.max(this.size, params[i].size());
			}
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public abstract Iterator<T> iterator();

	protected abstract class ParameterIteratorAbstract implements Iterator<T> {
		// Due performance reasons, the "ret" value is reused!
		// this does not matter
		int idx = 0;

		public boolean hasNext() {
			return idx < size;
		}

		public T next() {
			T ret;
			ret = getNext();
			idx++;
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		protected int getIndex(final int pos) {
			if (permutative) {
				int currIdx = idx;
				int mod = params[pos].size();
				for (int i = pos + 1; i < params.length; i++) {
					currIdx /= params[i].size();
				}
				return currIdx % mod;
			} else {
				return idx;
			}
		}

		protected abstract T getNext();
	}
}
