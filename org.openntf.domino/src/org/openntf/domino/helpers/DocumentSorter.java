package org.openntf.domino.helpers;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.helpers.DocumentSorter.DocumentData;
import org.openntf.domino.types.Null;

public class DocumentSorter extends ConcurrentSkipListSet<DocumentData> {
	private static final long serialVersionUID = 1L;

	public static class DocumentData implements Comparable<DocumentData>, Externalizable {
		private int nid_;
		private String replid_;
		private String unid_;
		private List<Serializable> values_;	//houses the results of the comparison criteria
		private int valSize_;

		public DocumentData() {
			//NTF - for deserialization (I wish we could leave as default access :-/)
		}

		@SuppressWarnings("rawtypes")
		public DocumentData(final Document document, final List<String> criteria) {
			nid_ = Integer.valueOf(document.getNoteID(), 16);
			replid_ = document.getAncestorDatabase().getReplicaID();
			unid_ = document.getUniversalID();
			values_ = new ArrayList<Serializable>();
			if (criteria != null && !criteria.isEmpty()) {
				for (String item : criteria) {
					Object obj = document.get(item);
					if (obj == null) {
						values_.add(Null.INSTANCE);
					} else if (obj instanceof Vector) {
						if (!((Vector) obj).isEmpty()) {
							Object first = ((Vector) obj).get(0);
							if (first instanceof Serializable) {
								values_.add((Serializable) first);
							} else {
								values_.add(Null.INSTANCE);
							}
						} else {
							values_.add(Null.INSTANCE);
						}
					} else if (obj instanceof Serializable) {
						values_.add((Serializable) obj);
					} else {
						values_.add(Null.INSTANCE);
					}
				}
			}
			valSize_ = values_.size();
		}

		public Document getDocument(final Database db) {
			return db.getDocumentByID(Integer.toString(nid_, 16));
		}

		public int compareTo(final DocumentData o) {
			int result = 0;
			int count0 = valSize_;
			int count1 = o.valSize_;
			if (count0 != count1) {
				throw new IllegalStateException("Cannot compare DocumentData arguments with different value sizes");
			}

			for (int i = 0; i < count0; i++) {
				Serializable ser0 = values_.get(i);
				Serializable ser1 = o.values_.get(i);
				if (ser0 == Null.INSTANCE && ser1 == Null.INSTANCE) {
					result = 0;
				} else if (ser0 == Null.INSTANCE) {
					result = -1;
				} else if (ser1 == Null.INSTANCE) {
					result = 1;
				} else if (ser0 instanceof Comparable && ser1 instanceof Comparable) {
					result = ((Comparable) ser0).compareTo(ser1);
				}
				if (result != 0)
					return result;
			}
			return result;
		}

		public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
			nid_ = arg0.readInt();
			replid_ = arg0.readUTF();
			unid_ = arg0.readUTF();
			boolean hasCriteria = arg0.readBoolean();
			values_ = new ArrayList<Serializable>();
			if (hasCriteria) {
				int criteriaCount = arg0.readInt();
				if (criteriaCount > 0) {
					for (int i = 0; i < criteriaCount; i++) {
						values_.add((Serializable) arg0.readObject());
					}
				}
			}
		}

		public void writeExternal(final ObjectOutput arg0) throws IOException {
			arg0.writeInt(nid_);
			arg0.writeUTF(replid_);
			arg0.writeUTF(unid_);
			if (values_ != null) {
				arg0.writeBoolean(true);
				arg0.writeInt(values_.size());
				for (Serializable value : values_) {
					arg0.writeObject(value);
				}
			} else {
				arg0.writeBoolean(false);
			}
		}

	}

	private List<String> criteria_ = new ArrayList<String>();
	private transient DocumentCollection sourceColl_;
	private Database database_;

	public DocumentSorter() {
		// NTF for serialization
	}

	public DocumentSorter(final DocumentCollection sourceCollection) {
		sourceColl_ = sourceCollection;
		database_ = sourceColl_.getAncestorDatabase();
	}

	public DocumentSorter(final DocumentCollection sourceCollection, final List<String> criteria) {
		criteria_ = criteria;
		sourceColl_ = sourceCollection;
		database_ = sourceColl_.getAncestorDatabase();
	}

	protected void _sort() {
		List<String> criteria = getCriteria();
		if (criteria.isEmpty()) {
			addCriteria("@created");
		}
		if (sourceColl_ == null) {
			throw new IllegalStateException(
					"Cannot sort from a null DocumentCollection. Please add a DocumentCollection that you want to sort...");
		}
		//		System.out.println("Normalizing collection of " + sourceColl_.getCount() + " docs");
		int docCount = 0;
		for (Document doc : sourceColl_) {
			//			System.out.println("Processed " + ++docCount + " documents");
			this.add(new DocumentData(doc, criteria));
		}
		//		System.out.println("Done normalizing");
	}

	public DocumentCollection sort() {
		DocumentCollection result = database_.createDocumentCollection();
		_sort();
		//		System.out.println("Beginning merge of " + this.size() + " DocumentDatas");
		for (DocumentData data : this) {
			result.add(data.getDocument(database_));
		}
		//		System.out.println("Completed merge for a result size of " + result.getCount());
		return result;
	}

	public List<String> getCriteria() {
		if (criteria_ == null) {
			criteria_ = new ArrayList<String>();
		}
		return criteria_;
	}

	public void addCriteria(final String crit) {
		getCriteria().add(crit);
	}

	public void removeCriteria(final String crit) {
		getCriteria().remove(crit);
	}

}
