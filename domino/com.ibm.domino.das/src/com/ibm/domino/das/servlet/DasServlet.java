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

package com.ibm.domino.das.servlet;

import static com.ibm.domino.das.service.RestService.URL_PARAM_OWNER;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_STREAMING;
import static com.ibm.domino.services.rest.RestParameterConstants.PARAM_VALUE_TRUE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_CODE;
import static com.ibm.domino.services.rest.RestServiceConstants.ATTR_TEXT;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lotus.domino.Session;

import org.apache.wink.server.utils.RegistrationUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonGenerator.Generator;
import com.ibm.commons.util.io.json.JsonGenerator.StringBuilderGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.domino.commons.RequestContext;
import com.ibm.domino.commons.model.Customer;
import com.ibm.domino.commons.model.ICustomerProvider;
import com.ibm.domino.commons.model.IGatekeeperProvider;
import com.ibm.domino.commons.model.IStatisticsProvider;
import com.ibm.domino.commons.model.ProviderFactory;
import com.ibm.domino.das.service.CoreService;
import com.ibm.domino.das.service.DataService;
import com.ibm.domino.das.service.RestService;
import com.ibm.domino.das.service.IRestServiceExt;
import com.ibm.domino.das.servlet.DasStats.MutableDouble;
import com.ibm.domino.das.servlet.DasStats.MutableInteger;
import com.ibm.domino.das.utils.ErrorHelper;
import com.ibm.domino.das.utils.ScnContext;
import com.ibm.domino.das.utils.StatsContext;
import com.ibm.domino.osgi.core.context.ContextInfo;
import com.ibm.domino.services.AbstractRestServlet;
import com.ibm.domino.services.util.JsonWriter;
import com.ibm.xsp.acl.NoAccessSignal;


// Servlet created within the correct class loader
// This is a workaround to set the correct context class loader 
@SuppressWarnings("serial") // $NON-NLS-1$
public class DasServlet extends AbstractRestServlet {
    
    public static final LogMgr DAS_LOGGER = Log.load("com.ibm.domino.das");  //$NON-NLS-1$
    
    /**
     * The variable name [RestWebServices] that could be set in either Internet Site document for a domain,
     *  or a Server document for a specific server
     * This variable will return a list of services that are enabled or "" if no rest services are enabled.
     * 
     */
    private static final String CONFIG_RESTWEBSERVICES = "RestWebServices"; //$NON-NLS-1$
    
    private static final String DATA_SERVICE_NAME = "Data";//$NON-NLS-1$
    private static final String DATA_SERVICE_PATH = "data";//$NON-NLS-1$
    private static final String VERSION_ZERO = "0.0.0";  //$NON-NLS-1$
    private static final String DATA_SERVICE_VERSION = "9.0.1"; //$NON-NLS-1$
    
    private static final String CORE_SERVICE_NAME = "Core";//$NON-NLS-1$
    private static final String CORE_SERVICE_PATH = "core";//$NON-NLS-1$
    private static final String CORE_SERVICE_VERSION = "9.0.1"; //$NON-NLS-1$
    private static final int CORE_SERVICE_GKF = 427; // Defined by core SAAS code
    
    private static final long STATS_TIMER_INTERVAL = 30000;
    private static final String STATS_FACILITY = "API"; //$NON-NLS-1$
    private static final String STATS_REQUESTS = "Requests"; //$NON-NLS-1$
    private static final String STATS_TIME = "RequestTime"; //$NON-NLS-1$
    private static final String STATS_AVG_TIME = "AvgRequestTime"; //$NON-NLS-1$
    private static final String STATS_TOTAL_REQUESTS = "Total." + STATS_REQUESTS; //$NON-NLS-1$
    private static final String STATS_TOTAL_TIME = "Total." + STATS_TIME; //$NON-NLS-1$
    private static final String STATS_TOTAL_AVG_TIME = "Total." + STATS_AVG_TIME; //$NON-NLS-1$

    private static Boolean s_initialized = new Boolean(false);
    
