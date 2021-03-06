/* Generated By:JJTree: Do not edit this line. ASTAssignment.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
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
 */
package org.openntf.formula.ast;

import java.util.Set;

import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaReturnException;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.parse.AtFormulaParserImpl;

/**
 * ASTAssignment stores the value in a FIELD, VAR or ENV-VAR
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class ASTAssignment extends SimpleNode {
	public static final int FIELD = 1;
	public static final int VAR = 2;
	public static final int ENV = 3;
	public static final int DEFAULT = 4;

	private int type;
	private String varName;
	/** for performance reasons, value is stored also in lowerCase */
	private String varNameLC;

	public ASTAssignment(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	/**
	 * Called from the parser. Init varName and type
	 */
	public void init(final String _varName, final int _type) {
		varName = _varName;
		varNameLC = varName.toLowerCase();
		type = _type;

	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.SimpleNode#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		switch (type) {
		case FIELD:
			return super.toString() + ": FIELD " + varName;

		case VAR:
			return super.toString() + ": VAR " + varName;

		case ENV:
			return super.toString() + ": ENV " + varName;

		case DEFAULT:
			return super.toString() + ": DEFAULT " + varName;
		}
		return super.toString() + ": ? " + varName;
	}

	/**
	 * ASTAssignment stores the value in a FIELD, VAR or ENV-VAR. There is no special error handling needed here.
	 */
	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		ValueHolder value;
		value = children[0].evaluate(ctx);

		switch (type) {
		case FIELD:
			ctx.setField(varName, value);
			break;

		case VAR:
			ctx.setVarLC(varNameLC, value);
			break;

		case ENV:
			ctx.setEnv(varName, value.getString(0));
			break;

		case DEFAULT:
			ctx.setDefaultLC(varNameLC, varName, value);
			break;
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.SimpleNode#analyzeThis(java.util.Set, java.util.Set, java.util.Set, java.util.Set)
	 */
	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		switch (type) {
		case FIELD:
			modifiedFields.add(varName.toLowerCase());
			break;

		case VAR:
		case DEFAULT:
			variables.add(varName.toLowerCase());
			break;

		case ENV:
			break;
		}

	}
}
/* JavaCC - OriginalChecksum=5985303b30076529d3e2aa0d267e0702 (do not edit this line) */
