#!/bin/bash -x
set -e
. "$(dirname "${BASH_SOURCE[0]}")/common.sh"

echo "Checking for code modifications."
git -C "${CHECKED_OUT_TEST_DIR}" diff --exit-code "${GIT_HASH}" "${CHECKED_OUT_TEST_DIR}"
if [ $? -ne 0 ]; then
  echo "Code modifications detected. This is probably formatting fixes or out of date .gitignore files."
  exit 1
fi
