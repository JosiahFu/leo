#!/bin/bash

echo "Executing 'mvn clean package'."

commands () {
  set -e
  . "$(dirname "${BASH_SOURCE[0]}")/common.sh"

  mvn --file "${STAGED_FILES_TEMP_DIR}/pom.xml" clean package
}
OUTPUT="$(commands 2>&1)"

if [ $? -ne 0 ]; then
  echo "${OUTPUT}"
  echo -e
  echo "   !!! Maven failed. !!!"
  echo -e
  echo "   This is probably due to a failing test."
  echo "   Please examine the output above, fix, commit, and try again."
  exit 1
fi
