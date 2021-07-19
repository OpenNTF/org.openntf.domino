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
package org.openntf.domino;

import org.openntf.domino.types.Encapsulated;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DateRange.
 */
public interface DateRange extends Base<lotus.domino.DateRange>, lotus.domino.DateRange, org.openntf.domino.ext.DateRange, Encapsulated,
		SessionDescendant {

	public static class Schema extends FactorySchema<DateRange, lotus.domino.DateRange, Session> {
		@Override
		public Class<DateRange> typeClass() {
			return DateRange.class;
		}

		@Override
		public Class<lotus.domino.DateRange> delegateClass() {
			return lotus.domino.DateRange.class;
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
	 * @see lotus.domino.DateRange#getEndDateTime()
	 */
	@Override
	public DateTime getEndDateTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#getStartDateTime()
	 */
	@Override
	public DateTime getStartDateTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#getText()
	 */
	@Override
	public String getText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#setEndDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setEndDateTime(final lotus.domino.DateTime end);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#setStartDateTime(lotus.domino.DateTime)
	 */
	@Override
	public void setStartDateTime(final lotus.domino.DateTime start);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DateRange#setText(java.lang.String)
	 */
	@Override
	public void setText(final String text);

}
