/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * This class provide some system functions.
 * @ibm-non-published
 */
public final class TSystem {
    
    /** Operating system is Windows NT. */
    public static final int OS_WINNT        = 0x00000001;
    /** Operating system is Windows 95. */
    public static final int OS_WIN95        = 0x00000002;
    /** Operating system is Windows 98. */
    public static final int OS_WIN98        = 0x00000004;
    /** Operating system is Solaris. */
    public static final int OS_SOLARIS      = 0x00000008;
    /** Operating system is Linux. */
    public static final int OS_LINUX        = 0x00000010;
    /** Operating system is HP-UX. */
    public static final int OS_HP           = 0x00000020;
    /** Operating system is IBM AIX. */
    public static final int OS_AIX          = 0x00000040;
    /** Operating system is SGI IRIX. */
    public static final int OS_IRIX         = 0x00000080;
    /** Operating system is Sun OS. */
    public static final int OS_SUNOS        = 0x00000100;
    /** Operating system is DEC (Digital Unix). */
    public static final int OS_DEC          = 0x00000200;
    /** Operating system is OS/2. */
    public static final int OS_OS2          = 0x00000400;
    /** Operating system is Mac. */
    public static final int OS_MAC          = 0x00000800;
    /** Operating system is Windows 2000. */
    public static final int OS_WIN2000      = 0x00001000;
    /** Operating system is Windows XP. */
    public static final int OS_WINXP        = 0x00002000;
    /** Operating system is Windows XP. */
    public static final int OS_WINVISTA     = 0x00003000;
    /** Operating system is Windows XP. */
    public static final int OS_WIN7         = 0x00003500;
    /** Operating system is AS/400. */
    public static final int OS_AS400        = 0x00004000;
    /** Operating system is OS/390. */
    public static final int OS_OS390        = 0x00008000;
    /** Operating system is Windows 2000. */
    public static final int OS_OTHERWIN     = 0x00010000;
    /** Operating system is unknown. */
    public static final int OS_OTHER        = 0x10000000;

    /** A mask for Windows platforms. */
    public static final int OS_WINDOWS_MASK = OS_WINNT | OS_WIN95 | OS_WIN98 | OS_WIN2000 | OS_WINXP | OS_OTHERWIN;
    /** A mask for Windows NT platforms. */
    public static final int OS_WINNT_MASK = OS_WINNT | OS_WIN2000 | OS_WINXP | OS_OTHERWIN;
    /** A mask for Unix platforms. */
    public static final int OS_UNIX_MASK = OS_SOLARIS | OS_LINUX | OS_HP | OS_AIX | OS_IRIX | OS_SUNOS | OS_DEC;

    /** CPU Intel x86*/
    public static final int CPU_I386        = 0x00000001;
    /** CPU is unknown*/
    public static final int CPU_OTHER       = 0x10000000;

    /**
     * Get the java vendor of the VM.
     * @return the java vendor name
     */
    public static final String getVMVendor() {
        try {
            return System.getProperty("java.vendor"); //$NON-NLS-1$
        } catch( Exception e ) {}
        return ""; //$NON-NLS-1$
    }

