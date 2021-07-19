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

import java.io.Externalizable;

public interface DataLocation extends Externalizable {

	public NoteCoordinate getNoteCoordinate();

	public void setNoteCoordinate(NoteCoordinate coordinate);

	public String getItemName();

	public void setItemName(String itemname);

	public String getAttachmentName();

	public void setAttachmentName(String attachmentName);

	public boolean isSecure();

	public int getSecurityType();

	public void setSecurityType(int securityType);

	public int getBegins();

	public void setBegins(int begins);

	public int getEnds();

	public void setEnds(int ends);

}
