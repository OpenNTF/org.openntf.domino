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

import org.eclipse.jdt.internal.junit.runner.ITestLoader;
import org.eclipse.jdt.internal.junit.runner.ITestReference;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;

public class JUnit4TestLoader implements ITestLoader {
	
	public ITestReference[] loadTests(
			@SuppressWarnings("unchecked") Class[] testClasses, // https://bugs.eclipse.org/bugs/show_bug.cgi?id=164472
			String testName,
			String[] failureNames,
			RemoteTestRunner listener) {
		
		ITestReference[] refs= new ITestReference[testClasses.length];
		for (int i= 0; i < testClasses.length; i++) {
			Class<?> clazz= testClasses[i];
			ITestReference ref= createTest(clazz, testName);
			refs[i]= ref;
		}
		return refs;
	}

	private ITestReference createTest(Class<?> clazz, String testName) {
		if (clazz == null)
			return null;
		if (testName == null)
			return new JUnit4TestClassReference(clazz);
		return new JUnit4TestMethodReference(clazz, testName);
	}
}
