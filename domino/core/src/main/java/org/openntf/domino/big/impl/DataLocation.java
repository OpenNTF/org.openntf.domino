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
package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.openntf.domino.big.NoteCoordinate;

public class DataLocation implements org.openntf.domino.big.DataLocation {
	protected org.openntf.domino.big.NoteCoordinate nc_;
	protected String itemName_;
	protected String attachmentName_;
	protected int securityType_;
	protected int begins_;
	protected int ends_;

	public DataLocation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void readExternal(final ObjectInput input) throws IOException, ClassNotFoundException {
		nc_ = (NoteCoordinate) input.readObject();
		securityType_ = input.readInt();
		begins_ = input.readInt();
		ends_ = input.readInt();
		itemName_ = input.readUTF();
		attachmentName_ = input.readUTF();
	}

	@Override
	public void writeExternal(final ObjectOutput output) throws IOException {
		output.writeObject(nc_);
		output.writeInt(securityType_);
		output.writeInt(begins_);
		output.writeInt(ends_);
		output.writeUTF(itemName_);
		output.writeUTF(attachmentName_);
	}

	@Override
	public NoteCoordinate getNoteCoordinate() {
		return nc_;
	}

	@Override
	public void setNoteCoordinate(final org.openntf.domino.big.NoteCoordinate coordinate) {
		nc_ = coordinate;
	}

	@Override
	public String getItemName() {
		return itemName_;
	}

	@Override
	public void setItemName(final String itemname) {
		itemName_ = itemname;
	}

	@Override
	public String getAttachmentName() {
		return attachmentName_;
	}

	@Override
	public void setAttachmentName(final String attachmentName) {
		attachmentName_ = attachmentName;
	}

	@Override
	public boolean isSecure() {
		return securityType_ == 0 ? false : true;
	}

	@Override
	public int getBegins() {
		return begins_;
	}

	@Override
	public void setBegins(final int begins) {
		begins_ = begins;
	}

	@Override
	public int getEnds() {
		return ends_;
	}

	@Override
	public void setEnds(final int ends) {
		ends_ = ends;
	}

	@Override
	public int getSecurityType() {
		return securityType_;
	}

	@Override
	public void setSecurityType(final int securityType) {
		securityType_ = securityType;
	}

}
