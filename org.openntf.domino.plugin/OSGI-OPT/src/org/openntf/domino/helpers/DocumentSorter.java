package org.openntf.domino.helpers;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.impl.DocumentList;
import org.openntf.domino.types.Null;

public class DocumentSorter implements Externalizable {
	private static final long serialVersionUID = 1L;
	private static boolean debug = false;
	private DocumentData[] dataset_;
	private SortedSet<DocumentData> treeSet_ = new TreeSet<DocumentData>();
	private SortedSet<DocumentData> skipListSet_ = new ConcurrentSkipListSet<DocumentData>();

	public static class DocumentData implements Comparable<DocumentData>, Externalizable {
		private static boolean debug = false;
		private int nid_;
		//		private String replid_;
		//		private String unid_;
		private final byte[] address_ = new byte[48];
		//		private List<Serializable> values_;	//houses the results of the comparison criteria
		private Serializable[] values_;

		//		private int valSize_;

		public Serializable[] _debugGetValues() {
			return values_;
		}

		public DocumentData() {
			//NTF - for deserialization (I wish we could leave as default access :-/)
		}

		@SuppressWarnings("rawtypes")
		public DocumentData(final Document document, final List<String> criteria) {
			nid_ = Integer.valueOf(document.getNoteID(), 16);
			//			replid_ = document.getAncestorDatabase().getReplicaID();
			//			unid_ = document.getUniversalID();
			String rid = document.getAncestorDatabase().getReplicaID();
			System.arraycopy(rid.getBytes(), 0, address_, 0, 16);
			String unid = document.getUniversalID();
			System.arraycopy(unid.getBytes(), 0, address_, 16, 32);
			//			values_ = new ArrayList<Serializable>();
			values_ = new Serializable[criteria.size()];
			if (criteria != null && !criteria.isEmpty()) {
				for (int i = 0; i < criteria.size(); i++) {
					Object obj = document.get(criteria.get(i));
					if (obj == null) {
						values_[i] = Null.INSTANCE;
					} else if (obj instanceof Vector) {
						if (!((Vector) obj).isEmpty()) {
							Object first = ((Vector) obj).get(0);
							if (first instanceof Serializable) {
								if (first instanceof Date) {
									values_[i] = ((Date) first).getTime();
								} else {
									values_[i] = (Serializable) first;
								}
							} else {
								values_[i] = Null.INSTANCE;
							}
						} else {
							values_[i] = Null.INSTANCE;
						}
					} else if (obj instanceof Serializable) {
						if (obj instanceof Date) {
							values_[i] = ((Date) obj).getTime();
						} else {
							values_[i] = (Serializable) obj;
						}
					} else {
						values_[i] = Null.INSTANCE;
					}
				}
			}
			//			valSize_ = values_.length;
		}

		public Document getDocument(final Database db) {
			if (nid_ != 0) {
				return db.getDocumentByID(Integer.toString(nid_, 16));
			} else {
				return db.getDocumentByUNID(new String(address_, 16, 32));
			}
		}

		public int compareTo(final DocumentData o) {
			int result = 0;
			int count0 = values_.length;
			int count1 = o.values_.length;
			if (count0 != count1) {
				throw new IllegalStateException("Cannot compare DocumentData arguments with different value sizes");
			}

			for (int i = 0; i < count0; i++) {
				Serializable ser0 = values_[i];
				Serializable ser1 = o.values_[i];
				if (ser0 == Null.INSTANCE && ser1 == Null.INSTANCE) {
					result = 0;
					if (debug) {
						System.out.println("Both values are null instances. They're equals");
					}
				} else if (ser0 == Null.INSTANCE) {
					result = -1;
					if (debug) {
						System.out.println("This value is null");
					}
				} else if (ser1 == Null.INSTANCE) {
					result = 1;
					if (debug) {
						System.out.println("Other value is null");
					}
				} else if (ser0 instanceof String && ser1 instanceof String) {
					result = ((String) ser0).compareTo((String) ser1);
					if (debug) {
						System.out.println("Comparing " + ser0 + " to " + ser1 + " resulting in " + result);
					}
				} else if (ser0 instanceof Comparable && ser1 instanceof Comparable) {
					result = ((Comparable) ser0).compareTo(ser1);
					if (debug) {
						System.out.println("Comparing " + ser0 + " to " + ser1 + " resulting in " + result);
					}
				} else {
					System.out.println("Unable to compare values of " + ser0 + " and " + ser1);
				}
				if (result != 0) {
					return result;
				}
			}
			if (result == 0) {
				if (debug) {
					System.out.println("Results match. Resorting to nid compare...");
				}
				if (nid_ > o.nid_)
					return -1;
				if (nid_ < o.nid_)
					return 1;
				//				result = replid_.compareTo(o.replid_);
			}
			//			if (result == 0) {
			//				result = unid_.compareTo(o.unid_);
			//			}
			return result;
		}

