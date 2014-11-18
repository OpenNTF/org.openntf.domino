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

import java.util.ArrayList;
import java.util.Iterator;

public class TestExecution {
	private boolean fShouldStop = false;

	private IListensToTestExecutions fExecutionListener;

	private IClassifiesThrowables fClassifier;

	private ArrayList fStopListeners = new ArrayList();

	public TestExecution(IListensToTestExecutions listener,
			IClassifiesThrowables classifier) {
		fClassifier = classifier;
		fExecutionListener = listener;
	}

	public void run(ITestReference[] suites) {
		for (int i = 0; i < suites.length; i++) {
			if (fShouldStop)
				return;
			suites[i].run(this);
		}
	}

	public boolean shouldStop() {
		return fShouldStop;
	}

	public void stop() {
		fShouldStop = true;
		for (Iterator iter = fStopListeners.iterator(); iter.hasNext();) {
			IStopListener listener = (IStopListener) iter.next();
			listener.stop();
		}
	}

	public IListensToTestExecutions getListener() {
		return fExecutionListener;
	}

	public IClassifiesThrowables getClassifier() {
		return fClassifier;
	}

	public void addStopListener(IStopListener listener) {
		fStopListeners.add(listener);
	}
}
