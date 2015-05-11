package org.openntf.domino.xsp.session;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.designer.runtime.domino.bootstrap.util.StringUtil;
import com.ibm.domino.osgi.core.context.ContextInfo;

/**
 * The XPageCurrentSessionFactory returns (as the name says) the current XPage Session if available.
 * 
 * 
 * If the ´Factory is passed across threads, it tries to create an XPage-Session with the same username that was available at construction
 * time. (This implies, that there is a valid XPage session available at construction time)
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class DasCurrentSessionFactory extends AbstractXPageSessionFactory {

	private static final long serialVersionUID = 1L;
	private String runAs_;

	public DasCurrentSessionFactory() {
		super();
		final lotus.domino.Session rawSession = ContextInfo.getUserSession();
		try {
			runAs_ = rawSession.getEffectiveUserName();
			lotus.domino.Database rawDb = rawSession.getCurrentDatabase();
			if (rawDb != null) {
				if (StringUtil.isEmpty(rawDb.getServer())) {
					currentApiPath_ = rawDb.getFilePath();
				} else {
					currentApiPath_ = rawDb.getServer() + "!!" + rawDb.getFilePath();
				}
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}

	}

	/**
	 * returns the current Das-Session
	 * 
	 * @throws
	 */
	@Override
	public Session createSession() {
		return wrapSession(ContextInfo.getUserSession(), false);
	}

}
