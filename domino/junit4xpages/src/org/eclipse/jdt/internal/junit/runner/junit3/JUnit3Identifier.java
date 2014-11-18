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

/**
 *
 */
package org.eclipse.jdt.internal.junit.runner.junit3;

import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;

class JUnit3Identifier implements ITestIdentifier {
	private final JUnit3TestReference ref;

	JUnit3Identifier(JUnit3TestReference ref) {
		this.ref = ref;
	}

	public String getName() {
		return ref.getName();
	}

	public boolean equals(Object obj) {
		JUnit3Identifier id = (JUnit3Identifier) obj;
		return ref.equals(id.ref);
	}

	public int hashCode() {
		return ref.hashCode();
	}
}