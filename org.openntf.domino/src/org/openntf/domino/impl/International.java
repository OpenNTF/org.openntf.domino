/*
 * Copyright 2013
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
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class International.
 */
public class International extends Base<org.openntf.domino.International, lotus.domino.International, Session> implements
		org.openntf.domino.International {

	/**
	 * Instantiates a new international.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public International(final lotus.domino.International delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

	/**
	 * Instantiates a new International.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public International(final lotus.domino.International delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_INTL);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.International delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Session.SCHEMA, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getAMString()
	 */
	@Override
	public String getAMString() {
		try {
			return getDelegate().getAMString();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getCurrencyDigits()
	 */
	@Override
	public int getCurrencyDigits() {
		try {
			return getDelegate().getCurrencyDigits();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getCurrencySymbol()
	 */
	@Override
	public String getCurrencySymbol() {
		try {
			return getDelegate().getCurrencySymbol();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getDateSep()
	 */
	@Override
	public String getDateSep() {
		try {
			return getDelegate().getDateSep();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getDecimalSep()
	 */
	@Override
	public String getDecimalSep() {
		try {
			return getDelegate().getDecimalSep();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getPMString()
	 */
	@Override
	public String getPMString() {
		try {
			return getDelegate().getPMString();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getThousandsSep()
	 */
	@Override
	public String getThousandsSep() {
		try {
			return getDelegate().getThousandsSep();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getTimeSep()
	 */
	@Override
	public String getTimeSep() {
		try {
			return getDelegate().getTimeSep();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getTimeZone()
	 */
	@Override
	public int getTimeZone() {
		try {
			return getDelegate().getTimeZone();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getToday()
	 */
	@Override
	public String getToday() {
		try {
			return getDelegate().getToday();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getTomorrow()
	 */
	@Override
	public String getTomorrow() {
		try {
			return getDelegate().getTomorrow();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#getYesterday()
	 */
	@Override
	public String getYesterday() {
		try {
			return getDelegate().getYesterday();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isCurrencySpace()
	 */
	@Override
	public boolean isCurrencySpace() {
		try {
			return getDelegate().isCurrencySpace();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isCurrencySuffix()
	 */
	@Override
	public boolean isCurrencySuffix() {
		try {
			return getDelegate().isCurrencySuffix();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isCurrencyZero()
	 */
	@Override
	public boolean isCurrencyZero() {
		try {
			return getDelegate().isCurrencyZero();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isDST()
	 */
	@Override
	public boolean isDST() {
		try {
			return getDelegate().isDST();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isDateDMY()
	 */
	@Override
	public boolean isDateDMY() {
		try {
			return getDelegate().isDateDMY();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isDateMDY()
	 */
	@Override
	public boolean isDateMDY() {
		try {
			return getDelegate().isDateMDY();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isDateYMD()
	 */
	@Override
	public boolean isDateYMD() {
		try {
			return getDelegate().isDateYMD();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.International#isTime24Hour()
	 */
	@Override
	public boolean isTime24Hour() {
		try {
			return getDelegate().isTime24Hour();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
