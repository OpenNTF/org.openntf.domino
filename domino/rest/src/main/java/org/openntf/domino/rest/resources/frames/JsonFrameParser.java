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
package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonParser;

import java.io.Reader;

public class JsonFrameParser {

	private JsonFactory _factory = new JsonObjectFactory();

	private class JsonObjectFactory extends JsonJavaFactory {

	}

	public Object parse(Reader jsonInput) throws JsonException {
		Object result = null;
		Object object = JsonParser.fromJson(_factory, jsonInput);

		if (object instanceof JsonFrameAdapter) {
			JsonFrameAdapter adapter = (JsonFrameAdapter) object;
			result = adapter.getFrame();
		}

		return result;
	}

}
