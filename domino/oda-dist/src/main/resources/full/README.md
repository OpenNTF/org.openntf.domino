OpenNTF Domino API
------------------

Current information about the OpenNTF Domino API can be found on [its GitHubPage](https://github.com/OpenNTF/org.openntf.domino) and the associated wiki, as well as on the [OpenNTF project page](http://www.openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API).

This distribution contains several components:

 * `UpdateSite.zip`: an Eclipse Update Site suitable for installation into an NSF Update Site database (after unzipping) or directly into Designer. This is the only required component to use the API.
 * `LICENSE`: the license applying to non-external components of the OpenNTF Domino API.
 * `NOTICE`: a description of licensed third-party code included in the OpenNTF Domino API.
 * `presentations`: a collection of presentation files from webinars and conference sessions relating to the API.
 * `demo`:
   * `demoDb`: a general demonstration application for the API, packaged as an on-disk project suitable for importing into Designer.
   * `emptyConferenceNSFs.zip`: a sample application demonstrating the use of the graph API to create a conference-session application.
   * `OpenNTF Hyper Search Demo.zip`: a sample application demonstrating the use of Hyper Search, packaged as an on-disk project suitable for importing into Designer.

Requirements
============

- Domino 9.0.1 (recent fix packs recommended)
- Extension Library v17 or above (for the graph REST service)