package com.tinkerpop.frames.modules.javahandler;

public class JavaHandlerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JavaHandlerException() {
	}

	public JavaHandlerException(String message) {
		super(message);
	}

	public JavaHandlerException(Throwable cause) {
		super(cause);
	}

	public JavaHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

}
