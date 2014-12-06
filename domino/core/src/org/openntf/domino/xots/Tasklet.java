package org.openntf.domino.xots;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface Tasklet {
	public interface Interface {

		public ISessionFactory getSessionFactory();

		//use a named session factory instead
		//public String getRunAs();

		public Scope getScope();

		public Context getContext();

		//public void setAccessControlContext(AccessControlContext context);

		//public AccessControlContext getAccessControlContext();

		//public void setSession(final lotus.domino.Session session);

		// public org.openntf.domino.Session getSession();

		//public void setRunAs(String username);

		//public void setDominoExecutionContext(DominoExecutionContext tctx);

		//public DominoExecutionContext getDominoExecutionContext();

		/**
		 * Set the thread. Needed to interrupt
		 * 
		 */
		public void setCurrentThread(Thread thread);

		public void stop(final boolean force);
	}

	/**
	 * The Scope determines the concurrency
	 */
	public enum Scope {
		/**
		 * One instance per server is allowed
		 */
		SERVER,

		/**
		 * One instance per template
		 */
		TEMPLATE,

		/**
		 * One instance per application
		 */
		APPLICATION,

		/**
		 * One instance per server and user is allowed
		 */
		SERVER_USER,
		/**
		 * One instance per template and user is allowed
		 */
		TEMPLATE_USER,
		/**
		 * One instance per application and user is allowed
		 */
		APPLICATION_USER,

		/**
		 * No scope, you can run as many instances as you have resource
		 */
		NONE
	}

	/**
	 * The session type for XOTS Tasklets: Note that CLONE, CLONE_FULL_ACCESS, SIGNER, SIGNER_FULL_ACCESS are XPage-Sessions and may be
	 * restricted by maximumInternetAccess
	 * 
	 * @author Roland Praml, FOCONIS AG
	 * 
	 */
	public enum Session {

		/**
		 * Clones the {@link SessionType#CURRENT CURRENT} Session from the current context. This means if you are running in a XPage, you
		 * have the same user session in your XOTS Runnable. If you are running a scheduled servlet, you have a
		 * {@link Factory.SessionType#SESSION_AS_SIGNER SESSION_AS_SIGNER}.
		 * 
		 * This Session may be created with createXPageSession and may be restricted to maximum internet access!
		 */
		CLONE,

		/**
		 * @See {@link #CLONE} but with full access
		 */
		CLONE_FULL_ACCESS,

		/**
		 * Run as signer of that Runnable.class. If the Runnable is inside an NSF. It will run with a named session of the Signer. If the
		 * Runnable is a Plugin-Java object, it will run with the Server-ID
		 * 
		 * This Session may be created with createXPageSession and may be restricted to maximum internet access!
		 */
		SIGNER,

		/**
		 * @See {@link #SIGNER} but with full access
		 */
		SIGNER_FULL_ACCESS,

		/**
		 * Use the native session. Session is created with NotesFactory.createSession()
		 * 
		 * User and Effective user is the current server (or on client the current user) This Session should not be restricted by maximum
		 * internet access!
		 */
		NATIVE,

		/**
		 * Use a full access session. Session is created with NotesFactory.createSessionWithFullAccess()
		 * 
		 * Access is not restricted to readers/authors fields
		 */
		FULL_ACCESS,

		/**
		 * Use a trusted session. Session is created with NotesFactory.createTrustedSession()<br>
		 * 
		 * Applications running on a server installation that need to access databases on a remote server must have either a Trusted Server
		 * relationship, or a Trusted Session. The userID authority that the application is running under must be accounted for in the ACL
		 * of the remote database. That userID is often the serverID.
		 * 
		 * <font color=red>This does NOT yet work</font>
		 */
		TRUSTED,

		/**
		 * Do not create a session
		 */
		NONE
	}

	public enum Context {
		/**
		 * Run the Tasklet in Plugin context (this does NOT open/lock a module, and the code of that Tasklet MUST not reside in the NSF)
		 */
		PLUGIN,

		/**
		 * Run in Default Context. This means in MODULE, if there is one running or in PLUGIN-Context if not
		 */
		DEFAULT,

		/**
		 * Run the Tasklet in Module context (this does NOT work outside OSGI-Environment)
		 */
		MODULE,

		/**
		 * Run in XSP Context: with access to the Xsp dependencies, but without any context (faces or otherwise)
		 */
		@Deprecated
		XSPBARE,

		/**
		 * would be "it's running with access to the scoped variables within it's environment." So that would set up access to
		 * ApplicationScoped, ServerScope, IdentityScope in it
		 */
		@Deprecated
		XSPSCOPED,
		/**
		 * would force the Tasklet to run in the same NSFComponentModule context as any given Xpage this would force the Application to be
		 * activated, thus triggering ApplicationListeners, for instance
		 */
		@Deprecated
		XSPFORCE
	}

	Tasklet.Session session() default Tasklet.Session.CLONE;

	Tasklet.Scope scope() default Tasklet.Scope.APPLICATION;

	Tasklet.Context context() default Tasklet.Context.DEFAULT;

	/**
	 * Defines if this tasklet is public. I.e. is runnable/scheduleable with "xots run"
	 * 
	 */
	boolean isPublic() default false;
}
