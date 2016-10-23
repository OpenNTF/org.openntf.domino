/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

public class FirstRunExecutionListener implements IListensToTestExecutions {
	protected MessageSender fSender;

	private TestIdMap fIds;

	FirstRunExecutionListener(MessageSender sender, TestIdMap ids) {
		fSender = sender;
		if (ids == null)
			throw new NullPointerException();
		fIds = ids;
	}

	public void notifyTestEnded(ITestIdentifier test) {
		sendMessage(test, MessageIds.TEST_END);
	}

	public void notifyTestFailed(TestReferenceFailure failure) {
		sendMessage(failure.getTest(), failure.getStatus());
		sendFailure(failure, MessageIds.TRACE_START, MessageIds.TRACE_END);
	}

	public void notifyTestStarted(ITestIdentifier test) {
		sendMessage(test, MessageIds.TEST_START);
		fSender.flush();
	}

	private String getTestId(ITestIdentifier test) {
		return fIds.getTestId(test);
	}

	protected void sendFailure(TestReferenceFailure failure, String startTrace,
			String endTrace) {
		FailedComparison comparison = failure.getComparison();
		if (comparison != null)
			comparison.sendMessages(fSender);

		fSender.sendMessage(startTrace);
		fSender.sendMessage(failure.getTrace());
		fSender.sendMessage(endTrace);
		fSender.flush();
	}

	private void sendMessage(ITestIdentifier test, String status) {
		fSender.sendMessage(status + getTestId(test) + ',' + test.getName());
	}

}
