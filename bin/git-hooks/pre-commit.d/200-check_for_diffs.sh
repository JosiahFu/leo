#!/bin/bash

echo "Checking for diffs."

commands () {
  set -e
  . "$(dirname "${BASH_SOURCE[0]}")/common.sh"

  git -C "${STAGED_FILES_TEMP_DIR}" diff --exit-code "${GIT_HASH}" "${STAGED_FILES_TEMP_DIR}"
}
OUTPUT="$(commands 2>&1)"

if [ $? -ne 0 ]; then
  echo "${OUTPUT}"
  echo -e
  echo "   !!! Diffs detected. !!!"
  echo -e
  echo "   This is probably due to a formatting fix or an out-of-date .gitignore file."
  echo "   Please fix the diffs above, commit, and try again."
  exit 1
fi
