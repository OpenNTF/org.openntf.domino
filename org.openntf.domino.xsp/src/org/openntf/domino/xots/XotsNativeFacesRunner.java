package org.openntf.domino.xots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lotus.notes.NotesThread;

import org.openntf.domino.Database;
import org.openntf.domino.thread.DominoNativeRunner;

import com.ibm.commons.util.NotImplementedException;
import com.ibm.designer.runtime.domino.adapter.servlet.LCDAdapterHttpSession;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsNativeFacesRunner extends DominoNativeRunner {
	protected NSFComponentModule module_;

	public XotsNativeFacesRunner(final Runnable runnable) {
		super(runnable);
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNativeRunner.class.getName());
		}
		initModule();
	}

	public XotsNativeFacesRunner(final Runnable runnable, final ClassLoader classLoader) {
		super(runnable, classLoader);
		initModule();
	}

	public XotsNativeFacesRunner(final Runnable runnable, final NSFComponentModule module) {
		super(runnable);
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNativeRunner.class.getName());
		}
		module_ = module;
	}

	public XotsNativeFacesRunner(final Runnable runnable, final NSFComponentModule module, final ClassLoader classLoader) {
		super(runnable, classLoader);
		module_ = module;
	}

	private void initModule() {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if (ctx != null) {
			module_ = ctx.getRunningModule();
		} else {
			if (classLoader_ == null) {
				classLoader_ = XotsNativeFacesRunner.class.getClassLoader();
			} else if (classLoader_ instanceof ModuleClassLoader) {
				throw new IllegalArgumentException("Can't queue a " + XotsNativeFacesRunner.class.getName()
						+ " without a current NotesContext.");
			} else {
				classLoader_ = XotsNativeFacesRunner.class.getClassLoader();
			}
		}
	}

	@Override
	protected void preRun() {
		if (module_ != null) {
			NotesContext nctx = new NotesContext(module_);
			NotesContext.initThread(nctx);

			LCDAdapterHttpSession httpSession = LCDAdapterHttpSession.getAdaptedSession(module_, "sometestid" + new Date(),
					module_.getServletContext());
			HttpServletRequest request = new StubHttpServletRequest(httpSession, new StubServerPrincipal());
			HttpServletResponse response = new StubHttpServletResponse();
			try {
				nctx.initRequest(request);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
			contextFactory.getFacesContext(module_.getServletContext(), request, response, dummyLifeCycle);
		} else {
			NotesThread.sinitThread();
		}
		super.preRun();

		if (module_ != null) {
			Database database = getSession().getDatabase(module_.getDatabasePath());
			getSession().setCurrentDatabase(database);
		}
	}

	@Override
	protected void postRun() {
		super.postRun();
		if (module_ != null) {
			NotesContext.termThread();
		} else {
			NotesThread.stermThread();
		}
	}

	private static Lifecycle dummyLifeCycle = new Lifecycle() {
		@Override
		public void render(final FacesContext context) throws FacesException {
			throw new NotImplementedException();
		}

		@Override
		public void removePhaseListener(final PhaseListener listener) {
			throw new NotImplementedException();
		}

		@Override
		public PhaseListener[] getPhaseListeners() {
			throw new NotImplementedException();
		}

		@Override
		public void execute(final FacesContext context) throws FacesException {
			throw new NotImplementedException();
		}

		@Override
		public void addPhaseListener(final PhaseListener listener) {
			throw new NotImplementedException();
		}
	};

	public static class StubServerPrincipal implements Principal {

		@Override
		public String getName() {
			return "CN=Pelias-L/O=Frost";
		}

	}

	public static class StubHttpServletRequest implements HttpServletRequest {
		private final HttpSession session_;
		private final Principal principal_;

		private void debug(final Object message) {
			System.out.println(">>> " + getClass().getName() + " >>> " + message);
		}

		public StubHttpServletRequest(final HttpSession session, final Principal principal) {
			session_ = session;
			principal_ = principal;
		}

		@Override
		public Object getAttribute(final String arg0) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration getAttributeNames() {
			return Collections.enumeration(new ArrayList<String>());
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public int getContentLength() {
			return 0;
		}

		@Override
		public String getContentType() {
			return "";
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public String getLocalAddr() {
			return "127.0.0.1";
		}

		@Override
		public String getLocalName() {
			return null;
		}

		@Override
		public int getLocalPort() {
			return 80;
		}

		@Override
		public Locale getLocale() {
			return Locale.getDefault();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration getLocales() {
			return Collections.enumeration(Arrays.asList(Locale.getAvailableLocales()));
		}

		@Override
		public String getParameter(final String arg0) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Map getParameterMap() {
			return Collections.emptyMap();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration getParameterNames() {
			return Collections.enumeration(new ArrayList<String>());
		}

		@Override
		public String[] getParameterValues(final String arg0) {
			return new String[] {};
		}

		@Override
		public String getProtocol() {
			return "http";
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return null;
		}

		@Override
		public String getRealPath(final String arg0) {
			debug("asked for RealPath");
			return null;
		}

		@Override
		public String getRemoteAddr() {
			return "127.0.0.1";
		}

		@Override
		public String getRemoteHost() {
			return "127.0.0.1";
		}

		@Override
		public int getRemotePort() {
			return 80;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(final String arg0) {
			debug("asked for RequestDispatcher");
			return null;
		}

		@Override
		public String getScheme() {
			debug("asked for Scheme");
			return null;
		}

		@Override
		public String getServerName() {
			return "127.0.0.1";
		}

		@Override
		public int getServerPort() {
			return 80;
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public void removeAttribute(final String arg0) {

		}

		@Override
		public void setAttribute(final String arg0, final Object arg1) {

		}

		@Override
		public void setCharacterEncoding(final String arg0) throws UnsupportedEncodingException {

		}

		@Override
		public String getAuthType() {
			return null;
		}

		@Override
		public String getContextPath() {
			debug("asked for ContextPath");
			return null;
		}

		@Override
		public Cookie[] getCookies() {
			return new Cookie[] {};
		}

		@Override
		public long getDateHeader(final String arg0) {
			return 0;
		}

		@Override
		public String getHeader(final String arg0) {
			if ("User-Agent".equals(arg0)) {
				return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0";
			}
			return "";
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration getHeaderNames() {
			return Collections.enumeration(new ArrayList<String>());
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Enumeration getHeaders(final String arg0) {
			return Collections.enumeration(new ArrayList<String>());
		}

		@Override
		public int getIntHeader(final String arg0) {
			return 0;
		}

		@Override
		public String getMethod() {
			return "GET";
		}

		@Override
		public String getPathInfo() {
			debug("asked for PathInfo");
			return "";
		}

		@Override
		public String getPathTranslated() {
			debug("asked for PathTranslated");
			return "";
		}

		@Override
		public String getQueryString() {
			return "";
		}

		@Override
		public String getRemoteUser() {
			return getUserPrincipal().getName();
		}

		@Override
		public String getRequestURI() {
			debug("asked for RequestURI");
			return "";
		}

		@Override
		public StringBuffer getRequestURL() {
			return new StringBuffer();
		}

		@Override
		public String getRequestedSessionId() {
			return session_.getId();
		}

		@Override
		public String getServletPath() {
			return "/xsp/nonesuch";
		}

		@Override
		public HttpSession getSession() {
			return session_;
		}

		@Override
		public HttpSession getSession(final boolean arg0) {
			return session_;
		}

		@Override
		public Principal getUserPrincipal() {
			return principal_;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromUrl() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return true;
		}

		@Override
		public boolean isUserInRole(final String arg0) {
			return false;
		}

	}

	public static class StubHttpServletResponse implements HttpServletResponse {

		@Override
		public void flushBuffer() throws IOException {

		}

		@Override
		public int getBufferSize() {
			return 0;
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return null;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return null;
		}

		@Override
		public boolean isCommitted() {
			return false;
		}

		@Override
		public void reset() {

		}

		@Override
		public void resetBuffer() {

		}

		@Override
		public void setBufferSize(final int arg0) {

		}

		@Override
		public void setCharacterEncoding(final String arg0) {

		}

		@Override
		public void setContentLength(final int arg0) {

		}

		@Override
		public void setContentType(final String arg0) {

		}

		@Override
		public void setLocale(final Locale arg0) {

		}

		@Override
		public void addCookie(final Cookie arg0) {

		}

		@Override
		public void addDateHeader(final String arg0, final long arg1) {

		}

		@Override
		public void addHeader(final String arg0, final String arg1) {

		}

		@Override
		public void addIntHeader(final String arg0, final int arg1) {

		}

		@Override
		public boolean containsHeader(final String arg0) {
			return false;
		}

		@Override
		public String encodeRedirectURL(final String arg0) {
			return null;
		}

		@Override
		public String encodeRedirectUrl(final String arg0) {
			return null;
		}

		@Override
		public String encodeURL(final String arg0) {
			return null;
		}

		@Override
		public String encodeUrl(final String arg0) {
			return null;
		}

		@Override
		public void sendError(final int arg0) throws IOException {

		}

		@Override
		public void sendError(final int arg0, final String arg1) throws IOException {

		}

		@Override
		public void sendRedirect(final String arg0) throws IOException {

		}

		@Override
		public void setDateHeader(final String arg0, final long arg1) {

		}

		@Override
		public void setHeader(final String arg0, final String arg1) {

		}

		@Override
		public void setIntHeader(final String arg0, final int arg1) {

		}

		@Override
		public void setStatus(final int arg0) {

		}

		@Override
		public void setStatus(final int arg0, final String arg1) {

		}

	}
}
