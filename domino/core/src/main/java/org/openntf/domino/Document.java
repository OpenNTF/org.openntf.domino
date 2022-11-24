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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import lotus.domino.UserID;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;

/**
 * Document in a Notes application. It contains practical enhancements for daily use, check the {@link org.openntf.domino.ext.Document}
 * interface in the org.openntf.domino.ext package for documentation and examples.
 *
 * <h3>Notable enhancements</h3>
 * <p>
 * <ul>
 * <li>Use {@link org.openntf.domino.ext.Document#appendItemValue(String, Object, boolean)} to add a value to an item only if the value
 * doesn't exist in the item already</li>
 * <li>Use {@link org.openntf.domino.ext.Document#toJson(boolean)} to retrieve a JSON representation of the document</li>
 * <li>Use {@link org.openntf.domino.ext.Document#getItemValue(String, Class)} to retrieve an item's value casted directly to a specific
 * object type</li>
 * <li>Use {@link org.openntf.domino.ext.Document#getItemValues(String, Class)} to retrieve multivalue item as a List of specific object
 * types</li>
 * <li>Use {@link org.openntf.domino.ext.Document#containsValue(Object, String[])} to check if document contains a value in one of the
 * specified items</li>
 * <li>Use {@link org.openntf.domino.ext.Document#getItemTable(CharSequence...)} and
 * {@link org.openntf.domino.ext.Document#getItemTablePivot(CharSequence...)} to conveniently work with multiple items which logically form
 * a table.</li>
 * </ul>
 * </p>
 * <h3>Creation</h3>
 * <p>
 * To create a new Document object, use {@link Database#createDocument()}.
 * </p>
 * <h3>Access</h3>
 * <p>
 * To access existing documents:
 * </p>
 * <ul>
 * <li>To get all the documents in a database, use {@link Database#getAllDocuments()}.</li>
 * <li>To get a document based on its position in a view, use a {@link View} object.</li>
 * <li>To get a document based on its position in a response hierarchy, use a {@link View} object. To get all documents that are responses
 * to a particular document, use {@link Document#getResponses()}. To get a response document's parent document, use
 * {@link Document#getParentDocumentUNID()} followed by {@link Database#getDocumentByUNID(String)}.</li>
 * <li>To get all the documents that match a full-text search query, use {@link Database#FTSearch(String)} or
 * {@link View#FTSearch(String)}.</li>
 * <li>To get all the documents in a database that meet search criteria, where the criteria are defined using the formula language, use
 * {@link Database#search(String)}.</li>
 * <li>To get documents in a database not yet processed by the current agent, use {@link AgentContext#getUnprocessedDocuments()},
 * {@link AgentContext#unprocessedFTSearch(String, int)}, and
 * {@link AgentContext#unprocessedSearch(String, lotus.domino.DateTime, int)}.</li>
 * <li>To get a document based on its note ID or UNID, use {@link Database#getDocumentByID(int)} or
 * {@link Database#getDocumentByUNID(String)}.</li>
 * </ul>
 * <p>
 * Once you have a view, you can navigate to a specific document using methods in the {@link View} class.
 * </p>
 * <p>
 * Once you have a collection of documents, you can navigate to a specific document using methods in the {@link DocumentCollection} class.
 * </p>
 * <h3>Saving changes</h3>
 * <p>
 * After you create, modify, or delete a document, you must save the changes by calling the {@link #save()} method. If you don't call
 * <code>save</code> before the program finishes, all of your changes to a <code>Document</code> are lost.
 * </p>
 * <p>
 * If you create and save a new document without adding any items to it, the document is saved with one item "$UpdatedBy." This item
 * contains the name of the creator of the document.
 * </p>
 * <h3>Encryption</h3>
 * <p>
 * A program attempts to decrypt an encrypted document the first time the program accesses one of the <code>Document</code> properties or
 * methods. If decryption fails, an exception is thrown.
 * </p>
 */
