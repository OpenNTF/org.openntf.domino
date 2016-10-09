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
