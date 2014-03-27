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

// TODO RPr remove domino.DateTime completely!
import org.openntf.domino.DateTime;

public class DominoFormatter implements Formatter {
	private static DominoFormatter instance;

	public DateTime parseDate(final String image) throws java.text.ParseException {
		//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return new org.openntf.domino.impl.DateTime(image);
		//		try {
		//			//Date d = formatter.parse(image);
		//			DateTime dt = new org.openntf.domino.impl.DateTime(image);
		//			//dt.set
		//			//dt.setLocalTime(image);
		//
		//			//dt.setAnyTime();
		//			return dt;
		//		} catch (Exception e) {
		//			throw new java.text.ParseException(e.getMessage(), 0);
		//		}

	}

	public Number parseNumber(final String el) {
		//NumberFormat nf = NumberFormat.getInstance();
		// TODO Auto-generated method stub
		return Double.valueOf(el.replace(',', '.'));
	}

	public static synchronized Formatter getInstance() {
		if (instance == null) {
			instance = new DominoFormatter();
		}
		return instance;
	}
}
