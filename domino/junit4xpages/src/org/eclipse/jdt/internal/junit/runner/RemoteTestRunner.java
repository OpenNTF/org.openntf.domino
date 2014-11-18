/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sebastian Davids: sdavids@gmx.de bug 26754
 *******************************************************************************/
package org.eclipse.jdt.internal.junit.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jdt.internal.junit.runner.junit3.JUnit3TestLoader;
import org.openntf.junit4xpages.OsgiTest;
import org.osgi.framework.Bundle;

/**
 * A TestRunner that reports results via a socket connection. See MessageIds for more information about the protocol.
 */
public class RemoteTestRunner implements MessageSender, IVisitsTestTrees {
	private static String BOOTSTRAP_CLASS_LOADER = "com.ibm.domino.http.bootstrap.BootstrapClassLoader";

	/**
	 * Holder for information for a rerun request
	 */
	private static class RerunRequest {
		String fRerunClassName;
		String fRerunTestName;
		int fRerunTestId;

		public RerunRequest(final int testId, final String className, final String testName) {
			fRerunTestId = testId;
			fRerunClassName = className;
			fRerunTestName = testName;
		}

	}

	public static final String RERAN_FAILURE = "FAILURE"; //$NON-NLS-1$

	public static final String RERAN_ERROR = "ERROR"; //$NON-NLS-1$

	public static final String RERAN_OK = "OK"; //$NON-NLS-1$

	/**
	 * The name of the test classes to be executed
	 */
	private String[] fTestClassNames;
	/**
	 * The name of the test (argument -test)
	 */
	private String fTestName;
	/**
	 * The current test result
	 */
	private TestExecution fExecution;

	/**
	 * The version expected by the client
	 */
	private String fVersion = ""; //$NON-NLS-1$

	/**
	 * The client socket.
	 */
	private Socket fClientSocket;
	/**
	 * Print writer for sending messages
	 */
	private PrintWriter fWriter;
	/**
	 * Reader for incoming messages
	 */
	private BufferedReader fReader;
	/**
	 * Host to connect to, default is the localhost
	 */
	private String fHost = ""; //$NON-NLS-1$
	/**
	 * Port to connect to.
	 */
	private int fPort = -1;
	/**
	 * Is the debug mode enabled?
	 */
	private boolean fDebugMode = false;
	/**
	 * Keep the test run server alive after a test run has finished. This allows to rerun tests.
	 */
	private boolean fKeepAlive = false;
	/**
	 * Has the server been stopped
	 */
	private boolean fStopped = false;
	/**
	 * Queue of rerun requests.
	 */
	private Vector fRerunRequests = new Vector(10);
	/**
	 * Thread reading from the socket
	 */
	private ReaderThread fReaderThread;

	private String fRerunTest;

	private final TestIdMap fIds = new TestIdMap();

	private String[] fFailureNames;

	private ITestLoader fLoader;

	private MessageSender fSender;

	private boolean fConsoleMode = false;

	/**
	 * Reader thread that processes messages from the client.
	 */
	private class ReaderThread extends Thread {
		public ReaderThread() {
			super("ReaderThread"); //$NON-NLS-1$
		}

		@Override
		public void run() {
			try {
				String message = null;
				while (true) {
					if ((message = fReader.readLine()) != null) {

						if (message.startsWith(MessageIds.TEST_STOP)) {
							fStopped = true;
							RemoteTestRunner.this.stop();
							synchronized (RemoteTestRunner.this) {
								RemoteTestRunner.this.notifyAll();
							}
							break;
						}

						else if (message.startsWith(MessageIds.TEST_RERUN)) {
							String arg = message.substring(MessageIds.MSG_HEADER_LENGTH);
							//format: testId className testName
							int c0 = arg.indexOf(' ');
							int c1 = arg.indexOf(' ', c0 + 1);
							String s = arg.substring(0, c0);
							int testId = Integer.parseInt(s);
							String className = arg.substring(c0 + 1, c1);
							String testName = arg.substring(c1 + 1, arg.length());
							synchronized (RemoteTestRunner.this) {
								fRerunRequests.add(new RerunRequest(testId, className, testName));
								RemoteTestRunner.this.notifyAll();
							}
						}
					}
				}
			} catch (Exception e) {
				RemoteTestRunner.this.stop();
			}
		}
	}

