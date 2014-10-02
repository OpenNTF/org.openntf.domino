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
package org.openntf.domino.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoFormatter.
 */
public class DominoFormatter extends ThreadLocal<Object> {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DominoFormatter.class.getName());

	/** The date only format_. */
	private String dateOnlyFormat_;

	/** The time only format_. */
	private String timeOnlyFormat_;

	/** The date time format_. */
	private String dateTimeFormat_;

	/** The df_. */
	private SimpleDateFormat tdf_;
	private SimpleDateFormat ddf_;
	private SimpleDateFormat dtdf_;

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
	 * Instantiates a new Domino formatter.
	 * 
	 * @param intl
	 *            the intl
	 * @throws NotesException
	 *             the notes exception
	 */
	public DominoFormatter(final lotus.domino.International intl) throws NotesException {
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

	private DateFormat getTimeOnlyFormat() {
		if (tdf_ == null) {
			tdf_ = new SimpleDateFormat(timeOnlyFormat_, ULocale.getDefault());
		}
		return tdf_;
	}

	private DateFormat getDateOnlyFormat() {
		if (ddf_ == null) {
			ddf_ = new SimpleDateFormat(dateOnlyFormat_, ULocale.getDefault());
		}
		return ddf_;
	}

	private DateFormat getDateTimeFormat() {
		if (dtdf_ == null) {
			dtdf_ = new SimpleDateFormat(dateTimeFormat_, ULocale.getDefault());
		}
		return dtdf_;
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
	public String getDateOnly(final Date date) {
		DateFormat df = getDateOnlyFormat();
		synchronized (df) {
			return df.format(date);
		}
	}

	/**
	 * Gets the time only.
	 * 
	 * @param date
	 *            the date
	 * @return the time only
	 */
	public String getTimeOnly(final Date date) {
		DateFormat df = getTimeOnlyFormat();
		synchronized (df) {
			return df.format(date);
		}
	}

	/**
	 * Gets the date time.
	 * 
	 * @param date
	 *            the date
	 * @return the date time
	 */
	public String getDateTime(final Date date) {
		DateFormat df = getDateTimeFormat();
		synchronized (df) {
			return df.format(date);
		}
	}

	/**
	 * Parses the date from string.
	 * 
	 * @param dateString
	 *            the date string
	 * @return the date
	 */
	public Date parseDateFromString(final String dateString) {
		DateFormat df = getDateTimeFormat();
		synchronized (df) {
			try {
				return df.parse(dateString);
			} catch (ParseException e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
	}

}
