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
package org.openntf.domino.iterators;

import java.util.Iterator;

import org.openntf.domino.DocumentCollection;

public class DocumentCollectionIterator implements Iterator<org.openntf.domino.Document> {
	private final org.openntf.domino.DocumentCollection documentCollection_;

	private org.openntf.domino.Document currWrapper = null;
	private org.openntf.domino.Document nextWrapper;

	public DocumentCollectionIterator(final DocumentCollection documentCollection) {
		documentCollection_ = documentCollection;
		nextWrapper = documentCollection_.getFirstDocument();
	}

	@Override
	public boolean hasNext() {
		return nextWrapper != null; // something in Queue?
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public org.openntf.domino.Document next() {
		//		try {
		//currLotusDoc = nextLotusDoc;
		//			if (currWrapper != null) {
		//				System.out.println("CollIsDead:" + documentCollection_.isDead());
		//				System.out.println("CurrIsDead:" + currWrapper.isDead());
		//				System.out.println("NextIsDead:" + nextWrapper.isDead());
		//				System.out.println("CurrUNID:  " + currWrapper.getUniversalID());
		//				System.out.println("NextUNID:  " + nextWrapper.getUniversalID());
		//			}

		if (nextWrapper == null || nextWrapper.isDead()) {
			if (nextWrapper == null) {
				System.out.println("ALERT: Wrapped version of next document is NULL");
			} else if (nextWrapper.isDead()) {
				System.out.println("ALERT: Wrapped version of next document is dead");
			} else {
				System.out.println("ALERT: It should have been impossible to arrive here");
				throw new RuntimeException();
			}
		}
		currWrapper = nextWrapper;
		if (nextWrapper != null) {
			nextWrapper = documentCollection_.getNextDocument(nextWrapper);
		}
		return currWrapper;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		documentCollection_.deleteDocument(currWrapper);
	}

}