package org.openntf.domino.xsp.formula;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

import org.openntf.domino.xsp.model.DominoDocumentMapAdapter;
import org.openntf.formula.ASTNode;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.Formulas;

import com.ibm.xsp.binding.ValueBindingEx;
import com.ibm.xsp.exception.EvaluationExceptionEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.ValueBindingUtil;

public class FormulaValueBinding extends ValueBindingEx {

	private String formulaStr;
	private transient SoftReference<ASTNode> astNodeCache;

	public FormulaValueBinding(final String str) {
		// TODO Auto-generated constructor stub
		this.formulaStr = (str != null ? str.intern() : null);
	}

	@Override
	public Class<?> getType(final FacesContext arg0) throws EvaluationException, PropertyNotFoundException {
		return getExpectedType();
	}

	@Override
	public Object getValue(final FacesContext ctx) throws EvaluationException, PropertyNotFoundException {
		boolean rootWasNull = false;
		if (ctx.getViewRoot() == null) {
			rootWasNull = true;
			ctx.setViewRoot(FacesUtil.getViewRoot(getComponent()));
		}

		final DominoDocument dominoDoc = (DominoDocument) ExtLibUtil.resolveVariable(ctx, "currentDocument");
		Map<String, Object> dataMap = null;
		if (dominoDoc instanceof Map) {
			dataMap = (Map<String, Object>) dominoDoc;
		} else {
			dataMap = new DominoDocumentMapAdapter(dominoDoc);
		}
		List<Object> ret = null;
		try {
			FormulaContextXsp fctx = (FormulaContextXsp) Formulas.createContext(dataMap, Formulas.getParser());
			fctx.setComponent(this.getComponent());
			ret = getASTNode().solve(fctx);
		} catch (EvaluateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormulaParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rootWasNull) {
				ctx.setViewRoot(null);
			}
		}
		Object firstValue = ret.size() >= 1 ? ret.get(0) : null;

		Class localClass = getExpectedType();
		if (localClass != null) {
			if (localClass == String.class) {
				if (ret.isEmpty()) {
					return null;
				}
				if (ret.size() == 1) {
					return ret.get(0).toString();
				}
				return ret.toString();
			}
			if ((localClass == Boolean.class) || (localClass == Boolean.TYPE)) {
				return firstValue == null ? Boolean.FALSE : (Boolean) firstValue;
			}
			if ((localClass == Character.class) || (localClass == Character.TYPE)) {
				String str = firstValue == null ? "" : firstValue.toString();
				if (str.length() > 0) {
					return new Character(str.charAt(0));
				}
			}
			if ((localClass.isPrimitive()) || (Number.class.isAssignableFrom(localClass))) {
				if ((localClass == Double.class) || (localClass == Double.TYPE)) {
					return ((Number) firstValue).doubleValue();
				}
				if ((localClass == Integer.class) || (localClass == Integer.TYPE)) {
					return ((Number) firstValue).intValue();

				}
				if ((localClass == Long.class) || (localClass == Long.TYPE)) {
					return ((Number) firstValue).longValue();
				}
				if ((localClass == Byte.class) || (localClass == Byte.TYPE)) {
					return ((Number) firstValue).byteValue();
				}
				if ((localClass == Short.class) || (localClass == Short.TYPE)) {
					return ((Number) firstValue).shortValue();
				}
				if ((localClass == Float.class) || (localClass == Float.TYPE)) {
					return ((Number) firstValue).floatValue();
				}
			}
		}

		return convertToExpectedType(ctx, ret);

		// TODO Auto-generated method stub

	}

	@Override
	public boolean isReadOnly(final FacesContext arg0) throws EvaluationException, PropertyNotFoundException {
		return true;
	}

	@Override
	public void setValue(final FacesContext arg0, final Object arg1) throws EvaluationException, PropertyNotFoundException {
		throw new EvaluationExceptionEx("FormulaValueBinding is read-only", this);
	}

	protected ASTNode getASTNode() throws FormulaParseException {
		if (astNodeCache != null) {
			ASTNode node = astNodeCache.get();
			if (node != null)
				return node;
		}

		ASTNode node = Formulas.getParser().parse(formulaStr);
		astNodeCache = new SoftReference<ASTNode>(node);
		return node;

	}

	// --- some methods we have to overwrite
	@Override
	public String getExpressionString() {
		return ValueBindingUtil.getExpressionString(FormulaBindingFactory.FORMULA, this.formulaStr, ValueBindingUtil.RUNTIME_EXPRESSION);
	}

	@Override
	public Object saveState(final FacesContext paramFacesContext) {
		Object[] arr = new Object[2];
		arr[0] = super.saveState(paramFacesContext);
		arr[1] = this.formulaStr;
		return arr;
	}

	@Override
	public void restoreState(final FacesContext paramFacesContext, final Object paramObject) {
		Object[] arr = (Object[]) paramObject;
		super.restoreState(paramFacesContext, arr[0]);
		this.formulaStr = ((String) arr[1]);
	}
}
