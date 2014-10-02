package de.foconis.lsext;

public class LSExtConvertException extends RuntimeException {

	private static final long serialVersionUID = 8232381828551405951L;

	public LSExtConvertException(final String cause) {
		super("Can't convert between LS and Java: " + cause);
	}

	public LSExtConvertException(final int pos, final String cause) {
		this("[Position " + pos + "] " + cause);
	}
}