	public RemoteTestRunner() {
		setMessageSender(this);
	}

	public void setMessageSender(final MessageSender sender) {
		fSender = sender;
	}

	/**
	 * The main entry point.
	 * 
	 * @param args
	 *            Parameters:
	 * 
	 *            <pre>
	 * -classnames: the name of the test suite class
	 * -testfilename: the name of a file containing classnames of test suites
	 * -test: the test method name (format classname testname)
	 * -host: the host to connect to default local host
	 * -port: the port to connect to, mandatory argument
	 * -keepalive: keep the process alive after a test run
	 * </pre>
	 */
	public static void main(final String[] args) {
		boolean osgiPresent = false;
		boolean osgiTest = false;
		try {
			Class<?> bcl = Class.forName(BOOTSTRAP_CLASS_LOADER);
			Method method = bcl.getMethod("getSharedClassLoader");
			ClassLoader sharedCl = (ClassLoader) method.invoke(null);
			osgiPresent = sharedCl != null;
		} catch (Exception rex) {

		}

		try {
			RemoteTestRunner testRunServer = new RemoteTestRunner();
			testRunServer.init(args);

			if (!osgiPresent) {
				osgiTest = testRunServer.hasOsgiTests();
			}

			if (osgiTest) {
				runAsOsgi(args);
			} else {
				testRunServer.run();
			}
		} catch (Throwable e) {
			e.printStackTrace(); // don't allow System.exit(0) to swallow exceptions
		} finally {
			// fix for 14434
			if (!osgiPresent)
				System.exit(0);
		}
	}

	protected boolean hasOsgiTests() {
		Set<URL> osgiClassPaths = new HashSet<URL>();
		for (Class<?> c : loadClasses(fTestClassNames)) {
			OsgiTest annot = c.getAnnotation(OsgiTest.class);
			if (annot != null) {
				if (annot.value() == null) {
					// nop
				} else if (annot.value().equals("")) {
					// nop
				} else if (System.getProperty("junit4xpages.bundle", "").equals("")) {
					System.setProperty("junit4xpages.bundle", annot.value());
				}
				osgiClassPaths.add(c.getProtectionDomain().getCodeSource().getLocation());
			}
		}

		if (osgiClassPaths.isEmpty())
			return false;
		StringBuilder sb = new StringBuilder();
		for (URL u : osgiClassPaths) {
			if (sb.length() > 0)
				sb.append(';');
			sb.append(u.toString());
		}

		System.setProperty("junit4xpages.classpath", sb.toString());
		return true;

	}

