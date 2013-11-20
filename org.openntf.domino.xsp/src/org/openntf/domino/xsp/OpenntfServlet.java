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

	public OpenntfServlet() {
		super();
		System.out.println("Creating new OpenntfServlet object!");
	}

	@Override
	public void service(final ServletRequest arg0, final ServletResponse arg1) throws ServletException, IOException {
		super.service(arg0, arg1);
		String username = "";
		if (arg0 instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) arg0;
			Principal principal = request.getUserPrincipal();
			username = principal.getName();
		}
		arg1.getWriter().write(
				"<html><body>This IBM Domino server is using the " + org.openntf.domino.utils.Factory.getTitle() + " Version: "
						+ org.openntf.domino.utils.Factory.getVersion() + ".<p/>Find out more at <a href=\""
						+ org.openntf.domino.utils.Factory.getUrl() + "\">OpenNTF</a><p/>" + "Request Principal: " + username + "<p/>" + ""
						+ NSA.INSTANCE.getReport() + "</body></html>");
	}

}