    /**
     * Get the operating system on which the IDE is running.
     * @return one of the <code>OS_*</code> constants (such as {@link #OS_WINNT})
     */
    public static final int getOperatingSystem () {
        if (operatingSystem == -1) {
            try {
                String osName = System.getProperty ("os.name"); //$NON-NLS-1$
                if ("Windows NT".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WINNT;
                else if ("Windows 95".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WIN95;
                else if ("Windows 98".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WIN98;
                else if ("Windows 2000".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WIN2000;
                else if ("Windows XP".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WINXP;
                else if ("Windows Vista".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WINVISTA;
                else if ("Windows 7".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_WIN7;
                else if (osName.indexOf("Windows")>=0) //$NON-NLS-1$
                    operatingSystem = OS_OTHERWIN;
                else if ("Solaris".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_SOLARIS;
                else if (osName.startsWith ("SunOS")) //$NON-NLS-1$
                    operatingSystem = OS_SOLARIS;
                else if ("Linux".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_LINUX;
                else if ("HP-UX".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_HP;
                else if ("AIX".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_AIX;
                else if ("Irix".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_IRIX;
                else if ("SunOS".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_SUNOS;
                else if ("Digital UNIX".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_DEC;
                else if ("OS/390".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_OS390; // Confirm that.
                else if ("AS/400".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_AS400; // Confirm that.
                else if ("OS/2".equals (osName)) //$NON-NLS-1$
                    operatingSystem = OS_OS2;
                else if (osName.startsWith ("Mac OS")) //$NON-NLS-1$
                    operatingSystem = OS_MAC;
                else if (osName.startsWith ("Darwin")) //$NON-NLS-1$
                    operatingSystem = OS_MAC;
                else
                    operatingSystem = OS_OTHER;
            } catch( Exception e ) {
                operatingSystem = OS_OTHER;
            }
        }
        return operatingSystem;
    }

    /**
     * Test whether the code is running on some variant of Windows.
     * @return <code>true</code> if Windows, <code>false</code> if some other manner of operating system
     */
    public static final boolean isWindows () {
        return (getOperatingSystem () & OS_WINDOWS_MASK) != 0;
    }

    /**
     * Test whether the code is running on some variant of Windows NT.
     * @return <code>true</code> if Windows NT, <code>false</code> if some other manner of operating system, even Win 95/98
     */
    public static final boolean isWindowsNT () {
        return (getOperatingSystem () & OS_WINNT_MASK) != 0;
    }

    /**
     * Test whether the code is running on Windows XP..
     * @return <code>true</code> if Windows XP, <code>false</code> if some other manner of operating system, even Win 2000/NT
     */
    public static final boolean isWindowsXP () {
        return getOperatingSystem () == OS_WINXP;
    }
    
    /**
     * Test whether the code is running on Windows Vista..
     * @return <code>true</code> if Windows Vista, <code>false</code> if some other manner of operating system, even Win 2000/NT/XP
     */
    public static final boolean isWindowsVista () {
        return getOperatingSystem () == OS_WINVISTA;
    }
    
    /**
     * Test whether the code is running on Windows 7..
     * @return <code>true</code> if Windows 7, <code>false</code> if some other manner of operating system, even Win 2000/NT/XP/Vista
     */
    public static final boolean isWindows7 () {
        return getOperatingSystem () == OS_WIN7;
    }

    /**
     * Test whether the code is running on some variant of Unix.
     * Linux is included as well as the commercial vendors.
     * @return <code>true</code> some sort of Unix, <code>false</code> if some other manner of operating system
     */
    public static final boolean isUnix () {
        return (getOperatingSystem () & OS_UNIX_MASK) != 0;
    }

    /**
     * The operating system on which the app runs
     */
    private static int operatingSystem = -1;

    /**
     * Get the CPU on which the app is running.
     * @return one of the <code>CPU_*</code> constants (such as {@link #CPU_I386})
     */
    public static final int getCPU() {
        if( cpu==-1 ) {
            try {
                String cpuName = System.getProperty ("os.arch"); //$NON-NLS-1$
                if( "i386".equals(cpuName) || "x86".equals(cpuName) ) //$NON-NLS-1$ //$NON-NLS-2$
                    cpu = CPU_I386;
                else
                    cpu = CPU_OTHER;
            } catch( Exception e ) {
                cpu = CPU_OTHER;
            }
        }
        return cpu;
    }

    /**
     * The CPU on which FlowBuilder runs
     */
    private static int cpu = -1;
    
    
    public static boolean isDevelopment() {
        return false;
    }

    
    /**
     * Load a native windows DLL from the jar file.
     */
    public static void loadWindowsDLL( String libName, int version, String resourceName ) throws IOException {
        String libPath = installWindowsDLL(libName,version,resourceName);
        System.load(libPath);
    }
    public static String installWindowsDLL( String libName, int version, String resourceName ) throws IOException {
        if( !isWindows() ) {
            //throw new IOException( String.format("Unable to load a DLL on non Windows system") ); //$NLS-TSystem.TSystem.NonWindows.Exception-1$
            throw new IOException( StringUtil.format("Cannot load a Windows DLL on a non Windows operating system") ); // $NLS-TSystem.CannotloadaWindowsDLLonanonWindow-1$
        }
        // Get the file name
        File dllFile = null;
        if( libName.indexOf("/")<=0 && libName.indexOf("\\")<=0 ) { //$NON-NLS-1$ //$NON-NLS-2$
            File tempDirectory = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
            dllFile = new File(tempDirectory,libName+"_"+version+".dll"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            // For debug only
            dllFile = new File(libName);
        }
        // If not already exist, load it from the resources
        // Also, check for the size
        if( TSystem.isDevelopment() && dllFile.exists() ) {
            dllFile.delete();
        }
        if( !dllFile.exists() ) {
            //TDiag.trace( "Creating DLL {0}", dllFile );
            InputStream dll=TSystem.class.getResourceAsStream(resourceName);
            if( dll==null ) {
                //throw new IOException( StringUtil.format("Unable to find resource: {0}", resourceName) ); //$NLS-TSystem.TSystem.ResourceNotFound.Exception-1$
                throw new IOException( StringUtil.format("Windows DLL {0} not found", resourceName) ); // $NLS-TSystem.WindowsDLL0notfound-1$
            }
            try {
                // Find the directory where to extract the DLL
                FileOutputStream dllout=new FileOutputStream(dllFile);
                try {
                    byte[] data=new byte[4096];
                    int read=dll.read(data, 0, data.length);
                    while( read>0 ) {
                        dllout.write(data, 0, read);
                        read=dll.read(data, 0, data.length);
                    }
                } finally {
                    dllout.close();
                }
            } finally {
                dll.close();
            }
        } else {
            //TDiag.trace( "DLL already loaded: {0} ", dllFile );
        }
        // And load the library
        return dllFile.getPath();
    }
    
    /**
    *
    */
   public static final int getJavaVersion() {
       if( javaVersion<0 ) {
           try {
               javaVersion=0;
               String[] v = StringUtil.splitString(System.getProperty("java.version"),'.',true);//NORES //$NON-NLS-1$
               if( v.length>=1 ) {
                   int major  = parseInt(v[0]);
                   int minor  = v.length>=2 ? parseInt(v[1]) : 0;
                   int minor2 = v.length>=3 ? parseInt(v[2]) : 0;
                   javaVersion = major*100 + minor*10 + minor2;
               }
           } catch( Exception e ) {
               // If we cannot get the version for any reason,
               // simply assume java2
               javaVersion = 120;
           }
       }
       return javaVersion;
   }
   private static final int parseInt( String s ) {
       // Some VM return something like 1.3.0_02
       // We just get the first digit to build the number
       return s.length()>0 ? s.charAt(0)-'0' : 0;
   }
   private static int javaVersion = -1;
   public static final boolean isJava2() {
       return getJavaVersion()>=120;
   }

    
}
