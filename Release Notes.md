##Release Notes

###4.1.0 (Second 9.0.1 FP8 Release)  

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

###4.2.0 (First 9.0.1 FP9 Zircon release)

- Additional method required for FP9 (Zircon) - EmbeddedObject.getFileEncoding()

###4.2.1
- Setters for expiry on images, javascript, css and files
- setDasMode extended onto DatabaseDesign class
- Form / subform creation added to Design API
- Fixes and streamlining of methods for creating a blank database. Use a single filepath instead of folder / filename. Ability added to create on remote server.