		public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
			nid_ = arg0.readInt();
			//			replid_ = arg0.readUTF();
			//			unid_ = arg0.readUTF();
			//			arg0.read(address_);
			boolean hasCriteria = arg0.readBoolean();

			if (hasCriteria) {
				int criteriaCount = arg0.readInt();
				values_ = new Serializable[criteriaCount];
				if (criteriaCount > 0) {
					for (int i = 0; i < criteriaCount; i++) {
						values_[i] = (Serializable) arg0.readObject();
					}
				}
			}
		}

		public void writeExternal(final ObjectOutput arg0) throws IOException {
			arg0.writeInt(nid_);
			//			arg0.write(address_);
			//			arg0.writeUTF(replid_);
			//			arg0.writeUTF(unid_);
			if (values_ != null) {
				arg0.writeBoolean(true);
				arg0.writeInt(values_.length);
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
	private transient Database database_;

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

	public void setDatabase(final Database db) {
		database_ = db;
	}

	public void setSourceCollection(final DocumentCollection coll) {
		sourceColl_ = coll;
		dataset_ = null;
	}

	protected void _normalize() {
		if (dataset_ == null) {
			List<String> criteria = getCriteria();
			if (criteria.isEmpty()) {
				addCriteria("@created");
			}
			if (sourceColl_ == null) {
				throw new IllegalStateException(
						"Cannot sort from a null DocumentCollection. Please add a DocumentCollection that you want to sort...");
			}
			//		System.out.println("Normalizing collection of " + sourceColl_.getCount() + " docs");
			long startTime = System.nanoTime();
			int docCount = 0;
			dataset_ = new DocumentData[sourceColl_.getCount()];
			for (Document doc : sourceColl_) {
				dataset_[docCount++] = new DocumentData(doc, criteria);
				//			System.out.println("Processed " + ++docCount + " documents");
				if (debug) {
					if (docCount % 50000 == 0) {
						System.out.println("Added " + docCount + " documents to set for sorting...");
					}
				}
			}
			long endTime = System.nanoTime();
			if (debug) {
				System.out.println("Normalized dataset of " + docCount + " in " + (endTime - startTime) / 1000000 + "ms");
			}
		}
	}

	protected void _sort() {
		_normalize();
		if (debug) {
			//			_treeSort();
			//			_skipListSort();
		}
		_arraySort();
		//		System.out.println("Done normalizing to a set of size " + size() + " after iterating over " + docCount + " docs.");
	}

	private void _treeSort() {
		long startTime = System.nanoTime();
		treeSet_.addAll(Arrays.asList(dataset_));
		long endTime = System.nanoTime();
		if (debug) {
			System.out.println("Tree sorted dataset of " + treeSet_.size() + " in " + (endTime - startTime) / 1000000 + "ms");
		}
	}

	private void _skipListSort() {
		long startTime = System.nanoTime();
		skipListSet_.addAll(Arrays.asList(dataset_));
		long endTime = System.nanoTime();
		if (debug) {
			System.out.println("SkipList sorted dataset of " + skipListSet_.size() + " in " + (endTime - startTime) / 1000000 + "ms");
		}
	}

	private void _arraySort() {
		long startTime = System.nanoTime();
		Arrays.sort(dataset_);
		long endTime = System.nanoTime();
		if (debug) {
			System.out.println("Array sorted dataset of " + dataset_.length + " in " + (endTime - startTime) / 1000000 + "ms");
		}
	}

	public DocumentData[] _debugGetDataset() {
		return dataset_;
	}

	public DocumentCollection sort() {
		long startMemory = Runtime.getRuntime().freeMemory();
		_sort();
		int[] nids = new int[dataset_.length];
		//		System.out.println("Beginning merge of " + dataset_.length + " DocumentDatas");
		for (int i = 0; i < nids.length; i++) {
			nids[i] = dataset_[i].nid_;
		}
		DocumentCollection result = new DocumentList(nids, database_);
		//		for (DocumentData data : dataset_) {
		//			result.merge(data.nid_);
		//		}
		//		System.out.println("Completed merge for a result size of " + result.getCount());
		long endMemory = Runtime.getRuntime().freeMemory();
		if (debug) {
			System.out.println("Total memory consumed: " + (endMemory - startMemory) / 1024 + "KB");
		}
		return result;
	}

	public int getCount() {
		return dataset_.length;
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

	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		criteria_ = (List<String>) arg0.readObject();
		int dLen = arg0.readInt();
		dataset_ = new DocumentData[dLen];
		for (int i = 0; i < dataset_.length; i++) {
			dataset_[i] = (DocumentData) arg0.readObject();
		}
		//		System.out.println("Completed deserialization of a DocumentSorter with " + dLen + " in the dataset");
	}

	public void writeExternal(final ObjectOutput arg0) throws IOException {
		arg0.writeObject(criteria_);
		arg0.writeInt(dataset_.length);
		for (int i = 0; i < dataset_.length; i++) {
			arg0.writeObject(dataset_[i]);
		}
	}

}
