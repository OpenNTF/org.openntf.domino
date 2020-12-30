/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import lotus.domino.NotesException;

/**
 * The Class DominoFormatter.
 */
@SuppressWarnings("nls")
public class DominoFormatter extends ThreadLocal<Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DominoFormatter.class.getName());

	private String dateOnlyFormat_;

	private String timeOnlyFormat_;

	private String dateTimeFormat_;

	private SimpleDateFormat tdf_;
	private SimpleDateFormat ddf_;
	private SimpleDateFormat dtdf_;

	@SuppressWarnings("unused")
	private String am_;

	private String dateSep_;

	@SuppressWarnings("unused")
	private String pm_;

	private String timeSep_;

	private boolean time24_;

	private boolean dmy_;

	private boolean mdy_;

	@SuppressWarnings("unused")
	private boolean ymd_;

	/**
	 * Instantiates a new Domino formatter.
	 * 
	 * @param intl
	 *            the intl
	 * @throws NotesException
	 *             the notes exception
	 */
	public DominoFormatter(final org.openntf.domino.International intl) throws NotesException {
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
	}

	private DateFormat getTimeOnlyFormat() {
		if (tdf_ == null) {
			tdf_ = new SimpleDateFormat(timeOnlyFormat_, Locale.getDefault());
		}
		return tdf_;
	}

	private DateFormat getDateOnlyFormat() {
		if (ddf_ == null) {
			ddf_ = new SimpleDateFormat(dateOnlyFormat_, Locale.getDefault());
		}
		return ddf_;
	}

	private DateFormat getDateTimeFormat() {
		if (dtdf_ == null) {
			dtdf_ = new SimpleDateFormat(dateTimeFormat_, Locale.getDefault());
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
