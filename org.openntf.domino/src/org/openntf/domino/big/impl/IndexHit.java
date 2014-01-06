/**
 * 
 */
package org.openntf.domino.big.impl;

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
public class IndexHit {
	private static final Logger log_ = Logger.getLogger(IndexHit.class.getName());
	private static final long serialVersionUID = 1L;
	private String term_;
	private String dbid_;
	private String item_;
	private String unid_;
	private String form_;

	public IndexHit() {

	}

	public IndexHit(final String term, final String dbid, final String item, final String listing) {
		term_ = term;
		dbid_ = dbid;
		item_ = item;
		unid_ = listing.substring(0, 32);
		form_ = listing.substring(32);
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
	}
}
