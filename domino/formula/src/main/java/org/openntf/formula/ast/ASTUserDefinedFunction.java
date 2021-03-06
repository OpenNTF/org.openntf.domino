/* Generated By:JJTree: Do not edit this line. ASTUserDefinedFunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.openntf.formula.ast;

import java.util.Set;

import org.openntf.formula.FormulaContext;
import org.openntf.formula.FormulaReturnException;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.impl.UserDefinedFunction;
import org.openntf.formula.parse.AtFormulaParserImpl;

/**
 * In the org.openntf.formula engine, you can define own functions:
 * 
 * <br/>
 * <br/>
 * <b>Example:</b> Define the function "myFunc" with 1-2 parametes:<br/>
 * <code>{@literal @}function( {@literal @}myfunc( a; b:=3) ) = {@literal @}Left(a,b+1);</code><br/>
 * <br/>
 * Calling <code>{@literal @}myfunc( "Hello" )</code> will return "Hell" ( the same as <code>{@literal @}Left("Hello";4)</code> )
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class ASTUserDefinedFunction extends SimpleNode {

	public ASTUserDefinedFunction(final AtFormulaParserImpl p, final int id) {
		super(p, id);
	}

	@Override
	public ValueHolder evaluate(final FormulaContext ctx) throws FormulaReturnException {
		return ValueHolder.valueOf(""); //$NON-NLS-1$
	}

	@Override
	protected void analyzeThis(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
		// CHECKME RPr Should we list custom definde functions in the function list
	}

	/**
	 * A extended function needs not to inspect it's children. If the function is never invoked, nothing is needed
	 */
	@Override
	public void inspect(final Set<String> readFields, final Set<String> modifiedFields, final Set<String> variables,
			final Set<String> functions) {
	}

	/**
	 * Regarding this formula: {@literal @}function( {@literal @}myfunc( a; b:=3); x; y ) = {@literal @}Left(a,b+1);
	 * 
	 * 
	 * <ul>
	 * <li>This Node is "@function"</li>
	 * 
	 * <ul>
	 * <li>The first child of this AST-Node is the ASTUserDefinedFunctionDef-node (="@myfunc")</li>
	 * <ul>
	 * <li>{@literal @}myfunc itself has ASTUserDefinedFunctionParameters as child nodes (Here: "a" and "b:=3")</li>
	 * </ul>
	 * <li>The second to (n-1) child not of this node are optional and declares parameter variables used in this formula. (Here "x" and "y")
	 * </li> <li>The last node is the concrete implementation of {@literal @}myFunc</li> </ul> </ul>
	 */
	public void init() {
		// Regarding this formula: @function( @myfunc( a; b:=3); x; y ) = @Left(a,b+1);
		// This Node is "@function"
		// 		The first child of this AST-Node is the ASTUserDefinedFunctionDef-node (="@myfunc")
		//			@myfunc itself has ASTUserDefinedFunctionParameters as child nodes
		//		The second to (n-1) child not of this node are optional and declares parameter variables used in this formula. (Here "x" and "y"
		//		
		int functionVariables = 0;
		ASTUserDefinedFunctionDef def = (ASTUserDefinedFunctionDef) children[0];

		UserDefinedFunction function = def.getFunction();

		for (int i = 1; i < children.length; i++) {
			if (children[i] instanceof ASTUserDefinedFunctionVariable) {
				functionVariables++;
			} else {
				function.setFunction(children[i]);
			}
		}

		ASTUserDefinedFunctionVariable[] var = new ASTUserDefinedFunctionVariable[functionVariables];
		for (int i = 0; i < functionVariables; i++) {
			var[i] = (ASTUserDefinedFunctionVariable) children[i + 1];
		}

		function.setVariables(var);

	}
}
/* JavaCC - OriginalChecksum=3108394236ef426155ab016a1734bf8a (do not edit this line) */
