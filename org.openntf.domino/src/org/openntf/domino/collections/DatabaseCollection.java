package org.openntf.domino.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

public class DatabaseCollection extends HashSet<Database> {
	private static final long serialVersionUID = 1L;
	private static final Logger log_ = Logger.getLogger(DatabaseCollection.class.getName());
	private Set<String> replicaIds_;

	@Override
	public boolean add(Database database) {
		String replicaId = database.getReplicaID();
		return getReplicaIds().add(replicaId);
	}

	@Override
	public void clear() {
		getReplicaIds().clear();
	}

	@Override
	public Iterator<Database> iterator() {
		return new Iterator<Database>() {
			private final Iterator<String> replicaIterator = getReplicaIds().iterator();

			@Override
			public boolean hasNext() {
				return replicaIterator.hasNext();
			}

			@Override
			public Database next() {
				Database result = null;
				String replicaId = (String) replicaIterator.next();
				Session session = Factory.getSession();
				result = session.getDatabase(null, null);
				String server = session.getServerName();
				if (!result.openByReplicaID(server, replicaId)) {
					result = null;
				}
				return result;
			}

			@Override
			public void remove() {
				replicaIterator.remove();
			}
		};
	}

	@Override
	public boolean remove(Object object) {
		boolean result = false;
		if (object instanceof Database) {
			Database database = (Database) object;
			String replicaId = database.getReplicaID();
			result = getReplicaIds().remove(replicaId);
		}
		return result;
	}

	protected Set<String> getReplicaIds() {
		if (replicaIds_ == null) {
			replicaIds_ = new HashSet<String>();
		}
		return replicaIds_;
	}

}
