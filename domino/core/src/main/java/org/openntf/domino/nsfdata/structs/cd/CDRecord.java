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

import org.openntf.domino.nsfdata.structs.AbstractStruct;
import org.openntf.domino.nsfdata.structs.SIG;

public abstract class CDRecord extends AbstractStruct {

	@Override
	public void init() {
		super.init();
		getHeader().setRecordLength(size());
	}

	// RPr: we must not overwrite the length. SIG should have parsed the correct length already
	//	@Override
	//	public void init(final ByteBuffer data) {
	//		super.init(data);
	//		getHeader().setRecordLength(data.limit() - data.position());
	//	}

	public abstract SIG getHeader();
}
