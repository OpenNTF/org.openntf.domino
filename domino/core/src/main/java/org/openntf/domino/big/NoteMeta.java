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
package org.openntf.domino.big;

import java.util.Date;

import org.openntf.domino.ext.Database;
import org.openntf.domino.ext.Document;
import org.openntf.domino.ext.Session;

public interface NoteMeta extends BaseMeta {
	public java.util.List<ItemMeta> getItemMetaList();

	public ItemMeta getItemMeta(String itemname);

	public String getForm();

	public NoteType getType();

	public String getUnid();

	public String getReplid();

	public int getNoteid();

	public boolean hasLocalItems();

	public Document getDocument(Database db);

	public Document getDocument(Session session, String server);

	public Date getCreated();

	public Date getLastModified();

	public boolean hasReaders();

	public boolean hasAuthors();

	public void setDocument(Document doc);

	public void setUnid(String unid);

	public void setReplid(String replid);

	public void setNoteid(int noteid);

	public void setCreated(Date created);

	public void setLastModified(Date lastModified);

	public void setReaders(boolean readers);

	public void setAuthors(boolean authors);
}
