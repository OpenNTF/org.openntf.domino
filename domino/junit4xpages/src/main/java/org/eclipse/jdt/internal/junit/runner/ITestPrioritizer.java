/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - bug 102632: [JUnit] Support for JUnit 4.
 *******************************************************************************/

package org.eclipse.jdt.internal.junit.runner;

import junit.framework.Test;

/**
 * Strategy to prioritize a test suite
 */
public interface ITestPrioritizer {
	/**
	 * Prioritize a test
	 *
	 * @param input tests to be prioritized
	 * @return the prioritized test suite
	 */
	Test prioritize(Test input);
}
