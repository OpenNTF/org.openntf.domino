This project sets up a dummy Domain to run JUnit Tests.


Name of the domain:	O=CI-DUMMY
Name of the Server-ID:	CN=junit/O=CI-DUMMY

Certifier Password:	openntf


How this works:
===============

1) Download binaries
This will download a binary zip file, that is specified in "domino.binary.url" property.
Note, that these binaries are property of IBM and will not be redistributed, so you have to build
the ZIP-File yourself (see below)

2) Extract the Binaries to $HOME/.m2/openntf-domino-ci/bin

3) Copy the ID-Files to $HOME/.m2/openntf-domino-ci/bin

4) Extract the resources/nsfs/*.zip to $HOME/.m2/openntf-domino-ci/data

5) Create a Notes.ini in $HOME/.m2/openntf-domino-ci/bin


Now you have a working Domino Installation in $HOME/.m2/openntf-domino-ci



How to build the ZIP-File:
==========================
You need an installed domino. And of course, you need a license to use the binaries!

1) Copy the following files and folders to a new directory:
	*.dll
	*.so
	icudt34l.dat
	*.tlb
	N\
	XmlSchemas\

2) Zip the folder, so that the files above are in the root of the ZIP. Upload the ZIP and specify the download link:

	mvn install -Ddomino.binary.url=<your-download-link>

I have no license, or I do not care about unitTests
===================================================
use the "-Dskip.tests=true" parameter. e.g.

	mvn install -Dskip.tests


