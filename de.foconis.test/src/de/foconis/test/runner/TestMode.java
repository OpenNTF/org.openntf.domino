package de.foconis.test.runner;

public enum TestMode {
	PASS(true), FAIL(true), NONE(false);
	public boolean enabled;

	TestMode(final boolean b) {
		enabled = b;
	}
}