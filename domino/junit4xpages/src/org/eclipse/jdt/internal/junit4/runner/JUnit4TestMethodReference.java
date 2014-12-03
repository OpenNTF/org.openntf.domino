/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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

import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.IVisitsTestTrees;
import org.junit.runner.Description;
import org.junit.runner.Request;

public class JUnit4TestMethodReference extends JUnit4TestReference {
	private final Description fDescription;

	public JUnit4TestMethodReference(Class<?> clazz, String methodName) {
		super(Request.method(clazz, methodName));
		fDescription = Description.createTestDescription(clazz, methodName);
	}

	public int countTestCases() {
		return 1;
	}

	public void sendTree(IVisitsTestTrees notified) {
		notified.visitTreeEntry(getIdentifier(), false, 1);
	}

	public String getName() {
		return fDescription.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof JUnit4TestMethodReference))
			return false;

		JUnit4TestMethodReference ref = (JUnit4TestMethodReference) obj;
		return (ref.fDescription.equals(fDescription));
	}

	@Override
	public int hashCode() {
		return fDescription.hashCode();
	}

	@Override
	public String toString() {
		return fDescription.toString();
	}

	public ITestIdentifier getIdentifier() {
		return new JUnit4Identifier(fDescription);
	}
}
