/**
 * 
 */
package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IndexQuery {
	private static final Logger log_ = Logger.getLogger(IndexQuery.class.getName());
	private static final long serialVersionUID = 1L;

	private Set<String> terms_;
	private Set<String> dbids_;
	private Set<String> items_;
	private Set<String> forms_;
	private boolean isAnd_ = false;
	private int limit_ = 0;
	private transient IndexResults results_;

	public IndexQuery() {

	}

	public void setLimit(final int limit) {
		limit_ = limit;
	}

	public int getLimit() {
		return limit_;
	}

	public void setAnd(final boolean value) {
		isAnd_ = value;
	}

	public boolean isAnd() {
		return isAnd_;
	}

	/**
	 * @return the terms
	 */
	public Set<String> getTerms() {
		return terms_;
	}

	/**
	 * @param terms
	 *            the terms to set
	 */
	public void setTerms(final java.util.Collection<?> terms) {
		terms_ = IndexDatabase.toStringSet(terms);
	}

	public void setMergedTerms(final String terms) {
		Set<String> set = new HashSet<String>();
		if (terms.indexOf(' ') > 0) {
			String[] strings = terms.split(" ");
			for (String str : strings) {
				set.add(str);
			}
		} else {
			set.add(terms);
		}
		setTerms(set);
	}

	public void setTerms(final String term) {
		terms_ = IndexDatabase.toStringSet(term);
	}

	/**
	 * @return the dbids
	 */
	public Set<String> getDbids() {
		return dbids_;
	}

	/**
	 * @param dbids
	 *            the dbids to set
	 */
	public void setDbids(final Collection<Object> dbids) {
		dbids_ = IndexDatabase.toStringSet(dbids);
	}

	/**
	 * @return the items
	 */
	public Set<String> getItems() {
		return items_;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(final Collection<Object> items) {
		items_ = IndexDatabase.toStringSet(items);
	}

	/**
	 * @return the forms
	 */
	public Set<String> getForms() {
		return forms_;
	}

	/**
	 * @param forms
	 *            the forms to set
	 */
	public void setForms(final Collection<Object> forms) {
		forms_ = IndexDatabase.toStringSet(forms);
	}

	private static final boolean profile_ = true;

	public IndexResults execute(final IndexDatabase db) {
		long startNanos = 0;
		if (profile_)
			startNanos = System.nanoTime();
		IndexResults result = null;
		for (String term : getTerms()) {
			List<IndexHit> hits = db.getTermResults(term, getLimit(), getDbids(), IndexDatabase.toCISSet(getItems()), getForms());
			if (result == null) {
				result = new IndexResults(hits);
			} else {
				IndexResults temp = new IndexResults(hits);
				if (isAnd()) {
					result.intersect(temp);
				} else {
					result.merge(temp);
				}
			}
		}
		if (profile_) {
			int resultCount = result.getHits().size();
			System.out
					.println("IndexQuery executed for " + resultCount + " results in " + ((System.nanoTime() - startNanos) / 1000) + "us");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		isAnd_ = in.readBoolean();
		limit_ = in.readInt();
		boolean hasTerms = in.readBoolean();
		if (hasTerms) {
			int termSize = in.readInt();
			Set<String> terms_ = new HashSet<String>();
			for (int i = 0; i < termSize; i++) {
				terms_.add(in.readUTF());
			}
		}
		boolean hasDbs = in.readBoolean();
		if (hasDbs) {
			int dbSize = in.readInt();
			Set<String> dbids_ = new HashSet<String>();
			for (int i = 0; i < dbSize; i++) {
				dbids_.add(in.readUTF());
			}
		}
		boolean hasForms = in.readBoolean();
		if (hasForms) {
			int formSize = in.readInt();
			Set<String> forms_ = new HashSet<String>();
			for (int i = 0; i < formSize; i++) {
				forms_.add(in.readUTF());
			}
		}
		boolean hasItems = in.readBoolean();
		if (hasItems) {
			int itemSize = in.readInt();
			Set<String> items_ = new HashSet<String>();
			for (int i = 0; i < itemSize; i++) {
				items_.add(in.readUTF());
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(isAnd_);
		out.writeInt(limit_);
		if (terms_ != null) {
			out.writeBoolean(true);
			out.writeInt(terms_.size());
			for (String term : terms_) {
				out.writeUTF(term);
			}
		} else {
			out.writeBoolean(false);
		}
		if (dbids_ != null) {
			out.writeBoolean(true);
			out.writeInt(dbids_.size());
			for (String dbid : dbids_) {
				out.writeUTF(dbid);
			}
		} else {
			out.writeBoolean(false);
		}
		if (forms_ != null) {
			out.writeBoolean(true);
			out.writeInt(forms_.size());
			for (String form : forms_) {
				out.writeUTF(form);
			}
		} else {
			out.writeBoolean(false);
		}
		if (items_ != null) {
			out.writeBoolean(true);
			out.writeInt(items_.size());
			for (String item : items_) {
				out.writeUTF(item);
			}
		} else {
			out.writeBoolean(false);
		}
	}

}
