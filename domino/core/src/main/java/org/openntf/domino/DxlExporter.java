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

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the conversion of Domino data to DXL (Domino XML).
 * <h3>Creation and access</h3>
 * <p>
 * Use {@link Session#createDxlExporter()} to create a <code>DxlExporter</code> object.
 * </p>
 * <p>
 * Call one of the {@link #exportDxl(lotus.domino.Database) exportDxl} methods to initiate an export.
 * </p>
 */
public interface DxlExporter
		extends Base<lotus.domino.DxlExporter>, lotus.domino.DxlExporter, org.openntf.domino.ext.DxlExporter, SessionDescendant {

	public static class Schema extends FactorySchema<DxlExporter, lotus.domino.DxlExporter, Session> {
		@Override
		public Class<DxlExporter> typeClass() {
			return DxlExporter.class;
		}

		@Override
		public Class<lotus.domino.DxlExporter> delegateClass() {
			return lotus.domino.DxlExporter.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to Rich Text options.
	 *
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum RichTextOption {
		DXL(DxlExporter.DXLRICHTEXTOPTION_DXL), RAW(DxlExporter.DXLRICHTEXTOPTION_RAW);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 *
		 * @param value
		 *            the value
		 */
		private RichTextOption(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}

		public static RichTextOption valueOf(final int value) {
			for (RichTextOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Enum to allow easy access to MIME options
	 *
	 * @since org.openntf.domino 4.5.0
	 */
	public static enum MIMEOption {
		DXL(DxlExporter.DXLMIMEOPTION_DXL), RAW(DxlExporter.DXLMIMEOPTION_RAW);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new dB option.
		 *
		 * @param value
		 *            the value
		 */
		private MIMEOption(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}

		public static MIMEOption valueOf(final int value) {
			for (MIMEOption opt : values()) {
				if (opt.getValue() == value) {
					return opt;
				}
			}
			return null;
		}
	}

	/**
	 * Converts Domino data to DXL data.
	 *
	 * @param database
	 *            The Domino data to be converted, in this case the entire database.
	 *
	 * @return The exported DXL.
	 */
	@Override
	public String exportDxl(final lotus.domino.Database database);

	/**
	 * Converts Domino data to DXL data.
	 *
	 * @param doc
	 *            The Domino data to be converted, in this case one document.
	 *
	 * @return The exported DXL.
	 */
	@Override
	public String exportDxl(final lotus.domino.Document doc);

	/**
	 * Converts Domino data to DXL data.
	 *
	 * @param docs
	 *            The Domino data to be converted, in this case the documents in a document collection.
	 *
	 * @return The exported DXL.
	 */
	@Override
	public String exportDxl(final lotus.domino.DocumentCollection docs);

	/**
	 * Converts Domino data to DXL data.
	 *
	 * @param notes
	 *            The Domino data to be converted, in this case the elements in a note collection.
	 *
	 * @return The exported DXL.
	 */
	@Override
	public String exportDxl(final lotus.domino.NoteCollection notes);

	/**
	 * Text to insert within richtext precisely where an &ltattachmentref&gt was omitted, such as "[Attachment omitted]". This text may
	 * contain XML markup but must be valid DXL richtext content, for example, "&ltrun&gt&ltfont style='bold'/&gt[Attachment
	 * omitted]&lt/run&gt".
	 *
	 * @return Text to insert within richtext where an &ltattachmentref&gt was omitted
	 */
	@Override
	public String getAttachmentOmittedText();

	/**
	 * Indicates whether bit maps pasted in rich text items should be converted to GIF format.
	 * <p>
	 * Converted bit maps result in the following DXL:
	 * </p>
	 *
	 * <pre>
	 * &lt;gif orignalformat='notesbitmap'&gt;gif representation&lt;/gif&gt;
	 * </pre>
	 * <p>
	 * While non-converted bit maps are:
	 * </p>
	 *
	 * <pre>
	 * &lt;notesbitmap&gt;notes representation&lt;/notesbitmap&gt;
	 * </pre>
	 * <p>
	 * On import, GIF files are converted back to Notes format if the "originalformat" attribute is present.
	 * </p>
	 *
	 * @return true if bit maps pasted in rich text items should be converted to GIF format.
	 *
	 */
	@Override
	public boolean getConvertNotesBitmapsToGIF();

	/**
	 * The value of SYSTEM in the exported &excl;DOCTYPE statement.
	 * <p>
	 * {@link #setOutputDOCTYPE(boolean)} must be true (the default) to export a DOCTYPE statement.
	 * </p>
	 * <p>
	 * This property defaults to SYSTEM 'domino.dtd' so that, for example, the DOCTYPE statement for a database appears as follows:
	 * </p>
	 *
	 * <pre>
	 * &lt;!DOCTYPE database SYSTEM 'domino.dtd'&gt;
	 * </pre>
	 * <p>
	 * Specifying this property as an empty string suppresses the SYSTEM clause.
	 * </p>
	 *
	 * @return The value of SYSTEM in the exported !DOCTYPE statement.
	 *
	 */
	@Override
	public String getDoctypeSYSTEM();

	/**
	 * Indicates that processing should terminate if any fatal error is encountered.
	 *
	 * @return true if processing should terminate if any fatal error is encountered.
	 */
	@Override
	public boolean getExitOnFirstFatalError();

	/**
	 * Indicates whether to write the output DXL as note elements (in the Domino DTD) rather than formatted elements such as document, form,
	 * and view.
	 *
	 * @return true if the output DXL will write note elements
	 */
	@Override
	public boolean getForceNoteFormat();

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
	 * Specifies the format of MIME items or documents that occur in the exported DXL.
	 * <p>
	 * Legal values
	 * </p>
	 * <ul>
	 * <li>MIMEOPTION_DXL (0) A document in MIME format is output in DXL primarily as a &lt;mime&gt element, although the document may
	 * contain additional &lt;item&gt elements as well. This is currently only recommended for use when exporting mail databases. Any
	 * document containing MIME-related items is interpreted as a MIME mail message, and most of the items are processed and converted into
	 * a single stream of MIME data, much as the Domino router would do to send to another mail server in MIME format. This data becomes the
	 * content of the &lt;mime&gt element.<br/>
	 * Note: Some of the MIME parts within this content might be content-transfer-encoded so as to not conflict with the character encoding
	 * of the DXL document. Because MIME data often contains HTML, which includes special characters such as '&lt;', the Exporter will
	 * ususally place the content of the &lt;mime&gt element within an XML CDATA section so that no escaping of the data is necessary.</li>
	 * <li>MIMEOPTION_RAW (1) (default) Any item whose data type is a MIME data type is output as an &lt;item&gt which has a
	 * &lt;rawitemdata&gt element containing the raw data for the item, encoded in Base64.<br/>
	 * Note: This was the only DXL format for MIME data prior to Release 8, and has been retained as the default for backward compatibility
	 * with earlier applications.</li>
	 * </ul>
	 */
	@Override
	public int getMIMEOption();

	/**
	 * Text to insert within richtext precisely where an &lt;objectref&gt was omitted, such as "[OLE object omitted]". This text may contain
	 * XML markup but must be valid DXL richtext content, for example, "&lt;run&gt&lt;font style='bold'/&gt[OLE object omitted]&lt;/run&gt".
	 */
	@Override
	public String getOLEObjectOmittedText();

	/**
	 * A list of item names to be omitted from documents in the exported DXL. The DXL will not contain any &lt;item&gt; elements with any of
	 * those names.
	 */
	@Override
	public Vector<String> getOmitItemNames();

	/**
	 * Specifies whether to omit miscellaneous file objects that occur within documents.
	 */
	@Override
	public boolean getOmitMiscFileObjects();

	/**
	 * Specifies whether to omit OLE objects that occur within documents.
	 * <p>
	 * Omitting an OLE object from a document affects both the &lt;objectref&gt; within &lt;richtext&gt; that refers to the OLE object, and
	 * the corresponding set of &lt;item&gt;s with name='$FILE' that contains the OLE object's data. Use
	 * {@link #setOLEObjectOmittedText(String)} to specify replacement text (if desired) for where the &lt;objectref&gt; is omitted.
	 * </p>
	 *
	 * @return true if OLE objects that occur within documents will be omitted from the DXL
	 *
	 */
	@Override
	public boolean getOmitOLEObjects();

	/**
	 * Specifies whether to omit richtext attachments that occur within documents.
	 * <p>
	 * Omitting an attachment from a document affects both the &lt;attachmentref&gt; within &lt;richtext&gt; that refers to the attachment's
	 * file object, and the corresponding &lt;item&gt; with name='$FILE' that contains the file object data. Use
	 * {@link #setAttachmentOmittedText(String)} to specify replacement text (if desired) for where the &lt;attachmentref&gt; is omitted.
	 * </p>
	 *
	 * @return true if richtext attachments that occur within documents will be omitted from the DXL.
	 *
	 */
	@Override
	public boolean getOmitRichtextAttachments();

	/**
	 * Specifies whether to omit richtext pictures or graphics that occur within documents.
	 * <p>
	 * Pictures that occur within &lt;imagemap&gt; are not omitted.
	 * </p>
	 * <p>
	 * Pictures that occur within &lt;attachmentref&gt; are not omitted unless attachments are omitted, using the
	 * {@link #setOmitRichtextAttachments(boolean)} property. Use the {@link #setPictureOmittedText(String)} property to specify replacement
	 * text (if desired) for where the &lt;picture&gt; is omitted.
	 * </p>
	 *
	 * @return True if richtext pictures or graphics that occur within documents will be omitted from the DXL.
	 *
	 */
	@Override
	public boolean getOmitRichtextPictures();

	/**
	 * Indicates whether a &excl;DOCTYPE statement is exported.
	 * <p>
	 * The !DOCTYPE statement depends on the output and is typically one of the following:
	 * </p>
	 * <p>
	 * You can change or suppress the SYSTEM clause with {@link #setDoctypeSYSTEM(String)}.
	 * </p>
	 *
	 * @return true if a !DOCTYPE statement is exported.
	 *
	 */
	@Override
	public boolean getOutputDOCTYPE();

	/**
	 * Text to insert within richtext precisely where a &lt;picture&gt; was omitted, such as "[Graphic omitted]". This text may contain XML
	 * markup but must be valid DXL richtext content, for example, "&lt;run&gt;&lt;font style='bold'/&gt;[Picture omitted]&lt;/run&gt;".
	 *
	 */
	@Override
	public String getPictureOmittedText();

	/**
	 * Specifies a list of item names to be included in documents in the exported DXL. The DXL will only contain &lt;item&gt; elements with
	 * one of the names on the list.
	 *
	 */
	@Override
	public Vector<String> getRestrictToItemNames();

	/**
	 * Specifies the format of richtext items that occur in the exported DXL.
	 * <p>
	 * If you use RICHTEXTOPTION_RAW, the rich text item will be the same as the original on the binary level, but if it's a design note, it
	 * might not be functionally identical unless you also use "note format" for the note generally using the
	 * {@link #setForceNoteFormat(boolean)} property.
	 * </p>
	 * <p>
	 * For example, if a form uses non-standard fonts, there's a font table stored in a separate item. The $Body rich text item, containing
	 * the bulk of the form data, doesn't refer to fonts by name, but by a number giving the position of the font in this table. If you
	 * reproduce the rich text item exactly, but not the font table, then your rich text refers to font table entries that don't exist, and
	 * it will display the wrong fonts.
	 * </p>
	 * <p>
	 * Legal values:
	 * </p>
	 * <ul>
	 * <li>RICHTEXTOPTION_DXL (0) (default) Richtext is output in its normal DXL format, as a &lt;richtext&gt; element.</li>
	 * <li>RICHTEXTOPTION_RAW (1) Richtext is output as a &lt;rawitemdata&gt; element containing the raw data for the richtext item, encoded
	 * in Base64. This option can be useful if the only purpose of the DXL will be to import it into another database, because it assures
	 * that the imported data will be precisely the same as the original.</li>
	 * </ul>
	 *
	 * @return The format of richtext items that occur in the exported DXL.
	 *
	 */
	@Override
	public int getRichTextOption();

	/**
	 * Specifies whether to uncompress attachments that occur anywhere within the exported DXL.
	 */
	@Override
	public boolean getUncompressAttachments();

	/**
	 * Sets the text to insert within richtext precisely where an &ltattachmentref&gt was omitted, such as "[Attachment omitted]". This text
	 * may contain XML markup but must be valid DXL richtext content, for example, "&ltrun&gt&ltfont style='bold'/&gt[Attachment
	 * omitted]&lt/run&gt".
	 *
	 * @param replacementText
	 *            Text to insert within richtext where an &ltattachmentref&gt was omitted
	 */
	@Override
	public void setAttachmentOmittedText(final String replacementText);

	/**
	 * Sets whether bit maps pasted in rich text items should be converted to GIF format.
	 * <p>
	 * Converted bit maps result in the following DXL:
	 * </p>
	 *
	 * <pre>
	 * &lt;gif orignalformat='notesbitmap'&gt;gif representation&lt;/gif&gt;
	 * </pre>
	 * <p>
	 * While non-converted bit maps are:
	 * </p>
	 *
	 * <pre>
	 * &lt;notesbitmap&gt;notes representation&lt;/notesbitmap&gt;
	 * </pre>
	 * <p>
	 * On import, GIF files are converted back to Notes format if the "originalformat" attribute is present.
	 * </p>
	 *
	 * @param flag
	 *            true if bit maps pasted in rich text items should be converted to GIF format.
	 *
	 */
	@Override
	public void setConvertNotesBitmapsToGIF(final boolean flag);

	/**
	 * Sets the value of SYSTEM in the exported !DOCTYPE statement.
	 * <p>
	 * {@link #setOutputDOCTYPE(boolean)} must be true (the default) to export a DOCTYPE statement.
	 * </p>
	 * <p>
	 * This property defaults to SYSTEM 'domino.dtd' so that, for example, the DOCTYPE statement for a database appears as follows:
	 * </p>
	 *
	 * <pre>
	 * &lt;!DOCTYPE database SYSTEM 'domino.dtd'&gt;
	 * </pre>
	 * <p>
	 * Specifying this property as an empty string suppresses the SYSTEM clause.
	 * </p>
	 *
	 * @param system
	 *            The value of SYSTEM in the exported !DOCTYPE statement.
	 *
	 */
	@Override
	public void setDoctypeSYSTEM(final String system);

	/**
	 * Sets whether processing should terminate if any fatal error is encountered.
	 *
	 * @param true
	 *            if processing should terminate if any fatal error is encountered.
	 */
	@Override
	public void setExitOnFirstFatalError(final boolean flag);

	/**
	 * Sets whether to write the output DXL as note elements (in the Domino DTD) rather than formatted elements such as document, form, and
	 * view.
	 *
	 * @param true
	 *            if the document, form etc. should be written as note elements
	 */
	@Override
	public void setForceNoteFormat(final boolean flag);

	/**
	 * Sets the comment added to the beginning of the log.
	 *
	 * @param comment
	 *            the comment
	 */
	@Override
	public void setLogComment(final String comment);

	/**
	 * Specifies the format of MIME items or documents that occur in the exported DXL.
	 *
	 * @param option
	 *            One of
	 *            <ul>
	 *            <li>MIMEOPTION_DXL (0) A document in MIME format is output in DXL primarily as a &lt;mime&gt element, although the
	 *            document may contain additional &lt;item&gt elements as well. This is currently only recommended for use when exporting
	 *            mail databases. Any document containing MIME-related items is interpreted as a MIME mail message, and most of the items
	 *            are processed and converted into a single stream of MIME data, much as the Domino router would do to send to another mail
	 *            server in MIME format. This data becomes the content of the &lt;mime&gt element.<br/>
	 *            Note: Some of the MIME parts within this content might be content-transfer-encoded so as to not conflict with the
	 *            character encoding of the DXL document. Because MIME data often contains HTML, which includes special characters such as
	 *            '&lt;', the Exporter will ususally place the content of the &lt;mime&gt element within an XML CDATA section so that no
	 *            escaping of the data is necessary.</li>
	 *            <li>MIMEOPTION_RAW (1) (default) Any item whose data type is a MIME data type is output as an &lt;item&gt which has a
	 *            &lt;rawitemdata&gt element containing the raw data for the item, encoded in Base64.<br/>
	 *            Note: This was the only DXL format for MIME data prior to Release 8, and has been retained as the default for backward
	 *            compatibility with earlier applications.</li>
	 *            </ul>
	 * @deprecated Use the {@link org.openntf.domino.ext.DxlExporter#setMIMEOption(MIMEOption)} method
	 */
	@Override
	@Deprecated
	public void setMIMEOption(final int option);

	/**
	 * Text to insert within richtext precisely where an &lt;objectref&gt was omitted, such as "[OLE object omitted]". This text may contain
	 * XML markup but must be valid DXL richtext content, for example, "&lt;run&gt&lt;font style='bold'/&gt[OLE object omitted]&lt;/run&gt".
	 *
	 * @param replacementText
	 *            Text to insert within richtext precisely where an &lt;objectref&gt was omitted
	 */
	@Override
	public void setOLEObjectOmittedText(final String replacementText);

	/**
	 * A list of item names to be omitted from documents in the exported DXL. The DXL will not contain any &lt;item&gt; elements with any of
	 * those names.
	 *
	 * @param names
	 *            item names to be omitted
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setOmitItemNames(final Vector names);

	/**
	 * Sets whether to omit miscellaneous file objects that occur within documents.
	 *
	 * @param flag
	 *            true to omit miscellaneous file objects
	 */
	@Override
	public void setOmitMiscFileObjects(final boolean flag);

	/**
	 * Sets whether to omit OLE objects that occur within documents.
	 * <p>
	 * Omitting an OLE object from a document affects both the &lt;objectref&gt; within &lt;richtext&gt; that refers to the OLE object, and
	 * the corresponding set of &lt;item&gt;s with name='$FILE' that contains the OLE object's data. Use
	 * {@link #setOLEObjectOmittedText(String)} to specify replacement text (if desired) for where the &lt;objectref&gt; is omitted.
	 * </p>
	 *
	 * @param flag
	 *            specify true if OLE objects that occur within documents will be omitted from the DXL
	 *
	 */
	@Override
	public void setOmitOLEObjects(final boolean flag);

	/**
	 * Sets whether to omit richtext attachments that occur within documents.
	 * <p>
	 * Omitting an attachment from a document affects both the &lt;attachmentref&gt; within &lt;richtext&gt; that refers to the attachment's
	 * file object, and the corresponding &lt;item&gt; with name='$FILE' that contains the file object data. Use
	 * {@link #setAttachmentOmittedText(String)} to specify replacement text (if desired) for where the &lt;attachmentref&gt; is omitted.
	 * </p>
	 *
	 * @param flag
	 *            specify true to omit richtext attachments that occur within documents
	 */
	@Override
	public void setOmitRichtextAttachments(final boolean flag);

	/**
	 * Sets whether to omit richtext pictures or graphics that occur within documents.
	 * <p>
	 * Pictures that occur within &lt;imagemap&gt; are not omitted.
	 * </p>
	 * <p>
	 * Pictures that occur within &lt;attachmentref&gt; are not omitted unless attachments are omitted, using the
	 * {@link #setOmitRichtextAttachments(boolean)} property. Use the {@link #setPictureOmittedText(String)} property to specify replacement
	 * text (if desired) for where the &lt;picture&gt; is omitted.
	 * </p>
	 *
	 *
	 */
	@Override
	public void setOmitRichtextPictures(final boolean flag);

	/**
	 * Sets whether a &excl;DOCTYPE statement is exported.
	 * <p>
	 * The !DOCTYPE statement depends on the output and is typically one of the following:
	 * </p>
	 *
	 * <pre>
	 * <code>&lt;!DOCTYPE database SYSTEM 'domino.dtd'&gt;
	&lt;!DOCTYPE document SYSTEM 'domino.dtd'&gt;</code>
	 * </pre>
	 * <p>
	 * You can change or suppress the SYSTEM clause with
	 * <a class="xref" href="H_DOCTYPESYSTEM_PROPERTY_EXPORTER_JAVA.html" title="Read-write. The value of SYSTEM in the exported !DOCTYPE
	 * statement.">DoctypeSYSTEM</a>.
	 * </p>
	 *
	 * @param flag
	 *            specify true to include the !DOCTYPE statement
	 */
	@Override
	public void setOutputDOCTYPE(final boolean flag);

	/**
	 * Sets the text to insert within richtext precisely where a &lt;picture&gt; was omitted, such as "[Graphic omitted]". This text may
	 * contain XML markup but must be valid DXL richtext content, for example, "&lt;run&gt;&lt;font style='bold'/&gt;[Picture
	 * omitted]&lt;/run&gt;".
	 *
	 * @param replacementText
	 *            the text to use instead of a picture
	 */
	@Override
	public void setPictureOmittedText(final String replacementText);

	/**
	 * Specifies a list of item names to be included in documents in the exported DXL. The DXL will only contain &lt;item&gt; elements with
	 * one of the names on the list.
	 *
	 * @param names
	 *            the names of items to be included
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setRestrictToItemNames(final Vector names);

	/**
	 * Sets the format of richtext items that occur in the exported DXL.
	 * <p>
	 * If you use RICHTEXTOPTION_RAW, the rich text item will be the same as the original on the binary level, but if it's a design note, it
	 * might not be functionally identical unless you also use "note format" for the note generally using the
	 * {@link #setForceNoteFormat(boolean)} property.
	 * </p>
	 * <p>
	 * For example, if a form uses non-standard fonts, there's a font table stored in a separate item. The $Body rich text item, containing
	 * the bulk of the form data, doesn't refer to fonts by name, but by a number giving the position of the font in this table. If you
	 * reproduce the rich text item exactly, but not the font table, then your rich text refers to font table entries that don't exist, and
	 * it will display the wrong fonts.
	 * </p>
	 *
	 * @param option
	 *            The format of richtext items that occur in the exported DXL. One of
	 *            <ul>
	 *            <li>RICHTEXTOPTION_DXL (0) (default) Richtext is output in its normal DXL format, as a &lt;richtext&gt; element.</li>
	 *            <li>RICHTEXTOPTION_RAW (1) Richtext is output as a &lt;rawitemdata&gt; element containing the raw data for the richtext
	 *            item, encoded in Base64. This option can be useful if the only purpose of the DXL will be to import it into another
	 *            database, because it assures that the imported data will be precisely the same as the original.</li>
	 *            </ul>
	 * @deprecated Use {@link org.openntf.domino.ext.DxlExporter#setRichTextOption(RichTextOption)} instead
	 */
	@Override
	@Deprecated
	public void setRichTextOption(final int option);

	/**
	 * Sets whether to uncompress attachments that occur anywhere within the exported DXL.
	 *
	 * @param flag
	 *            specify true to uncompress attachments
	 */
	@Override
	public void setUncompressAttachments(final boolean flag);

}
