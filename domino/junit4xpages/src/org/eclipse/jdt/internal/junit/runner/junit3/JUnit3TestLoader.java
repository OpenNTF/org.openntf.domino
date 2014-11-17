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

package org.eclipse.jdt.internal.junit.runner.junit3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.jdt.internal.junit.runner.FailuresFirstPrioritizer;
import org.eclipse.jdt.internal.junit.runner.ITestLoader;
import org.eclipse.jdt.internal.junit.runner.ITestPrioritizer;
import org.eclipse.jdt.internal.junit.runner.ITestReference;
import org.eclipse.jdt.internal.junit.runner.JUnitMessages;
import org.eclipse.jdt.internal.junit.runner.NullPrioritizer;
import org.eclipse.jdt.internal.junit.runner.RemoteTestRunner;

public class JUnit3TestLoader implements ITestLoader {
	private static final String SUITE_METHODNAME= "suite"; //$NON-NLS-1$
	private static final String SET_UP_TEST_METHOD_NAME = "setUpTest"; //$NON-NLS-1$

	// WANT: give test loaders a schema

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jdt.internal.junit.runner.TestLoader#loadSuites(java.lang.String[],
	 *      java.lang.String, java.lang.String[],
	 *      org.eclipse.jdt.internal.junit.runner.RunFailureListener,
	 *      org.eclipse.jdt.internal.junit.runner.TestIdMap)
	 */
	public ITestReference[] loadTests(Class[] testClasses, String testName, String[] failureNames, RemoteTestRunner listener) {
		// instantiate all tests
		ITestReference[] suites= new ITestReference[testClasses.length];
		ITestPrioritizer prioritizer;

		if (failureNames != null)
			prioritizer= new FailuresFirstPrioritizer(failureNames);
		else
			prioritizer= new NullPrioritizer();

		for (int i= 0; i < suites.length; i++) {
			Class testClassName= testClasses[i];
			Test test= getTest(testClassName, testName, listener);
			prioritizer.prioritize(test);
			suites[i]= new JUnit3TestReference(test);
		}

		return suites;
	}

	private Test createTest(String testName, Class testClass) {
		Class[] classArgs= { String.class };
		Test test;
		Constructor constructor= null;
		try {
			try {
				constructor= testClass.getConstructor(classArgs);
				test= (Test) constructor.newInstance(new Object[] { testName });
			} catch (NoSuchMethodException e) {
				// try the no arg constructor supported in 3.8.1
				constructor= testClass.getConstructor(new Class[0]);
				test= (Test) constructor.newInstance(new Object[0]);
				if (test instanceof TestCase)
					((TestCase) test).setName(testName);
			}
			if (test != null)
				return test;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (ClassCastException e) {
		}
		return warning("Could not create test \'" + testName + "\' "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Test getTest(Class testClass, String testName, RemoteTestRunner failureListener) {
		if (testName != null) {
			return setupTest(testClass, createTest(testName, testClass));
		}
		Method suiteMethod= null;
		try {
			suiteMethod= testClass.getMethod(JUnit3TestLoader.SUITE_METHODNAME, new Class[0]);
		} catch (Exception e) {
			// try to extract a test suite automatically
			return new TestSuite(testClass);
		}
		if (!Modifier.isStatic(suiteMethod.getModifiers())) {
			return warning(JUnitMessages.getString("RemoteTestRunner.error.suite.notstatic"));//$NON-NLS-1$
		}
		try {
			Test test= (Test) suiteMethod.invoke(null, new Class[0]); // static
			if (test != null) {
				return test;
			}
			return warning(JUnitMessages.getString("RemoteTestRunner.error.suite.nullreturn")); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			String message= JUnitMessages.getFormattedString("RemoteTestRunner.error.invoke", e.getTargetException().toString()); //$NON-NLS-1$
			failureListener.runFailed(message, e);
			return new TestSuite(testClass);
		} catch (IllegalAccessException e) {
			String message= JUnitMessages.getFormattedString("RemoteTestRunner.error.invoke", e.toString()); //$NON-NLS-1$
			failureListener.runFailed(message, e);
			return new TestSuite(testClass);
		}
	}

	/**
	 * Prepare a single test to be run standalone. If the test case class
	 * provides a static method Test setUpTest(Test test) then this method will
	 * be invoked. Instead of calling the test method directly the "decorated"
	 * test returned from setUpTest will be called. The purpose of this
	 * mechanism is to enable tests which requires a set-up to be run
	 * individually.
	 *
	 * @param reloadedTestClass test class
	 * @param reloadedTest test instance
	 * @return the reloaded test, or the test wrapped with setUpTest(..) if available
	 */
	private Test setupTest(Class reloadedTestClass, Test reloadedTest) {
		if (reloadedTestClass == null)
			return reloadedTest;

		Method setup= null;
		try {
			setup= reloadedTestClass.getMethod(SET_UP_TEST_METHOD_NAME, new Class[] { Test.class });
		} catch (SecurityException e1) {
			return reloadedTest;
		} catch (NoSuchMethodException e) {
			return reloadedTest;
		}
		if (setup.getReturnType() != Test.class)
			return warning(JUnitMessages.getString("RemoteTestRunner.error.notestreturn")); //$NON-NLS-1$
		if (!Modifier.isPublic(setup.getModifiers()))
			return warning(JUnitMessages.getString("RemoteTestRunner.error.shouldbepublic")); //$NON-NLS-1$
		if (!Modifier.isStatic(setup.getModifiers()))
			return warning(JUnitMessages.getString("RemoteTestRunner.error.shouldbestatic")); //$NON-NLS-1$
		try {
			Test test= (Test) setup.invoke(null, new Object[] { reloadedTest });
			if (test == null)
				return warning(JUnitMessages.getString("RemoteTestRunner.error.nullreturn")); //$NON-NLS-1$
			return test;
		} catch (IllegalArgumentException e) {
			return warning(JUnitMessages.getFormattedString("RemoteTestRunner.error.couldnotinvoke", e)); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			return warning(JUnitMessages.getFormattedString("RemoteTestRunner.error.couldnotinvoke", e)); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			return warning(JUnitMessages.getFormattedString("RemoteTestRunner.error.invocationexception", e.getTargetException())); //$NON-NLS-1$
		}
	}

	/**
	 * @param message warning message
	 * @return a test which will fail and log a warning message.
	 */
	private Test warning(final String message) {
		return new TestCase("warning") { //$NON-NLS-1$
			protected void runTest() {
				fail(message);
			}
		};
	}
}
