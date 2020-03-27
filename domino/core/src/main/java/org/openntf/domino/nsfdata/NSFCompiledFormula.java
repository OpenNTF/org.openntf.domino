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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Storage unit for compiled formula data. It's really just a byte array, but it's good to give it a name, and a tool using the actual Notes
 * API could expect this for NSFFormulaDecompile.
 * 
 * @author jgallagher
 *
 */
public class NSFCompiledFormula implements Externalizable {
	private byte[] data_;

	public NSFCompiledFormula(final byte[] data) {
		data_ = Arrays.copyOf(data, data.length);
	}

	public NSFCompiledFormula(final ByteBuffer data) {
		ByteBuffer localData = data.duplicate();
		int length = localData.limit() - localData.position();
		data_ = new byte[length];
		localData.get(data_);
	}

	public byte[] getData() {
		return Arrays.copyOf(data_, data_.length);
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]";
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int length = in.readInt();
		data_ = new byte[length];
		in.read(data_);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.write(data_.length);
		out.write(data_);
	}

}
