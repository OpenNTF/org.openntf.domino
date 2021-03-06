/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openntf.formula.EvaluateException;
import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaReturnException;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.ValueHolder.DataType;
import org.openntf.formula.parse.AtFormulaParserImpl;
import org.openntf.formula.parse.Token;

public abstract class SimpleNode implements Node {

	protected Node parent;
	protected Node[] children;
	protected int id;
	protected int codeLine;
	protected int codeColumn;
	// these need not to be serialized!
	protected transient AtFormulaParserImpl parser;
	private transient Set<String> functions;
	private transient Set<String> variables;
	private transient Set<String> readFields;
	private transient Set<String> modifiedFields;
	private String formula;

	/**
	 * create a new node with ID and parser as reference
	 */
	public SimpleNode(final AtFormulaParserImpl p, final int i) {
		id = i;
		parser = p;
		Token t = p.token;
		codeLine = t.beginLine;
		codeColumn = t.beginColumn;
	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ASTNode#setFormula(java.lang.String)
	 */
	public void setFormula(final String formula) {
		this.formula = formula;
	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ASTNode#getFormula()
	 */
	public String getFormula() {
		if (formula != null)
			return formula;
		if (parent != null)
			return parent.getFormula();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtOpen()
	 */
	public void jjtOpen() {
	}

	/*
	* (non-Javadoc)
	* @see org.openntf.formula.ast.Node#jjtClose()
	*/
	public void jjtClose() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtSetParent(org.openntf.formula.ast.Node)
	 */
	public void jjtSetParent(final Node n) {
		parent = n;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtGetParent()
	 */
	public Node jjtGetParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtAddChild(org.openntf.formula.ast.Node, int)
	 */
	public void jjtAddChild(final Node n, final int i) {
		if (children == null) {
			children = new Node[i + 1];
		} else if (i >= children.length) {
			Node c[] = new Node[i + 1];
			System.arraycopy(children, 0, c, 0, children.length);
			children = c;
		}
		children[i] = n;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtGetChild(int)
	 */
	public Node jjtGetChild(final int i) {
		return children[i];
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#jjtGetNumChildren()
	 */
	public int jjtGetNumChildren() {
		return (children == null) ? 0 : children.length;
	}

	/**
	 * The default toString returns the node name
	 */
	@Override
	public String toString() {
		return AtFormulaParserImplTreeConstants.jjtNodeName[id];
	}

	/**
	 * Returns a prefix and the node name. Used for "dump"
	 * 
	 * @param prefix
	 *            the prefix (Spaces, to indent the string
	 * @return a String that describes the node
	 */
	public String toString(final String prefix) {
		return prefix + toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#dump(java.lang.String)
	 */
	public void dump(final String prefix) {
		System.out.println(toString(prefix));
		if (children != null) {
			for (int i = 0; i < children.length; ++i) {
				SimpleNode n = (SimpleNode) children[i];
				if (n != null) {
					n.dump(prefix + " "); //$NON-NLS-1$
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#evaluate(org.openntf.formula.FormulaContext)
	 */
	public abstract ValueHolder evaluate(FormulaContext ctx) throws FormulaReturnException;

	/* (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#solve(org.openntf.formula.FormulaContext)
	 */
	@Override
	public final List<Object> solve(final FormulaContext ctx) throws EvaluateException {
		try {
			ValueHolder vh;
			try {
				vh = evaluate(ctx);
			} catch (FormulaReturnException e) {
				vh = e.getValue();
			}
			if (vh.dataType == DataType.ERROR)
				throw vh.getError();
			return vh.toList();
		} catch (EvaluateException ev) {
			throw ev;
		}
	}

	// =================== formula inspection ===============================

	/**
	 * inspects formulas recursively to detect the elements that are used
	 */
	public void inspect(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {

		analyzeThis(readFields, modifiedFields, variables, functions);
		if (children == null)
			return;
		for (int i = 0; i < children.length; i++) {
			((SimpleNode) children[i]).inspect(readFields, modifiedFields, variables, functions);
		}
	}

	/**
	 * This method must be implemented in every AST node.
	 * 
	 * @param readFields
	 *            return value - which fields are used (lowercase)
	 * @param modifiedFields
	 *            return value - which fields are modified (lowercase)
	 * @param variables
	 *            return value - which variables are used (lowercase)
	 * @param functions
	 *            return value - which functions are used (lowercase)
	 */
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {

	}

	/**
	 * Initializion of inspection is done once
	 */
	private void initInspection() {
		if (readFields != null)
			return;
		readFields = new HashSet<String>();
		functions = new HashSet<String>();
		variables = new HashSet<String>();
		modifiedFields = new HashSet<String>();
		inspect(readFields, modifiedFields, variables, functions);
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#getFunctions()
	 */
	public Set<String> getFunctions() {
		initInspection();
		return functions;

	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#getVariables()
	 */
	public Set<String> getVariables() {
		initInspection();
		return variables;

	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#getReadFields()
	 */
	public Set<String> getReadFields() {
		initInspection();
		return readFields;

	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.formula.ast.Node#getModifiedFields()
	 */
	public Set<String> getModifiedFields() {
		initInspection();
		return modifiedFields;

	}

}

/* JavaCC - OriginalChecksum=f757f6165359770f80bc5b67ed15fa71 (do not edit this line) */
