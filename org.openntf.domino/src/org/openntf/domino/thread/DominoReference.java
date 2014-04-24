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
package org.openntf.domino.thread;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * @author praml
 * 
 *         DominoReference tracks the lifetime of reference object and recycles delegate if reference object is GCed
 * 
 */
public class DominoReference extends WeakReference<Object> implements Comparable<DominoReference> {
	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReference.class.getName());

	/** The delegate_. This is the wrapped Object */
	private lotus.domino.Base delegate_;

	/** This is the CPP-ID or an other unique hash value **/
	private long key_;

	private transient int hashcode_;

	private boolean noRecycle = false;

	/**
	 * Instantiates a new domino reference.
	 * 
	 * @param reference
	 *            the wrapper to track
	 * @param q
	 *            the q
	 * @param delegate
	 *            the delegate
	 */
	public DominoReference(final long key, final Object reference, final lotus.domino.Base delegate, final ReferenceQueue<Object> q) {
		super(reference, q);

		// Because the reference separately contains a pointer to the delegate object, it's still available even
		// though the wrapper is null
		this.delegate_ = delegate;
		this.key_ = key;
	}

	//void clearLotusReference() {
	//	delegate_ = null;
	//}

	/**
	 * Recycle.
	 */
	void recycle() {
		if (delegate_ != null) {
			try {
				if (org.openntf.domino.impl.Base.isDead(delegate_)) {
					// the object is dead, so let's see who recycled this
					if (org.openntf.domino.impl.Base.isInvalid(delegate_)) {
						// if it is also invalid, someone called recycle on the delegate object.
						// this is already counted as manual recycle, so do not count twice
					} else {
						// otherwise, it's parent is recycled.
						// TODO this should get counted on a own counter
						Factory.countManualRecycle(delegate_.getClass());
					}

				} else {
					// recycle the delegate, because no hard ref points to us.
					if (!noRecycle) {
						delegate_.recycle();
					}
					int total = Factory.countAutoRecycle(delegate_.getClass());

					if (log_.isLoggable(Level.FINE)) {
						if (total % 5000 == 0) {
							log_.log(Level.FINE, "Auto-recycled " + total + " references");
						}
					}

				}
			} catch (NotesException e) {
				Factory.countRecycleError(delegate_.getClass());
				DominoUtils.handleException(e);
			}
		}
	}

	/**
	 * Prevents that the delegate object will be really recycled. Autorecycle counting is done. - So you must ensure that you recycle the
	 * document yourself or wrap it again
	 * 
	 * @param what
	 */
	public void setNoRecycle(final boolean what) {
		noRecycle = what;
	}

	/**
	 * A WeakValue is equal to another WeakValue iff they both refer to objects that are, in turn, equal according to their own equals
	 * methods. Key is not checked here, because there might be "dummy" values without a key so that "contains" works
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (!(obj instanceof DominoReference))
			return false;

		Object ref1 = this.get();
		Object ref2 = ((DominoReference) obj).get();

		if (ref1 == ref2)
			return true;

		if ((ref1 == null) || (ref2 == null))
			return false;

		return ref1.equals(ref2);
	}

	/**
     *
     */
	@Override
	public int hashCode() {
		if (hashcode_ == 0) {
			Object ref = this.get();
			hashcode_ = (ref == null) ? 0 : ref.hashCode();
		}
		return hashcode_;
	}

	long getKey() {
		return key_;
	}

	boolean isDead() {
		return org.openntf.domino.impl.Base.isDead(delegate_);
	}

	public static int getPrecedence(final lotus.domino.Base lotus) {

		if (lotus instanceof lotus.domino.Session)  		// Contains: AgentContext, Database, DateRange, DateTime, DbDirectory, International, Log, Name, Newsletter, 
			return 0;										//			 NotesProperty, PropertyBroker, Registration, RichTextParagraphStyle, RichTextStyle, Stream  
		// Contained by: AgentBase, NotesFactory (no objects we have to recycle)

		// --- Directory
		if (lotus instanceof lotus.domino.Directory)
			return -1;										// Contained by: Session 
		if (lotus instanceof lotus.domino.DirectoryNavigator)
			return -2;										// Contained by: Directory
		// ---

		if (lotus instanceof lotus.domino.Newsletter)		// Contains: Document 
			return -1;										// Contained by: _Session_ 

		if (lotus instanceof lotus.domino.DbDirectory)		// Contains: Database  
			return -2;										// Contained by: _Session_

		if (lotus instanceof lotus.domino.AgentContext) 	// Contains: Agent, Database, DateTime, Document, DocumentCollection 
			return -2;										// Contained by: _Session_ 

		if (lotus instanceof lotus.domino.DxlExporter)
			return -2;										// Contained by Session 

		if (lotus instanceof lotus.domino.DxlImporter)
			return -2;										// Contained by Session

		if (lotus instanceof lotus.domino.Database) 		// Can contain: ACL, Agent, DateTime, Document, DocumentCollection, Form, Outline, Replication, View 
			return -3;										// Contained by: _AgentContext_, _DbDirectory_, and Session 

		// --- VIEW
		if (lotus instanceof lotus.domino.View)				// Contains: Document, DateTime, ViewColumn, ViewEntry, ViewEntryCollection, ViewNavigator 
			return -4;										// Contained by: Database 
		if (lotus instanceof lotus.domino.ViewColumn) 		// Contains nothing
			return -5;										// Contained by: View 
		if (lotus instanceof lotus.domino.ViewEntryCollection) // Contains: ViewEntry 
			return -5;										 // Contained by: View 
		if (lotus instanceof lotus.domino.ViewNavigator)	// Contains: ViewEntry 
			return -5;										// Contained by: View 
		if (lotus instanceof lotus.domino.ViewEntry) 		// Contains: Document 
			return -6;										// Contained by: View, ViewEntryCollection, ViewNavigator
		// ----

		if (lotus instanceof lotus.domino.DocumentCollection)	// Contains: Document 	
			return -5;										// Contained by: AgentContext, Database, _View_ 

		// --- Document
		if (lotus instanceof lotus.domino.Document) 		// Contains: DateTime, EmbeddedObject, Item, MimeEntity, RichTextItem 
			return -7;										// Contained by: Database, _DocumentCollection_, Newsletter, View, ViewEntry!!!
		if (lotus instanceof lotus.domino.Item)  			// Contains: DateTime, MIMEEntity 
			return -8;										// Contained by: Document 
		if (lotus instanceof lotus.domino.RichTextItem)		// Contains: EmbeddedObject 
			return -8;										// Contained by: Document 
		if (lotus instanceof lotus.domino.EmbeddedObject)	// Contains nothing
			return -9;										// Contained by: Document and RichTextItem 
		if (lotus instanceof lotus.domino.MIMEEntity)		// Contains: MIMEHeader 
			return -9;										// Contained by: Item, Document 
		if (lotus instanceof lotus.domino.MIMEHeader)		// Contains nothing
			return -10;										// Contained by: MIMEEntity 
		// ---

		if (lotus instanceof lotus.domino.Agent)			// Contains: <strike>Database</strike> and DateTime 
			return -4;										// Contained by: AgentContext and _Database_ 

		if (lotus instanceof lotus.domino.Form)
			return -4;										// Contained by: Database 

		// ACL---
		if (lotus instanceof lotus.domino.ACL)				// Contains: ACLEntry 
			return -4;										// Contained by: Database 
		if (lotus instanceof lotus.domino.ACLEntry)			// Contains: Name 
			return -5;										// Contained by: ACL
		// ---

		if (lotus instanceof lotus.domino.AdministrationProcess)
			return -2;										// Contained by: _Session_

		// Richtext ----
		if (lotus instanceof lotus.domino.RichTextNavigator) // Contains: RichTextDocLink, RichTextSection, RichTextTable
			return -9;										// Contained by: RichTextItem
		if (lotus instanceof lotus.domino.RichTextSection)	// Contains: ColorObject, RichTextStyle 
			return -10;										// Contained by: RichTextNavigator 
		if (lotus instanceof lotus.domino.RichTextDoclink)	// Contains: RichTextStyle 
			return -11;										// Contained by: RichTextItem, RichTextNavigator 
		if (lotus instanceof lotus.domino.RichTextTable)	// Contains: ColorObject 
			return -12;										// Contained by: RichTextItem, RichTextNavigator 

		if (lotus instanceof lotus.domino.RichTextParagraphStyle) // Contains: RichTextTab 
			return -1;										// Contained by: Session 
		if (lotus instanceof lotus.domino.RichTextTab)
			return -2;										// Contained by: RichTextParagraphStyle 

		if (lotus instanceof lotus.domino.RichTextRange)	// Contains: RichTextStyle 
			return -9;										// Contained by: RichTextItem 
		if (lotus instanceof lotus.domino.RichTextStyle)
			return -10;										// Contained by: Session and RichTextItem 
		// ----

		if (lotus instanceof lotus.domino.ColorObject)		// Contained by: RichTextSection, RichTextTable, Session 
			return -13;

		//		if (lotus instanceof lotus.domino.DateRange)
		//			return 5;
		//		if (lotus instanceof lotus.domino.DateTime)
		//			return 6;
		//		if (lotus instanceof lotus.domino.Name)
		//			return 12;

		if (lotus instanceof lotus.domino.International)
			return -1;												// Contained by: Session 

		if (lotus instanceof lotus.domino.Log)
			return -1;												// Contained by: Session 

		if (lotus instanceof lotus.domino.NoteCollection)
			return -4;												// Contained by: Database

		if (lotus instanceof lotus.domino.NotesCalendar)
			return -1;												// Contained by: Session 

		if (lotus instanceof lotus.domino.NotesCalendarEntry)
			return -2;	// dunno
		if (lotus instanceof lotus.domino.NotesCalendarNotice)
			return -3;

		if (lotus instanceof lotus.domino.PropertyBroker)
			return -1;												// Contained by: Session 
		if (lotus instanceof lotus.domino.NotesProperty)
			return -2;

		if (lotus instanceof lotus.domino.Outline)
			return -4;												// Contained by: Database 
		if (lotus instanceof lotus.domino.OutlineEntry)
			return -5;

		if (lotus instanceof lotus.domino.Registration)
			return -1;												// Contained by: Session 

		if (lotus instanceof lotus.domino.Replication)
			return -4;												// Contained by: Database
		if (lotus instanceof lotus.domino.ReplicationEntry)
			return -5;

		if (lotus instanceof lotus.domino.Stream)
			return -1;												// Contained by: Session 
		return -99;
	}

	public int compareTo(final DominoReference o) {
		int i = getPrecedence(delegate_);
		int j = getPrecedence(o.delegate_);
		return i == j ? 0 : i < j ? -1 : 1;
	}

}
