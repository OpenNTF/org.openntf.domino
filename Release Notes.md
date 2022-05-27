## Release Notes

### 10.0.1
- 10.0.1 support
- Fixes for name DC part (#166)
- `getVersion()` method wrapped in AccessController block (#167)
- Various cleanup
- Off-by-one issue in ViewNavigator fixed
- Nathan's work merged in
- XspOpenLogPhaseListener exception-sniffing improved
- OpenLog fixes, see https://stackoverflow.com/questions/53480994/xpages-openlog-logger-errordoc
- Fixes for `toGMTDate()` non-US date format

### 10.0.0 (First Domino V10 release)
- Fix for XOTS running as current user when triggered from REST service plugin
- Additional Xsp Property, "snap", which forces all calls on `getDocumentByKey` etc to exact match (second parameter set to `true`)
- Fix for case where s_recycle tried to recycle non-Base objects
- `BasicXotsXspCallbackRunnable` class added, which takes an `IXspHttpServletResponseCallback` and the `HttpServletRequest` as the parameters for the constructor
- Helpers to process asynchronous tasks in the background: `XspUtils.intialiseAndProcessResponseAsAsync(IXspHttpServletResponseCallback)`, which uses a `BasicXotsXspCallbackRunnable`
- NotesCalendar methods removed, as in core
- New methods for V10
	- Database.createFromTemplate
	- DateTime.getReplicaID
	- Document.isCancelSendOnMissingKey
	- Document.setCancelSendOnMissingKey
	- Registration.crossCertify
	- RichTextItem.convertToHTML
	- RichTextItem.getHTMLReferences
	- Session.applicationShouldQuit
	- View.FTSearchSorted methods overloaded with final boolean webQuerySyntax parameter

### 4.4.0 (First 9.0.1 FP10 release)
- Overloaded Database.getUserID() method
- Support for getting List and Set from items
- Logic bug fixed in TypeUtils.toPrimitive
- DOTS license file added
- pom and version number cleanup in source code

### 4.3.0 (Last planned 9.0.1 FP9 release)
- Getting OnRefreshType of Views and Folders in design API
- Callback interfaces and helper methods for doing boilerplating for extracting HttpRequest and HttpResponse, processing, and properly terminating the response from an XAgent. One is specifically designed to allow population of a JSON object which will get passed back to the REST request

<pre>
   /*
    * One method call to XspUtils.initialiseAndProcessResponse() does all the boilerplating for extracting the request, response and JsonJavaObject
    * and properly terminating the response. JsonJavaObject is basically the same as a Java Map. All gotchas are handled for you!
    *
    * The bit that could be new to most is that XspUtils.initialiseAndProcessResponseAsJson() takes an anonymous inner class as its method. This is just
    * a way to pass in a process() method that can interact with the request and response set up by XspUtils.initialiseAndProcessResponseAsJson()
    *
    * With Java 8 it becomes more readable with lambdas:
    *
    * XspUtils.initialiseAndProcessResponseAsJson((request, response, jsonObj) -> {
    *     // do stuff here
    * });
    */
</pre>

- OpenLog fix for full logging of XPages SSJS uncaught exceptions.
- Fix for IconNote creation

### 4.2.1
- Setters for expiry on images, javascript, css and files
- setDasMode extended onto DatabaseDesign class
- Form / subform creation added to Design API
- Fixes and streamlining of methods for creating a blank database. Use a single filepath instead of folder / filename. Ability added to create on remote server.

### 4.2.0 (First 9.0.1 FP9 Zircon release)

- Additional method required for FP9 (Zircon) - EmbeddedObject.getFileEncoding()

### 4.1.1
- OpenLog fix for full logging of XPages SSJS uncaught exceptions.
- Fix for IconNote creation

### 4.1.0 (Second 9.0.1 FP8 Release)  

- Additional Javadoc documentation added
- Fix for Document.containsValue()
- Fix for DocumentCollections.isEmpty()
- Massive number of Graph bug fixes
- Overhaul of Graph-based cross-NSF indexing
- Change in how Xots tasks are initiated in order to support future task stat tracking
- Addition of Actions to Graph REST API
- EMBridgeMessageQueue extensions
- Base needs variables for IDVault and UserID classes. Also needs GetCppObj(Base) method added in Nathan's branch Document _isNew visibility needed updating for Database class
- FtDomainSearch method added to Database class
- Xots getService() method visibility amended - making private is likely to break existing applications, including my own!
- REST plugin pointed to 9.0.1 minimum version, onError amended to onUnknownError (as in core ExtLib 17)
- Fix for a broken infinite loop guard in CData
- Modified TypeUtils to convert null values to string as "" instead of "null"
- Bundled Tinkerpop tweaked for Java 8 compilation
- TestRunner tweaked for Java 8 compilation
- Adding catch in XspOpenLogErrorHolder for developer passing SSJS function as "this"
- OpenLog updates from XPages OpenLog Logger M7.0
- Additional Session methods - getServerNameAsName(), getServerNameAbbreviated(), createBlankDatabase(), createBlankDatabaseAbsolutePath()
- Fix for ViewEntry.getMetaversalId()
- Fix for DominoUtils.escapeForFormulaString, needs to replace "" with "\"
- Additional development of DesignColumn class
- Additional development of DesignBase class
- Additional methods on Agent class to get whether agent is profiled, enums and methods for target and trigger, getting last run date, getting actual name and alias - getName() method doesn't work for getting Design note
- Additional development of DatabaseDesign including setters and getters for all database properties
- Started development on TemplateBuildSharedField class - not completed, because it needs to set a date
- DesignParseException
- Additional methods in Database for getting private and shared private on first use views
- Fix for getting private views
- Additional development of DesignAgent classes
- Fix for DominoEmail sending JSON when null
- Changing OpenLog to use Session.NATIVE instead of Session.SIGNER
- Additions to DesignForm - gathering subform information for DesignForms / Subforms. Making DesignForm extend Subform
- Fix for XMLNode.removeAttribute
- Additional getters / setters in IconNote for some Database properties
- Additional build tests
