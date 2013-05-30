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

import java.util.Vector;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface NotesProperty.
 */
public interface NotesProperty extends Base<lotus.domino.NotesProperty>, lotus.domino.NotesProperty, org.openntf.domino.ext.NotesProperty,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#clear()
	 */
	@Override
	public void clear();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getDescription()
	 */
	@Override
	public String getDescription();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getNamespace()
	 */
	@Override
	public String getNamespace();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getTitle()
	 */
	@Override
	public String getTitle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getTypeName()
	 */
	@Override
	public String getTypeName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getValues()
	 */
	@Override
	public Vector<Object> getValues();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#getValueString()
	 */
	@Override
	public String getValueString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#isInput()
	 */
	@Override
	public boolean isInput();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.NotesProperty#publish()
	 */
	@Override
	public void publish();

}
