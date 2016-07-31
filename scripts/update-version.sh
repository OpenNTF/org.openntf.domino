#!/usr/bin/env bash

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."
NEW_VERSION=$1

if [ -z "$NEW_VERSION" ]
then
	echo "Usage: $0 <new version>"
	exit 1
fi

if [[ ! $NEW_VERSION == *"-SNAPSHOT" ]]
then
	# Set the OSGi versions to "-SNAPSHOT" to retain the qualifier for OSGi
	mvn -f "$BASE_DIR/domino" tycho-versions:set-version -DnewVersion="$NEW_VERSION-SNAPSHOT"
	mvn -f "$BASE_DIR/domino" versions:set -DnewVersion="$NEW_VERSION" -DgenerateBackupPoms=false
else
	# Just set the version using Tycho
	mvn -f "$BASE_DIR/domino" tycho-versions:set-version -DnewVersion="$NEW_VERSION"
fi