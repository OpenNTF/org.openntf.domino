/*
 * Copyright OpenNTF 2013
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

import lotus.domino.NotesException;

/**
 * The Interface Base.
 * 
 * @param <D>
 *            the generic type
 */
public interface Base<D extends lotus.domino.Base> extends lotus.domino.Base, org.openntf.domino.ext.Base {

	// public abstract D getDelegate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Override
	@Deprecated
	public void recycle() throws NotesException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle(java.util.Vector)
	 */
	@Override
	@Deprecated
	public void recycle(Vector arg0) throws NotesException;
}
