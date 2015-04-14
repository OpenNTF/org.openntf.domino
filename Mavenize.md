Why Maven without Tycho
=======================

Tycho uses a *MANIFEST-first* approach, that means all dependencies and so on are listed in the MANIFEST.MF file while Maven by default uses a *pom-first* approach.
That means it generates a MANIFEST.MF file during build from the information stored in the pom.xml-file.

Eclipse uses also the MANIFEST.MF to resolve dependencies by default, 
so Tycho is a good choice, if you develop in eclipse, because you can use a lot of wizzards and so on.

But the problem is, that you cannot mix *MANIFEST-first* and *pom-first* in the same build which leads to the next problem:<br>
All maven artifacts in the Maven-repository are *pom-first* => you can't use them.<br>

Dependency-resolving in Tycho is done in a very early stage, so that you also cannot trigger a 
download & install in the pom.xml (which is necessary for a simple CI setup)

Between commit 9116e91896fb5edf0289c03ff024198adaa6de4a and 238949a66e0911640fd30e180dd96955f85163ef the 
ODA was changed to use (pure) Maven and no longer the Maven-Tycho-plugin to build the update site.

This was not an easy task at all, as there is only a p2 repository of the IBM-XPages-libraries on OpenNTF
which has to be converted to Maven Artifacts first.

There were also some bugs and pit fails in the ODA code and also in the IBM code.

But now, we have the advantage, that a lot of things (=nearly all) are done automatically.
Maven will resolve all dependencies, so it's perfect if you have a CI-server. 
If you import the maven project into eclipse, it also finds the dependencies. No XPages SDK required.

**Note** It is recommended that you select `Name Template = [groupId].[artifactId]` in the advanced tab, 
when you import the project into eclipse.

Structural Changes
------------------
I (=Roland) tried to do the change very accurate, so that it won't break too much. 

I also try to summarize all changes I've made in the directory layout, so that other developers can follow my thoughts.

The first thing is, that we use POM's now, instead of `MANIFEST.MF`, `build.properties`, `.classpath` files.
So I've deleted ALL these files, the m2eclipse plugin will recreate these files on `ALT+F5`.<br/>
**Remember:** you should never adjust or commit one of these files. 
 
### These Files/Directories are REMOVED 

(currently moved in the "old" folder. This folder will be removed completely soon)

 * `domino\externals\guava`<br/>
  (Dependency of the Graph API, maybe useful in other places, too)<br/>
  **NOW:** `com.google.guava:guava:jar:18.0` from the Maven Repository


* `domino\externals\javassist`<br/>
  (Dependency of the Graph API/Tinkerpop-Frames)<br/>
  **NOW:** `org.javassist:javassist:jar:3.18.0-GA` from the Maven Repository

* `domino\externals\jsr305`<br/>
  (NO dependency in ODA, old? used in external projects?)<br/>
  **REMOVED COMPLETELY!**
  
* `domino\externals\rxjava`<br/>
  (NO dependency in ODA, old? used in external projects?)<br/>
  **REMOVED COMPLETELY!** 
 
* `domino\externals\tinkerpop`<br/>
  (Dependency of the Graph API)<br/>
  **NOW:** Lots of dependencies of tinkerpop can be satisfied with stock jar. 
  (tinkerpop-frames is still self-compiled, because it has a REAL HUGE dependency tree)<br/>
  [ ] TODO: Analyze if the dependency tree of tinkerpop/blueprints could be still reduced.

* `domino\core\javadoc` + `domino\xsp\doc`<br/>
  **NOW:** use `mvn site` to generate a Maven-site + javadoc 

* `domino\junit4xpages`<br/>
  **REMOVED COMPLETELY!**<br/>
  It was a 'hack' and mainly a wrapper for junit 4.11. itself.<br/>
  [ ] TODO: Add JUnit support to run tests with the maven-surefire plugin

* `domino\org.openntf.domino.feature.group` + `org.openntf.domino.updatesite`<br/>
  UpdateSite is generated in a complete different way.<br/>
  **NOW:** Updatesite is in `domino\updatesite\target\repository`<br/>
  [ ] TODO: submit patch requests of p2-maven-plugin to Author.


### Changes in `domino\core`

#### Moves and deletions:
* deleted `META-INF\MANIFEST.MF` (file has no effect)
* deleted `build.properties` (file has no effect)
* deleted `archive` folder. (old stuff)
* removed `org.openntf.domino.junit` package (moved to old-main)
* moved `plugin.xml` to `src\main\resources`
* moved all resources (xml & property files) from `src\main\java` to `src\main\resources`
* renamed the `src\test` directory to `src\old-test`, so that surefire doesn't try to execute tests.<br/>
  [ ] TODO: Change the previous tests & test framework that they will run.
