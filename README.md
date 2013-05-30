org.openntf.domino
==================

Open replacement for lotus.domino package in IBM Domino

Project goals:

1. Eliminate ham-fisted Exception handling in lotus.domino API by allowing static exception delegation
2. Modernize getters/setters to use Java standard interfaces
3. Modernize collection objects to implement Iterators where appropriate
4. Implement Maps and Collections using Domino data objects (ie: Document implements Map<String, Object>)
5. Using MIME storage, allow any Serializable content to be stored in an Item
6. Correct methods with have dangerous side-effects (ie: View.isFolder() which builds the index if it didn't already exist)
7. Provide useful global convenience settings like alwaysUseJavaDates and alwaysStoreGMTTime
8. Provide useful static utility methods like incinerate(), toDxl() and toUnid(String)
9. Have some operations that currently throw Exceptions unnecessarily instead simply return null (ie: Database.getDocumentByUnid())
10. Provide coherent content assist via Javadoc annotations and retention of parameter names in byte code


OPENNTF
=======
This project is an OpenNTF project, and is available under the Apache Licence V2.0. All other aspects of the project, including contributions, defect reports, discussions, feature requests and reviews are subject to the OpenNTF Terms of Use - available at [http://openntf.org/Internal/home.nsf/dx/Terms_of_Use](http://openntf.org/Internal/home.nsf/dx/Terms_of_Use).

YOURKIT
=======
YourKit is kindly supporting OPENNTF open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
<a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and
<a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>.

We cannot recommend YourKit enough. It's a superb profiling tool with great Eclipse integration. If you're serious about Java development, you need to be using YourKit. No arguments, just do it.
