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
package org.openntf.domino.xsp.msg;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

import com.ibm.xsp.binding.ValueBindingEx;
import com.ibm.xsp.exception.EvaluationExceptionEx;
import com.ibm.xsp.util.ValueBindingUtil;

public class MsgValueBinding extends ValueBindingEx {

	private String msgPar;

	public MsgValueBinding(final String str) {
		msgPar = (str == null) ? null : str.intern();
	}

	@Override
	public Class<?> getType(final FacesContext fc) throws EvaluationException, PropertyNotFoundException {
		return String.class;
	}

	@Override
	public boolean isReadOnly(final FacesContext fc) throws EvaluationException, PropertyNotFoundException {
		return true;
	}

	@Override
	public Object getValue(final FacesContext fc) throws EvaluationException, PropertyNotFoundException {
		return MsgUtilXsp.getMsg(fc, getComponent(), msgPar);
	}

	@Override
	public void setValue(final FacesContext fc, final Object o) throws EvaluationException, PropertyNotFoundException {
		throw new EvaluationExceptionEx("MsgValueBinding is read-only", this);
	}

	// --- some methods we have to overwrite
	@Override
	public String getExpressionString() {
		return ValueBindingUtil.getExpressionString(MsgBindingFactory.MSG, msgPar, ValueBindingUtil.RUNTIME_EXPRESSION);
	}

	/*
	 * Needed for restoreState
	 */
	public MsgValueBinding() {
		msgPar = null;
	}

	@Override
	public Object saveState(final FacesContext fc) {
		Object[] arr = new Object[2];
		arr[0] = super.saveState(fc);
		arr[1] = msgPar;
		return arr;
	}

	@Override
	public void restoreState(final FacesContext fc, final Object obj) {
		Object[] arr = (Object[]) obj;
		super.restoreState(fc, arr[0]);
		msgPar = ((String) arr[1]);
	}

}