    private static Timer s_statsTimer = null;
    private static TimerTask s_statsTimerTask = new TimerTask() {

        @Override
        public void run() {
            
            // Write all stats to Domino.  We do this on a timer
            // to avoid unneccessary churn.
            
            IStatisticsProvider provider = ProviderFactory.getStatisticsProvider();
            if ( provider != null ) {
                
                Set<Map.Entry<String, Object>> set = DasStats.get().getEntries();
                for ( Map.Entry<String, Object> entry : set ) {
                    Object value = entry.getValue();
                    if ( value instanceof MutableInteger ) {
                        int iValue = ((MutableInteger)value).getValue();
                        provider.UpdateInt(STATS_FACILITY, entry.getKey(), false, iValue);
                    }
                    else if ( value instanceof MutableDouble ) {
                        double dValue = ((MutableDouble)value).getValue();
                        provider.UpdateNumber(STATS_FACILITY, entry.getKey(), dValue);
                    }
                }
            }
        }
        
    };
    
    private static Map<String, DasService> s_services = new HashMap<String, DasService>();
    private static String s_enabledServices = null;

    private static Pattern s_acceptLanguagePattern = Pattern.compile("(\\w{1,8})(?:\\-(\\w{1,8}))?(?:\\;q=(\\d(?:\\.\\d)?))?"); // $NON-NLS-1$

    /**
     * Private class used to keep track of what services are enabled.
     */
    private class DasService {
        private String _name;
        private String _path;
        private boolean _enabled = false;
        private String _version;
        private int _gkf; // Gatekeeper feature #
        private Application _application;
        private boolean _initialized= false;
        
        public DasService(String name, String path, String version, int gkf, Application application) {
            _name = name;
            _path = path;
            _version = version;
            _gkf = gkf;
            _application = application;
        }

        public boolean isEnabled() {
            return _enabled;
        }

        public void setEnabled(boolean enabled) {
            _enabled = enabled;
        }

        public String getName() {
            return _name;
        }

        public String getPath() {
            return _path;
        }
        
        public String getVersion() {
            return _version;
        }

        public Application getApplication() {
            return _application;
        }
        
        public void setInitialized(boolean initialized) {
            _initialized = initialized;
        }
        
        public boolean isInitialized() {
            return _initialized;
        }
        
        public int getGkf() {
            return _gkf;
        }
    }
    
    public void doInit() throws ServletException {
        synchronized(s_initialized) {
            if ( !s_initialized ) {
                super.doInit();
                
                // Initialize the core service
                initCoreService();
                
                // Initialize the data service
                initDataService();
                
                // Initialize resources from other plugins
                initDynamicResources();
                
                // Schedule the timer task to run every 30 seconds
                IStatisticsProvider provider = ProviderFactory.getStatisticsProvider();
                if ( provider != null ) {
                    s_statsTimer = new Timer(true);
                    s_statsTimer.schedule(s_statsTimerTask, STATS_TIMER_INTERVAL, STATS_TIMER_INTERVAL);
                }
                
                s_initialized = true;
                DAS_LOGGER.getLogger().fine("DasServlet initialized."); // $NON-NLS-1$
            }
        }
    }
    
