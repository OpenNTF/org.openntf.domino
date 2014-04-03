/*
 * © Copyright FOCONIS AG, 2014
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
 * 
 */
package org.openntf.domino.formula;

import java.util.Date;
import java.util.Locale;

import org.openntf.domino.ISimpleDateTime;

import com.ibm.icu.util.Calendar;

// TODO RPr This is not yet finished
public interface Formatter {

	Locale getLocale();

	ISimpleDateTime getNewSDTInstance();

	ISimpleDateTime getNewInitializedSDTInstance(Date date, boolean noDate, boolean noTime);

	ISimpleDateTime getCopyOfSDTInstance(ISimpleDateTime sdt);

	ISimpleDateTime parseDate(String image);

	Calendar parseDateToCal(String image, boolean[] noDT);

	Number parseNumber(String image);

	String formatDateTime(ISimpleDateTime sdt);

	String formatDateTime(ISimpleDateTime sdt, String lotusOpts);

	String formatCalDateTime(Calendar cal);

	String formatCalDateOnly(Calendar cal);

	String formatCalTimeOnly(Calendar cal);

	String formatNumber(Number n);

	String formatNumber(Number n, String lotusOpts);
}
