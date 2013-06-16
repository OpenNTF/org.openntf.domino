/**
 * 
 */
package org.openntf.domino.helpers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
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

		FormulaSyntaxException(final String expression, final Vector syntaxDetails) {
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

	public void setSession(final Session session) {
		parent_ = session;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(final String expression) {
		Vector vec = getSession().evaluate("@CheckFormulaSyntax(" + expression + ")");
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
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getValue() {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector vec = getSession().evaluate(expression_);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> T getValue(final Class<?> T) {
		Vector v = getValue();
		return TypeUtils.vectorToClass(v, T, getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue()
	 */
	@SuppressWarnings("rawtypes")
	public Vector getValue(final Session session) {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector vec = session.evaluate(expression_);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public <T> T getValue(final Session session, final Class<?> T) {
		Vector v = getValue(session);
		return TypeUtils.vectorToClass(v, T, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(org.openntf.domino.Document)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getValue(final Document document) {
		if (expression_ == null)
			throw new NoFormulaSetException();
		Vector vec = document.getAncestorSession().evaluate(expression_, document);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Formula#getValue(org.openntf.domino.Document, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> T getValue(final Document document, final Class<?> T) {
		Vector v = getValue(document);
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
}
