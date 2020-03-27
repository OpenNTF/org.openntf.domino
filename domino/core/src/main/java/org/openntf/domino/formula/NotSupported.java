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
package org.openntf.domino.formula;

import java.util.Map;

import org.openntf.formula.Function;
import org.openntf.formula.FunctionFactory;
import org.openntf.formula.FunctionSet;
import org.openntf.formula.ValueHolder;
import org.openntf.formula.annotation.ParamCount;

/**
 * at-Functions listed here are not supported. This implementation defines paramcount only
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public enum NotSupported {
	;

	public static class Functions extends FunctionSet {
		private static final Map<String, Function> functionSet = FunctionFactory.getFunctions(NotSupported.class);

		@Override
		public Map<String, Function> getFunctions() {
			return functionSet;
		}
	}

	private static ValueHolder notSupported() {
		throw new UnsupportedOperationException();
	}

	@ParamCount({ 1, 99 })
	public static ValueHolder atCommand() {
		return notSupported();
	}

	@ParamCount(2)
	public static ValueHolder atDDEExecute() {
		return notSupported();
	}

	@ParamCount(2)
	public static ValueHolder atDDEInitiate() {
		return notSupported();
	}

	@ParamCount(3)
	public static ValueHolder atDDEPoke() {
		return notSupported();
	}

	@ParamCount(1)
	public static ValueHolder atDDETerminate() {
		return notSupported();
	}

	@ParamCount({ 2, 3 })
	public static ValueHolder atDialogBox() {
		return notSupported();
	}

	@ParamCount(1)
	public static ValueHolder atEnableAlarms() {
		return notSupported();
	}

	@ParamCount(0)
	public static ValueHolder atFontList() {
		return notSupported();
	}

	@ParamCount(0)
	public static ValueHolder atGetIMContactListGroupNames() {
		return notSupported();
	}

	@ParamCount({ 1, 99 })
	public static ValueHolder atPrompt() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atCheckAlarms() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atDocLock() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atGetViewInfo() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atIsDB2() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atIsValid() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atMailSend() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atPicklist() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atPostedCommand() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atSetTargetFrame() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atSetViewInfo() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atTemplateVersion() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atURLGetHeader() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atURLHistory() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atURLOpen() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atUserAccess() {
		return notSupported();
	}

	@ParamCount({ 0, 99 })
	// TODO
	public static ValueHolder atViewShowThisUnread() {
		return notSupported();
	}
}
