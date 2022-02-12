# OpenNTF Domino API

## Description

Open replacement for lotus.domino package in IBM Domino

Project goals:

1. Eliminate Exception handling in lotus.domino API by allowing static exception delegation and standard Java logging
2. Modernize getters/setters to use Java standard interfaces
3. Modernize collection objects to implement Iterators where appropriate
4. Implement `Map`s and `Collection`s using Domino data objects (eg: `Document implements Map<String, Object>`)
5. Using MIME storage, allow any Serializable content to be stored in an Item
6. Correct methods with have dangerous side-effects (eg: View.isFolder() which builds the index if it didn't already exist)
7. Provide useful global convenience settings like alwaysUseJavaDates and alwaysStoreGMTTime
8. Provide useful static utility methods like incinerate(), toDxl() and toUnid(String)
9. Have some operations that currently throw Exceptions unnecessarily instead simply return null (ie: Database.getDocumentByUnid())
10. Provide coherent content assist via Javadoc annotations and retention of parameter names in byte code
11. Eliminate the need to .recycle() Domino objects. Ever.
12. Greatly reduce the need for defensive coding by allowing typed data access
13. Provide helper classes and convenience methods for common Domino programming idioms (eg: Name.getGroups(), Database.getDocumentWithKey())
14. Leverage and republish high-quality core Java libraries like Google Guava and Javolution
15. Greatly extend the ability of the API to inspect and manipulate design elements
16. Provide entirely new paradigms like database schemas, graphs, tasklets, event listeners, transactional boundaries, and multi-NSF searching and collections.
17. Provide an alternative, pure Java implementation of the @Formula engine

## Development Notes

ODA is organized as a Tycho-based Maven tree and depends on a p2 update site from Domino. Historically, the IBM-provided Update Site for Build Management was the route to use, but it has not kept up with additions to the Notes.jar API. It is not currently legal to redistribute newer versions, but you can create an appropriate update site from an existing Notes or Domino installation using the [generate-domino-update-site](https://stash.openntf.org/projects/P2T/repos/generate-domino-update-site/browse) tool.

Once that is run and you have a p2 site to work with, create a property in your Maven `~/.m2/settings.xml` called `notes-platform` to point to it. For example:

    <?xml version="1.0"?>
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
        <profiles>
            <profile>
                <id>main</id>
                <properties>
                    <notes-platform>file:///Users/someuser/Documents/Java/IBM/UpdateSite</notes-platform>
                </properties>
            </profile>
        </profiles>
        <activeProfiles>
            <activeProfile>main</activeProfile>
        </activeProfiles>
    </settings>

To develop in Eclipse, your Target Platform will need to include this directory, and should also include directory entries for the two directories in "dependencies" in the repository.

### Running Automated Tests

Running the compile-time tests requires either Notes or Domino installed locally with a bitness that matches the Java runtime used to execute the Maven build. Additionally, that installation should either have an ID with no password or should be configured in User Security to allow external Notes-based applications to run without a password prompt. In the latter case, Notes must be open during the test.

Maven-run tests are configured by the presence of a `notes-program` property in your effective Maven settings pointing to the program directory of the Notes or Domino installation. For example, a minimal `.m2/settings.xml` file to run tests via the Mac Notes client in its normal installation location would be:

	<notes-program>/Applications/IBM Notes.app/Contents/MacOS</notes-program>

For Windows it would be e.g.:

	<notes-program>E:\Programs\IBM\Domino</notes-program>

You will also need to amend your Target Platform in Eclipse to add the directory location from the repository "dependencies\Misc". That folder includes junit, which it's looking for. By telling the Target Platform where to find that, the project will no longer throw an error.

### Building a Release Versions

In `domino` folder run:

	mvn versions:set -DnewVersion=4.2.0 -DgenerateBackupPoms=false

Then run:

	mvn tycho-versions:set-version -DnewVersion=4.2.0

Then run the maven command (if you don't have up-to-date FP version in C:\UpdateSite folder, this will need to be run from Eclipse):

	mvn -Pdistribution clean package

This generates Javadocs etc.

To deploy to artifactory, you need access to write to artifactory and the following block in your Maven settings (m2/settings.xml):

	<servers>
		<server>
			<id>artifactory.openntf.org</id>
			<username>YOUR OPENNTF USERNAME</username>
			<password>YOUR OPENNTF PASSWORD</password>
		</server>
	</servers>

This is a child of the settings XML object. Then run `mvn deploy` (I had to run this from Maven command line, I don't think eclipse has the right SSL settings).
Updating back to a Snapshot version

In `domino` folder run:

	mvn tycho-versions:set-version -DnewVersion=4.3.0-SNAPSHOT

Then run:

	mvn versions:set -DnewVersion=4.3.0-SNAPSHOT -DgenerateBackupPoms=false


## Acknowlegements

### OpenNTF
This project is an OpenNTF project, and is available under the Apache Licence V2.0. All other aspects of the project, including contributions, defect reports, discussions, feature requests and reviews are subject to the OpenNTF Terms of Use - available at [http://openntf.org/Internal/home.nsf/dx/Terms_of_Use](http://openntf.org/Internal/home.nsf/dx/Terms_of_Use).

### YourKit
YourKit is kindly supporting OPENNTF open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
<a href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java Profiler</a> and
<a href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET Profiler</a>.

We cannot recommend YourKit enough. It's a superb profiling tool with great Eclipse integration. If you're serious about Java development, you need to be using YourKit. No arguments, just do it.
