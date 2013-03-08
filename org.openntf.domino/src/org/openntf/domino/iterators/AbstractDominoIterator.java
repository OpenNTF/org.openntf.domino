/*
 * © Copyright OpenNTF 2013
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
package org.openntf.domino.iterators;

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Session;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

@SuppressWarnings({ "rawtypes" })
public abstract class AbstractDominoIterator<T> implements Iterator<T> {
	private String serverName_;
	private String filePath_;
	private Base collection_;
	private transient Session session_;

	protected AbstractDominoIterator(Base collection) {
		setCollection(collection);
	}

	protected Session getSession() {
		Session result = null;
		if (session_ == null) {
			try {
				result = Factory.getSession();
			} catch (Throwable e) {
				DominoUtils.handleException(e);
				return null;
			}
		}
		return result;
	}

	protected Database getDatabase() {
		Database result = null;
		Session session = getSession();
		try {
			result = session.getDatabase(getServerName(), getFilePath());
		} catch (Throwable e) {
			DominoUtils.handleException(e);
			return null;
		}
		return result;
	}

	protected String getFilePath() {
		return filePath_;
	}

	protected String getServerName() {
		return serverName_;
	}

	protected void setDatabase(Database database) {
		if (database != null) {
			try {
				setFilePath(database.getFilePath());
				setServerName(database.getServer());
			} catch (Throwable e) {
				DominoUtils.handleException(e);
			}
		}
	}

	protected void setFilePath(String filePath) {
		filePath_ = filePath;
	}

	protected void setServerName(String serverName) {
		serverName_ = serverName;
	}

	public Base getCollection() {
		return collection_;
	}

	public void setCollection(Base collection) {
		if (collection != null) {
			Database database = null;
			if (collection instanceof DocumentCollection) {
				lotus.domino.Database parent = ((DocumentCollection) collection).getParent();
				database = Factory.fromLotus(parent, Database.class);
			} else if (collection instanceof ViewEntryCollection) {
				lotus.domino.View vw = ((ViewEntryCollection) collection).getParent();
				try {
					lotus.domino.Database parent = vw.getParent();
					database = Factory.fromLotus(parent, Database.class);
				} catch (NotesException e) {
					DominoUtils.handleException(e);
				} finally {
					DominoUtils.incinerate(vw);
				}
			}
			if (database != null) {
				setDatabase(database);
			}
		}
		collection_ = collection;
	}
}
