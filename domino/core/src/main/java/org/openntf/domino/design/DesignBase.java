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

package org.openntf.domino.design;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.openntf.domino.Database;
import org.openntf.domino.design.impl.DesignFactory;

/**
 * @author jgallagher
 * 
 */
public interface DesignBase extends org.openntf.domino.types.Design, org.openntf.domino.types.DatabaseDescendant, Serializable {

	enum ItemFlag {
		_SIGN, _SUMMARY
	}

	enum DxlFormat {
		/** No DXL present (access must be done at doc level */
		NONE,
		/** DXL is a raw note */
		RAWNOTE,
		/** DXL is in DXL-Format */
		DXL
	}

	public static final Set<ItemFlag> FLAG_NONE = EnumSet.noneOf(ItemFlag.class);
	public static final Set<ItemFlag> FLAG_SIGN = EnumSet.of(ItemFlag._SIGN);
	public static final Set<ItemFlag> FLAG_SUMMARY = EnumSet.of(ItemFlag._SUMMARY);
	public static final Set<ItemFlag> FLAG_SIGN_SUMMARY = EnumSet.of(ItemFlag._SIGN, ItemFlag._SUMMARY);

	/**
	 * @return whether hidden from web
	 */
	public boolean isHideFromWeb();

	/**
	 * @return whether hidden from notes
	 */
	public boolean isHideFromNotes();

	/**
	 * @return whether refresh flag is set
	 */
	public boolean isNeedsRefresh();

	/**
	 * @return whether prohibit design refresh is set
	 */
	public boolean isPreventChanges();

	/**
	 * @return whether the design element propagates its prevent-changes settings
	 */
	public boolean isPropagatePreventChanges();

	/**
	 * Reattach the design element to a new Database, either due to deserialization or to copy to a new DB
	 * 
	 * @param database
	 *            the Database object to attach the design element to
	 */
	public void reattach(Database database);

	public void setHideFromWeb(boolean hideFromWeb);

	public void setHideFromNotes(boolean hideFromNotes);

	public void setNeedsRefresh(boolean needsRefresh);

	public void setPreventChanges(boolean preventChanges);

	public void setPropagatePreventChanges(boolean propagatePreventChanges);

	/**
	 * Save any changes to the design element (may change the Note ID)
	 */
	public boolean save(DxlConverter dxlConverter);

	/**
	 * Exports the design to file by using the dxlConverter
	 * 
	 * @param dxlConverter
	 *            the DxlConverter that converts the file data in a "friendly" format. (e.g. gitFriendly)
	 * @param file
	 *            the file
	 * @throws IOException
	 *             if an IO-Error occurs
	 */
	public void exportDesign(DxlConverter dxlConverter, File file) throws IOException;

	/**
	 * Exports the design to file by using the dxlConverter
	 * 
	 * @param dxlConverter
	 *            the DxlConverter that converts the file data in a "friendly" format. (e.g. gitFriendly)
	 * @param outputStream
	 *            the OutputStream
	 * @throws IOException
	 *             if an IO-Error occurs
	 */
	public void exportDesign(DxlConverter dxlConverter, OutputStream outputStream) throws IOException;

	/**
	 * Imports the design from file by using the dxlConverter
	 * 
	 * @param dxlConverter
	 *            the DxlConverter that converts the file data back.
	 * @param file
	 *            the file
	 * @throws IOException
	 *             if an IO-Error occurs
	 */
	public void importDesign(DxlConverter dxlConverter, File file) throws IOException;

	/**
	 * Imports the design from file by using the dxlConverter
	 * 
	 * @param dxlConverter
	 *            the DxlConverter that converts the file data back.
	 * @param inputStream
	 *            the inputStream
	 * @throws IOException
	 *             if an IO-Error occurs
	 */
	public void importDesign(DxlConverter dxlConverter, InputStream inputStream) throws IOException;

	/**
	 * Gets the note id.
	 * 
	 * @return the note id
	 */
	@Override
	public String getNoteID();

	/**
	 * Gets the universal id.
	 * 
	 * @return the universal id
	 */
	@Override
	public String getUniversalID();

	/**
	 * Sets the universal id.
	 * 
	 * @param unid
	 *            the universal id
	 */
	public void setUniversalID(String unid);

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	@Override
	public org.openntf.domino.Document getDocument();

	/**
	 * Returns the item names
	 * 
	 * @return the item names
	 */
	public Collection<String> getItemNames();

	/**
	 * <code>TRUE</code> if this is a private element. <b>ATTENTION:</b> If the designindex is read with NAPI, you cannot access private
	 * elements.
	 * 
	 * @return true if the element is private
	 */
	public boolean isPrivate();

	/**
	 * <code>TRUE</code> if this is the default element
	 * 
	 * @return true if the element is a default element
	 */
	boolean isDefault();

	/**
	 * Returns the mapping of this DesignBase
	 * 
	 * @return the mapping
	 */
	public DesignFactory getMapping();

	/**
	 * Frees up memory
	 */
	void flush();

}