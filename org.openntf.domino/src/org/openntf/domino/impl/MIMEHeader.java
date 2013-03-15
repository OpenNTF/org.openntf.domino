package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class MIMEHeader extends Base<org.openntf.domino.MIMEHeader, lotus.domino.MIMEHeader> implements org.openntf.domino.MIMEHeader {

	public MIMEHeader(lotus.domino.MIMEHeader delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public boolean addValText(String valueText) {
		try {
			return getDelegate().addValText(valueText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean addValText(String valueText, String charSet) {
		try {
			return getDelegate().addValText(valueText, charSet);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getHeaderName() {
		try {
			return getDelegate().getHeaderName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderVal() {
		try {
			return getDelegate().getHeaderVal();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderVal(boolean folded) {
		try {
			return getDelegate().getHeaderVal(folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderVal(boolean folded, boolean decoded) {
		try {
			return getDelegate().getHeaderVal(folded, decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderValAndParams() {
		try {
			return getDelegate().getHeaderValAndParams();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderValAndParams(boolean folded) {
		try {
			return getDelegate().getHeaderValAndParams(folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHeaderValAndParams(boolean folded, boolean decoded) {
		try {
			return getDelegate().getHeaderValAndParams(folded, decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getParamVal(String paramName) {
		try {
			return getDelegate().getParamVal(paramName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getParamVal(String paramName, boolean folded) {
		try {
			return getDelegate().getParamVal(paramName, folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean setHeaderVal(String headerValue) {
		try {
			return getDelegate().setHeaderVal(headerValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setHeaderValAndParams(String headerParamValue) {
		try {
			return getDelegate().setHeaderValAndParams(headerParamValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setParamVal(String parameterName, String parameterValue) {
		try {
			return getDelegate().setParamVal(parameterName, parameterValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}
}
