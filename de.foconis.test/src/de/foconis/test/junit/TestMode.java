package de.foconis.test.junit;

enum TestMode {
	PASS(true), FAIL(true), NONE(false);
	boolean enabled;

	TestMode(final boolean b) {
		enabled = b;
	}
}