* moved the `org.openntf.domino.big` , `.graph` and `.graph2` packages to the new `graph` bundle.  

#### Code Changes (that requires investigation)
* moved the `org.openntf.domino.helpers.DocumentScanner` to the `graph` bundle 
  and **renamed** the package to  `org.openntf.domino.graph2.helpers.DocumentScanner`<br/>
  **IMPORTANT:** two bundles should never contain the same package. (here: `org.openntf.domino.helpers`)
  Otherwise you get [Split Packages](http://wiki.osgi.org/wiki/Split_Packages) which should be avoided.<br/>
  [ ] TODO: Check if this change will break existing code

* Changed the MD5 computation in `org.openntf.domino.config.Configuration`.<br/>
  Now it uses DominoUtils.md5 instead of guava-hash function. This was the only place where
  guava was used in the core => Now it doesn't require guava any more
  
* Removed `DominoGraph.clearDocumentCache();` in `Factory.termThread`
  [ ] **IMPORTANT TODO:** the graph bundle must register a `terminateHook` to clear the graph-cache
  
so in total, there were only 2 LOC that have changed in the core.

### Changes in `domino\formula`

* deleted `META-INF\MANIFEST.MF` (file has no effect)
* deleted `build.properties` (file has no effect)
* changed the directory layout to `src\main\java` / `src\main\resources`

No code change was done in formula

### Changes in `domino\graph`

This is a new project. 

Files were moved from the `core` to this package. No code-change was done, 
but the package of DocumentScanner has changed. **This may break something**

### Changes in `domino\xsp`

* deleted `META-INF\MANIFEST.MF` (file has no effect)
* deleted `build.properties` (file has no effect)

No code change was done in xsp (only the import of DocumentScanner in WebinarSamples has to be changed to avoid split-packages)

### External dependencies

TODO: Tinkerpop/Javolution

### Third Party Download

TODO

Other important things you should know
--------------------------------------

TODO





Graph API Dependency:
---------------------

Here is a listing of the dependency tree.

All packages except `org.eclipse.*`, `com.ibm.*` and `org.openntf:thirdparty` are packaged
into the updatesite.
```
org.openntf.domino:xsp:bundle:1.5.0-SNAPSHOT
+- org.openntf.domino:core:jar:1.5.0-SNAPSHOT:compile
|  +- com.ibm.notes:java.api:jar:9.0.1:compile
|  +- com.ibm.commons:com.ibm.commons:jar:9.0.1:compile
|  +- org.javolution:javolution-core-java:jar:6.1.0-ODA-SNAPSHOT:compile
|  |  \- org.eclipse.osgi:services:jar:3.1.200-v20071203:compile (version selected from constraint [3.1.0,3.2.0))
|  \- org.openntf:formula:jar:1.5.0-SNAPSHOT:compile
|     \- com.ibm.icu:base:jar:3.8.1:compile
+- org.openntf.domino:graph:jar:1.5.0-SNAPSHOT:compile
|  +- com.tinkerpop.blueprints:blueprints-core:jar:2.6.0:compile
|  |  +- org.codehaus.jettison:jettison:jar:1.3.3:compile
|  |  |  \- stax:stax-api:jar:1.0.1:compile
|  |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.2.3:compile
|  |  |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.2.3:compile
|  |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.2.3:compile
|  |  +- com.carrotsearch:hppc:jar:0.6.0:compile
|  |  +- commons-configuration:commons-configuration:jar:1.6:compile
|  |  |  +- commons-collections:commons-collections:jar:3.2.1:compile
|  |  |  +- commons-lang:commons-lang:jar:2.4:compile
|  |  |  +- commons-digester:commons-digester:jar:1.8:compile
|  |  |  |  \- commons-beanutils:commons-beanutils:jar:1.7.0:compile
|  |  |  \- commons-beanutils:commons-beanutils-core:jar:1.8.0:compile
|  |  \- commons-logging:commons-logging:jar:1.1.1:compile
|  +- com.tinkerpop:frames:jar:2.6.0-ODA-SNAPSHOT:compile
|  |  +- com.tinkerpop.gremlin:gremlin-groovy:jar:2.6.0:compile
|  |  |  +- com.tinkerpop.gremlin:gremlin-java:jar:2.6.0:compile
|  |  |  +- org.apache.ivy:ivy:jar:2.3.0:compile
|  |  |  +- com.tinkerpop:pipes:jar:2.6.0:compile
|  |  |  +- org.codehaus.groovy:groovy:jar:1.8.9:compile
|  |  |  |  +- antlr:antlr:jar:2.7.7:compile
|  |  |  |  +- asm:asm:jar:3.2:compile
|  |  |  |  +- asm:asm-commons:jar:3.2:compile
|  |  |  |  +- asm:asm-util:jar:3.2:compile
|  |  |  |  +- asm:asm-analysis:jar:3.2:compile
|  |  |  |  \- asm:asm-tree:jar:3.2:compile
|  |  |  +- org.apache.ant:ant:jar:1.8.3:compile
|  |  |  |  \- org.apache.ant:ant-launcher:jar:1.8.3:compile
|  |  |  +- org.fusesource.jansi:jansi:jar:1.5:compile
|  |  |  \- jline:jline:jar:0.9.94:compile
|  |  \- org.javassist:javassist:jar:3.18.0-GA:compile
|  \- com.google.guava:guava:jar:18.0:compile
+- org.openntf.domino:java.sharedapi:jar:1.5.0-SNAPSHOT:compile
+- com.ibm.xsp:domino:jar:9.0.1:compile
|  +- com.ibm.xsp:core:jar:9.0.1:compile
|  |  \- com.ibm.designer:lib.acf:jar:9.0.1:compile
|  +- com.ibm.commons:xml:jar:9.0.1:compile
|  |  \- com.ibm.designer:lib.fastinfoset:jar:9.0.1:compile
|  +- com.ibm.commons:vfs:jar:9.0.1:compile
|  +- com.ibm.designer:runtime.directory:jar:9.0.1:compile
|  +- com.ibm.designer:lib.javamail:jar:9.0.1:compile
|  \- com.ibm.domino:xsp.adapter:jar:9.0.1:compile
+- com.ibm.xsp:extlib:jar:9.0.1:compile
|  +- com.ibm.designer:runtime.acl:jar:9.0.1:compile
|  +- com.ibm.domino:services:jar:9.0.1:compile
|  +- com.ibm.xsp:extlib.core:jar:9.0.1:compile
|  +- com.ibm.xsp:extlib.controls:jar:9.0.1:compile
|  +- com.ibm.xsp:extlib.mobile:jar:9.0.1:compile
|  +- com.ibm.xsp:extlib.oneui:jar:9.0.1:compile
|  \- com.ibm.xsp:extlib.domino:jar:9.0.1:compile
+- com.ibm.xsp:extsn:jar:9.0.1:compile
|  +- com.ibm.jscript:com.ibm.jscript:jar:9.0.1:compile
|  \- com.ibm.icu:icu4j:jar:3.8:compile
+- com.ibm.designer:runtime:jar:9.0.1:compile
+- com.ibm.domino:xsp.bootstrap:jar:9.0.1:compile
+- com.ibm.xsp:designer:jar:9.0.1:compile
+- com.ibm.domino:napi:jar:9.0.1:compile
+- org.eclipse.osgi:org.eclipse.osgi:jar:3.4.3-R34x_v20081215:compile
+- junit:junit:jar:4.11:compile
|  \- org.hamcrest:hamcrest-core:jar:1.3:compile
+- com.ibm.icu:com.ibm.icu:jar:3.8.1:compile
+- org.eclipse.core:runtime:jar:3.4.0:compile
|  +- org.eclipse.equinox:common:jar:3.6.200-v20130402-1505:compile (version selected from constraint [3.2.0,4.0.0))
|  +- org.eclipse.core:jobs:jar:3.6.0-v20140424-0053:compile (version selected from constraint [3.2.0,4.0.0))
|  +- org.eclipse.equinox:registry:jar:3.5.400-v20140428-1507:compile (version selected from constraint [3.4.0,4.0.0))
|  +- org.eclipse.equinox:preferences:jar:3.2.201-R34x_v20080709:compile (version selected from constraint [3.2.0,3.5.0))
|  +- org.eclipse.equinox:contenttype:jar:3.3.0-v20080604:compile (version selected from constraint [3.3.0,3.5.0))
|  \- org.eclipse.equinox:app:jar:1.3.200-v20130910-1609:compile (version selected from constraint [1.0.0,2.0.0))
+- com.ibm.designer:lib.jsf:jar:9.0.1:compile
|  \- com.ibm.pvc:servlet:jar:2.5.0:compile
+- com.ibm.domino:xsp.bridge.http:jar:9.0.1:compile
\- org.openntf:thirdparty:jar:1.5.0-SNAPSHOT:compile
```