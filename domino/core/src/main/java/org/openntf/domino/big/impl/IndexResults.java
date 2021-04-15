/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
/**
 * 
 */
package org.openntf.domino.big.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IndexResults {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(IndexResults.class.getName());
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	protected Map<CharSequence, AtomicInteger> terms_;
	protected Map<CharSequence, AtomicInteger> dbids_;
	protected Map<CharSequence, AtomicInteger> items_;
	protected Map<CharSequence, AtomicInteger> forms_;
	protected final List<IndexHit> hits_;

	public IndexResults(final List<IndexHit> hits) {
		hits_ = hits;
	}

	public void merge(final IndexResults result) {
		if (!result.hits_.isEmpty()) {
			for (IndexHit hit : result.hits_) {
				if (!hits_.contains(hit)) {
					hits_.add(hit);
				}
			}
		}
	}

	public void intersect(final IndexResults result) {
		if (result.hits_.isEmpty()) {
			hits_.clear();
		} else {
			ArrayList<IndexHit> remove = new ArrayList<IndexHit>();
			for (IndexHit hit : hits_) {
				if (!result.hits_.contains(hit)) {
					remove.add(hit);
				}
			}
			hits_.removeAll(remove);
		}
	}

	public Map<CharSequence, AtomicInteger> getTerms() {
		if (terms_ == null) {
			terms_ = new HashMap<CharSequence, AtomicInteger>();
			for (IndexHit hit : hits_) {
				String term = hit.getTerm();
				if (terms_.containsKey(term)) {
					terms_.get(term).incrementAndGet();
				} else {
					terms_.put(term, new AtomicInteger(1));
				}
			}
		}
		return terms_;
	}

	public Map<CharSequence, AtomicInteger> getDbids() {
		if (dbids_ == null) {
			dbids_ = new HashMap<CharSequence, AtomicInteger>();
			for (IndexHit hit : hits_) {
				String term = hit.getDbid();
				if (dbids_.containsKey(term)) {
					dbids_.get(term).incrementAndGet();
				} else {
					dbids_.put(term, new AtomicInteger(1));
				}
			}
		}
		return dbids_;
	}

	public Map<CharSequence, AtomicInteger> getForms() {
		if (forms_ == null) {
			forms_ = new HashMap<CharSequence, AtomicInteger>();
			for (IndexHit hit : hits_) {
				String term = hit.getDbid() + hit.getForm();
				if (forms_.containsKey(term)) {
					forms_.get(term).incrementAndGet();
				} else {
					forms_.put(term, new AtomicInteger(1));
				}
			}
		}
		return forms_;
	}

	public Map<CharSequence, AtomicInteger> getItems() {
		if (items_ == null) {
			items_ = new HashMap<CharSequence, AtomicInteger>();
			for (IndexHit hit : hits_) {
				String term = hit.getDbid() + hit.getItem();
				if (items_.containsKey(term)) {
					items_.get(term).incrementAndGet();
				} else {
					items_.put(term, new AtomicInteger(1));
				}
			}
		}
		return items_;
	}

	public List<IndexHit> getHits() {
		return hits_;
	}

	public Integer getTermCount(final String term) {
		AtomicInteger ai = getTerms().get(term);
		return ai == null ? 0 : ai.get();
	}

	public Integer getDbidCount(final String dbid) {
		AtomicInteger ai = getDbids().get(dbid);
		return ai == null ? 0 : ai.get();
	}

	public Integer getFormCount(final String form) {
		AtomicInteger ai = getForms().get(form);
		return ai == null ? 0 : ai.get();
	}

	public Integer getItemCount(final String item) {
		AtomicInteger ai = getItems().get(item);
		return ai == null ? 0 : ai.get();
	}

}
