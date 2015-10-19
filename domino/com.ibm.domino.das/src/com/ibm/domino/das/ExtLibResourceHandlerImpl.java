 /*
 * © Copyright IBM Corp. 2011, 2012
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
package com.ibm.domino.das;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.ibm.domino.commons.RequestContext;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.core.ResourceHandlerImpl;

/**
 * @author Padraic
 *
 */
public class ExtLibResourceHandlerImpl extends ResourceHandlerImpl {
    private final Class<?> classInPackage;
    private final String specialAudiencePropsInPackage;
    private Map<Locale, ResourceBundle> _specialAudienceResourceBundle;
    public ExtLibResourceHandlerImpl(Class<?> classInPackage, 
            String userPropsInPackage, 
            String loggingPropsInPackage,
            String specialAudiencePropsInPackage) {
        super(classInPackage, userPropsInPackage, loggingPropsInPackage);
        this.classInPackage = classInPackage;
        this.specialAudiencePropsInPackage = specialAudiencePropsInPackage;
    }
    
    public String getSpecialAudienceString(String key) {
        Locale locale = getUserLocale();
        ResourceBundle bundle = null == _specialAudienceResourceBundle? null : _specialAudienceResourceBundle.get(locale);
        if( null == bundle ){
            if(null == _specialAudienceResourceBundle ){
                _specialAudienceResourceBundle = new HashMap<Locale, ResourceBundle>();
            }
            bundle = getResourceBundle(specialAudiencePropsInPackage, locale);
            _specialAudienceResourceBundle.put(locale, bundle);
        }
        return getResourceBundleString(bundle, key);
    }
    
    /**
     * @return
     */
    private Locale getUserLocale() {
        // TODO change superclass to so don't need to re-implement user locale detection here
        Locale locale = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if(context != null) {
            // First, use the Locale as provided by the ViewRoot
            UIViewRoot root = context.getViewRoot();
            if(root!=null) {
                locale = root.getLocale();
            }
            // Look if the Locale has been forced by the user
            if(locale==null) {
                if( context instanceof FacesContextEx ){
                    locale = ((FacesContextEx)context).getSessionData().getLocale();
                }
                // else may not be FacesContextEx if this is called 
                // before finish context initialization.
            } 
            if(locale==null) {
                // Else looks for the Locale expected by the browser, in order
                Iterator<?> it=context.getExternalContext().getRequestLocales();
                if(it.hasNext()) {
                    locale = (Locale)it.next();
                }
            }
        }
        
        if (locale == null ) {
        	// Try getting the locale from the request context (e.g. REST request)
        	RequestContext rctx = RequestContext.getCurrentInstance();
        	locale = rctx.getUserLocale();
        }
        
        if(locale==null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    private ResourceBundle getResourceBundle(String bundleSuffix, final Locale locale) {
        final String bundlePackage = buildResourcePath(bundleSuffix);
        ResourceBundle bundle = AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() {
            public ResourceBundle run() {
                // privileged code goes here:
                try {
                    return ResourceBundle.getBundle( bundlePackage, locale );
                }
                catch (MissingResourceException e) {
                    // does nothing - this method will return null and
                    // getString(String) will return the key
                    // it was called with
                    return null;
                }
            }
        });
        return bundle;
    }
    
    private String buildResourcePath(String name) {
        String clName = classInPackage.getName();
        return clName.substring( 0, clName.lastIndexOf('.') + 1 ) + name; 
    }

    private String getResourceBundleString(ResourceBundle bundle, String key){
        if (bundle != null) {
             try {
                 return bundle.getString(key);
             }
             catch (MissingResourceException e) {
                 // fall through
             }
        }
        return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
     } 
}