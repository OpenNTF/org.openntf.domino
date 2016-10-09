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
import javax.faces.el.MethodNotFoundException;

import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.util.ValueBindingUtil;

public class MsgMethodBinding extends MethodBindingEx {

	private String msgPar;

	public MsgMethodBinding(final String str, final Class<?>[] paramClasses) {
		msgPar = (str == null) ? null : str.intern();
	}

	@Override
	public Class<?> getType(final FacesContext arg0) throws MethodNotFoundException {
		return String.class;
	}

	@Override
	public Object invoke(final FacesContext fc, final Object[] arg1) throws EvaluationException, MethodNotFoundException {
		return MsgUtilXsp.getMsg(fc, getComponent(), msgPar);
	}

	// --- some methods we have to overwrite
	@Override
	public String getExpressionString() {
		return ValueBindingUtil.getExpressionString(MsgBindingFactory.MSG, msgPar, ValueBindingUtil.RUNTIME_EXPRESSION);
	}

	/*
	 * Needed for restoreState
	 */
	public MsgMethodBinding() {
		msgPar = null;
	}

	@Override
	public Object saveState(final FacesContext ctx) {
		Object[] arr = new Object[2];
		arr[0] = super.saveState(ctx);
		arr[1] = msgPar;
		return arr;
	}

	@Override
	public void restoreState(final FacesContext ctx, final Object obj) {
		Object[] arr = (Object[]) obj;
		super.restoreState(ctx, arr[0]);
		msgPar = ((String) arr[1]);
	}

}
