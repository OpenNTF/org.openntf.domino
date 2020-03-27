/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.types;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CaseInsensitiveHashSet extends AbstractSet<String> {
	private Map<String, String> delegate = new HashMap<String, String>();

	@Override
	public boolean add(final String e) {
		if (delegate.containsKey(e.toLowerCase()))
			return false;
		delegate.put(e.toLowerCase(), e);
		return true;
	}

	@Override
	public void clear() {
		delegate.clear();

	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof String) {
			return delegate.containsKey(((String) arg0).toLowerCase());
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof String) {
			return delegate.remove(((String) arg0).toLowerCase()) != null;
		}
		return false;
	}

	@Override
	public Iterator<String> iterator() {
		return delegate.values().iterator();
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public Object[] toArray() {
		return delegate.values().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate.values().toArray(a);
	}

	@Override
	public String toString() {
		return delegate.values().toString();
	}

}
