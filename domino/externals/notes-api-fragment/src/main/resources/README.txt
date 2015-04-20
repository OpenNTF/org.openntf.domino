This fragment extends the system-bundle, so that the lotus.notes.* and 
lotus.domino.* packages are exported properly. This is normally the task of the
"com.ibm.notes.java.api_8.5.3" bundle, but this is broken because the bundle
claims that it exports lotus.notes.* and lotus.domino.* but it doesn't! 
(there are no classes inside nor is it a fragment for system.bundle)

It seems that the "com.ibm.notes.java.api_9.0.1" bundle contains the Notes.jar,
so this will work (but this will collide with the Notes.jar in the lib/ext 
irectory.)

A proper export is required if you want to use BND and "Import-Package" instead
of "Required-Bundle". 

If "Import-Package" is used, the bundle that exports a package MUST	contain the
package in classpath (which is not true for api-8.5.3 and leads in a 
ClassNotFound exception)

In "Required-Bundle" mode, this CNFE is caught and as fallback, the	system-
classloader is asked.
		
The curious thing is, that this works for the "lotus.domino" package.
In a long debuggin session, we (RPr, AWa) found the reason:
The LaunchRCP-class generates a dynamic fragment in
DATA-DIR/domino/workspace/.config/domino/eclipse/plugins
that exports "lotus.domino" (it looks like a hack)