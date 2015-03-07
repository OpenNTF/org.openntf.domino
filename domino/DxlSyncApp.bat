@echo off


rem -Dnotes.binary=${domino_install} -Djava.library.path=${domino_install} -Djava.ext.dirs=${domino_install}\jvm\lib\ext\;${domino_install}\ndext; -Djava.compiler=jitc -Dnotes.init.jvm1 -Xbootclasspath/a:${domino_install}\jvm\lib\tools.jar -Xdisablejavadump -Xrs:all -Xnoagent -Xdebug

for /f "tokens=2* delims=	 " %%a in ('reg query "hklm\SOFTWARE\Lotus\Domino" /v Path') do set SERVER_DIR=%%b


%SERVER_DIR%\jvm\bin\java -cp formula\target\org.openntf.formula-1.5.0-SNAPSHOT.jar;^
core\target\org.openntf.domino-1.5.0-SNAPSHOT.jar;^
externals\tinkerpop\target\com.tinkerpop-2.6.0.qualifier.jar;^
externals\javolution\target\javolution-6.1.0.qualifier.jar;^
%SERVER_DIR%\osgi\shared\eclipse\plugins\com.ibm.commons_9.0.1.20140801-1000\lwpd.commons.jar;^
%SERVER_DIR%\osgi\rcp\eclipse\plugins\org.eclipse.osgi.services_3.1.200.v20071203.jar;^
%SERVER_DIR%\osgi\shared\eclipse\plugins\com.ibm.domino.napi_9.0.1.20140801-1000\lwpd.domino.napi.jar;^
%SERVER_DIR%\osgi\rcp\eclipse\plugins\com.ibm.icu_3.8.1.v20120530.jar -Xmx64M -Xms16M  ^
org.openntf.domino.design.impl.DxlSyncApp -db entwicklung\40fof\mis.nsf -dir D:\daten\temp\runner -export -view de.foconis.lib.view.all

