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
package org.openntf.domino.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import lotus.domino.NotesException;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoFormatter.
 */
public class DominoFormatter extends ThreadLocal<Object> {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoFormatter.class.getName());

	/** The date only format_. */
	private String dateOnlyFormat_;

	/** The time only format_. */
	private String timeOnlyFormat_;

	/** The date time format_. */
	private String dateTimeFormat_;

	/** The df_. */
	private final SimpleDateFormat df_ = new SimpleDateFormat();

	/** The am_. */
	private String am_;

	/** The date sep_. */
	private String dateSep_;

	/** The pm_. */
	private String pm_;

	/** The time sep_. */
	private String timeSep_;

	/** The time24_. */
	private boolean time24_;

	/** The dmy_. */
	private boolean dmy_;

	/** The mdy_. */
	private boolean mdy_;

	/** The ymd_. */
	private boolean ymd_;

	/**
	 * Instantiates a new domino formatter.
	 * 
	 * @param intl
	 *            the intl
	 * @throws NotesException
	 *             the notes exception
	 */
	public DominoFormatter(lotus.domino.International intl) throws NotesException {
		am_ = intl.getAMString();
		dateSep_ = intl.getDateSep();
		pm_ = intl.getPMString();
		timeSep_ = intl.getTimeSep();
		time24_ = intl.isTime24Hour();
		dmy_ = intl.isDateDMY();
		mdy_ = intl.isDateMDY();
		ymd_ = intl.isDateYMD();
		if (dmy_) {
			dateOnlyFormat_ = "dd" + dateSep_ + "MM" + dateSep_ + "yyyy";
		} else if (mdy_) {
			dateOnlyFormat_ = "MM" + dateSep_ + "dd" + dateSep_ + "yyyy";
		} else {
			dateOnlyFormat_ = "yyyy" + dateSep_ + "MM" + dateSep_ + "dd";
		}

		if (time24_) {
			timeOnlyFormat_ = "HH" + timeSep_ + "mm" + timeSep_ + "ss";
		} else {
			timeOnlyFormat_ = "hh" + timeSep_ + "mm" + timeSep_ + "ss aaa";
		}

		dateTimeFormat_ = dateOnlyFormat_ + " " + timeOnlyFormat_;
		org.openntf.domino.impl.Base.s_recycle(intl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ThreadLocal#initialValue()
	 */
	@Override
	protected Object initialValue() {
		return super.initialValue();
	}

	/**
	 * Gets the date only.
	 * 
	 * @param date
	 *            the date
	 * @return the date only
	 */
	public String getDateOnly(Date date) {
		synchronized (df_) {
			df_.applyPattern(dateOnlyFormat_);
			return df_.format(date);
		}
	}

	/**
	 * Gets the time only.
	 * 
	 * @param date
	 *            the date
	 * @return the time only
	 */
	public String getTimeOnly(Date date) {
		synchronized (df_) {
			df_.applyPattern(timeOnlyFormat_);
			return df_.format(date);
		}
	}

	/**
	 * Gets the date time.
	 * 
	 * @param date
	 *            the date
	 * @return the date time
	 */
	public String getDateTime(Date date) {
		synchronized (df_) {
			df_.applyPattern(dateTimeFormat_);
			return df_.format(date);
		}
	}

	public Date parseDateFromString(String dateString) {
		synchronized (df_) {
			df_.applyPattern(dateTimeFormat_);
			try {
				return df_.parse(dateString);
			} catch (ParseException e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
	}

}
