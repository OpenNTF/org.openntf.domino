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
package org.openntf.domino.nsfdata;

import java.io.Serializable;

public class NSFDateRange implements Serializable, NSFDateTimeValue {
	private static final long serialVersionUID = 1L;

	private final NSFDateTime start_;
	private final NSFDateTime end_;

	public NSFDateRange(final NSFDateTime start, final NSFDateTime end) {
		start_ = start;
		end_ = end;
	}

	public NSFDateTime getStart() {
		return start_;
	}
	public NSFDateTime getEnd() {
		return end_;
	}
}