	private static void runAsOsgi(final String[] args) {
		try {
			System.out.println("[JUnit4XPages] starting framework");

			Class<?> bcl = Class.forName(BOOTSTRAP_CLASS_LOADER);
			Method method = bcl.getMethod("findClass", String.class);

			Class<?> beClass = (Class<?>) method.invoke(null, "com.ibm.designer.runtime.domino.bootstrap.BootstrapEnvironment");

			// BootstrapEnvironment be = BootstrapEnvironment.getInstance();
			Object /* BootstrapEnvironment */be = beClass.getMethod("getInstance").invoke(null);

			// LCDRequestHandler lcdReqHandler = be.getRequestHandler();
			Object /* LCDEnvironment */lcdReqHandler = be.getClass().getMethod("getLCDRequestHandler").invoke(be);

			// lcdReqHandler.initialize();
			lcdReqHandler.getClass().getMethod("initialize").invoke(lcdReqHandler);
			System.out.println("[JUnit4XPages] framework started");

			try {
				// Now start the runner = this class INSIDE the framework
				ClassLoader sharedCl = (ClassLoader) bcl.getMethod("getSharedClassLoader").invoke(null);

				// load the Runner from the xsp-bundle
				Class platformCl = sharedCl.loadClass("org.eclipse.core.runtime.Platform");

				Object bundle = platformCl.getMethod("getBundle", String.class).invoke(null, "org.openntf.junit4xpages");
				method = bundle.getClass().getMethod("loadClass", String.class);
				Class osgiRunnerCl = (Class) method.invoke(bundle, RemoteTestRunner.class.getName());

				Thread.currentThread().setContextClassLoader(osgiRunnerCl.getClassLoader());
				System.out.println("[JUnit4XPages] starting OSGI-Runner");

				osgiRunnerCl.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });

			} finally {

				System.out.println("[JUnit4XPages] stopping framework");
				// lcdReqHandler.stop();
				method = lcdReqHandler.getClass().getMethod("destroy");
				method.invoke(lcdReqHandler);
				System.out.println("[JUnit4XPages] framework stopped");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse command line arguments. Hook for subclasses to process additional arguments.
	 * 
	 * @param args
	 *            the arguments
	 */
	protected void init(final String[] args) {
		defaultInit(args);
	}

	private ClassLoader classLoader;

	/**
	 * The class loader to be used for loading tests. Subclasses may override to use another class loader.
	 * 
	 * @return the class loader to lead test classes
	 */
	protected ClassLoader getTestClassLoader() {
		if (classLoader == null) {
			String cp = System.getProperty("junit4xpages.classpath");
			if (cp == null) {
				classLoader = getClass().getClassLoader();
			} else {
				String[] paths = cp.split(";");
				URL[] urls = new URL[paths.length];
				for (int i = 0; i < paths.length; i++) {
					try {
						urls[i] = new URL(paths[i]);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				ClassLoader cl1 = getClass().getClassLoader();
				ClassLoader cl2 = Thread.currentThread().getContextClassLoader();

				String bundleName = System.getProperty("junit4xpages.bundle", "org.openntf.domino.xsp");
				final Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(bundleName);

				classLoader = new URLClassLoader(urls, null) {
					@Override
					public java.lang.Class<?> loadClass(final String className) throws ClassNotFoundException {
						Class<?> ret;
						try {
							ret = super.loadClass(className);
							//System.out.println("super.loadClass(" + className + ")");
							return ret;
						} catch (ClassNotFoundException cnf) {
							ret = bundle.loadClass(className);
							//System.out.println("bundle.loadClass(" + className + ")");
							return ret;
						}
					}

					@Override
					public URL getResource(final String resName) {
						// TODO Auto-generated method stub
						URL ret = super.getResource(resName);
						if (ret == null) {
							ret = bundle.getResource(resName);
						}
						return ret;
					}

				};
			}
		}
		return classLoader;

	}

	/**
	 * Process the default arguments.
	 * 
	 * @param args
	 *            arguments
	 */
	protected final void defaultInit(final String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].toLowerCase().equals("-classnames") || args[i].toLowerCase().equals("-classname")) { //$NON-NLS-1$ //$NON-NLS-2$
				Vector list = new Vector();
				for (int j = i + 1; j < args.length; j++) {
					if (args[j].startsWith("-")) //$NON-NLS-1$
						break;
					list.add(args[j]);
				}
				fTestClassNames = (String[]) list.toArray(new String[list.size()]);
			} else if (args[i].toLowerCase().equals("-test")) { //$NON-NLS-1$
				String testName = args[i + 1];
				int p = testName.indexOf(':');
				if (p == -1)
					throw new IllegalArgumentException("Testname not separated by \'%\'"); //$NON-NLS-1$
				fTestName = testName.substring(p + 1);
				fTestClassNames = new String[] { testName.substring(0, p) };
				i++;
			} else if (args[i].toLowerCase().equals("-testnamefile")) { //$NON-NLS-1$
				String testNameFile = args[i + 1];
				try {
					readTestNames(testNameFile);
				} catch (IOException e) {
					throw new IllegalArgumentException("Cannot read testname file.");		 //$NON-NLS-1$
				}
				i++;

			} else if (args[i].toLowerCase().equals("-testfailures")) { //$NON-NLS-1$
				String testFailuresFile = args[i + 1];
				try {
					readFailureNames(testFailuresFile);
				} catch (IOException e) {
					throw new IllegalArgumentException("Cannot read testfailures file.");		 //$NON-NLS-1$
				}
				i++;

			} else if (args[i].toLowerCase().equals("-port")) { //$NON-NLS-1$
				fPort = Integer.parseInt(args[i + 1]);
				i++;
			} else if (args[i].toLowerCase().equals("-host")) { //$NON-NLS-1$
				fHost = args[i + 1];
				i++;
			} else if (args[i].toLowerCase().equals("-rerun")) { //$NON-NLS-1$
				fRerunTest = args[i + 1];
				i++;
			} else if (args[i].toLowerCase().equals("-keepalive")) { //$NON-NLS-1$
				fKeepAlive = true;
			} else if (args[i].toLowerCase().equals("-debugging") || args[i].toLowerCase().equals("-debug")) { //$NON-NLS-1$ //$NON-NLS-2$
				fDebugMode = true;
			} else if (args[i].toLowerCase().equals("-version")) { //$NON-NLS-1$
				fVersion = args[i + 1];
				i++;
			} else if (args[i].toLowerCase().equals("-junitconsole")) { //$NON-NLS-1$
				fConsoleMode = true;
			} else if (args[i].toLowerCase().equals("-testloaderclass")) { //$NON-NLS-1$
				String className = args[i + 1];
				createLoader(className);
				i++;
			}
		}

		if (getTestLoader() == null)
			initDefaultLoader();

		if (fTestClassNames == null || fTestClassNames.length == 0)
			throw new IllegalArgumentException(JUnitMessages.getString("RemoteTestRunner.error.classnamemissing")); //$NON-NLS-1$

		if (fPort == -1)
			throw new IllegalArgumentException(JUnitMessages.getString("RemoteTestRunner.error.portmissing")); //$NON-NLS-1$
		if (fDebugMode)
			System.out.println("keepalive " + fKeepAlive); //$NON-NLS-1$
	}

