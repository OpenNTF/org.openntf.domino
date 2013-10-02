/**
 * 
 */
package org.openntf.domino.xsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public class OpenntfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public OpenntfServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service(final ServletRequest arg0, final ServletResponse arg1) throws ServletException, IOException {
		super.service(arg0, arg1);

		arg1.getWriter().write(
				"<html><body>This IBM Domino server is using the OpenNTF Domino API. Version: " + Factory.VERSION + "</body></html>");
	}
}
