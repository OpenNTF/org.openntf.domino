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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import junit.extensions.TestDecorator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FailuresFirstPrioritizer implements ITestPrioritizer {
	private HashSet fPriorities;

	public FailuresFirstPrioritizer(final String[] priorities) {
		fPriorities = new HashSet(Arrays.asList(priorities));
	}

	public Test prioritize(final Test suite) {
		doPrioritize(suite, new ArrayList());
		return suite;
	}

	private void doPrioritize(final Test suite, final List path) {
		if (suite instanceof TestCase) {
			TestCase testCase = (TestCase) suite;
			if (hasPriority(testCase))
				reorder(testCase, path);
		} else if (suite instanceof TestSuite) {
			TestSuite aSuite = (TestSuite) suite;
			path.add(suite);
			loopTests(path, aSuite);
			path.remove(path.size() - 1);
		} else if (suite instanceof TestDecorator) {
			TestDecorator aDecorator = (TestDecorator) suite;
			path.add(aDecorator);
			doPrioritize(aDecorator.getTest(), path);
			path.remove(path.size() - 1);
		}
	}

	private void loopTests(final List path, final TestSuite aSuite) {
		for (Enumeration e = aSuite.tests(); e.hasMoreElements();) {
			doPrioritize((Test) e.nextElement(), path);
		}
	}

	private void reorder(final Test test, final List path) {
		doReorder(test, path, path.size() - 1);
	}

	private void doReorder(final Test test, final List path, final int top) {
		if (top < 0)
			return;
		Test topTest = (Test) path.get(top);
		// only reorder TestSuites
		if (topTest instanceof TestSuite) {
			TestSuite suite = (TestSuite) topTest;
			moveTestToFront(suite, test);
		}
		doReorder(topTest, path, top - 1);
	}

	void moveTestToFront(final TestSuite suite, final Test test) {
		Vector tests = (Vector) getField(suite, "fTests"); //$NON-NLS-1$
		for (int i = 0; i < tests.size(); i++) {
			if (tests.get(i) == test) {
				tests.remove(i);
				tests.insertElementAt(test, 0);
			}
		}
	}

	private boolean hasPriority(final TestCase testCase) {
		return fPriorities.contains(testCase.toString());
	}

	public static Object getField(final Object object, final String fieldName) {
		return getFieldInClass(object, fieldName, object.getClass());
	}

	private static Object getFieldInClass(final Object object, final String fieldName, final Class clazz) {
		Field field = null;
		if (clazz == null)
			return null;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			// fall through
		}
		return getFieldInClass(object, fieldName, clazz.getSuperclass());
	}
}
