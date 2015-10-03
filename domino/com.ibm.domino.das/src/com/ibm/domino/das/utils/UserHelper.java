 /*
 * © Copyright IBM Corp. 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.domino.das.utils;

import static com.ibm.domino.das.servlet.DasServlet.DAS_LOGGER;

import java.util.Vector;
import java.util.logging.Level;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.Directory;
import lotus.domino.DirectoryNavigator;
import lotus.domino.Name;
import lotus.domino.NotesError;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.domino.commons.util.BackendUtil;
import com.ibm.domino.das.service.RestService;
import com.ibm.domino.osgi.core.context.ContextInfo;
import com.ibm.xsp.acl.NoAccessSignal;

public class UserHelper {
    
    private static final String DOT_NSF = ".nsf"; //$NON-NLS-1$
	
	private Name userName;
	private String EmailAddress;
	private String mailServer;
	private Name mailServerName;
    private String mailFile;
	private String url;

	private static Vector<String> s_lookupItems = lookupItems();
	private static Vector<String> s_serverLookupItems = serverLookupItems();

	public UserHelper() {
	}
	
	private void setup(String name, String localServerBaseUrl) {
        String effectiveUser = null;
        Directory lookupDir = null;

        Session session = ContextInfo.getUserSession();
    	if ( session != null ) {
			try {
			    effectiveUser = session.getEffectiveUserName();
			}
			catch (NotesException e) {
                throw new NoAccessSignal("Need user context"); // $NLX-UserHelper_NeedUserContext_Problem-1$
			}
			
			if ( effectiveUser == null || effectiveUser.equals("Anonymous")) { //$NON-NLS-1$
	            throw new NoAccessSignal("Need user context"); // $NLX-UserHelper_NeedUserContext_Null-1$
			}
    	}
    	
    	if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
    	    DAS_LOGGER.getLogger().finest("Effective user is " + effectiveUser); //$NON-NLS-1$
    	}
    	
    	// Lookup the authenticated user's mail file
    	
		try {
			lookupDir = session.getDirectory();
			if ( lookupDir == null ) {
				throw new WebApplicationException(RestService.createErrorResponse("Can't lookup the name.", Response.Status.INTERNAL_SERVER_ERROR)); // $NLX-UserHelper_CannotLookupTheName-1$
			}
			
			Vector<String> vName = new Vector<String>();
			if ( StringUtil.isEmpty(name) ) {
    			vName.addElement(effectiveUser);
    	        if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
    	            DAS_LOGGER.getLogger().finest("Looking up " + effectiveUser); //$NON-NLS-1$
    	        }
			}
			else {
	            vName.addElement(name);
                if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
                    DAS_LOGGER.getLogger().finest("Looking up " + name); //$NON-NLS-1$
                }
			}

			DirectoryNavigator dirNav = lookupDir.lookupNames("($Users)", vName, s_lookupItems, true);	//$NON-NLS-1$
			if( dirNav == null || dirNav.getCurrentMatches() == 0 ){
			    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
			    if ( name != null ) {
			        status = Response.Status.BAD_REQUEST;
			    }
			    
				throw new WebApplicationException(RestService.createErrorResponse("Name not found.", // $NLX-UserHelper_NameNotFound-1$ 
						status));
			}

			// Digest the results of the lookup
			
			Vector<String> value = null;
			value = dirNav.getFirstItemValue();
			String fullName = value.elementAt(0);
			userName = session.createName(fullName);
			
			value = dirNav.getNextItemValue();
			String shortName = value.elementAt(0);
			
			value = dirNav.getNextItemValue();
			mailFile = value.elementAt(0);
			
			value = dirNav.getNextItemValue();
			mailServer = value.elementAt(0);
			
            try {
                if ( mailServer != null ) {
                    mailServerName = session.createName(mailServer);
                }
            } catch (NotesException e) {
                // Ignore
            }

			value = dirNav.getNextItemValue();
			String mailSystem = value.elementAt(0);
			
			value = dirNav.getNextItemValue();
			EmailAddress = value.elementAt(0);
			
			// Lookup the user's mail server.
			//
			// Re: SPR# NRBY99VSC3 -- We used to throw an exception when we couldn't find
			// the server in the ($servers) view.  Now we don't throw an exception until
			// we really need the server info (i.e. when there is no local replica of
			// the user's mail file).
			
			vName = new Vector<String>();
			vName.addElement(mailServer);

            if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
                DAS_LOGGER.getLogger().finest("Looking up home server " + mailServer); //$NON-NLS-1$
            }

            String serverName = null;
			String internetAddress = null;
			dirNav = lookupDir.lookupNames("($Servers)", vName, s_serverLookupItems, true);	//$NON-NLS-1$
			if( dirNav != null && dirNav.getCurrentMatches() > 0 ){

			    // Digest the results of the server lookup

	            value = dirNav.getFirstItemValue();
	            serverName = value.elementAt(0);
	            
	            Vector<String> ports = dirNav.getNextItemValue();
	            value = dirNav.getNextItemValue();
	            for ( int i = 0; i < ports.size(); i++) {
	                if ( "TCPIP".equals(ports.elementAt(i)) ) { //$NON-NLS-1$
	                    internetAddress = value.elementAt(i);
	                    break;
	                }
	            }
			}
			
			if ( mailFile != null ) {
			    
			    // Strip trailing .nsf off (if it's there).  We'll add it back later.
			    
			    mailFile = mailFile.toLowerCase();
			    int index = mailFile.indexOf(DOT_NSF);
			    if ( index != -1 ) {
			        mailFile = mailFile.substring(0, index);
			    }
			}
			
			if ( localServerBaseUrl != null && mailFile != null) {

			    // Check for a local copy of the mail file
			    
			    if ( localFileExists(session, mailFile + DOT_NSF) ) {
	                url = localServerBaseUrl + "/" + mailFile.replace('\\', '/') + DOT_NSF; //$NON-NLS-1$
	                if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
	                    DAS_LOGGER.getLogger().finest("Mail file " + mailFile + " exists locally"); //$NON-NLS-1$ //$NON-NLS-2$
	                }
			    }
			    else {
                    if ( DAS_LOGGER.getLogger().isLoggable(Level.FINEST) ) {
                        DAS_LOGGER.getLogger().finest("Mail file " + mailFile + " does NOT exist locally"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
			    }
			}

			if ( url == null ) {
	            if ( serverName == null ) {
	                throw new WebApplicationException(RestService.createErrorResponse("Server not found.", // $NLX-UserHelper_ServerNotFound-1$ 
	                        Response.Status.INTERNAL_SERVER_ERROR));
	            }
	            
    			// Validate the component parts of the URL
    			
    			if ( internetAddress == null || mailFile == null ) {
    			    throw new WebApplicationException(RestService.createErrorResponse("Cannot build service resource address.", Response.Status.INTERNAL_SERVER_ERROR));// $NLX-UserHelper_CannotBuildResourceAddress-1$
    			}
    			
    			// Assemble the URL
    			
    			url = "http://" + internetAddress.toLowerCase() + "/" + mailFile.replace('\\', '/') + DOT_NSF; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} 
		catch (NotesException e) {
            throw new WebApplicationException(RestService.createErrorResponse(e));
		}
		finally {
		    BackendUtil.safeRecycle(lookupDir);
		}
    	
	}

    
    private static Vector<String> lookupItems() {
		Vector<String> lookupItems = new Vector<String>();
		
		lookupItems.addElement("FullName");			//$NON-NLS-1$
		lookupItems.addElement("ShortName");		//$NON-NLS-1$
		lookupItems.addElement("MailFile");			//$NON-NLS-1$
		lookupItems.addElement("MailServer");		//$NON-NLS-1$
		lookupItems.addElement("MailSystem");		//$NON-NLS-1$
		lookupItems.addElement("InternetAddress");	//$NON-NLS-1$
		
		return lookupItems;
    }
    
    private static Vector<String> serverLookupItems() {
    	
		Vector<String> lookupItems = new Vector<String>();
		
		lookupItems.addElement("ServerName");	//$NON-NLS-1$
		lookupItems.addElement("Ports");		//$NON-NLS-1$
		lookupItems.addElement("NetAddresses");	//$NON-NLS-1$
		
		return lookupItems;
    }
    
    private boolean localFileExists(Session session, String filepath) throws NotesException {
        boolean exists = false;
        DbDirectory dbdir = null;
        Database localDb = null;
        
        try {
            try {
                // Optimistically attempt to open the database
                localDb = session.getDatabase(null, filepath, false);
                if ( localDb != null ) {
                    exists = true;
                }
            }
            catch (NotesException e) {
                if ( e.id == NotesError.NOTES_ERR_DBNOACCESS ) {
                    // An access error proves the database exists
                    exists = true;
                }
                else {
                    // Ignore other errors and fall into the exhaustive search below.
                }
            }
            
            if ( !exists) {
                
                // Exhaustive search of database directory
                
                dbdir = session.getDbDirectory(null);
                Database database = dbdir.getFirstDatabase(DbDirectory.DATABASE);
                while ( database != null ) {
                    
                    String path = database.getFilePath();
                    if ( filepath.equals(path) ) {
                        // TODO: Make sure this is database is really a replica of the user's mail file.
                        // Usually replicas have the same name, but it's not guaranteed.
            
                        exists = true;
                        break;
                    }
                    
                    Database next = dbdir.getNextDatabase();
                    BackendUtil.safeRecycle(database);
                    database = next;
                }
            }
        }
        finally {
            BackendUtil.safeRecycle(localDb);
            BackendUtil.safeRecycle(dbdir);
        }
        
        return exists;
    }
    
    /**
     * Gets a helper object for the currently authenticated user.
     * 
     * <p>The UserHelper instance returned by this method always includes
     * a URL referring to the user's home mail server.
     * 
     * @return
     */
    public static UserHelper getUser() {
    	UserHelper user = new UserHelper();
    	
    	user.setup(null, null);
    	
    	return user;
    }

    /**
     * Gets a helper object for the currently authenticated user.
     * 
     * <p>The UserHelper instance returned by this method may include
     * a URL referring to the local server.  If the local server doesn't 
     * include a replica of the user's mail file, the URL refers to
     * the user's home mail server.
     *
     * @param localServerBaseUrl
     * @return
     */
    public static UserHelper getUser(String localServerBaseUrl) {
        UserHelper user = new UserHelper();
        
        user.setup(null, localServerBaseUrl);
        
        return user;
    }
    
    /**
     * Gets a helper object for the named user.
     * 
     * @param name
     * @param localServerBaseUrl
     * @return
     */
    public static UserHelper getNamedUser(String name, String localServerBaseUrl) {
        UserHelper user = new UserHelper();
        
        user.setup(name, localServerBaseUrl);
        
        return user;
    }

	public String getEmailAddress() {
		return EmailAddress;
	}

	public Name getUserName() {
		return userName;
	}

	public String getMailServer() {
		return mailServer;
	}

    /**
     * @return the mailServerName
     */
    public Name getMailServerName() {
        return mailServerName;
    }

	public String getMailFile() {
		return mailFile;
	}

	public String getUrl() {
		return url;
	}
}
