/*
 * Copyright OpenNTF 2013
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

// TODO: Auto-generated Javadoc
/**
 * The Class International.
 */
public class International extends Base<org.openntf.domino.International, lotus.domino.International> implements
		org.openntf.domino.International {

	/**
	 * Instantiates a new international.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public International(lotus.domino.International delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getAMString()
	 */
	@Override
	public String getAMString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getCurrencyDigits()
	 */
	@Override
	public int getCurrencyDigits() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getCurrencySymbol()
	 */
	@Override
	public String getCurrencySymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getDateSep()
	 */
	@Override
	public String getDateSep() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getDecimalSep()
	 */
	@Override
	public String getDecimalSep() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getPMString()
	 */
	@Override
	public String getPMString() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getThousandsSep()
	 */
	@Override
	public String getThousandsSep() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getTimeSep()
	 */
	@Override
	public String getTimeSep() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getTimeZone()
	 */
	@Override
	public int getTimeZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getToday()
	 */
	@Override
	public String getToday() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getTomorrow()
	 */
	@Override
	public String getTomorrow() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#getYesterday()
	 */
	@Override
	public String getYesterday() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isCurrencySpace()
	 */
	@Override
	public boolean isCurrencySpace() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isCurrencySuffix()
	 */
	@Override
	public boolean isCurrencySuffix() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isCurrencyZero()
	 */
	@Override
	public boolean isCurrencyZero() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isDST()
	 */
	@Override
	public boolean isDST() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isDateDMY()
	 */
	@Override
	public boolean isDateDMY() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isDateMDY()
	 */
	@Override
	public boolean isDateMDY() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isDateYMD()
	 */
	@Override
	public boolean isDateYMD() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.International#isTime24Hour()
	 */
	@Override
	public boolean isTime24Hour() {
		// TODO Auto-generated method stub
		return false;
	}
}
