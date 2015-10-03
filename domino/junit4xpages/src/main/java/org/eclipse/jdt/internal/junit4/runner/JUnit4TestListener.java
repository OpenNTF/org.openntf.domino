/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   David Saff (saff@mit.edu) - initial API and implementation
 *             (bug 102632: [JUnit] Support for JUnit 4.)
 *******************************************************************************/
package org.eclipse.jdt.internal.junit4.runner;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jdt.internal.junit.runner.FailedComparison;
import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnit4TestListener extends RunListener {
	
	private static class IgnoredTestIdentifier extends JUnit4Identifier {
		public IgnoredTestIdentifier(Description description) {
			super(description);
		}
		@Override
		public String getName() {
			String name= super.getName();
			if (name != null)
				return MessageIds.IGNORED_TEST_PREFIX + name;
			return null;
		}
	}

	
	private final IListensToTestExecutions fNotified;

	public JUnit4TestListener(IListensToTestExecutions notified) {
		fNotified= notified;
	}

	@Override
	public void testStarted(Description plan) throws Exception {
		fNotified.notifyTestStarted(getIdentifier(plan));
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		TestReferenceFailure testReferenceFailure;
		try {
			Throwable exception= failure.getException();
			String status= exception instanceof AssertionError ? MessageIds.TEST_FAILED : MessageIds.TEST_ERROR;
			FailedComparison comparison= null;
			if (exception instanceof junit.framework.ComparisonFailure) {
				junit.framework.ComparisonFailure comparisonFailure= (junit.framework.ComparisonFailure) exception;
				comparison= new FailedComparison(comparisonFailure.getExpected(), comparisonFailure.getActual());
			} else if (exception instanceof org.junit.ComparisonFailure) {
				org.junit.ComparisonFailure comparisonFailure= (org.junit.ComparisonFailure) exception;
				comparison= new FailedComparison(comparisonFailure.getExpected(), comparisonFailure.getActual());
			}
			testReferenceFailure= new TestReferenceFailure(getIdentifier(failure.getDescription()), status, failure.getTrace(), comparison);
		} catch (RuntimeException e) {
			StringWriter stringWriter= new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			testReferenceFailure= new TestReferenceFailure(getIdentifier(failure.getDescription()), MessageIds.TEST_FAILED, stringWriter.getBuffer().toString(), null);
		}
		fNotified.notifyTestFailed(testReferenceFailure);
	}

	@Override
	public void testIgnored(Description plan) throws Exception {
		// Send message to listeners which would be stale otherwise 
		ITestIdentifier identifier= new IgnoredTestIdentifier(plan);
		fNotified.notifyTestStarted(identifier);
		fNotified.notifyTestEnded(identifier);
	}

	@Override
	public void testFinished(Description plan) throws Exception {
		fNotified.notifyTestEnded(getIdentifier(plan));
	}

	private ITestIdentifier getIdentifier(Description plan) {
		return new JUnit4Identifier(plan);
	}
}
