/**
 * 
 */
package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
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
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IndexQuery.class.getName());

	private Set<CharSequence> terms_;
	private Set<CharSequence> dbids_;
	private Set<CharSequence> items_;
	private Set<CharSequence> forms_;
	private boolean isAnd_ = false;
	private int limit_ = 0;
	@SuppressWarnings("unused")
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
	public Set<CharSequence> getTerms() {
		return terms_;
	}

	/**
	 * @param terms
	 *            the terms to set
	 */
	public void setTerms(final java.util.Collection<?> terms) {
		terms_ = IndexDatabase.toStringSet(terms);
	}

	public void setStringTerms(final Set<CharSequence> terms) {
		terms_ = terms;
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

	public void setMergedTerms(final CharSequence terms, final String split) {
		Set<CharSequence> set = new HashSet<CharSequence>();
		String[] strings = terms.toString().split(split);
		//		System.out.println("Setting up " + strings.length + " terms");
		for (String str : strings) {
			set.add(str);
		}
		setStringTerms(set);
	}

	public void setTerms(final CharSequence term) {
		terms_ = IndexDatabase.toStringSet(term);
	}

	/**
	 * @return the dbids
	 */
	public Set<CharSequence> getDbids() {
		return dbids_;
	}

	/**
	 * @param dbids
	 *            the dbids to set
	 */
	public void setDbids(final Collection<Object> dbids) {
		dbids_ = IndexDatabase.toStringSet(dbids);
		if (dbids != null && !dbids.isEmpty()) {
			//			System.out.println("Setting dbids filter to " + dbids.getClass().getSimpleName() + " of size " + dbids.size() + ": "
			//					+ debugStringSet(dbids_));
		}
	}

	/**
	 * @return the items
	 */
	public Set<CharSequence> getItems() {
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
	public Set<CharSequence> getForms() {
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

	protected IndexResults createResultsFromHitList(final List<IndexHit> hits) {
		return new IndexResults(hits);
	}

	//	private static final List<IndexHit> emptyHits = new ArrayList<IndexHit>();

	@SuppressWarnings("unused")
	private static String debugStringSet(final Set<String> set) {
		StringBuilder debug = new StringBuilder();
		debug.append('[');
		if (set == null || set.size() == 0) {

		} else if (set.size() == 1) {
			debug.append(set.iterator().next());
		} else {
			boolean isFirst = true;
			for (String term : set) {
				if (!isFirst) {
					debug.append(" : ");
				}
				isFirst = false;
				debug.append(term);
			}
		}
		debug.append(']');
		return debug.toString();
	}

	@SuppressWarnings("unused")
	private String debugGetTerms() {
		StringBuilder debug = new StringBuilder();
		Set<CharSequence> terms = terms_;
		debug.append('[');
		if (terms == null || terms.size() == 0) {

		} else if (terms.size() == 1) {
			debug.append(terms.iterator().next());
		} else {
			boolean isFirst = true;
			for (CharSequence term : terms) {
				if (!isFirst) {
					if (isAnd()) {
						debug.append(" AND ");
					} else {
						debug.append(" OR ");
					}
				}
				isFirst = false;
				debug.append(term);
			}
		}
		debug.append(']');
		return debug.toString();
	}

	public IndexResults execute(final IndexDatabase db) {
		long startNanos = 0;
		if (profile_)
			startNanos = System.nanoTime();
		IndexResults result = null;
		//		System.out.println("Executing with: " + debugGetTerms());
		Set<CharSequence> localTerms = getTerms();
		if (!db.getCaseSensitive()) {
			localTerms = IndexDatabase.toCISSet(localTerms);
		}
		if (isAnd() && localTerms.size() > 1) {
			for (CharSequence term : localTerms) {
				List<IndexHit> hits = db.getTermResults(term, getLimit(), getDbids(), IndexDatabase.toCISSet(getItems()), getForms());
				if (result == null) {
					result = createResultsFromHitList(hits);
				} else {
					IndexResults temp = createResultsFromHitList(hits);
					result.intersect(temp);
				}
			}
		} else {
			result = createResultsFromHitList(new ArrayList<IndexHit>());
			for (CharSequence term : localTerms) {
				List<IndexHit> hits = db.getTermResults(term, getLimit(), getDbids(), IndexDatabase.toCISSet(getItems()), getForms());
				IndexResults temp = createResultsFromHitList(hits);
				result.merge(temp);
			}
		}
		if (profile_) {
			int resultCount = result == null ? 0 : result.getHits().size();
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
			for (CharSequence term : terms_) {
				out.writeUTF(term.toString());
			}
		} else {
			out.writeBoolean(false);
		}
		if (dbids_ != null) {
			out.writeBoolean(true);
			out.writeInt(dbids_.size());
			for (CharSequence dbid : dbids_) {
				out.writeUTF(dbid.toString());
			}
		} else {
			out.writeBoolean(false);
		}
		if (forms_ != null) {
			out.writeBoolean(true);
			out.writeInt(forms_.size());
			for (CharSequence form : forms_) {
				out.writeUTF(form.toString());
			}
		} else {
			out.writeBoolean(false);
		}
		if (items_ != null) {
			out.writeBoolean(true);
			out.writeInt(items_.size());
			for (CharSequence item : items_) {
				out.writeUTF(item.toString());
			}
		} else {
			out.writeBoolean(false);
		}
	}

}
