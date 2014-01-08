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
import org.openntf.domino.Session;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IndexHit implements Externalizable {
	private static final Logger log_ = Logger.getLogger(IndexHit.class.getName());
	private static final long serialVersionUID = 1L;
	private String term_;
	private String dbid_;
	private String item_;
	private String unid_;
	private String form_;
	private boolean hasReaders_ = false;

	public IndexHit() {

	}

	public IndexHit(final String term, final String dbid, final String item, final String listing) {
		term_ = term;
		dbid_ = dbid;
		item_ = item;
		try {
			unid_ = listing.substring(0, 32);
			String readerFlag = listing.substring(32, 33);
			hasReaders_ = readerFlag.equals("1");
			form_ = listing.substring(33);
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
		return dbid_ + unid_;
	}

	public Document getDocument(final Session session, final String serverName) {
		return session.getDocumentByMetaversalID(getMetaversalID(), serverName);
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term_;
	}

	/**
	 * @return the dbid
	 */
	public String getDbid() {
		return dbid_;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item_;
	}

	/**
	 * @return the unid
	 */
	public String getUnid() {
		return unid_;
	}

	public boolean hasReaders() {
		return hasReaders_;
	}

	/**
	 * @return the form
	 */
	public String getForm() {
		return form_;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
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
	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeUTF(term_);
		arg0.writeUTF(dbid_);
		arg0.writeUTF(item_);
		arg0.writeUTF(form_);
		arg0.writeUTF(unid_);
		arg0.writeBoolean(hasReaders_);
	}
}
