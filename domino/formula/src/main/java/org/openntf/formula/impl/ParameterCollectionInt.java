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
package org.openntf.formula.impl;

import java.util.Iterator;

import org.openntf.formula.ValueHolder;

public class ParameterCollectionInt extends ParameterCollectionAbstract<int[]> {

	public ParameterCollectionInt(final ValueHolder[] params, final boolean permutative) {
		super(params, permutative);
	}

	@Override
	public Iterator<int[]> iterator() {
		return new ParameterIteratorInt();
	}

	protected class ParameterIteratorInt extends ParameterIteratorAbstract {
		int[] ret = new int[params.length];

		@Override
		protected int[] getNext() {
			for (int i = 0; i < ret.length; i++) {
				ret[i] = params[i].getInt(getIndex(i));
			}
			return ret;
		}
	}

}
