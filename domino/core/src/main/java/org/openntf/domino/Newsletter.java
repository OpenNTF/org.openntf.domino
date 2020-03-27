/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Document or set of documents that contains information from, or links to, several other documents.
 * <h3>Creation</h3>
 * <p>
 * To create a new <code>Newsletter</code>, use a <code>DocumentCollection</code> object containing the documents you want with
 * {@link Session#createNewsletter(lotus.domino.DocumentCollection)}.
 * <h3>Usage</h3>
 * <p>
 * Once you create a newsletter, you can:
 * </p>
 * <ul>
 * <li>Use {@link #formatDocument(lotus.domino.Database, int)} to create a new document with a rendering of one of the documents in the
 * collection.</li>
 * <li>Use {@link #formatMsgWithDoclinks(lotus.domino.Database)} doclinks to create a new document with links to each of the documents in
 * the collection.</li>
 * </ul>
 * </p>
 */
public interface Newsletter
		extends Base<lotus.domino.Newsletter>, lotus.domino.Newsletter, org.openntf.domino.ext.Newsletter, SessionDescendant {

	public static class Schema extends FactorySchema<Newsletter, lotus.domino.Newsletter, Session> {
		@Override
		public Class<Newsletter> typeClass() {
			return Newsletter.class;
		}

		@Override
		public Class<lotus.domino.Newsletter> delegateClass() {
			return lotus.domino.Newsletter.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Creates a document containing a rendering of a document in the newsletter collection. This is similar to forwarding a document, which
	 * displays a rendering of the forwarded document.
	 * <p>
	 * If you want to save the rendered document, you must explicitly call the {@link Document#save()} method.
	 * </p>
	 * <p>
	 * If you want to mail the rendered document, you must explicitly call the {@link Document#send()} method.
	 * </p>
	 * <p>
	 * The target database must have a default view or the following occurs: "Notes error: Special database object cannot be located."
	 * </p>
	 *
	 * @param database
	 *            The database in which to create the newsletter document. If you specify this parameter as null, the document is created in
	 *            the user's default mail database.
	 * @param index
	 *            A number indicating the document to render. Use 1 to indicate the first document in the collection, 2 to indicate the
	 *            second document, and so on.
	 * @return A document in db, containing a rendering of the nth document in the collection. The rendering is placed into the Body item.
	 */
	@Override
	public lotus.domino.Document formatDocument(final lotus.domino.Database database, final int index);

	/**
	 * Creates a document that contains a link to each document in the newsletter collection.
	 * <p>
	 * The Body item of the returned document contains the following:
	 * </p>
	 * <ul>
	 * <li>The file name of the database that contains the documents in the newsletter collection.</li>
	 * <li>A link to each document in the newsletter collection.</li>
	 * <li>If the collection is sorted and the DoScore property is true, the relevance score of each document. You must set this property
	 * before calling this method.</li>
	 * <li>If the IsDoSubject property is true and the SubjectItemName property has a value, a title for each document. You must set this
	 * property before calling this method.</li>
	 * <li>The query that produced the newsletter collection.</li>
	 * </ul>
	 * <p>
	 * If you want to save the newsletter, you must explicitly call the {@link Document#save()} method.
	 * </p>
	 * <p>
	 * If you want to mail the newsletter, you must explicitly call the {@link Document#send()} method.
	 * </p>
	 * <p>
	 * The target database must have a default view or the following occurs: "Notes error: Special database object cannot be located."
	 * </p>
	 *
	 *
	 * @param db
	 *            The database in which to create the newsletter document. If you specify this parameter as null, the document is created in
	 *            the user's default mail database.
	 *
	 * @return A document that contains a link to each document in the newsletter collection.
	 */
	@Override
	public lotus.domino.Document formatMsgWithDoclinks(final lotus.domino.Database database);

	/**
	 * The Domino session that contains a Newsletter object.
	 *
	 * @return The DominoÂ® session that contains a Newsletter object.
	 */
	@Override
	public Session getParent();

	/**
	 * For a newsletter document created using the {@link #formatMsgWithDoclinks(lotus.domino.Database)} method, indicates the name of the
	 * item on the documents of a newsletter that contains the text you want to use as a subject line.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)} method.
	 * </p>
	 * <p>
	 * This property must be used in conjunction with the {@link #isDoSubject()} property</a>. For example, if the IsDoSubject property is
	 * true and SubjectItemName is "Subject," then the newsletter contains the contents of each document Subject item next to the document
	 * link. If the IsDoSubject property is false, the SubjectItemName property has no effect.
	 * </p>
	 * <p>
	 * You must set both <code>SubjectItemName</code> and <code>IsDoSubject</code> before calling <code>formatMsgWithDoclinks</code>.
	 * </p>
	 *
	 * @return For a newsletter document created using the formatMsgWithDoclinks method, indicates the name of the item on the documents of
	 *         a newsletter that contains the text you want to use as a subject line.
	 */
	@Override
	public String getSubjectItemName();

	/**
	 * For a newsletter document created using the formatMsgWithDoclinks method, indicates if the newsletter includes the relevance score
	 * for each document.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)}
	 * method</a>.
	 * </p>
	 * <p>
	 * This property applies only to newsletters with sorted collections; for example, a collection produced by a call to the
	 * {@link Database#FTSearch(String)} method</a>. If a newsletter collection is unsorted, this property has no effect.
	 * </p>
	 *
	 * @return True if the newsletter includes the relevance score for each document.
	 *
	 */
	@Override
	public boolean isDoScore();

	/**
	 * For a newsletter document created using the formatMsgWithDoclinks method, indicates if the newsletter includes a string describing
	 * the subject of each document.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)}
	 * method</a>.
	 * </p>
	 * <p>
	 * This property must be used in conjunction with the {@link #getSubjectItemName() SubjectItemName} property. If you do not specify the
	 * SubjectItemName property, the {@link #isDoSubject() IsDoSubject} property has no effect.
	 * </p>
	 * <p>
	 * You must set both <code>SubjectItemName</code> and <code>IsDoSubject</code> before calling
	 * {@link #formatMsgWithDoclinks(lotus.domino.Database)}.
	 * </p>
	 *
	 * @return For a newsletter document created using the formatMsgWithDoclinks method, indicates if the newsletter includes a string
	 *         describing the subject of each document.
	 *
	 */
	@Override
	public boolean isDoSubject();

	/**
	 * For a newsletter document created using the formatMsgWithDoclinks method, indicates if the newsletter includes the relevance score
	 * for each document.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)}
	 * method</a>.
	 * </p>
	 * <p>
	 * This property applies only to newsletters with sorted collections; for example, a collection produced by a call to the
	 * {@link Database#FTSearch(String)} method</a>. If a newsletter collection is unsorted, this property has no effect.
	 * </p>
	 *
	 * @param flag
	 *            specify true if the newsletter should include the relevance score for each document.
	 *
	 */
	@Override
	public void setDoScore(final boolean flag);

	/**
	 * For a newsletter document created using the formatMsgWithDoclinks method, indicates if the newsletter includes a string describing
	 * the subject of each document.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)}
	 * method</a>.
	 * </p>
	 * <p>
	 * This property must be used in conjunction with the {@link #getSubjectItemName() SubjectItemName} property. If you do not specify the
	 * SubjectItemName property, the {@link #isDoSubject() IsDoSubject} property has no effect.
	 * </p>
	 * <p>
	 * You must set both <code>SubjectItemName</code> and <code>IsDoSubject</code> before calling
	 * {@link #formatMsgWithDoclinks(lotus.domino.Database)}.
	 * </p>
	 *
	 * @param flag
	 *            Secify true if the newsletter should include a string describing the subject of each document.
	 *
	 */
	@Override
	public void setDoSubject(final boolean flag);

	/**
	 * For a newsletter document created using the {@link #formatMsgWithDoclinks(lotus.domino.Database)} method, indicates the name of the
	 * item on the documents of a newsletter that contains the text you want to use as a subject line.
	 * <p>
	 * This property has no effect for a newsletter document created using the {@link #formatDocument(lotus.domino.Database, int)} method.
	 * </p>
	 * <p>
	 * This property must be used in conjunction with the {@link #isDoSubject()} property</a>. For example, if the IsDoSubject property is
	 * true and SubjectItemName is "Subject," then the newsletter contains the contents of each document Subject item next to the document
	 * link. If the IsDoSubject property is false, the SubjectItemName property has no effect.
	 * </p>
	 * <p>
	 * You must set both <code>SubjectItemName</code> and <code>IsDoSubject</code> before calling <code>formatMsgWithDoclinks</code>.
	 * </p>
	 *
	 * @param name
	 *            name of the item on the documents of a newsletter that contains the text you want to use as a subject line.
	 */
	@Override
	public void setSubjectItemName(final String name);

}
