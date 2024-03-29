/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.impl.DElementStore;

public class PersonStore extends DElementStore {

	public PersonStore() {
		this.addType(Person.class);
		this.setStoreKey("names.nsf"); //$NON-NLS-1$
	}

	@Override
	public Object findElementDelegate(final Object delegateKey/*, final Class<? extends Element> type*/) {
		if (this.getProxyStoreDelegate() == null) {
			throw new IllegalStateException("Cannot find elements in a User store without a proxy store set"); //$NON-NLS-1$
		}
		return super.findElementDelegate(delegateKey/*, type*/);
	}

}
