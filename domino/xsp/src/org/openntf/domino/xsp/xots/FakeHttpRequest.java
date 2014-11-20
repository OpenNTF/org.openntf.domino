package org.openntf.domino.xsp.xots;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import com.ibm.xsp.servlet.local.LocalHttpServletRequest;
import com.ibm.xsp.servlet.local.LocalHttpSession;

public class FakeHttpRequest extends LocalHttpServletRequest {
	HttpSession session = new LocalHttpSession();
	private String userName_;

	//	public FakeHttpRequest() {
	//		super(null, null);
	//	}

	public FakeHttpRequest(final String userName) {
		super(null, null);
		userName_ = userName;
	}

	@Override
	public Principal getUserPrincipal() {
		return new Principal() {
			@Override
			public String getName() {
				return userName_;
			}
		};
	}

	@Override
	public HttpSession getSession(final boolean paramBoolean) {
		return session;
	}
}