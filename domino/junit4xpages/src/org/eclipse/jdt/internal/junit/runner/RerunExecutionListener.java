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

public class RerunExecutionListener extends FirstRunExecutionListener {
	// Don't send ids here, since they don't match the ids of the original run:
	// RemoteTestRunner#rerunTest(..) reloads Test, so ITestReferences are not equals(..).

	public RerunExecutionListener(MessageSender sender, TestIdMap ids) {
		super(sender, ids);
	}

	private String fStatus = RemoteTestRunner.RERAN_OK;

	public void notifyTestFailed(TestReferenceFailure failure) {
		sendFailure(failure, MessageIds.RTRACE_START, MessageIds.RTRACE_END);

		String status = failure.getStatus();
		if (status.equals(MessageIds.TEST_FAILED))
			fStatus = RemoteTestRunner.RERAN_FAILURE;
		else if (status.equals(MessageIds.TEST_ERROR))
			fStatus = RemoteTestRunner.RERAN_ERROR;
		else
			throw new IllegalArgumentException(status);
	}

	public void notifyTestStarted(ITestIdentifier test) {
		// do nothing
	}

	public void notifyTestEnded(ITestIdentifier test) {
		// do nothing
	}

	public String getStatus() {
		return fStatus;
	}

}
