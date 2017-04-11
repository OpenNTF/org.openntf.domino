#!/usr/bin/env bash

set -e

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."

mvn -f "$BASE_DIR/domino" -Pdistribution clean package
mvn -f "$BASE_DIR/domino" deploy