    public void doDestroy() {
        if ( s_initialized ) {
            Iterator<String> iterator = s_services.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                DasService service = s_services.get(key);
                Application application = service.getApplication();
                if ( application instanceof RestService ) {
                    try {
                        ((RestService)application).destroy();
                    }
                    catch(Throwable e) {
                        DAS_LOGGER.warn(e, e.getMessage());
                    }
                }
            }
            
            // Clean up Domino stats
            
            IStatisticsProvider provider = ProviderFactory.getStatisticsProvider();
            if ( provider != null ) {
                
                Set<Map.Entry<String, Object>> set = DasStats.get().getEntries();
                for ( Map.Entry<String, Object> entry : set ) {
                    provider.Delete(STATS_FACILITY, entry.getKey());
                }
            }
        }
        super.doDestroy();
    }
    
    /**
     * Override the service to do the access control check before processing the REST request
     */
    @Override
    public void doService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StatsContext.init();
        
        try {
            // CSRF protection
            if (!verifyDasTokens(request)){
                handleError(response, Response.Status.FORBIDDEN, null);
                return;
            }
            
            // Set the SCN context
            setScnContext(request);
            
            // Make sure the service is enabled for this server or internet site
            if ( ! serviceEnabled(request) ) {
                handleError(response, Response.Status.FORBIDDEN, null);
                return;             
            }
            
            // Set the request context
            setRequestContext(request);
            
            //Wrap the http request for X-HTTP-Method-Override header manipulation
            DasHttpRequestWrapper requestWrapper = new DasHttpRequestWrapper(request);
            
            //Wrap the http response for Gzip/Deflate the output stream
            DasHttpResponseWrapper responseWrapper = new DasHttpResponseWrapper(request, response);
            
            // Enable streaming.
            String param = requestWrapper.getParameter(PARAM_STREAMING);  
            if (param != null) {
                boolean preventCache = param.contentEquals(PARAM_VALUE_TRUE); 
                responseWrapper.setPreventCache(preventCache);
            }
            
            Application app = getService(request);
			try {
				if (app instanceof IRestServiceExt) {
					if (((IRestServiceExt) app).beforeDoService(request)) {
						super.doService(requestWrapper, responseWrapper);
						((IRestServiceExt) app).afterDoService(request);
					}
				} else {
					super.doService(requestWrapper, responseWrapper);
				}
			} catch (ServletException e) {
				if (app instanceof IRestServiceExt) {
					((IRestServiceExt) app).onError(request, e);
				}
				Throwable cause = e.getCause();
				if (cause instanceof NoAccessSignal) {
					throw (NoAccessSignal) cause;
				} else {
					handleUnknownException(responseWrapper, e);
				}
			} catch (Throwable e) {
				((IRestServiceExt) app).onError(request, e);
				// Avoid throwing unknown exceptions to the container
				handleUnknownException(responseWrapper, e);
			}
        }
        finally {
            DasStats stats = DasStats.get();
            Date now = new Date();
            long elapsed = now.getTime() - StatsContext.getCurrentInstance().getStartTime().getTime();
            int requests = stats.addInteger(STATS_TOTAL_REQUESTS, 1);
            double requestTime = stats.addNumber(STATS_TOTAL_TIME, elapsed);

            // The average time is an approximation because we are not synchronizing threads.
            // When multiple threads update the same stat at the same time, the calculation could
            // be off, but it's not worth keeping a thread waiting for better precision.
            
            if ( requests != 0 ) {
                stats.setInteger(STATS_TOTAL_AVG_TIME, (int)(requestTime/requests));
            }
            
            String serviceName = StatsContext.getCurrentInstance().getServiceName();
            if ( StringUtil.isNotEmpty(serviceName) ) {
                requests = stats.addInteger(serviceName + "." + STATS_TOTAL_REQUESTS, 1);
                requestTime = stats.addNumber(serviceName + "." + STATS_TOTAL_TIME, elapsed);
                if ( requests != 0 ) {
                    stats.setInteger(serviceName + "." + STATS_TOTAL_AVG_TIME, (int)(requestTime/requests));
                }
                
                String category = StatsContext.getCurrentInstance().getRequestCategory();
                if ( StringUtil.isNotEmpty(category) ) {
                    requests = stats.addInteger(serviceName + "." + category + "." + STATS_REQUESTS, 1);
                    requestTime = stats.addNumber(serviceName + "." + category + "." + STATS_TIME, elapsed);
                    if ( requests != 0 ) {
                        stats.setInteger(serviceName + "." + category + "." + STATS_AVG_TIME, (int)(requestTime/requests));
                    }
                }
            }
        }
    }

    /**
     * @param request
     * @return
     */
    private boolean verifyDasTokens(HttpServletRequest request) {
        String CSRFCookieName = "DasToken"; // $NON-NLS-1$
        String CSRFHeaderName = "X-DAS-Token"; // $NON-NLS-1$

        boolean verified = true;
            
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
                return verified;
        for (Cookie cookie : cookies)
        {
            String name = cookie.getName();
            if (name.equals(CSRFCookieName)){
                String csrfHeader = request.getHeader(CSRFHeaderName);
                if( csrfHeader == null || !cookie.getValue().equals(csrfHeader)){
                    verified = false;
                } 
                break;
            }      
        }
        
        return verified;
    }

    public static String getServicesResponse(String baseUrl) throws IOException, JsonException {
        refreshServiceMap();
        
        StringBuilder sb = new StringBuilder();
        Generator generator = new StringBuilderGenerator(JsonJavaFactory.instanceEx, sb, false);
        generator.out("{");
        generator.nl();
        generator.incIndent();

        generator.indent();
        generator.outPropertyName("services"); // $NON-NLS-1$
        generator.out(":[");
        generator.nl();
        generator.incIndent();
        
        Iterator<String> iterator = s_services.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            DasService service = s_services.get(key);
            
            generator.indent();
            generator.out("{");
            generator.nl();
            generator.incIndent();
            
            generator.indent();
            generator.outPropertyName("name"); // $NON-NLS-1$
            generator.out(":");
            generator.outLiteral(service.getName());
            generator.out(",");
            generator.nl();
            
            generator.indent();
            generator.outPropertyName("enabled"); // $NON-NLS-1$
            generator.out(":");
            generator.outBooleanLiteral(service.isEnabled());
            generator.out(",");
            generator.nl();
            
            generator.indent();
            generator.outPropertyName("version"); // $NON-NLS-1$
            generator.out(":");
            generator.outLiteral(service.getVersion());
            generator.out(",");
            generator.nl();
            
            generator.indent();
            generator.outPropertyName("href"); // $NON-NLS-1$
            generator.out(":");
            generator.outLiteral(baseUrl + service.getPath());
            generator.nl();
            generator.decIndent();

            generator.indent();
            generator.out("}");
            if ( iterator.hasNext() ) {
                generator.out(",");
            }
            generator.nl();
        }
        
        generator.decIndent();
        generator.indent();
        generator.out("]");
        
        generator.decIndent();
        generator.nl();
        generator.indent();
        generator.out("}");

        return sb.toString();
    }
    
    private void initCoreService() {
        
        try {
            Application application = new CoreService();
            
            // Add the service to our map
            DasService service = new DasService(CORE_SERVICE_NAME, CORE_SERVICE_PATH, 
                                        CORE_SERVICE_VERSION, CORE_SERVICE_GKF, application);
            s_services.put(CORE_SERVICE_PATH, service);

            DAS_LOGGER.getLogger().fine("Registered the core DAS service"); // $NON-NLS-1$
        }
        catch (Throwable e) {
            DAS_LOGGER.warn(e, e.getMessage());
        }
    }
    
    private void initDataService() {
        
        try {
            Application application = new DataService();
            
            // Add the service to our map
            DasService service = new DasService(DATA_SERVICE_NAME, DATA_SERVICE_PATH, 
                                        DATA_SERVICE_VERSION, 0, application);
            s_services.put(DATA_SERVICE_PATH, service);

            DAS_LOGGER.getLogger().fine("Registered the data service"); // $NON-NLS-1$
        }
        catch (Throwable e) {
            DAS_LOGGER.warn(e, e.getMessage());
        }
    }
    
    /**
     * Initialize dynamic resources from other plugins.
     */
    private void initDynamicResources() {
        
        // Get a list of all registered extensions
        
        IExtension extensions[] = null;
        final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        if (extensionRegistry != null) {
            final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("com.ibm.domino.das.service"); // $NON-NLS-1$
            if (extensionPoint != null) {
                extensions = extensionPoint.getExtensions();
            }
        }
        
        if (extensions == null) {
            return;
        }

        // Walk through each extension in the list
        
        for (final IExtension extension : extensions) {
            final IConfigurationElement configElements[] = extension.getConfigurationElements();
            if (configElements == null) {
                continue;
            }
            
            for (final IConfigurationElement configElement : configElements) {
                try {
                    // We only handle serviceResources elements for now
                    DAS_LOGGER.getLogger().fine("Config element: " + configElement.getName()); // $NON-NLS-1$
                    if ( !("serviceResources".equalsIgnoreCase(configElement.getName())) ) { // $NON-NLS-1$
                        continue;
                    }
                    
                    String serviceName = configElement.getAttribute("name"); // $NON-NLS-1$
                    String servicePath = configElement.getAttribute("path"); // $NON-NLS-1$
                    String serviceVersion = configElement.getAttribute("version"); // $NON-NLS-1$
                    String serviceGkf = configElement.getAttribute("gkf"); // $NON-NLS-1$
                    if ( serviceName == null || servicePath == null ) {
                        DAS_LOGGER.getLogger().warning(StringUtil.format("DAS service {0} ignored. Both name and path must be defined.", serviceName)); // $NLW-DasServlet.DASservice0ignoredBothnameandpath-1$
                        continue;
                    }
                    
                    // Parse the gatekeeper feature #
                    int iGkf = 0;
                    if ( StringUtil.isNotEmpty(serviceGkf)) {
                        try {
                            iGkf = Integer.parseInt(serviceGkf);
                        }
                        catch (Throwable e) {
                            // Ignore parser exception
                        }
                    }
                    
                    DAS_LOGGER.getLogger().fine(StringUtil.format("Found a DAS service extension: {0} (/{1})", serviceName, servicePath)); // $NON-NLS-1$

                    final Object object = configElement.createExecutableExtension("class"); // $NON-NLS-1$
                    if ( ! (object instanceof Application) ) {
                        // Class was constructed but it is the wrong type
                        DAS_LOGGER.getLogger().warning(StringUtil.format("DAS service {0} ignored. Class is the wrong type.", serviceName)); // $NLW-DasServlet.DASservice0ignoredClassisthewrong-1$
                        continue;
                    }

                    // This is a critical section.  Things can go wrong inside registerApplication
                    // (e.g. NoClassDefFound).  So catch all exceptions and log them, but continue 
                    // to the next service.
                    
                    try {
                        Application application = (Application) object;
                        
                        // Add the service to our map
                        DasService service = new DasService(serviceName, servicePath, 
                                                    (serviceVersion != null) ? serviceVersion : VERSION_ZERO,
                                                     iGkf, application);
                        s_services.put(servicePath.toLowerCase(), service);
    
                        DAS_LOGGER.getLogger().fine("Registered a DAS service extension"); // $NON-NLS-1$
                    }
                    catch (Throwable e) {
                        DAS_LOGGER.warn(e, e.getMessage());
                    }
                } catch (final CoreException e) {
                    DAS_LOGGER.error(e, e.getMessage());
                }
            }
        }
    }
    
    /**
     * Tests whether the service corresponding to this request is enabled.
     * 
     * @param request
     * @return
     */
    private boolean serviceEnabled(HttpServletRequest request) {
        if ( !isDominoServer() ) {
            // We only handle requests on the domino server
            return false;
        }

        boolean enabled = true;
        String requestPath = null;
        
        if ( request.getPathInfo() != null ) {
            StringTokenizer tokenizer = new StringTokenizer(request.getPathInfo(), "/");
            try {
                requestPath = tokenizer.nextToken();
                if ( requestPath != null ) {
                    requestPath.toLowerCase();
                }
            }
            catch (NoSuchElementException e) {
                // Ignore this
            }
        }
        
        if ( requestPath != null ) {
            refreshServiceMap();
            
            DasService service = s_services.get(requestPath);
            if ( service != null ) {
                StatsContext.getCurrentInstance().setServiceName(service.getName());

                // Make sure service is enabled on this server
                
                enabled = service.isEnabled();
                if ( !enabled ) {
                    if ( DAS_LOGGER.getLogger().isLoggable(Level.FINE)) {
                        DAS_LOGGER.getLogger().fine(StringUtil.format(
                                "The {0} service is disabled on this server.", // $NON-NLS-1$ 
                                service.getName()));                        
                    }
                }

                // Do some SAAS-only checks
                
                if ( enabled && ScnContext.getCurrentInstance().isScn() ) {
                    String customerId = ScnContext.getCurrentInstance().getCustomerId();

                    // Service is enabled on this server.  Now check the 
                    // gatekeeper feature.  When running in SAAS, this makes sure
                    // the service is enabled for the customer.
                    if ( service.getGkf() != 0 ) {
                        String userId = ScnContext.getCurrentInstance().getUserId();
                        IGatekeeperProvider provider = ProviderFactory.getGatekeeperProvider();
                        enabled = provider.isFeatureEnabled(service.getGkf(), 
                                    customerId, userId);

                        if ( !enabled && DAS_LOGGER.getLogger().isLoggable(Level.FINE)) {
                            DAS_LOGGER.getLogger().fine(StringUtil.format(
                                    "The {0} service is disabled by the gatekeeper for customer {1}.", // $NON-NLS-1$ 
                                    service.getName(), 
                                    customerId));                        
                        }
                    }

                    // Even if the service is enabled thru gatekeeper, prevent
                    // self service trial customers from making API requests.
                    if ( enabled && StringUtil.isNotEmpty(customerId) ) {
                        try {
                            ICustomerProvider provider = ProviderFactory.getCustomerProvider();
                            if ( provider != null ) {
                                Customer customer = provider.getCustomer(customerId);
                                if ( customer.isSelfTrial() ) {
                                    enabled = false;
                                }
                            }
                        }
                        catch (Throwable e) {
                            // Do nothing.  Assume the request is NOT for a self service trial customer.
                        }
                    }
                }

                // Just in time initialization

                if (enabled && !service.isInitialized()) {
                    synchronized(service) {
                        try {
                            RegistrationUtils.registerApplication(service.getApplication(), getServletContext());
                            service.setInitialized(true);
                        }
                        catch (Throwable e) {
                            enabled = false;
                            service.setEnabled(false);
                            DAS_LOGGER.warn(e, StringUtil.format(
                                    "Automatically disabling {0} service because there was an error registering its resources.", // $NLW-DasServlet.Automaticallydisablingaservicebec-1$ 
                                    service.getName()));
                        }
                    }
                }
            }
        }
        
        return enabled;
    }
	
	protected Application getService(HttpServletRequest request) {
		String requestPath = null;
		Application app = null;
		if (request.getPathInfo() != null) {
			StringTokenizer tokenizer = new StringTokenizer(request.getPathInfo(), "/");
			try {
				requestPath = tokenizer.nextToken();
				if (requestPath != null) {
					requestPath.toLowerCase();
				}
			} catch (NoSuchElementException e) {
				// Ignore this
			}
		}

		if (requestPath != null) {
			refreshServiceMap();
			DasService service = s_services.get(requestPath);
			if (service != null) {
				app = service.getApplication();
			}
		}
		return app;
	}
    
    /**
     * Refreshes the service map.
     * 
     * <p>This method enables all the services listed in the Internet Site document
     * or server document.
     */
    private static void refreshServiceMap() {
        
        String enabledServices = ContextInfo.getServerVariable(CONFIG_RESTWEBSERVICES);
        if ( enabledServices.equals(s_enabledServices) ) {
            // No change in the list of services.  We're done.
            return;
        }
 
        synchronized(s_services) {
            
            // Disable all services except the core service
            
            Iterator<String> iterator = s_services.keySet().iterator();
            while (iterator.hasNext()) {
                DasService service = s_services.get(iterator.next());
                if ( CORE_SERVICE_NAME.equals(service.getName()) ) {
                    service.setEnabled(true);
                }
                else {
                    service.setEnabled(false);
                }
            }
            
            // Enable just the services in the list 
            
            StringTokenizer tokenizer = new StringTokenizer(enabledServices, ", ");
            while ( tokenizer.hasMoreTokens() ) {
                String serviceName = tokenizer.nextToken();
                
                // Enable the service
                iterator = s_services.keySet().iterator();
                while (iterator.hasNext()) {
                    DasService service = s_services.get(iterator.next());
                    if ( serviceName.equalsIgnoreCase(service.getName())) {
                        service.setEnabled(true);
                        break;
                    }
                }
            }
            
            // Remember the last value
            
            s_enabledServices = enabledServices;
        }
    }
    
    private boolean isDominoServer() {
        boolean isServer = false;
        Session session = null;
        
        try {
            session = ContextInfo.getUserSession();
            isServer = session.isOnServer();
        }
        catch (Throwable e) {
            // Ignore the exception.  Just assume we are not on the server.
        }
        
        return isServer;
    }
    
    private void handleUnknownException(HttpServletResponse response, Throwable t) throws ServletException, IOException {
        handleError(response, Response.Status.INTERNAL_SERVER_ERROR, t);
    }
    
    /**
     * Writes the error to the HTTP response.
     * 
     * @param response
     * @param status
     * @param t
     * @throws ServletException
     * @throws IOException
     */
    private void handleError(HttpServletResponse response, Response.Status status, Throwable t) throws ServletException, IOException {
        
        String message = status.getReasonPhrase();
        
        try {
            response.sendError(status.getStatusCode(), message);
            response.setContentType(MediaType.APPLICATION_JSON);
            StringWriter writer = new StringWriter();
            Boolean compact = false;
            JsonWriter jWriter = new JsonWriter(writer, compact);
            
            try {
                jWriter.startObject();
                ErrorHelper.writeProperty(jWriter, ATTR_CODE, status.getStatusCode());
                ErrorHelper.writeProperty(jWriter, ATTR_TEXT, message);
                if ( t != null ) {
                    ErrorHelper.writeException(jWriter, t);
                }
            } 
            finally {
                jWriter.endObject();
            }

            StringBuffer buffer = writer.getBuffer();
            ServletOutputStream os = response.getOutputStream();
            os.print(buffer.toString());
            os.flush();
        }
        catch (IOException e) {
            DAS_LOGGER.warn(e, "Error creating response.");  //$NLW-DasServlet.Errorcreatingresponse-1$
        }
    }
    
    /**
     * Sets the request context for lower level utilities -- like 
     * string resouce handlers.
     * 
     * @param request
     */
    private void setRequestContext(HttpServletRequest request) {
        Locale userLocale = null;
        List<Locale> locales = parseLocales(request);
        if ( locales != null && locales.size() > 0 ) {
            userLocale = locales.get(0);
        }
        
        RequestContext ctx = RequestContext.getCurrentInstance();
        ctx.setUserLocale(userLocale);
        ctx.setCustomerId(ScnContext.getCurrentInstance().getCustomerId());
    }
    
    /**
     * Sets the SCN context.
     * 
     * @param request
     */
    private void setScnContext(HttpServletRequest request) {
        ScnContext ctx = ScnContext.init();

        String dominoDn = request.getHeader("X-DominoDN"); // $NON-NLS-1$
        String dominoSamlTo = request.getHeader("X-DominoSAMLTo"); // $NON-NLS-1$
        if ( StringUtil.isNotEmpty(dominoDn) && StringUtil.isNotEmpty(dominoSamlTo) ) {
            ctx.setScn(true);
        }
        
        String ownerId = request.getParameter(URL_PARAM_OWNER);
        ctx.setOwnerId(ownerId);

        String customerId = request.getHeader("X-DominoCustomerID"); // $NON-NLS-1$
        if ( StringUtil.isNotEmpty(customerId) ) {
            ctx.setCustomerId(customerId);
        }
        
        String userId = request.getHeader("X-DominoUserID"); // $NON-NLS-1$
        if ( StringUtil.isNotEmpty(userId) ) {
            ctx.setUserId(userId);
        }

    }
    
    /**
     * Parses all Accept-Language headers.
     * 
     * @param request
     * @return
     */
    private List<Locale> parseLocales(HttpServletRequest request)
    {
        List<Locale> locales = null;
        
        try {
        
            Enumeration values = request.getHeaders("accept-language"); // $NON-NLS-1$
            while(values.hasMoreElements())
            {
                if ( locales == null ) {
                    locales = new ArrayList<Locale>();
                }
                String value = values.nextElement().toString();
                parseLocales(locales, value);
            }
        }
        catch (Throwable t) {
            // Shouldn't happen, but just in case, ignore parser errors
            // and other unchecked exceptions
        }
        
        return locales;
    }

    /**
     * Parse a single Accept-Language header.
     * 
     * @param locales
     * @param headerValue
     */
    private void parseLocales(List<Locale> locales, String headerValue)
    {
        // Portions of this method were stolen from XspCmdHttpServletRequest.  The
        // following comment is from there:
        //
        // Http Header parsing
        // Accept-Language = "Accept-Language" ":"
        // 1#( language-range [ ";" "q" "=" qvalue ] )
        // qvalue = ( "0" [ "." 0*3DIGIT ] )
        // | ( "1" [ "." 0*3("0") ] )
        // language-range = ( ( 1*8ALPHA *( "-" 1*8ALPHA ) ) | "*" )
        // Each language-range MAY be given an associated quality value which represents an estimate of the user's
        // preference for the languages specified by that range. The quality value defaults to "q=1". For example,
        //
        // Accept-Language: da, en-gb;q=0.8, en;q=0.7

        String[] s = StringUtil.splitString(headerValue, ',', true);

        for(int i = 0; i < s.length; i++)
        {
            Matcher matcher = s_acceptLanguagePattern.matcher(s[i]);
            if(matcher.find())
            {
                String l1 = matcher.group(1);
                if(l1 == null)
                {
                    l1 = "";
                }
                String l2 = matcher.group(2);
                if(l2 == null)
                {
                    l2 = "";
                }
                Locale l = new Locale(l1, l2);

                // TODO: Handle quality. Although we are parsing it, we
                // are throwing it away for now.
                
                double q = 1.0;
                String qs = matcher.group(3);
                if(qs != null)
                {
                    q = Double.parseDouble(qs);
                }

                locales.add(l);
            }
        }
    }
}