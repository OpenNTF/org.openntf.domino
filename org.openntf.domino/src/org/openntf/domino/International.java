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
package org.openntf.domino;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface International.
 */
public interface International extends Base<lotus.domino.International>, lotus.domino.International, org.openntf.domino.ext.International,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getAMString()
	 */
	@Override
	public String getAMString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getCurrencyDigits()
	 */
	@Override
	public int getCurrencyDigits();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getCurrencySymbol()
	 */
	@Override
	public String getCurrencySymbol();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getDateSep()
	 */
	@Override
	public String getDateSep();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getDecimalSep()
	 */
	@Override
	public String getDecimalSep();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getPMString()
	 */
	@Override
	public String getPMString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getThousandsSep()
	 */
	@Override
	public String getThousandsSep();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getTimeSep()
	 */
	@Override
	public String getTimeSep();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getTimeZone()
	 */
	@Override
	public int getTimeZone();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getToday()
	 */
	@Override
	public String getToday();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getTomorrow()
	 */
	@Override
	public String getTomorrow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#getYesterday()
	 */
	@Override
	public String getYesterday();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isCurrencySpace()
	 */
	@Override
	public boolean isCurrencySpace();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isCurrencySuffix()
	 */
	@Override
	public boolean isCurrencySuffix();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isCurrencyZero()
	 */
	@Override
	public boolean isCurrencyZero();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isDateDMY()
	 */
	@Override
	public boolean isDateDMY();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isDateMDY()
	 */
	@Override
	public boolean isDateMDY();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isDateYMD()
	 */
	@Override
	public boolean isDateYMD();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isDST()
	 */
	@Override
	public boolean isDST();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.International#isTime24Hour()
	 */
	@Override
	public boolean isTime24Hour();

}
