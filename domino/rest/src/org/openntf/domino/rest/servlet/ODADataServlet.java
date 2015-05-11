package org.openntf.domino.rest.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Factory.ThreadConfig;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.DasCurrentSessionFactory;

import com.ibm.domino.das.servlet.DasServlet;

public class ODADataServlet extends DasServlet {
	private static final long serialVersionUID = 1L;

	public ODADataServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doInit() throws ServletException {
		ODAPlatform.start();
		super.doInit();
	}

	@Override
	public void doDestroy() {
		super.doDestroy();
		ODAPlatform.stop();
	}

	protected ThreadConfig getDataServiceConfig() {
		Fixes[] fixes = Fixes.values();
		AutoMime autoMime = AutoMime.WRAP_32K;
		boolean bubbleExceptions = false;
		return new ThreadConfig(fixes, autoMime, bubbleExceptions);
	}

	@Override
	public void doService(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		Factory.initThread(getDataServiceConfig());
		Factory.setSessionFactory(new DasCurrentSessionFactory(), SessionType.CURRENT);
		super.doService(request, response);
		Factory.termThread();
	}

}
