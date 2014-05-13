/**
 * 
 */
package org.openntf.domino.xsp;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.openntf.domino.xsp.helpers.NSA;

/**
 * @author Nathan T. Freeman
 * 
 */
public class OpenntfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public OpenntfServlet() {
		super();
		System.out.println("Creating new OpenntfServlet object!");
		// IndexDatabase index = new IndexDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(final ServletRequest servletRequest, final ServletResponse servletResponse) throws ServletException, IOException {
		super.service(servletRequest, servletResponse);
		String username = "";
		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			Principal principal = request.getUserPrincipal();
			username = principal.getName();
		}
		servletResponse.getWriter().write(
				"<html><body><p>This IBM Domino server is using the " + org.openntf.domino.utils.Factory.getTitle() + " Version: "
						+ org.openntf.domino.utils.Factory.getVersion() + ".</p><p>Find out more at <a href=\""
						+ org.openntf.domino.utils.Factory.getUrl() + "\">OpenNTF</a></p><p>" + "Request Principal: " + username + "</p>"
						+ NSA.INSTANCE.getReport() + "</body></html>");
	}

}
