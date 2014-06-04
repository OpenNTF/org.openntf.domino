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
	public void setValue(final FacesContext fc, final Object o) throws EvaluationException, PropertyNotFoundException {
		throw new EvaluationExceptionEx("MsgValueBinding is read-only", this);
	}

	// --- some methods we have to overwrite
	@Override
	public String getExpressionString() {
		return ValueBindingUtil.getExpressionString(MsgBindingFactory.MSG, msgPar, ValueBindingUtil.RUNTIME_EXPRESSION);
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

	/*
	 * Needed for restoreState
	 */
	public MsgValueBinding() {
		msgPar = null;
	}

	@Override
	public Object getValue(final FacesContext fc) throws EvaluationException, PropertyNotFoundException {
		return new MsgProviderXsp(fc, getComponent(), msgPar).getMsg();
	}
}
