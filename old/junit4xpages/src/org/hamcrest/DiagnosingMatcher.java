package org.hamcrest;

/**
 * TODO(ngd): Document.
 *
 */
public abstract class DiagnosingMatcher<T> extends BaseMatcher<T> {

	@Override
	public final boolean matches(final Object item) {
		return matches(item, Description.NONE);
	}

	@Override
	public final void describeMismatch(final Object item, final Description mismatchDescription) {
		matches(item, mismatchDescription);
	}

	protected abstract boolean matches(Object item, Description mismatchDescription);
}
