/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - initial API and implementation
 *             (bug 102632: [JUnit] Support for JUnit 4.)
 *******************************************************************************/

package org.eclipse.jdt.internal.junit.runner;

public class TestReferenceFailure {

	private final ITestIdentifier fTest;

	private final String fTrace;

	private final String fStatus;

	private FailedComparison fComparison;

	public TestReferenceFailure(final ITestIdentifier ref, final String status, final String trace) {
		this(ref, status, trace, null);
	}

	public TestReferenceFailure(final ITestIdentifier reference, final String status, final String trace, final FailedComparison comparison) {
		fTest = reference;
		fStatus = status;
		fTrace = trace;
		fComparison = comparison;
	}

	public TestReferenceFailure(final ITestReference reference, final String status, final String trace) {
		this(reference.getIdentifier(), status, trace);
	}

	public String getStatus() {
		return fStatus;
	}

	public String getTrace() {
		return fTrace;
	}

	public ITestIdentifier getTest() {
		return fTest;
	}

	@Override
	public String toString() {
		return fStatus + " " + fTest.getName(); //$NON-NLS-1$
	}

	public void setComparison(final FailedComparison comparison) {
		fComparison = comparison;
	}

	public FailedComparison getComparison() {
		return fComparison;
	}
}
