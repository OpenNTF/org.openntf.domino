/*
 * Copyright 2015
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

package org.openntf.domino.design.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.design.DesignBase;

import com.ibm.commons.util.StringUtil;

/**
 * This is the Root class of all DesignNotes
 * 
 * @author jgallagher, Roland Praml
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDesignBase implements DesignBase {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDesignBase.class.getName());

	private static final char DESIGN_FLAG_PRESERVE = 'P';
	private static final char DESIGN_FLAG_PROPAGATE_NOCHANGE = 'r';
	private static final char DESIGN_FLAG_NEEDSREFRESH = '$';
	private static final char DESIGN_FLAG_HIDE_FROM_WEB = 'w';
	private static final char DESIGN_FLAG_HIDE_FROM_NOTES = 'n';

	/* for web apps, this file is ready for primetime */
	protected static final char DESIGN_FLAGEXT_FILE_DEPLOYABLE = 'D';
	protected static final char DESIGN_FLAG_READONLY = '&';

	protected static final String FLAGS_ITEM = "$Flags";
	protected static final String FLAGS_EXT_ITEM = "$FlagsExt";
	protected static final String DESIGNER_VERSION_ITEM = "$DesignerVersion";
	protected static final String DESIGNER_VERSION_VALUE = "8.5.3";

	protected static final String TITLE_ITEM = "$TITLE";
	protected static final String COMMENT_ITEM = "$Comment";
	protected static final String CLASS_ITEM = "$Class";
	protected static final String ASSIST_TYPE = "$AssistType";

	protected static final String DEFAULT_FILEDATA_FIELD = "$FileData";
	protected static final String DEFAULT_CONFIGDATA_FIELD = "$ConfigData";
	protected static final String DEFAULT_FILESIZE_FIELD = "$FileSize";
	protected static final String DEFAULT_CONFIGSIZE_FIELD = "$ConfigSize";
	protected static final String MIMETYPE_ITEM = "$MimeType";

	/** the universalID of the underlying document */
	private String universalId_;

	// caches
	private transient Database database_;
	protected transient Document document_;
	private transient DesignFactory odpMapping_;

	/**
	 * Create a new DesignBase based on the given database. You may add content to this DesignBase and save it afterwards.
	 * 
	 * @param database
	 *            the Database
	 */
	public void init(final Database database) {
		database_ = database;
	}

	/**
	 * Create a new DesginBase based on the given document. This Method will be invoked by {@link DesignFactory#fromDocument(Document)}
	 * 
	 * @param document
	 */
	protected void init(final Document document) {
		setDocument(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromNotes()
	 */
	@Override
	public boolean isHideFromNotes() {
		return hasFlag(DESIGN_FLAG_HIDE_FROM_NOTES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isHideFromWeb()
	 */
	@Override
	public boolean isHideFromWeb() {
		return hasFlag(DESIGN_FLAG_HIDE_FROM_WEB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isNeedsRefresh()
	 */
	@Override
	public boolean isNeedsRefresh() {
		return hasFlag(DESIGN_FLAG_NEEDSREFRESH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPreventChanges()
	 */
	@Override
	public boolean isPreventChanges() {
		return hasFlag(DESIGN_FLAG_PRESERVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#isPropagatePreventChanges()
	 */
	@Override
	public boolean isPropagatePreventChanges() {
		return hasFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromNotes(boolean)
	 */
	@Override
	public void setHideFromNotes(final boolean hideFromNotes) {
		setFlag(DESIGN_FLAG_HIDE_FROM_NOTES, hideFromNotes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setHideFromWeb(boolean)
	 */
	@Override
	public void setHideFromWeb(final boolean hideFromWeb) {
		setFlag(DESIGN_FLAG_HIDE_FROM_WEB, hideFromWeb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setNeedsRefresh(boolean)
	 */
	@Override
	public void setNeedsRefresh(final boolean needsRefresh) {
		setFlag(DESIGN_FLAG_NEEDSREFRESH, needsRefresh);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPreventChanges(boolean)
	 */
	@Override
	public void setPreventChanges(final boolean preventChanges) {
		setFlag(DESIGN_FLAG_PRESERVE, preventChanges);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setPropagatePreventChanges(boolean)
	 */
	@Override
	public void setPropagatePreventChanges(final boolean propagatePreventChanges) {
		setFlag(DESIGN_FLAG_PROPAGATE_NOCHANGE, propagatePreventChanges);
	}

	// -------------- Flags stuff

	/**
	 * Returns all flags of this DesignElement.
	 * 
	 * @return the flags
	 */
	protected abstract String getFlags();

	/**
	 * Write the flags back to document
	 * 
	 * @param flags
	 *            Flags to write back
	 */
	protected abstract void setFlags(final String flags);

	/**
	 * Sets/clears a given flag character in this DesignElement
	 * 
	 * @param flag
	 *            the flag char
	 * @param enabled
	 *            if the flag should be set or cleared
	 */
	protected void setFlag(final char flag, final boolean enabled) {
		String flags = getFlags();
		if (enabled) {
			if (flags.indexOf(flag) < 0) {
				setFlags(flags + flag);
			}
		} else {
			if (flags.indexOf(flag) >= 0) {
				setFlags(flags.replace(String.valueOf(flag), ""));
			}
		}
	}

	/**
	 * Checks if a given flag is present
	 * 
	 * @param flag
	 *            the flag char
	 * @return true if the flag is present
	 */
	protected boolean hasFlag(final char flag) {
		return getFlags().indexOf(flag) >= 0;
	}

	// same for extended
	/**
	 * Returns all extended-flags of this DesignElement
	 * 
	 * @return the extended flags
	 */
	protected abstract String getFlagsExt();

	/**
	 * Write the flags back to document
	 * 
	 * @param flags
	 *            Extended-Flags to write back
	 */
	protected abstract void setFlagsExt(final String flags);

	/**
	 * Sets/clears a given exteded-flag character in this DesignElement
	 * 
	 * @param flag
	 *            the flag char
	 * @param enabled
	 *            if the flag should be set or cleared
	 */
	protected void setFlagExt(final char flag, final boolean enabled) {
		String flags = getFlagsExt();
		if (enabled) {
			if (flags.indexOf(flag) < 0) {
				setFlagsExt(flags + flag);
			}
		} else {
			if (flags.indexOf(flag) >= 0) {
				setFlagsExt(flags.replace(String.valueOf(flag), ""));
			}
		}
	}

	/**
	 * Checks if a given flag is present
	 * 
	 * @param flag
	 *            the flag char
	 * @return true if the flag is present
	 */
	protected boolean hasFlagExt(final char flag) {
		return getFlagsExt().indexOf(flag) >= 0;
	}

	// Mapping

	/**
	 * Returns the DesignMapping of this Design element
	 * 
	 * @return the mapping
	 */
	@Override
	public final DesignFactory getMapping() {
		if (odpMapping_ == null) {
			odpMapping_ = DesignFactory.valueOf(getClass());
		}
		return odpMapping_;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#reattach(org.openntf.domino.Database)
	 */
	@Override
	public void reattach(final Database database) {
		database_ = database;
		document_ = null;
	}

	/**
	 * Set the document. (Must be done after import)
	 * 
	 * @param document
	 */
	protected final void setDocument(final Document document) {
		database_ = document.getAncestorDatabase();
		universalId_ = document.getUniversalID(); // we must save the UNID. because NoteID may change on various instances
		document_ = document;
		flush();
	}

	/* (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return database_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return getAncestorDatabase().getAncestorSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public final Document getDocument() {
		if (document_ != null && !document_.isDead()) {
			if (document_.getUniversalID().equals(universalId_)) {
				return document_;
			}
		}
		if (database_ == null) {
			return null;
		}
		if (!StringUtil.isEmpty(universalId_)) {
			document_ = database_.getDocumentByUNID(universalId_);
			return document_;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public final String getUniversalID() {
		return universalId_;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openntf.domino.design.DesignBase#setUniversalID(java.lang.String)
	 */
	@Override
	public void setUniversalID(final String unid) {
		universalId_ = unid;
	}

	public static byte[] toBytes(final InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();
		return buffer.toByteArray();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " (UniversalID: " + universalId_ + ")";
	}
}