public interface Document extends Base<lotus.domino.Document>, lotus.domino.Document, org.openntf.domino.ext.Document, Resurrectable,
DatabaseDescendant, Map<String, Object>, AsDocMap, ExceptionDetails {
	//defaults when 'large summary' hasn't been set
    //see https://help.hcltechsw.com/dom_designer/11.0.1/basic/H_NOTES_AND_DOMINO_KNOWN_LIMITS.html
    public static int MAX_NATIVE_FIELD_SIZE = 32000;        
	public static int MAX_SUMMARY_FIELD_SIZE = 64000;

	public static class Schema extends FactorySchema<Document, lotus.domino.Document, Database> {
		@Override
		public Class<Document> typeClass() {
			return Document.class;
		}

		@Override
		public Class<lotus.domino.Document> delegateClass() {
			return lotus.domino.Document.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Creates a new item in this document.
	 * <p>
	 * In general, {@link #replaceItemValue(String, Object)} is favored over this method. If an item of the same name already exists in a
	 * document, <code>appendItemValue</code> creates a second item of the same name, and the duplicate items are not accessible except
	 * through a work-around. If you are creating a new document, <code>appendItemValue</code> is safe.
	 * </p>
	 * <p>
	 * To keep the new item, you call {@link #save()} after calling appendItemValue.
	 * </p>
	 * <p>
	 * If the document already has an item called <code>name</code>, this method does not replace it. Instead, it creates another item of
	 * the same name and gives it the value you specify.
	 * </p>
	 *
	 * @param name
	 *            The name of the new item.
	 * @return The new item.
	 */
	@Override
	public Item appendItemValue(final String name);

	/**
	 * Creates a new item in this document or if the <code>Fixes.APPEND_ITEM_VALUE</code> fix is enabled, appends a value to an existing
	 * item.
	 * <p>
	 * In general, {@link #replaceItemValue(String, Object)} is favored over this method. If an item of the same name already exists in a
	 * document, <code>appendItemValue</code> creates a second item of the same name, and the duplicate items are not accessible except
	 * through a work-around. If you are creating a new document, <code>appendItemValue</code> is safe.
	 * </p>
	 * <p>
	 * To keep the new item, you call {@link #save()} after calling appendItemValue.
	 * </p>
	 * <p>
	 * If the document already has an item called <code>name</code>, this method does not replace it. Instead, it creates another item of
	 * the same name and gives it the value you specify.
	 * </p>
	 * <h5>Note</h5>
	 * <p>
	 * When the Khan mode is active, this method appends the <code>value</code> to an item if it already exists. It does not create a new
	 * item.
	 * </p>
	 * <p>
	 * To enable the Khan mode, put the line <code>org.openntf.domino.xsp=khan</code> to the xsp.properties file.
	 * </p>
	 * <p>
	 * To enable only this specific behaviour without enabling the Khan mode, call
	 * <code>Session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true)</code> before calling the <code>appendItemValue()</code> method.
	 * </p>
	 *
	 * @param name
	 *            The name of the new item.
	 * @param value
	 *            The value of the new item.
	 * @return The new item.
	 */
	@Override
	public Item appendItemValue(final String name, final double value);

	/**
	 * Creates a new item in this document or if the <code>Fixes.APPEND_ITEM_VALUE</code> fix is enabled, appends a value to an existing
	 * item.
	 * <p>
	 * In general, {@link #replaceItemValue(String, Object)} is favored over this method. If an item of the same name already exists in a
	 * document, <code>appendItemValue</code> creates a second item of the same name, and the duplicate items are not accessible except
	 * through a work-around. If you are creating a new document, <code>appendItemValue</code> is safe.
	 * </p>
	 * <p>
	 * To keep the new item, you call {@link #save()} after calling appendItemValue.
	 * </p>
	 * <p>
	 * If the document already has an item called <code>name</code>, this method does not replace it. Instead, it creates another item of
	 * the same name and gives it the value you specify.
	 * </p>
	 * <h5>Note</h5>
	 * <p>
	 * When the Khan mode is active, this method appends the <code>value</code> to an item if it already exists. It does not create a new
	 * item.
	 * </p>
	 * <p>
	 * To enable the Khan mode, put the line <code>org.openntf.domino.xsp=khan</code> to the xsp.properties file.
	 * </p>
	 * <p>
	 * To enable only this specific behaviour without enabling the Khan mode, call
	 * <code>Session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true)</code> before calling the <code>appendItemValue()</code> method.
	 * </p>
	 *
	 * @param name
	 *            The name of the new item.
	 * @param value
	 *            The value of the new item.
	 * @return The new item.
	 */
	@Override
	public Item appendItemValue(final String name, final int value);

	/**
	 * Creates a new item in this document or if the <code>Fixes.APPEND_ITEM_VALUE</code> fix is enabled, appends a value to an existing
	 * item.
	 * <p>
	 * In general, {@link #replaceItemValue(String, Object)} is favored over this method. If an item of the same name already exists in a
	 * document, <code>appendItemValue</code> creates a second item of the same name, and the duplicate items are not accessible except
	 * through a work-around. If you are creating a new document, <code>appendItemValue</code> is safe.
	 * </p>
	 * <p>
	 * To keep the new item, you call {@link #save()} after calling appendItemValue.
	 * </p>
	 * <p>
	 * If the document already has an item called <code>name</code>, this method does not replace it. Instead, it creates another item of
	 * the same name and gives it the value you specify.
	 * </p>
	 * <p>
	 * The data type of the new item depends upon the data type of the value that you place in it:
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" class="table" rules="all" frame="border" border="1">
	 * <thead align="left" valign="bottom">
	 * <tr>
	 * <th align="left" valign="top" width="NaN%" id="d466934e95">Data type of value</th>
	 * <th align="left" valign="top" width="NaN%" id="d466934e100">Resulting item</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">String</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Text</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Integer</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Number</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Double</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Number</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">DateTime</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Date-time item</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">java.util.Vector with String, Integer, Double, or DateTime elements
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Multi-value text, number, or date-time item</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Item</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Same data type as the Item</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <h5>Note</h5>
	 * <p>
	 * When the Khan mode is active, this method appends the <code>value</code> to an item if it already exists. It does not create a new
	 * item.
	 * </p>
	 * <p>
	 * To enable the Khan mode, put the line <code>org.openntf.domino.xsp=khan</code> to the xsp.properties file.
	 * </p>
	 * <p>
	 * To enable only this specific behaviour without enabling the Khan mode, call
	 * <code>Session.setFixEnable(Fixes.APPEND_ITEM_VALUE, true)</code> before calling the <code>appendItemValue()</code> method.
	 * </p>
	 *
	 * @param name
	 *            The name of the new item.
	 * @param value
	 *            The value of the new item.
	 *
	 * @return The new item.
	 */
	@Override
	public Item appendItemValue(final String name, final Object value);

	/**
	 * Attaches one or more documents in vCard format.
	 * <p>
	 * To keep the new attachment, call {@link #save()} after calling <code>attachVCard</code>.
	 * </p>
	 * <p>
	 * If an empty collection is supplied, the error "No documents in collection" will be thrown.
	 * </p>
	 * <p>
	 * If documents are not Contact documents, the documents will not be attached, and the error "Attach of VCard failed" will be thrown.
	 * </p>
	 * <p>
	 * The attachment will be in the UTF-8 character set.
	 * </p>
	 *
	 * @param document
	 *            {@link Document}, {@link DocumentCollection}, or {@link NoteCollection}. The Contact document or documents to be attached
	 *            in vCard format. If a collection is specified, a single attachment will be made with the individual vCards lying end to
	 *            end.
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document);

	/**
	 * Attaches one or more documents in vCard format.
	 * <p>
	 * To keep the new attachment, call {@link #save()} after calling <code>attachVCard</code>.
	 * </p>
	 * <p>
	 * If an empty collection is supplied, the error "No documents in collection" will be thrown.
	 * </p>
	 * <p>
	 * If documents are not Contact documents, the documents will not be attached, and the error "Attach of VCard failed" will be thrown.
	 * </p>
	 * <p>
	 * The attachment will be in the UTF-8 character set.
	 * </p>
	 *
	 * @param document
	 *            {@link Document}, {@link DocumentCollection}, or {@link NoteCollection}. The Contact document or documents to be attached
	 *            in vCard format. If a collection is specified, a single attachment will be made with the individual vCards lying end to
	 *            end.
	 * @param fileName
	 *            The filename of the Contact document or documents to be attached in vCard format.
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document, final String fileName);

	/**
	 * Closes MIME processing associated with item named "Body".
	 * <p>
	 * This method allows you terminate the scope of MIME processing so you can safely work on items through the item interface. Do not work
	 * on items directly without first closing MIME processing.
	 * </p>
	 * <p>
	 * Saving the MIME entity content and headers with this method is an in-memory process. To save document content permanently, you must
	 * call {@link #save}.
	 * </p>
	 *
	 * @return true if MIME processing for the items closes successfully
	 */
	@Override
	public boolean closeMIMEEntities();

	/**
	 * Closes MIME processing associated with item named "Body".
	 * <p>
	 * This method allows you terminate the scope of MIME processing so you can safely work on items through the item interface. Do not work
	 * on items directly without first closing MIME processing.
	 * </p>
	 * <p>
	 * Saving the MIME entity content and headers with this method is an in-memory process. To save document content permanently, you must
	 * call {@link #save}.
	 * </p>
	 *
	 *
	 * @param savechanges
	 *            Whether MIME entity content and header changes are saved in the associated in-memory items.
	 * @return true if MIME processing for the items closes successfully
	 */
	@Override
	public boolean closeMIMEEntities(final boolean savechanges);

	/**
	 * Closes MIME processing associated with items of a specified name.
	 * <p>
	 * This method allows you terminate the scope of MIME processing so you can safely work on items through the item interface. Do not work
	 * on items directly without first closing MIME processing.
	 * </p>
	 * <p>
	 * Saving the MIME entity content and headers with this method is an in-memory process. To save document content permanently, you must
	 * call {@link #save}.
	 * </p>
	 * <p>
	 * This method returns false if no MIME processing is associated with items of the specified name.
	 * </p>
	 *
	 * @param savechanges
	 *            Whether MIME entity content and header changes are saved in the associated in-memory items. Specify true to retain changes
	 *            to MIME entity content and headers or false to discard changes to MIME entity content and headers.
	 * @param entityitemname
	 *            The name of the item or items associated with the MIME processing.
	 * @return true if MIME processing for the items closes successfully
	 */
	@Override
	public boolean closeMIMEEntities(final boolean savechanges, final String entityitemname);

	/**
	 * Validates a document by executing the default value, translation, and validation formulas, if any are defined in the document form.
	 * <p>
	 * The form is as follows:
	 * </p>
	 * <ul>
	 * <li>The form stored in the document, if any.</li>
	 * <li>The value of the Form item, if no form is stored in the document.</li>
	 * <li>The database default form, if the document does not have a Form item.</li>
	 * </ul>
	 * <p>
	 * In the user interface, you must use a form to create a document. The document must meet the form requirements for input validation,
	 * and the user interface informs you if the document does not meet these requirements. Programatically you can create a document
	 * without a form. The <code>computeWithForm</code> method provides a means of checking that the data you placed in a document meets the
	 * requirements of a form, although (unlike in the user interface) you can still save a document if <code>computeWithForm</code> returns
	 * false or throws an exception.
	 * </p>
	 *
	 * @param dodatatypes
	 *            The method ignores this parameter. Specify either true or false.
	 * @param raiseerror
	 *            If true, an error is raised if the validation fails. If false, no error is raised; instead, the method returns false if
	 *            validation fails.
	 * @return true if there are no errors in the document or false if there are errors
	 */
	@Override
	public boolean computeWithForm(final boolean dodatatypes, final boolean raiseerror);

	/**
	 * Converts a document in Notes format to MIME format similar to a mail router.
	 * <p>
	 * The document's form is evaluated and the fields are used in the MIME conversion.
	 * </p>
	 * <p>
	 * The conversion of rich text is imperfect.
	 * </p>
	 *
	 */
	@Override
	public void convertToMIME();

	/**
	 * Converts a document in Notes format to MIME format similar to a mail router.
	 * <p>
	 * The document's form is evaluated and the fields are used in the MIME conversion.
	 * </p>
	 * <p>
	 * The conversion of rich text is imperfect.
	 * </p>
	 *
	 * @param conversiontype
	 *            One of the following:
	 *            <ul>
	 *            <li>Document.CONVERT_RT_TO_HTML (2) produces MIME output with a Text/HTML part that is a representation of the Notes rich
	 *            text. Some data loss is possible in the rendering. Improvements in fidelity may occur at any time.</li>
	 *            <li>Document.CONVERT_RT_TO_PLAINTEXT (1) produces MIME output with a Text/Plain part that is a representation of the Notes
	 *            rich text. Everything but text contained in rich text is lost. For tables, a crude approximation is made using non-graphic
	 *            characters.</li>
	 *            <li>Document.CONVERT_RT_TO_PLAINTEXT_AND_HTML (3) (default) produces MIME output with two renditions of the Notes rich
	 *            text: a Text/Plain part and a Text/HTML part. The characteristics of each stream are the same as their corresponding
	 *            descriptions. This is useful when the target audience may or may not have an HTML-capable mail reader, or can receive only
	 *            text.</li>
	 *            </ul>
	 */
	@Override
	public void convertToMIME(final int conversiontype);

	/**
	 * Converts a document in Notes format to MIME format similar to a mail router.
	 * <p>
	 * The document's form is evaluated and the fields are used in the MIME conversion.
	 * </p>
	 * <p>
	 * The conversion of rich text is imperfect.
	 * </p>
	 *
	 * @param conversiontype
	 *            One of the following:
	 *            <ul>
	 *            <li>Document.CONVERT_RT_TO_HTML (2) produces MIME output with a Text/HTML part that is a representation of the Notes rich
	 *            text. Some data loss is possible in the rendering. Improvements in fidelity may occur at any time.</li>
	 *            <li>Document.CONVERT_RT_TO_PLAINTEXT (1) produces MIME output with a Text/Plain part that is a representation of the Notes
	 *            rich text. Everything but text contained in rich text is lost. For tables, a crude approximation is made using non-graphic
	 *            characters.</li>
	 *            <li>Document.CONVERT_RT_TO_PLAINTEXT_AND_HTML (3) (default) produces MIME output with two renditions of the Notes rich
	 *            text: a Text/Plain part and a Text/HTML part. The characteristics of each stream are the same as their corresponding
	 *            descriptions. This is useful when the target audience may or may not have an HTML-capable mail reader, or can receive only
	 *            text.</li>
	 *            </ul>
	 * @param options
	 *            Reserved for future use.
	 */
	@Override
	public void convertToMIME(final int conversiontype, final int options);

	/**
	 * Copies all items in this document into the destination document. The item names are unchanged.
	 * <p>
	 * If you are not copying to a newly created document, you should probably specify true for the second parameter. See
	 * {@link #appendItemValue(String)} for a note about appending items to existing documents.
	 * </p>
	 *
	 * @param doc
	 *            The destination document.
	 * @param replace
	 *            If true, the items in the destination document are replaced. If false, the items in the destination document are appended.
	 */
	@Override
	public void copyAllItems(final lotus.domino.Document doc, final boolean replace);

	/**
	 * Copies an item into this document.
	 *
	 * @param item
	 *            The item, usually from another document, that you want to copy. Cannot be null.
	 * @return A copy of the specified item parameter
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item);

	/**
	 * Copies an item into this document and assigns the copied item a new name.
	 *
	 * @param item
	 *            The item, usually from another document, that you want to copy. Cannot be null.
	 * @param newName
	 *            The name to assign to the copied item. Specify null to retain the existing name of the item.
	 * @return A copy of the specified item parameter, identical except for its <code>newName</code>.
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item, final String newName);

	/**
	 * Copies a document to a specified database and saves it.
	 *
	 * @param db
	 *            The database to which you want to copy the document. Cannot be null.
	 * @return The new document in the specified database.
	 */
	@Override
	public Document copyToDatabase(final lotus.domino.Database db);

	/**
	 * Creates an uninitialized top-level MIME entity named "Body" in a document.
	 * <p>
	 * An item created with this method is of type <code>MIME_PART</code>.
	 * </p>
	 * <p>
	 * To initialize the MIME entity, provide some content and save the containing document.
	 * </p>
	 * <p>
	 * A MIME entity named "Body" gets special support as a Domino mail message. In particular, non-Content headers become items in the
	 * document.
	 * </p>
	 *
	 * @return The new MIME entity.
	 */
	@Override
	public MIMEEntity createMIMEEntity();

	/**
	 * Creates an uninitialized top-level MIME entity in a document.
	 * <p>
	 * An item created with this method is of type <code>MIME_PART</code>.
	 * </p>
	 * <p>
	 * To initialize the MIME entity, provide some content and save the containing document.
	 * </p>
	 * <p>
	 * A MIME entity named "Body" gets special support as a Domino mail message. In particular, non-Content headers become items in the
	 * document. If you specify an item name other than Body, the containing document cannot be mailed.
	 * </p>
	 *
	 * @param itemName
	 *            The name of the item containing the MIME entity.
	 * @return The new MIME entity.
	 */
	@Override
	public MIMEEntity createMIMEEntity(final String itemName);

	/**
	 * Creates a new document that is formatted as a reply to this document.
	 * <p>
	 * The new document does not contain a Subject item. If you want one, the program must explicitly add it to the document.
	 * </p>
	 * <p>
	 * The new document does not automatically get mailed. If you want to mail it, the program must explicitly call the {@link #send()}
	 * method.
	 * </p>
	 *
	 * @param toall
	 *            If true, the new document recipient list contains all the recipients of the original. If false, the new document recipient
	 *            list contains only the sender of the original.
	 * @return A reply to this document.
	 */
	@Override
	public Document createReplyMessage(final boolean toall);

	/**
	 * Creates a new rich-text item in a document.
	 *
	 * @param name
	 *            The name of the new rich-text item.
	 * @return The newly created item.
	 */
	@Override
	public RichTextItem createRichTextItem(final String name);

	/**
	 * Encrypts a document.
	 * <p>
	 * The encrypted document is not saved until you call {@link #save()}. Only the items for which {@link Item#isEncrypted()} is true are
	 * encrypted. Items for which <code>isEncrypted</code> is false remain visible to any user, even if the user does not have the proper
	 * encryption key.
	 * </p>
	 * <p>
	 * If the {@link #getEncryptionKeys() EncryptionKeys} property is set with one or more named keys, those keys are used to encrypt the
	 * document. Any user who has one of the encryption keys can decrypt the document. If there are no encryption keys specified, the
	 * document is encrypted with the user's public key, in which case only the user who encrypted the document can decrypt it.
	 * </p>
	 * <p>
	 * If the program is running on a server, it must have permission to use Encrypt.
	 * </p>
	 * <p>
	 * Since mail encryption works differently, do not use this method if you want to mail an encrypted document. Instead, set the
	 * {@link #setEncryptOnSend(boolean) EncryptOnSend} property to true, and use the {@link #send()} method.
	 * </p>
	 * <p>
	 * You cannot use this method on a <code>Document</code> object returned by {@link AgentContext#getDocumentContext()}.
	 * </p>
	 *
	 */
	@Override
	public void encrypt();

	/**
	 * Generates an XML representation of a document and returns it as a String. The XML conforms to the Domino Document DTD.
	 * <p>
	 * This method supports the following simple items:
	 * </p>
	 * <ul>
	 * <li>Text</li>
	 * <li>Text list</li>
	 * <li>Number</li>
	 * <li>Number list</li>
	 * <li>Datetime</li>
	 * <li>Datetime list</li>
	 * </ul>
	 * <p>
	 * To generate form semantics, you must call {@link #computeWithForm(boolean, boolean)} method before calling the
	 * <code>generateXML</code> method.
	 * </p>
	 *
	 * @return The XML representation of the document.
	 */
	@Override
	public String generateXML();

	/**
	 * Generates an XML representation of a document using the given stylesheet and writes the XML to given target. The XML conforms to the
	 * Domino Document DTD.
	 * <p>
	 * The generateXML method supports the following simple items:
	 * </p>
	 * <ul>
	 * <li>Text</li>
	 * <li>Text list</li>
	 * <li>Number</li>
	 * <li>Number list</li>
	 * <li>Datetime</li>
	 * <li>Datetime list</li>
	 * </ul>
	 * <p>
	 * To generate form semantics, you must call {@link #computeWithForm(boolean, boolean)} method before calling the
	 * <code>generateXML</code> method.
	 * </p>
	 *
	 * @param style
	 *            The style sheet you want to use to transform the generated XML.
	 * @param target
	 *            The object that receives the transformed XML.
	 */
	@Override
	public void generateXML(final Object style, final lotus.domino.XSLTResultTarget target) throws IOException;

	/**
	 * Generates an XML representation of a document and sends it to the given writer. The XML conforms to the Domino Document DTD.
	 * <p>
	 * The generateXML method supports the following simple items:
	 * </p>
	 * <ul>
	 * <li>Text</li>
	 * <li>Text list</li>
	 * <li>Number</li>
	 * <li>Number list</li>
	 * <li>Datetime</li>
	 * <li>Datetime list</li>
	 * </ul>
	 * <p>
	 * To generate form semantics, you must call {@link #computeWithForm(boolean, boolean)} method before calling the
	 * <code>generateXML</code> method.
	 * </p>
	 *
	 * @param w
	 *            The Writer that will receive the result XML.
	 */
	@Override
	public void generateXML(final Writer w) throws IOException;

	/**
	 * Returns a representation of a file attachment. You can use this method to find file attachments that are not contained in a rich text
	 * item as well as file attachments that are contained in a rich text item.
	 * <p>
	 * The {@link EmbeddedObject#getParent() Parent} property for the returned {@link EmbeddedObject} returns null, since it was not
	 * accessed through a {@link RichTextItem}.
	 * </p>
	 *
	 * @param fileName
	 *            The file name of the file attachment.
	 * @return A representation of the file attachment. Returns null if an attachment by the specified name is not found.
	 */
	@Override
	public EmbeddedObject getAttachment(final String fileName);

	/**
	 * The names of the people who have saved a document.
	 * <p>
	 * If a name is hierarchical, this property returns the fully distinguished name.
	 * </p>
	 * <p>
	 * This property does not return the names of people who have permission to edit a document (as found in an item of type Authors).
	 * Therefore, the people returned by the Authors property and the people listed in an Authors item may differ.
	 * </p>
	 *
	 * @return The names of the people who have saved a document.
	 */
	@Override
	public Vector<String> getAuthors();

	/**
	 * A vector of values, each element of which corresponds to a column value in the document's parent view. The first value in the vector
	 * is the value that appears in the view's first column for the document, the second value is the one that appears in the second column,
	 * and so on. The value of each element of the vector is the result of the corresponding column's formula and the items on the current
	 * document. Some elements in the vector might have no value.
	 * <p>
	 * If you do not access the document through a view, this property has no value.
	 * </p>
	 * <p>
	 * If a document contains an item that's visible in a view, the <code>ColumnValues</code> property provides efficient access to its
	 * value. Accessing the item value directly is less efficient.
	 * </p>
	 * <p>
	 * This property returns a value for each column in the parent view, regardless of whether the column is considered "Responses-only."
	 * For example, if the third column in a view is responses-only, doc.GetColumnValues( 2 ) evaluates the column formula for the document
	 * and returns a result, whether the document is a response or not.
	 * </p>
	 * <p>
	 * A column value is not returned if it is determined by:
	 * <ul>
	 * <li>A formula containing a UI-only function such as &commat;IsExpandable or &commat;DocNumber.</li>
	 * <li>A constant.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Take care to treat a column with multiple values as type {@link java.util.Vector}.
	 * </p>
	 *
	 * @return A vector where each element corresponds to a column value in the document's parent view.
	 *
	 */
	@Override
	public Vector<Object> getColumnValues();

	/**
	 * The date/time a document was created.
	 */
	@Override
	public DateTime getCreated();

	/**
	 * The OLE/2 and OLE/1 embedded objects in a document.
	 * <p>
	 * Unlike the {@link RichTextItem#getEmbeddedObjects() EmbeddedObjects} property in {@link RichTextItem}, this property does
	 * <em>not</em> include file attachments, nor OLE/1 objects created in Notes Release 3.
	 * </p>
	 * <p>
	 * This property <em>does</em> include OLE/2 and OLE/1 objects created in Release 4 and higher. It also includes objects in the document
	 * that were originally embedded in the document's form. Such objects must have been activated, modified, and re-saved in order to be
	 * returned by this property (otherwise they remain a part of the form, not the document).
	 * </p>
	 * <p>
	 * The vector is empty if the document contains no embedded objects.
	 * </p>
	 *
	 * @return The OLE/2 and OLE/1 embedded objects in a document.
	 *
	 */
	@Override
	public Vector<EmbeddedObject> getEmbeddedObjects();

	/**
	 * The key(s) used to encrypt a document. The {@link #encrypt()} method uses these keys when it encrypts the document.
	 * <p>
	 * Each element in <code>EncryptionKeys</code> contains the name of an encryption key that you want to use to encrypt the document. The
	 * document can be decrypted by any user who posesses one of the keys. If there are no encryption keys specified for a document, the
	 * document is encrypted with the current user's public key and can only be decrypted by that user.
	 * </p>
	 * <p>
	 * You must call the {@link #encrypt()} and {@link #save()} methods to actually encrypt the document. Since encryption works differently
	 * when a document is mailed, the EncryptionKeys property has no effect when a document is encrypted when mailed.
	 * </p>
	 * <p>
	 * The name of each encryption key in a document is stored in a text item called <code>SecretEncryptionKeys</code>. This property
	 * returns the contents of the item.
	 * </p>
	 *
	 * @return The key(s) used to encrypt a document. The encrypt method uses these keys when it encrypts the document.
	 */
	@Override
	public Vector<String> getEncryptionKeys();

	/**
	 * Returns the first item of a specified name in a document.
	 * <p>
	 * If multiple items in a document have the same name, programmatic access is limited to the first item. The remaining items yield
	 * invalid data. A work-around is to get the first item, process it, remove it, again get the first item (which was the second item),
	 * and so on until you process all the items with the same name. If you do not save the document, the items are not actually removed.
	 * However, the recommendation is that you avoid creating multiple items with the same name.
	 * </p>
	 * <p>
	 * If the value of a field is computed for display, the value is not stored as an item and is inaccessible from a Document object. In
	 * some cases, you can access the field value another way. For example, if a document has a DateComposed field computed for display with
	 * the formula &commat;Created, use {@link #getCreated()}.
	 * </p>
	 *
	 * @param name
	 *            The name of the item you want to find.
	 * @return The first item with name. Returns null if the document does not contain an item with name.
	 */
	@Override
	public Item getFirstItem(final String name);

	/**
	 * The names of the folders containing a document.
	 * <p>
	 * The database must have the <code>$FolderInfo</code> and <code>$FolderRefInfo</code> hidden views to support folder references. These
	 * views can be copied from the mail template. This property does not return view references.
	 * </p>
	 * <p>
	 * Folder references must be enabled for the database. See the {@link Database#getFolderReferencesEnabled()} property.
	 * </p>
	 *
	 * @return The names of the folders containing a document.
	 * @see Database#getFolderReferencesEnabled()
	 */
	@Override
	public Vector<String> getFolderReferences();

	/**
	 * The full-text search score of a document, if it was retrieved as part of a full-text search.
	 * <p>
	 * The score is determined by the number of target words that are found in the document, the term weights assigned to the target words,
	 * and any proximity operators in the search query. If the document is not retrieved as part of a full-text search, returns 0. If the
	 * document is retrieved using an {@link Database#FTSearch(String)} method on a database without a full-text index, returns an
	 * unpredictable number.
	 * </p>
	 * <p>
	 * If a document is in more than one {@link DocumentCollection} or {@link ViewEntryCollection}, its score is that of the last collection
	 * from which it was retrieved. The score is correct unless you get the score from the current object after retrieving the same document
	 * from another collection.
	 * </p>
	 * <p>
	 * Documents added to a collection have a search score of 0.
	 * </p>
	 * <p>
	 * Documents deleted from a view have a search score of 0.
	 * </p>
	 *
	 * @return The full-text search score of a document, if it was retrieved as part of a full-text search.
	 */
	@Override
	public int getFTSearchScore();

	/**
	 * The Domino URL of a form when HTTP protocols are in effect.
	 * <p>
	 * If HTTP protocols are not available, this property returns an empty string.
	 * </p>
	 * <p>
	 * See {@link Session#resolve(String)} for additional information and examples.
	 * </p>
	 *
	 * @return The Domino URL of a form when HTTP protocols are in effect.
	 *
	 */
	@Override
	public String getHttpURL();

	/**
	 * The date/time a document was initially modified. This property is null or 0 if the document is not modified.
	 */
	@Override
	public DateTime getInitiallyModified();

	/**
	 * All the items in this document. An item is any piece of data stored in a document.
	 *
	 * @return All the items in a document.
	 */
	@Override
	public Vector<Item> getItems();

	/**
	 * Returns the value of an item.
	 * <p>
	 * If multiple items have the same name, this method returns the value of the first item.
	 * </p>
	 * <p>
	 * If the item has no value, this method returns an empty vector.
	 * </p>
	 * <p>
	 * If no item with the specified name exists, this method returns an empty vector. It does not throw an exception. Use
	 * {@link #hasItem(String)} to verify the existence of an item.
	 * </p>
	 *
	 * <p>
	 * This property returns the same value(s) for an item as {@link Item#getValues()}.
	 * </p>
	 *
	 * @param name
	 *            The name of an item.
	 * @return The value or values contained in the item. The data type of the value depends on the data type of the item.
	 *         <table cellpadding="4" cellspacing="0" summary="" class="table" rules="all" frame="border" border="1">
	 *         <thead class="thead" align="left" valign="bottom">
	 *         <tr >
	 *         <th align="left" valign="top" width="NaN%" id="d1563738e74">
	 *         <p >
	 *         Notes item type
	 *         </p>
	 *         </th>
	 *         <th align="left" valign="top" width="NaN%" id="d1563738e81">
	 *         <p >
	 *         Value return type
	 *         </p>
	 *         </th>
	 *         </tr>
	 *         </thead> <tbody class="tbody">
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e74 ">
	 *         <p >
	 *         Rich text
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e81 ">
	 *         <p >
	 *         java.util.Vector with one String element rendered into plain text
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e74 ">
	 *         <p >
	 *         Text (includes Names, Authors, and Readers item types) or text list
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e81 ">
	 *         <p >
	 *         java.util.Vector with String elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e74 ">
	 *         <p >
	 *         Number or number list
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e81 ">
	 *         <p >
	 *         java.util.Vector with Double elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e74 ">
	 *         <p >
	 *         Date-time or range of date-time values
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d1563738e81 ">
	 *         <p >
	 *         java.util.Vector with DateTime elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         </tbody>
	 *         </table>
	 */
	@Override
	public Vector<Object> getItemValue(final String name);

	/**
	 * Returns as an object the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link #getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link #replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @return An object that receives the value of the item. Must have the same class definition as the object written to the item.
	 */
	@Override
	public Object getItemValueCustomData(final String itemName) throws IOException, ClassNotFoundException;

	/**
	 * Returns as an object the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link #getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link #replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param dataTypeName
	 *            The name of the data type. If specified, this name must match the data type name specified when the item was written.
	 * @return An object that receives the value of the item. Must have the same class definition as the object written to the item.
	 */
	@Override
	public Object getItemValueCustomData(final String itemName, final String dataTypeName) throws IOException, ClassNotFoundException;

	/**
	 * Returns as a byte array the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * *
	 * <ul>
	 * <li>{@link #getItemValueCustomData(String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link #replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param dataTypeName
	 *            The name of the data type. This name must match the data type name specified when the item was written.
	 * @return A byte array that receives the value of the item.
	 */
	@Override
	public byte[] getItemValueCustomDataBytes(final String itemName, final String dataTypeName) throws IOException;

	/**
	 * Returns the value of a date-time item in a document.
	 * <p>
	 * You can determine the class of each element with Object.getClass().getName(). For the local interface, the class name is either
	 * lotus.domino.local.DateTime or lotus.domino.local.DateRange; for the remote interface, the class name is either
	 * lotus.domino.cso.DateTime or lotus.domino.cso.DateRange.
	 * </p>
	 *
	 * @param name
	 *            The name of a date-time item.
	 * @return The value or values contained in the item. Each element in the vector corresponds to a value in the item and is of type
	 *         DateTime or DateRange. If the item contains a single value, the vector has one element.
	 */
	@Override
	public Vector<Base<?>> getItemValueDateTimeArray(final String name);

	/**
	 * Returns the value of an item with a single numeric value.
	 * <p>
	 * If multiple items have the same name, this method returns the value of the first item.
	 * </p>
	 * <p>
	 * If the item has no value or the value is text, date-time, or empty, this method returns 0.0.
	 * </p>
	 * <p>
	 * If no item with the specified name exists, this method returns 0.0. It does not throw an exception. Use {@link #hasItem(String)} to
	 * verify the existence of an item.
	 * </p>
	 * <p>
	 * If the item has multiple values, this method returns the first value.
	 * </p>
	 *
	 * @param name
	 *            The name of the item.
	 * @return The value of the item.
	 */
	@Override
	public double getItemValueDouble(final String name);

	/**
	 * Returns the value of an item with a single numeric value.
	 * <p>
	 * If multiple items have the same name, this method returns the value of the first item.
	 * </p>
	 * <p>
	 * If the item has no value or the value is text, date-time, or empty, this method returns 0.
	 * </p>
	 * <p>
	 * If no item with the specified name exists, this method returns 0. It does not throw an exception. Use {@link #hasItem(String)} to
	 * verify the existence of an item.
	 * </p>
	 * <p>
	 * If the item has multiple values, this method returns the first value.
	 * </p>
	 * <p>
	 * A decimal number is rounded down if the fraction is less than 0.5 and up if the fraction is 0.5 or greater.
	 * </p>
	 *
	 * @param name
	 *            The name of the item.
	 * @return The value of the item, rounded to the nearest integer.
	 */
	@Override
	public int getItemValueInteger(final String name);

	/**
	 * Returns the value of an item with a single text value.
	 * <p>
	 * If multiple items have the same name, this method returns the value of the first item.
	 * </p>
	 * <p>
	 * If the item has no value or the value is numeric or date-time, this method returns null in Domino Designer 6.5 and earlier, and an
	 * empty java string in Domino Designer 6.55 and later. To test for an empty java string, use the <code>length</code> property.
	 * </p>
	 * <p>
	 * If no item with the specified name exists, this method returns null in Domino Designer 6.5 and earlier, and an empty java string in
	 * Domino Designer 6.55 and later. It does not throw an exception. Use {@link #hasItem(String)} to verify the existence of an item.
	 * </p>
	 * <p>
	 * This method returns a rich text item rendered to plain text. Formatting and embedded objects are lost.
	 * </p>
	 * <p>
	 * If the item has multiple values, this method returns the first value.
	 * </p>
	 *
	 * @param name
	 *            The name of the item.
	 * @return The value of the item.
	 */
	@Override
	public String getItemValueString(final String name);

	/**
	 * If this is a profile document, returns the user name (key) of the profile.
	 *
	 */
	@Override
	public String getKey();

	/**
	 * The date/time a document was last modified or read.
	 * <p>
	 * The value returned is exact only to the day, not the hour. If the document is edited, the property is always updated. If the document
	 * is read more than once during the same 24-hour period, the value is only updated the first time accessed.
	 * </p>
	 *
	 * @return The date/time a document was last modified or read.
	 *
	 */
	@Override
	public DateTime getLastAccessed();

	/**
	 * The date/time a document was last modified.
	 * <p>
	 * &commat;Modified and {@link #getLastModified()} are not equivalent.
	 * </p>
	 *
	 * @return The date/time a document was last modified.
	 *
	 */
	@Override
	public DateTime getLastModified();

	/**
	 * The names of the holders of a lock.
	 * <p>
	 * If the document is locked, the vector contains the names of the lock holders. The document can be locked by one or more users or
	 * groups.
	 * </p>
	 * <p>
	 * If the document is not locked, the vector contains one element whose value is an empty string ("").
	 * </p>
	 *
	 * @return The names of the holders of a lock.
	 *
	 */
	@Override
	public Vector<String> getLockHolders();

	/**
	 * Gets a top-level MIME entity for the "Body" item.
	 * <p>
	 * An item containing a MIME entity is of type MIME_PART.
	 * </p>
	 * <p>
	 * This method returns null if the requested item does not exist or is not of type MIME_PART.
	 * </p>
	 * <p>
	 * This method is equivalent to {@link Document#getFirstItem(String)} followed by {@link Item#getMIMEEntity()}.
	 * </p>
	 *
	 * @return The MIME entity for an item named "Body"
	 */
	@Override
	public MIMEEntity getMIMEEntity();

	/**
	 * Gets a top-level MIME entity for an item of a given name.
	 * <p>
	 * An item containing a MIME entity is of type MIME_PART.
	 * </p>
	 * <p>
	 * This method returns null if the requested item does not exist or is not of type MIME_PART.
	 * </p>
	 * <p>
	 * This method is equivalent to {@link Document#getFirstItem(String)} followed by {@link Item#getMIMEEntity()}.
	 * </p>
	 *
	 * @param itemName
	 *            The name of the item containing the MIME entity.
	 * @return The MIME entity.
	 */
	@Override
	public MIMEEntity getMIMEEntity(final String itemName);

	/**
	 * If a profile document, the name of the profile.
	 */
	@Override
	public String getNameOfProfile();

	/**
	 * The note ID of this document, which is a hexadecimal value of up to 8 characters that uniquely identifies a document within a
	 * particular database.
	 * <p>
	 * A typical note ID looks like this: 20FA. A note ID represents the location of a document within a specific database file, so
	 * documents that are replicas of one another generally have different note IDs. A note ID does not change, unless the document is
	 * deleted.
	 * </p>
	 *
	 * @return The note ID of this document
	 *
	 */
	@Override
	public String getNoteID();

	/**
	 * The Domino URL of a form when Notes protocols are in effect.
	 * <p>
	 * If Notes protocols are not available, this property returns an empty string.
	 * </p>
	 *
	 * @return The Domino URL of a form when Notes protocols are in effect or empty string if not
	 *
	 */
	@Override
	public String getNotesURL();

	/**
	 * The database that contains this document
	 *
	 */
	@Override
	public org.openntf.domino.Database getParentDatabase();

	/**
	 * The universal ID of a document's parent, if the document is a response. Returns an empty string if a document doesn't have a parent.
	 * <p>
	 * When used in conjunction with {@link Database#getDocumentByUNID(String)}, this property allows you to get the parent document for any
	 * document.
	 * </p>
	 * <p>
	 * For more information on universal IDs, see the {@link #getUniversalID()}.
	 * </p>
	 *
	 * @return The universal ID of a document's parent, if the document is a response. Returns an empty string if a document doesn't have a
	 *         parent.
	 */
	@Override
	public String getParentDocumentUNID();

	/**
	 * The view from which a document was retrieved, if any.
	 * <p>
	 * If the document was retrieved directly from the database or a document collection, this method returns null.
	 * </p>
	 *
	 * @return The view from which a document was retrieved, if any.
	 */
	@Override
	public View getParentView();

	/**
	 * Returns true if the current document has been read by the current user, otherwise false.
	 */
	@Override
	public boolean getRead();

	/**
	 * Returns true if the current document has been read by given user name, otherwise false.
	 *
	 * @param username
	 *            the username who has or has not read the document
	 * @return True if the current document has been read by username. False if it has not been read by username.
	 */
	@Override
	public boolean getRead(final String username);

	/**
	 * Gets the text values of the <code>Received</code> items in a mail document.
	 * <p>
	 * This method applies to <code>Received</code> items generated from an Internet mail message. The items can be in MIME or Notes format.
	 * </p>
	 * <p>
	 * If the document has no <code>Received</code> items, this method returns a vector of one element whose value is an empty string.
	 * </p>
	 * <p>
	 * A <code>Received</code> item with an incorrect format (not an Internet mail message) throws NotesError.NOTES_ERR_INVALID_RECEIVEDITEM
	 * (4639) "Received Item is not standard format - access with GetItemValue."
	 * </p>
	 *
	 * @return Vector of String elements. The text values of the <code>Received</code> items, one item per element.
	 */
	@Override
	public Vector<String> getReceivedItemText();

	/**
	 * The immediate responses to this document.
	 * <p>
	 * Each document returned is an immediate response to the first document. Responses-to-responses are not included. If the current
	 * document has no responses, the vector contains zero documents.
	 * </p>
	 *
	 * @return The immediate responses to this document.
	 *
	 */
	@Override
	public DocumentCollection getResponses();

	/**
	 * The name of the person who created the signature, if a document is signed.
	 *
	 * @return when this document is signed, returns the name of the person who created the signature. If this document is not signed or if
	 *         the signer is not trusted, returns an empty string
	 */
	@Override
	public String getSigner();

	/**
	 * The size of a document in bytes, which includes the size of any file attachments to the document.
	 */
	@Override
	public int getSize();

	/**
	 * The universal ID, which uniquely identifies a document across all replicas of a database. In character format, the universal ID is a
	 * 32-character combination of hexadecimal digits (0-9, A-F).
	 * <p>
	 * If two documents in replica databases share the same universal ID, the documents are replicas.
	 * </p>
	 * <p>
	 * If you modify the UNID of an existing document, it becomes a new document.
	 * </p>
	 * <p>
	 * Saving a document with the same UNID as an existing document throws NotesError.NOTES_MIN_ERROR_CODE (4000).
	 * </p>
	 *
	 */
	@Override
	public String getUniversalID();

	/**
	 * Returns the Domino URL for the database containing this document
	 */
	@Override
	public String getURL();

	/**
	 * The name of the certificate that verified a signature, if a document is signed.
	 * <p>
	 * This property is an empty string if the document is not signed.
	 * </p>
	 * <p>
	 * This property is an empty string if the signer is not trusted.
	 * </p>
	 *
	 * @return The name of the certificate that verified a signature, if a document is signed.
	 *
	 */
	@Override
	public String getVerifier();

	/**
	 * Indicates whether a document contains one or more embedded objects, object links, or file attachments.
	 * <p>
	 * Note: Embedded objects and object links are not supported for OS/2, UNIX, and the Macintosh. File attachments are.
	 * </p>
	 *
	 * @return true if the document contains one or more embedded objects, object links, or file attachments.
	 */
	@Override
	public boolean hasEmbedded();

	/**
	 * Indicates whether an item exists in the document.
	 *
	 * @param name
	 *            The name of an item.
	 * @return true, if an item with given name exists in this document
	 */
	@Override
	public boolean hasItem(final String name);

	/**
	 * Indicates whether this Document object represents an existing document (not a deletion stub) on an ongoing basis.
	 * <p>
	 * IsDeleted indicates the current state. {@link #isValid()} indicates the initial state.
	 * </p>
	 *
	 * @return true if the document is a deletion stub, false if the document exists
	 *
	 */
	@Override
	public boolean isDeleted();

	/**
	 * Indicates whether a document is encrypted.
	 * <p>
	 * See {@link Document#encrypt()} and {@link Item#isEncrypted()} for additional information.
	 * </p>
	 *
	 * @return true if the document is encrypted
	 *
	 */
	@Override
	public boolean isEncrypted();

	/**
	 * Indicates whether a document is encrypted when mailed.
	 * <p>
	 * To encrypt a document when mailed, this method looks for the public key of each recipient in the Domino Directory. If it cannot find
	 * a recipient's public key, the method sends an unencrypted copy of the document to that recipient. All other recipients receive an
	 * encrypted copy of the document.
	 * </p>
	 * <p>
	 * This property has no effect on whether a document is encrypted when saved to a database.
	 * </p>
	 *
	 * @return true if the document is encrypted when mailed
	 *
	 */
	@Override
	public boolean isEncryptOnSend();

	/**
	 * Indicates whether a document is new. A document is new if it has not been saved.
	 */
	@Override
	public boolean isNewNote();

	/**
	 * Indicates whether Java dates are preferred.
	 *
	 * @return true, if the document prefers Java dates
	 */
	@Override
	public boolean isPreferJavaDates();

	/**
	 * Indicates whether a Document object is a profile document.
	 */
	@Override
	public boolean isProfile();

	/**
	 * Indicates whether a document is a response to another document.
	 */
	@Override
	public boolean isResponse();

	/**
	 * Sets whether a document is saved to a database when mailed. Applies only to new documents that have not yet been saved.
	 * <p>
	 * When SaveMessageOnSend is true, the document is saved just after being mailed.
	 * </p>
	 *
	 * @return true to save the document when mailed
	 *
	 */
	@Override
	public boolean isSaveMessageOnSend();

	/**
	 * Indicates whether a document was mailed by a Domino program.
	 * <p>
	 * In agents that respond to mail messages, you can use this property to make sure that the agent is not responding to mail that was
	 * sent by a program.
	 * </p>
	 * <p>
	 * Documents sent by a program contain an item called <code>$AssistMail</code> with a value that is set to 1.
	 * </p>
	 *
	 * @return true when this document was mailed by a Domino program
	 */
	@Override
	public boolean isSentByAgent();

	/**
	 * Indicates whether a document contains a signature.
	 * <p>
	 * You can get the {@link #getSigner() Signer} and the {@link #getVerifier() Verifier} for a signed document. To access the signature
	 * itself, you must find the item of type SIGNATURE in the document.
	 * </p>
	 *
	 */
	@Override
	public boolean isSigned();

	/**
	 * Indicates whether this document is signed when mailed.
	 *
	 */
	@Override
	public boolean isSignOnSend();

	/**
	 * Indicates whether a Document object represents an existing document (not a deletion stub) initially.
	 * <p>
	 * IsValid is set once and reflects the state of the document when the Document object is created. Use {@link #isDeleted()} to see if a
	 * valid document is deleted while you are working on it.
	 * </p>
	 *
	 */
	@Override
	public boolean isValid();

	/**
	 * Locks a document for the effective user.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>throws an exception if the administration server is not available.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock();

	/**
	 * Locks a document for the effective user.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>places a provisional lock if the administration server is not available and the parameter is true.</li>
	 * <li>throws an exception if the administration server is not available and the parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final boolean provisionalok);

	/**
	 * Locks a document for given user or group.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>throws an exception if the administration server is not available.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final String name);

	/**
	 * Locks a document for given user or group.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>throws an exception if the administration server is not available and the second parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 *
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalok);

	/**
	 * Locks a document for given users or groups.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>throws an exception if the administration server is not available.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 * @return true if the lock is placed, false if not
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/**
	 * Locks a document for given users or groups.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method:
	 * </p>
	 * <ul>
	 * <li>places a persistent lock if the administration (master lock) server is available.</li>
	 * <li>places a provisional lock if the administration server is not available and the second parameter is true.</li>
	 * <li>throws an exception if the administration server is not available and the second parameter is false.</li>
	 * </ul>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The names of the lock holders. Each name must be a user or group. The empty string ("") is not permitted.
	 *
	 * @param provisionalok
	 *            true to permit the placement of a provisional lock
	 * @return true if the lock is placed, false if not
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalok);

	/**
	 * Locks a document provisionally for the effective user.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lockProvisional();

	/**
	 * Locks a document provisionally.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 * @param name
	 *            The name of the lock holder. Must be a user or group. The empty string ("") is not permitted.
	 *
	 * @return true if the lock is placed
	 */
	@Override
	public boolean lockProvisional(final String name);

	/**
	 * Locks a document provisionally.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()} must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * The following actions occur depending on the current lock status:
	 * </p>
	 * <ul>
	 * <li>If the document is not locked, this method places the lock and returns true.</li>
	 * <li>If the document is locked and the current user is one of the lock holders, this method returns true.</li>
	 * <li>If the document is locked and the current user is not one of the lock holders, this method returns false.</li>
	 * </ul>
	 * <p>
	 * If the document is modified by another user before the lock can be placed, this method throws an exception.
	 * </p>
	 *
	 *
	 * @param names
	 *            The names of the lock holders. Each lock holder must be a user or group. The empty string ("") is not permitted.
	 *
	 * @return true if the lock is placed
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/**
	 * Makes one document a response to another. The two documents must be in the same database.
	 * <p>
	 * You must call {@link #save()} after this method if you want to save the change you have made.
	 * </p>
	 *
	 * @param doc
	 *            The document to which this document becomes a response. Cannot be null.
	 */
	@Override
	public void makeResponse(final lotus.domino.Document doc);

	/**
	 * Marks the document as read on behalf of the current user ID.. If the database does not track unread marks, all documents are
	 * considered read, and this method has no effect.
	 */
	@Override
	public void markRead();

	/**
	 * Marks the document as read on behalf of the given name. If the database does not track unread marks, all documents are considered
	 * read, and this method has no effect.
	 *
	 * @param username
	 *            user name for whom to mark this document as read
	 */
	@Override
	public void markRead(final String username);

	/**
	 * Marks the document as unread for the current user. If the database does not track unread marks, all documents are considered read,
	 * and this method has no effect.
	 */
	@Override
	public void markUnread();

	/**
	 * Marks the document as unread for the given user. If the database does not track unread marks, all documents are considered read, and
	 * this method has no effect.
	 *
	 * @param username
	 *            user name for whom to mark this document as unread
	 */
	@Override
	public void markUnread(final String username);

	/**
	 * Adds a document to a folder. If the folder does not exist, it is created.
	 * <p>
	 * If the document is already inside the folder you specify, putInFolder does nothing. If you specify a path to a folder, and none of
	 * the folders exists, the method creates all of them for you. For example: <code>doc.putInFolder( "Vehicles\\Bikes" );</code>
	 * </p>
	 * <p>
	 * If neither Vehicles nor Bikes exists, putInFolder creates both, placing the Bikes folder inside the Vehicles folder.
	 * </p>
	 * <p>
	 * This method cannot add the first document to a folder that is "Shared, Personal on first use."
	 * </p>
	 * <p>
	 * You cannot use this method on an unsaved document. Note that the method throws no error if you attempt to do so.
	 * </p>
	 *
	 * @param name
	 *            The name of the folder in which to place the document. The folder may be personal if the program is running on a
	 *            workstation. If the folder is within another folder, specify a path to it, separating folder names with backslashes. For
	 *            example: "Vehicles\\Bikes"
	 */
	@Override
	public void putInFolder(final String name);

	/**
	 * Adds a document to a folder.
	 * <p>
	 * If the document is already inside the folder you specify, putInFolder does nothing. If you specify a path to a folder, and none of
	 * the folders exists, the method creates all of them for you. For example: <code>doc.putInFolder( "Vehicles\\Bikes" );</code>
	 * </p>
	 * <p>
	 * If neither Vehicles nor Bikes exists, putInFolder creates both, placing the Bikes folder inside the Vehicles folder.
	 * </p>
	 * <p>
	 * This method cannot add the first document to a folder that is "Shared, Personal on first use."
	 * </p>
	 * <p>
	 * You cannot use this method on an unsaved document. Note that the method throws no error if you attempt to do so.
	 * </p>
	 *
	 * @param name
	 *            The name of the folder in which to place the document. The folder may be personal if the program is running on a
	 *            workstation. If the folder is within another folder, specify a path to it, separating folder names with backslashes. For
	 *            example: "Vehicles\\Bikes"
	 * @param createonfail
	 *            If true, creates the folder if it does not exist.
	 */
	@Override
	public void putInFolder(final String name, final boolean createonfail);

	/**
	 * Permanently removes a document from a database, but does a soft deletion if soft deletions are enabled.
	 * <p>
	 * This method does a soft deletion if "Allow soft deletions" is enabled. See {@link #removePermanently(boolean)} to do a hard deletion.
	 * </p>
	 * <p>
	 * A document that is deleted cannot be used as the basis for navigation in a view or document collection.
	 * </p>
	 * <p>
	 * You cannot use the remove method on a Document object returned by {@link AgentContext#getDocumentContext()}.
	 * </p>
	 *
	 * @param force
	 *            If true, the document is removed even if another user modifies the document after your program opens it. If false, the
	 *            document is not removed if another user modifies it.
	 * @return true if the document is successfully removed, false if the document is not deleted because another user modified it and the
	 *         force parameter is set to false
	 */
	@Override
	public boolean remove(final boolean force);

	/**
	 * Removes a document from a folder.
	 * <p>
	 * The method does nothing if the document is not in the folder you specify, or if the folder you specify does not exist.
	 * </p>
	 *
	 * @param name
	 *            The name of the folder from which to remove the document. The folder may be personal if the program is running on a
	 *            workstation. If the folder is within another folder, specify a path to it, separating folder names with backslashes. For
	 *            example: "Vehicles\\Bikes"
	 */
	@Override
	public void removeFromFolder(final String name);

	/**
	 * Removes an item from a document.
	 * <p>
	 * You can achieve the same result with {@link Item#remove()}.
	 * </p>
	 * <p>
	 * To keep the changes to the document, you must call {@link #save()} after removing the item.
	 * </p>
	 *
	 * @param name
	 *            The name of the item to remove from the document. If more than one item has the specified name, all items with this name
	 *            are removed. If there is no item with the specified name, the method does nothing.
	 */
	@Override
	public void removeItem(final String name);

	/**
	 * Permanently deletes a document from a database, doing a hard deletion even if soft deletions are enabled.
	 * <p>
	 * This method does a hard deletion even if "Allow soft deletions" is enabled. See {@link #remove(boolean)} to do a soft deletion.
	 * </p>
	 * <p>
	 * A deleted document cannot be used as a basis for navigation in a view or a document collection.
	 * </p>
	 * <p>
	 * You cannot use the remove method on a Document object returned by the {@link AgentContext#getDocumentContext()} property.
	 * </p>
	 *
	 * @param force
	 *            If true, the document is deleted even if another user modifies the document after the program opens it. If false, the
	 *            document is not deleted if another user modifies it.
	 * @return true if the document is successfully deleted, false if the document is not deleted, because another user modified it and the
	 *         force parameter is set to false
	 */
	@Override
	public boolean removePermanently(final boolean force);

	/**
	 * Creates a picture of this document and places it into a rich-text item that you specify.
	 * <p>
	 * The picture is created using both the document and its form. Therefore, the input translation and validation formulas of the form are
	 * executed.
	 * </p>
	 * <p>
	 * If the target rich text item is in a new document, you must save the document before calling this method.
	 * </p>
	 *
	 * @param rtitem
	 *            The destination for the picture. Cannot be null.
	 * @return If true, the method is successful. If false, the method is not successful and the rich text item remains unchanged. This can
	 *         happen if an input validation formula fails on the document form.
	 */
	@Override
	public boolean renderToRTItem(final lotus.domino.RichTextItem rtitem);

	/**
	 * Replaces all items of the specified name with one new item, which is assigned the specified value. If the document does not contain
	 * an item with the specified name, this method creates a new item and adds it to the document.
	 * <p>
	 * To keep the changes, you must call save after calling replaceItemValue.
	 * </p>
	 * The {@link Item#isSummary()} property of the new item defaults to true, which means that the item value can be displayed in a view or
	 * folder.
	 * </p>
	 * <p>
	 * Do not use this method to replace the value of a rich text item (or MIME entity) unless you want to change it to a text item. To
	 * replace the contents of a rich text item, use {@link Document#removeItem(String)} or {@link RichTextItem#remove()} to delete the old
	 * item, {@link Document#createRichTextItem(String)} to create a new one with the same name, and {@link RichTextItem#appendText(String)}
	 * and other methods to provide the new content.
	 * </p>
	 * <p>
	 * The resulting item type depends on the value:
	 * </p>
	 * <table cellpadding="4" cellspacing="0" summary="" class="table" rules="all" frame="border" border="1">
	 * <thead align="left" valign="bottom">
	 * <tr>
	 * <th align="left" valign="top" width="NaN%" id="d466934e95">Data type of value</th>
	 * <th align="left" valign="top" width="NaN%" id="d466934e100">Resulting item</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">String</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Text</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Integer</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Number</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Double</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Number</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">DateTime</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Date-time item</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">java.util.Vector with String, Integer, Double, or DateTime elements
	 * </td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Multi-value text, number, or date-time item</td>
	 * </tr>
	 * <tr >
	 * <td align="left" valign="top" width="NaN%" headers="d466934e95 ">Item</td>
	 * <td align="left" valign="top" width="NaN%" headers="d466934e100 ">Same data type as the Item</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param itemName
	 *            The name of the item or items you want to replace.
	 * @param value
	 *            The value of the new item. The data type of the item depends upon the data type of value, and does not need to match the
	 *            data type of the old item.
	 * @return The new item, which replaces all previous items with the same name.
	 *
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value);

	/**
	 * Replaces all items of the specified name with one new item, which is assigned custom data from an object. If the document does not
	 * contain an item with the specified name, this method creates a new item and adds it to the document.
	 * <p>
	 * The new item replaces all previous items with the same name.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link #save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64 KB.
	 * </p>
	 * <p>
	 * If you intend to get the custom data through a language binding other than Java, use a "Bytes" method.
	 * </p>
	 * <p>
	 * For methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link #getItemValueCustomData(String)}</li>
	 * <li>{@link #getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * For other methods that write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param userObj
	 *            An object that contains the custom data. The class that defines this object must implement Serializable. If desired, you
	 *            can override <code>readObject</code> and <code>writeObject</code>.
	 * @return The new item, which replaces all previous items with the same name.
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final Object userObj) throws IOException;

	/**
	 * Replaces all items of the specified name with one new item, which is assigned custom data from an object. If the document does not
	 * contain an item with the specified name, this method creates a new item and adds it to the document.
	 * <p>
	 * The new item replaces all previous items with the same name.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link #save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64 KB.
	 * </p>
	 * <p>
	 * If you intend to get the custom data through a language binding other than Java, use a "Bytes" method.
	 * </p>
	 * <p>
	 * For methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link #getItemValueCustomData(String)}</li>
	 * <li>{@link #getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * For other methods that write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param dataTypeName
	 *            A name for the data type. When getting custom data, use this name for verification.
	 * @param userObj
	 *            An object that contains the custom data. The class that defines this object must implement Serializable. If desired, you
	 *            can override <code>readObject</code> and <code>writeObject</code>.
	 * @return The new item, which replaces all previous items with the same name.
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final String dataTypeName, final Object userObj) throws IOException;

	/**
	 * Replaces all items of the specified name with one new item, which is assigned custom data from a byte array. If the document does not
	 * contain an item with the specified name, this method creates a new item and adds it to the document.
	 * <p>
	 * The new item replaces all previous items with the same name.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link #save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64 KB.
	 * </p>
	 * <p>
	 * Use this method if you intend to get the custom data through a language binding other than Java.
	 * </p>
	 * <p>
	 * For methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link #getItemValueCustomData(String)}</li>
	 * <li>{@link #getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link Item#getValueCustomData()}</li>
	 * <li>{@link Item#getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * For other methods that write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link #replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Item#setValueCustomData(Object)}</li>
	 * <li>{@link Item#setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param itemName
	 *            The name of the item.
	 * @param dataTypeName
	 *            A name for the data type. When getting custom data, use this name for verification.
	 * @param byteArray
	 *            A byte array that contains the custom data.
	 * @return The new item, which replaces all previous items with the same name.
	 */
	@Override
	public Item replaceItemValueCustomDataBytes(final String itemName, final String dataTypeName, final byte[] byteArray)
			throws IOException;

	/**
	 * Saves any changes you have made to a document.
	 *
	 * @return true if the document is successfully saved, false if not
	 */
	@Override
	public boolean save();

	/**
	 * Saves any changes you have made to a document.
	 *
	 * @param force
	 *            If true, the document is saved even if someone else edits and saves the document while the program is running. The last
	 *            version of the document that was saved wins; the earlier version is discarded.
	 * @return true if the document is successfully saved, false if not
	 */
	@Override
	public boolean save(final boolean force);

	/**
	 * Saves any changes you have made to a document.
	 *
	 * @param force
	 *            If true, the document is saved even if someone else edits and saves the document while the program is running. The last
	 *            version of the document that was saved wins; the earlier version is discarded. If false, and someone else edits the
	 *            document while the program is running, the makeresponse argument determines what happens. If the database does not track
	 *            unread marks, all documents are considered read, and this parameter has no effect.
	 * @param makeresponse
	 *            If true, the current document becomes a response to the original document (this is what the replicator does when there's a
	 *            replication conflict). If false, the save is canceled. If the force parameter is true, the makeresponse parameter has no
	 *            effect.
	 * @return true if the document is successfully saved, false if not
	 */
	@Override
	public boolean save(final boolean force, final boolean makeresponse);

	/**
	 * Saves any changes you have made to a document.
	 *
	 * @param force
	 *            If true, the document is saved even if someone else edits and saves the document while the program is running. The last
	 *            version of the document that was saved wins; the earlier version is discarded. If false, and someone else edits the
	 *            document while the program is running, the makeresponse argument determines what happens. If the database does not track
	 *            unread marks, all documents are considered read, and this parameter has no effect.
	 * @param makeresponse
	 *            If true, the current document becomes a response to the original document (this is what the replicator does when there's a
	 *            replication conflict). If false, the save is canceled. If the force parameter is true, the makeresponse parameter has no
	 *            effect.
	 * @param markread
	 *            If true, the document is marked as read on behalf of the current user ID. If false, the document is not marked as read.
	 * @return true if the document is successfully saved, false if not
	 */
	@Override
	public boolean save(final boolean force, final boolean makeresponse, final boolean markread);

	/**
	 * Mails a document to recipients listed in the <code>SendTo</code> item.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 */
	@Override
	public void send();

	/**
	 * Mails a document to recipients listed in the <code>SendTo</code> item.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * Sending the form increases the size of the document, but ensures that the recipient can see all of the items on the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 * @param attachform
	 *            If true, the form is stored and sent along with the document. If false, it isn't. Do not attach a form that uses computed
	 *            subforms.
	 */
	@Override
	public void send(final boolean attachform);

	/**
	 * Mails a document.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * Sending the form increases the size of the document, but ensures that the recipient can see all of the items on the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 * @param attachform
	 *            If true, the form is stored and sent along with the document. If false, it isn't. Do not attach a form that uses computed
	 *            subforms.
	 * @param recipient
	 *            The recipient of the document.
	 */
	@Override
	public void send(final boolean attachform, final String recipient);

	/**
	 * Mails a document.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * Sending the form increases the size of the document, but ensures that the recipient can see all of the items on the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 * @param attachform
	 *            If true, the form is stored and sent along with the document. If false, it isn't. Do not attach a form that uses computed
	 *            subforms.
	 * @param recipients
	 *            Vector of String elements. The recipients of the document.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final boolean attachform, final Vector recipients);

	/**
	 * Mails a document.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 * @param recipient
	 *            The recipient of the document.
	 */
	@Override
	public void send(final String recipient);

	/**
	 * Mails a document.
	 * <p>
	 * If you have only Reader access to a database, you can run an agent that creates and sends a document, but the agent will not work if
	 * you attach a file to that document.
	 * </p>
	 * <p>
	 * Two kinds of items can affect the mailing of the document when you use send:
	 * </p>
	 * <ul>
	 * <li>If the document contains additional recipient items, such as <code>CopyTo</code> or <code>BlindCopyTo</code>, the document is
	 * mailed to those recipients.</li>
	 * <li>If the document contains items to control the routing of mail, such as <code>DeliveryPriority</code>,
	 * <code>DeliveryReport</code>, or <code>ReturnReceipt</code>, they are used when sending the document.</li>
	 * </ul>
	 * <p>
	 * The {@link #isSaveMessageOnSend()} property controls whether the sent document is saved in the database. If
	 * {@link #isSaveMessageOnSend()} is true and you attach the form to the document, the form is saved with the document.
	 * </p>
	 * <p>
	 * The send method automatically creates an item called <code>$AssistMail</code> on the sent document. The <code>SentByAgent</code>
	 * property uses this item to determine if a document was mailed by an agent.
	 * </p>
	 * <p>
	 * If a program runs on a workstation, the mailed document contains the current user's name in the <code>From</code> item. If a program
	 * runs as an agent on a server, the mailed document contains the server's name in the <code>From</code> item.
	 * </p>
	 *
	 * @param recipients
	 *            Vector of String elements. The recipients of the document.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final Vector recipients);

	/**
	 * Sets the key(s) used to encrypt a document. The {@link #encrypt()} method uses these keys when it encrypts the document.
	 * <p>
	 * Each element in EncryptionKeys contains the name of an encryption key that you want to use to encrypt the document. The document can
	 * be decrypted by any user who posesses one of the keys. If there are no encryption keys specified for a document, the document is
	 * encrypted with the current user's public key and can only be decrypted by that user.
	 * </p>
	 * <p>
	 * You must call the {@link #encrypt()} and {@link #save()} methods to actually encrypt the document. Since encryption works differently
	 * when a document is mailed, the EncryptionKeys property has no effect when a document is encrypted when mailed.
	 * </p>
	 * <p>
	 * The name of each encryption key in a document is stored in a text item called <code>SecretEncryptionKeys</code>. This property
	 * returns the contents of the item.
	 * </p>
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setEncryptionKeys(final Vector keys);

	/**
	 * Sets whether a document is encrypted when mailed.
	 * <p>
	 * To encrypt a document when mailed, this method looks for the public key of each recipient in the Domino Directory. If it cannot find
	 * a recipient's public key, the method sends an unencrypted copy of the document to that recipient. All other recipients receive an
	 * encrypted copy of the document.
	 * </p>
	 * <p>
	 * This property has no effect on whether a document is encrypted when saved to a database.
	 * </p>
	 *
	 * @param flag
	 *            specify true if the document should be encrypted when maile
	 */
	@Override
	public void setEncryptOnSend(final boolean flag);

	/**
	 * Sets whether Java dates are preferred.
	 *
	 * @param flag
	 *            specify true if the document prefers Java dates
	 */
	@Override
	public void setPreferJavaDates(final boolean flag);

	/**
	 * Sets whether a document is saved to a database when mailed. Applies only to new documents that have not yet been saved.
	 *
	 * @param flag
	 *            specify true if the document should be saved just after being mailed.
	 */
	@Override
	public void setSaveMessageOnSend(final boolean flag);

	/**
	 * Sets whether a document is signed when mailed.
	 *
	 * @param flag
	 *            specify true if the document should be signed when mailed
	 */
	@Override
	public void setSignOnSend(final boolean flag);

	/**
	 * Sets the universal ID, which uniquely identifies a document across all replicas of a database. In character format, the universal ID
	 * is a 32-character combination of hexadecimal digits (0-9, A-F).
	 * <p>
	 * The universal ID is also known as the unique ID or UNID.
	 * </p>
	 * <p>
	 * If two documents in replica databases share the same universal ID, the documents are replicas.
	 * </p>
	 * <p>
	 * If you modify the UNID of an existing document, it becomes a new document.
	 * </p>
	 * <p>
	 * Saving a document with the same UNID as an existing document throws NotesError.NOTES_MIN_ERROR_CODE (4000).
	 * </p>
	 *
	 * @param unid
	 *            the new universal ID for this document
	 */
	@Override
	public void setUniversalID(final String unid);

	/**
	 * Signs a document.
	 * <p>
	 * If you want the signature to be saved, you must call the {@link #save()} method after signing the document.
	 * </p>
	 * <p>
	 * If the script is running on a server, the signer must be listed in "Run unrestricted methods and operations" under the Security tab
	 * in the Server record of the Domino Directory. Otherwise the following error occurs: NOTES_ERR_SIGN_NOPERM (4165) "You must have
	 * permission to sign documents for server based agents."
	 * </p>
	 */
	@Override
	public void sign();

	/**
	 * Unlocks a document.
	 * <p>
	 * {@link Database#isDocumentLockingEnabled()}must be true or this method throws an exception.
	 * </p>
	 * <p>
	 * This method throws an exception if the current user is not one of the lock holders and does not have lock breaking authority.
	 * </p>
	 */
	@Override
	public void unlock();

	/**
	 * Encrypts a document.
	 * <p>
	 * The encrypted document is not saved until you call {@link #save()}. Only the items for which {@link #isEncrypted()} is true are
	 * encrypted. Items for which {@link #isEncrypted()} is false remain visible to any user, even if the user does not have the proper
	 * encryption key.
	 * </p>
	 * <p>
	 * If the {@link #getEncryptionKeys() EncryptionKeys} property is set with one or more named keys, those keys are used to encrypt the
	 * document. Any user who has one of the encryption keys can decrypt the document. If there are no encryption keys specified, the
	 * document is encrypted with the user's public key, in which case only the user who encrypted the document can decrypt it.
	 * </p>
	 * <p>
	 * If the program is running on a server, it must have permission to encrypt.
	 * </p>
	 * <p>
	 * Since mail encryption works differently, do not use this method if you want to mail an encrypted document. Instead, set the
	 * {@link #setEncryptOnSend(boolean) EncryptOnSend} property to true, and use the {@link #send()} method.
	 * </p>
	 * <h5>Usage with XPages</h5> This method can also be used for encrypting XPages. To encrypt a document with a private key on web or
	 * server use the following general code steps:
	 * <ul>
	 * <li>Fetch the {@link IDVault} object</li>
	 * <li>Fetch a UserID object out of the IDVault for a particular user with the {@link IDVault#getUserIDFile(String, String, String)}
	 * method.</li>
	 * <li>Get a list of private key names that are available to use with the UserID with the {@link UserID#getEncryptionKeys()}
	 * method.</li>
	 * <li>Specify which key to use to encrypt the doc with the {@link #setEncryptionKeys(Vector)} method.</li>
	 * <li>Use the encrypt method to encrypt note with the specified encryption key set with {@link #setEncryptionKeys(Vector)}</li>
	 * </ul>
	 *
	 * @param userId
	 *            After setting the User id, documents in this database will be decrypted with encryption keys of this user id
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void encrypt(UserID userId);

	/**
	 * Encrypts a document.
	 * <p>
	 * The encrypted document is not saved until you call {@link #save()}. Only the items for which {@link #isEncrypted()} is true are
	 * encrypted. Items for which {@link #isEncrypted()} is false remain visible to any user, even if the user does not have the proper
	 * encryption key.
	 * </p>
	 * <p>
	 * If the {@link #getEncryptionKeys() EncryptionKeys} property is set with one or more named keys, those keys are used to encrypt the
	 * document. Any user who has one of the encryption keys can decrypt the document. If there are no encryption keys specified, the
	 * document is encrypted with the user's public key, in which case only the user who encrypted the document can decrypt it.
	 * </p>
	 * <p>
	 * If the program is running on a server, it must have permission to encrypt.
	 * </p>
	 * <p>
	 * Since mail encryption works differently, do not use this method if you want to mail an encrypted document. Instead, set the
	 * {@link #setEncryptOnSend(boolean) EncryptOnSend} property to true, and use the {@link #send()} method.
	 * </p>
	 * <h5>Usage with XPages</h5> This method can also be used for encrypting XPages. To encrypt a document with a private key on web or
	 * server use the following general code steps:
	 * <ul>
	 * <li>Fetch the {@link IDVault} object</li>
	 * <li>Fetch a UserID object out of the IDVault for a particular user with the {@link IDVault#getUserIDFile(String, String, String)}
	 * method.</li>
	 * <li>Get a list of private key names that are available to use with the UserID with the {@link UserID#getEncryptionKeys()}
	 * method.</li>
	 * <li>Specify which key to use to encrypt the doc with the {@link #setEncryptionKeys(Vector)} method.</li>
	 * <li>Use the encrypt method to encrypt note with the specified encryption key set with {@link #setEncryptionKeys(Vector)}</li>
	 * </ul>
	 *
	 * @param filePath
	 *            File path of an id file. After setting it, all documents in this database will be decrypted with encryption keys of this
	 *            id file.
	 * @param password
	 *            password
	 * @since Domino 9.0.1 FP8
	 */
	@Override
	public void encrypt(String filePath, String password);

	/**
	 * Whether send method should be aborted
	 *
	 * @return cancel sending
	 * @since 10.0.0
	 */
	@Override
	public boolean isCancelSendOnMissingKey();

	/**
	 * Whether or not send method should be aborted
	 * @param cancelSend to cancel sending
	 * @since 10.0.0
	 */
	@Override
	public void setCancelSendOnMissingKey(final boolean cancelSend);

	/**
	 * Retrieves the name of the document, if it is a named document.
	 * 
	 * @return the name of the document, or an empty value if it is
	 *         not named
	 * @since 12.0.1
	 */
	@Override
	String getNameOfDoc();
	
	/**
	 * Retrieves the user name of the document, if it is a named document
	 * with an associated user name.
	 * 
	 * @return the user name of the document, or an empty value if it is
	 *         not named or does not have an associated user name
	 * @since 12.0.1
	 */
	@Override
	String getUserNameOfDoc();
	
	/**
	 * Determines whether the document is a named document. This differs from
	 * the profile-document mechanism.
	 * 
	 * @return {@code true} if the document is a named document; {@code false}
	 *         otherwise
	 * @since 12.0.1
	 */
	@Override
	boolean isNamedDoc();
}
