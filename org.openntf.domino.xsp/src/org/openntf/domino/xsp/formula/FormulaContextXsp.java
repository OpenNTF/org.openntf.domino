package org.openntf.domino.xsp.formula;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.formula.FormulaContextNotes;
import org.openntf.domino.utils.Factory;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.ValueHolder;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.binding.ComponentBindingObject;
import com.ibm.xsp.binding.ValueBindingEx;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class FormulaContextXsp extends FormulaContextNotes {
	private static final Logger log_ = Logger.getLogger(FormulaContextNotes.class.getName());
	private UIComponent component;
	private FacesContext context;
	private Map<String, ValueBinding> valueBindings = new HashMap<String, ValueBinding>();

	@Override
	public Database getDatabase() {
		if (dataMap instanceof DominoDocument) {
			getDocument().getAncestorDatabase();
		}
		return super.getDatabase();
	}

	@Override
	public Document getDocument() {
		if (dataMap instanceof DominoDocument) {
			return Factory.fromLotus(getXspDocument().getDocument(), Document.SCHEMA, null);
		}
		return super.getDocument();
	}

	public DominoDocument getXspDocument() {
		if (dataMap instanceof DominoDocument) {
			return (DominoDocument) dataMap;
		}
		return null;
	}

	public void init(final UIComponent component, final FacesContext ctx) {
		this.component = component;
		this.context = ctx;

	}

	public UIComponent getComponent() {
		return component;
	}

	@Override
	public ValueHolder getField(final String key) {
		if (key.indexOf('.') < 0) {
			return super.getField(key);
		} else {
			Object var = getValueBinding(key).getValue(context);
			return ValueHolder.valueOf(var);
		}
	}

	@Override
	public void setField(final String key, final ValueHolder elem) {
		// TODO Auto-generated method stub
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
