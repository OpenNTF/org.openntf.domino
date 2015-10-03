/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
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

package org.eclipse.jdt.internal.junit.runner.junit3;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

import org.eclipse.jdt.internal.junit.runner.IClassifiesThrowables;
import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.MessageIds;
import org.eclipse.jdt.internal.junit.runner.TestExecution;

public class JUnit3Listener implements TestListener {
	private final IListensToTestExecutions fNotified;

	private final IClassifiesThrowables fClassifier;

	public JUnit3Listener(TestExecution execution) {
		fNotified= execution.getListener();
		fClassifier= execution.getClassifier();
	}

	public void startTest(Test test) {
		fNotified.notifyTestStarted(id(test));
	}

	public void addError(Test test, Throwable throwable) {
		newReference(test).sendFailure(throwable, fClassifier, MessageIds.TEST_ERROR, fNotified);
	}

	public void addFailure(Test test, AssertionFailedError assertionFailedError) {
		newReference(test).sendFailure(assertionFailedError, fClassifier, MessageIds.TEST_FAILED, fNotified);
	}

	public void endTest(Test test) {
		fNotified.notifyTestEnded(id(test));
	}

	private ITestIdentifier id(Test test) {
		return newReference(test).getIdentifier();
	}

	private JUnit3TestReference newReference(Test test) {
		return new JUnit3TestReference(test);
	}
}
