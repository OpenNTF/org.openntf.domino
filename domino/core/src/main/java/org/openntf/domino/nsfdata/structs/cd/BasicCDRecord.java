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
package org.openntf.domino.nsfdata.structs.cd;

import java.nio.ByteBuffer;

import org.openntf.domino.nsfdata.structs.SIG;

public class BasicCDRecord extends CDRecord {
	private static final long serialVersionUID = 1L;

	private SIG header_;

	protected BasicCDRecord(final SIG header, final ByteBuffer data) {
		header_ = header;
	}

	@Override
	public SIG getHeader() {
		return header_;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": Signature=" + getHeader() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}