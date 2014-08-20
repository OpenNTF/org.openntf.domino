/*
 * Copyright Paul Withers, Intec 2011-2013
=======
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 *
 * Some significant enhancements here from the OpenNTF version
 *
 * 1. Everything is designed to work regardless of ExtLib packages
 *
 * 2. _logDbName and debug level set from logging.properties.
 *
 * 3. setThisAgent(boolean) method has been added. By default it gets the current page.
 * Otherwise it gets the previous page. Why? Because if we've been redirected to an error page,
 * we want to know which page the ACTUAL error occurred on.
 *
 *
 * Nathan T. Freeman, GBS Jun 20, 2011
 * Developers notes...
 *
 * Because the log methods are static, one simply needs to call..
 *
 * OpenLogItem.logError(session, throwable)
 *
 * or...
 *
 * OpenLogItem.logError(session, throwable, message, level, document)
 *
 * or...
 *
 * OpenLogItem.logEvent(session, message, level, document)
 * 
 */

package org.openntf.domino.logging;

/**
 * The Class DominoOpenLogItem.
 * 
 */
public class DominoOpenLogItem extends BaseOpenLogItem {

	/**
	 * Constructor
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public DominoOpenLogItem() {

	}

}