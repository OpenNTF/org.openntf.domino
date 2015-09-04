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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

import org.apache.wink.common.internal.http.AcceptEncoding;

import com.ibm.commons.util.io.ByteStreamCache;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.domino.osgi.core.context.ContextInfo;

/**
 * This class is basded on ContentEncodingResponseFilter from org.apache.wink and modified for DAS Wink.
 * 
 * Original Class: org.apache.wink.server.internal.servlet.contentencode.ContentEncodingResponseFilter
 * It wraps the original ServletResponse with gzip/deflate encoding for the OutputStream support
 * Note: Call the finishResponse() to close the EncodedOutputStream which is required by DeflaterOutputStream and 
 * 
 * sauriemma: Added support for Content-Length using the ByteStreamCache. 
 *               This feature is required for persistent connections.
 * 
 * @author Mao Chuan
 *
 */
public class DasHttpResponseWrapper extends HttpServletResponseWrapper {

    public final static String ENCODER_GZIP = "gzip"; // $NON-NLS-1$
    public final static String ENCODER_DEFLATE = "deflate"; // $NON-NLS-1$
    public final static String ENV_VAR_DAS_PREVENT_GZIP = "DASPreventGzip"; // $NON-NLS-1$
    public final static String ENV_VAR_DAS_PREVENT_CACHE = "DASPreventCache"; // $NON-NLS-1$

    private final static HeaderDelegate<AcceptEncoding> acceptEncodingHeaderDelegate = 
        RuntimeDelegate.getInstance().createHeaderDelegate(AcceptEncoding.class);
    
    private final HttpServletResponse httpServletResponse;
    private final HttpServletRequest httpServletRequest;
    private EncodedOutputStream encodedOutputStream;
    private PrintWriter servletWriter;
    private String encoder = "";
    
    private boolean preventCache;
    private boolean preventGzip;
    
    public DasHttpResponseWrapper(HttpServletRequest request, HttpServletResponse response) {

        super(response);
        this.httpServletResponse = response;
        this.httpServletRequest = request;
    }
    
    public boolean isPreventGzip() {
        return preventGzip;
    }
    public void setPreventGzip(boolean preventGzip) {
        this.preventGzip = preventGzip;
    }

    public boolean isPreventCache() {
        return preventCache;
    }
    public void setPreventCache(boolean preventCache) {
        this.preventCache = preventCache;
    }

    private AcceptEncoding getAcceptEncodingHeader() {

        Enumeration<String> acceptEncodingEnum = httpServletRequest.getHeaders(HttpHeaders.ACCEPT_ENCODING);
        StringBuilder sb = new StringBuilder();
        
        if (acceptEncodingEnum.hasMoreElements()) {
            sb.append(acceptEncodingEnum.nextElement());
            while (acceptEncodingEnum.hasMoreElements()) {
                sb.append(","); //$NON-NLS-1$
                sb.append(acceptEncodingEnum.nextElement());
            }
            String acceptEncodingHeader = sb.toString();

            return acceptEncodingHeaderDelegate.fromString(acceptEncodingHeader);
        }

        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        
        if(servletWriter != null){
            throw new IllegalStateException("Servlet writer is open."); //$NON-NLS-1$
        }
        return this.getEncodedOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        
        if(servletWriter == null){
            ServletOutputStream outputStream = getOutputStream();
            OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
            servletWriter = new PrintWriter(outWriter);
        }
        
        return servletWriter;
    }
    
//  private ServletOutputStream getServletOutputStream() throws IOException{
//      
//      return super.getOutputStream();
//  }
    
