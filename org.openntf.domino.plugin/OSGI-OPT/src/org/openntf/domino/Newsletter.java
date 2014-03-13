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

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface Newsletter.
 */
public interface Newsletter extends Base<lotus.domino.Newsletter>, lotus.domino.Newsletter, org.openntf.domino.ext.Newsletter,
		SessionDescendant {

	public static class Schema extends FactorySchema<Newsletter, lotus.domino.Newsletter, Session> {
		@Override
		public Class<Newsletter> typeClass() {
			return Newsletter.class;
		}

		@Override
		public Class<lotus.domino.Newsletter> delegateClass() {
			return lotus.domino.Newsletter.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#formatDocument(lotus.domino.Database, int)
	 */
	@Override
	public lotus.domino.Document formatDocument(final lotus.domino.Database database, final int index);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#formatMsgWithDoclinks(lotus.domino.Database)
	 */
	@Override
	public lotus.domino.Document formatMsgWithDoclinks(final lotus.domino.Database database);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#getSubjectItemName()
	 */
	@Override
	public String getSubjectItemName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#isDoScore()
	 */
	@Override
	public boolean isDoScore();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#isDoSubject()
	 */
	@Override
	public boolean isDoSubject();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#setDoScore(boolean)
	 */
	@Override
	public void setDoScore(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#setDoSubject(boolean)
	 */
	@Override
	public void setDoSubject(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Newsletter#setSubjectItemName(java.lang.String)
	 */
	@Override
	public void setSubjectItemName(final String name);

}
