package org.openntf.domino.thread.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openntf.domino.session.ISessionFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface XotsTasklet {
	public interface Interface {

		public ISessionFactory getSessionFactory();

		public String getRunAs();

		public String getSigner();

		public Scope getScope();

		public Context getContext();

		//public void setAccessControlContext(AccessControlContext context);

		//public AccessControlContext getAccessControlContext();

		//public void setSession(final lotus.domino.Session session);

		// public org.openntf.domino.Session getSession();

		//public void setRunAs(String username);

		//public void setDominoExecutionContext(DominoExecutionContext tctx);

		//public DominoExecutionContext getDominoExecutionContext();

		public void setCurrentThread(Thread thread);

		public void stop(final boolean force);
	}

	SessionType session();

	Scope scope();

	Context context();

}
