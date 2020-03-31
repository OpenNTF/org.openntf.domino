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
package org.openntf.domino.nsfdata;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NSFDateTime implements Serializable, NSFDateTimeValue {
	private static final long serialVersionUID = 1L;

	private static final ThreadLocal<DateFormat> DATE_TIME_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); };
	};
	private static final ThreadLocal<DateFormat> DATE_ONLY_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() { return new SimpleDateFormat("yyyy-MM-dd"); };
	};
	private static final ThreadLocal<DateFormat> TIME_ONLY_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() { return new SimpleDateFormat("HH:mm:ss.SSS"); };
	};

	private final java.util.Date dateTime_;
	private final java.sql.Time timeOnly_;
	private final java.sql.Date dateOnly_;
	private final int utcOffset_;
	private final boolean dst_;

	public NSFDateTime(final java.util.Date dateTime, final int utcOffset, final boolean dst) {
		dateTime_ = dateTime;
		timeOnly_ = null;
		dateOnly_ = null;
		utcOffset_ = utcOffset;
		dst_ = dst;
	}
	public NSFDateTime(final java.sql.Time timeOnly) {
		dateTime_ = null;
		timeOnly_ = timeOnly;
		dateOnly_ = null;
		utcOffset_ = 0;
		dst_ = false;
	}
	public NSFDateTime(final java.sql.Date dateOnly) {
		dateTime_ = null;
		timeOnly_ = null;
		dateOnly_ = dateOnly;
		utcOffset_ = 0;
		dst_ = false;
	}

	public java.util.Date toDate() {
		long time = 0;
		if(dateTime_ != null) {
			time = dateTime_.getTime();
		} else if(timeOnly_ != null) {
			time = timeOnly_.getTime();
		} else if(dateOnly_ != null) {
			time = dateOnly_.getTime();
		}
		return new java.util.Date(time);
	}

	public int getUTCOffset() {
		return utcOffset_;
	}
	public boolean isDST() {
		return dst_;
	}

	public boolean isTimeOnly() {
		return timeOnly_ != null;
	}

	public boolean isDateOnly() {
		return dateOnly_ != null;
	}

	@Override
	public String toString() {
		String stringVal = null;
		if(dateTime_ != null) {
			stringVal = DATE_TIME_FORMAT.get().format(dateTime_);
		} else if(timeOnly_ != null) {
			java.util.Date date = new java.util.Date(timeOnly_.getTime());
			stringVal = TIME_ONLY_FORMAT.get().format(date);
		} else if(dateOnly_ != null) {
			java.util.Date date = new java.util.Date(dateOnly_.getTime());
			stringVal = DATE_ONLY_FORMAT.get().format(date);
		}
		return "[" + getClass().getSimpleName() + ": " + stringVal + "]";
	}
}
