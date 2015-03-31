How to setup test environment:
==============================


1) Configure your JVM
---------------------

Go to "Window/Preferences" then "Java/Installed JREs"

Add a new one:

	JRE Home: 
		<your_server_dir>\jvm
	JRE Name: 
		Domino Server JVM
	Default VM arguments:
		-Xmx64M -Xms16M -Dnotes.binary=${domino_install} -Djava.library.path=${domino_install} -Djava.ext.dirs=${domino_install}\jvm\lib\ext\;${domino_install}\ndext; -Djava.compiler=jitc -Dnotes.init.jvm1 -Xbootclasspath/a:${domino_install}\jvm\lib\tools.jar -Xdisablejavadump -Xrs:all -Xnoagent -Xdebug

IMPORTANT: Check if the variable "domino_install" is pointing to <your_server_dir>


2) Configure your path
----------------------
The DXL-Exporter/Importer requires some DLLs that are located in <your_server_dir> (exactly: nskn50en.dll)
Unfortunately, this DLL is searched
- in the current path of "java.exe" <your_server_dir>\jvm\bin
- in the Windows directory
- in your system path
- but NOT in <your_server_dir>

=> So copy either the missing DLLs in one of the directory above or add <your_server_dir> to your %PATH% variable.
(or say me, how to add the path to 


3) Configure your Notes.ini
---------------------------
Add:

junit_remote_untrusted_server=CN=srv-01-nprod/O=FOCONIS
junit_remote_trusted_server=CN=srv-01-ntest85/OU=srv/O=FOCONIS

(or any other server that has trusted/untrusted state) to your Notes.INI
This values are required to perform some session tests.
 
		


