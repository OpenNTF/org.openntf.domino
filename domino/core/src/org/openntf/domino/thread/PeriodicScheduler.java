package org.openntf.domino.thread;

import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.ibm.commons.util.StringUtil;

public class PeriodicScheduler implements Scheduler {
	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());

	// computation is done on an calendar object, as this is easier.
	final Calendar nextExecTime = Calendar.getInstance();
	int periodSecond = 0;

	// startSecond & endSecond defines a time window
	int startSecond = 0;
	int endSecond = 24 * 60 * 60;

	// day bits for all days 
	private static final int ALL_DAYS = 1 << Calendar.SUNDAY | 1 << Calendar.MONDAY | 1 << Calendar.TUESDAY | 1 << Calendar.WEDNESDAY
			| 1 << Calendar.THURSDAY | 1 << Calendar.FRIDAY | 1 << Calendar.SATURDAY;

	int dayBits = ALL_DAYS;

	/**
	 * Constructs a new periodic scheduler
	 * 
	 * @param start
	 *            the next possible start time
	 * @param period
	 *            If positive: the period, if negative: the delay, if zero: not periodic
	 * @param timeUnit
	 *            the timeUnit of start & period
	 */
	public PeriodicScheduler(final long delay, final long period, final TimeUnit timeUnit) {
		super();
		this.nextExecTime.setTimeInMillis(System.currentTimeMillis() + timeUnit.toMillis(delay));
		this.periodSecond = (int) timeUnit.toSeconds(period);
	}

	/**
	 * Constructs a new periodic scheduler
	 * 
	 * @param defString
	 */
	public PeriodicScheduler(String defString) {
		int sign;
		if (defString.startsWith("period:")) {
			defString = defString.substring(7);
			sign = +1;
		} else if (defString.startsWith("delay:")) {
			defString = defString.substring(6);
			sign = -1;
		} else {
			throw new NumberFormatException("Invalid Time Definition String: " + defString);
		}
		StringTokenizer strTok = new StringTokenizer(defString);

		if (!strTok.hasMoreTokens()) { // delay value
			throw new NumberFormatException("Invalid Time Definition String: " + defString);
		}

		this.nextExecTime.setTimeInMillis(System.currentTimeMillis());
		this.periodSecond = parseToSeconds(strTok.nextToken()) * sign;

		addJitter(1.0);

		if (strTok.hasMoreTokens()) { // time window
			String[] parts = StringUtil.splitString(strTok.nextToken(), '-');
			if (parts.length != 2)
				throw new NumberFormatException("Invalid Time Definition String: " + defString);
			this.startSecond = parseToSeconds(parts[0]);
			this.endSecond = parseToSeconds(parts[1]);
			if (strTok.hasMoreTokens()) { // days
				setDays(strTok.nextToken());
			}
		}
		shiftToNextTimeWindow();

	}

	private void addJitter(final double d) {
		this.nextExecTime.add(Calendar.SECOND, (int) (Math.abs(this.periodSecond) * Math.random() * d)); // add some random jitter
	}

	/**
	 * Set the time window
	 * 
	 * @param startSecond
	 *            the start of the time window
	 * @param endSecond
	 *            the end of the time window
	 * @param days
	 *            the days {@link Calendar#SUNDAY} - {@link Calendar#SATURDAY}
	 */
	public void setTimeWindow(final int startSecond, final int endSecond, final int[] days) {
		this.startSecond = startSecond;
		this.endSecond = endSecond;

		this.dayBits = 0;
		for (int day : days) {
			this.dayBits |= 1 << day;
		}
		if (dayBits < (1 << Calendar.SUNDAY))
			throw new IllegalStateException("No valid day bits set");
	}

	/**
	 * Parses a time string to seconds
	 * 
	 * @param str
	 *            13:37 => 13*3600 + 37*60
	 * @return
	 */
	private int parseToSeconds(final String str) {

		int parts[] = new int[3];
		if (str.indexOf(':') != -1) {
			String[] strParts = StringUtil.splitString(str, ':');
			if (strParts.length == 3) { // 00:00:00
				parts[0] = Integer.valueOf(strParts[0]); // hour
				parts[1] = Integer.valueOf(strParts[1]); // minute
				parts[2] = Integer.valueOf(strParts[2]); // seconds
			} else if (strParts.length == 2) {
				parts[0] = Integer.valueOf(strParts[0]); // hour
				parts[1] = Integer.valueOf(strParts[1]); // minute
				parts[2] = 0; // seconds
			} else {
				throw new NumberFormatException(str + " is not in the format 00:00 or 00:00:00");
			}
		} else {
			// the string is in the format 90m30s or 2h30
			int tmp = 0;
			int idx = 0; // Hour
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if ('0' <= ch & ch <= '9') {	// numeric
					tmp = tmp * 10 + (ch - '0');
					parts[idx] = tmp; // save the computed value at current parts position 
				} else if (ch == 'h' & idx == 0) { // h is only allowed on first position 
					tmp = 0;
					idx = 1;
				} else if (ch == 'm' & idx < 1) {
					tmp = 0;
					if (idx == 0) { // no hour specified
						parts[1] = parts[0];
						parts[0] = 0;
					}
					idx = 1;
				} else if (ch == 's' & idx < 2) {
					tmp = 0;
					if (idx == 0) { // no hour specified
						parts[2] = parts[0];
						parts[1] = 0;
						parts[0] = 0;
					} else if (idx == 1) { // no minute specified
						parts[2] = parts[1];
						parts[1] = 0;
					}
					idx = 2;
				} else {
					throw new NumberFormatException(str + " is not in the format 00, 00h 00h00m or 00h00m00s");
				}
			}
		}

		return parts[0] * 3600 + parts[1] * 60 + parts[2];
	}

	public void setDays(final String days) {
		dayBits = 0;
		for (int i = 0; i < days.length(); i++) {
			char ch = days.charAt(i);
			switch (ch) {
			case 'M':
			case 'm':
				dayBits |= 1 << Calendar.MONDAY;
				break;

			case 'T':
			case 't':
				dayBits |= 1 << Calendar.TUESDAY;
				break;

			case 'W':
			case 'w':
				dayBits |= 1 << Calendar.WEDNESDAY;
				break;

			case 'R':
			case 'r':
				dayBits |= 1 << Calendar.THURSDAY;
				break;

			case 'F':
			case 'f':
				dayBits |= 1 << Calendar.FRIDAY;
				break;

			case 'S':
			case 's':
				dayBits |= 1 << Calendar.SATURDAY;
				break;

			case 'U':
			case 'u':
				dayBits |= 1 << Calendar.SUNDAY;
				break;

			}
		}
		if (dayBits < (1 << Calendar.SUNDAY))
			throw new IllegalStateException("No valid day bits set");
	}

	@Override
	public void eventStart(final Calendar now) {
		if (periodSecond > 0) {
			synchronized (nextExecTime) {
				nextExecTime.add(Calendar.SECOND, periodSecond);
				if (nextExecTime.before(now)) {
					nextExecTime.setTimeInMillis(now.getTimeInMillis());
					addJitter(0.1);
					log_.info("Misfire detected, setting next execution time to: " + nextExecTime);
				}
				shiftToNextTimeWindow();
			}
		}
	}

	@Override
	public void eventStop(final Calendar now) {
		if (periodSecond < 0) {
			synchronized (nextExecTime) {
				nextExecTime.setTimeInMillis(now.getTimeInMillis());
				nextExecTime.add(Calendar.SECOND, -periodSecond);
				shiftToNextTimeWindow();
			}
		}
	}

	protected void shiftToNextTimeWindow() {
		if (startSecond == 0 && endSecond == 24 * 60 * 60 && dayBits == ALL_DAYS) {
			return; // nothing to do
		}

		//int jitter = (int) TimeUnit.SECONDS.convert((long) (this.period * Math.random()), TimeUnit.NANOSECONDS);
		int secondOfTheDay = nextExecTime.get(Calendar.HOUR_OF_DAY) * 3600 + nextExecTime.get(Calendar.MINUTE) * 60
				+ nextExecTime.get(Calendar.SECOND);

		if ((dayBits & (1 << nextExecTime.get(Calendar.DAY_OF_WEEK))) == 0 || secondOfTheDay > startSecond) {
			// not allowed for this day, so goto next day
			nextExecTime.set(Calendar.HOUR_OF_DAY, 0);
			nextExecTime.set(Calendar.MINUTE, 0);
			nextExecTime.set(Calendar.MILLISECOND, 0);
			nextExecTime.set(Calendar.SECOND, startSecond); // somewhere on next start window
			addJitter(0.5);
			// and now, find next day
			int i = 0;
			do {
				nextExecTime.add(Calendar.DAY_OF_MONTH, 1);
				if (i++ > 7) {
					throw new IllegalStateException("Could not determine next execution time for the next 7 days");
				}
			} while ((dayBits & (1 << nextExecTime.get(Calendar.DAY_OF_WEEK))) == 0);

		} else if (secondOfTheDay < startSecond) {
			// before allowed execution (at this day)
			nextExecTime.set(Calendar.SECOND, startSecond);
			addJitter(0.5);
		}
	}

	@Override
	public long getNextExecutionTimeInMillis() {
		return nextExecTime.getTimeInMillis();
	}

	@Override
	public boolean isPeriodic() {
		return periodSecond != 0L;
	}

	private String getDayString() {
		if (dayBits == ALL_DAYS)
			return "every day";
		String[] namesOfDays = new String[] { "Spare-day", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= 7; i++) {
			if ((dayBits & (1 << i)) != 0) {
				if (sb.length() > 0)
					sb.append(',');
				sb.append(namesOfDays[i]);
			}
		}
		return sb.toString();
	}

	private String getTime(final int seconds) {
		int hr = seconds / 3600;
		int rem = seconds % 3600;
		int mn = rem / 60;
		int sec = rem % 60;
		StringBuilder sb = new StringBuilder();
		if (hr < 10)
			sb.append('0');
		sb.append(hr);
		sb.append(':');
		if (mn < 10)
			sb.append('0');
		sb.append(mn);
		sb.append(':');
		if (sec < 10)
			sb.append('0');
		sb.append(sec);
		return sb.toString();
	}

	private String getTimeWindow() {
		if (startSecond == 0 && endSecond == 24 * 60 * 60)
			return "all time";
		return getTime(startSecond) + "-" + getTime(endSecond);
	}

	@Override
	public String toString() {
		return "Next Exec: " + nextExecTime.getTime() + ", period: " + getTime(periodSecond) + ", " + getTimeWindow() + " "
				+ getDayString();
	}
}
