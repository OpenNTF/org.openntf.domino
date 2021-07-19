/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.tests.ntf;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

@SuppressWarnings("unused")
public class DateFormatTester implements Runnable {
	public static void main(final String[] args) {
		Thread thread = new Thread(new DateFormatTester(), "My thread");
		thread.start();
	}

	private static String DATE_TIME = "09/02/2018 02:03:51 PM";
	private static String DATE_ONLY = "09/02/2018";
	private static final DateTimeFormatter GMT_FORMAT_2 = new DateTimeFormatterBuilder().appendPattern("MM/dd/uuuu hh:mm:ss a")
			.parseCaseInsensitive().toFormatter(Locale.ENGLISH).withResolverStyle(ResolverStyle.LENIENT)
			.withZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC));
	private static final DateTimeFormatter GMT_FORMAT_DATE = new DateTimeFormatterBuilder().appendPattern("MM/dd/uuuu")
			.parseCaseInsensitive().toFormatter(Locale.ENGLISH).withResolverStyle(ResolverStyle.LENIENT)
			.withZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC));

	public DateFormatTester() {
		// whatever you might want to do in your constructor, but stay away from Domino objects
	}

	@Override
	public void run() {
		try {
			ZonedDateTime zdt = ZonedDateTime.parse(DATE_TIME, GMT_FORMAT_2);
			System.out.println("Result: " + zdt.toString());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		try {
			LocalDate zdt = LocalDate.parse(DATE_ONLY, GMT_FORMAT_DATE);
			System.out.println("Result: " + zdt.toString());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
