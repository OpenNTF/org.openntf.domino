/**
 * 
 */
package org.openntf.domino.helpers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.TypeUtils;

/**
 * @author nfreeman
 * 
 */
public class Formula implements org.openntf.domino.ext.Formula, Serializable {
	private static final Logger log_ = Logger.getLogger(Formula.class.getName());
	private static final long serialVersionUID = 1L;

	public static interface Decompiler {
		public String decompile(byte[] compiled) throws Exception;

		public String decompileB64(String compiled);
	}

	private Decompiler decompiler_;

	public void setDecompiler(final Decompiler decomp) {
		decompiler_ = decomp;
	}

	static class NoFormulaSetException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		NoFormulaSetException() {
			super("No expression has been set. There is nothing to evaluate.");
		}
	}

	public static class FormulaUnableToDecompile extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		FormulaUnableToDecompile(final String original) {
			super("Unable to decompile a compiled expression: " + original);
		}
	}

	public static class FormulaSyntaxException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Vector<?> syntaxDetails_;
		// "errorMessage" : "errorLine" : "errorColumn" : "errorOffset" : "errorLength" : "errorText"
		private String expression_;

		FormulaSyntaxException(final String expression, final Vector<Object> syntaxDetails) {
			super();
			expression_ = expression;
			syntaxDetails_ = syntaxDetails;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(0));
			} else {
				return "Details unavailable";
			}
		}

		/**
		 * @return the expression
		 */
		public String getExpression() {
			return expression_;
		}

		public String getErrorLine() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(1));
			} else {
				return "Details unavailable";
			}
		}

		public String getErrorColumn() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(2));
			} else {
				return "Details unavailable";
			}
		}

		public String getErrorOffset() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(3));
			} else {
				return "Details unavailable";
			}
		}

		public String getErrorLength() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(4));
			} else {
				return "Details unavailable";
			}
		}

		public String getErrorText() {
			if (syntaxDetails_ != null) {
				return String.valueOf(syntaxDetails_.get(5));
			} else {
				return "Details unavailable";
			}
		}

	}

	private transient Session parent_;
	private String expression_;
	private boolean isValid_;

	/**
	 * 
	 */
	public Formula() {
		// TODO Auto-generated constructor stub
	}

	public Formula(final Session parent) {
		parent_ = parent;
	}

	public Formula(final String expression) throws FormulaSyntaxException {
		this();
		try {
			setExpression(expression);
		} catch (FormulaSyntaxException fe) {
			isValid_ = false;
			log_.log(Level.INFO, "Error confirming formula syntax: " + fe.getExpression() + " (" + fe.getErrorText() + ")");
		}
	}

	@Override
	public void setSession(final Session session) {
		parent_ = session;
	}

	@Override
	public String getExpression() {
		return expression_;
	}

	public Parser getParser() {
		if (!isValid_)
			return null;
		return new Parser(getExpression());
	}

	public void setExpression(final String expression, final boolean force) {
		isValid_ = true;
		expression_ = expression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(String expression) {
		if (expression.length() > 2000) {
			isValid_ = true;
			expression_ = expression;
			return;
		}
		Vector<Object> vec = getSession().evaluate("@CheckFormulaSyntax({" + DominoUtils.escapeForFormulaString(expression) + "})");
		if (vec == null) {
			isValid_ = false;
		} else if (vec.size() > 2) {
			isValid_ = false;
		} else {
			isValid_ = true;
		}
		if (!isValid_) {
			if (decompiler_ != null && !expression.contains("@") && !expression.contains(";")) {//NTF - good chance its compiled
				expression = decompiler_.decompileB64(expression);
				if (expression != null) {
					vec = getSession().evaluate("@CheckFormulaSyntax({" + DominoUtils.escapeForFormulaString(expression) + "})");
					if (vec == null || vec.size() > 2) {
						isValid_ = false;
						throw new FormulaSyntaxException(expression, vec);
					} else {
						System.out.println("Successfully decompiled a formula!");
						isValid_ = true;
					}
				} else {
					throw new FormulaUnableToDecompile(expression);
				}
			} else {
				throw new FormulaSyntaxException(expression, vec);
			}
		}
		if (isValid_) {
			expression_ = expression;
		}
	}

	private Session getSession() {
		if (parent_ == null) {
			// CHECKME RPr: Is that the correct session
			parent_ = Factory.getSession(SessionType.CURRENT);
		}
		return parent_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue()
	 */
	@Override
	public Vector<Object> getValue() {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector<Object> vec = getSession().evaluate(expression_);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(java.lang.Class)
	 */
	@Override
	public <T> T getValue(final Class<T> type) {
		Vector<Object> v = getValue();
		return TypeUtils.collectionToClass(v, type, getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue()
	 */
	@Override
	public Vector<Object> getValue(final Session session) {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector<Object> vec = session.evaluate(expression_);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(java.lang.Class)
	 */
	@Override
	public <T> T getValue(final Session session, final Class<T> type) {
		Vector<Object> v = getValue(session);
		return TypeUtils.collectionToClass(v, type, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(org.openntf.domino.Document)
	 */
	@Override
	public Vector<Object> getValue(final Document document) {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector<Object> vec = document.getAncestorSession().evaluate(expression_, document);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(org.openntf.domino.Document, java.lang.Class)
	 */
	@Override
	public <T> T getValue(final Document document, final Class<T> type) {
		Vector<Object> v = getValue(document);
		return TypeUtils.collectionToClass(v, type, document.getAncestorSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		expression_ = in.readUTF();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeUTF(expression_);
	}

	public static class ParserException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		private final String expression_;

		public ParserException(final String message, final String expression) {
			super(message);
			expression_ = expression;
		}

		public String getExpression() {
			return expression_;
		}
	}

	public static class Parser {
		/*
		 * NTF - Please note that this parser is pretty brutal. It's not intended to construct ASTs from Formula.
		 * It's intended to allow for analytics of Formula code. So searching for the use of specific @functions,
		 * literals, keywords, and to try to differentiate local variables from field-based variable references.
		 * 
		 * A proper AST parser for Formula would be considerably more involved and honestly of questionable value
		 * since actually having knowledge of the syntax tree would only be useful in an interpretter/converter 
		 * context.
		 */
		@SuppressWarnings("unused")
		private static final Logger log_ = Logger.getLogger(Formula.Parser.class.getName());
		public static final String DEFAULT = "DEFAULT";
		public static final String REM = "REM";
		public static final String ENVIRONMENT = "ENVIRONMENT";
		public static final String FIELD = "FIELD";
		public static final String LOCAL = "LOCAL";

		private final String source_;
		private Set<String> functions_;
		private Set<String> keywords_;
		private Set<String> localVars_;
		private Set<String> fieldVars_;
		private Set<String> envVars_;
		private Set<String> literals_;
		private Set<String> numberLiterals_;
		private Set<String> variables_;
		private Set<String> commands_;
		private Stack<String> identStack_;
		//		private boolean inLiteral_;
		//		private boolean inBracket_;
		//		private boolean inEscape_;
		private boolean inRightSide_;
		@SuppressWarnings("unused")
		private Boolean isAssignment_;
		@SuppressWarnings("unused")
		private boolean justClosedKeyword_;
		@SuppressWarnings("unused")
		private boolean justClosedExpression_;
		private int parenDepth_ = 0;
		private char lastOpChar_;
		private String curStatementType_;

		//		private StringBuilder buffer_;

		Parser(final String source) {
			source_ = source;
		}

		public void parse() {
			parseStatement(source_);
			//			String[] lines = source_.split("\\n");
			//			for (String line : lines) {
			//				parseLine(line);
			//			}
		}

		public void parseStatement(final String statement) {
			inRightSide_ = false;
			curStatementType_ = "";
			if (statement != null) {
				String result = statement.replaceAll("\\n", "");
				result = result.replaceAll("\\r", "").trim();
				while (result != null && result.length() > 0) {
					//				System.out.println("Parsing next statement");
					if (result.startsWith(REM)) {
						isAssignment_ = Boolean.FALSE;
						result = parseComment(result.substring(REM.length()).trim());
					} else if (result.startsWith(DEFAULT)) {
						result = parseDefaultStatement(result.substring(DEFAULT.length()).trim());
					} else if (result.startsWith(ENVIRONMENT)) {
						result = parseEnvironmentStatement(result.substring(ENVIRONMENT.length()).trim());
					} else if (result.startsWith(FIELD)) {
						result = parseFieldStatement(result.substring(FIELD.length()).trim());
					} else {
						//					curStatementType_ = "";
						isAssignment_ = null;	//we don't know whether this will be an assignment until we see ':='
						result = parseNextStatement(result);
						//					curStatementType_ = "";
					}
					//				inRightSide_ = false;
				}
			}
		}

		public String parseDefaultStatement(final String line) {
			//			System.out.println("Parsing Default");
			curStatementType_ = DEFAULT;
			isAssignment_ = Boolean.TRUE;
			String result = parseNextStatement(line);
			return result.trim();
		}

		public String parseFieldStatement(final String line) {
			//			System.out.println("Parsing Field");
			curStatementType_ = FIELD;
			isAssignment_ = Boolean.TRUE;
			String result = parseNextStatement(line);
			//			curStatementType_ = "";
			return result.trim();
		}

		public String parseEnvironmentStatement(final String line) {
			//			System.out.println("Parsing Environment");
			curStatementType_ = ENVIRONMENT;
			isAssignment_ = Boolean.TRUE;
			String result = parseNextStatement(line);
			//			curStatementType_ = "";
			return result.trim();
		}

		public String parseComment(final String line) {
			//			System.out.println("Parsing Comment");
			curStatementType_ = REM;
			String result = parseNextStatement(line);	//we expect this to immediately lead to a literal and then be done
			char[] chars = result.toCharArray();
			int pos = 0;
			for (char c : chars) {
				pos++;
				if (c == ' ' || c == ';') {
					curStatementType_ = "";
					return result.substring(pos).trim();
				} else {

				}
			}
			//			curStatementType_ = "";
			System.out.println("End of comment statement reached and no more characters are available");
			return "";
		}

		public String parseNextQuoteLiteral(final String statement) {
			boolean inEscape = false;
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == '\\') {
					if (inEscape) {
						inEscape = false;
						buffer.append(c);
						//						System.out.println("Found escaped quote!");
					} else {
						inEscape = true;
					}
				} else if (c == '"') {
					if (inEscape) {
						inEscape = false;
						buffer.append(c);
						//						System.out.println("Found escaped quote!");
					} else {
						getLiterals().add(buffer.toString());
						return statement.substring(pos);
					}
				} else {
					buffer.append(c);
					if (inEscape)
						inEscape = false;
				}
			}
			throw new ParserException("End of line reached before end of string literal", statement);
		}

		public String parseNextBraceLiteral(final String statement) {
			//			System.out.println("Parsing brace literal");
			boolean inEscape = false;
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == '\\') {
					if (inEscape) {
						buffer.append(c);
						inEscape = false;
					} else {
						inEscape = true;
					}
				} else if (c == '}') {
					if (inEscape) {
						buffer.append(c);
						inEscape = false;
					} else {
						getLiterals().add(buffer.toString());
						return statement.substring(pos);
					}
				} else {
					buffer.append(c);
					if (inEscape)
						inEscape = false;
				}
			}
			throw new ParserException("End of line reached before end of string literal", statement);
		}

		public String parseNextKeyword(final String statement) {
			//			System.out.println("Parsing Keyword");
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == ']') {
					String keyword = buffer.toString();
					getKeywords().add(keyword);
					//					System.out.println("Found keyword: " + keyword);
					return statement.substring(pos);
				} else {
					buffer.append(c);
				}
			}
			throw new ParserException("End of line reached before end of keyword", statement);
		}

		public String parseNextLiteral(final String statement, final boolean isBrace) {
			//			System.out.println("Parsing Literal");
			if (isBrace) {
				return parseNextBraceLiteral(statement);
			} else {
				try {
					return parseNextQuoteLiteral(statement);
				} catch (ParserException pe) {
					pe.printStackTrace();
					System.out.println(pe.getExpression());
					return "";
				}
			}
		}

		public String parseNextNumberLiteral(final String statement, final char startingDigit) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			buffer.append(startingDigit);
			for (char c : chars) {
				pos++;
				if (Character.isDigit(c)) {
					buffer.append(c);
				} else if (c == ';') {
					pos--;
					break;
				} else if (c == ':') {
					lastOpChar_ = c;
					break;
				} else if (c == '(') {
					parenDepth_++;
					//					System.out.println("Opening parens " + parenDepth_);
					break;
				} else if (c == ')') {
					parenDepth_--;
					//					System.out.println("Closing parens " + parenDepth_);
					break;
				} else if (c == '[') {
					pos--;
					break;
				} else if (c == ']') {
					break;
				} else {
					break;
				}
			}
			getNumberLiterals().add(buffer.toString());
			return statement.substring(pos);
			//			throw new ParserException("End of line reached before end of number literal", statement);
		}

		public String parseNextFunction(final String statement) {
			//			System.out.println("Parsing Function");
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == '(') {
					getFunctions().add(buffer.toString());
					parenDepth_++;
					//					System.out.println("Opening parens " + parenDepth_);
					return statement.substring(pos);
				} else if (c == ')') {
					getFunctions().add(buffer.toString());
					parenDepth_--;
					//					System.out.println("Closing parens " + parenDepth_);
					return statement.substring(pos);
				} else if (c == ' ') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				} else if (c == ';') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos - 1);	//let the calling method handle the end of the statement
				} else if (c == '[') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos - 1);
				} else if (c == ']') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				} else {
					buffer.append(c);
				}
			}
			String function = buffer.toString();
			getFunctions().add(function);
			//			log_.log(Level.INFO, "Statement ended with a function: " + function);
			return "";
		}

		public String parseNextIdentifier(final String statement, final char startingChar) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			buffer.append(startingChar);
			for (char c : chars) {
				pos++;
				if (c == ';') {
					pos--;
					break;
				} else if (c == ':') {
					lastOpChar_ = c;
					break;
				} else if (c == '(') {
					parenDepth_++;
					//					System.out.println("Opening parens " + parenDepth_);
					break;
				} else if (c == ')') {
					parenDepth_--;
					//					System.out.println("Closing parens " + parenDepth_);
					break;
				} else if (c == '[') {
					//					parenDepth_++;
					pos--;
					break;
				} else if (c == ']') {
					//					parenDepth_--;
					break;
				} else if (isOperator(c)) {
					break;
				} else if (c == ' ') {
					break;
				} else {
					buffer.append(c);
				}
			}
			String identifier = buffer.toString();
			getIdentStack().push(identifier);
			if (inRightSide_ && !getLocalVars().contains(identifier)) {
				//				System.out.println("Adding to field Vars because not in localvar list and we're in the right side");
				getFieldVars().add(identifier);
			} else {
				if (curStatementType_.equals(FIELD)) {
					getFieldVars().add(identifier);
				} else if (curStatementType_.equals(ENVIRONMENT)) {
					getEnvVars().add(identifier);
				} else {
					//					getLocalVars().add(identifier);
				}
			}
			//			System.out.println("Completed parsing identifier: " + identifier + " on " + (inRightSide_ ? "right" : "left"));
			return statement.substring(pos);
		}

		public String parseNextStatement(final String segment) {
			//			System.out.println("Parsing " + curStatementType_ + " Statement Type");
			if (segment == null)
				return "";
			char[] chars = segment.toCharArray();
			int pos = 0;
			String result = "";
			for (char c : chars) {
				pos++;
				if (c == '{') {
					String nextSegment = segment.substring(pos);
					result = parseNextLiteral(nextSegment, true);
					return result;
				} else if (c == '"') {
					String nextSegment = segment.substring(pos);
					result = parseNextLiteral(nextSegment, false);
					return result;
				} else if (c == ':') {
					lastOpChar_ = c;
				} else if (c == '=') {
					if (lastOpChar_ == ':') {
						//assignment taking place!
						//						System.out.println("Found assignment!");
						inRightSide_ = true;
						String lastIdent = getIdentStack().pop();
						if (curStatementType_.equals(FIELD)) {
							getFieldVars().add(lastIdent);
						} else if (curStatementType_.equals(DEFAULT)) {
							getLocalVars().add(lastIdent);
						} else if (curStatementType_.equals(ENVIRONMENT)) {
							getEnvVars().add(lastIdent);
						} else if (curStatementType_.length() < 1) {
							curStatementType_ = LOCAL;
							getLocalVars().add(lastIdent);
						}
						lastOpChar_ = c;
					}
				} else if (c == '[') {
					String nextSegment = segment.substring(pos);
					result = parseNextKeyword(nextSegment);
					return result;
				} else if (c == '@') {
					String nextSegment = segment.substring(pos);
					result = parseNextFunction(nextSegment);
					return result;
				} else if (Character.isLetter(c) || c == '_' || c == '$') {
					String nextSegment = segment.substring(pos);
					result = parseNextIdentifier(nextSegment, c);
					return result;
				} else if (Character.isDigit(c)) {
					String nextSegment = segment.substring(pos);
					result = parseNextNumberLiteral(nextSegment, c);
					return result;
				} else if (isOperator(c)) {
					//					System.out.println("Skipping an operator " + c);
				} else if (c == '(') {
					parenDepth_++;
					//					System.out.println("Opening parens " + parenDepth_);
				} else if (c == ')') {
					parenDepth_--;
					//					System.out.println("Closing parens " + parenDepth_);
				} else if (c == '\n') {
					//					System.out.println("Skipping whitespace");
				} else if (c == '\t') {
					//					System.out.println("Skipping whitespace");
				} else if (c == '\r') {
					//					System.out.println("Skipping whitespace");
				} else if (c == ' ') {
					//					System.out.println("Skipping whitespace");
				} else if (c == ';') {
					if (parenDepth_ == 0) {
						//						System.out.println("Statement completed");
						inRightSide_ = false;
						if (curStatementType_ == null || curStatementType_.length() < 1) {
							//							System.out.println("Ending result expression");

							for (String ident : getIdentStack()) {
								if (!getLocalVars().contains(ident)) {
									getFieldVars().add(ident);	//if the variable was never defined locally, it's most likely a field
								}
							}

						} else {
							//							System.out.println("Ending " + curStatementType_ + " expression");
						}
						getIdentStack().clear();
						curStatementType_ = "";
						return segment.substring(pos).trim();
					} else {
						//						System.out.println("Next argument...");
					}
				}
			}
			return result;
		}

		//		public void parseAllSegments(final String statement) {
		//			char[] chars = statement.toCharArray();
		//			int pos = 0;
		//			StringBuilder buffer = new StringBuilder();
		//			String result = null;
		//			for (char c : chars) {
		//				pos++;
		//				String nextSegment = statement.substring(pos);
		//
		//			}
		//		}

		//		public void parseStatement(final String line) {
		//			char[] chars = line.toCharArray();
		//			StringBuilder buffer = new StringBuilder();
		//			int pos = 0;
		//			for (char c : chars) {
		//				pos++;
		//
		//			}
		//
		//		}

		public static boolean isOperator(final char c) {
			return isListOp(c) || isMathOp(c) || isLogicOp(c);
		}

		public static boolean isListOp(final char c) {
			return c == ':';
		}

		public static boolean isMathOp(final char c) {
			return c == '+' || c == '=' || c == '<' || c == '>' || c == '*' || c == '/';
		}

		public static boolean isLogicOp(final char c) {
			return c == '|' || c == '!' || c == '&';
		}

		/**
		 * @return the functions
		 */
		public Set<String> getFunctions() {
			if (functions_ == null) {
				functions_ = new LinkedHashSet<String>();
			}
			return functions_;
		}

		/**
		 * @return the keywords
		 */
		public Set<String> getKeywords() {
			if (keywords_ == null) {
				keywords_ = new LinkedHashSet<String>();
			}
			return keywords_;
		}

		/**
		 * @return the localVars
		 */
		public Set<String> getLocalVars() {
			if (localVars_ == null) {
				localVars_ = new LinkedHashSet<String>();
			}
			return localVars_;
		}

		/**
		 * @return the envVars
		 */
		public Set<String> getEnvVars() {
			if (envVars_ == null) {
				envVars_ = new LinkedHashSet<String>();
			}
			return envVars_;
		}

		/**
		 * @return the variables
		 */
		public Set<String> getVariables() {
			if (variables_ == null) {
				variables_ = new LinkedHashSet<String>();
			}
			return variables_;
		}

		/**
		 * @return the commands
		 */
		public Set<String> getCommands() {
			if (commands_ == null) {
				commands_ = new LinkedHashSet<String>();
			}
			return commands_;
		}

		public Set<String> getLiterals() {
			if (literals_ == null) {
				literals_ = new LinkedHashSet<String>();
			}
			return literals_;
		}

		public Set<String> getNumberLiterals() {
			if (numberLiterals_ == null) {
				numberLiterals_ = new LinkedHashSet<String>();
			}
			return numberLiterals_;
		}

		public Set<String> getFieldVars() {
			if (fieldVars_ == null) {
				fieldVars_ = new LinkedHashSet<String>();
			}
			return fieldVars_;
		}

		public Stack<String> getIdentStack() {
			if (identStack_ == null) {
				identStack_ = new Stack<String>();
			}
			return identStack_;
		}
	}

}
