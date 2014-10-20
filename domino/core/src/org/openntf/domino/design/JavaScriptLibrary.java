package org.openntf.domino.design;

import java.util.Map;

public interface JavaScriptLibrary extends ScriptLibrary {
	public Map<String, byte[]> getClassData();

	public Map<String, String> getClassSource();
}
