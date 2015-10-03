package org.openntf.domino.graph2;

import java.util.Iterator;

import com.tinkerpop.blueprints.Element;

@SuppressWarnings("rawtypes")
public interface DElementIterable {
	public interface DElementIterator {
		public Element next();

		public boolean hasNext();

		public void remove();
	}

	public Iterator<? extends Element> iterator();
}
