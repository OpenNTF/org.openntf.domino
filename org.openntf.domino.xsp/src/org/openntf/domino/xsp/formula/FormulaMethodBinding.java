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
package org.openntf.domino.xsp.formula;

import java.lang.ref.SoftReference;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;

import org.openntf.domino.xsp.model.DominoDocumentMapAdapter;
import org.openntf.formula.ASTNode;
import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaParseException;
import org.openntf.formula.Formulas;

import com.ibm.xsp.binding.MethodBindingEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.ValueBindingUtil;

/**
 * This is a MethodBinding for formula language in XPages. It just executes the formula
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class FormulaMethodBinding extends MethodBindingEx {
	private String formulaStr;
	private transient SoftReference<ASTNode> astNodeCache;

	/**
	 * Constructor
	 * 
	 * @param str
	 *            the formula (without #{...} delimiters)
	 * @param paramClasses
	 *            not yet used here
	 */
	public FormulaMethodBinding(final String str, final Class<?>[] paramClasses) {
		// TODO Auto-generated constructor stub
		this.formulaStr = (str != null ? str.intern() : null);
	}

	/**
	 * Trivial Constructor (needed for restoreState)
	 */
	public FormulaMethodBinding() {
		this.formulaStr = null;
	}

	/**
	 * The type. Not used here
	 * 
	 * @return {@link Object}.class
	 */
	@Override
	public Class<?> getType(final FacesContext arg0) throws MethodNotFoundException {
		return Object.class;
	}

	/**
	 * Invokes the binding (and the formula)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(final FacesContext ctx, final Object[] arg1) throws EvaluationException, MethodNotFoundException {
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
		try {
			FormulaContextXsp fctx = (FormulaContextXsp) Formulas.createContext(dataMap, Formulas.getParser());
			fctx.init(this.getComponent(), ctx);
			return getASTNode().solve(fctx);
		} catch (EvaluateException e) {
			throw new EvaluationException(e);
		} catch (FormulaParseException e) {
			throw new EvaluationException(e);
		} finally {
			if (rootWasNull) {
				ctx.setViewRoot(null);
			}
		}

	}

	/**
	 * Returns the corresponding {@link ASTNode} for the formula
	 * 
	 * @return {@link ASTNode}
	 * @throws FormulaParseException
	 *             if the formula was invalid
	 */
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
	public Object saveState(final FacesContext ctx) {
		Object[] arr = new Object[2];
		arr[0] = super.saveState(ctx);
		arr[1] = this.formulaStr;
		return arr;
	}

	@Override
	public void restoreState(final FacesContext ctx, final Object obj) {
		Object[] arr = (Object[]) obj;
		super.restoreState(ctx, arr[0]);
		this.formulaStr = ((String) arr[1]);
	}
}
