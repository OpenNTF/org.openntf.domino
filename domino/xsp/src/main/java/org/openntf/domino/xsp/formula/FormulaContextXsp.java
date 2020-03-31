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
package org.openntf.domino.xsp.formula;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.openntf.domino.Document;
import org.openntf.domino.formula.FormulaContextNotes;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.model.DominoDocumentMapAdapter;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.ValueHolder;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.binding.ComponentBindingObject;
import com.ibm.xsp.binding.ValueBindingEx;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * This is the formula context in Xsp-environment. It adds additional features to the {@link FormulaContextNotes} like accessing the current
 * component or FacesContext in the {@literal @}Formulas.
 * 
 * DataMap may be a {@link org.openntf.domino.Document} or a {@link DominoDocument} so that you can directly interact on the
 * "currentDocument"
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class FormulaContextXsp extends FormulaContextNotes {
	private UIComponent component;
	private FacesContext context;
	private Map<String, ValueBinding> valueBindings = new HashMap<String, ValueBinding>();

	/**
	 * Initialize the context with xpage parameters
	 * 
	 * @param component
	 *            the current component
	 * @param ctx
	 *            the current FacesContext
	 */
	public void init(final UIComponent component, final FacesContext ctx) {
		this.component = component;
		this.context = ctx;
	}

	/**
	 * returns the current document. If dataMap is a {@link DominoDocument}, the containing document is returned. Otherwise
	 * {@link FormulaContextNotes#getDocument()} is returned.
	 * 
	 * @return the current {@link Document}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Document getDocument() {
		DominoDocument dominoDoc = getXspDocument();
		if (dominoDoc != null) {
			return Factory.fromLotus(dominoDoc.getDocument(), Document.SCHEMA, null);
		} else {
			return super.getDocument();
		}
	}

	/**
	 * returns the current Xpage-Document or null if we don't have one
	 * 
	 * @return the current {@link DominoDocument}
	 */
	public DominoDocument getXspDocument() {
		if (dataMap instanceof DominoDocument) {
			return (DominoDocument) dataMap;
		} else if (dataMap instanceof DominoDocumentMapAdapter) {
			return ((DominoDocumentMapAdapter) dataMap).getDelegate();
		}
		return null;
	}

	/**
	 * Returns the UI-Component or null if we don't have one
	 * 
	 * @return a {@link UIComponent}
	 */
	public UIComponent getComponent() {
		return component;
	}

	/**
	 * Returns a field
	 * 
	 * @param key
	 *            if it is a field identifier (like <code>Subject</code>) then dataMap.get(key) is returned.<br/>
	 *            if it contains a "." and we have a valid FacesContext then it is treated as XPage-value. So you can access XPage-Values
	 *            inside formula language. e.g. {@literal @}text(myBean.myValue)
	 * @return the value wrapped in a {@link ValueHolder}
	 */
	@Override
	public ValueHolder getField(final String key) {
		if (key.indexOf('.') < 0 || context == null) {
			return super.getField(key);
		} else {
			Object var = getValueBinding(key).getValue(context);
			return ValueHolder.valueOf(var);
		}
	}

	/**
	 * Sets a field
	 * 
	 * @param key
	 *            You can read/write dataMap and/or XPage-values. See {@link #getField(String)}
	 */
	@Override
	public void setField(final String key, final ValueHolder elem) {
		if (key.indexOf('.') < 0) {
			super.setField(key, elem);
		} else {
			try {
				Object var = elem.toList();
				getValueBinding(key).setValue(context, var);
			} catch (EvaluateException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create a value binding for {@link #getField(String)} and {@link #setField(String, ValueHolder)}
	 * 
	 * @param variable
	 *            the variableName, like <code>"document2.Form"</code>
	 * @return a {@link ValueBinding}
	 */
	protected ValueBinding getValueBinding(final String variable) {
		ValueBinding ret = valueBindings.get(variable);
		if (ret == null) {
			ApplicationEx app = (ApplicationEx) context.getApplication();
			ret = app.createValueBinding("#{" + variable + "}");
			if ((ret instanceof ValueBindingEx)) {
				ValueBindingEx valueEx = (ValueBindingEx) ret;
				valueEx.setComponent(component);
				valueEx.setSourceReferenceId(null); // TODO RPr: What to set here
				valueEx.setExpectedType(Object.class);
			} else if ((ret instanceof ComponentBindingObject)) {
				((ComponentBindingObject) ret).setComponent(component);
			}
			valueBindings.put(variable, ret);
		}
		return ret;
	}
}
