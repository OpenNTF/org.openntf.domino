org.openntf.domino
==================

Open replacement for lotus.domino package in IBM Domino

Project goals:
1) Eliminate ham-fisted Exception handling in lotus.domino API by allowing static exception delegation
2) Modernize getters/setters to use Java standard interfaces
3) Modernize collection objects to implement Iterators where appropriate
4) Implement Maps and Collections using Domino data objects (ie: Document implements Map<String, Object>)
5) Using MIME storage, allow any Serializable content to be stored in an Item
6) Correct methods with have dangerous side-effects (ie: View.isFolder() which builds the index if it didn't already exist)
7) Provide useful global convenience settings like alwaysUseJavaDates and alwaysStoreGMTTime
8) Provide useful static utility methods like incinerate(), toDxl() and toUnid(String)
9) Have some operations that currently throw Exceptions unnecessarily instead simply return null (ie: Database.getDocumentByUnid())