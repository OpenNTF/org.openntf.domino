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
package org.openntf.domino.xsp.xots;

import java.io.PrintWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.openntf.domino.Database;
import org.openntf.domino.design.AnyFileResource;
import org.openntf.domino.design.DatabaseDesign;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class JSR223Tasklet extends AbstractDominoRunnable {
	private static final long serialVersionUID = 1L;

	private final String script_;
	private final String scriptExt_;
	private final String databasePath_;

	public JSR223Tasklet(final String scriptName, final Database database) {
		int extIndex = scriptName.lastIndexOf('.');
		scriptExt_ = scriptName.substring(extIndex + 1);

		DatabaseDesign design = database.getDesign();
		AnyFileResource script = design.getAnyFileResource(scriptName);
		script_ = new String(script.getFileData());
		databasePath_ = database.getApiPath();
	}

	@Override
	public void run() {
		Database database = Factory.getSession(SessionType.CURRENT).getDatabase(databasePath_);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension(scriptExt_);

		engine.put("database", database);
		engine.put("session", database.getAncestorSession());

		ScriptContext context = engine.getContext();
		context.setWriter(new PrintWriter(System.out));
		context.setErrorWriter(new PrintWriter(System.err));

		try {
			engine.eval(script_);
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}
}
