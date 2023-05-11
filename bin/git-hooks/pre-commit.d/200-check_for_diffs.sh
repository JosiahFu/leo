#!/bin/bash -x
set -e
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "Checking for code modifications."
git -C "${STAGED_FILES_TEMP_DIR}" diff --exit-code "${GIT_HASH}" "${STAGED_FILES_TEMP_DIR}"
if [ $? -ne 0 ]; then
  echo "Code modifications detected. This is probably formatting fixes or out of date .gitignore files."
  exit 1
fi
