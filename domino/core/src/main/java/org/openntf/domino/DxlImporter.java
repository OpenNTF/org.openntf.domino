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
 * Represents the conversion of DXL (Domino XML) to Domino data.
 * <h3>Creation and access</h3>
 * <p>
 * Use {@link Session#createDxlImporter()} to create a <code>DxlImporter</code> object.
 * </p>
 *
 * <p>
 * Use {@link org.openntf.domino.ext.DxlImporter#setAclImportOption(AclImportOption)},
 * {@link org.openntf.domino.ext.DxlImporter#setDesignImportOption(DesignImportOption)}, and
 * {@link org.openntf.domino.ext.DxlImporter#setDocumentImportOption(DocumentImportOption)} to indicate whether you want to create
 * additional elements in the output database from the incoming DXL, ignore incoming elements, replace existing elements, or update existing
 * elements.
 * </p>
 *
 * <p>
 * Call {@link #importDxl(String, lotus.domino.Database) importDxl methods} to initiate an import.
 * </p>
 */
public interface DxlImporter
		extends Base<lotus.domino.DxlImporter>, lotus.domino.DxlImporter, org.openntf.domino.ext.DxlImporter, SessionDescendant {

	public static class Schema extends FactorySchema<DxlImporter, lotus.domino.DxlImporter, Session> {
		@Override
		public Class<DxlImporter> typeClass() {
			return DxlImporter.class;
		}

		@Override
		public Class<lotus.domino.DxlImporter> delegateClass() {
			return lotus.domino.DxlImporter.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to Document import options
	 *
	 * @since org.openntf.domino 2.5.0
	 *
	 */
	public static enum DocumentImportOption {
		IGNORE(DxlImporter.DXLIMPORTOPTION_IGNORE), CREATE(DxlImporter.DXLIMPORTOPTION_CREATE),
		REPLACE_ELSE_CREATE(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE),
		REPLACE_ELSE_IGNORE(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE),
		UPDATE_ELSE_CREATE(DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE),
		UPDATE_ELSE_IGNORE(DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE);

		private final int value_;

		private DocumentImportOption(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static DocumentImportOption valueOf(final int value) {
			for (DocumentImportOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Enum to allow easy access to design element import options
	 *
	 * @since org.openntf.domino 2.5.0
	 *
	 */
	public static enum DesignImportOption {
		IGNORE(DxlImporter.DXLIMPORTOPTION_IGNORE), CREATE(DxlImporter.DXLIMPORTOPTION_CREATE),
		REPLACE_ELSE_CREATE(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE),
		REPLACE_ELSE_IGNORE(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE);

		private final int value_;

		private DesignImportOption(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static DesignImportOption valueOf(final int value) {
			for (DesignImportOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Enum to allow easy access to ACL import settings
	 *
	 * @since org.openntf.domino 2.5.0
	 *
	 */
	public static enum AclImportOption {
		IGNORE(DxlImporter.DXLIMPORTOPTION_IGNORE), REPLACE_ELSE_IGNORE(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE),
		UPDATE_ELSE_CREATE(DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE),
		UPDATE_ELSE_IGNORE(DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE);

		private final int value_;

		private AclImportOption(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static AclImportOption valueOf(final int value) {
			for (AclImportOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	public static enum InputValidationOption {
		NEVER(DxlImporter.DXLVALIDATIONOPTION_VALIDATE_NEVER), ALWAYS(DxlImporter.DXLVALIDATIONOPTION_VALIDATE_ALWAYS),
		AUTO(DxlImporter.DXLVALIDATIONOPTION_VALIDATE_AUTO);

		private final int value_;

		private InputValidationOption(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

		public static InputValidationOption valueOf(final int value) {
			for (InputValidationOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Indicates the handling of the incoming ACL entries: ignore, replace, or update.
	 *
	 * @return how to handle incoming ACL entries. One of:
	 *         <ul>
	 *         <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - (Default) Ignores any ACL in the incoming DXL and uses the ACL in the output
	 *         database as is.</li>
	 *         <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces the ACL in the output database with the ACL in the
	 *         incoming DXL.</li>
	 *         <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE (10) - Replaces ACL entries in the output database with ACL entries in the
	 *         incoming DXL that match and adds any new ACL entries from the incoming DXL.</li>
	 *         <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE (9) - Replaces ACL entries in the output database with ACL entries in the
	 *         incoming DXL that match and leaves non-matching ACL entries in the output database.</li>
	 *         </ul>
	 */
	@Override
	public int getAclImportOption();

	/**
	 * Specifies whether the Importer should compile any LotusScript code that occurs within imported notes.
	 * <p>
	 * Consider specifying false for this property in the following situations:
	 * </p>
	 * <ul>
	 * <li>The DXL contains a significant amount of LotusScript code, you wish to speed up the Importer performance, and you are willing to
	 * accept the potential performance impact of compiling on first use.</li>
	 * <li>The DXL contains LotusScript code that references script libraries which are not contained in the DXL or in the import database.
	 * This will avoid generating LotusScript compilation warning messages from the Importer.</li>
	 * </ul>
	 *
	 * @return true when the Importer should compile any LotusScript code that occurs within imported notes.
	 *
	 *
	 */
	@Override
	public boolean getCompileLotusScript();

	/**
	 * Indicates whether the database receiving the imported notes is full-text indexed.
	 *
	 * @return true if the database is full-text indexed
	 */
	@Override
	public boolean getCreateFTIndex();

	/**
	 * Indicates the handling of the incoming design elements: create, ignore, or replace.
	 * <p>
	 * For purposes of replacement, two design elements match if:
	 * </p>
	 * <ul>
	 * <li>The design elements are of the same type (forms, views, folders, and so on).</li>
	 * <li>A name or alias in one design element matches a name or alias in the other design element.</li>
	 * <li>The design elements are of the same language if the design element uses a language.</li>
	 * </ul>
	 * <h5>CAUTION</h5>
	 * <p>
	 * If multiple elements share a name or alias, Replace_Else_Create and Replace_Else_Ignore will have unpredictable results. If there is
	 * at most one matching design element in the output database, whichever incoming element is the last one processed will replace all
	 * previously processed matching elements. If there are multiple matching design elements in the output database, there is no way to
	 * predict which one will be replaced. These options should only be used when there is at most one element with the same name or alias.
	 * </p>
	 * <p>
	 * Returns one of the values:
	 * </p>
	 * <ul>
	 * <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - (Default) Ignores design elements in the incoming DXL and leaves the design elements in
	 * the output database intact.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_CREATE (2) - Creates new design elements from the incoming DXL, leaving existing design elements in
	 * the output database intact.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE (6) - Replaces design elements in the output database with design elements in the
	 * incoming DXL that match. Adds any new design elements from the incoming DXL.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces design elements in the output database with design elements in the
	 * incoming DXL that match. Leaves non-matching design elements in the output database.</li> </ul
	 *
	 * @return Indicates the handling of the incoming design elements: create, ignore, or replace.
	 */
	@Override
	public int getDesignImportOption();

	/**
	 * Indicates the handling of incoming documents: create, ignore, replace, or update.
	 * <p>
	 * For purposes of replacement, two documents match if:
	 * </p>
	 * <ul>
	 * <li>The replica ID of the output database matches the replica ID of the incoming DXL.</li>
	 * <li>The universal IDs of the two documents match.</li>
	 * </ul>
	 * <p>
	 * Returns one of the values:
	 * </p>
	 * <ul>
	 * <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - Ignores documents in the incoming DXL and leaves the documents in the output database
	 * intact.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_CREATE (2) - (Default) Creates new documents from the incoming DXL, leaving existing documents in the
	 * output database intact.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE (6) - Replaces documents in the output database with documents in the incoming
	 * DXL that match. Adds any new documents from the incoming DXL.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces documents in the output database with documents in the incoming
	 * DXL that match. Leaves non-matching documents in the output database.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE (10) - Replaces document items in the output database with document items in the
	 * incoming DXL that match. Adds new documents and document items from the incoming DXL.</li>
	 * <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE (9) - Replaces document items in the output database with document items in the
	 * incoming DXL that match. Leaves non-matching documents and document items in the output database.</li>
	 * </ul>
	 *
	 * @return Indicates the handling of incoming documents: create, ignore, replace, or update.
	 */
	@Override
	public int getDocumentImportOption();

	/**
	 * Indicates that processing should terminate if any fatal error is encountered.
	 *
	 * @return if true then processing will terminate if any fatal error is encountered.
	 */
	@Override
	public boolean getExitOnFirstFatalError();

	/**
	 * Gets the first note in the collection.
	 *
	 * @return The ID of the first note.
	 */
	@Override
	public String getFirstImportedNoteID();

	/**
	 * The number of Domino notes being imported.
	 */
	@Override
	public int getImportedNoteCount();

	/**
	 * Indicates that if a DTD is specified in the XML declaration statement, it should be used to validate the input XML.
	 * <p>
	 * Returns one of the options:
	 * </p>
	 * <ul>
	 * <li>DxlImporter.DXLVALIDATIONOPTION_VALIDATE_NEVER (0) - Do not try to validate.</li>
	 * <li>DxlImporter.DXLVALIDATIONOPTION_ VALIDATE_ALWAYS (1) - Validate the input.</li>
	 * <li>DxlImporter.DXLVALIDATIONOPTION_ VALIDATE_AUTO (2) - (Default) If the DTD is specified, validate the input; otherwise, do not
	 * validate.</li>
	 * </ul>
	 *
	 * @return Indicates that if a DTD is specified in the XML declaration statement, it should be used to validate the input XML.
	 */
	@Override
	public int getInputValidationOption();

	/**
	 * An XML representation of warnings, errors, and fatal errors generated by the processor.
	 *
	 */
	@Override
	public String getLog();

	/**
	 * A comment added to the beginning of the log.
	 */
	@Override
	public String getLogComment();

	/**
	 * Given a note, finds the note immediately following it in a collection.
	 *
	 * @param noteid
	 *            A valid note ID.
	 * @return The ID of the note following the specified note.
	 */
	@Override
	public String getNextImportedNoteID(final String noteid);

	/**
	 * Indicates whether the incoming DXL replaces the database properties.
	 * <p>
	 * This property is useful, for example, when you are using DxlImporter to create a new database and want to import the database title
	 * from the DXL to the new database.
	 * </p>
	 * <p>
	 * See the attributes for database in the DominoDTD for a list of the database properties.
	 * </p>
	 *
	 *
	 * @return true when the incoming DXL replaces the database properties.
	 */
	@Override
	public boolean getReplaceDbProperties();

	/**
	 * Indicates whether the replica ID of the DXL and the target database must match.
	 * <p>
	 * If this property is true and the replica IDs do not match, the import operation throws NOTES_ERR_DXLIMPORTER_FAILED (4522) and adds
	 * the following message to the log: "Could not replace/update a matching NoteType note because the ReplicaRequiredForReplaceUpdate
	 * property is true and the DXL and database are not replicas."
	 * </p>
	 *
	 * @return true when the replica ID of the DXL and the target database must match.
	 *
	 */
	@Override
	public boolean getReplicaRequiredForReplaceOrUpdate();

	/**
	 * Enables you to assign different error messages to different types of errors thrown when unrecognized data is encountered by the
	 * importer.
	 * <p>
	 * Use the {@link #setInputValidationOption(InputValidationOption)} property to turn the validator on or off.
	 * </p>
	 * <p>
	 * The name of the log is IMPORTER.LOG.
	 * </p>
	 *
	 * @return One of the values:
	 *         <ul>
	 *         <li>DxlImporter.DXLLOGOPTION_FATALERROR (4) - (Default) Aborts the import and reports the error to the log.</li>
	 *         <li>DxlImporter.DXLLOGOPTION_ERROR (3) - Reports the error to the log.</li>
	 *         <li>DxlImporter.DXLLOGOPTION_WARNING (2) - Displays a warning that an error has been encountered.</li>
	 *         <li>DxlImporter.DXLLOGOPTION_IGNORE (1) - Does not abort the import, but ignores the error messages. Use this option if
	 *         validation is on.</li>
	 *         </ul>
	 *
	 */
	@Override
	public int getUnknownTokenLogOption();

	/**
	 * Converts DXL data to Domino data.
	 * <p>
	 * Before calling this method, set {@link #setAclImportOption(AclImportOption)}, {@link #setDesignImportOption(DesignImportOption)}, and
	 * {@link #setDocumentImportOption(DocumentImportOption)}.
	 * </p>
	 * <p>
	 * You cannot explicitly read or write a Stream object associated with a file prior to using it for XML input or output. For example, if
	 * you write to a file then use it for XML input, you must close and reopen the Stream object.
	 * </p>
	 *
	 * @param rtitem
	 *            The input DXL.
	 * @param database
	 *            The target database for the DXL.
	 */
	@Override
	public void importDxl(final lotus.domino.RichTextItem rtitem, final lotus.domino.Database database);

	/**
	 * Converts DXL data to Domino data.
	 * <p>
	 * Before calling this method, set {@link #setAclImportOption(AclImportOption)}, {@link #setDesignImportOption(DesignImportOption)}, and
	 * {@link #setDocumentImportOption(DocumentImportOption)}.
	 * </p>
	 * <p>
	 * You cannot explicitly read or write a Stream object associated with a file prior to using it for XML input or output. For example, if
	 * you write to a file then use it for XML input, you must close and reopen the Stream object.
	 * </p>
	 *
	 * @param stream
	 *            The input DXL.
	 * @param database
	 *            The target database for the DXL.
	 */
	@Override
	public void importDxl(final lotus.domino.Stream stream, final lotus.domino.Database database);

	/**
	 * Converts DXL data to Domino data.
	 * <p>
	 * Before calling this method, set {@link #setAclImportOption(AclImportOption)}, {@link #setDesignImportOption(DesignImportOption)}, and
	 * {@link #setDocumentImportOption(DocumentImportOption)}.
	 * </p>
	 * <p>
	 * You cannot explicitly read or write a Stream object associated with a file prior to using it for XML input or output. For example, if
	 * you write to a file then use it for XML input, you must close and reopen the Stream object.
	 * </p>
	 *
	 * @param dxl
	 *            The input DXL.
	 * @param database
	 *            The target database for the DXL.
	 */
	@Override
	public void importDxl(final String dxl, final lotus.domino.Database database);

	/**
	 * Sets the handling of the incoming ACL entries: ignore, replace, or update.
	 *
	 * @param option
	 *            One of the values:
	 *            <ul>
	 *            <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - (Default) Ignores any ACL in the incoming DXL and uses the ACL in the output
	 *            database as is.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces the ACL in the output database with the ACL in the
	 *            incoming DXL.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE (10) - Replaces ACL entries in the output database with ACL entries in
	 *            the incoming DXL that match and adds any new ACL entries from the incoming DXL.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE (9) - Replaces ACL entries in the output database with ACL entries in
	 *            the incoming DXL that match and leaves non-matching ACL entries in the output database.</li>
	 *            </ul>
	 * @deprecated replaced by {@link org.openntf.domino.ext.DxlImporter#setAclImportOption(AclImportOption)}
	 */
	@Override
	@Deprecated
	public void setAclImportOption(final int option);

	/**
	 * Specifies whether the Importer should compile any LotusScript code that occurs within imported notes.
	 * <p>
	 * Consider specifying false for this property in the following situations:
	 * </p>
	 * <ul>
	 * <li>The DXL contains a significant amount of LotusScript code, you wish to speed up the Importer performance, and you are willing to
	 * accept the potential performance impact of compiling on first use.</li>
	 * <li>The DXL contains LotusScript code that references script libraries which are not contained in the DXL or in the import database.
	 * This will avoid generating LotusScript compilation warning messages from the Importer.</li>
	 * </ul>
	 *
	 * @param flag
	 *            true if the Importer should compile LotusScript code
	 */
	@Override
	public void setCompileLotusScript(final boolean flag);

	/**
	 * Indicates whether the database receiving the imported notes is full-text indexed.
	 *
	 * @param flag
	 *            true if the database is full-text indexed
	 */
	@Override
	public void setCreateFTIndex(final boolean flag);

	/**
	 * Indicates the handling of the incoming design elements: create, ignore, or replace.
	 * <p>
	 * For purposes of replacement, two design elements match if:
	 * </p>
	 * <ul>
	 * <li>The design elements are of the same type (forms, views, folders, and so on).</li>
	 * <li>A name or alias in one design element matches a name or alias in the other design element.</li>
	 * <li>The design elements are of the same language if the design element uses a language.</li>
	 * </ul>
	 * <h5>CAUTION:</h5>
	 * <p>
	 * If multiple elements share a name or alias, Replace_Else_Create and Replace_Else_Ignore will have unpredictable results. If there is
	 * at most one matching design element in the output database, whichever incoming element is the last one processed will replace all
	 * previously processed matching elements. If there are multiple matching design elements in the output database, there is no way to
	 * predict which one will be replaced. These options should only be used when there is at most one element with the same name or alias.
	 * </p>
	 *
	 * @param option
	 *            One of the values
	 *            <ul>
	 *            <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - (Default) Ignores design elements in the incoming DXL and leaves the design
	 *            elements in the output database intact.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_CREATE (2) - Creates new design elements from the incoming DXL, leaving existing design
	 *            elements in the output database intact.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE (6) - Replaces design elements in the output database with design
	 *            elements in the incoming DXL that match. Adds any new design elements from the incoming DXL.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces design elements in the output database with design
	 *            elements in the incoming DXL that match. Leaves non-matching design elements in the output database.</li>
	 *            </ul>
	 * @deprecated Use {@link org.openntf.domino.ext.DxlImporter#setDesignImportOption(DesignImportOption)} instead
	 */
	@Override
	@Deprecated
	public void setDesignImportOption(final int option);

	/**
	 * Indicates the handling of incoming documents: create, ignore, replace, or update.
	 * <p>
	 * For purposes of replacement, two documents match if:
	 * </p>
	 * <ul>
	 * <li>The replica ID of the output database matches the replica ID of the incoming DXL.</li>
	 * <li>The universal IDs of the two documents match.</li>
	 * </ul>
	 *
	 * @param option
	 *            One of the values
	 *            <ul>
	 *            <li>DxlImporter.DXLIMPORTOPTION_IGNORE (1) - Ignores documents in the incoming DXL and leaves the documents in the output
	 *            database intact.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_CREATE (2) - (Default) Creates new documents from the incoming DXL, leaving existing
	 *            documents in the output database intact.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_CREATE (6) - Replaces documents in the output database with documents in the
	 *            incoming DXL that match. Adds any new documents from the incoming DXL.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE (5) - Replaces documents in the output database with documents in the
	 *            incoming DXL that match. Leaves non-matching documents in the output database.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_CREATE (10) - Replaces document items in the output database with document
	 *            items in the incoming DXL that match. Adds new documents and document items from the incoming DXL.</li>
	 *            <li>DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE (9) - Replaces document items in the output database with document
	 *            items in the incoming DXL that match. Leaves non-matching documents and document items in the output database.</li>
	 *            </ul>
	 * @deprecated Use {@link org.openntf.domino.ext.DxlImporter#setDocumentImportOption(DocumentImportOption)} instead
	 */
	@Override
	@Deprecated
	public void setDocumentImportOption(final int option);

	/**
	 * Sets whether processing should terminate if any fatal error is encountered.
	 *
	 * @param flag
	 *            true if the processing should terminate if any fatal error is encountered
	 */
	@Override
	public void setExitOnFirstFatalError(final boolean flag);

	/**
	 * Sets that if a DTD is specified in the XML declaration statement, it should be used to validate the input XML.
	 *
	 * @param option
	 *            true if the input XML should be validated against DTD if specified
	 * @deprecated Use {@link org.openntf.domino.ext.DxlImporter#setInputValidationOption(InputValidationOption)} instead
	 */
	@Override
	@Deprecated
	public void setInputValidationOption(final int option);

	/**
	 * Sets a comment added to the beginning of the log.
	 *
	 * @param comment
	 *            Comment to be added
	 */
	@Override
	public void setLogComment(final String comment);

	/**
	 * Sets whether the incoming DXL replaces the database properties.
	 * <p>
	 * This property is useful, for example, when you are using DxlImporter to create a new database and want to import the database title
	 * from the DXL to the new database.
	 * </p>
	 * <p>
	 * See the attributes for database in the DominoDTD for a list of the database properties.
	 * </p>
	 *
	 * @param flag
	 *            true if the incoming DXL replaces the database properties
	 */
	@Override
	public void setReplaceDbProperties(final boolean flag);

	/**
	 * Sets whether the replica ID of the DXL and the target database must match.
	 * <p>
	 * If this property is true and the replica IDs do not match, the import operation throws NOTES_ERR_DXLIMPORTER_FAILED (4522) and adds
	 * the following message to the log: "Could not replace/update a matching NoteType note because the ReplicaRequiredForReplaceUpdate
	 * property is true and the DXL and database are not replicas."
	 * </p>
	 *
	 * @param flag
	 *            true if the replica ID of the DXL and the target database must match
	 */
	@Override
	public void setReplicaRequiredForReplaceOrUpdate(final boolean flag);

	/**
	 * Enables you to assign different error messages to different types of errors thrown when unrecognized data is encountered by the
	 * importer.
	 * <p>
	 * Use the {@link #setInputValidationOption(InputValidationOption)} property to turn the validator on or off.
	 * </p>
	 * <p>
	 * The name of the log is IMPORTER.LOG.
	 * </p>
	 *
	 * @param option
	 *            One of the values
	 *            <ul>
	 *            <li>DxlImporter.DXLLOGOPTION_FATALERROR (4) - (Default) Aborts the import and reports the error to the log.</li>
	 *            <li>DxlImporter.DXLLOGOPTION_ERROR (3) - Reports the error to the log.</li>
	 *            <li>DxlImporter.DXLLOGOPTION_WARNING (2) - Displays a warning that an error has been encountered.</li>
	 *            <li>DxlImporter.DXLLOGOPTION_IGNORE (1) - Does not abort the import, but ignores the error messages. Use this option if
	 *            validation is on.</li>
	 *            </ul>
	 */
	@Override
	public void setUnknownTokenLogOption(final int option);
}
