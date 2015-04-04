package com.github.javafaker.service;

public class LocaleDoesNotExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LocaleDoesNotExistException(final String message) {
		super(message);
	}
}
