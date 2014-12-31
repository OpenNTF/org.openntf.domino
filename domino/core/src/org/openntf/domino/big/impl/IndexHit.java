/**
 * 
 */
package org.openntf.domino.big.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IndexHit implements Externalizable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IndexHit.class.getName());
	private static final long serialVersionUID = 1L;
	private CharSequence term_;
	private CharSequence dbid_;
	private CharSequence item_;
	private CharSequence unid_;
	private CharSequence form_;
	private boolean hasReaders_ = false;

	public IndexHit() {

	}

	public IndexHit(final CharSequence term, final CharSequence dbid, final CharSequence item, final CharSequence listing) {
		term_ = term;
		dbid_ = dbid;
		item_ = item;
		try {
			unid_ = listing.toString().substring(0, 32);
			String readerFlag = listing.toString().substring(32, 33);
			hasReaders_ = readerFlag.equals("1");
			form_ = listing.toString().substring(33);
		} catch (NullPointerException npe) {
			System.out.println("NullPointer for listing?");
			throw new RuntimeException(npe);
		} catch (Exception e) {
			System.out.println("Exception occured parsing listing: " + listing);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbid_ == null) ? 0 : dbid_.hashCode());
		result = prime * result + ((unid_ == null) ? 0 : unid_.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexHit other = (IndexHit) obj;
		if (dbid_ == null) {
			if (other.dbid_ != null)
				return false;
		} else if (!dbid_.equals(other.dbid_))
			return false;
		if (unid_ == null) {
			if (other.unid_ != null)
				return false;
		} else if (!unid_.equals(other.unid_))
			return false;
		return true;
	}

	public String getMetaversalID() {
		return dbid_.toString() + unid_.toString();
	}

	//	private transient Document document_;

	public Document getDocument(final Session session, final String serverName) {
		//		if (document_ == null) {
		//			document_ = session.getDocumentByMetaversalID(getMetaversalID(), serverName);
		//		} else {
		//			String fname = document_.getFormName();
		//			if (fname == null || fname.length() < 1) {
		//				document_ = session.getDocumentByMetaversalID(getMetaversalID(), serverName);
		//			}
		//		}
		//		return document_;
		try {
			return session.getDocumentByMetaversalID(getMetaversalID(), serverName);
		} catch (Throwable t) {
			return null;
		}
	}

	public String getHitContext(final Session session, final String serverName) {
		String result = "";
		Document doc = getDocument(session, serverName);
		if (doc != null && doc.isValid() && !doc.getItems().isEmpty()) {
			Item hitItem = doc.getFirstItem(getItem());
			if (hitItem.getTypeEx() == Item.Type.RICHTEXT) {
				//TODO NTF - possibly add some HTML formatting stuff...
			} else {

			}
			String text = hitItem.getValueString();
			int pos = text.indexOf(getTerm());
			if (pos > 30) {
				if (text.length() < 60) {
					result = text.substring(pos - 30, text.length());
				} else {
					result = text.substring(pos - 30, pos + 30);
				}
			} else {
				if (text.length() < 60) {
					result = text;
				} else {
					result = text.substring(0, 60);
				}
			}
		}
		return result;
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term_.toString();
	}

	/**
	 * @return the dbid
	 */
	public String getDbid() {
		return dbid_.toString();
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item_.toString();
	}

	/**
	 * @return the unid
	 */
	public String getUnid() {
		return unid_.toString();
	}

	public boolean hasReaders() {
		return hasReaders_;
	}

	/**
	 * @return the form
	 */
	public String getForm() {
		return form_.toString();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		term_ = arg0.readUTF();
		dbid_ = arg0.readUTF();
		item_ = arg0.readUTF();
		form_ = arg0.readUTF();
		unid_ = arg0.readUTF();
		hasReaders_ = arg0.readBoolean();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeUTF(term_.toString());
		arg0.writeUTF(dbid_.toString());
		arg0.writeUTF(item_.toString());
		arg0.writeUTF(form_.toString());
		arg0.writeUTF(unid_.toString());
		arg0.writeBoolean(hasReaders_);
	}
}
