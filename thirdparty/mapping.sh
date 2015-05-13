#!/bin/bash

# small script to generate the POM-File

template() {
	echo "<execution>"
	echo "	<phase>$1</phase>"
	echo "	<goals>"
	echo "		<goal>$2-file</goal>"
	echo "	</goals>"
	echo "	<id>$3</id>"
	echo "	<configuration>"
	echo "		<file>$4</file>"
	echo "		<pomFile>poms/$3.pom</pomFile>"
	echo "	</configuration>"
	echo "</execution>"
}

add() {
	template compile install $1 $2 >> install.temp
	template deploy deploy $1 $2 >> deploy.temp
}
echo. > install.temp
echo. > deploy.temp
 

add com.ibm.notes.java.api 				target/innerJars/Notes.jar
add com.ibm.notes.java.api.websvc		target/innerJars/websvc.jar
add com.ibm.domino.napi					target/innerJars/lwpd.domino.napi.jar
add org.eclipse.osgi					target/UpdateSite/plugins/org.eclipse.osgi_3.4.3.R34x_v20081215-1030-RCP20140609-1400.jar
add org.eclipse.osgi.services			target/UpdateSite/plugins/org.eclipse.osgi.services_3.1.200.v20071203.jar
add org.eclipse.core.runtime			target/UpdateSite/plugins/org.eclipse.core.runtime_3.4.0.v20080512.jar
add org.eclipse.equinox.preferences		target/UpdateSite/plugins/org.eclipse.equinox.preferences_3.2.201.R34x_v20080709-RCP20140609-1400.jar
add org.eclipse.core.contenttype		target/UpdateSite/plugins/org.eclipse.core.contenttype_3.3.0.v20080604-1400.jar
add com.ibm.commons						target/innerJars/lwpd.commons.jar
add com.ibm.commons.jdbc				target/innerJars/lwpd.commons.jdbc.jar
add com.ibm.commons.icu					target/innerJars/lwpd.commons.icu.jar
add com.ibm.commons.xml					target/innerJars/lwpd.commons.xml.jar
add com.ibm.commons.vfs					target/innerJars/lwpd.commons.vfs.jar
add com.ibm.designer.lib.fastinfoset	target/innerJars/lib/FastInfoset.jar
add	com.ibm.designer.runtime.directory	target/innerJars/lwpd.runtime.directory.jar
add com.ibm.designer.lib.jsf			target/innerJars/lib/jsf-api.jar
add com.ibm.xsp.domino					target/innerJars/lwpd.xsp.domino.jar
add com.ibm.designer.lib.javamail 		target/innerJars/lib/mail.jar
add com.ibm.xsp.extsn					target/innerJars/lwpd.xsp.extsn.jar
add com.ibm.xsp.extlib 					target/UpdateSite/plugins/com.ibm.xsp.extlib_9.0.1.v00_00_20140404-1000.jar
add com.ibm.icu.base					target/UpdateSite/plugins/com.ibm.icu.base_3.8.1.v20080530.jar
add com.ibm.icu 						target/UpdateSite/plugins/com.ibm.icu_3.8.1.v20120530.jar
add com.ibm.xsp.extlib.core 			target/UpdateSite/plugins/com.ibm.xsp.extlib.core_9.0.1.v00_00_20140404-1000.jar
add com.ibm.xsp.extlib.controls 		target/UpdateSite/plugins/com.ibm.xsp.extlib.controls_9.0.1.v00_00_20140404-1000.jar
add com.ibm.xsp.extlib.mobile			target/UpdateSite/plugins/com.ibm.xsp.extlib.mobile_9.0.1.v00_00_20140404-1000.jar 
add com.ibm.xsp.extlib.oneui 			target/UpdateSite/plugins/com.ibm.xsp.extlib.oneui_9.0.1.v00_00_20140404-1000.jar
add com.ibm.xsp.extlib.domino			target/UpdateSite/plugins/com.ibm.xsp.extlib.domino_9.0.1.v00_00_20140404-1000.jar
add com.ibm.designer.runtime			target/innerJars/lwpd.runtime.designer.jar
add com.ibm.designer.runtime.acl		target/innerJars/lwpd.runtime.acl.jar
add com.ibm.domino.services				target/UpdateSite/plugins/com.ibm.domino.services_9.0.1.v00_00_20140404-1000.jar
add com.ibm.domino.xsp.bootstrap		target/innerJars/xsp.domino.bridge.jar
add com.ibm.xsp.designer				target/innerJars/lwpd.xsp.designer.jar
add com.ibm.domino.xsp.bridge.http		target/innerJars/xsp.domino.bridge.http.jar
add com.ibm.domino.xsp.adapter			target/innerJars/lwpd.domino.adapter.jar
add com.ibm.pvc.servlet					target/UpdateSite/plugins/com.ibm.pvc.servlet_2.5.0.20140627-1700.jar
add com.ibm.designer.lib.acf			target/UpdateSite/plugins/com.ibm.designer.lib.acf_9.0.1.20140404-1000.jar
add com.ibm.xsp.core					target/innerJars/lwpd.xsp.core.jar
add com.ibm.jscript.jscript				target/innerJars/lib/lwpd.commons.ibmjs.jar
add com.ibm.jscript.jscript-lib			target/innerJars/lib/lwpd.commons.ibmjs.jar


awk '
    BEGIN       {p=1}
    /START:INSTALL/   {print;system("cat install.temp");p=0}
    /END:INSTALL/     {p=1}
    p' pom.xml > pom.xml.temp

awk '
    BEGIN       {p=1}
    /START:DEPLOY/   {print;system("cat deploy.temp");p=0}
    /END:DEPLOY/     {p=1}
    p' pom.xml.temp > pom.xml
    