    private EncodedOutputStream getEncodedOutputStream() throws IOException{
        
        //preventCache = new Boolean(ContextInfo.getEnvironmentString(ENV_VAR_DAS_PREVENT_CACHE));
        preventGzip = new Boolean(ContextInfo.getEnvironmentString(ENV_VAR_DAS_PREVENT_GZIP));
        
        if (this.encodedOutputStream == null) {     
            if(!preventGzip) {
                AcceptEncoding acceptEncoding = this.getAcceptEncodingHeader();
    
                if (acceptEncoding != null) {
                    List<String> acceptableEncodings = acceptEncoding.getAcceptableEncodings();
                    for (String encoding : acceptableEncodings) {
                        if (ENCODER_GZIP.equalsIgnoreCase(encoding)) {
                            encoder = ENCODER_GZIP;
                            break;
                        } else if (ENCODER_DEFLATE.equalsIgnoreCase(encoding)) {
                            encoder = ENCODER_DEFLATE;
                            break;
                        }
                    }
                    if (acceptEncoding.isAnyEncodingAllowed()
                            && !acceptEncoding.getBannedEncodings().contains(ENCODER_GZIP)) {
                        encoder = ENCODER_GZIP;
                    }
                    if (encoder != null) {
                        this.addHeader(HttpHeaders.CONTENT_ENCODING, encoder);
                        this.addHeader(HttpHeaders.VARY, HttpHeaders.ACCEPT_ENCODING);
                    }               
                }
            }
            this.encodedOutputStream = new EncodedOutputStream(httpServletResponse, encoder, preventCache);
        }       
        return this.encodedOutputStream;
    }

//  public void finishResponse() throws IOException {
//      
//      if(this.encodedOutputStream != null){
//          this.encodedOutputStream.close();
//      }
//              
//      if(this.servletWriter != null){
//          this.servletWriter.close();
//      }
//  }

    /**
     *  Inner class EncodedOutputStream used to support encodings like gzip and deflate.
     *  Cache is require to emit the Conent-Length and used to support persistent connections.
     **/
    private static class EncodedOutputStream extends ServletOutputStream {
        
        final private HttpServletResponse httpServletResponse;
        private ByteStreamCache byteStreamCache = null;
        private OutputStream outputStream = null;
        private boolean streamOpened = false;
        private boolean preventCache = false; 
        private String encoder = null;

        /**
         * @param httpServletResponse
         * @param encoder
         * @param preventCache
         */
        public EncodedOutputStream(HttpServletResponse httpServletResponse, String encoder, boolean preventCache) {

            this.httpServletResponse = httpServletResponse;
            this.preventCache = preventCache;
            this.encoder = encoder;
        }

        @Override
        public void write(int b) throws IOException {

            if (!streamOpened) {
                this.createEncoderOutputStream();
                streamOpened = true;
            }
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {

            if (!streamOpened) {
                this.createEncoderOutputStream();
                streamOpened = true;
            }
            outputStream.write(b, off, len);
        }

        @Override
        public void write(byte[] b) throws IOException {

            if (!streamOpened) {
                this.createEncoderOutputStream();
                streamOpened = true;
            }
            outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {

            if (!streamOpened) {
                this.createEncoderOutputStream();
                streamOpened = true;
            }
            this.finish();
            if (!preventCache) {
                httpServletResponse.setContentLength((int)byteStreamCache.getLength());
                // And copy the entire content
                InputStream is = byteStreamCache.getInputStream();
                OutputStream out = httpServletResponse.getOutputStream();
                StreamUtil.copyStream(is, out);
                out.flush();
                byteStreamCache.clear();
            }
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
        
            flush();
            outputStream.close();
        }

        public void finish() throws IOException {
            
            if (outputStream instanceof DeflaterOutputStream) {
                ((DeflaterOutputStream) outputStream).finish();
            }
        }

        private void createEncoderOutputStream() throws IOException {

            OutputStream os = null;
            if(preventCache) {
                os = httpServletResponse.getOutputStream();
            } else {
                byteStreamCache = new ByteStreamCache();
                os = byteStreamCache.getOutputStream();
            }
            
            if (this.encoder.equalsIgnoreCase(ENCODER_GZIP)) {
                outputStream = new GZIPOutputStream(os);
            } else if (this.encoder.equalsIgnoreCase(ENCODER_DEFLATE)) {
                outputStream = new DeflaterOutputStream(os);
            } else {
                outputStream = os;
            }
        }
    }


}