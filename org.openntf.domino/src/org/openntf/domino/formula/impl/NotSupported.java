package org.openntf.domino.formula.impl;

import org.openntf.domino.formula.ValueHolder;

/**
 * at-Functions listed here are not supported. This implementation defines paramcount only
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public enum NotSupported {
	;
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
}
