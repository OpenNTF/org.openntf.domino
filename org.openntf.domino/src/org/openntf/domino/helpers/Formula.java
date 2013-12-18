/**
 * 
 */
package org.openntf.domino.helpers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.TypeUtils;

/**
 * @author nfreeman
 * 
 */
public class Formula implements org.openntf.domino.ext.Formula, Serializable {
	private static final Logger log_ = Logger.getLogger(Formula.class.getName());
	private static final long serialVersionUID = 1L;

	static class NoFormulaSetException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		NoFormulaSetException() {
			super("No expression has been set. There is nothing to evaluate.");
		}
	}

	static class FormulaSyntaxException extends RuntimeException {
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
			return String.valueOf(syntaxDetails_.get(0));
		}

		/**
		 * @return the expression
		 */
		public String getExpression() {
			return expression_;
		}

		public String getErrorLine() {
			return String.valueOf(syntaxDetails_.get(1));
		}

		public String getErrorColumn() {
			return String.valueOf(syntaxDetails_.get(2));
		}

		public String getErrorOffset() {
			return String.valueOf(syntaxDetails_.get(3));
		}

		public String getErrorLength() {
			return String.valueOf(syntaxDetails_.get(4));
		}

		public String getErrorText() {
			return String.valueOf(syntaxDetails_.get(5));
		}

	}

	private transient Session parent_;
	private String expression_;

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
		setExpression(expression);
	}

	public void setSession(final Session session) {
		parent_ = session;
	}

	public String getExpression() {
		return expression_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(final String expression) {
		Vector<Object> vec = getSession().evaluate("@CheckFormulaSyntax(\"" + DominoUtils.escapeForFormulaString(expression) + "\")");
		if (vec.size() > 2) {
			throw new FormulaSyntaxException(expression, vec);
		}
		expression_ = expression;
	}

	private Session getSession() {
		if (parent_ == null) {
			parent_ = Factory.getSession();
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
	public <T> T getValue(final Class<?> T) {
		Vector<Object> v = getValue();
		return TypeUtils.vectorToClass(v, T, getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue()
	 */
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
	public <T> T getValue(final Session session, final Class<?> T) {
		Vector<Object> v = getValue(session);
		return TypeUtils.vectorToClass(v, T, session);
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
	public <T> T getValue(final Document document, final Class<?> T) {
		Vector<Object> v = getValue(document);
		return TypeUtils.vectorToClass(v, T, document.getAncestorSession());
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
		private static final Logger log_ = Logger.getLogger(Formula.Parser.class.getName());

		private final String source_;
		private List<String> functions_;
		private List<String> keywords_;
		private List<String> localVars_;
		private List<String> fieldVars_;
		private List<String> envVars_;
		private List<String> literals_;
		private List<String> numberLiterals_;
		private List<String> variables_;
		private List<String> commands_;
		//		private boolean inLiteral_;
		//		private boolean inBracket_;
		//		private boolean inEscape_;
		private boolean inRightSide_;

		//		private StringBuilder buffer_;

		Parser(final String source) {
			source_ = source;
		}

		public void parse() {
			String[] lines = source_.split("\\n");
			for (String line : lines) {
				parseLine(line);
			}
		}

		/**
		 * @return the functions
		 */
		public List<String> getFunctions() {
			if (functions_ == null) {
				functions_ = new ArrayList<String>();
			}
			return functions_;
		}

		/**
		 * @return the keywords
		 */
		public List<String> getKeywords() {
			if (keywords_ == null) {
				keywords_ = new ArrayList<String>();
			}
			return keywords_;
		}

		/**
		 * @return the localVars
		 */
		public List<String> getLocalVars() {
			if (localVars_ == null) {
				localVars_ = new ArrayList<String>();
			}
			return localVars_;
		}

		/**
		 * @return the envVars
		 */
		public List<String> getEnvVars() {
			if (envVars_ == null) {
				envVars_ = new ArrayList<String>();
			}
			return envVars_;
		}

		/**
		 * @return the variables
		 */
		public List<String> getVariables() {
			if (variables_ == null) {
				variables_ = new ArrayList<String>();
			}
			return variables_;
		}

		/**
		 * @return the commands
		 */
		public List<String> getCommands() {
			if (commands_ == null) {
				commands_ = new ArrayList<String>();
			}
			return commands_;
		}

		public List<String> getLiterals() {
			if (literals_ == null) {
				literals_ = new ArrayList<String>();
			}
			return literals_;
		}

		public List<String> getNumberLiterals() {
			if (numberLiterals_ == null) {
				numberLiterals_ = new ArrayList<String>();
			}
			return numberLiterals_;
		}

		public List<String> getFieldVars() {
			if (fieldVars_ == null) {
				fieldVars_ = new ArrayList<String>();
			}
			return fieldVars_;
		}

		public void parseLine(final String line) {
			inRightSide_ = false;
			if (line.startsWith("REM")) {
				parseComment(line.substring("REM".length()).trim());
			} else if (line.startsWith("DEFAULT")) {
				parseDefaultStatement(line.substring("DEFAULT".length()).trim());
			} else if (line.startsWith("ENVIRONMENT")) {
				parseEnvironmentStatement(line.substring("ENVIRONMENT".length()).trim());
			} else if (line.startsWith("FIELD")) {
				parseFieldStatement(line.substring("FIELD".length()).trim());
			} else {
				parseStatement(line);
			}
		}

		public void parseDefaultStatement(final String line) {

		}

		public void parseFieldStatement(final String line) {

		}

		public void parseEnvironmentStatement(final String line) {

		}

		public void parseComment(final String line) {
			String result = parseNextSegment(line);	//we expect this to immediately lead to a literal and then be done
			char[] chars = result.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == ' ' || c == ';') {

				} else {

				}
			}
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
						buffer.append(c);
						inEscape = false;
					} else {
						inEscape = true;
					}
				} else if (c == '"') {
					if (inEscape) {
						buffer.append(c);
						inEscape = false;
					} else {
						getLiterals().add(buffer.toString());
						return statement.substring(pos);
					}
				} else {
					buffer.append(c);
				}
			}
			throw new ParserException("End of line reached before end of string literal", statement);
		}

		public String parseNextBraceLiteral(final String statement) {
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
				}
			}
			throw new ParserException("End of line reached before end of string literal", statement);
		}

		public String parseNextKeyword(final String statement) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			for (char c : chars) {
				pos++;
				if (c == ']') {
					getLiterals().add(buffer.toString());
					return statement.substring(pos);
				} else {
					buffer.append(c);
				}
			}
			throw new ParserException("End of line reached before end of keyword", statement);
		}

		public String parseNextLiteral(final String statement, final boolean isBrace) {
			if (isBrace) {
				return parseNextBraceLiteral(statement);
			} else {
				return parseNextQuoteLiteral(statement);
			}
		}

		public String parseNextNumberLiteral(final String statement, final char startingDigit) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			buffer.append(startingDigit);
			String result = null;
			for (char c : chars) {
				pos++;
				if (Character.isDigit(c)) {
					buffer.append(c);
				} else {
					getNumberLiterals().add(buffer.toString());
					return statement.substring(pos);
				}
			}
			throw new ParserException("End of line reached before end of number literal", statement);
		}

		public String parseNextFunction(final String statement) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			String result = null;
			for (char c : chars) {
				pos++;
				if (c == '(') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				} else if (c == ' ') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				} else if (c == ';') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				}
			}
			return "";
		}

		public String parseNextIdentifier(final String statement) {
			char[] chars = statement.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			String result = null;
			for (char c : chars) {
				pos++;
				if (c == ';') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				} else if (c == ' ') {
					getFunctions().add(buffer.toString());
					return statement.substring(pos);
				}
			}
			return "";
		}

		public String parseNextSegment(final String segment) {
			char[] chars = segment.toCharArray();
			int pos = 0;
			StringBuilder buffer = new StringBuilder();
			String result = null;
			for (char c : chars) {
				pos++;
				if (c == '{') {
					result = parseNextLiteral(segment.substring(pos), true);
					return result;
				} else if (c == '"') {
					result = parseNextLiteral(segment.substring(pos), false);
					return result;
				} else if (c == '[') {
					result = parseNextKeyword(segment.substring(pos));
				} else if (c == '@') {
					result = parseNextFunction(segment.substring(pos));
					return result;
				} else if (Character.isLetter(c) || c == '_' || c == '$') {
					result = parseNextIdentifier(segment.substring(pos));
					return result;
				} else if (Character.isDigit(c)) {

				} else if (c == ' ') {

				}
			}
			return result;
		}

		public void parseStatement(final String line) {
			char[] chars = line.toCharArray();
			StringBuilder buffer = new StringBuilder();
			int pos = 0;
			for (char c : chars) {
				pos++;

			}

		}

	}

}
