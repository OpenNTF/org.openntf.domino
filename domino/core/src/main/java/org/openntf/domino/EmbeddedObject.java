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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.XSLTResultTarget;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.xml.sax.InputSource;

/**
 * Represents an embedded object, an object link or a file attachment.
 * <p>
 * Note: Embedded objects and object links are not supported for OS/2, UNIX, and the Macintosh. File attachments are.
 * </p>
 * <h3>Creation</h3>
 * <p>
 * To create a new object, object link, or file attachment, use {@link RichTextItem#embedObject(int, String, String, String)}.
 * </p>
 * <h3>Access</h3>
 * <p>
 * To access an existing object, object link, or file attachment:
 * </p>
 * <ul>
 * <li>To access an object, object link, or attachment when you know its name and the rich text item that contains it, use
 * {@link RichTextItem#getEmbeddedObject(String)}.</li>
 * <li>To access all the objects, object links, and attachments in a particular rich text item, use
 * {@link RichTextItem#getEmbeddedObjects()}.</li>
 * <li>To access the objects and object links in a particular document, including those that are not contained within a particular rich text
 * item, use {@link Document#getEmbeddedObjects()}. This property does not return file attachments or objects and object links created in
 * Notes Release 3.</li>
 * </ul>
 */
public interface EmbeddedObject
		extends Base<lotus.domino.EmbeddedObject>, lotus.domino.EmbeddedObject, org.openntf.domino.ext.EmbeddedObject, DocumentDescendant {
	public static enum Type {

		EMBED_ATTACHMENT(lotus.domino.EmbeddedObject.EMBED_ATTACHMENT), EMBED_OBJECT(lotus.domino.EmbeddedObject.EMBED_OBJECT);

		public static Type valueOf(final int value) {
			for (Type level : Type.values()) {
				if (level.getValue() == value) {
					return level;
				}
			}
			return null;
		}

		private final int value_;

		private Type(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	public static class Schema extends FactorySchema<EmbeddedObject, lotus.domino.EmbeddedObject, Document> {
		@Override
		public Class<EmbeddedObject> typeClass() {
			return EmbeddedObject.class;
		}

		@Override
		public Class<lotus.domino.EmbeddedObject> delegateClass() {
			return lotus.domino.EmbeddedObject.class;
		}

		@Override
		public Class<Document> parentClass() {
			return Document.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Causes an embedded object or object link to be loaded by OLE.
	 * <p>
	 * Agents running on a server must set the <em>flag</em> parameter to false.
	 * </p>
	 * <p>
	 * This method throws an exception if you invoke it on an <code>EmbeddedObject</code> that is a file attachment.
	 * </p>
	 * <p>
	 * This method may or may not return a valid OLE handle for object links, depending upon the application used to create the object link.
	 * </p>
	 *
	 * @param flag
	 *            If true, the OLE server application displays its user interface. If false, the server application hides its user
	 *            interface.
	 * @return int OLE handle
	 */
	@Override
	public int activate(final boolean flag);

	/**
	 * Executes a verb in an embedded object.
	 * <p>
	 * Verbs which require user interaction, such as Open or Edit, are not supported.
	 * </p>
	 *
	 * @param verb
	 *            The name of one of the object's verbs.
	 * @see #getVerbs() getVerbs() for a list of supported verbs
	 */
	@Override
	@Deprecated
	public void doVerb(final String verb);

	/**
	 * Writes a file attachment to storage.
	 * <p>
	 * For embedded objects and object links, this method throws an exception.
	 * </p>
	 *
	 * @param path
	 *            The path and file name where you want to store the extracted file.
	 */
	@Override
	public void extractFile(final String path);

	/**
	 * The name of the application that created an object.
	 *
	 * @return The name of the application that created an object or an empty string if this embedded object is a file attachment
	 *
	 */
	@Override
	public String getClassName();

	/**
	 * The size of a file attachment, in bytes.
	 *
	 * @return The size of a file attachment, in bytes or 0 for embedded objects and links.
	 *
	 */
	@Override
	public int getFileSize();

	/**
	 * The date and time the file was created
	 */
	@Override
	public DateTime getFileCreated();

	/**
	 * The date and time the file was last modified.
	 */
	@Override
	public DateTime getFileModified();

	/**
	 * SAX InputSource representation of the contents of an EmbeddedObject, Item, or MIMEEntity object.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 * <p>
	 * For applets that run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML
	 * parser. To use the XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or
	 * LotusXSL.cab file with the applet.
	 * </p>
	 * <p>
	 * This method creates a temporary file. The file is deleted when <code>EmbeddedObject</code> is recycled.
	 * </p>
	 *
	 * @return SAX InputSource representation of the contents of an EmbeddedObject, Item, or MIMEEntity object.
	 *
	 */
	@Override
	public InputSource getInputSource();

	/**
	 * InputStream representation of the contents of an EmbeddedObject, Item, or MIMEEntity object.
	 * <p>
	 * This method creates a temporary file. The file is deleted when the EmbeddedObject is recycled, but only if the close() method of the
	 * InputStream object returned by this property has been called before the EmbeddedObject is recycled. Failure to explicitly close the
	 * InputStream object will result in the temporary file remaining in the filesystem, which can cause the system to run out of disk space
	 * when running an agent that processes many EmbeddedObjects.
	 * </p>
	 *
	 * @return InputStream representation of the contents of an EmbeddedObject, Item, or MIMEEntity object.
	 *
	 */
	@Override
	public InputStream getInputStream();

	/**
	 * The name used to reference an object, object link, or file attachment.
	 * <p>
	 * If an object or object link does not have a name, this property returns an empty string.
	 * </p>
	 * <p>
	 * If an object or object link was created using {@link RichTextItem#embedObject(int, String, String, String)}, this property returns
	 * the <em>name</em> parameter specification. If the <em>name</em> parameter was not specified, this property is the same as the
	 * {@link #getSource() Source} property</a>.
	 * </p>
	 * <p>
	 * If the embedded object or object link was created using the user interface, this property returns the name of the object as it
	 * appears in the user interface.
	 * </p>
	 * <p>
	 * For file attachments, this property returns the name of the file unless the attachment is a duplicate. If the attachment is a
	 * duplicate, this property returns an internal name. (The {@link #getSource() Source} property returns the file name in all cases).
	 * </p>
	 *
	 * @return The name used to reference an object, object link, or file attachment.
	 */
	@Override
	public String getName();

	/**
	 * If an embedded object has been loaded into memory, returns the OLE handle (IUnknown or IDispatch handle). If the OLE object supports
	 * OLE Automation, you can invoke the methods and properties of the object using the handle.
	 * <p>
	 * This property may or may not return a valid OLE handle for object links, depending upon the application used to create the object
	 * link.
	 * </p>
	 *
	 * @return OLE Handle if the object has been loaded into memory
	 *
	 */
	@Override
	@Deprecated
	public int getObject();

	/**
	 * The rich text item that holds an object.
	 * <p>
	 * When you get an embedded object from a document, it does not contain a RichTextItem parent. Calling the Parent property on such an
	 * embedded object returns null.
	 * </p>
	 *
	 * @return The rich text item that holds an object
	 *
	 */
	@Override
	public RichTextItem getParent();

	/**
	 * Gets the parent document.
	 *
	 * @return the parent document
	 */
	public Document getParentDocument();

	/**
	 * Contents of an EmbeddedObject, Item, or MIMEEntity object in the form of a java.io.Reader object.
	 * <p>
	 * This method creates a temporary file. The file is deleted when EmbeddedObject is recycled.
	 * </p>
	 *
	 * @return Contents of an EmbeddedObject, Item, or MIMEEntity object
	 *
	 */
	@Override
	public Reader getReader();

	/**
	 * For an object or object link, returns the internal name for the source document. For a file attachment, returns the file name of the
	 * original file.
	 *
	 */
	@Override
	public String getSource();

	/**
	 * Indicates whether an embedded object is an object, an object link, or a file attachment.
	 *
	 * @return One of the values: <ul
	 *         <li>EmbeddedObject.EMBED_ATTACHMENT</li>
	 *         <li>EmbeddedObject.EMBED_OBJECT</li>
	 *         <li>EmbeddedObject.EMBED_OBJECTLINK</li>
	 *         </ul>
	 */
	@Override
	public int getType();

	/**
	 * The verbs that an object supports, if the object is an OLE/2 embedded object.
	 * <p>
	 * Throws an exception if not invoked from an OLE/2 embedded object.
	 * </p>
	 *
	 * @return The verbs that an object supports, if the object is an OLE/2 embedded object.
	 *
	 */
	@Override
	public Vector<String> getVerbs();

	/**
	 * Parses the contents of an attachment and creates the DOM tree of the XML.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 * <p>
	 * For applets that run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML
	 * parser. To use the XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or
	 * LotusXSL.cab file with the applet.
	 * </p>
	 * <p>
	 * Errors generated during parsing are directed to System.err.
	 * </p>
	 * <p>
	 * If a stream of XML contains relative or partial URLs, the <code>parseXML</code> method or <code>transformXML</code> method resolves
	 * the partial URL as a Page on the database where the InputStream originated. For example, when the <code>parseXML</code> or
	 * <code>transformXML</code> method encounters the XML stream &lt;!DOCTYPE software-release-note SYSTEM "readme.dtd"&gt;, it looks for a
	 * Page named "readme.dtd" in the database where the source stream originated.
	 * </p>
	 * <p>
	 * This method creates a temporary file. The file is deleted when EmbeddedObject is recycled.
	 * </p>
	 *
	 * @param validate
	 *            Specify true to use the Validating DOMParser and false to use the Non-Validating DOMParser.
	 * @return The DOM tree object.
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException;

	/**
	 * Removes an object, object link, or file attachment.
	 * <p>
	 * After calling remove, you must call {@link Document#save()} keep the change.
	 * </p>
	 *
	 */
	@Override
	public void remove();

	/**
	 * Transforms the contents of an attachment using the specified Domino EmbeddedObject, Item, MIMEEntity, or RichTextItem style, or any
	 * InputSource style, and provides the results to the specified XSLTResultTarget object.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 * <p>
	 * For applets that run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML
	 * parser. To use the XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or
	 * LotusXSL.cab file with the applet.
	 * </p>
	 * <p>
	 * Errors generated during transformation are directed to System.err.
	 * </p>
	 * <p>
	 * If a stream of XML contains relative or partial URLs, the <code>parseXML</code> method or <code>transformXML</code> method resolves
	 * the partial URL as a Page on the database where the InputStream originated. For example, when the <code>parseXML</code> or
	 * <code>transformXML</code> method encounters the XML stream &lt;!DOCTYPE software-release-note SYSTEM "readme.dtd"&gt;, it looks for a
	 * Page named "readme.dtd" in the database where the source stream originated.
	 * </p>
	 * <p>
	 * This method creates a temporary file. The file is deleted when EmbeddedObject is recycled.
	 * </p>
	 *
	 * @param style
	 *            The stylesheet source that you use to transform the XML data.
	 * @param result
	 *            The object that receives the transformed data.
	 */
	@Override
	public void transformXML(final Object style, final XSLTResultTarget result);

	/* (non-Javadoc)
	 * @see lotus.domino.EmbeddedObject#getFileEncoding()
	 */
	@Override
	public int getFileEncoding();

}