	public void initDefaultLoader() {
		createLoader(JUnit3TestLoader.class.getName());
	}

	public void createLoader(final String className) {
		setLoader(createRawTestLoader(className));
	}

	protected ITestLoader createRawTestLoader(final String className) {
		try {
			return (ITestLoader) loadTestLoaderClass(className).newInstance();
		} catch (Exception e) {
			StringWriter trace = new StringWriter();
			e.printStackTrace(new PrintWriter(trace));
			String message = JUnitMessages.getFormattedString(
					"RemoteTestRunner.error.invalidloader", new Object[] { className, trace.toString() }); //$NON-NLS-1$
			throw new IllegalArgumentException(message);
		}
	}

	protected Class loadTestLoaderClass(final String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	public void setLoader(final ITestLoader newInstance) {
		fLoader = newInstance;
	}

	private void readTestNames(final String testNameFile) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(testNameFile)), "UTF-8")); //$NON-NLS-1$
		try {
			String line;
			Vector list = new Vector();
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			fTestClassNames = (String[]) list.toArray(new String[list.size()]);
		} finally {
			br.close();
		}
		if (fDebugMode) {
			System.out.println("Tests:"); //$NON-NLS-1$
			for (int i = 0; i < fTestClassNames.length; i++) {
				System.out.println("    " + fTestClassNames[i]); //$NON-NLS-1$
			}
		}
	}

	private void readFailureNames(final String testFailureFile) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(testFailureFile)), "UTF-8")); //$NON-NLS-1$
		try {
			String line;
			Vector list = new Vector();
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			fFailureNames = (String[]) list.toArray(new String[list.size()]);
		} finally {
			br.close();
		}
		if (fDebugMode) {
			System.out.println("Failures:"); //$NON-NLS-1$
			for (int i = 0; i < fFailureNames.length; i++) {
				System.out.println("    " + fFailureNames[i]); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Connects to the remote ports and runs the tests.
	 */
	protected void run() {
		if (!connect())
			return;
		if (fRerunTest != null) {
			rerunTest(new RerunRequest(Integer.parseInt(fRerunTest), fTestClassNames[0], fTestName));
			return;
		}

		FirstRunExecutionListener listener = firstRunExecutionListener();
		fExecution = new TestExecution(listener, getClassifier());
		runTests(fExecution);
		if (fKeepAlive)
			waitForReruns();

		shutDown();

	}

	public FirstRunExecutionListener firstRunExecutionListener() {
		return new FirstRunExecutionListener(fSender, fIds);
	}

	/**
	 * Waits for rerun requests until an explicit stop request
	 */
	private synchronized void waitForReruns() {
		while (!fStopped) {
			try {
				wait();
				if (!fStopped && fRerunRequests.size() > 0) {
					RerunRequest r = (RerunRequest) fRerunRequests.remove(0);
					rerunTest(r);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public void runFailed(final String message, final Exception exception) {
		//TODO: remove System.err.println?
		System.err.println(message);
		if (exception != null)
			exception.printStackTrace(System.err);
	}

	protected Class[] loadClasses(final String[] testClassNames) {
		Vector classes = new Vector();
		for (int i = 0; i < testClassNames.length; i++) {
			String name = testClassNames[i];
			Class clazz = loadClass(name, this);
			if (clazz != null) {
				classes.add(clazz);
			}
		}
		return (Class[]) classes.toArray(new Class[classes.size()]);
	}

	protected void notifyListenersOfTestEnd(final TestExecution execution, final long testStartTime) {
		if (execution == null || execution.shouldStop())
			notifyTestRunStopped(System.currentTimeMillis() - testStartTime);
		else
			notifyTestRunEnded(System.currentTimeMillis() - testStartTime);
	}

	/**
	 * Runs a set of tests.
	 * 
	 * @param testClassNames
	 *            classes to be run
	 * @param testName
	 *            individual method to be run
	 * @param execution
	 *            executor
	 */
	public void runTests(final String[] testClassNames, final String testName, final TestExecution execution) {
		ITestReference[] suites = fLoader.loadTests(loadClasses(testClassNames), testName, fFailureNames, this);

		// count all testMethods and inform ITestRunListeners
		int count = countTests(suites);

		notifyTestRunStarted(count);

		if (count == 0) {
			notifyTestRunEnded(0);
			return;
		}

		sendTrees(suites);

		long testStartTime = System.currentTimeMillis();
		execution.run(suites);

		notifyListenersOfTestEnd(execution, testStartTime);
	}

	private void sendTrees(final ITestReference[] suites) {
		long startTime = System.currentTimeMillis();
		if (fDebugMode)
			System.out.print("start send tree..."); //$NON-NLS-1$
		for (int i = 0; i < suites.length; i++) {
			suites[i].sendTree(this);
		}
		if (fDebugMode)
			System.out.println("done send tree - time(ms): " + (System.currentTimeMillis() - startTime)); //$NON-NLS-1$
	}

	private int countTests(final ITestReference[] tests) {
		int count = 0;
		for (int i = 0; i < tests.length; i++) {
			ITestReference test = tests[i];
			if (test != null)
				count = count + test.countTestCases();
		}
		return count;
	}

	/**
	 * Reruns a test as defined by the fully qualified class name and the name of the test.
	 * 
	 * @param r
	 *            rerun request
	 */
	public void rerunTest(final RerunRequest r) {
		final Class[] classes = loadClasses(new String[] { r.fRerunClassName });
		ITestReference rerunTest1 = fLoader.loadTests(classes, r.fRerunTestName, null, this)[0];
		RerunExecutionListener service = rerunExecutionListener();

		TestExecution execution = new TestExecution(service, getClassifier());
		ITestReference[] suites = new ITestReference[] { rerunTest1 };
		execution.run(suites);

		notifyRerunComplete(r, service.getStatus());
	}

	public RerunExecutionListener rerunExecutionListener() {
		return new RerunExecutionListener(fSender, fIds);
	}

	protected IClassifiesThrowables getClassifier() {
		return new DefaultClassifier(fVersion);
	}

	public void visitTreeEntry(final ITestIdentifier id, final boolean b, final int i) {
		notifyTestTreeEntry(getTestId(id) + ',' + escapeComma(id.getName()) + ',' + b + ',' + i);
	}

	private String escapeComma(final String s) {
		if ((s.indexOf(',') < 0) && (s.indexOf('\\') < 0))
			return s;
		StringBuffer sb = new StringBuffer(s.length() + 10);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ',')
				sb.append("\\,"); //$NON-NLS-1$
			else if (c == '\\')
				sb.append("\\\\"); //$NON-NLS-1$
			else
				sb.append(c);
		}
		return sb.toString();
	}

	// WANT: work in bug fixes since RC2?
	private String getTestId(final ITestIdentifier id) {
		return fIds.getTestId(id);
	}

	/**
	 * Stop the current test run.
	 */
	protected void stop() {
		if (fExecution != null) {
			fExecution.stop();
		}
	}

	/**
	 * Connect to the remote test listener.
	 * 
	 * @return <code>true</code> if connection successful, <code>false</code> if failed
	 */
	protected boolean connect() {
		if (fConsoleMode) {
			fClientSocket = null;
			fWriter = new PrintWriter(System.out);
			fReader = new BufferedReader(new InputStreamReader(System.in));
			fReaderThread = new ReaderThread();
			fReaderThread.start();
			return true;
		}
		if (fDebugMode)
			System.out.println("RemoteTestRunner: trying to connect" + fHost + ":" + fPort); //$NON-NLS-1$ //$NON-NLS-2$
		Exception exception = null;
		for (int i = 1; i < 20; i++) {
			try {
				fClientSocket = new Socket(fHost, fPort);
				try {
					fWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fClientSocket.getOutputStream(), "UTF-8")), false/*true*/); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e1) {
					fWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fClientSocket.getOutputStream())), false/*true*/);
				}
				try {
					fReader = new BufferedReader(new InputStreamReader(fClientSocket.getInputStream(), "UTF-8")); //$NON-NLS-1$
				} catch (UnsupportedEncodingException e1) {
					fReader = new BufferedReader(new InputStreamReader(fClientSocket.getInputStream()));
				}
				fReaderThread = new ReaderThread();
				fReaderThread.start();
				return true;
			} catch (IOException e) {
				exception = e;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
		runFailed(
				JUnitMessages.getFormattedString("RemoteTestRunner.error.connect", new String[] { fHost, Integer.toString(fPort) }), exception); //$NON-NLS-1$
		return false;
	}

	/**
	 * Shutsdown the connection to the remote test listener.
	 */
	private void shutDown() {
		if (fWriter != null) {
			fWriter.close();
			fWriter = null;
		}
		try {
			if (fReaderThread != null) {
				// interrupt reader thread so that we don't block on close
				// on a lock held by the BufferedReader
				// fix for bug: 38955
				try {
					fReaderThread.interrupt();
				} catch (java.lang.NullPointerException NPE) {
					// Bug in NotesAgentManager - throws a NPE if there is no ThreadGroup present
				}
			}
			if (fReader != null) {
				fReader.close();
				fReader = null;
			}
		} catch (IOException e) {
			if (fDebugMode)
				e.printStackTrace();
		}

		try {
			if (fClientSocket != null) {
				fClientSocket.close();
				fClientSocket = null;
			}
		} catch (IOException e) {
			if (fDebugMode)
				e.printStackTrace();
		}
	}

	/*
	 * @see org.eclipse.jdt.internal.junit.runner.MessageSender#sendMessage(java.lang.String)
	 */
	public void sendMessage(final String msg) {
		if (fWriter == null)
			return;
		fWriter.println(msg);
		//		if (!fConsoleMode)
		//			System.out.println(msg);
	}

	protected void notifyTestRunStarted(final int testCount) {
		fSender.sendMessage(MessageIds.TEST_RUN_START + testCount + " " + "v2"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void notifyTestRunEnded(final long elapsedTime) {
		fSender.sendMessage(MessageIds.TEST_RUN_END + elapsedTime);
		fSender.flush();
		//shutDown();
	}

	protected void notifyTestRunStopped(final long elapsedTime) {
		fSender.sendMessage(MessageIds.TEST_STOPPED + elapsedTime);
		fSender.flush();
		//shutDown();
	}

	protected void notifyTestTreeEntry(final String treeEntry) {
		fSender.sendMessage(MessageIds.TEST_TREE + treeEntry);
	}

	/*
	 * @see org.eclipse.jdt.internal.junit.runner.RerunCompletionListener#notifyRerunComplete(org.eclipse.jdt.internal.junit.runner.RerunRequest,
	 *      java.lang.String)
	 */
	public void notifyRerunComplete(final RerunRequest r, final String status) {
		if (fPort != -1) {
			fSender.sendMessage(MessageIds.TEST_RERAN + r.fRerunTestId + " " + r.fRerunClassName + " " + r.fRerunTestName + " " + status); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			fSender.flush();
		}
	}

	public void flush() {
		fWriter.flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jdt.internal.junit.runner.TestRunner#runTests(org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.TestExecution)
	 */
	public void runTests(final TestExecution execution) {
		runTests(fTestClassNames, fTestName, execution);
	}

	public ITestLoader getTestLoader() {
		return fLoader;
	}

	public Class loadClass(final String className, final RemoteTestRunner listener) {
		Class clazz = null;
		try {
			clazz = getTestClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			listener.runFailed(JUnitMessages.getFormattedString("RemoteTestRunner.error.classnotfound", className), e); //$NON-NLS-1$
		}
		return clazz;
	}
}
