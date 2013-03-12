package org.openntf.domino.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import lotus.domino.NotesException;

public class DominoFormatter extends ThreadLocal<Object> {
	private static final Logger log_ = Logger.getLogger(DominoFormatter.class.getName());
	private String dateOnlyFormat_;
	private String timeOnlyFormat_;
	private String dateTimeFormat_;
	private final SimpleDateFormat df_ = new SimpleDateFormat();
	private String am_;
	private String dateSep_;
	private String pm_;
	private String timeSep_;
	private boolean time24_;
	private boolean dmy_;
	private boolean mdy_;
	private boolean ymd_;

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
		org.openntf.domino.impl.Base.recycle(intl);
	}

	@Override
	protected Object initialValue() {
		return super.initialValue();
	}

	public String getDateOnly(Date date) {
		synchronized (df_) {
			df_.applyPattern(dateOnlyFormat_);
			return df_.format(date);
		}
	}

	public synchronized String getTimeOnly(Date date) {
		synchronized (df_) {
			df_.applyPattern(timeOnlyFormat_);
			return df_.format(date);
		}
	}

	public synchronized String getDateTime(Date date) {
		synchronized (df_) {
			df_.applyPattern(dateTimeFormat_);
			return df_.format(date);
		}
	}

}
