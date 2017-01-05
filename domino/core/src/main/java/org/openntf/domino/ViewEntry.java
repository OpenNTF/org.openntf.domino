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

import java.util.Map;
import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface ViewEntry.
 */
public interface ViewEntry extends lotus.domino.ViewEntry, org.openntf.domino.ext.ViewEntry, Base<lotus.domino.ViewEntry>,
DatabaseDescendant, Map<String, Object> {

	public static class Schema extends FactorySchema<ViewEntry, lotus.domino.ViewEntry, View> {
		@Override
		public Class<ViewEntry> typeClass() {
			return ViewEntry.class;
		}

		@Override
		public Class<lotus.domino.ViewEntry> delegateClass() {
			return lotus.domino.ViewEntry.class;
		}

		@Override
		public Class<View> parentClass() {
			return View.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getChildCount()
	 */
	@Override
	public int getChildCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getColumnIndentLevel()
	 */
	@Override
	public int getColumnIndentLevel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getColumnValues()
	 */
	@Override
	public Vector<Object> getColumnValues();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getDescendantCount()
	 */
	@Override
	public int getDescendantCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getDocument()
	 */
	@Override
	public Document getDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getFTSearchScore()
	 */
	@Override
	public int getFTSearchScore();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getIndentLevel()
	 */
	@Override
	public int getIndentLevel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getNoteID()
	 */
	@Override
	public String getNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getNoteIDAsInt()
	 */
	@Override
	public int getNoteIDAsInt();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getParent()
	 */
	@Override
	public Base<?> getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getPosition(char)
	 */
	@Override
	public String getPosition(final char separator);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getRead()
	 */
	@Override
	public boolean getRead();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(final String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getSiblingCount()
	 */
	@Override
	public int getSiblingCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#getUniversalID()
	 */
	@Override
	public String getUniversalID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isCategory()
	 */
	@Override
	public boolean isCategory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isConflict()
	 */
	@Override
	public boolean isConflict();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isDocument()
	 */
	@Override
	public boolean isDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isTotal()
	 */
	@Override
	public boolean isTotal();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#isValid()
	 */
	@Override
	public boolean isValid();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.ViewEntry#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(final boolean flag);
}
