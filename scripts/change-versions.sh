#!/usr/bin/env bash

# This script sets a new version of ODA.

set -e

if [[ $# -eq 0 ]]; then
	echo "Usage: $0 <new version>"
	exit 1;
fi

# Context variables
ODA_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."
NEW_VERSION=$1
# There's probably a better way to do stored boolean in shell, but eh
if [[ $NEW_VERSION == *"-SNAPSHOT" ]]; then
	IS_SNAPSHOT="true"
else
	IS_SNAPSHOT="false"
fi

echo "Using repos dir $REPOS_DIR"
echo "Target ODA version is $1"
echo "Snapshot? $IS_SNAPSHOT"

echo "//////////////////////////////////////////////////////////"
echo "// Updating ODA"
echo "//////////////////////////////////////////////////////////"

if [[ $IS_SNAPSHOT == "true" ]]; then
	mvn -f "$ODA_DIR/domino/pom.xml" tycho-versions:set-version -DnewVersion="$NEW_VERSION"
else
	# Two-step process to keep the OSGi qualifiers in place
	mvn -f "$ODA_DIR/domino/pom.xml" tycho-versions:set-version -DnewVersion="$NEW_VERSION-SNAPSHOT"
	mvn -f "$ODA_DIR/domino/pom.xml" versions:set -DnewVersion="$NEW_VERSION" -DgenerateBackupPoms=false
fi
