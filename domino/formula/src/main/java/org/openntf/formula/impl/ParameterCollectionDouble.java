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

public class ParameterCollectionDouble extends ParameterCollectionAbstract<double[]> {

	public ParameterCollectionDouble(final ValueHolder[] params, final boolean permutative) {
		super(params, permutative);
	}

	@Override
	public Iterator<double[]> iterator() {
		return new ParameterIteratorDouble();
	}

	protected class ParameterIteratorDouble extends ParameterIteratorAbstract {
		double[] ret = new double[params.length];

		@Override
		protected double[] getNext() {
			for (int i = 0; i < ret.length; i++) {
				ret[i] = params[i].getDouble(getIndex(i));
			}
			return ret;
		}
	}

}
